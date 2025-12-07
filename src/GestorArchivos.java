import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de archivos para persistencia de datos.
 * Maneja la serialización y deserialización de datos en formato JSON y CSV.
 */
public class GestorArchivos {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    /**
     * Guarda el historial en formato JSON
     * @param conversiones Lista de conversiones a guardar
     * @param rutaArchivo Ruta del archivo donde guardar
     */
    public static void guardarHistorialJSON(List<Conversion> conversiones, String rutaArchivo) {
        try {
            // Crear directorio si no existe
            Files.createDirectories(Paths.get(rutaArchivo).getParent());
            
            // Serializar a JSON
            String json = gson.toJson(conversiones);
            
            // Escribir archivo
            try (FileWriter writer = new FileWriter(rutaArchivo)) {
                writer.write(json);
            }
            
        } catch (IOException e) {
            System.err.println("⚠️  Error al guardar historial: " + e.getMessage());
        }
    }

    /**
     * Carga el historial desde archivo JSON
     * @param rutaArchivo Ruta del archivo a cargar
     * @return Lista de conversiones cargadas
     */
    public static List<Conversion> cargarHistorialJSON(String rutaArchivo) {
        try {
            if (!Files.exists(Paths.get(rutaArchivo))) {
                return new ArrayList<>();
            }

            String json = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
            Type tipoLista = new TypeToken<ArrayList<Conversion>>(){}.getType();
            List<Conversion> conversiones = gson.fromJson(json, tipoLista);
            
            return conversiones != null ? conversiones : new ArrayList<>();
            
        } catch (IOException e) {
            System.err.println("⚠️  Error al cargar historial: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Exporta el historial a formato CSV
     * @param conversiones Lista de conversiones a exportar
     * @param rutaArchivo Ruta del archivo CSV de salida
     */
    public static void exportarCSV(List<Conversion> conversiones, String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            // Encabezados
            writer.println("ID,Fecha,Hora,Moneda Origen,Monto Origen,Moneda Destino,Monto Destino,Tasa de Cambio");
            
            // Datos
            for (Conversion c : conversiones) {
                writer.printf("%s,%s,%s,%s,%.2f,%s,%.2f,%.6f%n",
                    c.getId(),
                    c.getTimestamp().toLocalDate(),
                    c.getTimestamp().toLocalTime(),
                    c.getMonedaOrigen(),
                    c.getMontoOrigen(),
                    c.getMonedaDestino(),
                    c.getMontoDestino(),
                    c.getTasaCambio()
                );
            }
            
            System.out.println("✅ Historial exportado a: " + rutaArchivo);
            
        } catch (IOException e) {
            System.err.println("❌ Error al exportar CSV: " + e.getMessage());
        }
    }
}

/**
 * Adaptador personalizado para serializar/deserializar LocalDateTime con Gson
 */
class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(formatter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return LocalDateTime.parse(in.nextString(), formatter);
    }
}
