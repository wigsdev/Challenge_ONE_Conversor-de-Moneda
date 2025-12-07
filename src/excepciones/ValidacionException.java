package excepciones;

/**
 * Excepción para errores de validación de entrada del usuario.
 * Se lanza cuando los datos ingresados no cumplen con los requisitos.
 */
public class ValidacionException extends ConversorException {
    
    /**
     * Constructor con mensaje de error
     * @param mensaje Descripción del error de validación
     */
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
