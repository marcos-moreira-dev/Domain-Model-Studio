---
dms_version: "1"
diagram_type: "free-graph"
name: "Plantilla — Grafo libre"
sample_kind: "template"
domain: "general"
status: "importable-template"
graph_kind: "mixed"
importable: true
intended_output: "diagrama visual"
---
# Grafo libre

> Plantilla importable para iniciar un grafo genérico de nodos y relaciones.
> Salida esperada: diagrama visual con nodos rectangulares con título y contenido y relaciones etiquetadas.

# Nodos

## Nodo principal
id: nodo_principal
contenido: Resume la idea, concepto o elemento principal del grafo.

## Nodo relacionado
id: nodo_relacionado
contenido: Explica una idea asociada, dependencia, evidencia o consecuencia.

## Nodo de contexto
id: nodo_contexto
contenido: Aporta información complementaria para leer el grafo.

# Relaciones

- nodo_principal -> nodo_relacionado: se conecta con
- nodo_principal -- nodo_contexto: se entiende junto a

# Observaciones

Usa `->` para relaciones dirigidas y `--` para relaciones no dirigidas.
