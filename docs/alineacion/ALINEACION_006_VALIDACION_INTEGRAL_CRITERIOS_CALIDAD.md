# Alineación 006 — Validación integral y criterios de calidad

Estado: **alineación aplicada**  
Fecha: **2026-05-24**  
Alcance: **contrato de validación integral, severidades y criterios de calidad del Grafo lógico del negocio**  
Tipo de cambio: **documental y de guardarraíl fuente; no modifica lógica de ejecución**

## 1. Propósito

Esta alineación fija cómo debe validarse el tipo `logical-business-graph` antes de declararlo producto final. Esta alineación no implementa todavía la validación profunda; define el contrato que deberá cumplir la Tanda 42.

La regla central es:

```txt
validación integral = integridad estructural + coherencia semántica + trazabilidad + calidad visual mínima + pruebas reproducibles
```

El Grafo lógico del negocio no debe cerrarse como `AVAILABLE` solo porque renderiza nodos y relaciones. Debe poder explicar si el grafo es coherente, derivable, revisable y confiable como documentación canónica del negocio.

## 2. Relación con alineaciones anteriores

- La Alineación 001 definió que capacidad visible implica implementación, prueba, documentación y smoke.
- La Alineación 002 protegió zonas prohibidas y evitó contaminación del modelo conceptual.
- La Alineación 003 fijó el contrato semántico del Grafo lógico.
- La Alineación 004 fijó catálogo, capacidades, toolbar, workspace y exportación.
- La Alineación 005 separó guía académica, ayuda operativa y recursos IA.

Esta Alineación 006 define el estándar de calidad que debe cumplir la validación antes de actualizar matrices y release.

## 3. Fuentes técnicas actuales

La base existente ya ofrece piezas útiles:

```txt
LogicalBusinessGraphDocument
LogicalBusinessGraphNode
LogicalBusinessGraphEdge
LogicalBusinessGraphNodeKind
LogicalBusinessGraphRelationKind
LogicalBusinessGraphIssue
LogicalBusinessGraphIssueSeverity
semanticIssues()
canConnect(...)
```

El estado actual de `semanticIssues()` es básico. Detecta principalmente ausencia de macroflujo, relaciones en bucle y relaciones semánticamente inesperadas. Esa base no debe eliminarse; debe ampliarse con una política integral.

## 4. Canales de validación

La validación final debe operar en cinco canales coordinados:

| Canal | Responsable esperado | Qué protege |
|---|---|---|
| Integridad estructural de dominio | `LogicalBusinessGraphDocument`, records/enums del dominio | duplicados, referencias inexistentes, códigos obligatorios, tipo inferido por prefijo |
| Validación semántica | `semanticIssues()` o servicio dedicado de aplicación | relaciones inválidas, nodos aislados, faltas de trazabilidad, condiciones no protegidas |
| Validación de importación Markdown | parser + dispatcher + tests de roundtrip | Markdown compatible, secciones mínimas, relaciones hacia nodos existentes |
| Validación de UI/workspace | `ProjectValidationCoordinator` + ViewModel del Grafo lógico | que el menú `Diagrama > Validar proyecto` valide el tipo activo correcto |
| Validación de release | tests + smoke manual + matrices | que el grafo UENS abra, se edite, se guarde, se exporte y conserve significado |

Ningún canal debe resolver todo por sí solo. La validación de dominio protege invariantes internas; la validación de producto protege promesas visibles.

## 5. Modelo de severidad obligatorio

Se mantiene el modelo de severidad ya existente:

```txt
BLOCKING
WARNING
INFO
```

La interpretación final debe ser:

| Severidad | Significado | Efecto esperado |
|---|---|---|
| `BLOCKING` | Inconsistencia que impide confiar en el grafo como artefacto canónico | debe mostrarse como bloqueo; no debe usarse para cerrar RC sin corrección o justificación explícita |
| `WARNING` | Debilidad fuerte o hueco de trazabilidad que requiere revisión humana | puede permitir guardar/exportar, pero debe quedar visible |
| `INFO` | Mejora de legibilidad, densidad, completitud o documentación | no bloquea, orienta refinamiento |

