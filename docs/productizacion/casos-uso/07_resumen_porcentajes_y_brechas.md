# Resumen de porcentajes y brechas anti-fachada

Estado: **resumen ejecutivo para planificar implementación**  
Base: inspección estática del código, documentación previa y capturas recientes.  
Limitación: no se ejecutaron tests Maven en este entorno porque no hay `mvn` ni wrapper disponible.

---

## Matriz global por tipo visible

| Tipo visible | % al ojo | Nivel | Qué está realmente terminado | Principal brecha |
|---|---:|---|---|---|
| Modelo conceptual | 88% | Alto | Dominio, canvas, edición, layout, notaciones, PNG/SVG/Markdown, `.dms`. | Refactor de canvas por SRP y extracción de infraestructura común. |
| Diccionario de datos | 72% | Medio/alto | Editor documental, entidades/campos, PDF/Markdown, `.dms`. | Importación directa de Markdown de diccionario si se quiere abrir ejemplo UENS. |
| Mapa de módulos | 68% | Medio | Dominio, parser, editor visual básico, validación, PNG/Markdown, `.dms`. | Arrastre/layout/rutas persistentes con lienzo común. |
| Roles y permisos | 78% | Medio/alto | Matriz, roles/permisos/asignaciones, import/export, `.dms`. | Filtros, matriz grande y validaciones de auditoría. |
| Flujo de pantallas | 66% | Medio | Pantallas/transiciones, parser, editor básico, PNG/Markdown, `.dms`. | Layout navegacional interactivo y agrupación por módulo. |
| Wireframes administrativos | 74% | Medio/alto | Maquetas, componentes, plantillas, propiedades, PNG/Markdown, `.dms`. | Movimiento/redimensionado prudente sin volverse Figma. |
| UML Clases | 65% | Medio | Clases/miembros/relaciones, parser, editor básico, PNG/Markdown. | Agrupadores por módulo y relaciones UML visualmente fieles. |
| BPMN básico | 64% | Medio | Proceso básico con nodos/flujos, parser, exportación. | Carriles reales, símbolos y layout BPMN. |
| Flujo operativo | 68% | Medio | Pasos/conexiones, parser, exportación. | Orden/agrupación por responsable y ajuste visual. |
| UML Casos de uso | 56% | Medio/bajo | Datos y relaciones existen, import/export básico. | Límite del sistema, actores y estereotipos visuales. |
| UML Actividad | 62% | Medio | Acciones/decisiones/transiciones, import/export básico. | Guardas, swimlanes opcionales y layout editable. |
| UML Secuencia | 45% | Bajo/parcial fuerte | Participantes/mensajes como datos básicos e importación. | Editor temporal especializado: lifelines, tiempo vertical, mensajes ordenados. |
| UML Estados | 65% | Medio | Estados/transiciones, import/export básico. | Símbolos UML, eventos/guardas y layout editable. |
| C4 Contexto | 62% | Medio | Personas/sistemas/relaciones, parser, exportación. | Sistema principal, boundaries y layout C4. |
| C4 Contenedores | 63% | Medio | Apps/API/BD/servicios, relaciones, parser/export. | Boundaries, símbolos técnicos y rutas persistentes. |
| Despliegue técnico | 58% | Medio/bajo | Nodos/relaciones de despliegue, parser/export básico. | Ambientes/redes/hosting como zonas visuales reales. |

---

## Matriz por controles generales

| Control general | % al ojo | Estado | Nota de alineación |
|---|---:|---|---|
| Nuevo proyecto | 85% | Funcional | Debe probarse uno por tipo. |
| Abrir `.dms` | 78% | Funcional | Round-trip manual pendiente por familia. |
| Guardar `.dms` | 78% | Funcional | Confirmar sincronización de ViewModel especializado antes de guardar. |
| Cerrar pestaña/app con cambios | 75% | Funcional | Smoke con dirty state por tipo. |
| Pestañas mixtas | 78% | Funcional con riesgo | Abrir varios tipos y cambiar entre ellos. |
| Toolbar contextual | 82% | Bastante sólido | Revisar que no muestre acciones falsas. |
| Menú Exportar por capacidades | 78% | Funcional | SVG/PDF deben quedar limitados correctamente. |
| Importar Markdown | 80% | Funcional | Casos negativos pendientes. |
| Selector de ejemplos | 86% | Sólido | Falta botón/flujo de abrir familia completa. |
| Recursos IA | 88% | Sólido | Mantener sincronía con parsers. |
| Ayuda integrada | 90% | Sólido | Cuidar textos para no prometer más. |
| Exportar PNG activo | 76% | Funcional | Smoke tab activa obligatorio. |
| Exportar Markdown | 74% | Funcional con riesgo | Verificar pérdida de campos. |
| Exportar SVG | 55% | Parcial | Solo conceptual por ahora. |
| Exportar PDF | 70% | Específico | Solo diccionario por ahora. |
| Batch export | 60% | Parcial útil | Probar paquete de entrega real. |
| Undo/redo | 45% | Parcial | No prometer universalidad. |
| Zoom/fit/centrar | 55% | Parcial | Llevar a lienzo común. |
| Trazas internas/SRP | 64% | Con deuda | Clases grandes superan límites de auditoría. |

