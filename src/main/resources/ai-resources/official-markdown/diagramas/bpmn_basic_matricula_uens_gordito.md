---
dms_version: "1"
diagram_type: "bpmn-basic"
name: "UENS — BPMN básico asignación estudiante-sección"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Proceso BPMN simplificado de Secretaría para registrar o actualizar estudiante y dejarlo asignado a una sección vigente."
---
# Proceso

Nombre: Asignación vigente estudiante-sección
Participantes: Representante legal, Secretaría, Sistema administrativo, Dirección

# Flujo

- inicio: Representante solicita inscripción o actualización de datos del estudiante.
- actividad: Secretaría busca estudiante existente.
- decisión: ¿Estudiante ya registrado?
- actividad: Registrar o actualizar datos del estudiante.
- actividad: Buscar o registrar representante legal.
- actividad: Consultar secciones activas y cupos disponibles.
- decisión: ¿Existe cupo en la sección solicitada?
- actividad: Asignar sección vigente al estudiante.
- actividad: Guardar cambios en estudiante.seccion_id y registrar auditoría.
- actividad: Entregar confirmación administrativa.
- fin: Estudiante queda registrado con asignación vigente o trámite observado.

# Excepciones

- Si no existe cupo, Secretaría propone otra sección o deriva a Dirección.
- Si faltan datos del representante, el trámite queda observado hasta completar información.
- Si el sistema detecta posible duplicado, Secretaría revisa antes de guardar.
- Nota de alcance: matrícula se usa aquí como nombre operativo del trámite; no existe tabla matrícula en la BD V2.
