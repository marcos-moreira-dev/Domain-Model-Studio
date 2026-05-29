---
dms_version: "1"
diagram_type: "uml-state"
name: "UENS — UML estados solicitud de reporte"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Diagrama UML de estados basado en reporte_solicitud_queue, entidad con ciclo de vida explícito en la BD V2."
---
# Elemento observado

Nombre: Solicitud de reporte en cola

# Estados

- inicio
- pendiente
- en_proceso
- completada
- error
- fin

# Transiciones

- inicio -> pendiente: crear solicitud de reporte
- pendiente -> en_proceso: worker reclama solicitud disponible
- en_proceso -> completada: archivo XLSX/PDF/DOCX generado correctamente
- en_proceso -> error: falla generación o validación de parámetros
- error -> pendiente: reintentar solicitud permitida
- completada -> fin: descargar o archivar resultado
- error -> fin: cancelar o cerrar solicitud con evidencia

# Notas

Este ejemplo reemplaza el antiguo enfoque de estados de matrícula: no existe tabla matrícula ni entidad persistente de matrícula en la BD V2. El estado modelado corresponde a reporte_solicitud_queue.estado con PENDIENTE, EN_PROCESO, COMPLETADA y ERROR.
