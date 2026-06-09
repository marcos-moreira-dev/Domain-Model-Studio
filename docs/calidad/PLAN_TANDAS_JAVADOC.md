# Plan de tandas JavaDoc pedagógicas

## Objetivo

Convertir Domain Model Studio en un proyecto más útil para estudiar ingeniería de software, arquitectura por capas y desarrollo de software, sin inflar el código con comentarios obvios.

## Principio rector

```txt
JavaDoc debe explicar contrato, intención y uso.
No debe repetir el nombre del método.
```

## Tanda JD-1 — Contratos públicos de dominio

Estado: aplicada en JD-1.

Prioridad: alta.

Cubrir:

```txt
domain/logicalbusinessgraph
domain/logicalbusiness
domain/diagram
domain/layout
domain/style
```

Contenido esperado:

```txt
- Qué representa cada agregado.
- Invariantes protegidas.
- Qué operaciones devuelven nuevas instancias.
- Qué errores se rechazan.
- Cómo leer severidades de validación.
```

## Tanda JD-2 — Casos de uso y servicios de aplicación

Estado: aplicada en JD-2.

Prioridad: alta.

Cubrir:

```txt
application/catalog
application/logicalbusiness
application/logicalbusiness/derivation
application/export
application/project
application/visual
```

Contenido esperado:

```txt
- Precondiciones del caso de uso.
- Resultado esperado.
- Responsabilidad exacta de cada servicio.
- Qué no debe hacer el servicio.
```

## Tanda JD-3 — Infraestructura de importación, persistencia y exportación

Estado: aplicada en JD-3.

Prioridad: media-alta.

Cubrir:

```txt
infrastructure/markdown
infrastructure/json
infrastructure/svg
infrastructure/resources
infrastructure/pdf
```

Contenido esperado:

```txt
- Contratos de formato.
- Roundtrip esperado.
- Relación entre Markdown, .dms, SVG, PNG, PDF y recursos IA.
- Errores recuperables vs errores bloqueantes.
```

## Tanda JD-4 — Presentation, workbenches, SideDock y canvas transversal

Estado: aplicada en JD-4.

Prioridad: media.

Cubrir:

```txt
presentation/workspace
presentation/shell
presentation/sidedock
presentation/diagramcanvas
presentation/interactivecanvas
presentation/logicalbusinessgraph
```

Contenido esperado:

```txt
- Frontera entre ViewModel, View y dominio.
- Cómo se monta un workspace.
- Cómo se evita contaminar modelo conceptual.
- Cómo fluye selección, movimiento, exportación y ayuda operativa.
```

## Tanda JD-5 — Sitio JavaDoc y guardarraíl de cobertura

Estado: aplicada en JD-5.

Prioridad: media.

Acciones:

```txt
- Generar `target/site/apidocs`.
- Revisar navegación por paquetes.
- Crear un guardarraíl de cobertura gradual.
- No exigir 100% de métodos públicos al inicio.
- Documentar el uso del sitio en `docs/desarrollo/JAVADOC_SITIO_GUIA.md`.
```

Regla aplicada inicial:

```txt
Tipos públicos: mantener >= 95%.
Métodos públicos críticos: lista blanca por paquetes priorizados en tandas posteriores.
No bloquear getters/setters triviales.
Sitio generado esperado: target/site/apidocs/index.html.
```

## Tanda JD-6 — Ejemplos JavaDoc para estudio

Estado: aplicada en JD-6.

Prioridad: opcional.

Se agregaron ejemplos JavaDoc pedagógicos especialmente en:

```txt
LogicalBusinessGraphValidationPolicy
LogicalBusinessGraphDocument
LogicalBusinessGraphRelationKind
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphMarkdownExporter
DmsProjectJsonReader
DmsProjectJsonWriter
InteractiveCanvasAdapter
CanvasBendPointController
WorkbenchSideDock
```

Guía agregada:

```txt
docs/desarrollo/JAVADOC_EJEMPLOS_PEDAGOGICOS.md
```

## Cierre recomendado

Activar estas tandas después de aprobar el instalable o cuando quieras usar el proyecto como material de estudio intensivo.


## Tanda JD-7 — Onboarding vivo de arquitectura

Estado: aplicada en JD-7.

Prioridad: recomendada.

Acciones:

```txt
- Mantener una ruta de lectura por capas.
- Agregar diagramas o tablas simples de flujo dominio → aplicación → infraestructura → presentación.
- Relacionar JavaDoc con los scripts de validación y smoke.
- Crear ejemplos de lectura para estudiantes.
```

Motivo: el proyecto ya es suficientemente grande para servir como material de estudio. El onboarding debe guiar la lectura, no solo la ejecución.

Documento agregado en JD-7:

```txt
docs/desarrollo/ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md
```

Guardarraíl:

```txt
Suite global: scripts/02-ejecutar-tests.bat
```


## Tanda JD-8 — Guía de lectura por casos de uso completos

Estado: aplicada en JD-8.

Prioridad: recomendada.

Acciones aplicadas:

```txt
- Crear recorridos de lectura de punta a punta.
- Vincular dominio, aplicación, infraestructura y presentación por caso de uso.
- Cubrir flujo levantamiento lógico → grafo lógico → .dms → exportación → UI.
- Incluir preguntas de estudio para ingeniería de software.
- Registrar guardarraíl fuente y script focalizado.
```

Documento agregado:

```txt
docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md
```

Guardarraíl:

```txt
Suite global: scripts/02-ejecutar-tests.bat
```

Motivo: después de JD-7 ya existe suficiente documentación por capas y arquitectura; JD-8 agrega una guía que atraviesa capas por funcionalidades completas.


## Tanda JD-9 — ADRs pedagógicos y decisiones de diseño

Estado: aplicada en JD-9.

Prioridad: opcional recomendada.

Acciones:

```txt
- Crear registros breves de decisión arquitectónica para decisiones importantes.
- Explicar por qué existe SideDock transversal y sidebar conceptual separado.
- Explicar por qué el Grafo lógico no reutiliza FreeGraphDocument.
- Explicar por qué la ayuda académica y la ayuda operativa viven separadas.
- Explicar por qué el canvas común opera mediante adaptadores.
```

Motivo: para estudiar ingeniería de software no basta con saber qué hace el código; también ayuda conocer qué alternativas se descartaron y por qué.

Documento agregado:

```txt
docs/desarrollo/ADR_DECISIONES_DISENO_PEDAGOGICAS.md
```

Guardarraíl:

```txt
Suite global: scripts/02-ejecutar-tests.bat
```

Con JD-9, la línea JavaDoc queda cerrada para uso pedagógico principal. Mejoras futuras deberían ser puntuales: diagramas de arquitectura, ejemplos de clase o actualización de ADRs cuando cambie una decisión real.


## Tanda 38A — Revisión JavaDoc post-refactor

Estado: aplicada después del refactor integral Tandas 28–37.

Prioridad: puntual.

Acciones aplicadas:

```txt
- Revisar package-info y JavaDoc solo en paquetes tocados por el refactor.
- Documentar fachadas por familia, catálogos por familia, persistencia .dms, MarkdownImportDocument, ProjectChangeSupport, UML Clases, shell y canvas conceptual legacy.
- Mantener un solo entry point público para generación JavaDoc: scripts/31-generar-javadoc.bat.
- Evitar comentarios decorativos o repetitivos.
```

Documento agregado:

```txt
docs/desarrollo/TANDA_038A_JAVADOC_POST_REFACTOR.md
```

Con esta tanda, JavaDoc queda suficiente para pasar a smoke integral post-refactor.
