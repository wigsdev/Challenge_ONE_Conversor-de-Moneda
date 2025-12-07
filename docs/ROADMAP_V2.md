# 🚀 Roadmap - Conversor de Moneda v2.0

## Visión General

Este roadmap define las mejoras planificadas para llevar el Conversor de Moneda de la versión 1.0 a la 2.0, organizadas en tres fases priorizadas según su impacto en seguridad, funcionalidad y experiencia de usuario.

---

## 📊 Estado del Proyecto

- **Versión Actual**: 1.0
- **Versión Objetivo**: 2.0
- **Fecha de Inicio**: Diciembre 2025
- **Duración Estimada**: 3-4 meses

---

## 🎯 Fase 1 - Fundamentos (Crítico)

**Objetivo**: Resolver problemas críticos de seguridad, robustez y persistencia de datos.

**Duración Estimada**: 4-6 semanas

### 1.1 Externalizar API Key 🔐

**Prioridad**: 🔴 CRÍTICA

**Problema Actual**: La API key está hardcodeada en `ConsultaTasaDeCambio.java` (línea 8), lo que representa un riesgo de seguridad.

**Solución Propuesta**:
- [ ] Crear archivo `config.properties` en la raíz del proyecto
- [ ] Agregar `config.properties` al `.gitignore`
- [ ] Crear `config.properties.example` como plantilla
- [ ] Modificar `ConsultaTasaDeCambio.java` para leer desde archivo de configuración
- [ ] Implementar fallback a variable de entorno `EXCHANGE_RATE_API_KEY`
- [ ] Actualizar documentación con instrucciones de configuración

**Archivos a Modificar**:
- `src/ConsultaTasaDeCambio.java`
- `.gitignore`
- `README.md`

**Criterios de Aceptación**:
- ✅ API key no está en el código fuente
- ✅ Aplicación funciona con configuración externa
- ✅ Mensaje de error claro si falta la API key
- ✅ Documentación actualizada

---

### 1.2 Validación de Entrada Robusta ✅

**Prioridad**: 🔴 CRÍTICA

**Problema Actual**: La aplicación puede crashear con entradas inválidas (texto en lugar de números, opciones fuera de rango).

**Solución Propuesta**:
- [ ] Crear clase `ValidadorEntrada` con métodos de validación
- [ ] Implementar validación de opciones del menú (1-8)
- [ ] Implementar validación de montos (números positivos)
- [ ] Agregar manejo de `InputMismatchException`
- [ ] Implementar reintentos con límite (máximo 3 intentos)
- [ ] Agregar mensajes de error descriptivos

**Archivos a Crear**:
- `src/ValidadorEntrada.java`

**Archivos a Modificar**:
- `src/Principal.java`

**Ejemplo de Implementación**:
```java
public class ValidadorEntrada {
    public static int leerOpcion(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int opcion = scanner.nextInt();
                if (opcion >= min && opcion <= max) {
                    return opcion;
                }
                System.out.println("Opción fuera de rango. Ingrese un valor entre " + min + " y " + max);
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número.");
                scanner.next(); // Limpiar buffer
            }
        }
    }
}
```

**Criterios de Aceptación**:
- ✅ No hay crashes con entradas inválidas
- ✅ Mensajes de error claros y útiles
- ✅ Usuario puede reintentar sin reiniciar la aplicación
- ✅ Validación de todos los inputs del usuario

---

### 1.3 Persistencia del Historial 💾

**Prioridad**: 🔴 CRÍTICA

**Problema Actual**: El historial se pierde al cerrar la aplicación.

**Solución Propuesta**:
- [ ] Crear clase `GestorArchivos` para manejo de I/O
- [ ] Implementar serialización a JSON usando Gson
- [ ] Guardar historial automáticamente después de cada conversión
- [ ] Cargar historial al iniciar la aplicación
- [ ] Implementar opción de exportar historial a CSV
- [ ] Agregar opción de limpiar historial

**Archivos a Crear**:
- `src/GestorArchivos.java`
- `data/historial.json` (generado automáticamente)

