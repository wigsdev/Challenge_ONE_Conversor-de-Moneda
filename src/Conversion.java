import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modelo de datos para una conversión de moneda.
 * Representa una conversión individual con todos sus detalles.
 */
public class Conversion {
    private String id;
    private LocalDateTime timestamp;
    private String monedaOrigen;
    private String monedaDestino;
    private double montoOrigen;
    private double montoDestino;
    private double tasaCambio;

    /**
     * Constructor para crear una nueva conversión
     */
    public Conversion(String monedaOrigen, String monedaDestino, 
                     double montoOrigen, double montoDestino) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.montoOrigen = montoOrigen;
        this.montoDestino = montoDestino;
        this.tasaCambio = montoDestino / montoOrigen;
    }

    // Getters
    public String getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMonedaOrigen() { return monedaOrigen; }
    public String getMonedaDestino() { return monedaDestino; }
    public double getMontoOrigen() { return montoOrigen; }
    public double getMontoDestino() { return montoDestino; }
    public double getTasaCambio() { return tasaCambio; }

    @Override
    public String toString() {
        return String.format("%s: %.2f %s => %.2f %s (tasa: %.6f)",
            timestamp.toString().replace("T", " "),
            montoOrigen, monedaOrigen,
            montoDestino, monedaDestino,
            tasaCambio);
    }
}
