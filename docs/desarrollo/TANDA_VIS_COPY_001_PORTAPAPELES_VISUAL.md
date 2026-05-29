# Tanda VIS-COPY-001 — Portapapeles visual inicial

## Objetivo

Permitir que los diagramas visuales compatibles puedan copiar y pegar una selección rectangular sin perder relaciones ni propiedades visuales básicas.

Esta tanda implementa el primer corte seguro del contrato de selección y transferencia visual: portapapeles visual interno con `Ctrl+C` / `Ctrl+V` en el canvas, aplicado inicialmente a Grafo libre y Grafo lógico del negocio.

## Alcance implementado

- `InteractiveCanvasSurfaceView` reconoce `Ctrl+C` y `Ctrl+V` cuando el adaptador activo implementa `CanvasSelectionClipboardPort`.
- La selección copiada incluye nodos y conectores seleccionados.
- Cuando una relación seleccionada requiere sus extremos, los extremos se incluyen para preservar consistencia.
- Cuando dos nodos seleccionados ya tienen una relación entre sí, la relación se incluye en el payload.
- Al pegar, se generan IDs/códigos nuevos para evitar colisiones.
- Al pegar, las posiciones se desplazan ligeramente para que la copia no quede exactamente encima del original.
- Se preservan layouts de nodos, rutas de conectores, bendpoints, marcadores, offsets de etiqueta y visibilidad cuando existen en el layout activo.
- El portapapeles valida tipo de diagrama: no se pega una selección de Grafo libre en Grafo lógico ni viceversa.

## Diagramas habilitados

- Grafo libre.
- Grafo lógico del negocio.

## Decisiones de seguridad

- El portapapeles es interno de la aplicación; no escribe contenido en el portapapeles del sistema operativo.
- La compatibilidad se valida con `DiagramTypeId`.
- El pegado no reutiliza IDs originales: todos los nodos y relaciones reciben identificadores nuevos.
- La operación notifica por statusbar si no hay selección, si el destino no es compatible o si no hay proyecto activo.

## Fuera de alcance de esta tanda

- Diálogo de toolbar `Transferir selección...` hacia otro proyecto abierto.
- Pegar entre tipos con conversión semántica.
- Transferencia en UML Clases, Mapa de módulos, Wireframes, Flujo de pantallas, Arquitectura y Comportamiento.
- Portapapeles serializado externo.

## Siguiente tanda recomendada

`VIS-COPY-002`: diálogo `Transferir selección...` desde toolbar contextual. Debe listar proyectos abiertos compatibles, confirmar destino y copiar la selección hacia el proyecto elegido sin depender de activar manualmente la pestaña destino.