**Archivos a Modificar**:
- `src/HistorialConversiones.java`
- `src/Principal.java`
- `.gitignore` (agregar `/data/`)

**Estructura JSON Propuesta**:
```json
{
  "conversiones": [
    {
      "timestamp": "2025-12-06T18:30:00",
      "monedaOrigen": "USD",
      "monedaDestino": "ARS",
      "montoOrigen": 100.0,
      "montoDestino": 9850.0,
      "tasaCambio": 98.5
    }
  ]
}
```

**Criterios de Aceptación**:
- ✅ Historial persiste entre sesiones
- ✅ Formato JSON válido y legible
- ✅ Exportación a CSV funcional
- ✅ Manejo de errores de I/O

---

### 1.4 Manejo de Excepciones Mejorado ⚠️

**Prioridad**: 🟡 ALTA

**Problema Actual**: Manejo de excepciones limitado y poco específico.

**Solución Propuesta**:
- [ ] Crear jerarquía de excepciones personalizadas
- [ ] Implementar `ConversorException` como excepción base
- [ ] Crear `ApiException` para errores de API
- [ ] Crear `ConversionException` para errores de conversión
- [ ] Agregar logging con SLF4J + Logback
- [ ] Implementar reintentos automáticos para fallos de red

**Archivos a Crear**:
- `src/excepciones/ConversorException.java`
- `src/excepciones/ApiException.java`
- `src/excepciones/ConversionException.java`
- `src/excepciones/ValidacionException.java`
- `resources/logback.xml`

**Archivos a Modificar**:
- `src/ConsultaTasaDeCambio.java`
- `src/ConversorDeMoneda.java`
- `pom.xml` o configuración de dependencias

**Criterios de Aceptación**:
- ✅ Excepciones específicas para cada tipo de error
- ✅ Mensajes de error descriptivos
- ✅ Logs guardados en archivo
- ✅ Reintentos automáticos funcionando

---

## 🔧 Fase 2 - Funcionalidades (Importante)

**Objetivo**: Expandir capacidades y mejorar rendimiento.

**Duración Estimada**: 6-8 semanas

### 2.1 Conversión entre Cualquier Par de Monedas 🌍

**Prioridad**: 🟡 ALTA

**Problema Actual**: Solo se pueden convertir 6 pares de monedas predefinidos.

**Solución Propuesta**:
- [ ] Obtener lista completa de monedas soportadas desde la API
- [ ] Crear menú dinámico de selección de monedas
- [ ] Implementar búsqueda de monedas por código o nombre
- [ ] Agregar opción de "favoritos" para monedas frecuentes
- [ ] Mostrar nombre completo y símbolo de cada moneda

**Archivos a Crear**:
- `src/CatalogoMonedas.java`
- `data/monedas_soportadas.json`

**Archivos a Modificar**:
- `src/Principal.java`
- `src/ConsultaTasaDeCambio.java`

**Criterios de Aceptación**:
- ✅ Conversión entre cualquier par de monedas disponibles
- ✅ Lista de monedas actualizada desde la API
- ✅ Búsqueda funcional
- ✅ Sistema de favoritos operativo

---

### 2.2 Caché de Tasas de Cambio ⚡

**Prioridad**: 🟡 ALTA

**Problema Actual**: Cada conversión hace una llamada a la API, desperdiciando recursos y tiempo.

**Solución Propuesta**:
- [ ] Implementar sistema de caché con tiempo de expiración
- [ ] Cachear tasas por 1 hora (configurable)
- [ ] Mostrar timestamp de última actualización
- [ ] Agregar opción de forzar actualización manual
- [ ] Implementar caché persistente en archivo

**Archivos a Crear**:
- `src/CacheTasas.java`

**Archivos a Modificar**:
- `src/ConsultaTasaDeCambio.java`
- `src/ConversorDeMoneda.java`

**Criterios de Aceptación**:
- ✅ Reducción de llamadas API en 90%+
- ✅ Tiempo de respuesta < 100ms para conversiones cacheadas
- ✅ Indicador visual de frescura de datos
- ✅ Caché persiste entre sesiones

