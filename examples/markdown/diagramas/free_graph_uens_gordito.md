---
dms_version: "1"
diagram_type: "free-graph"
name: "UENS — grafo libre de relaciones escolares"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
graph_kind: "mixed"
importable: true
intended_output: "diagrama visual"
description: "Grafo libre para conversación inicial. No reemplaza UML, C4, BPMN, diccionario ni grafo lógico."
---
# Nodos

## Estudiante
id: estudiante
contenido: Persona registrada que participa en clases, evaluaciones y procesos administrativos.

## RepresentanteLegal
id: representante_legal
contenido: Responsable de contacto, autorización y seguimiento del estudiante.

## Secretaría
id: secretaria
contenido: Área que registra datos, actualiza estudiantes y asigna sección vigente.

## Sección
id: seccion
contenido: Grupo académico definido por grado, paralelo, cupo y año lectivo.

## Asignación vigente
id: asignacion_vigente
contenido: Vínculo operativo entre estudiante y sección, persistido como estudiante.seccion_id, no como tabla matrícula.

## Clase
id: clase
contenido: Oferta de asignatura en una sección, horario y docente opcional.

## Calificación
id: calificacion
contenido: Resultado académico registrado por clase y parcial.

## ReporteSolicitud
id: reporte_solicitud
contenido: Solicitud asíncrona de reporte con estado PENDIENTE, EN_PROCESO, COMPLETADA o ERROR.

## Auditoría
id: auditoria
contenido: Registro de cambios relevantes para trazabilidad humana.

## Usuario administrativo
id: usuario_admin
contenido: Cuenta ADMIN o SECRETARIA que opera el sistema.

# Relaciones

- representante_legal -> estudiante: responde por
- secretaria -> estudiante: registra o actualiza
- estudiante -> asignacion_vigente: tiene
- asignacion_vigente -> seccion: apunta a
- seccion -> clase: oferta
- clase -> calificacion: habilita
- calificacion -> estudiante: evalúa a
- usuario_admin -> reporte_solicitud: solicita
- reporte_solicitud -> calificacion: consulta datos de
- usuario_admin -> auditoria: genera eventos
- auditoria -> calificacion: conserva correcciones
- auditoria -> reporte_solicitud: conserva generación y errores
- secretaria -- representante_legal: intercambia información

# Observaciones

Este ejemplo solo ofrece una vista flexible de conversación. Para documentación formal usar modelo conceptual, diccionario, BPMN, UML, C4 o grafo lógico según corresponda.
