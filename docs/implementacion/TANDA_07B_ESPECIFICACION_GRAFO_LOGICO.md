# Tanda 7B — Especificación del futuro grafo lógico

Estado: aplicada.

## Objetivo

Dejar planificado el futuro tipo de proyecto **Grafo lógico de negocio** como artefacto propio dentro de Domain Model Studio, sin implementarlo todavía y sin contaminar el grafo libre ni el modelo conceptual.

## Decisión principal

El grafo lógico no será un grafo libre renombrado.

Debe reutilizar infraestructura visual cuando convenga, pero debe tener dominio, gramática, validación, ayuda, derivación y contrato Markdown propios.

## Documento agregado

Se agregó:

```txt
docs/arquitectura/19_plan_grafo_logico_negocio.md
```

El plan define:

- propósito del grafo lógico;
- relación con levantamiento lógico;
- diferencia con grafo libre;
- tipo de proyecto sugerido `logical-business-graph`;
- paquetes futuros recomendados;
- nodos semánticos mínimos;
- relaciones semánticas mínimas;
- reglas de validación;
- gramática Markdown propuesta;
- derivación desde `LogicalBusinessDocument`;
- SideDock y toolbar esperados;
- reutilización visual permitida;
- relación con UML, BPMN, diccionario, roles, pantallas, wireframes, reportes y pruebas;
- límites de alcance;
- guardarraíles futuros.

## Nodos mínimos fijados

```txt
MF
FL
CU
ACC
RN
PRE
INV
POST
ENT
EST
REP
RISK
PEND
```

## Relaciones mínimas fijadas

```txt
contiene
usa
reutiliza
ejecuta
aplica
requiere
protege
garantiza
crea
modifica
consulta
genera
alimenta
bloquea
habilita
depende_de
deriva_en
```

## Reglas de alcance

No se implementó todavía:

- nuevo tipo visible en catálogo;
- dominio `logicalbusinessgraph`;
- parser productivo;
- editor JavaFX;
- render kit;
- SVG específico;
- recursos IA finales;
- derivación automática productiva.

Esta tanda es deliberadamente arquitectónica para dejar la frontera correcta antes de cualquier implementación futura.

## Zonas protegidas

No se tocó:

- pantalla de inicio;
- modelo conceptual;
- canvas conceptual;
- dominio del grafo libre;
- parser del grafo libre;
- render del grafo libre.

## Guardarraíl agregado

Se agregó `Tanda7BLogicalBusinessGraphPlanSourceTest` para proteger que el plan mantenga:

- tipo `logical-business-graph`;
- separación respecto a `FreeGraphDocument`;
- nodos semánticos mínimos;
- relaciones semánticas mínimas;
- derivación desde `LogicalBusinessDocument`;
- regla de no contaminar modelo conceptual ni grafo libre.

## Validación local en este entorno

No se ejecutó Maven porque no está instalado en este entorno.

Se realizó validación documental por búsqueda de tokens y estructura de archivos.

## Frontera reforzada

La regla de esta tanda es no contaminar el grafo libre ni el modelo conceptual. El grafo libre puede inspirar capacidades visuales, pero el grafo lógico debe conservar contrato propio.
