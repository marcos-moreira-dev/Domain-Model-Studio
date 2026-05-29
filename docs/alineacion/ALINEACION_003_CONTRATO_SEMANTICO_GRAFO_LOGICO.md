# Alineación 003 — Contrato semántico del Grafo lógico del negocio

Estado: **alineación aplicada**  
Fecha: **2026-05-24**  
Alcance: **semántica del tipo `logical-business-graph`, vocabulario, relaciones, validación esperada y frontera con el Levantamiento lógico**  
Tipo de cambio: **documental y de guardarraíl; no modifica lógica de ejecución**

## 1. Propósito

Esta alineación fija qué significa semánticamente el **Grafo lógico del negocio** dentro de Domain Model Studio antes de cerrar catálogo, persistencia, exportaciones, guía académica y validación integral.

El objetivo es evitar que `logical-business-graph` quede reducido a un canvas de nodos o a un grafo libre con otro nombre. El tipo debe tener identidad propia, trazabilidad con el Levantamiento lógico y reglas de lectura coherentes con la documentación canónica de proyectos administrativos.

El dominio semántico del Grafo lógico no debe reutilizarse en Grafo libre ni depender del canvas conceptual.

Regla principal:

```txt
El Grafo lógico del negocio es una vista visual semántica derivada/revisable del Levantamiento lógico; no es un grafo libre renombrado.
```

## 2. Definición final del artefacto

El Grafo lógico del negocio se define como:

```txt
vista visual semántica del negocio que conecta macroflujos, flujos, casos de uso,
acciones transformadoras, reglas, condiciones lógicas, entidades, estados,
reportes, riesgos y preguntas pendientes para revisar trazabilidad y coherencia.
```

Debe ayudar a responder preguntas como:

- qué macroflujos existen;
- qué flujos o microflujos componen cada macroflujo;
- qué casos de uso materializan esos flujos;
- qué acciones transforman el estado del negocio;
- qué reglas aplican;
- qué precondiciones se requieren;
- qué invariantes se protegen;
- qué postcondiciones se garantizan;
- qué entidades, estados y reportes nacen o son consultados;
- qué riesgos y preguntas pendientes bloquean una decisión.

## 3. Lo que no es

El Grafo lógico del negocio no debe confundirse con:

| No es | Razón |
|---|---|
| Grafo libre | Sus nodos y relaciones son tipados; no son etiquetas arbitrarias. |
| BPMN | No modela notación BPMN formal, compuertas ni eventos BPMN. |
| UML Casos de uso | Puede contener CU, pero no reemplaza el diagrama UML de actores/casos. |
| UML Actividad | Puede sugerir flujo, pero no modela control de actividad ni swimlanes UML. |
| Modelo conceptual | Puede mencionar ENT, pero no reemplaza entidades, atributos, cardinalidades ni notación Chen/Pata de gallo. |
| Diccionario de datos | Puede justificar campos o reportes, pero no reemplaza definición de atributos. |
| Levantamiento lógico | Es una vista derivada/revisable; el expediente fuente sigue siendo el Levantamiento lógico. |
| Árbol rígido puro | Tiene una columna vertebral jerárquica, pero permite reutilización, dependencias y bloqueos cruzados. |

## 4. Relación con el Levantamiento lógico

El Levantamiento lógico es la fuente lógica canónica documental. El Grafo lógico es una vista visual compatible y revisable.

```txt
Levantamiento lógico
→ reglas, acciones, condiciones, entidades, estados, reportes, riesgos y pendientes
→ Grafo lógico del negocio
→ revisión de trazabilidad y coherencia
→ posibles derivaciones posteriores a diagramas/documentos especializados
```

La derivación nunca debe interpretarse como certificación automática. La salida del Grafo lógico debe revisarse humanamente antes de usarla como base para modelo conceptual, BPMN, UML, diccionario, roles, pantallas o wireframes.

## 5. Principio estructural: casi árbol, pero no árbol

El Grafo lógico tiene una columna vertebral recomendada:

```txt
MF → FL → CU → ACC
```

Pero no debe imponerse como árbol puro, porque en un negocio real:

- un caso de uso puede reutilizarse en más de un flujo;
- una acción puede proteger varias invariantes;
- una regla puede aplicar a varios nodos;
- una entidad puede ser creada por una acción y consultada por otra;
- un reporte puede alimentarse de varias entidades;
- un riesgo o pregunta pendiente puede bloquear más de una decisión.

Por eso, la estructura esperada es un **grafo semántico con backbone legible**, no una jerarquía cerrada.

## 6. Vocabulario oficial de nodos

El tipo `logical-business-graph` reconoce estos nodos oficiales:

