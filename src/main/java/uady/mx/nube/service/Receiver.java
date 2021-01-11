package uady.mx.nube.service;

import java.sql.Date;
import java.util.Optional;

import com.google.gson.Gson;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import uady.mx.nube.model.Cuenta;
import uady.mx.nube.model.Pago;
import uady.mx.nube.repository.CuentaRepository;
import uady.mx.nube.repository.PagoRepository;

@Service
public class Receiver {

  @Autowired
  CuentaRepository cr;

  @Autowired
  PagoRepository pr;

  @RabbitListener(queues = "${sample.rabbitmq.queue}")
  public void recievedMessage(String payment) throws Exception {
    System.out.println("Recieved Message: " + payment);

    // String replace = payment.replace("'", "\"");
    // System.out.println("Reemplazado: " + replace);

    // Gson gson = new Gson();
    // Pago pp = gson.fromJson(payment, Pago.class);

    Pago pago = new Pago();

    pago.setCuentaDestino(2);
    pago.setCuentaOrigen(1);
    pago.setEstado("new");
    pago.setFechaProcesa(new Date(System.currentTimeMillis()));
    pago.setFechaRegistro(new Date(System.currentTimeMillis()));
    pago.setMonto(500.0);

    // Cuando se reciba un mensaje del Queue
    int idOrigen = pago.getCuentaOrigen();
    int idDestino = pago.getCuentaDestino();
    Optional<Cuenta> cuentaOrigen = cr.findById(idOrigen);
    Optional<Cuenta> cuentaDestino = cr.findById(idDestino);

    // Si no existe la cuenta que envía el pago (Fallo)
    if (!cuentaOrigen.isPresent()) {
      throw new NotFoundException("El número origen de cuenta no existe");
      // SEND Dead Letter Queue
    }

    // Si no existe la cuenta que enrecibe el pago (Fallo)
    if (!cuentaDestino.isPresent()) {
      throw new NotFoundException("El número de cuenta no existe");
      // SEND Dead Letter Queue
    }

    Cuenta origen = cuentaOrigen.get();
    Cuenta destino = cuentaDestino.get();

    // Si no hay suficientes fondos en la cuenta de quien envía (Fallo)
    if (origen.getBalance() < pago.getMonto()) {
      throw new Exception("Los fondos no son suficientes");
      // SEND Dead Letter Queue
    }

    /**
     * Si existen las dos cuentas y se tiene suficiente saldo para transferir se
     * hace la transferencia.
     **/

    // Se resta el monto del balance de quien transfiere
    origen.setBalance(origen.getBalance() - pago.getMonto());
    cr.save(origen);

    // Se aumenta el monto en el balance de quien recibe
    destino.setBalance(destino.getBalance() + pago.getMonto());
    cr.save(destino);

    // Se almacena el pago
    pr.save(pago);

  }
}