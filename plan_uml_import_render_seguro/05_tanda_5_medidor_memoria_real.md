# Tanda 5 — Medidor de memoria real

## Objetivo

Usar memoria disponible real, no solo RAM instalada.

## Métricas deseadas

- RAM disponible del sistema;
- memoria máxima JVM;
- memoria usada JVM;
- memoria libre JVM;
- costo estimado de render;
- costo estimado de PNG.

## Resultado esperado

Si el proyecto es pesado, mostrar una advertencia como:

```text
RAM disponible: 6.8 GB
Máximo JVM: 2.0 GB
Render estimado: alto
Se abrirá en modo ligero.
```
