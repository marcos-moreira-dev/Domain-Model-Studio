# Tanda 9 — Render por lotes en JavaFX

## Objetivo

Montar nodos JavaFX gradualmente.

## Ejemplo

```text
batch 1: 30 clases
batch 2: 30 clases
batch 3: 30 clases
```

Entre lotes se debe dejar respirar al hilo JavaFX con programación diferida.

## Resultado esperado

Menos congelamiento y progreso visible:

```text
Renderizando 90/296 clases...
```