---

### 2.3 Tests Unitarios 🧪

**Prioridad**: 🟡 MEDIA

**Solución Propuesta**:
- [ ] Configurar JUnit 5
- [ ] Crear tests para `ConversorDeMoneda`
- [ ] Crear tests para `ValidadorEntrada`
- [ ] Crear tests para `HistorialConversiones`
- [ ] Implementar mocks para la API
- [ ] Configurar cobertura de código (JaCoCo)
- [ ] Objetivo: 80%+ de cobertura

**Archivos a Crear**:
- `test/ConversorDeMonedaTest.java`
- `test/ValidadorEntradaTest.java`
- `test/HistorialConversionesTest.java`
- `test/CacheTasasTest.java`

**Criterios de Aceptación**:
- ✅ Mínimo 80% de cobertura de código
- ✅ Todos los tests pasan
- ✅ Tests ejecutables con Maven/Gradle

---

### 2.4 Logging con SLF4J 📝

**Prioridad**: 🟢 MEDIA

**Solución Propuesta**:
- [ ] Agregar dependencias SLF4J + Logback
- [ ] Configurar niveles de log (DEBUG, INFO, WARN, ERROR)
- [ ] Implementar rotación de logs
- [ ] Logs separados por componente
- [ ] Agregar logs de auditoría para conversiones

**Archivos a Crear**:
- `resources/logback.xml`
- `logs/` (directorio)

**Criterios de Aceptación**:
- ✅ Logs estructurados y legibles
- ✅ Rotación automática de archivos
- ✅ Diferentes niveles configurables

---

## 🎨 Fase 3 - UX (Deseable)

**Objetivo**: Mejorar significativamente la experiencia de usuario.

**Duración Estimada**: 8-10 semanas

### 3.1 Interfaz Gráfica (JavaFX) 🖥️

**Prioridad**: 🟢 MEDIA

**Solución Propuesta**:
- [ ] Configurar JavaFX en el proyecto
- [ ] Diseñar mockups de la interfaz
- [ ] Implementar ventana principal con menú
- [ ] Crear panel de conversión con campos interactivos
- [ ] Implementar vista de historial con tabla
- [ ] Agregar gráficos de tendencias (opcional)
- [ ] Mantener compatibilidad con versión de consola

**Archivos a Crear**:
- `src/gui/VentanaPrincipal.java`
- `src/gui/PanelConversion.java`
- `src/gui/PanelHistorial.java`
- `resources/estilos.css`
- `resources/iconos/`

**Criterios de Aceptación**:
- ✅ Interfaz intuitiva y moderna
- ✅ Conversión en tiempo real al escribir
- ✅ Historial visible y exportable
- ✅ Responsive design

---

### 3.2 Soporte Multiidioma (i18n) 🌐

**Prioridad**: 🟢 BAJA

**Solución Propuesta**:
- [ ] Implementar ResourceBundle de Java
- [ ] Crear archivos de propiedades para español e inglés
- [ ] Externalizar todos los textos de la UI
- [ ] Agregar selector de idioma en configuración
- [ ] Detectar idioma del sistema automáticamente

**Archivos a Crear**:
- `resources/messages_es.properties`
- `resources/messages_en.properties`
- `src/i18n/GestorIdiomas.java`

**Criterios de Aceptación**:
- ✅ Cambio de idioma sin reiniciar
- ✅ Todos los textos traducidos
- ✅ Formato de números localizado

---

### 3.3 Gráficos de Tendencias 📈

**Prioridad**: 🟢 BAJA

**Solución Propuesta**:
- [ ] Integrar JFreeChart o similar
- [ ] Obtener datos históricos de tasas
- [ ] Mostrar gráfico de evolución de tasas
- [ ] Permitir selección de rango de fechas
- [ ] Exportar gráficos como imagen

**Archivos a Crear**:
- `src/graficos/GeneradorGraficos.java`

**Criterios de Aceptación**:
- ✅ Gráficos interactivos y claros
- ✅ Exportación funcional
- ✅ Rendimiento aceptable

---

