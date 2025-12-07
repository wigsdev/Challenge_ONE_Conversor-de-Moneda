# 📚 Documentación del Proyecto - Conversor de Moneda

Bienvenido a la documentación técnica del proyecto Conversor de Moneda. Esta carpeta contiene toda la documentación necesaria para entender, desarrollar y contribuir al proyecto.

---

## 📋 Índice de Documentos

### 🚀 [ROADMAP_V2.md](./ROADMAP_V2.md)
**Roadmap completo para la versión 2.0**

Documento maestro que define la visión, objetivos y plan de desarrollo para llevar el proyecto de la versión 1.0 a la 2.0. Incluye:
- Visión general del proyecto
- 3 fases de desarrollo (Fundamentos, Funcionalidades, UX)
- Tareas detalladas con criterios de aceptación
- Cronograma tentativo
- Métricas de éxito

**Audiencia**: Todo el equipo, stakeholders  
**Actualización**: Trimestral o cuando cambien prioridades

---

### 🏛️ [ARCHITECTURE.md](./ARCHITECTURE.md)
**Guía de arquitectura del sistema**

Describe la arquitectura actual y propuesta del proyecto, incluyendo:
- Diagramas de componentes
- Patrones de diseño aplicados
- Modelo de datos
- Flujos de información
- Estrategia de testing
- Métricas de calidad

**Audiencia**: Desarrolladores, arquitectos  
**Actualización**: Cuando haya cambios arquitectónicos significativos

---

### 🛠️ [IMPLEMENTATION_GUIDE_PHASE1.md](./IMPLEMENTATION_GUIDE_PHASE1.md)
**Guía de implementación detallada - Fase 1**

Instrucciones paso a paso para implementar las mejoras críticas de la Fase 1:
1. Externalizar API Key
2. Validación de Entrada Robusta
3. Persistencia del Historial
4. Manejo de Excepciones Mejorado

Incluye código de ejemplo, archivos a modificar y checklists de verificación.

**Audiencia**: Desarrolladores implementando Fase 1  
**Actualización**: Según feedback durante implementación

---

### 📋 [TASK_LIST.md](./TASK_LIST.md)
**Lista detallada de tareas**

Sistema de seguimiento de todas las 135 tareas del roadmap v2.0:
- Desglose por fase y prioridad
- Estado de cada tarea
- Estimaciones de tiempo
- Archivos afectados
- Métricas de progreso

**Audiencia**: Todo el equipo  
**Actualización**: Diaria/semanal según progreso

---

## 🎯 Guía Rápida por Rol

### Para Desarrolladores Nuevos
1. Lee el [README.md](../README.md) principal del proyecto
2. Revisa [ARCHITECTURE.md](./ARCHITECTURE.md) para entender la estructura
3. Consulta [IMPLEMENTATION_GUIDE_PHASE1.md](./IMPLEMENTATION_GUIDE_PHASE1.md) para empezar a contribuir
4. Usa [TASK_LIST.md](./TASK_LIST.md) para elegir una tarea

### Para Project Managers
1. Revisa [ROADMAP_V2.md](./ROADMAP_V2.md) para la visión general
2. Monitorea [TASK_LIST.md](./TASK_LIST.md) para el progreso
3. Consulta las métricas de éxito en el roadmap

### Para Arquitectos
1. Estudia [ARCHITECTURE.md](./ARCHITECTURE.md) en detalle
2. Revisa las decisiones técnicas en [ROADMAP_V2.md](./ROADMAP_V2.md)
3. Valida los patrones propuestos

---

## 📂 Estructura de Documentación

```
docs/
├── README.md                          # Este archivo
├── ROADMAP_V2.md                      # Roadmap completo v2.0
├── ARCHITECTURE.md                    # Arquitectura del sistema
├── IMPLEMENTATION_GUIDE_PHASE1.md     # Guía de implementación Fase 1
├── TASK_LIST.md                       # Lista de tareas detallada
└── (futuros documentos)
    ├── IMPLEMENTATION_GUIDE_PHASE2.md
    ├── IMPLEMENTATION_GUIDE_PHASE3.md
    ├── API_DOCUMENTATION.md
    ├── USER_GUIDE.md
    └── CONTRIBUTING.md
```

---

## 🔄 Proceso de Actualización de Documentación

### Cuándo Actualizar

| Documento | Frecuencia | Trigger |
|-----------|------------|---------|
| ROADMAP_V2.md | Trimestral | Cambios en prioridades o alcance |
| ARCHITECTURE.md | Por cambio | Modificaciones arquitectónicas |
| IMPLEMENTATION_GUIDE_*.md | Por feedback | Mejoras en el proceso |
| TASK_LIST.md | Diaria/Semanal | Completar tareas |

