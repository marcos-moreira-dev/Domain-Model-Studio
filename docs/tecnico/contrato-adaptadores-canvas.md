# Contrato de adaptadores del canvas interactivo

Estado: **vigente desde IMPW-11**

## Propósito

Un `InteractiveCanvasAdapter` es el puente entre un documento especializado y el lienzo común. Su responsabilidad es traducir el documento semántico a nodos, conectores, layouts, selección y comandos visuales.

## Puertos pequeños

```txt
CanvasReadPort             -> lectura de nodos, conectores y layouts
CanvasSelectionPort        -> selección actual y cambios de selección
CanvasLayoutCommandPort    -> movimiento/redimensionamiento de elementos
CanvasBendPointPort        -> puntos intermedios de conectores
CanvasDirtyPort            -> marca de cambios pendientes
```

`InteractiveCanvasAdapter` agrupa esos puertos por compatibilidad, pero las piezas transversales nuevas deben depender del puerto más pequeño posible.

## Content bounds

Desde IMPW-11, `CanvasReadPort` declara `contentBounds()` con implementación por defecto. Esto corrige el contrato roto detectado en IMPW-10, donde el exportador PNG común llamaba `adapter.contentBounds()` sin que el método existiera en la interfaz.

La implementación por defecto calcula los límites desde los layouts visibles de los nodos. Los adaptadores específicos pueden sobreescribirlo para aplicar márgenes mínimos o reglas propias de exportación.

## Responsabilidades del adaptador

```txt
Traducir documento a InteractiveCanvasNode.
Traducir relaciones a InteractiveCanvasConnector.
Leer y actualizar NodeLayout/ConnectorLayout.
Sincronizar selección con el ViewModel específico.
Persistir cambios visuales mediante casos de uso o ViewModel.
Resolver límites exportables cuando la familia necesite padding especial.
```

## Lo que el adaptador no debe hacer

```txt
No crear controles JavaFX.
No administrar ScrollPane, zoom, paneo o fit.
No crear headers ni paneles laterales.
No exportar PNG directamente.
No importar familias ajenas.
```

## Flujo de renderizado

```txt
Documento especializado
→ ViewModel específico
→ CanvasAdapter específico
→ InteractiveCanvasModel snapshot
→ RenderKit específico
→ ZoomableDiagramSurface
→ Workbench común
```