### 3.4 Modo Offline 📴

**Prioridad**: 🟢 BAJA

**Solución Propuesta**:
- [ ] Detectar estado de conexión
- [ ] Usar tasas cacheadas en modo offline
- [ ] Mostrar advertencia de datos desactualizados
- [ ] Sincronizar automáticamente al recuperar conexión

**Criterios de Aceptación**:
- ✅ Funcionalidad básica sin internet
- ✅ Indicador visual de modo offline
- ✅ Sincronización automática

---

## 📋 Checklist General de Implementación

### Antes de Empezar Cada Fase
- [ ] Crear rama de desarrollo (`git checkout -b feature/fase-X`)
- [ ] Revisar dependencias necesarias
- [ ] Actualizar documentación técnica

### Durante el Desarrollo
- [ ] Escribir tests antes del código (TDD)
- [ ] Hacer commits frecuentes y descriptivos
- [ ] Mantener README.md actualizado
- [ ] Documentar decisiones técnicas importantes

### Al Finalizar Cada Fase
- [ ] Ejecutar todos los tests
- [ ] Revisar cobertura de código
- [ ] Actualizar CHANGELOG.md
- [ ] Crear pull request para revisión
- [ ] Mergear a rama principal
- [ ] Crear tag de versión

---

## 🛠️ Tecnologías y Dependencias

### Actuales
- Java 11+
- Gson 2.x
- ExchangeRate-API

### A Agregar

#### Fase 1
- SLF4J 2.0+
- Logback 1.4+

#### Fase 2
- JUnit 5
- Mockito 5+
- JaCoCo (cobertura)

#### Fase 3
- JavaFX 21+
- JFreeChart 1.5+
- ControlsFX (componentes UI)

---

## 📊 Métricas de Éxito

### Fase 1
- ✅ 0 vulnerabilidades de seguridad
- ✅ 0 crashes por entrada inválida
- ✅ 100% de persistencia de datos

### Fase 2
- ✅ 80%+ cobertura de tests
- ✅ 90%+ reducción de llamadas API
- ✅ < 100ms tiempo de respuesta promedio

### Fase 3
- ✅ Satisfacción de usuario > 4/5
- ✅ Soporte para 2+ idiomas
- ✅ Interfaz gráfica funcional

---

## 🔄 Proceso de Desarrollo

### Metodología
- **Desarrollo Iterativo**: Cada fase se completa antes de iniciar la siguiente
- **Code Reviews**: Obligatorias para cada PR
- **Testing Continuo**: Tests automatizados en cada commit
- **Documentación Continua**: Actualización paralela al código

### Versionado Semántico
- **v2.0.0**: Lanzamiento completo de todas las fases
- **v1.1.0**: Fase 1 completada
- **v1.2.0**: Fase 2 completada
- **v1.3.0**: Fase 3 completada

---

## 📞 Contacto y Contribuciones

Para contribuir a este roadmap:
1. Revisa los issues abiertos en GitHub
2. Comenta en el issue que deseas trabajar
3. Sigue las guías de contribución en `CONTRIBUTING.md`
4. Envía un pull request

---

## 📅 Cronograma Tentativo

| Fase | Inicio | Fin | Duración |
|------|--------|-----|----------|
| Fase 1 - Fundamentos | Semana 1 | Semana 6 | 6 semanas |
| Fase 2 - Funcionalidades | Semana 7 | Semana 14 | 8 semanas |
| Fase 3 - UX | Semana 15 | Semana 24 | 10 semanas |
| **Total** | - | - | **24 semanas (~6 meses)** |

---

## 🎯 Próximos Pasos Inmediatos

1. ✅ Crear carpeta `docs/`
2. ✅ Documentar roadmap completo
3. [ ] Configurar sistema de gestión de tareas (GitHub Projects)
4. [ ] Crear issues para cada tarea de Fase 1
5. [ ] Iniciar implementación de externalización de API key

---

**Última Actualización**: 2025-12-06  
**Versión del Documento**: 1.0  
**Mantenedor**: [mysterio-wil](https://github.com/mysterio-wil)
