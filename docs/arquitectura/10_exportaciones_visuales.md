# Tanda 10 — Exportaciones visuales PNG/SVG/PDF

## Objetivo

Consolidar las salidas visuales para que lo exportado corresponda al contenido real del tab activo, no al viewport accidental ni a una captura parcial.

La regla de producto es:

> Si una vista final se puede revisar en pantalla, su exportación debe ser legible, completa y coherente con la naturaleza de esa vista.

## Clasificación de salidas

| Tipo de salida | Ruta esperada | Observación |
|---|---|---|
| Diagrama visual | PNG / SVG documental | PNG apunta a fidelidad visual del lienzo; SVG es vectorial documental, no WYSIWYG universal. |
| Matriz | PNG/Markdown tabular | No debe usar ruta de canvas libre. |
| Documento | PDF/Markdown documental | No debe pasar por snapshot de lienzo. |

## Decisiones aplicadas

1. **PNG de canvas por contenido**: `InteractiveCanvasPngExporter` ahora traduce el diagrama exportable hacia el origen de la imagen antes de hacer snapshot. Esto evita imágenes vacías cuando el contenido tiene coordenadas positivas, negativas o alejadas del viewport visible.
2. **Etiquetas incluidas en PNG**: las etiquetas de conectores se renderizan como parte del exportador común. La exportación usa `CanvasConnectorLabelPositioner`, por lo que respeta `labelOffsetX/Y` cuando el layout los contiene.
3. **Bounds robustos**: el área exportable considera tanto límites renderizados como límites semánticos del modelo. Así se reducen recortes por texto, etiquetas o símbolos compuestos.
4. **Modo limpio**: el exportador renderiza nodos y conectores como no seleccionados; no debe sacar handles, selección ni hitboxes visibles.
5. **Roles y permisos como matriz**: roles/permisos queda clasificado como `MATRIX`, no como `VISUAL_DIAGRAM`. Su PNG se delega a `MatrixSnapshotExporter`, separado de la ruta de canvas.
6. **Tab activo como fuente**: `ActiveOutputResolver` sigue resolviendo según el proyecto/tab activo. La salida de roles/permisos ahora comunica explícitamente que es matriz.
7. **SVG vectorial documental**: SVG representa el artefacto activo con formas y texto vectorial. No promete ser una copia WYSIWYG exacta del canvas JavaFX; la fidelidad visual exacta se revisa por PNG y smoke manual.

## Componentes relevantes

```txt
presentation/interactivecanvas/InteractiveCanvasPngExporter.java
presentation/interactivecanvas/CanvasExportSurface.java
presentation/exportable/MatrixSnapshotExporter.java
presentation/exportable/ExportableOutputDescriptor.java
presentation/exportable/ExportableOutputKind.java
presentation/exportable/ActiveOutputResolver.java
presentation/rolespermissions/RolesPermissionsMatrixView.java
```

## Exportación de canvas

La ruta común para diagramas visuales debe:

1. construir el modelo visible desde el adapter;
2. renderizar conectores, etiquetas y nodos;
3. aplicar CSS/layout en escena temporal;
4. calcular límites reales y semánticos;
5. agregar margen;
6. trasladar el diagrama para que el snapshot empiece en `(0,0)`;
7. exportar con fondo blanco explícito.

## Exportación de matriz

Roles y permisos no debe comportarse como grafo libre. Su PNG captura el nodo tabular completo mediante `MatrixSnapshotExporter`, con normalización de ruta y fondo blanco.

## Criterios de aceptación

- PNG de diagramas no depende del viewport actual.
- Las etiquetas de conectores aparecen en exportación limpia.
- Las etiquetas movidas por offset quedan reflejadas.
- La exportación no muestra selección ni handles de edición.
- Roles/permisos se anuncia como matriz y no como diagrama visual.
- Roles/permisos usa ruta de snapshot tabular para evitar PNG blanco.
- Los directorios destino se crean antes de escribir archivos.

## Pendientes para tandas posteriores

- Tanda 11 formaliza PDF/Markdown documental del diccionario.
- Tanda 12 formaliza roles/permisos como matriz/documento completo.
- Si se requiere PDF visual multipágina para diagramas gigantes, debe planificarse como mejora posterior y no mezclarse con esta tanda.
