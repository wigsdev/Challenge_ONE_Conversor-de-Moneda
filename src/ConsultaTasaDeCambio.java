import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Clase para consultar tasas de cambio desde la API de ExchangeRate.
 * Utiliza configuración externa para mayor seguridad.
 */
public class ConsultaTasaDeCambio {
    private final String apiKey;
    private final String urlBase;
    private final int timeout;
    private final HttpClient client;

    /**
     * Constructor que inicializa la configuración desde GestorConfiguracion
     */
    public ConsultaTasaDeCambio() {
        this.apiKey = GestorConfiguracion.obtenerApiKey();
        this.urlBase = GestorConfiguracion.obtenerUrlApi();
        this.timeout = GestorConfiguracion.obtenerInt("api.timeout");
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeout))
                .build();
    }

    /**
     * Obtiene las tasas de cambio desde la API
     * @return JSON con las tasas de cambio
     * @throws RuntimeException si hay error en la consulta
     */
    public String obtenerTasaDeCambio() {
        String urlCompleta = urlBase + apiKey + "/latest/USD";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompleta))
                .timeout(Duration.ofMillis(timeout))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("Error en la respuesta de la API. Código: " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al consultar la API: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la URL completa de la API (para compatibilidad)
     * @return URL de la API
     */
    public String getUrlApi() {
        return urlBase + apiKey + "/latest/USD";
    }
}
