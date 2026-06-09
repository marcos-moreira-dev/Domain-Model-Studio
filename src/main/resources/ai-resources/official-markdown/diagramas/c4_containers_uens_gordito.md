---
dms_version: "1"
diagram_type: "c4-containers"
name: "UENS — C4 contenedores sistema escolar"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "C4 Contenedores: desktop JavaFX, API Spring Boot, PostgreSQL, worker de reportes y almacenamiento de archivos."
---
# Contenedores

- Aplicación desktop JavaFX: interfaz administrativa para usuarios ADMIN y SECRETARIA.
- API backend Spring Boot: expone endpoints, validaciones, seguridad, auditoría y reglas de aplicación.
- Base de datos PostgreSQL: persiste usuarios, representantes, secciones, docentes, asignaturas, estudiantes, clases, calificaciones, reportes y auditoría.
- Cola/worker de reportes: procesa reporte_solicitud_queue y genera archivos bajo demanda.
- Servicio de archivos de reportes: almacena archivos XLSX, PDF y DOCX generados.
- Seguridad JWT: emisión y validación de tokens para solicitudes autenticadas.

# Relaciones

- Aplicación desktop JavaFX -> API backend Spring Boot: consume endpoints HTTP autenticados.
- API backend Spring Boot -> Seguridad JWT: genera y valida tokens de sesión.
- API backend Spring Boot -> Base de datos PostgreSQL: lectura y escritura transaccional.
- API backend Spring Boot -> Cola/worker de reportes: crea solicitudes PENDIENTE y consulta su estado.
- Cola/worker de reportes -> Base de datos PostgreSQL: lee datos y actualiza estado de solicitudes.
- Cola/worker de reportes -> Servicio de archivos de reportes: escribe XLSX, PDF o DOCX generado.
- Aplicación desktop JavaFX -> Servicio de archivos de reportes: descarga archivos autorizados a través de la API.

# Notas de alcance

- No se modela correo institucional como contenedor implementado; queda como integración futura/no implementada y planificada solo si la institución la confirma.
- Los roles implementados por el sistema son ADMIN y SECRETARIA.
