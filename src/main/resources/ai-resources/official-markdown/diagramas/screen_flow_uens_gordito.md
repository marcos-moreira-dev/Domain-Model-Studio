---
dms_version: "1"
diagram_type: "screen-flow"
name: "UENS — flujo de pantallas administrativo"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Flujo de navegación basado en ViewId reales del desktop UENS. Formularios y drawers se modelan como acciones internas cuando no son vistas principales."
---
# Pantallas

## Login
id: login
tipo: acceso
módulo: seguridad
ruta: /login
propósito: autenticar al usuario y abrir su sesión operativa.

## Dashboard
id: dashboard
tipo: principal
módulo: dashboard
ruta: /dashboard
propósito: mostrar accesos a módulos e indicadores administrativos.

## Estudiantes
id: estudiantes
tipo: listado
módulo: estudiantes
ruta: /estudiantes
propósito: buscar, crear, editar y asignar sección vigente al estudiante.

## Representantes
id: representantes
tipo: listado
módulo: representantes
ruta: /representantes
propósito: buscar, registrar y actualizar responsables legales.

## Docentes
id: docentes
tipo: listado
módulo: docentes
ruta: /docentes
propósito: mantener el catálogo de docentes.

## Secciones
id: secciones
tipo: listado
módulo: secciones
ruta: /secciones
propósito: mantener grados, paralelos, cupos y año lectivo.

## Asignaturas
id: asignaturas
tipo: listado
módulo: asignaturas
ruta: /asignaturas
propósito: mantener materias por grado y área.

## Clases
id: clases
tipo: listado
módulo: clases
ruta: /clases
propósito: gestionar oferta de asignaturas por sección, horario y docente.

## Calificaciones
id: calificaciones
tipo: formulario
módulo: calificaciones
ruta: /calificaciones
propósito: registrar y consultar notas por estudiante, clase y parcial.

## Reportes
id: reportes
tipo: listado
módulo: reportes
ruta: /reportes
propósito: solicitar, monitorear y descargar reportes.

## Auditoría
id: auditoria
tipo: consulta
módulo: auditoria
ruta: /auditoria
propósito: revisar eventos relevantes del sistema; acceso restringido a ADMIN.

# Navegación

- login -> dashboard: credenciales válidas.
- dashboard -> estudiantes: abrir módulo Estudiantes.
- dashboard -> representantes: abrir módulo Representantes.
- dashboard -> docentes: abrir módulo Docentes.
- dashboard -> secciones: abrir módulo Secciones.
- dashboard -> asignaturas: abrir módulo Asignaturas.
- dashboard -> clases: abrir módulo Clases.
- dashboard -> calificaciones: abrir módulo Calificaciones.
- dashboard -> reportes: abrir módulo Reportes.
- dashboard -> auditoria: acceso ADMIN a Auditoría.
- estudiantes -> representantes: buscar o mantener representante legal relacionado.
- estudiantes -> secciones: verificar cupo antes de asignar sección vigente.
- clases -> calificaciones: registrar notas de una clase.
- reportes -> auditoria: revisar trazabilidad de solicitud con rol ADMIN.
- auditoria -> reportes: solicitar reporte de auditoría administrativa.
