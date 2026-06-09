---
dms_version: "1"
diagram_type: "conceptual-model"
name: "Modelo conceptual — colegio mínimo importable"
sample_kind: "minimal"
domain: "colegio"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Entidades

## Estudiante
id: estudiante
module: academico
description: Persona matriculada o registrada en el colegio.

- pk id
- nombres
- apellidos
- estado

## RepresentanteLegal
id: representante_legal
module: academico
description: Persona responsable del estudiante.

- pk id
- nombres
- telefono [optional]

# Relaciones

## Representa
id: representa
from: RepresentanteLegal
to: Estudiante
from_cardinality: 1
to_cardinality: 0..M
description: Un representante puede tener varios estudiantes representados.
