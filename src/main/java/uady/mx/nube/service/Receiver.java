package uady.mx.nube.service;

import java.util.Date;
import java.util.Optional;

import com.google.gson.Gson;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import uady.mx.nube.dto.PagoDTO;
import uady.mx.nube.dto.RegistroDTO;
import uady.mx.nube.enums.EstadoEnum;
import uady.mx.nube.model.Cuenta;
import uady.mx.nube.model.Pago;
import uady.mx.nube.repository.CuentaRepository;
import uady.mx.nube.repository.PagoRepository;
import uady.mx.nube.util.Util;

@Service
public class Receiver {

  @Autowired
  CuentaRepository cr;

  @Autowired
  PagoRepository pr;

  @Autowired
  private RabbitTemplate template;

  // private Logger logger = LogManager.getLogger(this.getClass());

  @RabbitListener(queues = "${sample.rabbitmq.queue}")
  public void receiveMessage(String pago) throws AmqpRejectAndDontRequeueException {
    System.out.println("=====");
    System.out.println("PAGO RECIBIDO: " + pago);

    Gson g = new Gson();
    PagoDTO payment = g.fromJson(pago, PagoDTO.class);

    try {
      this.processPayment(payment);
    } catch (Exception e) {
      e.printStackTrace();
      throw new AmqpRejectAndDontRequeueException("Fallo");
    }

  }

  @RabbitListener(queues = "DLQ_queue")
  public void recieveDQL(String pago) {
    System.out.println("=====");
    System.out.println("DQL RECIBIDO: " + pago);
  }

  @Async
  public void processPayment(PagoDTO payment) throws Exception {

    Optional<Pago> pay = pr.findById(payment.getIdPago());

    if (!pay.isPresent()) {
      throw new NotFoundException("El pago no ha sido registrado");
    }

    Pago pago = pay.get();

    pago.setFechaProcesa(new Date());

    Cuenta cuentaOrigen = pago.getCuentaOrigen();
    Cuenta cuentaDestino = pago.getCuentaDestino();

    // Si no existe la cuenta que envía el pago (Fallo)
    if (cuentaOrigen == null) {
      pago.setEstado(EstadoEnum.ANULADO);
      pr.save(pago);
      throw new NotFoundException("El número origen de cuenta no existe");
      // SEND Dead Letter Queue
    }

    // Si no existe la cuenta que enrecibe el pago (Fallo)
    if (cuentaDestino == null) {
      pago.setEstado(EstadoEnum.ANULADO);
      pr.save(pago);
      throw new NotFoundException("El número de cuenta no existe");
      // SEND Dead Letter Queue
    }

    // Si no hay suficientes fondos en la cuenta de quien envía (Fallo)
    if (cuentaOrigen.getBalance() < pago.getMonto()) {
      pago.setEstado(EstadoEnum.NO_AUTORIZADO);
      pr.save(pago);
      throw new Exception("Los fondos no son suficientes");
      // SEND Dead Letter Queue
    }

    /**
     * Si existen las dos cuentas y se tiene suficiente saldo para transferir se
     * hace la transferencia.
     **/

    // Se resta el monto del balance de quien transfiere
    cuentaOrigen.setBalance(cuentaOrigen.getBalance() - pago.getMonto());
    cr.save(cuentaOrigen);

    // Se aumenta el monto en el balance de quien recibe
    cuentaDestino.setBalance(cuentaDestino.getBalance() + pago.getMonto());
    cr.save(cuentaDestino);

    pago.setEstado(EstadoEnum.PROCESADO);
    Date fechaProcesa = new Date();
    pago.setFechaProcesa(fechaProcesa);
    // Se almacena el pago
    pr.save(pago);

    Gson g = new Gson();
    System.out.println("=====");
    System.out.println("PAGO SATISFACTORIO: " + g.toJson(pago));

  }
}