---
dms_version: "1"
diagram_type: "uml-activity"
name: "UENS — UML actividad asignar estudiante a sección"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Actividad UML para el flujo de acciones de Secretaría al registrar o actualizar estudiante y su asignación vigente a sección."
---
# Actividad

Nombre: Asignar estudiante a sección vigente

# Carriles

- carril: Secretaría académica
- carril: Sistema administrativo
- carril: Dirección

# Flujo

- inicio
- acción: Recibir solicitud del representante legal.
- acción: Buscar estudiante por nombres, apellidos y fecha de nacimiento.
- decisión: ¿Existe estudiante?
- acción: Crear o actualizar datos del estudiante.
- acción: Buscar o registrar representante legal.
- acción: Seleccionar año lectivo y sección.
- decisión: ¿Sección activa con cupo disponible?
- acción: Actualizar asignación vigente del estudiante.
- acción: Guardar estudiante.seccion_id.
- acción: Registrar evento de auditoría.
- acción: Mostrar confirmación o advertencia.
- fin

# Excepciones

- Si hay posible duplicado, solicitar revisión antes de guardar.
- Si no hay cupo, elegir otra sección o escalar a Dirección.
- Si falta representante, no cerrar la operación como completa.
- Nota: “matrícula” puede aparecer como trámite operativo, pero no como tabla persistente en V2.