La UI puede agrupar `WARNING` en advertencia fuerte o suave, pero el contrato fuente debe conservar `BLOCKING`, `WARNING` e `INFO`.

## 6. Bloqueos esperados

La Tanda 42 debe evaluar como bloqueos, o como errores de construcción/importación equivalentes, al menos estos casos:

```txt
- código de nodo obligatorio vacío;
- título de nodo obligatorio vacío;
- código que no coincide con tipo semántico;
- nodo duplicado;
- relación duplicada;
- relación hacia nodo inexistente;
- relación sin origen o sin destino;
- relación semánticamente inválida según canConnect(...);
- relación que apunta al mismo nodo si no existe una justificación semántica explícita;
- grafo sin macroflujo cuando se valida como documento lógico derivable;
- documento importado inconsistente que no pueda reconstruirse como LogicalBusinessGraphDocument.
```

La creación de un proyecto vacío puede permitirse como estado inicial de edición. Sin embargo, al validar el documento como artefacto derivable o release/smoke, un grafo sin macroflujo debe reportarse como bloqueo o quedar explícitamente justificado como borrador.

## 7. Advertencias fuertes esperadas

La Tanda 42 debe reportar como advertencia fuerte al menos:

```txt
- MF sin FL;
- FL sin CU;
- CU sin ACC;
- ACC crítica sin PRE;
- ACC crítica sin POST;
- INV sin relación protege;
- POST sin relación garantiza;
- RN aislada o sin relación aplica/habilita/deriva_en;
- ENT aislada o sin relación crea/modifica/consulta/alimenta/deriva_en;
- REP sin fuente lógica o sin relación genera/alimenta/deriva_en;
- RISK sin elemento afectado;
- PEND sin elemento bloqueado;
- abuso de depende_de como sustituto de relaciones más precisas;
- nodo validado que todavía depende de pregunta pendiente crítica.
```

Estas advertencias no deben impedir guardar ni exportar. Sí deben aparecer en el panel de validación y en la acción global de validar proyecto.

## 8. Advertencias suaves e información

La validación puede emitir `INFO` o advertencias suaves para:

```txt
- nodo sin descripción;
- relación sin descripción;
- nodo sin referencias de fuente;
- grafo demasiado denso para lectura inmediata;
- relación cruzada que conviene revisar visualmente;
- código correcto pero título poco específico;
- ausencia de notas del documento;
- ejemplo importado sin referencias de fuente porque es plantilla mínima.
```

Estas observaciones deben ayudar a mejorar la documentación sin convertir la herramienta en un juez excesivamente rígido.

## 9. Criterios de calidad del Grafo lógico

Un Grafo lógico de negocio de calidad debe cumplir estas dimensiones:

| Dimensión | Criterio |
|---|---|
| Integridad | todos los nodos y relaciones son reconstruibles sin referencias rotas |
| Coherencia semántica | las relaciones respetan el vocabulario oficial y `canConnect(...)` |
| Trazas internas | reglas, acciones, entidades, estados, reportes, riesgos y preguntas tienen vínculos explicables |
| Derivabilidad | el grafo permite pasar del levantamiento lógico a artefactos revisables sin perder contexto |
| Legibilidad visual | el usuario puede identificar backbone, nodos críticos y pendientes sin depender de código fuente |
| Honestidad de producto | la validación no oculta huecos ni convierte warnings en éxito silencioso |
| Reproducibilidad | los hallazgos se pueden verificar con tests y smoke local |

## 10. Validación de backbone

El backbone recomendado sigue siendo:

```txt
MF → FL → CU → ACC
```

La validación no debe imponer un árbol rígido. El grafo puede tener relaciones cruzadas legítimas. Sin embargo, debe advertir cuando se pierda la estructura mínima que permite leer el negocio:

```txt
macroflujo sin flujo
flujo sin caso de uso
caso de uso sin acción
acción sin condiciones o efectos
```

El criterio final es: casi árbol como lectura principal, grafo como realidad semántica.

## 11. Validación de reglas, condiciones y efectos

La validación debe revisar especialmente:

