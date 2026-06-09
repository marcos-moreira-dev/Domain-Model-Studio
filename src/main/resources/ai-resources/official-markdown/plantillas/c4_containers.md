---
dms_version: "1"
diagram_type: "c4-containers"
name: "Plantilla — C4 Contenedores"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# C4 Contenedores

> Plantilla importable para generar el diagrama visual y ajustar la entrega.
> Salida esperada: diagrama visual.

# Contenedores

- Aplicación web: interfaz de usuario.
- API backend: reglas y servicios del sistema.
- Base de datos: información persistente.
- Servicio de reportes: generación de documentos.

# Relaciones

- Aplicación web -> API backend: solicitudes HTTP
- API backend -> Base de datos: lectura y escritura
- API backend -> Servicio de reportes: solicita documentos

