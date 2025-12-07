import excepciones.ApiException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Clase para consultar tasas de cambio desde la API de ExchangeRate.
 * Utiliza configuración externa para mayor seguridad.
 * Implementa reintentos automáticos con backoff exponencial.
 */
public class ConsultaTasaDeCambio {
    private final String apiKey;
    private final String urlBase;
    private final int timeout;
    private final int maxReintentos;
    private final HttpClient client;

    /**
     * Constructor que inicializa la configuración desde GestorConfiguracion
     */
    public ConsultaTasaDeCambio() {
        this.apiKey = GestorConfiguracion.obtenerApiKey();
        this.urlBase = GestorConfiguracion.obtenerUrlApi();
        this.timeout = GestorConfiguracion.obtenerInt("api.timeout");
        this.maxReintentos = GestorConfiguracion.obtenerInt("api.max.retries");
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeout))
                .build();
    }

    /**
     * Obtiene las tasas de cambio desde la API con reintentos automáticos
     * @return JSON con las tasas de cambio
     * @throws ApiException si hay error en la consulta después de todos los reintentos
     */
    public String obtenerTasaDeCambio() throws ApiException {
        String urlCompleta = urlBase + apiKey + "/latest/USD";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompleta))
                .timeout(Duration.ofMillis(timeout))
                .GET()
                .build();

        int intentos = 0;
        ApiException ultimaExcepcion = null;

        while (intentos < maxReintentos) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return response.body();
                } else if (response.statusCode() == 401) {
                    throw new ApiException("❌ API key inválida. Verifica tu configuración.", 401);
                } else if (response.statusCode() >= 500) {
                    ultimaExcepcion = new ApiException(
                        "⚠️  Servidor no disponible (código " + response.statusCode() + ")", 
                        response.statusCode()
                    );
                } else {
                    throw new ApiException(
                        "❌ Error en la API (código " + response.statusCode() + ")", 
                        response.statusCode()
                    );
                }

            } catch (IOException | InterruptedException e) {
                ultimaExcepcion = new ApiException(
                    "⚠️  Error de conexión: " + e.getMessage(), 
                    e
                );
            }

            intentos++;
            
            // Si no es el último intento y el error es recuperable, esperar antes de reintentar
            if (intentos < maxReintentos && (ultimaExcepcion == null || ultimaExcepcion.esRecuperable())) {
                try {
                    long tiempoEspera = (long) (1000 * Math.pow(2, intentos - 1)); // Backoff exponencial
                    System.out.println("🔄 Reintentando en " + tiempoEspera + "ms... (intento " + intentos + "/" + maxReintentos + ")");
                    Thread.sleep(tiempoEspera);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ApiException("❌ Reintentos interrumpidos", ie);
                }
            } else if (!ultimaExcepcion.esRecuperable()) {
                // Si el error no es recuperable, lanzar inmediatamente
                throw ultimaExcepcion;
            }
        }

        // Si llegamos aquí, se agotaron los reintentos
        throw new ApiException(
            "❌ No se pudo conectar a la API después de " + maxReintentos + " intentos", 
            ultimaExcepcion
        );
    }

    /**
     * Obtiene la URL completa de la API (para compatibilidad)
     * @return URL de la API
     */
    public String getUrlApi() {
        return urlBase + apiKey + "/latest/USD";
    }
}
