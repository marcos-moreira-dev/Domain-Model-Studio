---
dms_version: "1"
diagram_type: "roles-permissions-map"
name: "Roles y permisos — óptica mínimo"
sample_kind: "minimal"
domain: "óptica"
status: "importable"
importable: true
intended_output: "matriz visual"
---
# Roles

## Administrador
id: administrador
propósito: controla configuración, usuarios y reportes.

## Vendedor
id: vendedor
propósito: registra clientes, ventas y seguimiento de pedidos.

## Técnico
id: tecnico
propósito: actualiza trabajos de taller y reparaciones.

# Permisos

- clientes_leer: consultar clientes.
- clientes_editar: crear o actualizar clientes.
- ventas_crear: registrar ventas.
- taller_actualizar: cambiar estado de trabajos.
- reportes_ver: consultar reportes.

# Asignaciones

| Rol | Permiso | Decisión | Alcance | Observación |
|---|---|---|---|---|
| administrador | clientes_leer | Permitido | global | Acceso completo. |
| administrador | reportes_ver | Permitido | global | Revisión de negocio. |
| vendedor | ventas_crear | Permitido | ventas | No configura usuarios. |
| vendedor | clientes_editar | Permitido | clientes | Solo datos comerciales. |
| tecnico | taller_actualizar | Permitido | taller | No modifica cobros. |
