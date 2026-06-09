---
dms_version: "1"
diagram_type: "data-dictionary"
client: ""
organization: ""
author: ""
logo_reference: ""
introduction: ""
name: "Plantilla — Diccionario de datos"
sample_kind: "template"
domain: "general"
status: "importable-template"
importable: true
intended_output: "documento documental"
---
# Diccionario de datos

> Plantilla importable por la aplicación como borrador documental una vez completada con información real.
> Salida esperada: documento documental.

# Diccionario de datos

## Entidad o tabla
Propósito: qué información conserva.
Responsable del dato: área o rol responsable.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| id | entero/uuid | sí | único | Identificador interno. |
| nombre | texto | sí | no vacío | Nombre visible para usuarios. |

# Reglas generales

- Registrar validaciones, formatos y restricciones humanas.
- Mantener términos del negocio, no nombres internos de código.

