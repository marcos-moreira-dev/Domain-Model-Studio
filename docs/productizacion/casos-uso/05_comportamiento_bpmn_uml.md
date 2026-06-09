# Casos de uso — BPMN, flujo operativo y UML de comportamiento

Estado: **matriz de control de diagramas de comportamiento**  
Alcance: BPMN básico, flujo operativo, UML casos de uso, UML actividad, UML secuencia y UML estados.

---

## Resumen por tipo

| Tipo | % al ojo | Estado | Lectura rápida |
|---|---:|---|---|
| BPMN básico | 64% | Funcional básico | Tiene nodos/flujos/import/export, pero requiere semántica visual BPMN más fina. |
| Flujo operativo | 68% | Funcional básico aceptable | Menos formal; el editor genérico le afecta menos. |
| UML Casos de uso | 56% | Parcial visual | Requiere actores/casos/límite más fieles y layout UML. |
| UML Actividad | 62% | Funcional básico | Similar a BPMN/flujo; necesita pulido visual. |
| UML Secuencia | 45% | Parcial fuerte | Necesita editor temporal vertical con participantes y líneas de vida. |
| UML Estados | 65% | Funcional básico | Estados/transiciones existen; falta layout editable. |

---

# BPMN básico

Promesa correcta: **representar procesos de negocio con inicio, actividades, decisiones, fin, carriles y flujos**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| BPMN-01 Crear BPMN | Terminado funcional | 80% | `CreateBehaviorDiagramUseCase`, `BehaviorDiagramKind.BPMN_BASIC` | Smoke desde `Nuevo`. |
| BPMN-02 Importar Markdown | Terminado funcional | 80% | `BehaviorMarkdownParser` | Probar UENS y venta mínima. |
| BPMN-03 Agregar inicio/actividad/decisión/fin/carril | Terminado funcional | 75% | `AddBehaviorNodeUseCase`, toolbar BPMN | Ver figuras diferenciadas. |
| BPMN-04 Conectar flujo | Terminado funcional | 75% | `AddBehaviorEdgeUseCase` | Tabla y visual. |
| BPMN-05 Editar/eliminar | Terminado funcional | 70% | `Update/RemoveBehavior*UseCase` | Persistencia. |
| BPMN-06 Validar/exportar | Terminado básico | 70% | `ValidateBehaviorDiagramUseCase`, PNG/Markdown | Smoke tab activa. |
| BPMN-07 Carriles BPMN reales | Parcial | 35% | `BehaviorNodeKind.LANE` | Carriles deben organizar visualmente, no solo ser caja más. |
| BPMN-08 Layout BPMN legible | Parcial | 35% | Plan AV-I06 | Reducir cruces y permitir ajuste manual. |

## Casos faltantes BPMN

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| BPMN-FALT-01 | Carriles como contenedores reales | Alta | Sin carriles funcionales, BPMN se siente genérico. |
| BPMN-FALT-02 | Flujo horizontal/vertical configurable | Media | Mejora legibilidad. |
| BPMN-FALT-03 | Rutas de flujo persistentes | Alta | Evita líneas cruzadas en procesos grandes. |

---

# Flujo operativo

Promesa correcta: **explicar pasos operativos de negocio de forma simple, menos formal que BPMN**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| OPE-01 Crear flujo | Terminado funcional | 82% | `BehaviorDiagramKind.OPERATIONAL_FLOW` | Smoke desde `Nuevo`. |
| OPE-02 Importar Markdown | Terminado funcional | 80% | `BehaviorMarkdownParser` | Probar UENS y soporte mínimo. |
| OPE-03 Agregar paso/responsable/decisión/documento | Terminado funcional | 75% | `AddBehaviorNodeUseCase` | Figuras reconocibles. |
| OPE-04 Conectar pasos | Terminado funcional | 75% | `AddBehaviorEdgeUseCase` | Tabla y visual. |
| OPE-05 Editar/eliminar/validar | Terminado básico | 70% | `Update/Remove/ValidateBehavior*` | Mensajes útiles. |
| OPE-06 Exportar PNG/Markdown | Terminado funcional | 74% | `BehaviorDiagramViewModel.exportVisualAsPng` | Smoke tab activa. |
| OPE-07 Ajuste visual persistente | Parcial | 40% | Plan AV-I06 | Arrastre/rutas. |

## Casos faltantes flujo operativo

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| OPE-FALT-01 | Agrupar pasos por responsable | Media | Da más valor operativo. |
| OPE-FALT-02 | Numeración/orden explícito de pasos | Alta | El flujo operativo necesita orden humano claro. |
| OPE-FALT-03 | Ajuste manual de posiciones | Alta | Igual que otros diagramas visuales. |

---

# UML Casos de uso

