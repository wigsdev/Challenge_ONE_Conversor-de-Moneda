# Guía de Arquitectura - Conversor de Moneda v2.0

## 📐 Visión General de la Arquitectura

Este documento describe la arquitectura actual y propuesta del Conversor de Moneda, siguiendo principios de diseño orientado a objetos y patrones de arquitectura limpia.

---

## 🏛️ Arquitectura Actual (v1.0)

### Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────┐
│                      Principal.java                      │
│                    (Interfaz de Usuario)                 │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                 ConversorDeMoneda.java                   │
│              (Lógica de Conversión)                      │
└────────┬─────────────────────────────┬──────────────────┘
         │                             │
         ▼                             ▼
┌──────────────────────┐    ┌─────────────────────────────┐
│ ConsultaTasaDeCambio │    │  HistorialConversiones      │
│   (Cliente API)      │    │  (Gestión de Historial)     │
└──────────┬───────────┘    └─────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────────────────────────┐
│              ExchangeRate-API (Externo)                  │
└─────────────────────────────────────────────────────────┘
```

### Responsabilidades por Capa

#### Capa de Presentación
- **Principal.java**: Maneja la interacción con el usuario vía consola

#### Capa de Lógica de Negocio
- **ConversorDeMoneda.java**: Coordina la conversión entre monedas
- **HistorialConversiones.java**: Gestiona el registro de conversiones

#### Capa de Acceso a Datos
- **ConsultaTasaDeCambio.java**: Interactúa con la API externa

---

## 🎯 Arquitectura Propuesta (v2.0)

### Principios de Diseño

1. **Separación de Responsabilidades**: Cada clase tiene una única responsabilidad bien definida
2. **Inversión de Dependencias**: Las capas superiores no dependen de implementaciones concretas
3. **Abierto/Cerrado**: Extensible sin modificar código existente
4. **Inyección de Dependencias**: Facilita testing y mantenibilidad

### Diagrama de Arquitectura Completa

```
┌─────────────────────────────────────────────────────────────────┐
│                      CAPA DE PRESENTACIÓN                        │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────────┐              ┌──────────────────────┐     │
│  │  Principal.java  │              │  GUI (JavaFX)        │     │
│  │  (Consola)       │              │  - VentanaPrincipal  │     │
│  └────────┬─────────┘              │  - PanelConversion   │     │
│           │                        │  - PanelHistorial    │     │
│           │                        └──────────┬───────────┘     │
└───────────┼───────────────────────────────────┼─────────────────┘
            │                                   │
            └───────────────┬───────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    CAPA DE LÓGICA DE NEGOCIO                     │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────────────────────────┐   │
│  │              ConversorDeMoneda.java                      │   │
│  │         (Coordinador de Conversiones)                    │   │
│  └────┬──────────────────────────────────────────┬─────────┘   │
│       │                                          │              │
│       ▼                                          ▼              │
│  ┌─────────────────┐  ┌──────────────────┐  ┌─────────────┐   │
│  │ ValidadorEntrada│  │ CatalogoMonedas  │  │ CacheTasas  │   │
│  └─────────────────┘  └──────────────────┘  └─────────────┘   │
└─────────────────────────────────────────────────────────────────┘
            │                                   │
            ▼                                   ▼
┌─────────────────────────────────────────────────────────────────┐
│                  CAPA DE PERSISTENCIA                            │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────────────┐         ┌──────────────────────┐     │
│  │ HistorialConversiones│         │   GestorArchivos     │     │
│  │  (Modelo de Datos)   │         │   (I/O Manager)      │     │
│  └──────────────────────┘         └──────────────────────┘     │
└─────────────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────────────┐
│                  CAPA DE SERVICIOS EXTERNOS                      │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────────────────────────┐   │
│  │           ConsultaTasaDeCambio.java                      │   │
│  │              (Cliente API REST)                          │   │
│  └────────────────────────┬─────────────────────────────────┘   │
└───────────────────────────┼─────────────────────────────────────┘
                            ▼
                ┌───────────────────────┐
                │  ExchangeRate-API     │
                │  (Servicio Externo)   │
                └───────────────────────┘
