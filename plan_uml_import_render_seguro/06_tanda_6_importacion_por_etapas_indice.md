# Tanda 6 — Importación por etapas con modelo intermedio

## Objetivo

Separar importar de pintar.

## Flujo

```text
Escanear
→ Parsear
→ Normalizar
→ Construir índice de código
→ Generar UML light
→ Abrir editor
→ Cargar detalles bajo demanda
```

## Posibles clases

- `SourceCodeImportIndex`
- `SourceTypeSummary`
- `SourceMemberSummary`
- `SourceRelationSummary`

## Resultado esperado

El editor abre rápido con un modelo navegable, sin cargar todo el detalle visual de golpe.
