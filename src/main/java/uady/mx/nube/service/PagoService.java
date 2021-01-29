package uady.mx.nube.service;

import org.springframework.beans.factory.annotation.Autowired;

import uady.mx.nube.repository.PagoRepository;

public class PagoService {

    @Autowired
    PagoRepository pr;

    public void makePayment(){
        //TODO verify accounts
        // Verify Balance
        //DLQ
    }
}