```

---

## 🔧 Componentes Detallados

### Nuevos Componentes en v2.0

#### 1. ValidadorEntrada
**Responsabilidad**: Validar todas las entradas del usuario

```java
public class ValidadorEntrada {
    public static int leerOpcion(Scanner scanner, int min, int max);
    public static double leerMonto(Scanner scanner);
    public static String leerCodigoMoneda(Scanner scanner);
}
```

#### 2. GestorArchivos
**Responsabilidad**: Manejo centralizado de I/O

```java
public class GestorArchivos {
    public void guardarHistorial(HistorialConversiones historial, String ruta);
    public HistorialConversiones cargarHistorial(String ruta);
    public void exportarCSV(HistorialConversiones historial, String ruta);
}
```

#### 3. CacheTasas
**Responsabilidad**: Cachear tasas de cambio para optimizar rendimiento

```java
public class CacheTasas {
    private Map<String, TasaCambio> cache;
    private long tiempoExpiracion;
    
    public TasaCambio obtenerTasa(String moneda);
    public void actualizarCache(Map<String, Double> tasas);
    public boolean estaVigente();
}
```

#### 4. CatalogoMonedas
**Responsabilidad**: Gestionar información de monedas disponibles

```java
public class CatalogoMonedas {
    private Map<String, Moneda> monedas;
    
    public List<Moneda> obtenerTodasLasMonedas();
    public Moneda buscarPorCodigo(String codigo);
    public List<Moneda> buscarPorNombre(String nombre);
}
```

#### 5. Jerarquía de Excepciones

```
ConversorException (abstract)
    ├── ApiException
    │   ├── ApiNoDisponibleException
    │   └── ApiKeyInvalidaException
    ├── ConversionException
    │   ├── MonedaNoSoportadaException
    │   └── MontoInvalidoException
    └── ValidacionException
        ├── EntradaInvalidaException
        └── FormatoInvalidoException
```

---

## 📦 Patrones de Diseño Aplicados

### 1. Singleton
**Aplicado en**: `CacheTasas`, `CatalogoMonedas`

**Justificación**: Solo debe existir una instancia del caché y del catálogo de monedas en toda la aplicación.

```java
public class CacheTasas {
    private static CacheTasas instancia;
    
    private CacheTasas() {}
    
    public static CacheTasas obtenerInstancia() {
        if (instancia == null) {
            instancia = new CacheTasas();
        }
        return instancia;
    }
}
```

### 2. Strategy
**Aplicado en**: Diferentes estrategias de persistencia (JSON, CSV, Base de Datos)

```java
public interface EstrategiaPersistencia {
    void guardar(HistorialConversiones historial);
    HistorialConversiones cargar();
}

public class PersistenciaJSON implements EstrategiaPersistencia { }
public class PersistenciaCSV implements EstrategiaPersistencia { }
```

### 3. Factory
**Aplicado en**: Creación de diferentes tipos de conversores

```java
public class ConversorFactory {
    public static Conversor crearConversor(TipoConversor tipo) {
        switch(tipo) {
            case BASICO: return new ConversorBasico();
            case CON_CACHE: return new ConversorConCache();
            case OFFLINE: return new ConversorOffline();
        }
    }
}
```

### 4. Observer
**Aplicado en**: Notificaciones de cambios en tasas de cambio

```java
public interface ObservadorTasas {
    void onTasasActualizadas(Map<String, Double> nuevasTasas);
}
```

---

## 🗄️ Modelo de Datos

### Clase: Conversion

```java
public class Conversion {
    private String id;
    private LocalDateTime timestamp;
    private String monedaOrigen;
    private String monedaDestino;
    private double montoOrigen;
    private double montoDestino;
    private double tasaCambio;
    
    // Getters, setters, constructores
}
```

### Clase: Moneda

```java
public class Moneda {
    private String codigo;           // "USD"
    private String nombre;           // "Dólar estadounidense"
    private String simbolo;          // "$"
    private String pais;             // "Estados Unidos"
    private boolean esFavorita;
    
    // Getters, setters, constructores
}
```

### Clase: TasaCambio

```java
public class TasaCambio {
    private String moneda;
    private double tasa;
    private LocalDateTime ultimaActualizacion;
    private boolean esValida;
    