---

## Brechas prioritarias para que no sea fachada

| Prioridad | Brecha | Afecta | Acción sugerida |
|---:|---|---|---|
| 1 | Lienzo interactivo común | Mapa, flujo pantallas, UML, BPMN, C4, despliegue | Ejecutar AV-I01 y AV-I02 antes de migrar tipos. |
| 2 | UML Secuencia especializado | UML Secuencia | AV-I08 debe ir como editor temporal propio, no como cajas genéricas. |
| 3 | SVG real por familia o desactivado | Todos menos conceptual | AV-I10: exportador real o capacidad deshabilitada. |
| 4 | Smoke de tab activa/exportación | Todos | Probar exportar con varias pestañas abiertas. |
| 5 | Round-trip `.dms` por tipo | Todos | Guardar/cerrar/reabrir un proyecto por tipo. |
| 6 | Round-trip Markdown por tipo | Tipos importables | Importar → editar → exportar → revisar/reimportar. |
| 7 | Trazas internas/SRP | Shell, canvas, JSON, editors | Extraer coordinadores/controladores antes de features nuevas. |
| 8 | Abrir todos los ejemplos | Productización/smoke | Útil para pruebas y demostración. |
| 9 | Diccionario importable o honestamente referencial | Diccionario | Decidir: parser directo o seguir como referencia. |
| 10 | Matrices grandes y filtros | Roles/diccionario | Filtros y exportación legible. |

---

## Lectura de riesgo por implementación actual

| Riesgo | Evidencia de inspección | Consecuencia | Recomendación |
|---|---|---|---|
| Monolitos de coordinación | `MainShellCommandHandler` ~1600 líneas. | Fácil mezclar sesión, exportación, creación y edición. | Mantenerlo como fachada de coordinación temporal, pero extraer handlers por responsabilidad. |
| Canvas conceptual demasiado grande | `DiagramCanvasView` ~1120 líneas y `DiagramCanvasViewModel` ~908 líneas. | Reutilizarlo copiando código crearía deuda. | Extraer controladores: viewport, selección, drag, conectores, export surface. |
| JSON reader/writer grandes | `DmsProjectJsonReader` y `DmsProjectJsonWriter` superan límite humano. | Persistencia especializada difícil de revisar. | Separar serializers por documento especializado. |
| Editores especializados medianos/grandes | Views de diccionario, wireframe, módulo, behavior, architecture, etc. | Riesgo de duplicar lógica visual. | Adaptadores por tipo sobre lienzo común. |
| Tests de arquitectura podrían fallar | Límites declarados de 450/650 líneas frente a clases actuales mayores. | Guardarraíles pierden fuerza si están rojos o desactualizados. | Ejecutar en Windows y decidir: refactor o ajustar temporalmente con deuda explícita. |

---

## Recomendación de cierre inmediato

Antes de implementar más features visuales, dejar estos cuatro documentos como checklist de trabajo diario:

```text
docs/productizacion/casos-uso/00_indice_casos_uso.md
docs/productizacion/casos-uso/01_controles_generales_gestion_proyectos.md
docs/productizacion/casos-uso/02_entrada_salida_exportacion.md
docs/productizacion/casos-uso/07_resumen_porcentajes_y_brechas.md
```

Después, cada tanda AV-I debe actualizar el porcentaje del tipo afectado y mover casos de “parcial” a “terminado funcional” solo cuando haya prueba o smoke verificable.