| Prefijo | Nombre | Lectura semántica |
|---|---|---|
| `MF` | Macroflujo | Gran operación o área lógica del negocio. |
| `FL` | Flujo o microflujo | Variante operativa concreta dentro de un macroflujo. |
| `CU` | Caso de uso | Interacción funcional reutilizable entre actor y sistema. |
| `ACC` | Acción transformadora | Operación que transforma el estado del negocio. |
| `RN` | Regla de negocio | Condición, restricción, política o decisión del dominio. |
| `PRE` | Precondición | Condición que debe cumplirse antes de ejecutar un flujo, caso o acción. |
| `INV` | Invariante | Verdad que debe mantenerse durante la operación. |
| `POST` | Postcondición | Verdad que debe quedar garantizada al cerrar la operación. |
| `ENT` | Entidad candidata | Concepto persistible o relevante para diseño de datos. |
| `EST` | Estado | Situación válida del negocio durante su ciclo de vida. |
| `REP` | Reporte | Salida informativa derivada de datos, reglas o acciones. |
| `RISK` | Riesgo | Amenaza lógica, operativa, documental o de calidad. |
| `PEND` | Pregunta pendiente | Duda que impide validar completamente una decisión. |

Las abreviaciones pueden usarse en UI, Markdown, ejemplos y guías, pero siempre debe existir una leyenda visible o una ayuda fácil de encontrar.

## 7. Vocabulario oficial de relaciones

El tipo reconoce estas relaciones dirigidas:

| Relación | Lectura semántica |
|---|---|
| `contiene` | Agrupa jerárquicamente un elemento lógico. |
| `usa` | Usa un caso, acción o elemento lógico sin redefinirlo. |
| `reutiliza` | Reaprovecha un caso de uso o acción en otro flujo. |
| `ejecuta` | Dispara o materializa una acción transformadora. |
| `aplica` | Aplica una regla a un flujo, caso, acción, entidad o reporte. |
| `requiere` | Exige una precondición o requisito lógico previo. |
| `protege` | Mantiene una invariante durante la operación. |
| `garantiza` | Asegura una postcondición o cierre verificable. |
| `crea` | Crea una entidad, estado, reporte o evidencia lógica. |
| `modifica` | Actualiza una entidad o estado del negocio. |
| `consulta` | Lee una entidad, estado o reporte sin transformarlo. |
| `genera` | Produce un reporte, estado o evidencia. |
| `alimenta` | Aporta datos o evidencia a otro elemento. |
| `bloquea` | Impide validar o ejecutar otro elemento mientras siga abierto. |
| `habilita` | Permite avanzar cuando se satisface una condición lógica. |
| `depende_de` | Declara dependencia lógica o de información. |
| `deriva_en` | Justifica la existencia de un artefacto derivado. |

## 8. Lectura canónica de relaciones

La lectura preferente para el backbone es:

```txt
MF contiene FL
FL usa CU
CU usa ACC
CU ejecuta ACC
FL ejecuta ACC
```

La lectura preferente para reglas y condiciones es:

```txt
RN aplica MF/FL/CU/ACC/ENT/REP
FL/CU/ACC requiere PRE
FL/CU/ACC protege INV
FL/CU/ACC garantiza POST
PRE/POST/RN habilita FL/CU/ACC
```

La lectura preferente para datos, estados y reportes es:

```txt
CU/ACC crea ENT/EST
CU/ACC modifica ENT/EST
CU/ACC consulta ENT/EST
FL/CU/ACC genera REP
ENT/EST/REP alimenta REP/ACC/CU
RN/ACC/CU/FL/INV/POST deriva_en ENT/REP/EST/CU/ACC
```

La lectura preferente para riesgos y preguntas es:

```txt
RISK bloquea nodo afectado
PEND bloquea nodo afectado
```

La relación `depende_de` queda permitida como relación genérica, pero debe usarse con moderación. Si todo se conecta con `depende_de`, el grafo pierde valor semántico.

## 9. Decisiones semánticas cerradas

### 9.1 `contiene` se mantiene como jerarquía principal MF → FL

La relación `contiene` debe reservarse para la agrupación fuerte del macroflujo sobre sus flujos o microflujos.

```txt
MF contiene FL
```

Para conectar flujos con casos de uso se prefiere:

```txt
FL usa CU
```

Motivo: un caso de uso puede reutilizarse en varios flujos. Si se modela como contención rígida, el grafo se vuelve árbol artificial.

### 9.2 `usa` y `reutiliza` deben separar uso principal de reutilización

`usa` representa uso funcional normal.  
`reutiliza` representa reaprovechamiento explícito de un CU o acción ya existente.

### 9.3 `RISK` y `PEND` no son basura documental

Riesgos y preguntas pendientes son nodos legítimos del grafo porque ayudan a no ocultar incertidumbre. Deben conectarse con `bloquea` al elemento afectado.

### 9.4 `ENT` no sustituye el modelo conceptual

Un nodo `ENT` es candidato o referencia lógica. La entidad formal con atributos, relaciones y cardinalidades pertenece al modelo conceptual o al diccionario de datos.

## 10. Decisiones de ajuste para tandas técnicas posteriores

Esta alineación no modifica código, pero deja identificados ajustes semánticos que deben resolverse en T42 o en la tanda técnica que corresponda:

