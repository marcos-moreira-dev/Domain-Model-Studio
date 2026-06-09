# IMP-14 — Smoke visual de ejemplos UENS

## Decisión de dominio

UENS se trata como **unidad educativa**. La familia de ejemplos oficiales usa ese dominio escolar para que el usuario pueda comparar todos los tipos de diagrama sobre un mismo sistema administrativo.

## Contrato probado

La nueva prueba de regresión `VisualExportCapabilityCoherenceTest` no revisa belleza visual. Revisa que la promesa técnica mínima no sea fachada:

```text
Markdown oficial → importador → layout visual → movimiento → JSON .dms → reapertura → SVG vectorial
```

## Tipos importables cubiertos

```text
conceptual-model
admin-module-map
roles-permissions-map
screen-flow
admin-wireframes
uml-class
c4-context
c4-containers
technical-deployment
bpmn-basic
operational-flow
uml-use-case
uml-activity
uml-sequence
uml-state
```

`data-dictionary` queda fuera del smoke visual importable porque actualmente es referencia/documento técnico, no canvas libre ni importador editable desde Markdown oficial.

## Protección anti-fachada

El smoke exige que el SVG exportado:

```text
- contenga <svg>;
- tenga tamaño no trivial;
- no contenga data:image;
- no contenga <image>.
```

Esto protege contra falsos SVG basados en capturas raster.

## Protección de persistencia

El smoke mueve el primer nodo del layout activo, serializa con `DmsProjectJsonWriter`, reabre con `DmsProjectJsonReader` y compara coordenadas con tolerancia pequeña. Esto protege contra layouts que solo existan en pantalla y no sobrevivan al `.dms`.

## Nota sobre roles/permisos

Roles/permisos se mantiene como matriz estructurada. El test puede preparar layout visual para la persistencia general, pero el SVG público se enruta al exportador de matriz, no al grafo genérico.
