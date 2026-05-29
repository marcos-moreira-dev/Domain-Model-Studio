---
dms_version: "1"
diagram_type: "admin-module-map"
name: "UENS — mapa de módulos escolar"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Mapa funcional basado en módulos reales del backend y vistas reales del desktop UENS."
---
# Módulos

## Seguridad y autenticación
id: seguridad
responsabilidad: autentica usuarios administrativos y controla acceso por rol implementado.
descripción: cubre login, sesión, JWT, usuarios ADMIN/SECRETARIA y permisos de navegación.

### Submódulos
- auth: ingreso, refresh token y protección contra intentos fallidos.
- usuarios: alta, bloqueo y consulta de usuarios administrativos.
- sesiones: estado de sesión en desktop.
- roles: ADMIN y SECRETARIA como roles implementados.

## Secretaría académica
id: secretaria
responsabilidad: registra estudiantes, representantes, secciones y asignación vigente estudiante-sección.
descripción: núcleo de operación diaria de la escuelita; no existe tabla matrícula en V2.

### Submódulos
- estudiantes: alta, edición, búsqueda y estado del estudiante.
- representantes: contacto y responsabilidad legal.
- secciones: grado, paralelo, cupo y año lectivo.
- asignacion_seccion: operación que actualiza estudiante.seccion_id.

## Gestión académica
id: academico
responsabilidad: define docentes, asignaturas y clases ofertadas por sección.
descripción: organiza la oferta académica y habilita registro de calificaciones.

### Submódulos
- docentes: datos, estado y contacto.
- asignaturas: catálogo de materias por grado y área.
- clases: día, horario, sección, asignatura y docente opcional.

## Calificaciones
id: calificaciones
responsabilidad: registra y consulta notas por estudiante, clase y parcial.

### Submódulos
- registro_notas: captura de nota, parcial y observación.
- consulta_notas: búsqueda por estudiante, clase o sección.
- validacion_rango: control de nota 0..10 y parcial 1/2.

## Reportes
id: reportes
responsabilidad: gestiona solicitudes asíncronas y descarga de archivos XLSX, PDF o DOCX.

### Submódulos
- cola_reportes: estados PENDIENTE, EN_PROCESO, COMPLETADA y ERROR.
- reportes_estudiantes: listado por sección.
- reportes_calificaciones: calificaciones por sección y parcial.
- reportes_auditoria: operaciones administrativas para ADMIN.

## Auditoría
id: auditoria
responsabilidad: registra y consulta eventos relevantes del sistema.

### Submódulos
- eventos: acciones, entidad, actor, resultado y fecha.
- consultas: filtros por módulo, acción, resultado, actor y fecha.
- trazabilidad_reportes: evidencia de generación, error y descarga.

## Dashboard y consulta académica
id: dashboard_consulta
responsabilidad: consolida indicadores y consultas de apoyo para operación administrativa.

### Submódulos
- dashboard: tarjetas e indicadores iniciales.
- consulta_academica: consultas agregadas para estudiantes, secciones y calificaciones.

# Dependencias

- seguridad -> secretaria: restringe operaciones de estudiantes, representantes y secciones.
- seguridad -> calificaciones: restringe registro y consulta de notas.
- seguridad -> reportes: controla solicitudes y descargas por rol.
- secretaria -> academico: necesita secciones y clases para asignar estudiantes correctamente.
- academico -> calificaciones: la clase definida habilita calificaciones por parcial.
- secretaria -> reportes: alimenta reportes de estudiantes por sección.
- calificaciones -> reportes: alimenta reportes académicos.
- reportes -> auditoria: registra solicitudes, errores y descargas relevantes.
- auditoria -> dashboard_consulta: provee indicadores de operación y trazabilidad.
