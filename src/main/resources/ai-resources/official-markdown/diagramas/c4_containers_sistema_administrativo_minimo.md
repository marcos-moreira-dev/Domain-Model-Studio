---
dms_version: "1"
diagram_type: "c4-containers"
name: "C4 Contenedores — sistema administrativo mínimo"
sample_kind: "minimal"
domain: "sistema administrativo"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Contenedores

- Aplicación de escritorio: operación diaria del negocio.
- API backend: reglas, validaciones y servicios.
- Base de datos PostgreSQL: datos persistentes.
- Generador de reportes: documentos PDF y resúmenes.

# Relaciones

- Aplicación de escritorio -> API backend: solicitudes autenticadas.
- API backend -> Base de datos PostgreSQL: lectura y escritura.
- API backend -> Generador de reportes: pide documentos.
- Generador de reportes -> Base de datos PostgreSQL: consulta datos preparados.
