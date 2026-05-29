# Tanda 20 — Drag directo UML y lectura de capacidades transversales del canvas conceptual

## Estado

Aplicada.

## Objetivo

Corregir el detalle de interacción observado en UML Clases: el usuario no debe tener que hacer un clic para seleccionar y luego otro clic para arrastrar. El flujo correcto es:

```txt
presionar sobre clase o módulo → seleccionar → arrastrar en el mismo gesto → soltar y persistir posición
```

Además, esta tanda deja leída y documentada la referencia UX del modelo conceptual para alinear futuras capacidades transversales del canvas común sin mezclar código conceptual con los proyectos especializados.

## Corrección aplicada

Archivo principal:

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassStructurePanel.java
```

El problema era una interferencia entre el canvas UML y el panel de estructura:

1. El canvas seleccionaba una clase o módulo en `MOUSE_PRESSED`.
2. Esa selección sincronizaba `moduleList` o `classList` del panel lateral.
3. Los listeners de esas listas disparaban `refreshCanvas.run()` aunque la selección venía del canvas.
4. Ese refresh reconstruía el lienzo durante el mismo gesto de mouse.
5. El primer arrastre se perdía; por eso parecía necesario seleccionar primero y arrastrar en un segundo clic.

Se agregaron guardas `syncingControls` en los listeners de:

```txt
moduleList
classList
memberList
```

De esta forma, cuando la lista solo se está sincronizando desde una selección externa del canvas, no refresca el lienzo ni corta el gesto de arrastre.

## Capacidades observadas en el modelo conceptual

El modelo conceptual sigue congelado. No se copia ni se migra código. Se usa solo como referencia UX.

Capacidades UX relevantes observadas:

```txt
- Clic sobre nodo: selecciona y conserva selección.
- Presionar y arrastrar nodo: mueve en el mismo gesto, sin clic previo.
- Clic sobre relación: selecciona la relación y la resalta.
- Doble clic sobre un tramo de relación: agrega punto intermedio.
- Arrastre de punto intermedio: modifica la ruta de la relación.
- Botón Quitar punto / Suprimir: elimina el punto intermedio seleccionado.
- La edición de relaciones no impide mover nodos cuando el gesto inicia sobre un nodo.
```

## Alineación transversal futura

La siguiente alineación debe tratarse como una tanda propia, no como parte de esta corrección puntual.

Nombre sugerido:

```txt
Tanda 21 — Relaciones editables transversales en diagramas visuales
```

Debe revisar:

```txt
- Selección de conectores en diagramas visuales activos.
- Doble clic para agregar bendpoint donde el perfil lo permita.
- Arrastre de bendpoint.
- Eliminación con Quitar punto / Suprimir.
- Hit testing de conectores sin romper drag de nodos.
- Excluir o tratar aparte UML Casos de uso si su geometría especializada lo requiere.
```

## Fuera de alcance

```txt
- No tocar modelo conceptual.
- No migrar DiagramCanvasView.
- No copiar lógica de ModelTreeView / InspectorView.
- No reactivar todavía edición transversal de conectores si puede volver a bloquear drag de nodos.
- No iniciar release candidate antes de confirmar el smoke de UML Clases.
```
