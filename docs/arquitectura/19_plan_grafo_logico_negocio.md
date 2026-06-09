# Plan arquitectónico 19 — Grafo lógico de negocio

Estado: **especificación futura**  
Tanda origen: **7B — Especificación del futuro grafo lógico**

## 1. Propósito

El **grafo lógico de negocio** será un tipo de proyecto propio para representar la estructura lógica de un levantamiento administrativo desde macroflujos hasta reglas, acciones, entidades, estados, riesgos y preguntas pendientes.

No debe nacer como un simple grafo libre renombrado. Puede reutilizar capacidades visuales del grafo libre y del canvas común cuando convenga, pero debe tener dominio, gramática, validación, ayuda y derivación propios.

Su objetivo principal será convertir el levantamiento lógico en una vista navegable y trazable:

```txt
Levantamiento lógico de negocio
→ macroflujos
→ flujos / microflujos
→ casos de uso
→ acciones transformadoras
→ reglas, precondiciones, invariantes y postcondiciones
→ entidades, estados, reportes, riesgos y preguntas pendientes
```

## 2. Relación con Domain Model Studio

Domain Model Studio está enfocado en diseño y documentación de aplicaciones administrativas. El grafo lógico debe reforzar ese flujo:

```txt
Plantilla generada por la herramienta
→ IA completa Markdown con gramática controlada
→ Domain Model Studio importa documento lógico
→ usuario corrige y valida
→ se deriva grafo lógico y otros artefactos
→ se exporta documentación canónica
```

El grafo lógico no reemplaza los diagramas existentes. Funciona como una vista de trazabilidad y estructura lógica sobre la fuente lógica canónica del análisis.

## 3. Diferencia con grafo libre

| Aspecto | Grafo libre | Grafo lógico de negocio |
|---|---|---|
| Propósito | Relaciones flexibles, ideas, mapas generales | Levantamiento formal de negocio administrativo |
| Dominio | Nodo genérico y relación etiquetada | Nodos semánticos MF/FL/CU/ACC/RN/PRE/INV/POST/ENT/EST/REP/RISK/PEND |
| Relaciones | Etiquetas libres | Relaciones tipadas y validadas |
| Gramática | Flexible | Controlada y derivable desde levantamiento lógico |
| Validación | Básica: nodos aislados, loops, etiquetas | Semántica: jerarquía, trazabilidad, invariantes, bloqueos, derivaciones |
| Ayuda | Teoría breve de grafos | Guía operativa y académica del levantamiento lógico |
| Derivación | Puede recibir borradores simples | Debe derivarse desde el documento lógico y alimentar otros artefactos |

Regla arquitectónica:

```txt
Reutilizar infraestructura visual: sí.
Reutilizar el dominio de FreeGraph como dominio final: no.
```

## 4. Tipo de proyecto propuesto

ID sugerido:

```txt
logical-business-graph
```

Nombre visible sugerido:

```txt
Grafo lógico de negocio
```

Categoría sugerida:

```txt
Levantamiento y análisis
```

Perfil de interacción sugerido:

```txt
GRAPH controlado
```

Naturaleza:

```txt
Diagrama semántico derivable / editable
```

Salida principal:

```txt
Markdown canónico, SVG, PNG, .dms
```

