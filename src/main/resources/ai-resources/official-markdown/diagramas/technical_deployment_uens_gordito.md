---
dms_version: "1"
diagram_type: "technical-deployment"
name: "UENS — despliegue técnico escolar"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Despliegue técnico con ambientes, nodos, red local, API, PostgreSQL y almacenamiento de reportes."
---
# Ambientes

- Desarrollo local
- Piloto administrativo
- Producción escolar controlada

# Nodos

- PC de Secretaría: ejecuta aplicación desktop y opera estudiantes, representantes, secciones, calificaciones y reportes.
- PC de Administración: ejecuta aplicación desktop con rol ADMIN para usuarios, auditoría y reportes sensibles.
- Equipo de revisión institucional: consulta reportes mediante usuario administrativo autorizado cuando la política lo permita.
- Servidor de aplicación: aloja API backend Spring Boot, seguridad JWT y worker de reportes.
- Servidor PostgreSQL: conserva datos académicos y administrativos.
- Almacenamiento de reportes: guarda archivos XLSX, PDF y DOCX generados.
- Red local institucional: conecta estaciones internas con servidor de aplicación y base de datos.
- Laptop de soporte planificada: acceso controlado para diagnóstico, sin modificar datos sensibles salvo autorización.

# Conexiones

- PC de Secretaría -> Servidor de aplicación: HTTP interno autenticado.
- PC de Administración -> Servidor de aplicación: HTTP interno autenticado.
- Equipo de revisión institucional -> Servidor de aplicación: acceso controlado para reportes.
- Laptop de soporte planificada -> Servidor de aplicación: diagnóstico autorizado.
- Servidor de aplicación -> Servidor PostgreSQL: conexión privada a base de datos.
- Servidor de aplicación -> Almacenamiento de reportes: escritura y lectura de archivos generados.
- Red local institucional -> Servidor de aplicación: acceso interno restringido.

# Observaciones

- ENVIRONMENT y NETWORK deben comportarse como contenedores visuales, no como decoración.
- Las credenciales no deben quedar en archivos versionados.
- Los respaldos deben probarse con restauración periódica.
- La operación escolar debe considerar cortes de energía y conectividad limitada.
