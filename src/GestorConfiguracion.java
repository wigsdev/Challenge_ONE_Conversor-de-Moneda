import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gestor de configuración de la aplicación.
 * Lee la configuración desde archivo config.properties o variables de entorno.
 */
public class GestorConfiguracion {
    private static Properties propiedades;
    private static final String ARCHIVO_CONFIG = "config.properties";

    static {
        cargarConfiguracion();
    }

    /**
     * Carga la configuración desde el archivo config.properties
     */
    private static void cargarConfiguracion() {
        propiedades = new Properties();
        
        try (InputStream input = new FileInputStream(ARCHIVO_CONFIG)) {
            propiedades.load(input);
            System.out.println("✅ Configuración cargada desde " + ARCHIVO_CONFIG);
        } catch (IOException e) {
            System.err.println("⚠️  No se pudo cargar " + ARCHIVO_CONFIG + ": " + e.getMessage());
            System.err.println("🔍 Intentando cargar desde variables de entorno...");
            cargarDesdeVariablesEntorno();
        }
    }

    /**
     * Carga la configuración desde variables de entorno como fallback
     */
    private static void cargarDesdeVariablesEntorno() {
        String apiKey = System.getenv("EXCHANGE_RATE_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            propiedades.setProperty("api.key", apiKey);
            propiedades.setProperty("api.url", "https://v6.exchangerate-api.com/v6/");
            propiedades.setProperty("api.timeout", "5000");
            propiedades.setProperty("api.max.retries", "3");
            propiedades.setProperty("cache.enabled", "true");
            propiedades.setProperty("cache.expiration.minutes", "60");
            propiedades.setProperty("historial.auto.save", "true");
            propiedades.setProperty("historial.file.path", "data/historial.json");
            System.out.println("✅ Configuración cargada desde variable de entorno");
        } else {
            throw new RuntimeException(
                "❌ No se encontró configuración válida.\n" +
                "   Por favor:\n" +
                "   1. Crea un archivo config.properties (usa config.properties.example como plantilla), o\n" +
                "   2. Define la variable de entorno EXCHANGE_RATE_API_KEY"
            );
        }
    }

    /**
     * Obtiene un valor de configuración como String
     * @param clave La clave de configuración
     * @return El valor de configuración
     */
    public static String obtener(String clave) {
        return propiedades.getProperty(clave);
    }

    /**
     * Obtiene un valor de configuración como int
     * @param clave La clave de configuración
     * @return El valor de configuración como entero
     */
    public static int obtenerInt(String clave) {
        return Integer.parseInt(propiedades.getProperty(clave));
    }

    /**
     * Obtiene un valor de configuración como boolean
     * @param clave La clave de configuración
     * @return El valor de configuración como booleano
     */
    public static boolean obtenerBoolean(String clave) {
        return Boolean.parseBoolean(propiedades.getProperty(clave));
    }

    /**
     * Obtiene la API key validando que esté configurada
     * @return La API key
     * @throws RuntimeException si la API key no está configurada
     */
    public static String obtenerApiKey() {
        String apiKey = obtener("api.key");
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("TU_API_KEY_AQUI")) {
            throw new RuntimeException(
                "❌ API key no configurada correctamente.\n" +
                "   Edita el archivo config.properties y reemplaza 'TU_API_KEY_AQUI' con tu API key real.\n" +
                "   Obtén tu API key en: https://www.exchangerate-api.com/"
            );
        }
        return apiKey;
    }

    /**
     * Obtiene la URL base de la API
     * @return La URL de la API
     */
    public static String obtenerUrlApi() {
        return obtener("api.url");
    }
}
