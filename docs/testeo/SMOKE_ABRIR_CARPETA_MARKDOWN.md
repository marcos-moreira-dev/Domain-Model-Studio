# Smoke — Abrir carpeta Markdown

## Preparación

Crear una carpeta de prueba con:

```text
carpeta_prueba/
  01_comercial/
    01_levantamiento-logico.md
    notas_cliente.md
  02_inventario/
    02_grafo-logico.md
    03_diccionario-datos.md
  03_tecnico/
    04_uml-clases.md
    prompt_ia.md
  README.md
  logo.png
```

## Pasos

1. Abrir la aplicación.
2. Usar `Archivo > Abrir carpeta Markdown...` o el botón `Carpeta MD`.
3. Seleccionar `carpeta_prueba`.
4. Confirmar que se abren solo los Markdown de proyecto.
5. Confirmar que se recorren las subcarpetas y se abren los proyectos válidos.
6. Confirmar que `README.md`, `prompt_ia.md`, `notas_cliente.md` y `logo.png` no bloquean la operación.
7. Revisar el resumen y expandir el reporte detallado.
8. Copiar del reporte los Markdown no catalogados o rechazados para corregirlos con IA.
9. Reordenar tabs si hace falta.
10. Guardar un proyecto como `.dms`.
11. Reabrir el `.dms`.
12. Ejecutar `Exportar abiertos`.

## Criterios de aceptación

- Los proyectos válidos se abren en pestañas.
- Los archivos no-proyecto se ignoran sin detener el lote.
- Un archivo inválido se reporta con ruta relativa, estado y mensaje de error.
- El reporte permite copiar una lista útil para pedir corrección a IA.
- No se escribe nada automáticamente en la carpeta fuente.
- No se rompe importación individual de Markdown.
