---
dms_version: "1"
diagram_type: "data-dictionary"
client: "Unidad Educativa Niñitos Soñadores"
organization: "UENS"
author: "Domain Model Studio"
logo_reference: ""
introduction: "Diccionario basado en la base PostgreSQL V2 3FN de UENS. Documenta tablas físicas, campos, restricciones y reglas operativas sin inventar entidades no persistentes."
name: "UENS — diccionario de datos escolar V2"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "documento PDF/Markdown"
description: "Diccionario documental alineado con la BD V2 3FN: usuarios, representantes, secciones, docentes, asignaturas, estudiantes, clases, calificaciones, reportes y auditoría."
---
# Diccionario de datos

## usuario_sistema_administrativo
Propósito: almacenar cuentas administrativas autenticables para la aplicación desktop/API.
Responsable del dato: Administración.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador interno. |
| nombre_login | VARCHAR(80) | sí | único | Login usado para autenticación. |
| password_hash | TEXT | sí | hash seguro | Nunca guardar contraseña plana. |
| rol | VARCHAR(20) | sí | ADMIN/SECRETARIA | Roles implementados en fase actual. |
| estado | VARCHAR(10) | sí | ACTIVO/INACTIVO | Solo ACTIVO puede operar. |

## representante_legal
Propósito: conservar datos de contacto y responsabilidad legal del estudiante.
Responsable del dato: Secretaría académica.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador interno. |
| nombres | VARCHAR(120) | sí | no vacío | Nombres legales. |
| apellidos | VARCHAR(120) | sí | no vacío | Apellidos legales. |
| telefono | VARCHAR(30) | no | formato local | Puede cambiar con frecuencia. |
| correo_electronico | VARCHAR(254) | no | correo válido | Medio de contacto opcional. |

## seccion
Propósito: organizar estudiantes por grado, paralelo, cupo y año lectivo.
Responsable del dato: Secretaría académica.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador de sección. |
| grado | SMALLINT | sí | 1..7 | Grado escolar. |
| paralelo | VARCHAR(10) | sí | no vacío | Paralelo o código interno. |
| cupo_maximo | SMALLINT | sí | >0 y <=35 | Control de cupos. |
| anio_lectivo | VARCHAR(20) | sí | YYYY-YYYY | Ejemplo: 2025-2026. |
| estado | VARCHAR(10) | sí | ACTIVO/INACTIVO | Solo se asigna a secciones activas. |
| uq_seccion_unica | restricción | sí | único(anio_lectivo, grado, paralelo) | Evita duplicar secciones equivalentes. |

## docente
Propósito: registrar personas que pueden impartir clases o apoyar planificación académica.
Responsable del dato: Coordinación académica.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador interno. |
| nombres | VARCHAR(120) | sí | no vacío | Nombres legales. |
| apellidos | VARCHAR(120) | sí | no vacío | Apellidos legales. |
| telefono | VARCHAR(30) | no | formato local | Contacto opcional. |
| correo_electronico | VARCHAR(254) | no | correo válido | Contacto institucional o personal. |
| estado | VARCHAR(10) | sí | ACTIVO/INACTIVO | Solo docentes activos se asignan a clases. |

## asignatura
Propósito: mantener el catálogo académico de materias por grado.
Responsable del dato: Coordinación académica.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador interno. |
| nombre | VARCHAR(120) | sí | único por grado | Ejemplo: Matemática. |
| area | VARCHAR(80) | no | texto controlado | Agrupa materias por área. |
| descripcion | TEXT | no | longitud razonable | Explicación opcional. |
| grado | SMALLINT | sí | 1..7 | Fase 1: asignatura por grado. |
| estado | VARCHAR(10) | sí | ACTIVO/INACTIVO | No borrar asignaturas históricas. |

## estudiante
Propósito: registrar al niño y su asignación vigente a representante y sección.
Responsable del dato: Secretaría académica.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador interno. |
| nombres | VARCHAR(120) | sí | no vacío | Nombres legales. |
| apellidos | VARCHAR(120) | sí | no vacío | Apellidos legales. |
| fecha_nacimiento | DATE | sí | fecha válida | La edad se deriva; no se almacena. |
| estado | VARCHAR(10) | sí | ACTIVO/INACTIVO | Control operativo. |
| representante_legal_id | BIGINT | sí | FK representante_legal(pk_id) | Representante obligatorio. |
| seccion_id | BIGINT | no | FK seccion(pk_id) | Asignación vigente estudiante-sección. No existe tabla matrícula en V2. |

