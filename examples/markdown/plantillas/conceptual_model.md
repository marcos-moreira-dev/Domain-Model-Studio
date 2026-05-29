---
dms_version: "1"
diagram_type: "conceptual-model"
name: "Plantilla — Modelo conceptual"
sample_kind: "template"
domain: "general"
status: "importable-template"
importable: true
intended_output: "diagrama visual"
---
# Modelo conceptual

> Plantilla importable por el modelo conceptual actual cuando se completen entidades y relaciones reales.
> Salida esperada: diagrama visual.

# Entidades

## NombreEntidad
id: nombre_entidad
module: modulo_principal
description: Describe qué representa esta entidad en el negocio.

- pk id
- campo_obligatorio
- campo_opcional [optional]
- campo_derivado [derived]

# Relaciones

## NombreRelacion
id: nombre_relacion
from: NombreEntidad
to: NombreEntidad
from_cardinality: 1
to_cardinality: 0..M
description: Explica la regla de negocio de la relación.

