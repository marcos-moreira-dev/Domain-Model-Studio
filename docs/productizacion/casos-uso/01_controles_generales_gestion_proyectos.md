# Casos de uso — controles generales y gestión de proyectos

Estado: **matriz de control transversal**  
Alcance: shell JavaFX, pestañas, menú, toolbar, estado inferior, ayuda, ejemplos, recursos IA y controles visuales generales.

---

## Porcentaje general estimado

| Categoría | % al ojo | Estado | Motivo |
|---|---:|---|---|
| Ciclo de vida de proyecto | 80% | Funcional | Nuevo/abrir/guardar/cerrar existen y están conectados al shell. Falta smoke completo por todos los tipos. |
| Shell multiproyecto/multitipo | 78% | Funcional con riesgo | Hay sesiones por tab, ruteo y workspace activo. Riesgo: clases grandes y necesidad de probar tabs mixtas. |
| Menús y toolbars contextuales | 82% | Bastante sólido | Hay proveedor de acciones por tipo y política de capacidades; requiere comprobar visualmente todos los cambios de pestaña. |
| Dirty state y confirmación de cierre | 75% | Funcional | Hay estado sucio y confirmación; debe probarse en cada workspace especializado. |
| Undo/redo | 45% | Parcial | Está claro para modelo conceptual; no debe prometerse como universal en todos los editores. |
| Zoom/paneo/fit/centrar | 55% | Parcial | Maduro en conceptual; no homogéneo en lienzos especializados. |
| Validación general | 72% | Funcional básico | Hay validadores por familia, pero falta matriz de smoke por tipo. |
| Ayuda integrada | 90% | Sólido | Micro-Wikipedia, temas teóricos y figuras didácticas están trazados. |
| Recursos IA | 88% | Sólido | Hay recursos y gramáticas empaquetadas; deben mantenerse sincronizadas con parsers. |
| Trazas internas/SRP | 64% | Deuda controlada | Existen guardarraíles, pero varias clases superan límites humanos de revisión. |

---

## CU-GEN-01 Crear proyecto por tipo oficial

| Campo | Detalle |
|---|---|
| Usuario quiere | Elegir un tipo de diagrama y abrir una pestaña lista para trabajar. |
| Entrada | `Archivo > Nuevo` o botón `Nuevo`. |
| Implementación esperada | Catálogo oficial → diálogo de nuevo proyecto → creación del workspace correcto. |
| Anclaje observado | `DefaultDiagramTypeRegistry`, `DefaultCreateWorkspaceUseCase`, `WorkspaceTypeRoutingPolicy`, `WorkspaceRouteResolver`, `WorkspaceViewRegistry`. |
| Estado | **Terminado funcional — 85%** |
| Falta | Smoke de crear uno por cada tipo y verificar toolbar/status/workspace. |

### Anti-fachada

No basta con que el tipo aparezca en el selector. Debe abrir una vista real y no caer silenciosamente en el canvas conceptual.

---

## CU-GEN-02 Abrir proyecto `.dms`

| Campo | Detalle |
|---|---|
| Usuario quiere | Recuperar un proyecto guardado con su tipo y contenido. |
| Entrada | Botón `Abrir` / menú `Archivo`. |
| Anclaje observado | `OpenProjectUseCase`, `DmsProjectJsonReader`, `ProjectTabOpener`, `SpecializedWorkspaceCoordinator`. |
| Estado | **Terminado funcional — 78%** |
| Falta | Probar round-trip manual por cada familia especializada. |

### Riesgo

El archivo puede guardar documentos especializados, pero si el workspace activo no se reconstruye correctamente, el usuario ve una pestaña engañosa.

---

## CU-GEN-03 Guardar proyecto `.dms`

| Campo | Detalle |
|---|---|
| Usuario quiere | Persistir cambios del proyecto activo. |
| Entrada | Botón `Guardar`. |
| Anclaje observado | `SaveProjectUseCase`, `DmsProjectJsonWriter`, `currentProjectForSaving`. |
| Estado | **Terminado funcional — 78%** |
| Falta | Confirmar que cada editor especializado sincroniza su documento antes de guardar. |

---

## CU-GEN-04 Cerrar proyecto/pestaña sin perder cambios

| Campo | Detalle |
|---|---|
| Usuario quiere | Cerrar con confirmación si hay cambios sin guardar. |
| Entrada | Botón `Cerrar`, cerrar tab o cerrar app. |
| Anclaje observado | `DirtyStateUpdater`, `MainShellState`, `EditorTabViewState`, lógica de confirmación en shell. |
| Estado | **Terminado funcional — 75%** |
| Falta | Smoke: editar cada tipo, verificar `*`, intentar cerrar y cancelar/confirmar. |

---

## CU-GEN-05 Cambiar entre pestañas de tipos distintos

| Campo | Detalle |
|---|---|
| Usuario quiere | Trabajar con varios proyectos abiertos sin mezclar herramientas ni exportación. |
| Entrada | Tabs del editor. |
| Anclaje observado | `projectSessions`, `activateProjectSession`, `SpecializedWorkspaceCoordinator`, `ActiveOutputResolver`. |
| Estado | **Terminado funcional — 78%** |
| Falta | Smoke obligatorio con conceptual + wireframes + C4 + BPMN + roles/permisos. |

