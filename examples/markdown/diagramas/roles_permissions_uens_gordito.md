---
dms_version: "1"
diagram_type: "roles-permissions-map"
name: "UENS — roles y permisos escolares"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "matriz visual"
description: "Matriz alineada con roles implementados en UENS: ADMIN y SECRETARIA. Otros perfiles son stakeholders, no roles de login en fase actual."
---
# Roles

## ADMIN
id: admin
propósito: administra usuarios, auditoría, reportes sensibles y operación completa del sistema.

## SECRETARIA
id: secretaria
propósito: opera registros académicos y administrativos cotidianos: estudiantes, representantes, secciones, clases, calificaciones y reportes permitidos.

# Permisos

- dashboard_ver: abrir panel principal.
- estudiantes_leer: consultar estudiantes.
- estudiantes_editar: crear o actualizar estudiantes.
- representantes_leer: consultar representantes legales.
- representantes_editar: crear o actualizar representantes legales.
- secciones_editar: crear o modificar secciones y cupos.
- docentes_editar: crear o actualizar docentes.
- asignaturas_editar: crear o actualizar asignaturas.
- clases_editar: crear o actualizar clases académicas.
- calificaciones_leer: consultar calificaciones.
- calificaciones_editar: registrar o corregir calificaciones.
- reportes_solicitar: solicitar reportes académicos/administrativos.
- reportes_descargar: descargar reportes completados.
- auditoria_ver: revisar eventos auditables.
- usuarios_administrar: crear, bloquear o modificar usuarios administrativos.
- auditoria_reporte_solicitar: solicitar reporte de auditoría administrativa.

# Asignaciones

| Rol | Permiso | Decisión | Alcance | Observación |
|---|---|---|---|---|
| admin | dashboard_ver | Permitido | global | Acceso al tablero administrativo. |
| admin | estudiantes_leer | Permitido | global | Puede consultar todos los registros. |
| admin | estudiantes_editar | Permitido | global | Puede corregir datos con trazabilidad. |
| admin | representantes_leer | Permitido | global | Consulta contactos y responsables. |
| admin | representantes_editar | Permitido | global | Alta y actualización de representantes. |
| admin | secciones_editar | Permitido | global | Mantiene grados, paralelos y cupos. |
| admin | docentes_editar | Permitido | global | Mantiene catálogo de docentes. |
| admin | asignaturas_editar | Permitido | global | Mantiene catálogo de asignaturas. |
| admin | clases_editar | Permitido | global | Gestiona oferta de clases. |
| admin | calificaciones_leer | Permitido | global | Consulta notas. |
| admin | calificaciones_editar | Permitido | global | Corrección autorizada con auditoría. |
| admin | reportes_solicitar | Permitido | global | Puede generar reportes institucionales. |
| admin | reportes_descargar | Permitido | global | Descarga archivos generados. |
| admin | auditoria_ver | Permitido | global | Revisión de trazabilidad. |
| admin | usuarios_administrar | Permitido | global | Gestión de accesos. |
| admin | auditoria_reporte_solicitar | Permitido | global | Reporte sensible solo para ADMIN. |
| secretaria | dashboard_ver | Permitido | secretaría | Acceso operativo diario. |
| secretaria | estudiantes_leer | Permitido | secretaría | Consulta y búsqueda diaria. |
| secretaria | estudiantes_editar | Permitido | secretaría | Alta, actualización y asignación de sección. |
| secretaria | representantes_leer | Permitido | secretaría | Consulta datos de contacto. |
| secretaria | representantes_editar | Permitido | secretaría | Mantiene contactos actualizados. |
| secretaria | secciones_editar | Permitido | secretaría | Puede actualizar secciones si la política institucional lo permite. |
| secretaria | docentes_editar | Condicionado | secretaría | Puede registrar datos básicos bajo autorización. |
| secretaria | asignaturas_editar | Condicionado | secretaría | Mantenimiento operativo bajo autorización. |
| secretaria | clases_editar | Condicionado | secretaría | Ajustes de clases bajo coordinación. |
| secretaria | calificaciones_leer | Permitido | secretaría | Consulta operativa para atención. |
| secretaria | calificaciones_editar | Condicionado | secretaría | Registro/corrección según política interna. |
| secretaria | reportes_solicitar | Permitido | secretaría | Reportes académicos y administrativos permitidos. |
| secretaria | reportes_descargar | Permitido | secretaría | Descarga de reportes propios/autorizados. |
| secretaria | auditoria_ver | Denegado | auditoría | La revisión integral queda para ADMIN. |
| secretaria | usuarios_administrar | Denegado | seguridad | No administra cuentas. |
| secretaria | auditoria_reporte_solicitar | Denegado | auditoría | Reporte sensible reservado a ADMIN. |

# Notas

Docente, Dirección, Representante y Soporte son stakeholders o perfiles planificados; no aparecen como roles implementados de login en la fase actual.
