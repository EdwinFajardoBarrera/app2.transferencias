package uady.mx.nube.model;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "Cuentas")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "no_cuenta")
    private String noCuenta;

    @Column(name = "balance")
    private BigDecimal balance;

    public Cuenta(String noCuenta, BigDecimal balance) {
        this.noCuenta = noCuenta;
        this.balance = balance;
    }

    public Cuenta() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoCuenta() {
        return noCuenta;
    }

    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "{" + " número de cuenta ='" + getNoCuenta() + "'" + " balance ='" + getBalance() + "'" + " idCuenta='"
                + getId() + "}";
    }
}