## 5. Paquetes futuros recomendados

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/domain/logicalbusinessgraph
src/main/java/com/marcosmoreira/domainmodelstudio/application/logicalbusinessgraph
src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/logicalbusinessgraph
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph
```

Clases candidatas:

```txt
LogicalBusinessGraphDocument
LogicalBusinessGraphNode
LogicalBusinessGraphEdge
LogicalBusinessGraphNodeKind
LogicalBusinessGraphRelationKind
LogicalBusinessGraphValidationPolicy
LogicalBusinessGraphLayoutPolicy
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphMarkdownExporter
LogicalBusinessGraphCanvasAdapter
LogicalBusinessGraphRenderKit
LogicalBusinessGraphSvgWriter
```

## 6. Nodos semánticos mínimos

| Prefijo | Tipo | Lectura |
|---|---|---|
| MF | MACRO_FLOW | Macroflujo del negocio |
| FL | FLOW | Flujo o microflujo operativo |
| CU | USE_CASE | Caso de uso reutilizable |
| ACC | ACTION | Acción transformadora |
| RN | BUSINESS_RULE | Regla de negocio |
| PRE | PRECONDITION | Condición inicial |
| INV | INVARIANT | Verdad protegida durante operación |
| POST | POSTCONDITION | Condición de cierre |
| ENT | ENTITY | Entidad candidata o concepto persistible |
| EST | STATE | Estado del negocio o de entidad |
| REP | REPORT | Reporte o salida analítica |
| RISK | RISK | Riesgo operativo, lógico o técnico |
| PEND | PENDING_QUESTION | Pregunta pendiente del levantamiento |

Nota de vocabulario:

```txt
FL debe cubrir flujo y microflujo por ahora.
No introducir MICRO como prefijo independiente salvo necesidad futura demostrada.
```

## 7. Relaciones semánticas mínimas

Relaciones permitidas iniciales:

```txt
contiene
usa
reutiliza
ejecuta
aplica
requiere
protege
garantiza
crea
modifica
consulta
genera
alimenta
bloquea
habilita
depende_de
deriva_en
```

Lectura esperada:

| Relación | Uso típico |
|---|---|
| contiene | MF contiene FL; FL contiene CU si se usa modo jerárquico |
| usa | FL usa CU; CU usa ACC |
| reutiliza | Un CU aparece en varios FL |
| ejecuta | CU ejecuta ACC |
| aplica | RN aplica a CU, ACC, ENT o REP |
| requiere | ACC requiere PRE; CU requiere PRE |
| protege | ACC o CU protege INV |
| garantiza | ACC o CU garantiza POST |
| crea | ACC crea ENT |
| modifica | ACC modifica ENT o EST |
| consulta | ACC consulta ENT o REP |
| genera | ACC genera REP, evidencia o documento |
| alimenta | ENT alimenta REP; CU alimenta REP |
| bloquea | PEND o RISK bloquea CU, RN, ENT o derivación |
| habilita | PRE, POST o EST habilita otro CU/ACC |
| depende_de | REP depende_de ENT; CU depende_de RN |
| deriva_en | Nodo lógico deriva_en diagrama/documento futuro |

## 8. Reglas de validación inicial

La validación debe ser semántica, no solo visual.

Reglas mínimas:

```txt
MF debe contener al menos un FL.
FL debe usar al menos un CU o explicar su estado pendiente.
CU reutilizado debe definirse una sola vez y referenciarse varias veces.
ACC crítica debe declarar PRE, INV y POST cuando corresponda.
RN sin relación aplica/requiere/protege debe generar advertencia.
INV debe estar protegida por al menos una ACC o CU.
POST debe estar garantizada por al menos una ACC o CU.
PEND puede bloquear CU, RN, ENT, REP o derivaciones.
RISK debe estar vinculado a operación, regla, entidad o reporte.
ENT aislada sin regla, acción o reporte debe generar advertencia.
REP debe depender de ENT, ACC, CU o RN.
Las relaciones deben respetar compatibilidades por tipo.
```

No todas las advertencias deben bloquear. La herramienta debe diferenciar:

```txt
error estructural
advertencia de madurez
riesgo documental
pregunta pendiente
```

## 9. Gramática Markdown propuesta

La gramática debe ser importable y estable. Un borrador posible:

```md
---
diagram_type: logical-business-graph
contract: logical-business-graph-v1
source: logical-business-intake
---

# Nodos

| ID | Tipo | Nombre | Estado | Resumen |
|---|---|---|---|---|
| MF-001 | MF | Gestión académica | borrador | Agrupa matrícula, calificaciones y reportes. |
| FL-001 | FL | Registrar matrícula | validado parcialmente | Flujo operativo de inscripción. |
| CU-001 | CU | Validar datos del estudiante | validado | Caso reutilizable. |
| ACC-001 | ACC | Confirmar matrícula | pendiente | Cambia estado de solicitud a activa. |
| INV-001 | INV | Matrícula activa con estudiante válido | validado | Protege integridad académica. |
| ENT-001 | ENT | Estudiante | validado | Entidad candidata central. |

