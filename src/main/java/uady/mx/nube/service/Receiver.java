package uady.mx.nube.service;

import java.util.Date;
import java.util.Optional;

import com.google.gson.Gson;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import uady.mx.nube.dto.PagoDTO;
import uady.mx.nube.enums.EstadoEnum;
import uady.mx.nube.model.Cuenta;
import uady.mx.nube.model.Pago;
import uady.mx.nube.repository.CuentaRepository;
import uady.mx.nube.repository.PagoRepository;


@Service
public class Receiver {

  @Value("${sample.rabbitmq.exchange}")
  String exchange;
  @Value("${sample.rabbitmq.routingkey}")
  String routingkey;

  private static final Integer MAX_ATTEMPTS = 3;
  
  @Autowired
  CuentaRepository cr;

  @Autowired
  PagoRepository pr;

  @Autowired
  private AmqpTemplate rabbitTemplate;

  @Autowired
  private PagoService pagoService;


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
      throw new AmqpRejectAndDontRequeueException(e.getMessage());
    }

  }

  @RabbitListener(queues = "DLQ_queue")
  public void recieveDQL(String pago) {

    System.out.println("=====");
    System.out.println("DQL RECIBIDO: " + pago);

    Gson g = new Gson();
    PagoDTO payment = g.fromJson(pago, PagoDTO.class);

    Integer retry = payment.getRetry() + 1;
    payment.setRetry(retry);

    System.out.println("PAYMENT RECIBIDO: " + payment.getRetry());
    
    
    if (payment.getRetry() <= MAX_ATTEMPTS){
      String pay = g.toJson(payment);
      rabbitTemplate.convertAndSend(exchange, routingkey,pay);

    } else{
      System.out.println("Se excedio: " + pago);

      Pago fPayment = pagoService.getPago(payment.getIdPago());

      if (fPayment != null){
        fPayment.setEstado(EstadoEnum.NO_AUTORIZADO);
        Date fechaProcesa = new Date();
        fPayment.setFechaProcesa(fechaProcesa);
        pr.save(fPayment);
      }

    }
  }

  @Async
  public void processPayment(PagoDTO payment) throws Exception {

    Optional<Pago> pay = pr.findById(payment.getIdPago());
    Cuenta cuentaD = cr.findByNoCuenta(payment.getCuentaDestino());

    if (!pay.isPresent()) {
      throw new NotFoundException("El pago no ha sido registrado");
    }

    Pago pago = pay.get();

    pago.setFechaProcesa(new Date());

    Cuenta cuentaOrigen = pago.getCuentaOrigen();
    //Cuenta cuentaDestino = payment.getCuentaDestino();

    // Si no existe la cuenta que envía el pago (Fallo)
    if (cuentaOrigen == null) {
      throw new NotFoundException("El número origen de cuenta no existe");
      // SEND Dead Letter Queue
    }

    // Si no existe la cuenta que enrecibe el pago (Fallo)
    if (cuentaD == null) {

      throw new NotFoundException("El número de cuenta no existe");
      // SEND Dead Letter Queue
    }
    pago.setCuentaDestino(cuentaD);
    pr.save(pago);

    // Si no hay suficientes fondos en la cuenta de quien envía (Fallo)
    if (cuentaOrigen.getBalance() < pago.getMonto()) {
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
    cuentaD.setBalance(cuentaD.getBalance() + pago.getMonto());
    cr.save(cuentaD);

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