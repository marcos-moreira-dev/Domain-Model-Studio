# Alineación 005 — Ayuda académica, ayuda operativa y recursos IA

Estado: **alineación aplicada**  
Fecha: **2026-05-24**  
Alcance: **frontera entre teoría académica, ayuda operativa del SideDock y recursos IA del Grafo lógico del negocio**  
Tipo de cambio: **documental y de guardarraíl fuente; no modifica lógica de ejecución**

## 1. Propósito

Esta alineación fija el contrato de contenido que deben respetar tres familias distintas de ayuda y documentación dentro de Domain Model Studio:

```txt
menú Ayuda = guía académica teórica
SideDock Ayuda = ayuda operativa de herramienta
Recursos IA = materiales exportables para producir Markdown compatible y reimportable
```

La alineación no implementa todavía la guía académica del Grafo lógico ni modifica los descriptores de recursos IA. Su función es evitar que las tandas técnicas mezclen teoría, instrucciones de uso y prompts IA en un mismo lugar.

## 2. Relación con alineaciones anteriores

- La Alineación 001 definió que una capacidad visible requiere implementación, prueba, documentación y smoke.
- La Alineación 002 protegió pantalla de inicio, modelo conceptual, canvas conceptual y sidebar legacy.
- La Alineación 003 fijó la semántica del Grafo lógico del negocio.
- La Alineación 004 fijó el cableado esperado de catálogo, workspace, toolbar, exportación y persistencia.

Esta Alineación 005 fija el contrato para completar:

```txt
THEORY_HELP
AI_RESOURCES
Ayuda operativa del SideDock
naming correcto de la ventana del menú Ayuda
```

## 3. Frontera obligatoria de ayudas

| Entrada visible | Fuente técnica | Propósito | Contenido permitido | Contenido prohibido |
|---|---|---|---|---|
| Menú `Ayuda` | `ManualDialog`, `ManualContent`, `DefaultTheoryCatalog` | Guía académica teórica | teoría, notación, fundamentos, cuándo usar/no usar, errores comunes, figuras didácticas | pasos de botón, instrucciones operativas del SideDock, mensajes de toolbar |
| Botón `Ayuda` del SideDock | `OperationalHelpCatalog`, `OperationalHelpContent`, `StandardSideDockModules.operationalHelp(...)` | Ayuda operativa de herramienta | cómo usar el tipo activo, paneles, selección, edición, validación y exportación | teoría extensa, bibliografía, reemplazo de la guía académica |
| Recursos IA | `AiResourceDescriptor`, `ClasspathAiResourceCatalog`, archivos `ai-resources` | Material para IA y Markdown compatible | gramáticas, prompts, plantillas y ejemplos importables | prometer capacidades no implementadas o marcar prompts como Markdown importable |

Regla principal:

```txt
La guía académica explica por qué y cuándo usar un artefacto.
La ayuda operativa explica cómo usar la herramienta.
Los recursos IA explican cómo producir o reutilizar Markdown compatible.
```

## 4. Contrato de guía académica

La guía académica es estrictamente teórica. Debe vivir en el menú `Ayuda` y apoyarse en:

```txt
TheoryTopicId
DefaultTheoryCatalog
DefaultTheoryFigureCatalog
help/topics/*.md
ManualContent
ManualDialog
```

Para el Grafo lógico del negocio, la tanda técnica T40 deberá crear y registrar:

```txt
TheoryTopicId.LOGICAL_BUSINESS_GRAPH
help/topics/logical-business-graph.md
registro en DefaultTheoryCatalog
asignación de theoryTopicId en DefaultDiagramTypeDefinitions
figura didáctica en DefaultTheoryFigureCatalog
```

El tema académico deberá incluir, como micro-wikipedia mínima, secciones equivalentes a:

```txt
Qué es
Para qué sirve
Elementos principales
Relaciones y lectura
Casos especiales
Cuándo usarlo
Cuándo no usarlo
Errores comunes
```

No debe ser una copia de la ayuda operativa. Debe explicar conceptos como macroflujo, microflujo, caso de uso, acción transformadora, regla, precondición, invariante, postcondición, entidad, estado, reporte, riesgo, pregunta pendiente y trazabilidad.

## 5. Naming obligatorio del menú Ayuda

La ventana académica no debe llamarse guía operativa. En la línea final de producto, el naming esperado es:

```txt
Menú: Ayuda → Guía académica
Stage title: Guía académica — Domain Model Studio
Encabezado: REFERENCIA ACADÉMICA DE DIAGRAMAS
Título: Guía académica de Domain Model Studio
Estado: Guía académica abierta.
```

Los textos heredados con “Guía operativa” en `ManualDialog` o `ManualContent` quedan corregidos por la tanda técnica de guía académica. La ayuda operativa del SideDock conserva su nombre operativo porque sí describe uso de herramienta.

## 6. Contrato académico específico del Grafo lógico

El tema académico del `logical-business-graph` debe explicar que el artefacto es:

```txt
una vista visual semántica derivada del levantamiento lógico,
orientada a revisar trazabilidad, reglas, condiciones, acciones,
entidades, estados, reportes, riesgos y preguntas pendientes.
```

Debe dejar explícito que no es:

```txt
- grafo libre;
- BPMN;
- UML;
- modelo conceptual;
- diccionario de datos;
- reemplazo del Levantamiento lógico;
- árbol rígido puro.
```

Debe explicar el backbone recomendado:

```txt
MF → FL → CU → ACC
```