# Relaciones

| Origen | Relación | Destino | Guarda / condición | Observación |
|---|---|---|---|---|
| MF-001 | contiene | FL-001 |  |  |
| FL-001 | usa | CU-001 |  | Caso reutilizable. |
| CU-001 | ejecuta | ACC-001 | [datos completos] |  |
| ACC-001 | protege | INV-001 |  |  |
| ACC-001 | modifica | ENT-001 |  |  |
```

El parser debe tolerar alias controlados, pero el exportador debe escribir formato canónico.

## 10. Derivación desde levantamiento lógico

El grafo lógico debe poder derivarse desde `LogicalBusinessDocument`.

Fuente esperada:

```txt
LogicalBusinessDocument.sections()
LogicalBusinessItemKind.MACRO_FLOW
LogicalBusinessItemKind.FLOW
LogicalBusinessItemKind.USE_CASE
LogicalBusinessItemKind.ACTION
LogicalBusinessItemKind.RULE
LogicalBusinessItemKind.PRECONDITION
LogicalBusinessItemKind.INVARIANT
LogicalBusinessItemKind.POSTCONDITION
LogicalBusinessItemKind.ENTITY
LogicalBusinessItemKind.STATE
LogicalBusinessItemKind.REPORT
LogicalBusinessItemKind.RISK
LogicalBusinessItemKind.PENDING_QUESTION
```

Destino futuro:

```txt
LogicalBusinessDerivationTarget.LOGICAL_BUSINESS_GRAPH
```

La derivación debe ser revisable. No debe abrir ni modificar diagramas automáticamente sin confirmación del usuario.

## 11. SideDock y toolbar esperados

Toolbar contextual mínima:

```txt
Agregar nodo
Agregar relación
Autoorganizar
Validar
Derivar desde levantamiento
Exportar Markdown
Exportar SVG
Exportar PNG
```

SideDock sugerido:

```txt
Estructura
Propiedades
Relaciones
Validación
Trazas internas
Artefactos compatibles
Ayuda
```

La ayuda del SideDock debe explicar cómo operar el grafo. La guía académica debe explicar teoría de levantamiento lógico, trazabilidad, estados, precondiciones, invariantes y postcondiciones.

## 12. Reutilización visual permitida

Permitido reutilizar:

```txt
InteractiveCanvasSurfaceView
InteractiveCanvasAdapter
CanvasAdapterInteractionState
CanvasElementIdCodec
CanvasContentBoundsCalculator
CanvasSelectionSupport
CanvasConnectorLabelEditingSupport
CanvasBendPointEditingSupport
GraphLayoutPolicy si no fuerza semántica libre
```

No permitido reutilizar como modelo final:

```txt
FreeGraphDocument como documento de negocio lógico
FreeGraphNode como nodo semántico final
FreeGraphEdge como relación semántica final
FreeGraphMarkdownParser como parser canónico final
```

Puede existir un mapper temporal:

```txt
LogicalBusinessGraphDocument → FreeGraph-like visual view
```

pero no debe ser la fuente de verdad.

## 13. Relación con otros artefactos

El grafo lógico debe conectar con:

```txt
Levantamiento lógico: fuente lógica canónica.
UML Casos de uso: CU y actores derivados.
BPMN / Flujo operativo: MF, FL, ACC y decisiones.
UML Actividad: acciones y ramas.
UML Secuencia: interacciones actor-sistema desde CU/ACC.
UML Estados: EST y transiciones.
Diccionario de datos: ENT, atributos y relaciones candidatas.
Roles y permisos: actores, CU y reglas de autorización.
Pantallas / Wireframes: CU y ACC visibles al usuario.
Reportes: REP, ENT y RN.
Pruebas: PRE, INV, POST y reglas críticas.
```

## 14. Alcance fuera de esta especificación

No implementar todavía:

```txt
nuevo tipo visible en catálogo
parser productivo
editor JavaFX
render final
SVG final
recursos IA finales
importación automática desde levantamiento
transformaciones automáticas hacia todos los diagramas
```

Esta Tanda 7B solo fija la frontera arquitectónica para evitar que el futuro grafo lógico nazca contaminando el grafo libre o el modelo conceptual.

## 15. Guardarraíles futuros sugeridos

Tests recomendados cuando se implemente:

```txt
LogicalBusinessGraphNodeKind contiene MF/FL/CU/ACC/RN/PRE/INV/POST/ENT/EST/REP/RISK/PEND.
LogicalBusinessGraphRelationKind contiene las relaciones semánticas mínimas.
El parser importa nodos y relaciones canónicos.
El exporter round-trip conserva tipos y relaciones.
La validación detecta MF sin FL, FL sin CU, INV no protegida y PEND bloqueante.
La derivación desde LogicalBusinessDocument genera grafo revisable.
El editor usa perfil GRAPH controlado.
El SideDock muestra validación y trazabilidad.
El grafo lógico no depende de FreeGraphDocument como dominio final.
El modelo conceptual sigue congelado.
```

## 16. Criterio de aceptación futuro

El grafo lógico estará listo como tipo productivo cuando pueda:

```txt
importar Markdown logical-business-graph-v1;
abrir un canvas visual editable;
validar nodos y relaciones semánticas;
derivarse desde levantamiento lógico;
exportar Markdown reimportable;
exportar SVG/PNG documental;
mostrar ayuda operativa y guía académica;
no contaminar grafo libre ni modelo conceptual.
```

## 19. Contrato aplicado en Tanda 32

Desde Tanda 32, `logical-business-graph` queda registrado en el catálogo oficial como **Grafo lógico del negocio**. Desde Tanda 33, el proyecto ya cuenta con dominio puro propio para `LogicalBusinessGraphDocument`, nodos, relaciones, estados e issues semánticos. Desde Tanda 34 cuenta con parser/exportador Markdown. Desde Tanda 35 puede derivarse desde el Levantamiento lógico como borrador importable. Desde Tanda 36 tiene canvas visual inicial. Desde Tanda 37 tiene SideDock operativo, propiedades, leyenda visible y ayuda operativa. Desde Tanda 38 cuenta con ejemplo oficial UENS importable; todavía queda pendiente profundizar recursos IA/plantilla, guía académica, persistencia y exportaciones completas.

Estado inicial de producto:

```txt
supportStatus: IN_PREPARATION
workspaceKind: PLACEHOLDER_GUIDE
capabilities: PLANNING_VIEW
```

Esta decisión evita prometer una salida visual antes de implementar dominio, parser, canvas, SideDock, ayuda operativa, guía académica, persistencia y exportaciones.

### Leyenda obligatoria de abreviaciones

El futuro workspace, la ayuda operativa del sidebar y la guía académica deben exponer una leyenda visible con esta lectura mínima:

```txt
MF   Macroflujo
FL   Flujo o microflujo
CU   Caso de uso
ACC  Acción transformadora
RN   Regla de negocio
PRE  Precondición
INV  Invariante
POST Postcondición
ENT  Entidad candidata
EST  Estado
REP  Reporte
RISK Riesgo
PEND Pregunta pendiente
```

La abreviación puede usarse en tarjetas compactas y relaciones, pero el usuario debe poder entenderla sin salir de la herramienta.

### Separación de ayudas

```txt
Guía académica:
Explica conceptos lógicos, trazabilidad, relación con levantamiento lógico y fronteras con otros diagramas.

Ayuda del sidebar:
Explica cómo usar la herramienta: crear nodo, conectar, editar, validar, mover, exportar y leer la leyenda.
```

### Regla de reutilización visual

Se permite copiar o adaptar patrones de grafo libre y canvas común para acelerar implementación, pero el dominio final debe ser propio.

```txt
Permitido: reutilizar interacción visual, selección, movimiento, conectores, zoom, exportación y SideDock transversal.
Prohibido: declarar FreeGraphDocument como fuente de verdad del grafo lógico.
```
