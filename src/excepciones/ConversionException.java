package excepciones;

/**
 * Excepción para errores durante el proceso de conversión de monedas.
 * Se lanza cuando hay problemas con las monedas, tasas o cálculos.
 */
public class ConversionException extends ConversorException {
    
    /**
     * Constructor con mensaje de error
     * @param mensaje Descripción del error
     */
    public ConversionException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error
     */
    public ConversionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
