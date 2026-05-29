---
dms_version: "1"
diagram_type: "uml-state"
name: "Plantilla — UML Estados"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# UML Estados

> Plantilla oficial importable: la IA puede generar Markdown equivalente y la aplicación puede renderizarlo como diagrama visual.

# Elemento observado

Nombre: Orden o solicitud

# Estados

- borrador
- pendiente
- aprobada
- rechazada
- cerrada

# Transiciones

- borrador -> pendiente: enviar
- pendiente -> aprobada: aprobar
- pendiente -> rechazada: rechazar
- aprobada -> cerrada: cerrar

