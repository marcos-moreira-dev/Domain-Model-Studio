---
dms_version: "1"
diagram_type: "uml-class"
name: "UENS — UML clases dominio escolar"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "UML de clases alineado con entidades JPA/BD V2. La asignación vigente se expresa en Estudiante.seccionId; no existe clase persistente Matricula."
---
# Paquetes

## backend.secretaria
id: backend_secretaria
ruta: modules/estudiante-representante-seccion
propósito: estudiantes, representantes y asignación vigente a sección.

## backend.academico
id: backend_academico
ruta: modules/docente-asignatura-clase
propósito: docentes, asignaturas y clases ofertadas.

## backend.calificaciones
id: backend_calificaciones
ruta: modules/calificacion
propósito: notas por estudiante, clase y parcial.

## backend.reportes
id: backend_reportes
ruta: modules/reporte
propósito: solicitudes asíncronas y archivos generados.

## backend.seguridad
id: backend_seguridad
ruta: modules/usuario-auditoria-auth
propósito: usuarios administrativos, roles y auditoría.

# Clases

## EstudianteJpaEntity
paquete: backend_secretaria
responsabilidad: representa al estudiante persistido y su asignación vigente a representante/sección.
atributos:
- pkId: Long
- nombres: String
- apellidos: String
- fechaNacimiento: LocalDate
- estado: String
- representanteLegalId: Long
- seccionId: Long
métodos:
- activar(): void
- inactivar(): void
- asignarSeccion(seccionId): void

## RepresentanteLegalJpaEntity
paquete: backend_secretaria
responsabilidad: conserva datos del responsable legal.
atributos:
- pkId: Long
- nombres: String
- apellidos: String
- telefono: String
- correoElectronico: String
métodos:
- actualizarContacto(telefono, correoElectronico): void

## SeccionJpaEntity
paquete: backend_secretaria
responsabilidad: grupo por grado, paralelo, cupo y año lectivo.
atributos:
- pkId: Long
- grado: Integer
- paralelo: String
- cupoMaximo: Integer
- anioLectivo: String
- estado: String
métodos:
- tieneCupoDisponible(): boolean

## DocenteJpaEntity
paquete: backend_academico
responsabilidad: persona que puede impartir clases.
atributos:
- pkId: Long
- nombres: String
- apellidos: String
- telefono: String
- correoElectronico: String
- estado: String
métodos:
- puedeImpartir(): boolean

## AsignaturaJpaEntity
paquete: backend_academico
responsabilidad: materia definida por nombre, área y grado.
atributos:
- pkId: Long
- nombre: String
- area: String
- descripcion: String
- grado: Integer
- estado: String

## ClaseJpaEntity
paquete: backend_academico
responsabilidad: oferta concreta de una asignatura en una sección y horario.
atributos:
- pkId: Long
- diaSemana: String
- horaInicio: LocalTime
- horaFin: LocalTime
- estado: String
- seccionId: Long
- asignaturaId: Long
- docenteId: Long
métodos:
- asignarDocente(docenteId): void

## CalificacionJpaEntity
paquete: backend_calificaciones
responsabilidad: nota de un estudiante en una clase y parcial.
atributos:
- pkId: Long
- numeroParcial: Integer
- nota: BigDecimal
- fechaRegistro: LocalDate
- observacion: String
- estudianteId: Long
- claseId: Long
métodos:
- validarRango(): boolean

## ReporteSolicitudQueueJpaEntity
paquete: backend_reportes
responsabilidad: solicitud asíncrona de reporte y su ciclo de vida.
atributos:
- pkId: Long
- tipoReporte: String
- estado: String
- parametrosJson: String
- resultadoJson: String
- errorDetalle: String
- solicitadoPorUsuario: Long
- intentos: Integer
- fechaSolicitud: LocalDateTime
- fechaActualizacion: LocalDateTime
métodos:
- marcarEnProceso(): void
- marcarCompletada(resultadoJson): void
- marcarError(errorDetalle): void

## UsuarioSistemaAdministrativoJpaEntity
paquete: backend_seguridad
responsabilidad: cuenta autorizada para operar la aplicación.
atributos:
- pkId: Long
- nombreLogin: String
- passwordHash: String
- rol: String
- estado: String
métodos:
- puedeIngresar(): boolean

## AuditoriaEventoJpaEntity
paquete: backend_seguridad
responsabilidad: evidencia trazable de una acción relevante.
atributos:
- pkId: Long
- modulo: String
- accion: String
- entidad: String
- entidadId: String
- resultado: String
- actorLogin: String
- actorRol: String
- fechaEvento: LocalDateTime

# Relaciones

- RepresentanteLegalJpaEntity o-- EstudianteJpaEntity: representa
- SeccionJpaEntity o-- EstudianteJpaEntity: asignación vigente
- SeccionJpaEntity o-- ClaseJpaEntity: oferta clases
- AsignaturaJpaEntity --> ClaseJpaEntity: define materia
- DocenteJpaEntity --> ClaseJpaEntity: imparte opcionalmente
- EstudianteJpaEntity --> CalificacionJpaEntity: recibe nota
- ClaseJpaEntity --> CalificacionJpaEntity: evalúa
- UsuarioSistemaAdministrativoJpaEntity --> ReporteSolicitudQueueJpaEntity: solicita reporte
- UsuarioSistemaAdministrativoJpaEntity --> AuditoriaEventoJpaEntity: genera evento
- ReporteSolicitudQueueJpaEntity --> AuditoriaEventoJpaEntity: deja trazabilidad
