package uady.mx.nube.dto;

public class PagoDTO {

    private Integer idPago;
    private Double monto;
    private String cuentaOrigen;
    private String cuentaDestino;
    private String fechaRegistro;
    private String fechaProcesa;
    private String estado;

    public PagoDTO(Integer idPago, Double monto, String cuentaOrigen, String cuentaDestino, String fechaRegistro,
            String fechaProcesa, String estado) {
        this.idPago = idPago;
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.fechaRegistro = fechaRegistro;
        this.fechaProcesa = fechaProcesa;
        this.estado = estado;
    }

    public PagoDTO() {
    }

    public Integer getIdPago() {
        return this.idPago;
    }

    public Double getMonto() {
        return this.monto;
    }

    public String getCuentaOrigen() {
        return this.cuentaOrigen;
    }

    public String getCuentaDestino() {
        return this.cuentaDestino;
    }

    public String getFechaRegistro() {
        return this.fechaRegistro;
    }

    public String getFechaProcesa() {
        return this.fechaProcesa;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setCuentaOrigen(String cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setFechaProcesa(String fechaProcesa) {
        this.fechaProcesa = fechaProcesa;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}