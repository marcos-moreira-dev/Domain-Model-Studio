---
dms_version: "1"
diagram_type: "uml-sequence"
name: "UML Secuencia — inicio de sesión mínimo"
sample_kind: "minimal"
domain: "autenticación"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Participantes

- Usuario
- Pantalla de acceso
- Servicio de autenticación
- Base de datos

# Mensajes

1. Usuario -> Pantalla de acceso: ingresa credenciales
2. Pantalla de acceso -> Servicio de autenticación: solicita validación
3. Servicio de autenticación -> Base de datos: consulta usuario
4. Base de datos -> Servicio de autenticación: devuelve datos mínimos
5. Servicio de autenticación -> Pantalla de acceso: autoriza o rechaza
