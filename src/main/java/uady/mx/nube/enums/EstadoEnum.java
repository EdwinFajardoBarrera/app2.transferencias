package uady.mx.nube.enums;

public enum EstadoEnum {
    /**
     * Indica que el proceso ha iniciado pero no ha sido procesado.
     */
    PENDIENTE,
    /**
     * Indica que el proceso ha sido procesado.
     */
    PROCESADO,
    /**
     * Indica que el proceso fue anulado.
     */
    ANULADO,
    /**
     * Indica que el proceso no fue autorizado.
     */
    NO_AUTORIZADO;
    
}