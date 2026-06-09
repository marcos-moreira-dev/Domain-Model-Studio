---
dms_version: "1"
diagram_type: "c4-context"
name: "Plantilla — C4 Contexto"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# C4 Contexto

> Plantilla importable para generar el diagrama visual y ajustar la entrega.
> Salida esperada: diagrama visual.

# Contexto

Sistema: Sistema administrativo
Propósito: apoyar operaciones internas y reportes.

# Personas

- Administrador
- Operador
- Cliente externo

# Sistemas externos

- Servicio de correo
- Pasarela de pagos

# Relaciones

- Administrador -> Sistema administrativo: configura y revisa
- Sistema administrativo -> Servicio de correo: envía notificaciones

