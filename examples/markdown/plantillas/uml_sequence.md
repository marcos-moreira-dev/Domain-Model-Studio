---
dms_version: "1"
diagram_type: "uml-sequence"
name: "Plantilla — UML Secuencia"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# UML Secuencia

> Plantilla oficial importable: la IA puede generar Markdown equivalente y la aplicación puede renderizarlo como diagrama visual.

# Participantes

- Usuario
- Pantalla
- Servicio
- Base de datos

# Fragmentos combinados

- fragmento: loop | id: loop-operacion | titulo: Repetir operación | guarda: [quedan registros] | rango: 2..4 | operandos: [siguiente registro] 2..4
- fragmento: alt | id: alt-resultado | titulo: Resultado de validación | rango: 2..5 | operandos: [datos válidos] 2..4; [datos inválidos] 5..5

# Mensajes

1. Usuario -> Pantalla: solicita operación
2. Pantalla -> Servicio: envía datos validados
3. Servicio -> Base de datos: guarda información
4. Base de datos -> Servicio: confirma
5. Servicio -> Pantalla: muestra resultado

