package uady.mx.nube;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import uady.mx.nube.dto.PagoDTO;
import uady.mx.nube.dto.RegistroDTO;
import uady.mx.nube.enums.EstadoEnum;
import uady.mx.nube.util.Util;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);
  private Logger logger = LogManager.getLogger(this.getClass());

  @RabbitListener(queues = "sample.queue")
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

    String response = g.toJson(registro);
    
    return response;
  }

  public CountDownLatch getLatch() {
    return latch;
  }
}