Promesa correcta: **representar actores, casos de uso, límite del sistema y relaciones observables**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| USE-01 Crear diagrama | Terminado funcional | 78% | `BehaviorDiagramKind.UML_USE_CASE` | Smoke desde `Nuevo`. |
| USE-02 Importar Markdown | Terminado funcional | 80% | `BehaviorMarkdownParser` | Probar UENS y restaurante mínimo. |
| USE-03 Agregar actor/caso/límite | Parcial visual | 60% | `BehaviorNodeKind.ACTOR`, `USE_CASE`, `SYSTEM_BOUNDARY` | Actor y límite no deben ser cajas genéricas. |
| USE-04 Asociar/include/extend/generalización | Terminado funcional básico | 68% | `BehaviorEdgeKind` | Diferenciar flechas y etiquetas. |
| USE-05 Validar/exportar | Terminado básico | 68% | Validación + PNG/Markdown | Smoke tab activa. |
| USE-06 Layout UML específico | Parcial fuerte | 30% | Plan AV-I05/AV-I06 | Actores fuera, casos dentro del límite. |

## Casos faltantes UML casos de uso

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| USE-FALT-01 | Límite del sistema como contenedor real | Alta | Es central para casos de uso. |
| USE-FALT-02 | Actor con figura propia | Media | Mejora reconocimiento UML. |
| USE-FALT-03 | Include/extend con flechas y estereotipos correctos | Alta | Evita que parezca diagrama genérico. |

---

# UML Actividad

Promesa correcta: **representar flujo de acciones, decisiones, inicio, fin y transiciones**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| ACT-01 Crear actividad | Terminado funcional | 80% | `BehaviorDiagramKind.UML_ACTIVITY` | Smoke desde `Nuevo`. |
| ACT-02 Importar Markdown | Terminado funcional | 80% | `BehaviorMarkdownParser` | Probar ejemplo UENS. |
| ACT-03 Agregar acción/decisión/inicio/fin | Terminado funcional | 75% | `AddBehaviorNodeUseCase` | Figuras UML. |
| ACT-04 Conectar transiciones | Terminado funcional | 75% | `AddBehaviorEdgeUseCase` | Tabla y visual. |
| ACT-05 Validar/exportar | Terminado básico | 70% | Validación + PNG/Markdown | Smoke. |
| ACT-06 Semántica visual UML | Parcial | 35% | Plan AV-I05 | Pulir símbolos y layout. |

## Casos faltantes UML actividad

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| ACT-FALT-01 | Swimlanes opcionales | Media | No siempre necesario, pero útil. |
| ACT-FALT-02 | Decisiones con guardas visibles | Alta | Actividad necesita condiciones claras. |
| ACT-FALT-03 | Layout editable persistente | Alta | Evita cruces. |

---

# UML Secuencia

Promesa correcta: **representar mensajes entre participantes en el tiempo**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| SEQ-01 Crear secuencia | Terminado funcional de datos | 75% | `BehaviorDiagramKind.UML_SEQUENCE` | Smoke desde `Nuevo`. |
| SEQ-02 Importar Markdown | Terminado funcional | 78% | `BehaviorMarkdownParser` | Probar UENS y login mínimo. |
| SEQ-03 Agregar participantes/mensajes | Terminado funcional básico | 65% | `BehaviorNodeKind`, `BehaviorEdgeKind` | Diferenciar participantes/mensajes/retornos. |
| SEQ-04 Layout temporal vertical | No resuelto | 20% | Plan AV-I08 | Participantes arriba, líneas de vida, tiempo vertical. |
| SEQ-05 Activaciones | Parcial | 30% | Nodo/tipo de activación en comportamiento | Debe dibujarse sobre lifeline. |
| SEQ-06 Exportar sin mentir | Parcial | 45% | PNG/Markdown desde editor genérico | Etiquetar como básico hasta editor especializado. |

## Casos faltantes UML secuencia

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| SEQ-FALT-01 | Editor especializado de secuencia | Muy alta | Es el caso visual más distinto. |
| SEQ-FALT-02 | Participantes horizontales arriba | Muy alta | Sin esto no parece secuencia UML. |
| SEQ-FALT-03 | Líneas de vida verticales | Muy alta | Es la semántica principal. |
| SEQ-FALT-04 | Mensajes ordenados por tiempo | Muy alta | Debe preservar orden, no solo relaciones. |
| SEQ-FALT-05 | Retornos y activaciones visuales | Media | Mejora fidelidad UML. |

---

# UML Estados

Promesa correcta: **representar estados y transiciones de una entidad/proceso/componente**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| STA-01 Crear estados | Terminado funcional | 82% | `BehaviorDiagramKind.UML_STATE` | Smoke desde `Nuevo`. |
| STA-02 Importar Markdown | Terminado funcional | 80% | `BehaviorMarkdownParser` | Probar UENS y orden mínimo. |
| STA-03 Agregar inicio/estado/final | Terminado funcional | 75% | `AddBehaviorNodeUseCase` | Figuras diferenciadas. |
| STA-04 Agregar transición | Terminado funcional | 75% | `AddBehaviorEdgeUseCase` | Etiquetas/condiciones. |
| STA-05 Validar/exportar | Terminado básico | 72% | Validación + PNG/Markdown | Smoke tab activa. |
| STA-06 Layout editable | Parcial | 40% | Plan AV-I05/AV-I06 | Mover estados/rutas persistentes. |

## Casos faltantes UML estados

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| STA-FALT-01 | Estado inicial/final con símbolos UML correctos | Alta | Semántica visual. |
| STA-FALT-02 | Guardas/eventos en transiciones | Media | Útil para estados reales. |
| STA-FALT-03 | Auto-layout + edición manual | Alta | Evita cruces como en capturas. |
