---
dms_version: "1"
diagram_type: "uml-use-case"
name: "UML Casos de uso — restaurante mínimo"
sample_kind: "minimal"
domain: "restaurante"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Sistema

Nombre: Sistema de restaurante
Límite: Operación de pedidos, caja e inventario básico.

# Actores

- Mesero
- Cajero
- Administrador

# Casos de uso

- Registrar pedido
- Cobrar cuenta
- Revisar ventas del día
- Ajustar inventario

# Relaciones

- Mesero -> Registrar pedido
- Cajero -> Cobrar cuenta
- Administrador -> Revisar ventas del día
- Administrador -> Ajustar inventario
