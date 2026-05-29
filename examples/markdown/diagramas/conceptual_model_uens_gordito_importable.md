---
dms_version: "1"
diagram_type: "conceptual-model"
name: "UENS — modelo conceptual escolar completo"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
notation: "chen"
description: "Modelo conceptual del dominio escolar UENS. Aplica teoría ER/conceptual: entidades, atributos, relaciones y cardinalidades del negocio, sin copiar ciegamente la base física."
---
# Entidades

## Estudiante
id: estudiante
module: academico
description: Niño registrado en la institución; puede tener una asignación vigente a sección durante el año lectivo.

- pk id
- nombres
- apellidos
- fecha_nacimiento
- estado

## RepresentanteLegal
id: representante_legal
module: academico
description: Adulto responsable del estudiante para contacto, autorizaciones y seguimiento administrativo.

- pk id
- nombres
- apellidos
- telefono [optional]
- correo_electronico [optional]

## Seccion
id: seccion
module: academico
description: Grupo académico definido por grado, paralelo, cupo y año lectivo.

- pk id
- grado
- paralelo
- anio_lectivo
- cupo_maximo
- estado

## Docente
id: docente
module: academico
description: Persona registrada para impartir clases o acompañar planificación académica.

- pk id
- nombres
- apellidos
- telefono [optional]
- correo_electronico [optional]
- estado

## Asignatura
id: asignatura
module: academico
description: Materia definida por nombre, área y grado.

- pk id
- nombre
- area [optional]
- grado
- estado

## Clase
id: clase
module: academico
description: Oferta concreta de una asignatura en una sección, con horario y docente opcional.

- pk id
- dia_semana
- hora_inicio
- hora_fin
- estado

## Calificacion
id: calificacion
module: calificaciones
description: Nota registrada para un estudiante dentro de una clase y un parcial.

- pk id
- numero_parcial
- nota
- fecha_registro [optional]
- observacion [optional]

## UsuarioSistemaAdministrativo
id: usuario_sistema_administrativo
module: seguridad
description: Cuenta autorizada para ingresar al sistema administrativo con rol implementado ADMIN o SECRETARIA.

- pk id
- nombre_login
- password_hash
- rol
- estado

## ReporteSolicitudQueue
id: reporte_solicitud_queue
module: reportes
description: Solicitud asíncrona de reporte académico, administrativo o de auditoría.

- pk id
- tipo_reporte
- estado
- parametros_json [optional]
- resultado_json [optional]
- error_detalle [optional]
- intentos
- fecha_solicitud
- fecha_actualizacion

## AuditoriaEvento
id: auditoria_evento
module: auditoria
description: Evento trazable de una acción administrativa relevante, con actor, resultado y detalle.

- pk id
- modulo
- accion
- entidad [optional]
- entidad_id [optional]
- resultado
- fecha_evento
- detalle [optional]

# Relaciones

## Representa
id: representa
from: RepresentanteLegal
to: Estudiante
from_cardinality: 1
to_cardinality: 0..M
description: Un representante legal puede responsabilizarse por varios estudiantes; cada estudiante requiere un representante en la fase actual.

## PerteneceASeccionVigente
id: pertenece_seccion_vigente
from: Seccion
to: Estudiante
from_cardinality: 0..1
to_cardinality: 0..M
description: La asignación vigente se expresa como estudiante.seccion_id; no existe tabla matrícula en la versión V2.

## OfertaClase
id: oferta_clase
from: Seccion
to: Clase
from_cardinality: 1
to_cardinality: 0..M
description: Una sección puede ofertar varias clases dentro de su año lectivo.

## ClaseDeAsignatura
id: clase_de_asignatura
from: Asignatura
to: Clase
from_cardinality: 1
to_cardinality: 0..M
description: Cada clase concreta corresponde a una asignatura.

## ImparteClase
id: imparte_clase
from: Docente
to: Clase
from_cardinality: 0..1
to_cardinality: 0..M
description: La clase puede tener un docente asignado; en fase inicial esta asignación puede estar pendiente.

## RegistraCalificacion
id: registra_calificacion
from: Estudiante
to: Calificacion
from_cardinality: 1
to_cardinality: 0..M
description: Un estudiante puede tener calificaciones por clase y parcial.

## EvaluaClase
id: evalua_clase
from: Clase
to: Calificacion
from_cardinality: 1
to_cardinality: 0..M
description: La calificación se registra dentro de una clase específica.

## SolicitaReporte
id: solicita_reporte
from: UsuarioSistemaAdministrativo
to: ReporteSolicitudQueue
from_cardinality: 0..1
to_cardinality: 0..M
description: Un usuario administrativo puede solicitar reportes asíncronos; la solicitud conserva estado y parámetros.

## GeneraAuditoria
id: genera_auditoria
from: UsuarioSistemaAdministrativo
to: AuditoriaEvento
from_cardinality: 0..1
to_cardinality: 0..M
description: Las acciones relevantes quedan asociadas al usuario cuando existe actor autenticado.
