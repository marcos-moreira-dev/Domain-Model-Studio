---
dms_version: "1"
diagram_type: "uml-use-case"
name: "Plantilla — UML Casos de uso"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# UML Casos de uso

> Plantilla oficial importable: la IA puede generar Markdown equivalente y la aplicación puede renderizarlo como diagrama visual.

# Sistema

Nombre: Sistema administrativo
Límite: Funciones visibles para usuarios finales.

# Actores

- Administrador
- Operador
- Cliente

# Casos de uso

- Gestionar registros
- Validar permisos
- Consultar reportes
- Configurar permisos
- Exportar reporte

# Relaciones

- Administrador -> Configurar permisos
- Operador -> Gestionar registros
- Gestionar registros -> Validar permisos: include
- Consultar reportes -> Exportar reporte: extend

