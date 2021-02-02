package uady.mx.nube.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import uady.mx.nube.enums.EstadoEnum;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "monto")
    private BigDecimal monto;

    @ManyToOne
    @JoinColumn(name = "cuenta_origen", referencedColumnName = "id")
    private Cuenta cuentaOrigen;

    @ManyToOne
    @JoinColumn(name = "cuenta_destino", referencedColumnName = "id")
    private Cuenta cuentaDestino;

    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @Column(name = "fecha_procesa")
    private Date fechaProcesa;

    @Column
    @Enumerated(EnumType.STRING)
    private EstadoEnum estado;

    public Pago(BigDecimal monto, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
    }

    public Pago() {
    }

    public Integer getId() {
        return this.id;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public Cuenta getCuentaOrigen() {
        return this.cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return this.cuentaDestino;
    }

    public Date getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Date getFechaProcesa() {
        return this.fechaProcesa;
    }

    public EstadoEnum getEstado() {
        return this.estado;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setFechaProcesa(Date fechaProcesa) {
        this.fechaProcesa = fechaProcesa;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "{" + " monto ='" + getMonto() + "'" + " cuentaOrigen ='" + getCuentaOrigen() + "'" + " cuentaDestino ='"
                + getCuentaDestino() + "'" + " fechaRegistro ='" + getFechaRegistro() + "'" + " fechaProcesamiento ='"
                + getFechaProcesa() + "}";
    }

}
