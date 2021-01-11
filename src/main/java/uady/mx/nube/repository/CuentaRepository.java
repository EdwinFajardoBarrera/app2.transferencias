package uady.mx.nube.repository;

// import fmat.aplicaciones.nube.model.Cuenta;
import uady.mx.nube.model.Cuenta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {
  @Query("SELECT c from Cuenta c WHERE c.noCuenta=?1")
  public Cuenta findByNoCuenta(String noCuenta);
}
