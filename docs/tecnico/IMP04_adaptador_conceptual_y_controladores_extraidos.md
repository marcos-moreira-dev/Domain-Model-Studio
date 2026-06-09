# IMP-04 — Adaptador conceptual y controladores extraídos

## Decisión técnica

No se reemplazó `DiagramCanvasView` por `InteractiveDiagramCanvasView` en esta tanda porque el canvas conceptual contiene comportamiento maduro que no debe romperse:

```txt
- render Chen;
- render pata de gallo;
- previsualización de conectores;
- movimiento de entidades;
- movimiento de etiquetas;
- movimiento de endpoints;
- puntos intermedios;
- selección múltiple;
- exportación PNG/SVG conceptual.
```

En su lugar se hizo una extracción incremental.

## Adaptador conceptual

Se agregó:

```txt
presentation/canvas/ConceptualCanvasAdapter.java
```

Responsabilidad:

```txt
DiagramCanvasViewModel
→ InteractiveCanvasAdapter
```

El adaptador expone:

```txt
- entidades como InteractiveCanvasNode;
- relaciones como InteractiveCanvasConnector;
- layout activo por notación;
- selección normalizada de nodos, conectores y punto intermedio;
- operaciones de selección, movimiento y puntos intermedios delegadas al ViewModel.
```

La ubicación en `presentation.canvas` es deliberada. El paquete común `presentation.interactivecanvas` permanece sin conocer el modelo conceptual.

## Controladores extraídos

Se agregaron controladores específicos para reducir responsabilidad de `DiagramCanvasView` sin cambiar su renderer:

```txt
DiagramCanvasPanController
DiagramCanvasAreaSelectionController
DiagramCanvasKeyboardShortcutController
```

### Paneo

```txt
clic derecho + arrastre = paneo
```

El cálculo de scroll horizontal/vertical ya no vive directamente en el método principal de la vista.

### Selección rectangular

```txt
clic izquierdo en fondo + arrastre = selección rectangular
```

La clase solo administra la geometría y delega la decisión de qué nodos caen dentro al canvas conceptual.

### Atajos de teclado

```txt
Suprimir / Backspace = eliminar punto intermedio seleccionado
```

La eliminación de punto intermedio queda como acción explícita. No se usa clic derecho.

## Regla conservada

```txt
clic derecho = paneo únicamente
```

Cualquier acción destructiva o puntual debe ir por toolbar o atajo claro, no por menú contextual de diagrama.

## Riesgos pendientes

```txt
- DiagramCanvasView sigue siendo grande.
- El canvas común todavía no renderiza Chen/Crow's Foot.
- Los diagramas especializados aún no usan ConceptualCanvasAdapter; tendrán adaptadores propios.
- Persistencia visual multitipo sigue pendiente.
```

## Siguiente paso recomendado

No migrar aún BPMN/C4/UML. Primero continuar con:

```txt
IMP-05 — Layout visual multitipo y persistencia .dms
```

Después usar mapa de módulos como primer cliente real migrado en IMP-07.
