# 📋 Task List - Conversor de Moneda v2.0

Este documento proporciona un sistema de seguimiento detallado de todas las tareas del roadmap v2.0.

---

## 🎯 Fase 1 - Fundamentos (Crítico)

### 1.1 Externalizar API Key 🔐

**Estado**: ⏳ Pendiente  
**Prioridad**: 🔴 CRÍTICA  
**Estimación**: 2-3 horas

#### Tareas
- [ ] Crear archivo `config.properties` en la raíz
- [ ] Crear archivo `config.properties.example` como plantilla
- [ ] Agregar `config.properties` al `.gitignore`
- [ ] Crear clase `GestorConfiguracion.java`
- [ ] Modificar `ConsultaTasaDeCambio.java` para usar configuración externa
- [ ] Implementar fallback a variable de entorno
- [ ] Actualizar `README.md` con instrucciones de configuración
- [ ] Probar con API key desde archivo
- [ ] Probar con API key desde variable de entorno
- [ ] Verificar mensaje de error si falta API key

**Archivos Afectados**:
- `config.properties` (nuevo)
- `config.properties.example` (nuevo)
- `src/GestorConfiguracion.java` (nuevo)
- `src/ConsultaTasaDeCambio.java` (modificar)
- `.gitignore` (modificar)
- `README.md` (modificar)

---

### 1.2 Validación de Entrada Robusta ✅

**Estado**: ⏳ Pendiente  
**Prioridad**: 🔴 CRÍTICA  
**Estimación**: 3-4 horas

#### Tareas
- [ ] Crear clase `ValidadorEntrada.java`
- [ ] Implementar método `leerOpcion(Scanner, int, int)`
- [ ] Implementar método `leerMonto(Scanner)`
- [ ] Implementar método `leerCodigoMoneda(Scanner, String)`
- [ ] Implementar método `confirmar(Scanner, String)`
- [ ] Modificar `Principal.java` para usar validador
- [ ] Agregar manejo de `InputMismatchException`
- [ ] Implementar límite de reintentos (3 intentos)
- [ ] Agregar mensajes de error descriptivos con emojis
- [ ] Probar con entradas de texto en campos numéricos
- [ ] Probar con opciones fuera de rango
- [ ] Probar con montos negativos
- [ ] Verificar limpieza de buffer del scanner

**Archivos Afectados**:
- `src/ValidadorEntrada.java` (nuevo)
- `src/Principal.java` (modificar)

---

### 1.3 Persistencia del Historial 💾

**Estado**: ⏳ Pendiente  
**Prioridad**: 🔴 CRÍTICA  
**Estimación**: 4-5 horas

#### Tareas
- [ ] Crear clase `Conversion.java` (modelo de datos)
- [ ] Crear clase `GestorArchivos.java`
- [ ] Implementar método `guardarHistorialJSON()`
- [ ] Implementar método `cargarHistorialJSON()`
- [ ] Implementar método `exportarCSV()`
- [ ] Modificar `HistorialConversiones.java` para usar nuevo modelo
- [ ] Implementar auto-guardado después de cada conversión
- [ ] Implementar carga automática al iniciar
- [ ] Agregar opción de exportar a CSV en el menú
- [ ] Agregar opción de limpiar historial
- [ ] Crear directorio `data/` automáticamente
- [ ] Agregar `/data/` al `.gitignore`
- [ ] Probar guardado y carga de historial
- [ ] Probar exportación a CSV
- [ ] Verificar formato JSON válido

**Archivos Afectados**:
- `src/Conversion.java` (nuevo)
- `src/GestorArchivos.java` (nuevo)
- `src/HistorialConversiones.java` (modificar)
- `src/Principal.java` (modificar - agregar opciones de menú)
- `.gitignore` (modificar)

---

### 1.4 Manejo de Excepciones Mejorado ⚠️

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟡 ALTA  
**Estimación**: 4-5 horas

