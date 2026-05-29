# Tanda 2 — Canvas común canónico

## Propósito

Esta tanda deja preparado el canvas común como infraestructura reutilizable para los diagramas visuales especializados. El modelo conceptual se mantiene como canon de calidad interactiva: nodos movibles, conectores editables, etiquetas ajustables, puntos intermedios, zoom, paneo y exportación legible.

La decisión de esta tanda es **no crear un tercer canvas**. La ruta elegida es evolucionar `InteractiveCanvasSurfaceView`, que ya recibe `InteractiveCanvasAdapter`, `InteractiveCanvasRenderKit` y `ZoomableDiagramSurface`, agregando perfiles de interacción y puertos faltantes para capacidades futuras.

## Canon de referencia

| Capacidad | Modelo conceptual | Canvas común especializado | Decisión |
|---|---|---|---|
| Mover nodos | Maduro | Existente | Se conserva y se gobierna por perfil. |
| Selección por área | Maduro | Existente | Se conserva y se gobierna por perfil. |
| Seleccionar conectores | Maduro | Existente | Se conserva y se gobierna por perfil. |
| Puntos intermedios | Maduro | Parcial | Se gobierna por perfil y adapter. |
| Etiquetas movibles | Maduro | Pendiente | Se crea puerto para Tanda 4. |
| Endpoints movibles | Maduro | Pendiente | Se crea puerto para Tanda 4 o posterior. |
| Preview vivo | Maduro | Parcial | Se crea puerto para formalizarlo. |
| Resize de nodos | No central | Relevante para wireframes | Se crea puerto opcional. |

## Perfiles de interacción

Se agregó `DiagramInteractionProfile` como contrato para declarar qué operaciones permite el contexto visual activo. El canvas común ya no debe asumir que todo es un grafo libre.

Perfiles estándar:

- `GRAPH`: diagramas de nodos y conectores, como UML clases, casos de uso, BPMN, C4, módulos y flujo de pantallas.
- `SEQUENCE`: UML secuencia; mantiene orden temporal y bloquea puntos intermedios libres.
- `WIREFRAME`: maquetas administrativas; permite movimiento y resize de componentes, no conectores semánticos.
- `MATRIX`: matrices administrativas, como roles/permisos.
- `DOCUMENT`: documentos técnicos, como diccionario de datos.
- `READ_ONLY_REFERENCE`: referencia documental, como ayuda académica.

`DiagramInteractionProfileResolver` resuelve el perfil desde `DiagramTypeId`.

## Puertos agregados

Se agregaron puertos pequeños para capacidades que no deben vivir quemadas dentro del canvas:

- `CanvasConnectorLabelPort`: mover etiquetas de conectores.
- `CanvasEndpointPort`: reconectar extremos cuando la notación lo permita.
- `CanvasLivePreviewPort`: formalizar previsualización viva.
- `CanvasResizePort`: redimensionar nodos/componentes.

Estos puertos quedan listos para las tandas siguientes. No obligan todavía a todos los adapters a implementar capacidades que no les corresponden.

## Cambios en `InteractiveCanvasSurfaceView`

`InteractiveCanvasSurfaceView` ahora recibe un `DiagramInteractionProfile`. Los constructores existentes se mantienen y resuelven el perfil por defecto desde el `DiagramTypeId` del adapter.

El canvas consulta el perfil antes de habilitar:

- arrastre de nodos;
- selección por área;
- selección de conectores;
- creación/edición de puntos intermedios;
- eliminación de puntos intermedios por teclado.

Esto evita que UML secuencia, wireframes, matrices o documentos hereden comportamientos de grafo libre por accidente.

## Cambios en `CanvasInteractionController`

`CanvasInteractionController` también recibe perfil de interacción. La acción de borrar punto intermedio se bloquea si el perfil activo no admite bendpoints.

## Reglas de arquitectura

- El canvas común trabaja con nodos, conectores, selección, handles y viewport.
- El canvas común no conoce familias concretas como UML, BPMN, C4, diccionario o roles/permisos.
- El adapter traduce operaciones al modelo del workspace activo.
- El render kit dibuja según la familia visual.
- El perfil decide qué operaciones están permitidas.

## Lo que queda para tandas posteriores

- Tanda 3: extraer soportes comunes para adapters.
- Tanda 4: implementar etiquetas movibles, endpoints y conectores más ricos.
- Tanda 5: ampliar perfiles por tipo y conectar toolbars/sidebar.
- Tanda 8: centralizar fit, viewport y apertura cómoda de ejemplos.