y por qué se permiten relaciones cruzadas como `reutiliza`, `depende_de`, `bloquea`, `alimenta` o `deriva_en`.

## 7. Contrato de ayuda operativa del SideDock

La ayuda operativa del Grafo lógico ya existe en:

```txt
OperationalHelpCatalog.logicalBusinessGraph(...)
StandardSideDockModules.operationalHelp(...)
```

Su propósito es guiar el uso de la herramienta activa. Debe mantenerse enfocada en:

```txt
- seleccionar nodos y relaciones;
- navegar estructura;
- editar propiedades permitidas;
- leer la leyenda;
- revisar validación;
- mover nodos;
- entender módulos del SideDock;
- exportar cuando la salida activa exista.
```

No debe absorber explicaciones largas de teoría, historia de notaciones ni criterios académicos extensos. Si un texto operativo necesita teoría, debe apuntar conceptualmente a la guía académica, no duplicarla.

## 8. Contrato de recursos IA

Los recursos IA oficiales se organizan por descriptores y archivos classpath. Las fuentes principales son:

```txt
AiResourceDescriptor
OfficialAiResourceDescriptors
LogicalBusinessGraphAiResourceDescriptors
ClasspathAiResourceCatalog
ClasspathAiResourceExporter
AiResourceProductizationPolicy
```

El Grafo lógico del negocio tiene estos recursos esperados:

| Recurso | Archivo | Tipo de uso | Importabilidad esperada |
|---|---|---|---|
| Gramática | `18_grafo_logico_negocio_gramatica.md` | Explica sintaxis, secciones y reglas de Markdown | Importable como recurso de referencia, no como proyecto final por sí mismo |
| Prompt maestro | `19_grafo_logico_negocio_prompt_ia.md` | Instruye a una IA para producir Markdown compatible | **No importable** como proyecto; su salida esperada sí debe ser Markdown importable |
| Plantilla oficial | `official-markdown/plantillas/logical_business_graph.md` | Base mínima para generar o completar un grafo | Importable |
| Ejemplo mínimo | `official-markdown/diagramas/logical_business_graph_minimo.md` | Smoke pequeño y referencia de estructura mínima | Importable |
| Ejemplo UENS | `official-markdown/diagramas/logical_business_graph_uens_gordito.md` | Ejemplo completo de familia UENS | Importable |

El descriptor del prompt debe mantenerse como `PROMPT_GUIDE`: exportable para copiar a una IA, pero no importable como proyecto. Si el texto del prompt contiene `importable: true`, esa marca pertenece al Markdown que la IA debe producir, no al prompt como archivo.

## 9. Capacidad `AI_RESOURCES`

Cuando `logical-business-graph` pase a contrato productivo final, el catálogo deberá declarar:

```txt
AI_RESOURCES
```

solo si se cumple:

```txt
- los archivos existen en classpath;
- los descriptores están registrados;
- el exportador global los copia;
- el índice de recursos IA los clasifica correctamente;
- docs/ia/RECURSOS_IA.md menciona logical-business-graph;
- el prompt no se trata como Markdown importable;
- la plantilla y ejemplos sí son importables.
```

## 10. Capacidad `THEORY_HELP`

Cuando `logical-business-graph` pase a contrato productivo final, el catálogo deberá declarar:

```txt
THEORY_HELP
```

solo si existe:

```txt
- `TheoryTopicId.LOGICAL_BUSINESS_GRAPH`;
- `help/topics/logical-business-graph.md`;
- registro en `DefaultTheoryCatalog`;
- al menos una figura didáctica registrada en `DefaultTheoryFigureCatalog`;
- micro-wikipedia con profundidad suficiente;
- asignación `theoryTopicId` desde `DefaultDiagramTypeDefinitions`;
- naming del menú Ayuda corregido a Guía académica.
```

## 11. Casos de contradicción y decisión

| Contradicción | Decisión de alineación |
|---|---|
| Menú Ayuda decía “Guía operativa” | Cerrado en Tanda 32: Menú Ayuda y `ManualDialog` usan Guía académica. |
| SideDock Ayuda incluye teoría extensa | Recortar a operación o mover a guía académica. |
| Prompt IA contiene `importable: true` | Interpretarlo como salida esperada del prompt, no como descriptor del prompt. |
| `logical-business-graph` tiene recursos IA pero no capacidad `AI_RESOURCES` | Agregar capacidad solo cuando el contrato final de catálogo se cierre. |
| `logical-business-graph` no tiene teoría registrada | No declarar `THEORY_HELP` hasta crear tema, figura y catálogo; en términos de guardarraíl: no declarar `THEORY_HELP` hasta crear tema, figura y catálogo. |
| Docs IA no mencionan el nuevo tipo | Actualizar documentación en la tanda de matrices/productización. |

## 12. Criterio de cierre de Alineación 005

Esta alineación se considera aplicada cuando exista este documento, haya guardarraíl fuente y los documentos vivos apunten al contrato.

No se considera cerrada todavía la Tanda 40 ni la integración final de recursos IA. Esas tandas deben modificar código y documentación específica después de esta alineación.

## 13. Tests focalizados esperados

La validación local de esta alineación debe poder ejecutarse con:

```bat
scripts\21-validar-alineacion-05.bat
```

O directamente con:

```bat
mvn -Dtest=Alineacion5HelpAndAiResourcesContractSourceTest test
```

El test no debe exigir que T40 ya esté aplicada. Debe exigir que el contrato de frontera exista, que las fuentes actuales de ayuda operativa y recursos IA estén presentes, y que los documentos vivos mencionen esta alineación.
