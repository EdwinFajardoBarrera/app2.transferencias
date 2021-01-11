package uady.mx.nube.service;

import java.util.Date;
import java.util.Optional;

import com.google.gson.Gson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Component;
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

  private Logger logger = LogManager.getLogger(this.getClass());

  @RabbitListener(queues = "${sample.rabbitmq.queue}")
  public String receiveMessage(String pago) {
    logger.info("Mensaje Recibido");
    Gson g = new Gson();
    PagoDTO payment = g.fromJson(pago, PagoDTO.class);
    RegistroDTO registro = new RegistroDTO();

    String fechaProcesamiento = Util.getFormattedDate(new Date());
    String estado = EstadoEnum.PROCESADO.name();
    String message = "El pago ha sido recibido con exito";

    registro.setEstado(estado);
    registro.setFechaProcesa(fechaProcesamiento);
    registro.setMessage(message);

    try {
      this.processPayment(payment);
    } catch (Exception e) {
      e.printStackTrace();
    }

    String response = g.toJson(registro);
    
    return response;
  }

  @Async
  public void processPayment(PagoDTO payment) throws Exception {
    
    Optional<Pago> pay = pr.findById(payment.getIdPago());

    if (!pay.isPresent()){
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
    // Se almacena el pago
    pr.save(pago);
  }
}