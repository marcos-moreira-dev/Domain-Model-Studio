# Alineación 001 — Contrato final de producto

Estado: **alineación aplicada**  
Fecha: **2026-05-24**  
Alcance: **contrato de producto, disponibilidad, capacidades y cierre del Grafo lógico del negocio**  
Tipo de cambio: **documental y de guardarraíl; no modifica lógica de ejecución**

## 1. Propósito

Esta alineación convierte las lecturas masivas del proyecto en un contrato explícito para decidir cuándo un tipo de proyecto puede considerarse productivo dentro de Domain Model Studio.

La regla principal es:

```txt
capacidad visible = implementación real + prueba verificable + documentación trazable + smoke cuando corresponda
```

La alineación no corrige todavía catálogo, toolbar, persistencia, exportaciones, teoría ni validación. Su función es fijar el criterio que deberán cumplir las tandas siguientes antes de declarar el cierre del producto.

## 2. Jerarquía de verdad

Cuando haya contradicciones entre archivos, se usará esta prioridad:

1. log local más reciente de tests y smoke;
2. código actual;
3. catálogo Java de tipos y capacidades;
4. documentación viva de producto;
5. bitácoras históricas.

La documentación antigua conserva valor histórico, pero no puede contradecir el código ni el log más reciente.

## 3. Contrato mínimo de tipo productivo

Un tipo de proyecto visible se considera productivo solo si cumple todos los puntos aplicables:

| Dimensión | Criterio obligatorio |
|---|---|
| Identidad | ID oficial estable en `DiagramTypeId`. |
| Catálogo | Entrada única en `DefaultDiagramTypeDefinitions`. |
| Categoría | Categoría oficial coherente con su propósito. |
| Estado | `AVAILABLE` si abre workspace real; `IN_PREPARATION` si solo ofrece placeholder. |
| Workspace | Vista real coherente con `DiagramWorkspaceKind`; no debe abrir como `UNSUPPORTED`. |
| Capacidades | Solo declarar capacidades implementadas y comprobables. |
| Toolbar | La toolbar contextual debe exponer acciones coherentes con las capacidades declaradas. |
| Importación | Si declara `IMPORT_MARKDOWN`, debe tener parser, dispatcher, ejemplos y test. |
| Exportación | Si declara PNG/SVG/Markdown/PDF, el menú/toolbar debe resolver salida activa real. |
| Persistencia | Si declara `SAVE_DMS` / `LOAD_DMS`, debe guardar y reabrir el documento especializado. |
| Recursos IA | Si declara `AI_RESOURCES`, debe tener descriptor, archivo, exportación y documentación. |
| Guía académica | Si declara `THEORY_HELP`, debe tener tema académico, cobertura y figura didáctica. |
| Ayuda operativa | Si usa SideDock, debe tener ayuda operativa específica o genérica coherente. |
| Validación | Debe tener validación mínima útil para su dominio. |
| Matrices | Debe aparecer en capacidades, casos de uso, smoke y release cuando corresponda. |

## 4. Estados permitidos

El contrato final evita estados híbridos.

| Estado | Regla de uso |
|---|---|
| `AVAILABLE` | El tipo abre como producto real y sus capacidades declaradas son ejecutables. |
| `IN_PREPARATION` | El tipo no abre workspace productivo; debe usar placeholder honesto. |
| `EXPERIMENTAL` | No debe quedar como estado final de un tipo visible con workspace real, salvo que exista una política explícita para abrirlo y testearlo como experimental. |
| `DOCUMENTATION_AVAILABLE` | Solo válido para documentación sin workspace productivo. |
| `TEMPLATE_AVAILABLE` | Solo válido para plantilla sin workspace productivo. |

Decisión de alineación: **el Grafo lógico del negocio no debe quedar indefinidamente como `EXPERIMENTAL` si se mantiene visible y abre workspace real**. Debe cerrarse como `AVAILABLE` después de completar T40, T41 y T42, o volver a preparación si se decide no productivizarlo.

