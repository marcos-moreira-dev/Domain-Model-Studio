---
dms_version: "1"
diagram_type: "screen-flow"
name: "Plantilla — Flujo de pantallas"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Flujo de pantallas

> Plantilla oficial importable para generar flujo de pantallas.

# Pantallas

## Inicio
id: inicio
propósito: Punto de entrada del usuario.

## Gestión principal
id: gestion_principal
propósito: Operación central del módulo.

# Navegación

- inicio -> gestion_principal: Abrir gestión principal.
- gestion_principal -> inicio: Volver al inicio.

