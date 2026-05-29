# Gramática Markdown — Grafo libre

Estado: importable por la app.  
Salida esperada: diagrama visual de nodos y relaciones libres.  
Uso recomendado: representar ideas, dependencias, mapas de conocimiento, rutas de decisión simples o relaciones no cubiertas por UML, C4, BPMN ni módulos administrativos.

## Front matter obligatorio

```yaml
---
dms_version: "1"
diagram_type: "free-graph"
name: "Grafo libre — <tema>"
graph_kind: "mixed"
importable: true
intended_output: "diagrama visual"
---
```

## Estructura importable

```md
# Nodos

## Idea principal
id: idea_principal
contenido: Explicación breve que aparecerá asociada al nodo.

## Evidencia
id: evidencia
contenido: Dato, hecho o punto de apoyo.

## Contexto
id: contexto
contenido: Marco o situación que ayuda a interpretar el grafo.

# Relaciones

- idea_principal -> evidencia: se apoya en
- evidencia -- contexto: relacionado con

# Observaciones

Notas generales del grafo.
```

## Reglas

- `diagram_type` debe ser `free-graph`.
- `graph_kind` acepta `directed`, `undirected` o `mixed`.
- Cada nodo usa encabezado `##` dentro de `# Nodos`.
- Cada nodo puede declarar `id`; si se omite, la app genera un ID estable desde el título.
- Cada nodo puede declarar `contenido:` para guardar texto descriptivo.
- Las relaciones van en `# Relaciones` como lista Markdown.
- `origen -> destino: etiqueta` crea una relación dirigida.
- `origen -- destino: etiqueta` crea una relación no dirigida.
- La etiqueta de una relación es opcional. Una autorrelación se expresa repitiendo el mismo nodo, por ejemplo `nodo -> nodo: se revisa a sí mismo`.
- Las relaciones deben apuntar a nodos existentes por ID o por título.
- `# Observaciones` es opcional y guarda notas generales del grafo.

## Cuándo usarlo

Úsalo cuando necesitas un grafo genérico: mapa de conceptos, dependencias informales, ideas conectadas, relaciones causa-efecto simples, rutas de análisis o redes pequeñas de decisiones.

## Cuándo no usarlo

No lo uses para reemplazar diagramas especializados cuando la semántica ya existe: UML Clases para clases reales, BPMN para procesos, C4 para arquitectura, Mapa de módulos para áreas funcionales, Roles/permisos para autorización y Diccionario de datos para campos.
