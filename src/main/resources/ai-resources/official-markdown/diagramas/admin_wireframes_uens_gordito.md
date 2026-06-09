---
dms_version: "1"
diagram_type: "admin-wireframes"
name: "UENS — wireframes administrativos escolares"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "wireframe visual"
description: "Maquetas estructurales de vistas reales del desktop UENS. No son Figma ni controles JavaFX reales."
---
# Pantallas

## Login
id: login
módulo: seguridad
propósito: entrada segura al sistema administrativo.

### Secciones
- panel_marca: nombre institucional y contexto de la aplicación.
- formulario_acceso: usuario, contraseña y botón ingresar.
- zona_feedback: error de credenciales o sesión expirada.

### Controles
- campo_usuario: captura nombre_login.
- campo_password: captura contraseña.
- boton_ingresar: valida credenciales.
- alerta_error: muestra mensaje controlado.

## Dashboard
id: dashboard
módulo: dashboard
propósito: navegación principal e indicadores administrativos.

### Secciones
- menu_lateral: Estudiantes, Representantes, Docentes, Secciones, Asignaturas, Clases, Calificaciones, Reportes y Auditoría.
- tarjetas_resumen: estudiantes activos, secciones activas, reportes pendientes y eventos recientes.
- panel_alertas: cupos, datos incompletos o solicitudes con error.

### Controles
- boton_nuevo_estudiante: abre acción de registro dentro de Estudiantes.
- boton_solicitar_reporte: navega a Reportes.
- filtro_anio_lectivo: cambia contexto de indicadores si aplica.

## Estudiantes
id: estudiantes
módulo: estudiantes
propósito: consultar, filtrar, crear y actualizar estudiantes.

### Secciones
- encabezado: título y botón Nuevo estudiante.
- filtros: búsqueda, sección, estado y año lectivo.
- tabla_resultados: estudiante, representante, sección vigente, estado y acciones.
- panel_edicion: formulario/drawer para datos del estudiante y asignación vigente a sección.

### Controles
- campo_busqueda: busca por nombre o apellido.
- filtro_seccion: limita por grado y paralelo.
- selector_representante: vincula representante legal.
- selector_seccion: actualiza estudiante.seccion_id.
- accion_editar: abre formulario del estudiante.

## Representantes
id: representantes
módulo: representantes
propósito: mantener datos de contacto y responsabilidad legal.

### Secciones
- filtros: búsqueda por nombres, apellidos, teléfono o correo.
- tabla_resultados: representante, contacto y acciones.
- formulario_representante: nombres, apellidos, teléfono y correo electrónico.

### Controles
- boton_nuevo_representante: abre alta.
- accion_editar: actualiza contacto.
- alerta_contacto_incompleto: advierte falta de teléfono o correo.

## Clases
id: clases
módulo: clases
propósito: gestionar la oferta de asignaturas por sección, horario y docente opcional.

### Secciones
- filtros_academicos: sección, asignatura, docente y estado.
- tabla_clases: día, hora, sección, asignatura, docente y estado.
- formulario_clase: horario, sección, asignatura y docente.

### Controles
- selector_seccion: filtra o asigna sección.
- selector_asignatura: vincula materia.
- selector_docente: asigna docente opcional.
- boton_guardar_clase: persiste clase.

## Calificaciones
id: calificaciones
módulo: calificaciones
propósito: registrar notas por estudiante, clase y parcial.

### Secciones
- contexto_clase: sección, asignatura, docente, parcial y horario.
- tabla_estudiantes: estudiantes de la sección/clase con nota y observación.
- resumen_validacion: notas faltantes o fuera de rango.

### Controles
- selector_clase: selecciona oferta académica.
- selector_parcial: parcial 1 o 2.
- campo_nota: valor numérico 0..10.
- boton_guardar: confirma registro o actualización.

## Reportes
id: reportes
módulo: reportes
propósito: solicitar, monitorear y descargar reportes XLSX, PDF o DOCX.

### Secciones
- filtros_reporte: tipo, sección, parcial, fechas y formato.
- tabla_solicitudes: fecha, tipo, estado, usuario, intentos y acciones.
- panel_estado: detalle de PENDIENTE, EN_PROCESO, COMPLETADA o ERROR.

### Controles
- boton_solicitar: crea registro en reporte_solicitud_queue.
- accion_descargar: descarga archivo generado.
- accion_reintentar: reintenta solicitud con error cuando la política lo permita.

## Auditoría
id: auditoria
módulo: auditoria
propósito: revisar eventos administrativos y trazabilidad de operaciones sensibles.

### Secciones
- filtros_auditoria: fecha, módulo, acción, resultado, actor y rol.
- tabla_eventos: fecha, módulo, acción, entidad, resultado y actor.
- panel_detalle: request_id, IP, detalle y entidad afectada.

### Controles
- filtro_resultado: EXITO, ERROR, INFO o ADVERTENCIA.
- filtro_actor: usuario o login.
- boton_exportar: genera reporte de auditoría cuando el rol lo permite.
