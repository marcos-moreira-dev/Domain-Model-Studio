# Métricas post-refactor canvas — Tanda 16

## Resumen focalizado

| Archivo | Líneas Tanda 15 | Líneas Tanda 16 | Cambio |
|---|---:|---:|---:|
| `InteractiveCanvasSurfaceView.java` | 772 | 450 | -322 |
| `CanvasNodeInteractionCoordinator.java` | 0 | 155 | +155 |
| `CanvasConnectorLabelOverlayRenderer.java` | 0 | 149 | +149 |
| `CanvasNodeVisualRegistry.java` | 0 | 113 | +113 |
| `CanvasBendPointHandleRenderer.java` | 0 | 100 | +100 |
| `CanvasPointMapper.java` | 0 | 10 | +10 |

## Lectura

La cantidad total de líneas del paquete puede crecer ligeramente porque se agregan clases con nombres explícitos y documentación mínima, pero la clase central deja de ser monolítica y baja al límite de revisión humana definido por la arquitectura del proyecto.

## Estado arquitectónico

- `InteractiveCanvasSurfaceView` ya no está en la lista de deuda conocida de los tests de arquitectura.
- La lógica de nodos, etiquetas y bendpoints quedó en clases transversales independientes.
- Ninguna clase nueva del paquete `presentation/interactivecanvas` importa paquetes específicos de UML Clases o Grafo libre.

## Hotspots que siguen pendientes

La Tanda 16 no intenta resolver otros hotspots. Siguen pendientes para tandas posteriores:

- `MainShellCommandHandler.java`
- `UmlClassDiagramViewModel.java`
- `DmsProjectJsonReader.java`
- `DmsProjectJsonWriter.java`
- `DiagramCanvasView.java` y `DiagramCanvasViewModel.java`, que pertenecen al modelo conceptual protegido.
