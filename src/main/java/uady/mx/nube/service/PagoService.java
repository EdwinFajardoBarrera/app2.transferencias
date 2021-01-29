package uady.mx.nube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import uady.mx.nube.dto.PagoDTO;
import uady.mx.nube.enums.EstadoEnum;
import uady.mx.nube.model.Cuenta;
import uady.mx.nube.model.Pago;
import uady.mx.nube.repository.CuentaRepository;
import uady.mx.nube.repository.PagoRepository;

import java.util.Date;
import java.util.Optional;

import com.google.gson.Gson;


@Service
public class PagoService {

    @Autowired
    PagoRepository pr;

    @Autowired
    CuentaRepository cr;

    
    public Pago getPago(int id) {
        Optional<Pago> pay = pr.findById(id);

        if (!pay.isPresent()) {
            return null;
        }
        Pago pago = pay.get();

        return pago;
    }



}
