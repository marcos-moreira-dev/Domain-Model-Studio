---
dms_version: "1"
diagram_type: "admin-module-map"
name: "Plantilla — Mapa de módulos"
sample_kind: "template"
domain: "general"
status: "importable-template"
importable: true
intended_output: "diagrama visual"
---
# Mapa de módulos

> Plantilla importable para iniciar un mapa de módulos y ajustarlo con IA o edición manual.
> Salida esperada: diagrama visual.

# Módulos

## Módulo principal
id: modulo_principal
responsabilidad: Qué parte del negocio organiza.

### Submódulos
- submodulo_uno: tarea o área que cubre.
- submodulo_dos: tarea o área que cubre.

## Módulo de soporte
id: modulo_soporte
responsabilidad: Qué apoyo funcional entrega al módulo principal.

# Dependencias

- modulo_principal -> modulo_soporte: motivo de la dependencia.