### Anti-fachada

La pestaña activa debe controlar: toolbar, estado inferior, acciones disponibles, exportación y documento guardado.

---

## CU-GEN-06 Menú y toolbar contextual por tipo

| Campo | Detalle |
|---|---|
| Usuario quiere | Ver solo acciones coherentes con el tipo activo. |
| Entrada | Toolbar superior global y toolbar contextual. |
| Anclaje observado | `DefaultDiagramToolbarActionProvider`, `DiagramCapabilityPresentationPolicy`, `MainToolbarViewModel`. |
| Estado | **Terminado funcional — 82%** |
| Falta | Probar que SVG/PDF no aparezcan o no se habiliten para tipos incorrectos. |

---

## CU-GEN-07 Deshacer y rehacer

| Campo | Detalle |
|---|---|
| Usuario quiere | Revertir cambios de edición. |
| Entrada | Botones `Deshacer` / `Rehacer`, atajos. |
| Anclaje observado | `requestUndo`, `requestRedo`, historial del `DiagramCanvasViewModel`. |
| Estado | **Parcial — 45%** |
| Falta | Definir si será solo conceptual o contrato común por workspace. |

### Decisión recomendada

Mientras no haya historial por editor especializado, el texto de UI debe comunicarlo como control del lienzo conceptual o deshabilitarlo correctamente en otros tipos.

---

## CU-GEN-08 Zoom, fit, centrar selección y navegación visual

| Campo | Detalle |
|---|---|
| Usuario quiere | Navegar diagramas grandes sin perderse. |
| Entrada | Zoom +, zoom -, 100%, ajustar contenido, centrar selección, paneo. |
| Anclaje observado | `DiagramCanvasView`, `DiagramCanvasViewModel`, acciones de toolbar. |
| Estado | **Parcial — 55%** |
| Falta | Llevar estos controles al lienzo común para mapas, UML, BPMN, C4 y wireframes. |

---

## CU-GEN-09 Validar proyecto o documento activo

| Campo | Detalle |
|---|---|
| Usuario quiere | Saber si el modelo tiene errores básicos. |
| Entrada | Botón `Validar`. |
| Anclaje observado | Validadores por familia: conceptual, diccionario, módulos, roles, pantallas, wireframes, comportamiento y arquitectura. |
| Estado | **Terminado básico — 72%** |
| Falta | Estandarizar formato de resultado y pruebas manuales por tipo. |

---

## CU-GEN-10 Ayuda integrada tipo micro-Wikipedia

| Campo | Detalle |
|---|---|
| Usuario quiere | Entender para qué sirve cada tipo sin salir de la app. |
| Entrada | Menú `Ayuda` / centro de ayuda. |
| Anclaje observado | `ManualDialog`, `ManualContent`, `DefaultTheoryCatalog`, `help/topics/*`, `docs/teoria/*`. |
| Estado | **Terminado funcional — 90%** |
| Falta | Mantener los textos sincronizados con capacidades reales. |

---

## CU-GEN-11 Recursos IA

| Campo | Detalle |
|---|---|
| Usuario quiere | Exportar plantillas/gramáticas para pedir Markdown compatible a una IA. |
| Entrada | Botón `Recursos IA`. |
| Anclaje observado | `ExportAiResourcesUseCase`, `ClasspathAiResourceCatalog`, `ai-resources/*`. |
| Estado | **Terminado funcional — 88%** |
| Falta | Test de coherencia entre recursos, `diagram_type` y parser activo por cada tipo. |

---

## CU-GEN-12 Pantalla de inicio como hub

| Campo | Detalle |
|---|---|
| Usuario quiere | Empezar rápido: nuevo, abrir, importar, ejemplos, ayuda. |
| Entrada | Tab `Pantalla de inicio`. |
| Anclaje observado | `WelcomeWorkspaceView`, CSS `welcome-*`, planificación PF16/AV-I11. |
| Estado | **Parcial útil — 68%** |
| Falta | Convertirla en hub operativo completo sin mostrar jerga interna ni opciones falsas. |

---

## Casos faltantes detectados en controles generales

| ID faltante | Caso | Prioridad | Motivo |
|---|---|---:|---|
| GEN-FALT-01 | Historial de undo/redo por workspace especializado | Media | Ahora parece conceptual. No debe venderse como universal. |
| GEN-FALT-02 | Smoke automático/manual de pestañas mixtas | Alta | Es la forma principal de detectar contaminación entre tipos. |
| GEN-FALT-03 | Scroll/gestión de muchas pestañas | Media | Las capturas muestran muchas pestañas; la ergonomía puede romperse con ejemplos abiertos. |
| GEN-FALT-04 | Matriz de disponibilidad de controles visuales por workspace | Alta | Zoom/fit/centrar no aplican igual a matriz, documento y canvas. |
| GEN-FALT-05 | Política explícita de guardado antes de exportar lote | Media | Batch export puede exportar estados no guardados si no se define la regla visible. |
