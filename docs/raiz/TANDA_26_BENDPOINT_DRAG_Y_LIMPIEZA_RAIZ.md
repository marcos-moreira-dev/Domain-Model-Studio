# Tanda 26 — Drag de puntos intermedios y limpieza de Markdown raíz

## Estado

Aplicada.

## Objetivo

Cerrar dos detalles de producto después de la estabilización de relaciones editables:

1. Permitir arrastrar puntos intermedios de relaciones sin que el canvas se reconstruya durante el gesto.
2. Limpiar la carpeta raíz moviendo la documentación Markdown viva a una carpeta dedicada.

## Cambios funcionales

### Drag de puntos intermedios

Se ajustó `CanvasBendPointHandleRenderer` para que el handle se mueva visualmente durante `MOUSE_DRAGGED` y el refresco completo del canvas ocurra únicamente al soltar el mouse.

La regla queda así:

```txt
mouse pressed  -> selecciona punto y toma foco
mouse dragged  -> mueve visualmente el punto y actualiza el modelo
mouse released -> refresca el canvas y redibuja la relación completa
```

Esto evita que `refreshPreservingViewport` destruya el handle durante el mismo gesto de arrastre.

## Cambios documentales

La raíz ya no conserva archivos `.md` dispersos. Los Markdown raíz vivos se movieron a:

```txt
docs/raiz/
```

Se actualizaron guardarraíles y referencias que todavía apuntaban a documentos Markdown en la raíz.

## No se tocó

- Modelo conceptual.
- Parser de UML Clases.
- Inferencia de composición/agregación.
- Semántica de relaciones.
- Render de conectores salvo el drag de sus puntos intermedios.