```txt
RN aplica a flujos, casos, acciones, entidades o reportes.
PRE es requerida por FL, CU o ACC.
INV es protegida por FL, CU o ACC.
POST es garantizada por FL, CU o ACC.
ACC crea, modifica o consulta ENT/EST cuando transforma estado.
REP se genera, alimenta o deriva desde elementos lógicos verificables.
RISK y PEND deben bloquear o afectar algo concreto.
```

Esta validación conecta directamente con el fundamento lógico del negocio: precondiciones, invariantes y postcondiciones no son decoración; son garantías de corrección del flujo.

## 12. Validación de trazabilidad

El Grafo lógico debe poder responder:

```txt
¿Por qué existe esta entidad?
¿Qué acción crea o modifica este estado?
¿Qué regla aplica a este caso de uso?
¿Qué invariante protege esta operación?
¿Qué postcondición queda garantizada?
¿Qué riesgo o pregunta pendiente bloquea esta derivación?
```

Si el grafo no puede responder ninguna de esas preguntas para piezas centrales, debe advertirlo.

## 13. Validación de importación/exportación

La validación de calidad también debe incluir roundtrips:

```txt
Markdown oficial → importar → exportar Markdown → reimportar
Ejemplo UENS → importar → validar → exportar → reimportar
Proyecto .dms → guardar → abrir → validar
PNG/SVG → generar sin error y con contenido no vacío
```

El caso de grafo vacío debe resolverse explícitamente: exportar un grafo vacío no debe producir Markdown inválido ni relaciones hacia nodos inexistentes.

## 14. Integración con UI

La acción global:

```txt
Diagrama > Validar proyecto
```

debe validar el `logical-business-graph` cuando ese sea el proyecto activo. No debe caer al canvas conceptual ni mostrar mensajes genéricos de “no hay proyecto abierto” si el workspace del Grafo lógico está cargado.

El SideDock debe mostrar hallazgos de validación en una zona operativa, y la guía académica debe explicar el significado conceptual de los hallazgos sin duplicar instrucciones de botones.

## 15. Tests mínimos esperados para Tanda 42

La Tanda 42 debe agregar o reforzar tests como:

```txt
LogicalBusinessGraphValidationServiceTest
LogicalBusinessGraphDocumentSemanticIssuesTest
LogicalBusinessGraphMarkdownInvalidRelationsTest
LogicalBusinessGraphBackboneValidationTest
LogicalBusinessGraphTraceabilityValidationTest
LogicalBusinessGraphProjectValidationCoordinatorTest
LogicalBusinessGraphOfficialExampleValidationTest
LogicalBusinessGraphRoundTripValidationTest
```

No es obligatorio usar exactamente esos nombres, pero sí cubrir esas responsabilidades.

## 16. Criterio de cierre de validación integral

La validación integral se considera cerrada cuando:

```txt
- los bloqueos estructurales están cubiertos por dominio, parser o servicio de validación;
- los warnings fuertes del backbone y trazabilidad tienen tests;
- el ejemplo UENS valida sin bloqueos;
- las plantillas mínimas no generan falsos positivos graves;
- la acción global de validar proyecto enruta al Grafo lógico;
- el SideDock muestra o expone los hallazgos correctamente;
- el log local de Maven permite verificar los tests focalizados;
- las matrices documentales reflejan lo que realmente se valida.
```

## 17. Decisiones de no alcance

Esta alineación no implementa todavía:

```txt
- nuevo servicio de validación;
- cambios en semanticIssues();
- nuevas reglas de parser;
- integración con ProjectValidationCoordinator;
- cambios de UI;
- actualización de matrices de release.
```

Eso queda para Tanda 42 y para la alineación documental posterior.

## 18. Criterio de cierre de Alineación 006

Esta alineación se considera aplicada cuando exista este documento, haya guardarraíl fuente y los documentos vivos apunten al contrato.

No se considera cerrada la Tanda 42. Esta alineación solo fija el estándar para implementarla sin improvisar.

## 19. Tests focalizados esperados

```bat
scripts-validar-alineacion-06.bat
```

También puede ejecutarse directamente:

```bat
mvn -Dtest=Alineacion6ValidationQualityContractSourceTest test
```
