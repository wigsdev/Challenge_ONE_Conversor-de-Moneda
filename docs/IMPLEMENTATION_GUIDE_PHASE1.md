# Guía de Implementación - Fase 1: Fundamentos

Esta guía proporciona instrucciones paso a paso para implementar las mejoras críticas de la Fase 1 del roadmap v2.0.

---

## 📋 Tabla de Contenidos

1. [Externalizar API Key](#1-externalizar-api-key)
2. [Validación de Entrada Robusta](#2-validación-de-entrada-robusta)
3. [Persistencia del Historial](#3-persistencia-del-historial)
4. [Manejo de Excepciones Mejorado](#4-manejo-de-excepciones-mejorado)

---

## 1. Externalizar API Key

### 🎯 Objetivo
Eliminar la API key hardcodeada del código fuente para mejorar la seguridad.

### 📝 Pasos de Implementación

#### Paso 1.1: Crear archivo de configuración

Crear `config.properties` en la raíz del proyecto:

```properties
# Configuración de API
api.key=TU_API_KEY_AQUI
api.url=https://v6.exchangerate-api.com/v6/
api.timeout=5000
api.max.retries=3

# Configuración de caché
cache.enabled=true
cache.expiration.minutes=60

# Configuración de historial
historial.auto.save=true
historial.file.path=data/historial.json
```

#### Paso 1.2: Crear plantilla de configuración

Crear `config.properties.example`:

```properties
# Configuración de API
# Obtén tu API key en: https://www.exchangerate-api.com/
api.key=YOUR_API_KEY_HERE
api.url=https://v6.exchangerate-api.com/v6/
api.timeout=5000
api.max.retries=3

# Configuración de caché
cache.enabled=true
cache.expiration.minutes=60

# Configuración de historial
historial.auto.save=true
historial.file.path=data/historial.json
```

#### Paso 1.3: Actualizar .gitignore

Agregar al `.gitignore`:

```gitignore
# Archivos de configuración con datos sensibles
config.properties

# Datos generados
data/
logs/
```

#### Paso 1.4: Crear clase GestorConfiguracion

Crear `src/GestorConfiguracion.java`:

```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GestorConfiguracion {
    private static Properties propiedades;
    private static final String ARCHIVO_CONFIG = "config.properties";

    static {
        cargarConfiguracion();
    }

    private static void cargarConfiguracion() {
        propiedades = new Properties();
        
        try (InputStream input = new FileInputStream(ARCHIVO_CONFIG)) {
            propiedades.load(input);
        } catch (IOException e) {
            System.err.println("Error al cargar configuración: " + e.getMessage());
            System.err.println("Intentando cargar desde variables de entorno...");
            cargarDesdeVariablesEntorno();
        }
    }

    private static void cargarDesdeVariablesEntorno() {
        String apiKey = System.getenv("EXCHANGE_RATE_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            propiedades.setProperty("api.key", apiKey);
            propiedades.setProperty("api.url", "https://v6.exchangerate-api.com/v6/");
        } else {
            throw new RuntimeException(
                "No se encontró configuración. " +
                "Crea un archivo config.properties o define la variable de entorno EXCHANGE_RATE_API_KEY"
            );
        }
    }

    public static String obtener(String clave) {
        return propiedades.getProperty(clave);
    }

    public static int obtenerInt(String clave) {
        return Integer.parseInt(propiedades.getProperty(clave));
    }

    public static boolean obtenerBoolean(String clave) {
        return Boolean.parseBoolean(propiedades.getProperty(clave));
    }

    public static String obtenerApiKey() {
        String apiKey = obtener("api.key");
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_API_KEY_HERE")) {
            throw new RuntimeException("API key no configurada. Revisa config.properties");
        }
        return apiKey;
    }

    public static String obtenerUrlApi() {
        return obtener("api.url");
    }
}
```

#### Paso 1.5: Modificar ConsultaTasaDeCambio

Actualizar `src/ConsultaTasaDeCambio.java`:

```java
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConsultaTasaDeCambio {
    private final String apiKey;
    private final String urlBase;
    private final int timeout;
    private final HttpClient client;

    public ConsultaTasaDeCambio() {
        this.apiKey = GestorConfiguracion.obtenerApiKey();
        this.urlBase = GestorConfiguracion.obtenerUrlApi();
        this.timeout = GestorConfiguracion.obtenerInt("api.timeout");
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeout))
                .build();
    }

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

    public String getUrlApi() {
        return urlBase + apiKey + "/latest/USD";
    }
}
```

#### Paso 1.6: Actualizar ConversorDeMoneda

Modificar la llamada en `src/ConversorDeMoneda.java`:

```java
// Cambiar de:
String respuestaJson = consultaTasaDeCambio.obtenerTasaDeCambio(consultaTasaDeCambio.getUrlApi());

// A:
String respuestaJson = consultaTasaDeCambio.obtenerTasaDeCambio();
```

### ✅ Verificación

- [ ] `config.properties` no está en el repositorio
- [ ] `config.properties.example` está en el repositorio
- [ ] La aplicación funciona con configuración externa
- [ ] Mensaje de error claro si falta la API key
- [ ] README actualizado con instrucciones de configuración

---

## 2. Validación de Entrada Robusta

### 🎯 Objetivo
Prevenir crashes por entradas inválidas del usuario.

### 📝 Pasos de Implementación

#### Paso 2.1: Crear clase ValidadorEntrada

Crear `src/ValidadorEntrada.java`:

```java
import java.util.InputMismatchException;
import java.util.Scanner;

public class ValidadorEntrada {
    private static final int MAX_INTENTOS = 3;

    /**
     * Lee una opción del menú validando el rango
     */
    public static int leerOpcion(Scanner scanner, int min, int max) {
        int intentos = 0;
        
        while (intentos < MAX_INTENTOS) {
            try {
                System.out.print(">>> ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                
                if (opcion >= min && opcion <= max) {
                    return opcion;
                }
                
                System.out.println("⚠️  Opción fuera de rango. Ingrese un valor entre " + min + " y " + max);
                intentos++;
                
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Debe ingresar un número.");
                scanner.nextLine(); // Limpiar buffer
                intentos++;
            }
        }
        
        System.out.println("❌ Máximo de intentos alcanzado. Saliendo...");
        return max; // Retornar opción de salir
    }

    /**
     * Lee un monto validando que sea positivo
     */
    public static double leerMonto(Scanner scanner) {
        int intentos = 0;
        
        while (intentos < MAX_INTENTOS) {
            try {
                System.out.print("Ingrese el monto: ");
                double monto = scanner.nextDouble();
                scanner.nextLine(); // Limpiar buffer
                
                if (monto > 0) {
                    return monto;
                }
                
                System.out.println("⚠️  El monto debe ser mayor a cero.");
                intentos++;
                
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Debe ingresar un número válido.");
                scanner.nextLine(); // Limpiar buffer
                intentos++;
            }
        }
        
        throw new RuntimeException("No se pudo obtener un monto válido después de " + MAX_INTENTOS + " intentos.");
    }

    /**
     * Lee un código de moneda validando el formato
     */
    public static String leerCodigoMoneda(Scanner scanner, String mensaje) {
        int intentos = 0;
        
        while (intentos < MAX_INTENTOS) {
            System.out.print(mensaje);
            String codigo = scanner.nextLine().trim().toUpperCase();
            
            if (codigo.matches("[A-Z]{3}")) {
                return codigo;
            }
            
            System.out.println("⚠️  Código inválido. Debe ser de 3 letras (ej: USD, ARS, BRL)");
            intentos++;
        }
        
        throw new RuntimeException("No se pudo obtener un código de moneda válido.");
    }

    /**
     * Confirma una acción con el usuario
     */
    public static boolean confirmar(Scanner scanner, String mensaje) {
        System.out.print(mensaje + " (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        return respuesta.equals("S") || respuesta.equals("SI") || respuesta.equals("Y") || respuesta.equals("YES");
    }
}
```

#### Paso 2.2: Actualizar Principal.java

Modificar `src/Principal.java` para usar el validador:

```java
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConversorDeMoneda conversor = new ConversorDeMoneda();

        int opcion;

        do {
            mostrarMenu();
            opcion = ValidadorEntrada.leerOpcion(scanner, 1, 8);

            if (opcion == 8) {
                System.out.println("👋 Saliendo del conversor. ¡Hasta luego!");
                break;
            }

            if (opcion == 7) {
                conversor.getHistorial().mostrarHistorial();
                continue;
            }

            // Procesar conversión
            procesarConversion(scanner, conversor, opcion);

        } while (true);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n*****************************************************");
        System.out.println("¡Sea bienvenido/a al conversor de Moneda =]");
        System.out.println("");
        System.out.println("1) Dólar =>> Peso argentino");
        System.out.println("2) Peso argentino =>> Dólar");
        System.out.println("3) Dólar =>> Real brasileño");
        System.out.println("4) Real brasileño =>> Dólar");
        System.out.println("5) Dólar =>> Sol peruano");
        System.out.println("6) Sol peruano =>> Dólar");
        System.out.println("7) Mostrar historial de conversiones");
        System.out.println("8) Salir");
        System.out.println("Elija una opción válida:");
        System.out.println("*****************************************************");
    }

    private static void procesarConversion(Scanner scanner, ConversorDeMoneda conversor, int opcion) {
        String monedaOrigen, monedaDestino, nombreOrigen, nombreDestino;

        switch (opcion) {
            case 1:
                monedaOrigen = "USD"; monedaDestino = "ARS";
                nombreOrigen = "Dólares"; nombreDestino = "Pesos argentinos";
                break;
            case 2:
                monedaOrigen = "ARS"; monedaDestino = "USD";
                nombreOrigen = "Pesos argentinos"; nombreDestino = "Dólares";
                break;
            case 3:
                monedaOrigen = "USD"; monedaDestino = "BRL";
                nombreOrigen = "Dólares"; nombreDestino = "Reales brasileños";
                break;
            case 4:
                monedaOrigen = "BRL"; monedaDestino = "USD";
                nombreOrigen = "Reales brasileños"; nombreDestino = "Dólares";
                break;
            case 5:
                monedaOrigen = "USD"; monedaDestino = "PEN";
                nombreOrigen = "Dólares"; nombreDestino = "Soles peruanos";
                break;
            case 6:
                monedaOrigen = "PEN"; monedaDestino = "USD";
                nombreOrigen = "Soles peruanos"; nombreDestino = "Dólares";
                break;
            default:
                System.out.println("❌ Opción inválida.");
                return;
        }

        try {
            double monto = ValidadorEntrada.leerMonto(scanner);
            double resultado = conversor.convertir(monedaOrigen, monedaDestino, monto);
            System.out.printf("✅ %.2f %s son equivalentes a %.2f %s.%n", 
                monto, nombreOrigen, resultado, nombreDestino);
        } catch (Exception e) {
            System.out.println("❌ Error en la conversión: " + e.getMessage());
        }
    }
}
```

### ✅ Verificación

- [ ] No hay crashes con entradas de texto
- [ ] Validación de rangos funciona correctamente
- [ ] Mensajes de error son claros
- [ ] Límite de reintentos funciona
- [ ] Buffer del scanner se limpia correctamente

---

## 3. Persistencia del Historial

### 🎯 Objetivo
Guardar el historial de conversiones en formato JSON para que persista entre sesiones.

### 📝 Pasos de Implementación

#### Paso 3.1: Crear modelo de datos

Crear `src/Conversion.java`:

```java
import java.time.LocalDateTime;

public class Conversion {
    private String id;
    private LocalDateTime timestamp;
    private String monedaOrigen;
    private String monedaDestino;
    private double montoOrigen;
    private double montoDestino;
    private double tasaCambio;

    public Conversion(String monedaOrigen, String monedaDestino, 
                     double montoOrigen, double montoDestino) {
        this.id = java.util.UUID.randomUUID().toString();
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
}
```

#### Paso 3.2: Crear GestorArchivos

Crear `src/GestorArchivos.java`:

```java
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Guarda el historial en formato JSON
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
            System.err.println("Error al guardar historial: " + e.getMessage());
        }
    }

    /**
     * Carga el historial desde archivo JSON
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
            System.err.println("Error al cargar historial: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Exporta el historial a formato CSV
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
            System.err.println("Error al exportar CSV: " + e.getMessage());
        }
    }
}
```

#### Paso 3.3: Actualizar HistorialConversiones

Modificar `src/HistorialConversiones.java`:

```java
import java.util.ArrayList;
import java.util.List;

public class HistorialConversiones {
    private List<Conversion> conversiones;
    private final String rutaArchivo;
    private final boolean autoGuardar;

    public HistorialConversiones() {
        this.rutaArchivo = GestorConfiguracion.obtener("historial.file.path");
        this.autoGuardar = GestorConfiguracion.obtenerBoolean("historial.auto.save");
        this.conversiones = GestorArchivos.cargarHistorialJSON(rutaArchivo);
        
        System.out.println("📂 Historial cargado: " + conversiones.size() + " conversiones");
    }

    public void agregarConversion(String monedaOrigen, String monedaDestino, 
                                 double montoOrigen, double montoDestino) {
        Conversion conversion = new Conversion(monedaOrigen, monedaDestino, montoOrigen, montoDestino);
        conversiones.add(conversion);
        
        if (autoGuardar) {
            guardar();
        }
    }

    public void guardar() {
        GestorArchivos.guardarHistorialJSON(conversiones, rutaArchivo);
    }

    public void exportarCSV(String rutaArchivo) {
        GestorArchivos.exportarCSV(conversiones, rutaArchivo);
    }

    public void limpiar() {
        conversiones.clear();
        guardar();
        System.out.println("🗑️  Historial limpiado");
    }

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
                c.getTimestamp().toString().replace("T", " "),
                c.getMonedaOrigen(),
                c.getMontoOrigen(),
                c.getMonedaDestino(),
                c.getMontoDestino()
            );
        }
        System.out.println("═".repeat(100));
        System.out.println("Total de conversiones: " + conversiones.size());
    }

    public int getTamaño() {
        return conversiones.size();
    }
}
```

### ✅ Verificación

- [ ] Historial se guarda automáticamente
- [ ] Historial se carga al iniciar
- [ ] Formato JSON es válido
- [ ] Exportación a CSV funciona
- [ ] Directorio `data/` se crea automáticamente

---

## 4. Manejo de Excepciones Mejorado

### 🎯 Objetivo
Implementar una jerarquía de excepciones personalizadas y mejorar el manejo de errores.

### 📝 Pasos de Implementación

#### Paso 4.1: Crear jerarquía de excepciones

Crear directorio `src/excepciones/` y las siguientes clases:

**ConversorException.java**:
```java
package excepciones;

public abstract class ConversorException extends Exception {
    public ConversorException(String mensaje) {
        super(mensaje);
    }

    public ConversorException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
```

**ApiException.java**:
```java
package excepciones;

public class ApiException extends ConversorException {
    private final int codigoEstado;

    public ApiException(String mensaje, int codigoEstado) {
        super(mensaje);
        this.codigoEstado = codigoEstado;
    }

    public ApiException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoEstado = 0;
    }

    public int getCodigoEstado() {
        return codigoEstado;
    }
}
```

**ConversionException.java**:
```java
package excepciones;

public class ConversionException extends ConversorException {
    public ConversionException(String mensaje) {
        super(mensaje);
    }

    public ConversionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
```

**ValidacionException.java**:
```java
package excepciones;

public class ValidacionException extends ConversorException {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
```

#### Paso 4.2: Actualizar ConsultaTasaDeCambio

```java
import excepciones.ApiException;
// ... otros imports

public class ConsultaTasaDeCambio {
    // ... código existente

    public String obtenerTasaDeCambio() throws ApiException {
        String urlCompleta = urlBase + apiKey + "/latest/USD";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompleta))
                .timeout(Duration.ofMillis(timeout))
                .GET()
                .build();

        int intentos = 0;
        int maxReintentos = GestorConfiguracion.obtenerInt("api.max.retries");

        while (intentos < maxReintentos) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return response.body();
                } else if (response.statusCode() == 401) {
                    throw new ApiException("API key inválida", 401);
                } else if (response.statusCode() >= 500) {
                    throw new ApiException("Servidor no disponible", response.statusCode());
                } else {
                    throw new ApiException("Error en la API", response.statusCode());
                }

            } catch (IOException | InterruptedException e) {
                intentos++;
                if (intentos >= maxReintentos) {
                    throw new ApiException("Error de conexión después de " + maxReintentos + " intentos", e);
                }
                
                try {
                    Thread.sleep(1000 * intentos); // Backoff exponencial
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw new ApiException("No se pudo conectar a la API", 0);
    }
}
```

#### Paso 4.3: Actualizar ConversorDeMoneda

```java
import excepciones.ConversionException;
// ... otros imports

public class ConversorDeMoneda {
    // ... código existente

    public double convertir(String monedaOrigen, String monedaDestino, double monto) 
            throws ConversionException {
        try {
            String respuestaJson = consultaTasaDeCambio.obtenerTasaDeCambio();

            JsonObject jsonObject = JsonParser.parseString(respuestaJson).getAsJsonObject();
            JsonObject tasasDeCambio = jsonObject.getAsJsonObject("conversion_rates");

            if (!tasasDeCambio.has(monedaOrigen)) {
                throw new ConversionException("Moneda de origen no soportada: " + monedaOrigen);
            }

            if (!tasasDeCambio.has(monedaDestino)) {
                throw new ConversionException("Moneda de destino no soportada: " + monedaDestino);
            }

            double tasaOrigen = tasasDeCambio.get(monedaOrigen).getAsDouble();
            double tasaDestino = tasasDeCambio.get(monedaDestino).getAsDouble();

            double montoEnDestino = (monto / tasaOrigen) * tasaDestino;

            historial.agregarConversion(monedaOrigen, monedaDestino, monto, montoEnDestino);
            return montoEnDestino;

        } catch (ApiException e) {
            throw new ConversionException("Error al obtener tasas de cambio: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ConversionException("Error inesperado en la conversión", e);
        }
    }
}
```

### ✅ Verificación

- [ ] Excepciones personalizadas funcionan
- [ ] Mensajes de error son descriptivos
- [ ] Reintentos automáticos funcionan
- [ ] Backoff exponencial implementado
- [ ] Validación de monedas soportadas

---

## 📊 Checklist de Fase 1

- [ ] 1.1 Externalizar API Key
  - [ ] Crear config.properties
  - [ ] Crear config.properties.example
  - [ ] Actualizar .gitignore
  - [ ] Crear GestorConfiguracion
  - [ ] Modificar ConsultaTasaDeCambio
  - [ ] Actualizar README

- [ ] 1.2 Validación de Entrada
  - [ ] Crear ValidadorEntrada
  - [ ] Actualizar Principal
  - [ ] Probar con entradas inválidas
  - [ ] Verificar mensajes de error

- [ ] 1.3 Persistencia del Historial
  - [ ] Crear modelo Conversion
  - [ ] Crear GestorArchivos
  - [ ] Actualizar HistorialConversiones
  - [ ] Probar guardado/carga
  - [ ] Probar exportación CSV

- [ ] 1.4 Manejo de Excepciones
  - [ ] Crear jerarquía de excepciones
  - [ ] Actualizar ConsultaTasaDeCambio
  - [ ] Actualizar ConversorDeMoneda
  - [ ] Implementar reintentos
  - [ ] Probar diferentes escenarios de error

---

**Última Actualización**: 2025-12-06  
**Versión del Documento**: 1.0
