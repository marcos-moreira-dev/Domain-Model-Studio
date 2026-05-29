# Alineación 002 — Fronteras arquitectónicas y zonas prohibidas

Estado: **alineación aplicada**  
Fecha: **2026-05-24**  
Alcance: **fronteras de arquitectura, zonas congeladas, zonas restringidas y zonas operables para el cierre del Grafo lógico del negocio**  
Tipo de cambio: **documental y de guardarraíl fuente; no modifica lógica de ejecución**

## 1. Propósito

Esta alineación fija las fronteras que deben respetarse antes de implementar las tandas finales del Grafo lógico del negocio.

El objetivo no es corregir todavía catálogo, toolbar, persistencia, exportaciones ni guía académica. El objetivo es impedir que las correcciones finales mezclen responsabilidades, contaminen el modelo conceptual o conviertan el nuevo tipo de proyecto en un parche sobre componentes sagrados.

Regla principal:

```txt
corregir el Grafo lógico no autoriza tocar pantalla de inicio, modelo conceptual ni canvas conceptual.
```

## 2. Relación con la Alineación 001

La Alineación 001 definió cuándo un tipo de proyecto puede considerarse productivo. Esta Alineación 002 define **dónde se puede intervenir** para alcanzar ese contrato sin romper las zonas protegidas.

La jerarquía de verdad se mantiene:

```txt
1. log local más reciente de tests y smoke;
2. código actual;
3. catálogo Java de tipos y capacidades;
4. documentación viva;
5. bitácoras históricas.
```

## 3. Mapa de fronteras

| Nivel | Regla | Ejemplos |
|---|---|---|
| Sagrado / congelado | No tocar salvo emergencia explícita del usuario. | Pantalla de inicio, modelo conceptual, canvas conceptual. |
| Restringido transversal | Puede corregirse solo si hay impacto común comprobable. | Shell, workbench, SideDock, canvas interactivo, exportación común. |
| Operable de cierre | Puede modificarse para productivizar el Grafo lógico. | Dominio, parser/exporter, ViewModel, contributor, recursos IA, teoría y tests del Grafo lógico. |
| Documental viva | Debe actualizarse cuando el contrato técnico esté cerrado. | Matrices, smoke, release, recursos IA, estado de auditoría. |

## 4. Zonas sagradas congeladas

Estas zonas quedan fuera de las tandas normales de cierre:

```txt
pantalla de inicio;
modelo conceptual;
canvas conceptual;
DiagramCanvasView;
DiagramCanvasViewModel;
ChenDiagramRenderer;
CrowsFootDiagramRenderer;
presentation/sidebar/ como sidebar legacy del modelo conceptual.
```

### 4.1 Pantalla de inicio

La pantalla de inicio se considera visualmente terminada. No debe usarse como punto de entrada para experimentar con el Grafo lógico, nuevas ayudas, nuevos sidebars o cambios de estilo.

Solo se permite tocarla si el usuario pide explícitamente una emergencia visual o funcional.

### 4.2 Modelo conceptual

El modelo conceptual se considera código sagrado en esta fase. No debe recibir:

```txt
- código del SideDock transversal;
- dependencias del Grafo lógico;
- dependencias del Grafo libre;
- cambios de toolbar derivados del Grafo lógico;
- cambios de importación/exportación no relacionados con una emergencia conceptual;
- refactors de oportunidad.
```

### 4.3 Canvas conceptual

El canvas conceptual permanece separado del canvas transversal usado por workspaces especializados. No debe utilizarse como base para productivizar el Grafo lógico.

Queda prohibido introducir en `DiagramCanvasView`, `DiagramCanvasViewModel`, `ChenDiagramRenderer` o `CrowsFootDiagramRenderer` referencias a:

```txt
logicalbusinessgraph;
FreeGraphDocument;
SideDock;
WorkbenchSideDock;
DiagramWorkbenchView;
LogicalBusinessGraphDocument.
```

## 5. Frontera entre sidebar legacy y SideDock transversal

Hay dos familias diferentes y no deben mezclarse:

