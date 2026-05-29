# Tanda VIS-COPY-3 — Portapapeles visual multi-diagrama

## Objetivo

Ampliar el alcance del portapapeles visual para que la selección rectangular no quede limitada a Grafo libre y Grafo lógico. El objetivo de producto es que los diagramas visuales interactivos puedan copiar y pegar selecciones dentro de proyectos del mismo tipo, conservando nodos, relaciones y layout.

## Alcance implementado

La tanda extiende `Ctrl+C` / `Ctrl+V` mediante `CanvasSelectionClipboardPort` y `ProjectVisualSelectionTransferService` para estos diagramas visuales:

- Modelo conceptual.
- C4 Contexto.
- C4 Contenedores.
- Despliegue técnico.
- BPMN básico.
- Flujo operativo.
- UML Casos de uso.
- UML Actividad.
- UML Secuencia.
- UML Estados.
- UML Clases.
- Mapa de módulos.
- Flujo de pantallas.
- Wireframes administrativos.

Grafo libre y Grafo lógico conservan la implementación especializada de VIS-COPY-1/VIS-COPY-2 porque sus documentos tienen gramática y semántica propias.

## Reglas de transferencia

- Solo se pega entre proyectos del mismo `DiagramTypeId`.
- Una relación seleccionada arrastra sus extremos necesarios.
- Si se seleccionan dos nodos relacionados, la relación interna también queda incluida.
- Los IDs/códigos copiados se regeneran para evitar colisiones.
- El layout visual se desplaza al pegar para no quedar exactamente encima del original.
- Bendpoints, rutas y offsets de etiquetas se preservan cuando el tipo de diagrama los modela.
- El pegado entra al historial de edición cuando el workspace lo soporta.

## Fuera de alcance explícito

No se habilita esta operación para documentos que no son canvas visual transferible:

- Diccionario de datos.
- Levantamiento lógico.
- Roles/Permisos matricial.
- Recursos IA, documentos de ayuda, reportes y plantillas no abiertas como canvas.

Tampoco se generaliza todavía el botón de toolbar **Transferir** a todos los workspaces. VIS-COPY-2 lo mantiene para Grafo libre y Grafo lógico. La generalización de ese botón requiere que el shell pueda consultar el adapter/canvas activo de forma uniforme para todos los workspaces visuales.

## Próxima tanda sugerida

**VIS-COPY-4 — Transferir selección multi-diagrama desde toolbar**:

- exponer el portapapeles visual activo desde el workspace/canvas activo;
- listar proyectos abiertos compatibles por `DiagramTypeId`;
- reutilizar `ProjectVisualSelectionTransferService` para los tipos visuales soportados;
- mostrar mensajes diferenciados: sin selección, sin destino compatible, destino incompatible o transferencia correcta.

## Validación esperada

- Abrir dos proyectos del mismo tipo visual.
- Seleccionar con rectángulo nodos y relaciones.
- Ejecutar `Ctrl+C` en el origen.
- Activar el destino.
- Ejecutar `Ctrl+V`.
- Confirmar que aparecen copias con IDs nuevos, relaciones preservadas y layout desplazado.
- Repetir al menos en Modelo conceptual, UML Clases, Mapa de módulos, Flujo de pantallas y un diagrama C4.
