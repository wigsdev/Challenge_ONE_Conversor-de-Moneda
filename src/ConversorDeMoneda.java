import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import excepciones.ApiException;
import excepciones.ConversionException;

public class ConversorDeMoneda {
    private ConsultaTasaDeCambio consultaTasaDeCambio;
    private HistorialConversiones historial;

    public ConversorDeMoneda() {
        consultaTasaDeCambio = new ConsultaTasaDeCambio();
        historial = new HistorialConversiones();
    }

    /**
     * Convierte un monto de una moneda a otra
     * @param monedaOrigen Código de la moneda origen (ej: USD)
     * @param monedaDestino Código de la moneda destino (ej: ARS)
     * @param monto Monto a convertir
     * @return Monto convertido en la moneda destino
     * @throws ConversionException si hay error en la conversión
     */
    public double convertir(String monedaOrigen, String monedaDestino, double monto) 
            throws ConversionException {
        try {
            // Obtener la tasa de cambio en formato JSON
            String respuestaJson = consultaTasaDeCambio.obtenerTasaDeCambio();

            // Parsear la respuesta JSON para obtener la tasa de cambio
            JsonObject jsonObject = JsonParser.parseString(respuestaJson).getAsJsonObject();
            JsonObject tasasDeCambio = jsonObject.getAsJsonObject("conversion_rates");

            // Validar que las monedas existan
            if (!tasasDeCambio.has(monedaOrigen)) {
                throw new ConversionException(
                    "❌ Moneda de origen no soportada: " + monedaOrigen + 
                    "\n   Verifica el código de la moneda (debe ser de 3 letras, ej: USD, EUR, ARS)"
                );
            }

            if (!tasasDeCambio.has(monedaDestino)) {
                throw new ConversionException(
                    "❌ Moneda de destino no soportada: " + monedaDestino + 
                    "\n   Verifica el código de la moneda (debe ser de 3 letras, ej: USD, EUR, ARS)"
                );
            }

            // Obtener la tasa de cambio de las monedas de interés
            double tasaOrigen = tasasDeCambio.get(monedaOrigen).getAsDouble();
            double tasaDestino = tasasDeCambio.get(monedaDestino).getAsDouble();

            // Calcular la cantidad en la moneda de destino
            double montoEnDestino = (monto / tasaOrigen) * tasaDestino;

            // Agregar la conversión al historial
            historial.agregarConversion(monedaOrigen, monedaDestino, monto, montoEnDestino);
            return montoEnDestino; // Retornar el monto convertido
            
        } catch (ApiException e) {
            throw new ConversionException(
                "❌ Error al obtener tasas de cambio: " + e.getMessage(), 
                e
            );
        } catch (Exception e) {
            throw new ConversionException(
                "❌ Error inesperado en la conversión: " + e.getMessage(), 
                e
            );
        }
    }

    public HistorialConversiones getHistorial() {
        return historial;
    }
}