| Punto | Estado actual | Decisión de alineación |
|---|---|---|
| `consulta` sobre `REP` | La descripción dice entidad, estado o reporte; la implementación actual debe revisarse. | Si una acción/caso lee un reporte sin transformarlo, `consulta` debería poder apuntar a `REP`. |
| `EST habilita` | Un estado puede habilitar una acción o caso en procesos reales. | Evaluar permitir `EST habilita FL/CU/ACC` si se usa en ejemplos/guía. |
| Eliminación de relación | El dominio permite actualizar relaciones, pero falta operación directa de eliminación de relación. | Agregar `withoutEdge(...)` solo si la UI promete CRUD estructural. |
| Edición manual | Hoy debe entenderse como edición de cabecera, propiedades, selección, movimiento y relaciones/nodos existentes. | CRUD estructural completo no es obligatorio para `AVAILABLE` salvo que toolbar/capacidades lo prometan explícitamente. |
| IDs de relación | Los códigos de nodo se normalizan fuerte; los IDs de relación requieren convención final. | Definir canonicalización o documentar sensibilidad antes de ampliar CRUD. |

## 11. Contrato de edición manual

Para el cierre productivo inicial, `MANUAL_EDITING` significa como mínimo:

```txt
- editar datos generales del documento;
- seleccionar nodos y relaciones;
- editar título, descripción, estado y referencias de nodos existentes;
- editar descripción de relaciones existentes;
- mover nodos y conservar layout;
- revisar leyenda, estructura y validación.
```

No significa automáticamente:

```txt
- crear nodos desde cero en canvas;
- crear relaciones por arrastre;
- eliminar nodos desde toolbar;
- eliminar relaciones desde toolbar;
- convertir el grafo en FreeGraph.
```

Si esas operaciones se agregan, deben entrar como capacidad operativa explícita, con casos de uso y tests.

## 12. Contrato de validación esperado

La validación final debe distinguir entre bloqueos, advertencias fuertes y advertencias suaves.

### 12.1 Bloqueos

```txt
- relación apunta a nodo inexistente;
- código no coincide con tipo;
- nodo obligatorio sin título;
- duplicados de nodo o relación;
- relación semánticamente inválida;
- documento importado inconsistente;
- grafo sin macroflujo cuando se pretende validar como completo.
```

### 12.2 Advertencias fuertes

```txt
- MF sin FL;
- FL sin CU;
- CU sin ACC;
- ACC crítica sin PRE o POST;
- INV sin protección;
- POST sin garantía;
- RN aislada;
- ENT aislada;
- REP sin fuente lógica;
- RISK sin nodo afectado;
- PEND sin elemento bloqueado.
```

### 12.3 Advertencias suaves

```txt
- nodo sin descripción;
- relación sin descripción;
- nodo sin referencias;
- demasiadas relaciones genéricas depende_de;
- grafo visualmente denso o difícil de leer.
```

## 13. Contrato de derivación desde Levantamiento lógico

La derivación desde `logical-business-intake` hacia `logical-business-graph` debe producir un borrador revisable, no una certificación.

Debe intentar preservar:

```txt
MF, FL, CU, ACC, RN, PRE, INV, POST, ENT, EST, REP, RISK, PEND
```

Y relaciones compatibles como:

```txt
contiene, usa, ejecuta, aplica, requiere, protege, garantiza,
crea, modifica, consulta, genera, alimenta, bloquea, habilita,
depende_de, deriva_en
```

Si el levantamiento no trae suficiente estructura, puede crear un backbone mínimo para facilitar revisión, pero debe mantener la trazabilidad y no inventar certeza funcional.

## 14. Contrato visual semántico

El layout recomendado debe favorecer esta lectura:

```txt
MF → FL → CU → ACC
          ↘ RN / PRE / INV / POST / ENT / EST / REP / RISK / PEND
```

No es obligatorio que el layout sea perfecto en T41/T42, pero sí debe evitar que el grafo se lea como una nube arbitraria sin jerarquía ni trazabilidad.

## 15. Relación con guías y ayudas

La guía académica debe explicar teoría y lectura del artefacto:

```txt
qué es, para qué sirve, elementos, relaciones, casos especiales,
cuándo usarlo, cuándo no usarlo y errores comunes.
```

La ayuda operativa del SideDock debe explicar operación de herramienta:

```txt
cómo seleccionar, mover, editar propiedades, leer leyenda, revisar validación y exportar.
```

Ninguna de las dos debe reemplazar a la otra.

## 16. Guardarraíl agregado

Esta alineación agrega el test:

```bat
mvn -Dtest=Alineacion3LogicalBusinessGraphSemanticContractSourceTest test
```

El test verifica que este contrato exista, que preserve el vocabulario completo de nodos y relaciones, que declare el carácter casi árbol pero no árbol, que mantenga la frontera con Grafo libre y Levantamiento lógico, y que el código fuente conserve los enums semánticos principales.

## 17. Tandas que dependen de esta alineación

Esta alineación alimenta directamente:

1. Alineación 4 — Catálogo, capacidades, toolbar, workspace y exportación.
2. Alineación 5 — Ayuda académica, ayuda operativa y recursos IA.
3. Alineación 6 — Validación integral y criterios de calidad.
4. Tanda 40 — Guía académica del Grafo lógico.
5. Tanda 42 — Validación integral del nuevo proyecto.

