# Smoke — Canonización documental enterprise

## Preparación

Crear una carpeta raíz con:

```text
01_levantamiento-logico.md
02_grafo-logico.md
03_diccionario-datos.md
04_modelo-conceptual.md
05_uml-clases.md
README.md
prompt_usado.md
notas_cliente.md
logo.png
```

Los archivos de proyecto deben declarar `diagram_type` válido e `importable: true`.

## Pasos

1. Abrir Domain Model Studio.
2. Ejecutar **Archivo > Abrir carpeta Markdown...** o **Carpeta MD**.
3. Seleccionar la carpeta raíz.
4. Confirmar que se abren solo los proyectos compatibles.
5. Confirmar que README, prompt, notas e imagen se omiten.
6. Revisar el resumen de importación.
7. Verificar la sección **Canonización documental**.
8. Confirmar que el estado indica fuente lógica canónica única cuando existe un solo Levantamiento lógico.
9. Reordenar tabs para agrupar Levantamiento lógico, Grafo lógico y Diccionario.
10. Exportar proyectos abiertos.

## Criterios

- No se modifica la carpeta fuente.
- No se abren archivos auxiliares como proyectos.
- El resumen detecta el Levantamiento lógico.
- Los artefactos derivados se agrupan por rol.
- Los rechazos se reportan por archivo sin tumbar el lote.