### Cómo Actualizar

1. **Hacer cambios**: Edita el documento correspondiente
2. **Actualizar fecha**: Modifica "Última Actualización" al final del documento
3. **Incrementar versión**: Si es un cambio significativo
4. **Commit descriptivo**: Usa formato: `docs: actualizar [DOCUMENTO] - [razón]`
5. **Notificar al equipo**: Si afecta el trabajo en curso

---

## 📝 Convenciones de Documentación

### Formato
- Todos los documentos en **Markdown** (.md)
- Usar **GitHub Flavored Markdown**
- Incluir tabla de contenidos en documentos largos

### Estilo
- **Emojis** para mejorar legibilidad (con moderación)
- **Tablas** para información estructurada
- **Diagramas** en formato texto (ASCII art) o Mermaid
- **Code blocks** con syntax highlighting

### Estructura
- Título principal (H1) al inicio
- Secciones con H2, subsecciones con H3
- Líneas horizontales (`---`) para separar secciones principales
- Metadata al final: Última Actualización, Versión, Mantenedor

---

## 🤝 Contribuir a la Documentación

### Reportar Errores
Si encuentras errores, inconsistencias o información desactualizada:
1. Abre un issue en GitHub con etiqueta `documentation`
2. Describe el problema y la ubicación exacta
3. Sugiere una corrección si es posible

### Proponer Mejoras
Para proponer mejoras a la documentación:
1. Crea un issue describiendo la mejora
2. Si es aceptada, crea un PR con los cambios
3. Solicita revisión del mantenedor

### Agregar Nuevos Documentos
Para agregar documentación nueva:
1. Discute la necesidad en un issue
2. Sigue las convenciones establecidas
3. Actualiza este README.md con el nuevo documento
4. Crea PR para revisión

---

## 🔗 Enlaces Útiles

### Recursos del Proyecto
- [Repositorio Principal](https://github.com/mysterio-wil/Challenge_ONE_Conversor-de-Moneda)
- [Issues](https://github.com/mysterio-wil/Challenge_ONE_Conversor-de-Moneda/issues)
- [Pull Requests](https://github.com/mysterio-wil/Challenge_ONE_Conversor-de-Moneda/pulls)

### Recursos Externos
- [ExchangeRate-API Documentation](https://www.exchangerate-api.com/docs)
- [Java 11 Documentation](https://docs.oracle.com/en/java/javase/11/)
- [Gson User Guide](https://github.com/google/gson/blob/master/UserGuide.md)
- [JavaFX Documentation](https://openjfx.io/javadoc/21/)

### Guías de Estilo
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Markdown Guide](https://www.markdownguide.org/)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

## 📞 Contacto

**Mantenedor de Documentación**: [mysterio-wil](https://github.com/mysterio-wil)

Para preguntas sobre la documentación:
- Abre un issue con etiqueta `question` y `documentation`
- Contacta al mantenedor directamente para temas urgentes

---

## 📊 Estado de la Documentación

| Documento | Estado | Cobertura | Última Revisión |
|-----------|--------|-----------|-----------------|
| ROADMAP_V2.md | ✅ Completo | 100% | 2025-12-06 |
| ARCHITECTURE.md | ✅ Completo | 100% | 2025-12-06 |
| IMPLEMENTATION_GUIDE_PHASE1.md | ✅ Completo | 100% | 2025-12-06 |
| TASK_LIST.md | ✅ Completo | 100% | 2025-12-06 |
| IMPLEMENTATION_GUIDE_PHASE2.md | ⏳ Pendiente | 0% | - |
| IMPLEMENTATION_GUIDE_PHASE3.md | ⏳ Pendiente | 0% | - |
| API_DOCUMENTATION.md | ⏳ Pendiente | 0% | - |
| USER_GUIDE.md | ⏳ Pendiente | 0% | - |
| CONTRIBUTING.md | ⏳ Pendiente | 0% | - |

---

## 🎯 Próximos Pasos

- [ ] Crear IMPLEMENTATION_GUIDE_PHASE2.md
- [ ] Crear IMPLEMENTATION_GUIDE_PHASE3.md
- [ ] Crear API_DOCUMENTATION.md
- [ ] Crear USER_GUIDE.md
- [ ] Crear CONTRIBUTING.md
- [ ] Agregar diagramas Mermaid a ARCHITECTURE.md
- [ ] Crear video tutoriales

---

**Última Actualización**: 2025-12-06  
**Versión**: 1.0  
**Mantenedor**: [mysterio-wil](https://github.com/mysterio-wil)