| Familia | Ubicación | Propósito | Regla |
|---|---|---|---|
| Sidebar legacy conceptual | `presentation/sidebar/` | Árbol del modelo conceptual. | Congelado con el modelo conceptual. |
| SideDock transversal | `presentation/sidedock/` + `presentation/workbench/` | Carcasa modular de proyectos especializados. | Puede pulirse para proyectos nuevos. |

El Grafo lógico debe usar el SideDock transversal, no el sidebar legacy conceptual.

## 6. Frontera del shell general

El shell (`presentation/shell`) es restringido. Puede cambiarse únicamente para cablear rutas, sesiones, comandos globales o nombres de menú cuando el cambio sea transversal y verificable.

No debe convertirse en contenedor de lógica específica del Grafo lógico.

Regla:

```txt
MainShellView y MainShellCommandHandler coordinan; no modelan la semántica del Grafo lógico.
```

Cualquier lógica específica debe vivir en:

```txt
domain/logicalbusinessgraph;
application/logicalbusinessgraph si se crea;
infrastructure/markdown;
infrastructure/json;
presentation/logicalbusinessgraph;
presentation/toolbar contributor específico;
presentation/exportable contributor específico.
```

## 7. Frontera del canvas transversal

El canvas transversal es reutilizable, pero no debe cargarse con semántica de un tipo específico.

Zonas transversales:

```txt
presentation/diagramcanvas;
presentation/interactivecanvas;
presentation/workbench;
presentation/drawing.
```

Pueden corregirse si el problema es común: selección, zoom, paneo, fit, exportación PNG, estilos rectos, labels, errores de render o contratos de adapters.

No deben recibir reglas como:

```txt
MF contiene FL;
CU ejecuta ACC;
INV debe estar protegida;
PEND bloquea algo.
```

Esas reglas pertenecen al dominio o validación del Grafo lógico, no al canvas común.

## 8. Frontera del Grafo lógico del negocio

El Grafo lógico del negocio es tipo propio. Debe conservar estas reglas:

```txt
usa LogicalBusinessGraphDocument como documento especializado;
no usa FreeGraphDocument como dominio final;
no usa DiagramModel conceptual como semántica principal;
no depende de JavaFX en domain/logicalbusinessgraph;
no depende de Markdown, JSON, persistencia ni canvas en domain/logicalbusinessgraph;
reutiliza infraestructura visual mediante adapter, no por herencia conceptual del Grafo libre.
```

La reutilización permitida es infraestructura, no semántica:

| Puede reutilizar | No puede reutilizar como verdad de dominio |
|---|---|
| `ZoomableDiagramSurface` | `FreeGraphDocument` |
| `InteractiveCanvasSurfaceView` | `DiagramCanvasViewModel` conceptual |
| `DiagramWorkbenchView` | `ModelTreeView` conceptual |
| Render kits o estilos comunes | parser del grafo libre como contrato final |
| exportadores comunes cuando apliquen | reglas semánticas genéricas sin validación lógica |

## 9. Frontera con el Levantamiento lógico

El Levantamiento lógico es expediente fuente. El Grafo lógico es vista visual semántica derivada y revisable.

Regla:

```txt
Levantamiento lógico = fuente lógica canónica estructurada.
Grafo lógico = vista trazable y navegable de esa lógica.
```

No se debe trasladar todo el CRUD documental del Levantamiento lógico al Grafo lógico ni hacer que el Grafo lógico reemplace el expediente.

## 10. Frontera de ayudas

La separación queda blindada:

| Entrada | Contenido permitido | Contenido prohibido |
|---|---|---|
| Menú `Ayuda` | Guía académica teórica, notación, fundamentos, cuándo usar/no usar, errores comunes y figuras. | Instrucciones operativas tipo “presiona este botón del SideDock”. |
| Botón `Ayuda` del SideDock | Ayuda operativa del tipo activo: paneles, selección, edición, validación, exportación y lectura de la herramienta. | Micro-wikipedia académica extensa o teoría que pertenece al menú. |

La ventana del menú debe nombrarse como **Guía académica**. El SideDock conserva la **ayuda operativa**.

## 11. Frontera visual

La regla visual de cierre es:

```txt
UI nueva o corregida: sin bordes redondeados ornamentales.
```

Aplica a:

```txt
SideDock;
workbench;
toolbar contextual;
etiquetas de canvas;
paneles nuevos;
badges;
tarjetas de UI;
componentes del Grafo lógico.
```