## 5. Tipos oficiales que deben mantenerse trazados

Todo documento de capacidades, casos de uso, smoke y release debe poder mencionar o justificar estos 19 IDs:

| ID | Nombre operativo | Estado de alineación |
|---|---|---|
| `logical-business-intake` | Levantamiento lógico | Productivo documental. |
| `logical-business-graph` | Grafo lógico del negocio | Candidato a productivo; requiere cierre técnico. |
| `conceptual-model` | Modelo conceptual | Productivo y congelado. |
| `data-dictionary` | Diccionario de datos | Productivo documental. |
| `bpmn-basic` | BPMN básico | Productivo visual. |
| `operational-flow` | Flujo operativo | Productivo visual. |
| `c4-context` | C4 Contexto | Productivo visual. |
| `c4-containers` | C4 Contenedores | Productivo visual. |
| `technical-deployment` | Despliegue técnico | Productivo visual. |
| `uml-class` | UML Clases | Productivo visual con importación de código. |
| `uml-use-case` | UML Casos de uso | Productivo visual. |
| `uml-activity` | UML Actividad | Productivo visual. |
| `uml-sequence` | UML Secuencia | Productivo visual con fragmentos combinados. |
| `uml-state` | UML Estados | Productivo visual. |
| `admin-module-map` | Mapa de módulos | Productivo visual. |
| `roles-permissions-map` | Roles y permisos | Productivo matricial. |
| `screen-flow` | Flujo de pantallas | Productivo visual. |
| `admin-wireframes` | Wireframes administrativos | Productivo visual. |
| `free-graph` | Grafo libre | Productivo visual libre. |

## 6. Zonas protegidas

Quedan protegidas para las tandas de cierre:

```txt
pantalla de inicio;
modelo conceptual;
canvas conceptual;
DiagramCanvasView;
DiagramCanvasViewModel;
ChenDiagramRenderer;
CrowsFootDiagramRenderer.
```

No se deben usar como fuente de migración para el Grafo lógico, SideDock transversal, workbenches especializados ni nuevas tandas de UI.

## 7. Frontera de ayudas

La ayuda queda separada en dos contratos:

| Entrada | Propósito | Contenido permitido |
|---|---|---|
| Menú `Ayuda` | Guía académica teórica | teoría, notación, fundamentos, cuándo usar/no usar, errores comunes, figuras didácticas. |
| Botón `Ayuda` del SideDock | Ayuda operativa de herramienta | cómo usar el tipo activo, paneles, selección, validación, exportación y lectura operativa. |

Decisión de alineación: la ventana del menú debe nombrarse como **Guía académica**, no como guía operativa. La ayuda operativa debe permanecer en el SideDock.

## 8. Contrato objetivo del Grafo lógico del negocio

El Grafo lógico del negocio queda definido como:

```txt
vista visual semántica derivada del levantamiento lógico,
orientada a revisar trazabilidad, reglas, acciones,
condiciones, entidades, estados, reportes, riesgos y preguntas pendientes.
```

No es:

```txt
grafo libre renombrado;
BPMN;
UML;
modelo conceptual;
sustituto del levantamiento lógico;
árbol rígido puro.
```

### 8.1 Nodos objetivo

```txt
MF, FL, CU, ACC, RN, PRE, INV, POST, ENT, EST, REP, RISK, PEND
```

### 8.2 Relaciones objetivo

```txt
contiene, usa, reutiliza, ejecuta, aplica, requiere, protege, garantiza,
crea, modifica, consulta, genera, alimenta, bloquea, habilita,
depende_de, deriva_en
```

### 8.3 Capacidades objetivo para cierre productivo

El cierre técnico del Grafo lógico deberá tender a este contrato:

