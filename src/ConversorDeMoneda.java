import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConversorDeMoneda {
    private ConsultaTasaDeCambio consultaTasaDeCambio;
    private HistorialConversiones historial;

    public ConversorDeMoneda() {
        consultaTasaDeCambio = new ConsultaTasaDeCambio();
        historial = new HistorialConversiones();
    }

    public double convertir(String monedaOrigen, String monedaDestino, double monto) {
        try {
            // Obtener la tasa de cambio en formato JSON
            String respuestaJson = consultaTasaDeCambio.obtenerTasaDeCambio();

            // Parsear la respuesta JSON para obtener la tasa de cambio
            JsonObject jsonObject = JsonParser.parseString(respuestaJson).getAsJsonObject();
            JsonObject tasasDeCambio = jsonObject.getAsJsonObject("conversion_rates");

            // Obtener la tasa de cambio de las monedas de interés
            double tasaOrigen = tasasDeCambio.get(monedaOrigen).getAsDouble();
            double tasaDestino = tasasDeCambio.get(monedaDestino).getAsDouble();

            // Calcular la cantidad en la moneda de destino
            double montoEnDestino = (monto / tasaOrigen) * tasaDestino;

            // Agregar la conversión al historial
            historial.agregarConversion(monedaOrigen, monedaDestino, monto, montoEnDestino);
            return montoEnDestino; // Retornar el monto convertido
        } catch (NullPointerException e) {
            throw new RuntimeException("Error en la conversión de monedas. Verifique las monedas ingresadas.", e);
        }
    }

    public HistorialConversiones getHistorial() {
        return historial;
    }
}
