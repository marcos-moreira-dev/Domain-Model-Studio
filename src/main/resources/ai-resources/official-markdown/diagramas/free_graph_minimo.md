---
dms_version: "1"
diagram_type: "free-graph"
name: "Grafo libre — análisis de reparación mínimo"
sample_kind: "minimal"
domain: "reparación técnica"
status: "importable"
graph_kind: "mixed"
importable: true
intended_output: "diagrama visual"
---
# Nodos

## Equipo recibido
id: equipo_recibido
contenido: Celular o laptop que entra a revisión inicial.

## Diagnóstico
id: diagnostico
contenido: Evaluación técnica para detectar falla probable.

## Repuesto
id: repuesto
contenido: Pieza requerida para completar la reparación.

## Cliente
id: cliente
contenido: Persona que autoriza o rechaza la reparación.

# Relaciones

- equipo_recibido -> diagnostico: requiere revisión
- diagnostico -> repuesto: puede necesitar
- cliente -> diagnostico: solicita resultado
- cliente -> repuesto: autoriza compra

# Observaciones

Ejemplo pequeño para comprobar que el grafo libre importa nodos, relaciones dirigidas y etiquetas.
