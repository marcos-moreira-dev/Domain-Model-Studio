---
dms_version: "1"
diagram_type: "c4-context"
name: "UENS — C4 contexto sistema escolar"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "C4 Contexto: personas/stakeholders, sistema administrativo UENS y sistemas externos reales o planificados."
---
# Contexto

Sistema: Sistema administrativo UENS
Propósito: centralizar información escolar básica, asignación vigente estudiante-sección, clases, calificaciones, reportes asíncronos y auditoría.

# Personas

- Secretaría: rol implementado SECRETARIA; registra estudiantes, representantes, secciones, clases, calificaciones y reportes operativos.
- Administrador: rol implementado ADMIN; administra usuarios, revisa auditoría y opera funciones sensibles.
- Dirección: stakeholder institucional; consume reportes y decisiones, pero no aparece como rol implementado separado en fase actual.
- Representante legal: stakeholder externo; entrega datos y recibe información a través de Secretaría.
- Docente: stakeholder académico; aporta información de clases/calificaciones, sin rol de login implementado en fase actual.

# Sistemas externos

- Sistema de archivos/almacenamiento de reportes: conserva archivos XLSX, PDF y DOCX generados.
- Servicio de impresión local: permite imprimir reportes o constancias desde archivos generados.
- Correo institucional planificado: posible canal futuro de notificación; no se trata como integración implementada.

# Relaciones

- Secretaría -> Sistema administrativo UENS: opera registros académicos y administrativos.
- Administrador -> Sistema administrativo UENS: administra usuarios, reportes de auditoría y trazabilidad.
- Dirección -> Sistema administrativo UENS: solicita o recibe reportes mediante usuarios administrativos autorizados.
- Representante legal -> Secretaría: entrega datos, actualizaciones y solicitudes del estudiante.
- Docente -> Secretaría: entrega o valida información académica cuando el proceso institucional lo requiera.
- Sistema administrativo UENS -> Sistema de archivos/almacenamiento de reportes: guarda reportes generados.
- Sistema administrativo UENS -> Servicio de impresión local: habilita impresión de reportes descargados.
- Sistema administrativo UENS -> Correo institucional planificado: integración futura para avisos, no obligatoria en fase actual.
