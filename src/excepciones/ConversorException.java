package excepciones;

/**
 * Excepción base abstracta para todas las excepciones del conversor.
 * Proporciona una jerarquía de excepciones personalizada para mejor manejo de errores.
 */
public abstract class ConversorException extends Exception {
    
    /**
     * Constructor con mensaje de error
     * @param mensaje Descripción del error
     */
    public ConversorException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error
     */
    public ConversorException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
