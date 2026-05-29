---
dms_version: "1"
diagram_type: "roles-permissions-map"
name: "Plantilla — Roles y permisos"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "matriz visual"
---
# Roles y permisos

> Plantilla oficial importable por la aplicación.
> Salida esperada: matriz visual.

# Roles

## Rol operativo
id: rol_operativo
propósito: Qué puede hacer esta persona.

# Permisos

- permiso_crear: Crear registros del área asignada.
- permiso_consultar: Consultar información permitida.

# Asignaciones

| Rol | Permiso | Decisión | Alcance | Observación |
|---|---|---|---|---|
| rol_operativo | permiso_consultar | Permitido | propio módulo | No incluye administración global. |

