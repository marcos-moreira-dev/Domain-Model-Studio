---
dms_version: "1"
diagram_type: "technical-deployment"
name: "Plantilla — Despliegue técnico"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Despliegue técnico

> Plantilla importable para generar el diagrama visual y ajustar la entrega.
> Salida esperada: diagrama visual.

# Ambientes

- Desarrollo local
- Piloto
- Producción

# Nodos

- Equipo de usuario: ejecuta cliente o navegador.
- Servidor de aplicación: aloja servicios principales.
- Servidor de base de datos: conserva datos.

# Conexiones

- Equipo de usuario -> Servidor de aplicación: HTTPS o red local.
- Servidor de aplicación -> Servidor de base de datos: conexión privada.

