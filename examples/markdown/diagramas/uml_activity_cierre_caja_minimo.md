---
dms_version: "1"
diagram_type: "uml-activity"
name: "UML Actividad — cierre de caja mínimo"
sample_kind: "minimal"
domain: "caja"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Actividad

Nombre: Cierre de caja

# Flujo

- inicio
- acción: Consultar ventas del día.
- acción: Contar efectivo disponible.
- decisión: ¿Cuadra con el sistema?
- acción: Registrar cierre.
- acción: Generar reporte.
- fin

# Excepciones

- Si no cuadra, registrar observación y solicitar revisión.