#### Tareas
- [ ] Crear directorio `src/excepciones/`
- [ ] Crear clase `ConversorException.java` (base abstracta)
- [ ] Crear clase `ApiException.java`
- [ ] Crear clase `ConversionException.java`
- [ ] Crear clase `ValidacionException.java`
- [ ] Modificar `ConsultaTasaDeCambio.java` para lanzar `ApiException`
- [ ] Modificar `ConversorDeMoneda.java` para lanzar `ConversionException`
- [ ] Implementar reintentos automáticos (máximo 3)
- [ ] Implementar backoff exponencial
- [ ] Agregar validación de monedas soportadas
- [ ] Actualizar `Principal.java` para manejar excepciones
- [ ] Probar con API key inválida
- [ ] Probar con conexión a internet desactivada
- [ ] Probar con monedas no soportadas
- [ ] Verificar mensajes de error descriptivos

**Archivos Afectados**:
- `src/excepciones/ConversorException.java` (nuevo)
- `src/excepciones/ApiException.java` (nuevo)
- `src/excepciones/ConversionException.java` (nuevo)
- `src/excepciones/ValidacionException.java` (nuevo)
- `src/ConsultaTasaDeCambio.java` (modificar)
- `src/ConversorDeMoneda.java` (modificar)
- `src/Principal.java` (modificar)

---

## 🔧 Fase 2 - Funcionalidades (Importante)

### 2.1 Conversión entre Cualquier Par de Monedas 🌍

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟡 ALTA  
**Estimación**: 6-8 horas

#### Tareas
- [ ] Crear clase `CatalogoMonedas.java`
- [ ] Crear clase `Moneda.java` (modelo)
- [ ] Obtener lista de monedas desde la API
- [ ] Implementar búsqueda de monedas por código
- [ ] Implementar búsqueda de monedas por nombre
- [ ] Crear menú de selección dinámica de monedas
- [ ] Implementar sistema de favoritos
- [ ] Guardar favoritos en `config.properties`
- [ ] Mostrar nombre completo y símbolo de monedas
- [ ] Actualizar `Principal.java` con nuevo menú
- [ ] Probar conversión entre monedas no predefinidas
- [ ] Probar búsqueda de monedas
- [ ] Probar sistema de favoritos

**Archivos Afectados**:
- `src/CatalogoMonedas.java` (nuevo)
- `src/Moneda.java` (nuevo)
- `src/Principal.java` (modificar)
- `src/ConsultaTasaDeCambio.java` (modificar)
- `config.properties` (modificar)

---

### 2.2 Caché de Tasas de Cambio ⚡

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟡 ALTA  
**Estimación**: 5-6 horas

#### Tareas
- [ ] Crear clase `CacheTasas.java`
- [ ] Crear clase `TasaCambio.java` (modelo)
- [ ] Implementar patrón Singleton para el caché
- [ ] Implementar sistema de expiración (1 hora por defecto)
- [ ] Implementar persistencia del caché en archivo
- [ ] Modificar `ConversorDeMoneda.java` para usar caché
- [ ] Agregar opción de forzar actualización manual
- [ ] Mostrar timestamp de última actualización
- [ ] Agregar indicador visual de frescura de datos
- [ ] Configurar tiempo de expiración en `config.properties`
- [ ] Probar con caché válido
- [ ] Probar con caché expirado
- [ ] Medir reducción de llamadas a la API

**Archivos Afectados**:
- `src/CacheTasas.java` (nuevo)
- `src/TasaCambio.java` (nuevo)
- `src/ConversorDeMoneda.java` (modificar)
- `src/Principal.java` (modificar)
- `config.properties` (modificar)

---

### 2.3 Tests Unitarios 🧪

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟡 MEDIA  
**Estimación**: 8-10 horas