    // Getters, setters, constructores
}
```

---

## 🔐 Gestión de Configuración

### Archivo: config.properties

```properties
# API Configuration
api.key=${EXCHANGE_RATE_API_KEY}
api.url=https://v6.exchangerate-api.com/v6/
api.timeout=5000
api.max.retries=3

# Cache Configuration
cache.enabled=true
cache.expiration.minutes=60
cache.persist=true

# Historial Configuration
historial.max.entries=1000
historial.auto.save=true
historial.file.path=data/historial.json

# Logging Configuration
log.level=INFO
log.file.path=logs/conversor.log
log.max.size=10MB
```

### Clase: ConfiguracionApp

```java
public class ConfiguracionApp {
    private static Properties propiedades;
    
    static {
        cargarConfiguracion();
    }
    
    public static String obtener(String clave);
    public static int obtenerInt(String clave);
    public static boolean obtenerBoolean(String clave);
}
```

---

## 🔄 Flujos de Datos

### Flujo 1: Conversión de Moneda

```
Usuario → Principal → ConversorDeMoneda → CacheTasas
                                              │
                                              ├─ (Cache Hit) → Retornar tasa
                                              │
                                              └─ (Cache Miss) → ConsultaTasaDeCambio
                                                                      │
                                                                      ▼
                                                                ExchangeRate-API
                                                                      │
                                                                      ▼
                                                              Actualizar Cache
                                                                      │
                                                                      ▼
                                                              Retornar tasa
```

### Flujo 2: Persistencia de Historial

```
Conversión Exitosa → HistorialConversiones.agregar()
                              │
                              ▼
                    GestorArchivos.guardar()
                              │
                              ▼
                    Serializar a JSON
                              │
                              ▼
                    Escribir a data/historial.json
```

---

## 🧪 Estrategia de Testing

### Pirámide de Testing

```
        ┌─────────────┐
        │   E2E (5%)  │  ← Tests de interfaz completa
        ├─────────────┤
        │ Integration │  ← Tests de integración (15%)
        │    (15%)    │
        ├─────────────┤
        │    Unit     │  ← Tests unitarios (80%)
        │    (80%)    │
        └─────────────┘
```

### Tests Unitarios
- Cada clase tiene su archivo de test correspondiente
- Uso de mocks para dependencias externas
- Cobertura mínima: 80%

### Tests de Integración
- Validar interacción entre componentes
- Usar API mock para evitar dependencia externa

### Tests E2E
- Validar flujos completos de usuario
- Automatización de UI (para versión con GUI)

---

## 📊 Métricas de Calidad

### Métricas de Código

| Métrica | Objetivo | Herramienta |
|---------|----------|-------------|
| Cobertura de Tests | > 80% | JaCoCo |
| Complejidad Ciclomática | < 10 por método | SonarQube |
| Duplicación de Código | < 3% | SonarQube |
| Deuda Técnica | < 5% | SonarQube |

### Métricas de Rendimiento

| Métrica | Objetivo |
|---------|----------|
| Tiempo de respuesta (con cache) | < 100ms |
| Tiempo de respuesta (sin cache) | < 2s |
| Uso de memoria | < 100MB |
| Tiempo de inicio | < 3s |

---

## 🚀 Despliegue

### Empaquetado

```bash
# Compilar con Maven
mvn clean package

# Generar JAR ejecutable
mvn assembly:single

# Resultado: conversor-moneda-2.0.jar
```

### Estructura de Distribución

```
conversor-moneda-2.0/
├── bin/
│   ├── conversor.bat (Windows)
│   └── conversor.sh (Linux/Mac)
├── lib/
│   └── *.jar (dependencias)
├── config/
│   └── config.properties.example
├── data/
│   └── .gitkeep
├── logs/
│   └── .gitkeep
└── conversor-moneda-2.0.jar
```

---

## 📚 Referencias

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Java Design Patterns](https://java-design-patterns.com/)
- [ExchangeRate-API Documentation](https://www.exchangerate-api.com/docs)

---

**Última Actualización**: 2025-12-06  
**Versión del Documento**: 1.0  
**Mantenedor**: [mysterio-wil](https://github.com/mysterio-wil)
