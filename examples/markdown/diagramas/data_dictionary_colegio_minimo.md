---
dms_version: "1"
diagram_type: "data-dictionary"
client: ""
organization: ""
author: ""
logo_reference: ""
introduction: ""
name: "Diccionario de datos — colegio mínimo"
sample_kind: "minimal"
domain: "colegio"
status: "importable"
importable: true
intended_output: "documento PDF/Markdown"
---
# Diccionario de datos

## Estudiante
Propósito: registrar alumnos del colegio.
Responsable del dato: Secretaría académica.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| id | entero | sí | único | Identificador del estudiante. |
| nombres | texto | sí | no vacío | Nombres legales. |
| apellidos | texto | sí | no vacío | Apellidos legales. |
| fecha_nacimiento | fecha | sí | fecha válida | Permite derivar edad. |
| estado | catálogo | sí | ACTIVO/INACTIVO | No borrar históricos. |

## Representante legal
Propósito: registrar contacto responsable del estudiante.
Responsable del dato: Secretaría académica.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| id | entero | sí | único | Identificador interno. |
| nombres | texto | sí | no vacío | Nombre del representante. |
| teléfono | texto | no | formato local | Puede cambiar con frecuencia. |
| correo | texto | no | correo válido | Usado para avisos. |