no debe aplicarse de forma ciega a geometría semántica de notaciones, por ejemplo eventos BPMN, casos de uso UML o estados finales, cuando la forma sea parte de la teoría del diagrama.

## 12. Matriz de intervención permitida

| Área | Estado | Intervención permitida en cierre |
|---|---|---|
| Pantalla de inicio | Congelada | No tocar. |
| Modelo conceptual | Congelado | No tocar salvo emergencia explícita. |
| Canvas conceptual | Congelado | No tocar salvo emergencia explícita. |
| Sidebar legacy conceptual | Congelado | No tocar salvo emergencia conceptual. |
| Shell general | Restringido | Solo cableado transversal verificable. |
| SideDock transversal | Operable con cuidado | Pulir módulos, ayuda operativa y estilo recto. |
| Canvas transversal | Operable con cuidado | Solo comportamiento común o estilo común. |
| Grafo libre | Productivo independiente | No contaminar con semántica del Grafo lógico. |
| Levantamiento lógico | Fuente madre estable | Solo tocar si una integración lo exige de forma mínima. |
| Grafo lógico | Operable de cierre | Sí: catálogo, toolbar, validación, persistencia, exportaciones, teoría, recursos y tests. |
| Documentación viva | Operable al final | Actualizar después de cerrar capacidades reales. |

## 13. Reglas de importación entre paquetes

Durante el cierre, estas reglas deben mantenerse:

```txt
domain/logicalbusinessgraph no importa JavaFX;
domain/logicalbusinessgraph no importa infrastructure;
domain/logicalbusinessgraph no importa presentation;
domain/logicalbusinessgraph no importa domain/freegraph;
presentation/canvas conceptual no importa logicalbusinessgraph;
presentation/canvas conceptual no importa SideDock;
presentation/canvas conceptual no importa FreeGraphDocument;
presentation/logicalbusinessgraph puede adaptar dominio hacia canvas transversal;
infrastructure/markdown puede conocer el documento lógico para parsear/exportar;
infrastructure/json puede conocer el documento lógico para persistir;
presentation/exportable puede conocer el ViewModel para resolver salida activa.
```

## 14. Antipatrones prohibidos

Quedan prohibidos como solución rápida:

```txt
- resolver fallos del Grafo lógico tocando el modelo conceptual;
- copiar FreeGraphDocument y renombrarlo sin semántica propia;
- meter reglas de negocio dentro del canvas común;
- meter validación semántica dentro del shell;
- usar documentación para prometer capacidades que el código no tiene;
- actualizar matrices antes de cerrar implementación real;
- ocultar tests fallidos quitando referencias documentales;
- introducir radius ornamental en UI nueva/corregida.
```

## 15. Tests de comprobación agregados

Esta alineación agrega un guardarraíl documental y fuente:

```bat
mvn -Dtest=Alineacion2ArchitectureBoundariesSourceTest test
```

El test verifica que:

```txt
- existe este contrato de fronteras;
- se mencionan zonas sagradas y restringidas;
- se distingue sidebar legacy de SideDock transversal;
- se conserva la frontera guía académica / ayuda operativa;
- el dominio del Grafo lógico no importa JavaFX, infraestructura, presentación ni grafo libre;
- el canvas conceptual no referencia Grafo lógico, SideDock ni Grafo libre.
```

## 16. Criterio de salida de esta alineación

Esta alineación se considera completa si:

```txt
- el contrato de fronteras existe;
- las zonas sagradas quedan explícitas;
- el SideDock transversal queda separado del sidebar conceptual;
- el Grafo lógico queda definido como tipo propio sin usar FreeGraphDocument;
- la ayuda académica queda separada de la ayuda operativa;
- se agrega test focalizado y script de validación;
- el plan vivo marca Alineación 2 como aplicada y deja Alineación 3 como siguiente.
```

## 17. Siguiente paso

La siguiente tanda normal es:

```txt
Alineación 3 — Contrato semántico del Grafo lógico del negocio.
```

Esa tanda debe decidir el contrato final de nodos, relaciones, warnings, bloqueos, CRUD manual y alcance semántico antes de tocar catálogo, toolbar, persistencia o exportaciones.
