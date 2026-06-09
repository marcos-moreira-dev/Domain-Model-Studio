# Tanda 12 — Protección de exportación PNG/SVG

## Objetivo

Evitar exportaciones que revienten memoria.

## Cálculo PNG bruto

```text
bytes = ancho × alto × 4
```

## Mensaje esperado

```text
La imagen completa sería de 28000 x 16000 px.
Memoria bruta aproximada: 1.79 GB.
Se recomienda exportar por vista o usar SVG.
```

## Resultado esperado

Permitir exportar vista activa, módulo seleccionado, SVG o cancelar.
