# Mapa de arquitectura — Domain Model Studio

## Propósito

Este documento resume la arquitectura vigente del proyecto después de las tandas 0–16. Su objetivo es que el proyecto pueda retomarse sin depender de memoria conversacional ni de planes antiguos dispersos.

Regla de lectura:

> La documentación arquitectónica explica cómo mantener y extender el producto; la interfaz visible explica diagramas, modelos, documentos, matrices, vistas, ejemplos y exportaciones.

## Capas principales

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/
├── domain/          reglas y modelos del dominio
├── application/     casos de uso, puertos, catálogos y servicios de aplicación
├── infrastructure/  persistencia, archivos, Markdown, SVG/PDF y adaptadores técnicos
├── presentation/    JavaFX, canvas, workbenches, side dock, vistas y estilos
└── bootstrap/       composición de dependencias y arranque
```

### `domain`

Contiene conceptos estables del producto: modelos, layouts, tipos, identificadores, elementos de diagramas, documentos y matrices. No debe depender de JavaFX ni de infraestructura.

### `application`

Contiene casos de uso, puertos y servicios que coordinan reglas del dominio sin conocer detalles visuales. Aquí viven catálogos de tipos, capacidades, perfiles de interacción y contratos de sincronización/exportación.

### `infrastructure`

Implementa lectura/escritura, Markdown, SVG, PDF, sincronización con el sistema de archivos y adaptadores técnicos. Puede depender de `application` mediante puertos, pero no debe filtrar detalles técnicos hacia la UI.

### `presentation`

Contiene la aplicación JavaFX: shell, workbenches, canvas, render kits, shape kits, SideDock, ayuda, formularios y estilos CSS. Puede usar servicios de aplicación, pero no debe llamar infraestructura directamente cuando exista un puerto/caso de uso.

### `bootstrap`

Conecta dependencias y construye servicios. Es la frontera donde se ensamblan implementaciones reales.

## Piezas arquitectónicas clave


### Roles de toolbar, SideDock y workspace

Domain Model Studio trabaja con artefactos de modelado, no solo con diagramas. La distribución vigente de responsabilidades es:

```txt
Toolbar contextual = acciones operativas frecuentes.
SideDock / sidebar = navegación, estructura, inspección, propiedades, filtros y ayuda contextual.
Workspace = resultado principal del artefacto activo.
```

El workspace no debe comprimirse con inspectores persistentes salvo excepción explícita del artefacto. El inspector contextual debe vivir preferentemente como módulo alternable del SideDock. Ver `docs/arquitectura/17_roles_toolbar_sidebar_workspace.md`.

### Canvas común

La infraestructura visual especializada gira alrededor de:

```txt
InteractiveCanvasSurfaceView
ZoomableDiagramSurface
InteractiveCanvasAdapter
InteractiveCanvasRenderKit
DiagramInteractionProfile
```

El modelo conceptual sigue siendo canon de calidad interactiva. La infraestructura común debe absorber gradualmente sus mejores capacidades: nodos movibles, conectores editables, etiquetas movibles, bendpoints, selección, zoom, paneo y exportación.

### Adapters

Los `*CanvasAdapter` traducen entre cada modelo específico y el canvas común. No deben reimplementar indefinidamente selección, IDs, dirty state, bounds o bendpoints. Para eso existen soportes como:

```txt
CanvasElementIdCodec
CanvasDirtyState
CanvasSelectionSupport
CanvasContentBoundsCalculator
CanvasBendPointEditingSupport
CanvasConnectorLabelEditingSupport
```

### RenderKit y ShapeKit

Los `RenderKit` arman nodos y conectores completos. Los `ShapeKit` crean símbolos visuales primitivos o compuestos. La regla vigente es:

> Un símbolo puede estar hecho de muchas primitivas internas, pero debe comportarse como una sola unidad interactiva.

### Perfiles de interacción

Los perfiles evitan que todos los tipos se comporten como grafo libre:

```txt
GRAPH
SEQUENCE
WIREFRAME
MATRIX
DOCUMENT
READ_ONLY_REFERENCE
```

UML Secuencia, wireframes, roles/permisos, diccionario y ayuda no deben recibir herramientas incompatibles con su naturaleza.

### SideDock modular

El SideDock es una carcasa lateral modular. Reacciona al tab/pestaña activa y muestra módulos compatibles con el contexto:

```txt
Diagramas       → Estructura, Propiedades, Vista, Ayuda
Diccionario     → Secciones, Propiedades, Vista previa, Ayuda
Roles/permisos  → Roles, Permisos, Matriz, Ayuda
Ayuda           → Contenido, Buscar, Índice
```

Regla clave:

> El SideDock sigue al tab activo; no depende de un estado global fijo.



### Levantamiento lógico de negocio

El tipo planificado **Levantamiento lógico** entra como proyecto documental estructurado, no como diagrama. Su perfil esperado es documental y debe usar SideDock modular con centro de lectura/edición amplia. Su función es ser fuente lógica canónica para derivar Markdown hacia diagramas, matrices y documentos existentes. No debe tocar ni contaminar el modelo conceptual canónico.

Flujo conceptual:

```txt
entrevista / observación / transcripción
→ documento lógico estructurado
→ validación, madurez y trazabilidad
→ borradores Markdown derivados
→ módulos existentes de Domain Model Studio
```


### Grafo lógico de negocio futuro

El futuro **Grafo lógico de negocio** queda planificado y registrado como tipo propio `logical-business-graph` desde Tanda 32. Desde Tanda 33 ya existe dominio puro propio para nodos, relaciones, estados e issues semánticos. Por ahora permanece en `IN_PREPARATION` con `PLANNING_VIEW`; no debe prometer canvas ni exportación hasta que existan parser, SideDock, canvas y persistencia propios. No debe implementarse como simple variante del grafo libre. Puede reutilizar infraestructura visual del canvas común y patrones del grafo libre cuando convenga, pero su fuente de verdad debe ser un dominio semántico propio con nodos `MF/FL/CU/ACC/RN/PRE/INV/POST/ENT/EST/REP/RISK/PEND` y relaciones tipadas como `contiene`, `usa`, `reutiliza`, `ejecuta`, `aplica`, `requiere`, `protege`, `garantiza`, `crea`, `modifica`, `consulta`, `genera`, `alimenta`, `bloquea`, `habilita`, `depende_de` y `deriva_en`. La UI deberá mostrar leyenda visible de abreviaciones, ayuda operativa en SideDock y guía académica separada.

Regla clave:

```txt
FreeGraph puede inspirar la experiencia visual; no debe convertirse en el dominio final del grafo lógico.
```

Ver `docs/arquitectura/19_plan_grafo_logico_negocio.md`.

### Importación de código fuente hacia UML Clases

La importación desde código fuente queda como una entrada paralela a Markdown generado por IA. Los contratos neutrales viven en:

```txt
application/sourcecode
```

Regla clave:

> Los parsers de Java, TypeScript u otros lenguajes entran por puertos/adaptadores y no reemplazan `UmlClassMarkdownParser`.

El flujo previsto es:

```txt
Directorio de proyecto
→ SourceDirectoryScannerPort
→ SourceCodeParserPort
→ ParsedCodeProject
→ UmlClassDiagramDocument
```

Esta infraestructura debe soportar proyectos multi-raíz y multi-lenguaje, como backend Java/Spring Boot y frontend TypeScript/Angular dentro de un mismo sistema.

### Exportaciones

La exportación visual debe usar bounds de contenido, no el viewport accidental. Roles/permisos se exporta como matriz/documento, no por la ruta de canvas. El diccionario usa salida documental formal.

### Ayuda CHM académica

La ayuda es una referencia documental académica, no una lluvia de ideas. Debe explicar teoría, notación, casos especiales, ejemplos y errores comunes. Su estilo puede inspirarse en ayuda clásica tipo CHM con iconos de libro morado.

## Clasificación oficial de tipos

| Tipo | Naturaleza | Perfil esperado | Salida principal |
|---|---|---|---|
| Modelo conceptual | Diagrama | GRAPH / conceptual | Diagrama Chen/pata de gallo |
| UML Clases | Diagrama UML | GRAPH | Diagrama visual exportable |
| UML Casos de uso | Diagrama UML | GRAPH | Diagrama visual exportable |
| UML Actividad | Diagrama UML | GRAPH controlado | Diagrama visual exportable |
| UML Estados | Diagrama UML | GRAPH controlado | Diagrama visual exportable |
| UML Secuencia | Diagrama UML temporal | SEQUENCE | Diagrama visual exportable |
| BPMN básico | Diagrama de proceso | GRAPH controlado | Diagrama visual exportable |
| C4 / despliegue | Diagrama técnico | GRAPH | Diagrama visual exportable |
| Mapa de módulos | Diagrama administrativo | GRAPH | Mapa exportable |
| Flujo de pantallas | Diagrama de navegación | GRAPH | Diagrama exportable |
| Wireframes | Maqueta | WIREFRAME | Maqueta exportable |
| Diccionario de datos | Documento | DOCUMENT | PDF/Markdown formal |
| Roles y permisos | Matriz/documento | MATRIX | Markdown/SVG/PNG tabular |
| Levantamiento lógico | Documento estructurado | DOCUMENT | Markdown/.dms; uso posterior como fuente revisable |
| Grafo lógico de negocio futuro | Diagrama semántico derivable | GRAPH controlado | Markdown/SVG/PNG/.dms cuando se implemente |
| Ayuda | Referencia académica | READ_ONLY_REFERENCE | Lectura/búsqueda interna |

## Reglas de mantenimiento

- No agregar un tipo visible sin ejemplo oficial y salida real.
- No mostrar herramientas incompatibles con el perfil activo.
- No usar jerga interna de implementación en la UI final.
- No tratar documentos y matrices como canvas libre.
- No tocar el modelo conceptual agresivamente sin validación de regresión.
- No aumentar clases grandes sin plan de SRP.
- Documentar cada refactor grande con objetivo, alcance, riesgos y validación.

## Documentos relacionados

```txt
docs/arquitectura/01_mapa_patrones_arquitectonicos.md
docs/arquitectura/15_importacion_codigo_fuente_uml.md
docs/arquitectura/16_levantamiento_logico_negocio.md
docs/arquitectura/17_roles_toolbar_sidebar_workspace.md
docs/arquitectura/18_plan_uml_secuencia_fragmentos_combinados.md
docs/arquitectura/19_plan_grafo_logico_negocio.md
docs/desarrollo/agregar_nuevo_tipo_diagrama.md
docs/desarrollo/validacion.md
docs/producto/decisiones_producto.md
docs/estado/16_cierre_release.md
```
