# IMP-11 — UML Secuencia especializado sobre canvas común

## Decisión técnica

UML Secuencia conserva el documento semántico existente:

```text
BehaviorDiagramDocument
BehaviorNodeKind.PARTICIPANT
BehaviorNodeKind.ACTIVATION
BehaviorNodeKind.NOTE
BehaviorEdgeKind.MESSAGE
BehaviorEdgeKind.RETURN_MESSAGE
```

Pero ya no usa el renderizador genérico `BehaviorRenderKit`. El editor ahora selecciona renderizador según el tipo:

```text
BehaviorDiagramKind.UML_SEQUENCE -> SequenceCanvasAdapter + SequenceRenderKit
otros behavior              -> BehaviorCanvasAdapter + BehaviorRenderKit
```

Esto evita crear una jerarquía nueva de dominio antes de necesitarla, pero respeta la teoría mínima del diagrama de secuencia.

## Layout

El layout persistente sigue usando IDs existentes:

```text
behavior-node:<id>
behavior-edge:<id>
```

Interpretación especializada:

```text
Participante:
  x = posición horizontal persistente
  y = bloqueada en la zona superior

Mensaje:
  y = derivada del orden de BehaviorEdge dentro del documento
  x1/x2 = centro de las líneas de vida origen/destino
```

La primera posición horizontal evita la grilla de 3 columnas del layout genérico. Si el participante todavía tiene un `y` generado por layout inicial, el adapter calcula una X horizontal por orden. Cuando el usuario mueve el participante, se guarda `y = PARTICIPANT_TOP_Y`, permitiendo distinguir layout manual.

## Interacción

Se reutiliza la infraestructura común:

```text
scroll = zoom
clic derecho + arrastre = paneo
clic izquierdo sobre participante/nota/activación = seleccionar y mover
clic izquierdo sobre fondo + arrastre = selección rectangular
```

Para UML Secuencia:

```text
supportsBendPoints() = false
```

La razón es que los mensajes son horizontales y temporales; no conviene mezclar vértices libres con una línea de vida.

## SVG

`SpecializedVisualSvgWriter` ahora detecta:

```text
DiagramTypeId.UML_SEQUENCE
```

y usa una rama vectorial propia:

```text
sequence-participant
sequence-lifeline
sequence-message
sequence-message-return
sequence-label
```

El test `SequenceVisualSvgDiagramExporterTest` protege que el SVG contenga líneas de vida y mensajes vectoriales, y que no incruste raster.

## Riesgo pendiente

El siguiente refinamiento natural sería permitir reordenar mensajes con un control explícito de toolbar o panel derecho. No se debe implementar con drag libre sin definir antes cómo persistir el orden temporal.