## clase
Propósito: representar una asignatura ofertada en una sección y horario concretos.
Responsable del dato: Coordinación académica.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador interno. |
| dia_semana | VARCHAR(15) | sí | LUNES..SABADO | Día de clase. |
| hora_inicio | TIME | sí | menor que hora_fin | Inicio del bloque. |
| hora_fin | TIME | sí | mayor que hora_inicio | Fin del bloque. |
| estado | VARCHAR(10) | sí | ACTIVO/INACTIVO | Control operativo. |
| seccion_id | BIGINT | sí | FK seccion(pk_id) | Sección que recibe la clase. |
| asignatura_id | BIGINT | sí | FK asignatura(pk_id) | Materia dictada. |
| docente_id | BIGINT | no | FK docente(pk_id) | Docente opcional en fase inicial. |
| uq_clase_operativa | restricción | sí | único(seccion, asignatura, día, inicio, fin) | Evita duplicar el mismo bloque académico. |

## calificacion
Propósito: registrar nota por estudiante, clase y parcial.
Responsable del dato: Secretaría/Administración según política institucional.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador interno. |
| numero_parcial | SMALLINT | sí | 1 o 2 | Fase 1: dos parciales. |
| nota | NUMERIC(5,2) | sí | 0..10 | Rango de nota. |
| fecha_registro | DATE | no | fecha válida | Trazabilidad académica. |
| observacion | TEXT | no | longitud razonable | Comentario administrativo. |
| estudiante_id | BIGINT | sí | FK estudiante(pk_id) | Estudiante evaluado. |
| clase_id | BIGINT | sí | FK clase(pk_id) | Clase evaluada. |
| uq_calificacion_estudiante_clase_parcial | restricción | sí | único(estudiante, clase, parcial) | Evita dos notas para el mismo parcial. |

## reporte_solicitud_queue
Propósito: administrar generación asíncrona de reportes XLSX, PDF o DOCX.
Responsable del dato: Administración/Reportes.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador de solicitud. |
| tipo_reporte | VARCHAR(100) | sí | catálogo de reporte | Ejemplos: LISTADO_ESTUDIANTES_POR_SECCION, CALIFICACIONES_POR_SECCION_Y_PARCIAL, AUDITORIA_ADMIN_OPERACIONES. |
| estado | VARCHAR(20) | sí | PENDIENTE/EN_PROCESO/COMPLETADA/ERROR | Ciclo de vida de la solicitud. |
| parametros_json | TEXT | no | JSON controlado | Filtros de reporte. |
| resultado_json | TEXT | no | JSON controlado | Rutas/metadatos del resultado. |
| error_detalle | TEXT | no | solo si falla | Diagnóstico de generación. |
| solicitado_por_usuario | BIGINT | no | usuario existente | Usuario que solicitó el reporte. |
| intentos | INTEGER | sí | >=0 | Reintentos del worker. |
| fecha_solicitud | TIMESTAMP | sí | timestamp | Creación de solicitud. |
| fecha_actualizacion | TIMESTAMP | sí | timestamp | Último cambio de estado. |

## auditoria_evento
Propósito: conservar trazabilidad de acciones relevantes, errores y advertencias.
Responsable del dato: Administración.

| Campo | Tipo esperado | Obligatorio | Regla | Observación |
|---|---|---:|---|---|
| pk_id | BIGINT | sí | PK identity | Identificador interno. |
| modulo | VARCHAR(80) | sí | no vacío | Módulo funcional. |
| accion | VARCHAR(120) | sí | no vacío | Acción auditada. |
| entidad | VARCHAR(120) | no | nombre técnico | Ejemplo: estudiante, calificacion, reporte_solicitud_queue. |
| entidad_id | VARCHAR(120) | no | referencia interna | Identificador afectado. |
| resultado | VARCHAR(20) | sí | EXITO/ERROR/INFO/ADVERTENCIA | Resultado de la acción. |
| detalle | TEXT | no | longitud razonable | Contexto de auditoría. |
| request_id | VARCHAR(64) | no | trazabilidad técnica | Correlación de petición. |
| ip_origen | VARCHAR(64) | no | IP o equipo | Origen de operación. |
| actor_usuario_id | BIGINT | no | FK usuario_sistema_administrativo(pk_id) | Usuario autenticado cuando aplica. |
| actor_login | VARCHAR(80) | no | login visible | Copia útil para auditoría histórica. |
| actor_rol | VARCHAR(30) | no | ADMIN/SECRETARIA | Rol del actor. |
| fecha_evento | TIMESTAMP | sí | timestamp | Momento del evento. |