```txt
CREATE_PROJECT
IMPORT_MARKDOWN
SHOW_VISUAL_OUTPUT
MANUAL_EDITING
EXPORT_MARKDOWN
EXPORT_PNG
EXPORT_SVG
SAVE_DMS
LOAD_DMS
AI_RESOURCES
THEORY_HELP
```

Cada capacidad debe tener implementación, test y documentación antes de declararse final.

## 9. Obligaciones antes de declarar `logical-business-graph` como `AVAILABLE`

| Área | Obligación |
|---|---|
| Catálogo | Estado y capacidades coherentes; ejemplo mínimo distinto del UENS completo. |
| Workspace | Abrir como `PRODUCT_VIEW`, no como `UNSUPPORTED`. |
| Toolbar | Contributor propio; no mostrar acciones irrelevantes como quitar punto si no hay bendpoints. |
| Validación global | `Diagrama > Validar` debe usar validación del Grafo lógico. |
| Exportación activa | Markdown, PNG y SVG deben estar conectados desde menú/toolbar. |
| Persistencia | `.dms` debe guardar/reabrir documento y layout especializado. |
| Guía académica | Tema teórico propio con figura didáctica y micro-wikipedia. |
| Recursos IA | Capacidad declarada, docs actualizados y prompt no importable aclarado. |
| Matrices | Capacidades, casos de uso, smoke, release y recursos IA actualizados. |
| Validación integral | Issues semánticos con bloqueos, advertencias fuertes y advertencias suaves. |
| Estilo visual | UI nueva/corregida sin bordes redondeados ornamentales. |

## 10. Regla visual vigente

Para cualquier UI nueva o corregida:

```txt
sin bordes redondeados ornamentales;
sin -fx-border-radius distinto de 0;
sin -fx-background-radius distinto de 0;
no introducir CornerRadii no-cero en componentes de UI estructural.
```

No se debe aplicar limpieza global ciega sobre formas semánticas de diagramas. Eventos BPMN, casos de uso UML, estados finales u otras figuras teóricas pueden conservar su geometría propia si la notación lo exige.

## 11. Tests de comprobación agregados

Esta alineación agrega un guardarraíl documental:

```bat
mvn -Dtest=Alineacion1ProductContractSourceTest test
```

El test verifica que este contrato exista, que mencione los 19 tipos oficiales, que declare la frontera de ayuda académica/operativa, que preserve zonas protegidas, que incluya la regla visual sin radius y que liste las obligaciones del Grafo lógico antes de declararlo `AVAILABLE`.

## 12. Tandas posteriores que dependen de esta alineación

Después de esta alineación, el orden recomendado es:

1. Alineación 2 — Fronteras arquitectónicas y zonas prohibidas.
2. Alineación 3 — Contrato semántico del Grafo lógico del negocio.
3. Alineación 4 — Catálogo, capacidades, toolbar, workspace y exportación.
4. Alineación 5 — Ayuda académica, ayuda operativa y recursos IA.
5. Alineación 6 — Validación integral y criterios de calidad.
6. Alineación 7 — Documentación anti-fachada, matrices y release.
7. Alineación 8 — Plan quirúrgico de correcciones.
8. Tanda 40 — Guía académica del Grafo lógico.
9. Tanda 41 — Persistencia `.dms` y exportaciones.
10. Tanda 42 — Validación integral del nuevo proyecto.
11. Tanda 31 — Validación local Windows / Release Candidate.
12. Tanda 9 — Deuda SRP focalizada, solo si aparece bloqueo real.

## 13. Criterio de salida de esta alineación

Esta alineación se considera completa si:

```txt
- el contrato final de producto existe;
- los 19 tipos oficiales quedan trazados en el contrato;
- el Grafo lógico queda definido como candidato a producto, no como tipo final aún;
- las zonas protegidas quedan explícitas;
- la frontera de ayuda académica/operativa queda explícita;
- la regla visual sin bordes redondeados queda explícita;
- existe test fuente para verificar el contrato.
```