#### Tareas
- [ ] Configurar JUnit 5 en el proyecto
- [ ] Configurar Mockito
- [ ] Configurar JaCoCo para cobertura
- [ ] Crear `test/ConversorDeMonedaTest.java`
- [ ] Crear `test/ValidadorEntradaTest.java`
- [ ] Crear `test/HistorialConversionesTest.java`
- [ ] Crear `test/CacheTasasTest.java`
- [ ] Crear `test/GestorArchivosTest.java`
- [ ] Implementar mocks para la API
- [ ] Escribir tests para casos exitosos
- [ ] Escribir tests para casos de error
- [ ] Escribir tests para validaciones
- [ ] Ejecutar todos los tests
- [ ] Verificar cobertura > 80%
- [ ] Configurar ejecución de tests en Maven/Gradle

**Archivos Afectados**:
- `pom.xml` o `build.gradle` (modificar)
- `test/ConversorDeMonedaTest.java` (nuevo)
- `test/ValidadorEntradaTest.java` (nuevo)
- `test/HistorialConversionesTest.java` (nuevo)
- `test/CacheTasasTest.java` (nuevo)
- `test/GestorArchivosTest.java` (nuevo)

---

### 2.4 Logging con SLF4J 📝

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟢 MEDIA  
**Estimación**: 3-4 horas

#### Tareas
- [ ] Agregar dependencias SLF4J y Logback
- [ ] Crear `resources/logback.xml`
- [ ] Configurar niveles de log (DEBUG, INFO, WARN, ERROR)
- [ ] Configurar rotación de logs
- [ ] Agregar logs en `ConsultaTasaDeCambio`
- [ ] Agregar logs en `ConversorDeMoneda`
- [ ] Agregar logs de auditoría para conversiones
- [ ] Crear directorio `logs/`
- [ ] Agregar `/logs/` al `.gitignore`
- [ ] Probar diferentes niveles de log
- [ ] Verificar rotación de archivos

**Archivos Afectados**:
- `pom.xml` o `build.gradle` (modificar)
- `resources/logback.xml` (nuevo)
- `src/ConsultaTasaDeCambio.java` (modificar)
- `src/ConversorDeMoneda.java` (modificar)
- `.gitignore` (modificar)

---

## 🎨 Fase 3 - UX (Deseable)

### 3.1 Interfaz Gráfica (JavaFX) 🖥️

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟢 MEDIA  
**Estimación**: 20-25 horas

#### Tareas
- [ ] Configurar JavaFX en el proyecto
- [ ] Diseñar mockups de la interfaz
- [ ] Crear estructura de paquetes `src/gui/`
- [ ] Crear clase `VentanaPrincipal.java`
- [ ] Crear clase `PanelConversion.java`
- [ ] Crear clase `PanelHistorial.java`
- [ ] Crear clase `PanelConfiguracion.java`
- [ ] Crear archivo `resources/estilos.css`
- [ ] Implementar conversión en tiempo real
- [ ] Implementar tabla de historial
- [ ] Agregar gráficos de tendencias (opcional)
- [ ] Implementar selector de idioma
- [ ] Mantener compatibilidad con versión consola
- [ ] Probar interfaz en diferentes resoluciones
- [ ] Probar usabilidad

**Archivos Afectados**:
- `pom.xml` o `build.gradle` (modificar)
- `src/gui/VentanaPrincipal.java` (nuevo)
- `src/gui/PanelConversion.java` (nuevo)
- `src/gui/PanelHistorial.java` (nuevo)
- `src/gui/PanelConfiguracion.java` (nuevo)
- `resources/estilos.css` (nuevo)

---

### 3.2 Soporte Multiidioma (i18n) 🌐

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟢 BAJA  
**Estimación**: 6-8 horas

