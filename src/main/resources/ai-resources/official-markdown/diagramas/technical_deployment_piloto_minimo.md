---
dms_version: "1"
diagram_type: "technical-deployment"
name: "Despliegue técnico — piloto mínimo"
sample_kind: "minimal"
domain: "piloto"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Ambientes

- Desarrollo local
- Piloto interno

# Nodos

- Laptop del operador: ejecuta cliente o navegador.
- Servicio web piloto: aloja API y recursos web.
- Base de datos piloto: conserva datos de prueba controlada.

# Conexiones

- Laptop del operador -> Servicio web piloto: HTTPS.
- Servicio web piloto -> Base de datos piloto: conexión privada.

# Observaciones

- El ambiente piloto no debe mezclarse con datos reales de producción.
- Las credenciales se mantienen fuera del repositorio.
