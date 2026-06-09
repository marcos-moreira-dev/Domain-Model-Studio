---
dms_version: "1"
diagram_type: "c4-context"
name: "C4 Contexto — sistema administrativo mínimo"
sample_kind: "minimal"
domain: "sistema administrativo"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Contexto

Sistema: Sistema administrativo
Propósito: registrar operaciones internas, clientes y reportes.

# Personas

- Administrador: configura usuarios y revisa reportes.
- Operador: registra operaciones del día.
- Cliente: recibe comprobantes o información.

# Sistemas externos

- Correo electrónico: entrega notificaciones.
- Servicio de respaldo: conserva copias externas.

# Relaciones

- Administrador -> Sistema administrativo: administra configuración.
- Operador -> Sistema administrativo: registra información.
- Sistema administrativo -> Correo electrónico: envía avisos.
- Sistema administrativo -> Servicio de respaldo: envía copia programada.