#### Tareas
- [ ] Crear clase `GestorIdiomas.java`
- [ ] Crear `resources/messages_es.properties`
- [ ] Crear `resources/messages_en.properties`
- [ ] Externalizar todos los textos de la UI
- [ ] Implementar selector de idioma
- [ ] Detectar idioma del sistema automáticamente
- [ ] Guardar preferencia de idioma en config
- [ ] Implementar cambio de idioma sin reiniciar
- [ ] Traducir todos los mensajes al inglés
- [ ] Probar cambio de idioma
- [ ] Verificar formato de números localizado

**Archivos Afectados**:
- `src/i18n/GestorIdiomas.java` (nuevo)
- `resources/messages_es.properties` (nuevo)
- `resources/messages_en.properties` (nuevo)
- Todos los archivos con textos de UI (modificar)

---

### 3.3 Gráficos de Tendencias 📈

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟢 BAJA  
**Estimación**: 8-10 horas

#### Tareas
- [ ] Agregar dependencia JFreeChart
- [ ] Crear clase `GeneradorGraficos.java`
- [ ] Obtener datos históricos de tasas
- [ ] Implementar gráfico de líneas
- [ ] Implementar selector de rango de fechas
- [ ] Implementar zoom en gráficos
- [ ] Agregar exportación de gráficos como imagen
- [ ] Integrar con interfaz gráfica
- [ ] Probar con diferentes rangos de fechas
- [ ] Optimizar rendimiento

**Archivos Afectados**:
- `pom.xml` o `build.gradle` (modificar)
- `src/graficos/GeneradorGraficos.java` (nuevo)
- `src/gui/PanelGraficos.java` (nuevo)

---

### 3.4 Modo Offline 📴

**Estado**: ⏳ Pendiente  
**Prioridad**: 🟢 BAJA  
**Estimación**: 4-5 horas

#### Tareas
- [ ] Crear clase `DetectorConexion.java`
- [ ] Implementar detección de estado de conexión
- [ ] Usar tasas cacheadas en modo offline
- [ ] Mostrar advertencia de datos desactualizados
- [ ] Implementar sincronización automática
- [ ] Agregar indicador visual de modo offline
- [ ] Probar con conexión desactivada
- [ ] Probar sincronización al recuperar conexión

**Archivos Afectados**:
- `src/DetectorConexion.java` (nuevo)
- `src/ConversorDeMoneda.java` (modificar)
- `src/gui/VentanaPrincipal.java` (modificar)

---

## 📊 Resumen de Progreso

### Por Fase

| Fase | Total Tareas | Completadas | Pendientes | Progreso |
|------|--------------|-------------|------------|----------|
| Fase 1 - Fundamentos | 42 | 0 | 42 | 0% |
| Fase 2 - Funcionalidades | 51 | 0 | 51 | 0% |
| Fase 3 - UX | 42 | 0 | 42 | 0% |
| **TOTAL** | **135** | **0** | **135** | **0%** |

### Por Prioridad

| Prioridad | Total Tareas | Completadas | Pendientes |
|-----------|--------------|-------------|------------|
| 🔴 CRÍTICA | 32 | 0 | 32 |
| 🟡 ALTA | 29 | 0 | 29 |
| 🟢 MEDIA | 44 | 0 | 44 |
| 🟢 BAJA | 30 | 0 | 30 |

---

## 🎯 Próximos Pasos

1. ✅ Documentación completada
2. [ ] Configurar sistema de gestión de tareas (GitHub Projects)
3. [ ] Crear issues en GitHub para cada tarea de Fase 1
4. [ ] Iniciar implementación de Tarea 1.1: Externalizar API Key
5. [ ] Configurar entorno de desarrollo

---

## 📝 Notas

- Actualizar este documento conforme se completen las tareas
- Marcar tareas como `[x]` cuando estén completadas
- Agregar notas o comentarios según sea necesario
- Revisar estimaciones y ajustar según experiencia real

---

**Última Actualización**: 2025-12-06  
**Versión del Documento**: 1.0  
**Mantenedor**: [mysterio-wil](https://github.com/mysterio-wil)
