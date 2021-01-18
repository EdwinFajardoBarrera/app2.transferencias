package uady.mx.nube.dto;

public class RegistroDTO {

    private String message;
    private String fechaProcesa;
    private String estado;

    public RegistroDTO(String message, String fechaProcesa, String estado) {
        this.message = message;
        this.fechaProcesa = fechaProcesa;
        this.estado = estado;
    }

    public RegistroDTO() {
    }

    public String getMessage() {
        return this.message;
    }

    public String getFechaProcesa() {
        return this.fechaProcesa;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFechaProcesa(String fechaProcesa) {
        this.fechaProcesa = fechaProcesa;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
