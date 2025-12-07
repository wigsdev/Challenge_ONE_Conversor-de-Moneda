import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el historial de conversiones de moneda.
 * Utiliza persistencia en JSON para mantener el historial entre sesiones.
 */
public class HistorialConversiones {
    private List<Conversion> conversiones;
    private final String rutaArchivo;
    private final boolean autoGuardar;

    /**
     * Constructor que carga el historial desde archivo
     */
    public HistorialConversiones() {
        this.rutaArchivo = GestorConfiguracion.obtener("historial.file.path");
        this.autoGuardar = GestorConfiguracion.obtenerBoolean("historial.auto.save");
        this.conversiones = GestorArchivos.cargarHistorialJSON(rutaArchivo);
        
        if (conversiones.size() > 0) {
            System.out.println("📂 Historial cargado: " + conversiones.size() + " conversiones");
        }
    }

    /**
     * Agrega una nueva conversión al historial
     */
    public void agregarConversion(String monedaOrigen, String monedaDestino, 
                                 double montoOrigen, double montoDestino) {
        Conversion conversion = new Conversion(monedaOrigen, monedaDestino, montoOrigen, montoDestino);
        conversiones.add(conversion);
        
        if (autoGuardar) {
            guardar();
        }
    }

    /**
     * Guarda el historial en archivo JSON
     */
    public void guardar() {
        GestorArchivos.guardarHistorialJSON(conversiones, rutaArchivo);
    }

    /**
     * Exporta el historial a formato CSV
     */
    public void exportarCSV(String rutaArchivo) {
        GestorArchivos.exportarCSV(conversiones, rutaArchivo);
    }

    /**
     * Limpia todo el historial
     */
    public void limpiar() {
        conversiones.clear();
        guardar();
        System.out.println("🗑️  Historial limpiado");
    }

    /**
     * Muestra el historial de conversiones en formato tabular
     */
    public void mostrarHistorial() {
        if (conversiones.isEmpty()) {
            System.out.println("📭 El historial está vacío");
            return;
        }

        System.out.println("\n📊 Historial de Conversiones:");
        System.out.println("═".repeat(100));
        System.out.printf("%-25s %-15s %-15s %-15s %-15s%n", 
            "Fecha y Hora", "De", "Monto", "A", "Resultado");
        System.out.println("─".repeat(100));
        
        for (Conversion c : conversiones) {
            System.out.printf("%-25s %-15s %15.2f %-15s %15.2f%n",
                c.getTimestamp().toString().replace("T", " ").substring(0, 19),
                c.getMonedaOrigen(),
                c.getMontoOrigen(),
                c.getMonedaDestino(),
                c.getMontoDestino()
            );
        }
        System.out.println("═".repeat(100));
        System.out.println("Total de conversiones: " + conversiones.size());
    }

    /**
     * Obtiene el tamaño del historial
     */
    public int getTamaño() {
        return conversiones.size();
    }
}
