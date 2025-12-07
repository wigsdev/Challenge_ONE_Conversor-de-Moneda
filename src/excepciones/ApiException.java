package excepciones;

/**
 * Excepción para errores relacionados con la API de tasas de cambio.
 * Se lanza cuando hay problemas de conexión, autenticación o respuesta de la API.
 */
public class ApiException extends ConversorException {
    private final int codigoEstado;

    /**
     * Constructor con mensaje y código de estado HTTP
     * @param mensaje Descripción del error
     * @param codigoEstado Código de estado HTTP de la respuesta
     */
    public ApiException(String mensaje, int codigoEstado) {
        super(mensaje);
        this.codigoEstado = codigoEstado;
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción que causó este error
     */
    public ApiException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoEstado = 0;
    }

    /**
     * Obtiene el código de estado HTTP
     * @return Código de estado HTTP, 0 si no aplica
     */
    public int getCodigoEstado() {
        return codigoEstado;
    }

    /**
     * Verifica si el error es recuperable (puede reintentar)
     * @return true si el error es temporal y se puede reintentar
     */
    public boolean esRecuperable() {
        // Errores 5xx del servidor son recuperables
        return codigoEstado >= 500 && codigoEstado < 600;
    }
}
