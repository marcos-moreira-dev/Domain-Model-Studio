# PF01 — Inventario de brechas por tipo visible

Estado: **ejecutada como planificación**  
Tipo: **solo planificación, sin implementación**  
Prioridad: **crítica**

## 1. Objetivo de esta tanda

Levantar un inventario fino entre la promesa visible del producto y las rutas reales del código. Esta tanda no corrige Java, CSS, Markdown ni recursos; deja escrito qué está cumplido, parcial, roto o sobredocumentado para que la implementación posterior no sea un parche grande.

La pregunta rectora es:

```txt
Si un tipo aparece como disponible, ¿puede el usuario crearlo, verlo como salida real, editarlo mínimamente, guardarlo, abrirlo y exportarlo según lo prometido?
```

## 2. Archivos revisados

Se revisaron estas zonas como fuente del inventario:

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/domain/catalog/DiagramTypeId.java
src/main/java/com/marcosmoreira/domainmodelstudio/domain/catalog/DiagramCapability.java
src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/DefaultDiagramTypeRegistry.java
src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/DefaultDiagramCapabilityCatalog.java
src/main/java/com/marcosmoreira/domainmodelstudio/application/workspace/DefaultCreateWorkspaceUseCase.java
src/main/java/com/marcosmoreira/domainmodelstudio/domain/diagram/DiagramProject.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/PresentationCompositionRoot.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellViewModel.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellState.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/DiagramCommandHandler.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ActiveOutputResolver.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DefaultDiagramToolbarActionProvider.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs/ManualContent.java
src/main/resources/css/*.css
src/main/resources/ai-resources/official-markdown/plantillas/*.md
src/main/resources/ai-resources/official-markdown/diagramas/*.md
examples/markdown/plantillas/*.md
examples/markdown/diagramas/*.md
src/test/java/com/marcosmoreira/domainmodelstudio/regression/ProductMinimumCoherenceTest.java
```

## 3. Leyenda de evaluación

| Estado | Significado |
|---|---|
| **Cumple** | La ruta está declarada y existe una implementación visible coherente. |
| **Parcial** | Hay infraestructura, pero falta conexión, consistencia de menús, pruebas o lenguaje. |
| **Roto** | El usuario puede llegar a un flujo que no muestra la salida real o contradice lo declarado. |
| **Falso positivo** | El catálogo, documentación o toolbar promete algo que no está garantizado por la interfaz. |
| **No aplica** | La función no debe existir para ese tipo según el contrato de producto. |

## 4. Inventario ejecutivo por tipo

| Tipo visible | Salida declarada | Estado global | Brecha principal | Prioridad |
|---|---:|---|---|---|
| Modelo conceptual | Visual | **Cumple con deuda** | Funciona como núcleo, pero conserva responsabilidades mezcladas y acciones globales no abstraídas. | Alta |
| Diccionario de datos | Documental | **Parcial** | Vista conectada, pero debe aclararse si no importa Markdown aunque tenga gramática/plantilla. | Media |
| Mapa de módulos | Visual | **Parcial** | Editor conectado; requiere coherencia de comandos globales, estado contextual y pruebas de ruta visual. | Alta |
| Roles y permisos | Visual | **Parcial** | Editor conectado; requiere estado contextual, pruebas y exportación por capacidad real. | Alta |
| Flujo de pantallas | Visual | **Parcial** | Editor conectado; requiere estado contextual y comandos de vista realmente dirigidos al editor. | Alta |
| Wireframes administrativos | Visual | **Parcial** | Editor conectado; falta consolidar concepto de maqueta/scaffolding y plantillas por pantalla. | Alta |
| UML Clases | Visual | **Parcial** | Editor conectado; debe reforzarse agrupación por módulo/directorio y comandos globales por workspace. | Alta |
| BPMN básico | Visual | **Roto** | ViewModel/editor existen, pero el editor no está montado en el shell; menú exportar no lo habilita. | Crítica |
| Flujo operativo | Visual | **Roto** | Misma brecha de familia comportamiento. | Crítica |
| UML Casos de uso | Visual | **Roto** | Captura confirma toolbar activa pero pantalla central sigue en inicio/sin proyecto. | Crítica |
| UML Actividad | Visual | **Roto** | Misma brecha de familia comportamiento. | Crítica |
| UML Secuencia | Visual | **Roto** | Misma brecha de familia comportamiento. | Crítica |
| UML Estados | Visual | **Roto** | Misma brecha de familia comportamiento. | Crítica |
| C4 Contexto | Visual | **Parcial** | Editor conectado; menú y estado deben depender de capacidades y del proyecto activo. | Alta |
| C4 Contenedores | Visual | **Parcial** | Misma familia arquitectura. | Alta |
| Despliegue técnico | Visual | **Parcial** | Misma familia arquitectura. | Alta |

## 5. Hallazgos transversales

### 5.1 Catálogo disponible no equivale todavía a producto cerrado

`DefaultDiagramTypeRegistry` marca todos los tipos oficiales como `AVAILABLE`. `DefaultDiagramCapabilityCatalog` declara para casi todos los visuales:

```txt
CREATE_PROJECT
IMPORT_MARKDOWN
SHOW_VISUAL_OUTPUT
MANUAL_EDITING
EXPORT_PNG
EXPORT_MARKDOWN
SAVE_DMS
LOAD_DMS
AI_RESOURCES
THEORY_HELP
```

Eso es una promesa fuerte de producto. Sin embargo, el shell todavía no tiene una ruta uniforme que garantice que cada tipo declarado como visual llegue a su editor real, menú correcto, estado correcto y exportaciones correctas.

### 5.2 `DefaultCreateWorkspaceUseCase` confía en el catálogo

`DefaultCreateWorkspaceUseCase` decide `PRODUCT_VIEW` cuando el descriptor está disponible y soporta salida visual o documental. Esa regla es buena, pero se vuelve peligrosa si el catálogo declara salida real antes de que el shell la conecte.

Conclusión: la implementación posterior debe proteger la coherencia con pruebas de ruta visual, no solo con pruebas de catálogo.

### 5.3 El shell tiene rutas duplicadas por tipo

Actualmente hay listas de tipos repetidas en varias partes:

```txt
MainShellView.installActiveDiagramWorkspaceBehavior()
MainShellView.*WorkspaceActive()
MainShellView.genericSidePanelsAvailable()
MainShellView.productWorkspaceMessage()
MainShellView.refreshWorkAreaPanels()
MainShellView.pngExportUnavailable()
MainShellView.markdownExportUnavailable()
MainShellCommandHandler.showProjectInEditor()
MainShellCommandHandler.requestValidateProject()
MainShellCommandHandler.isBatchExportableVisualDiagram()
DefaultDiagramToolbarActionProvider.actionsFor()
ActiveOutputResolver.activeOutput()
```

Esta duplicación explica por qué una familia puede estar presente en toolbar y exportador, pero ausente en el área central.

### 5.4 `BehaviorDiagramEditorView` existe, pero no se monta en `MainShellView`

Se encontró infraestructura de comportamiento:

```txt
BehaviorDiagramViewModel
BehaviorDiagramEditorView
BehaviorDiagramDocument
BehaviorMarkdownParser
BehaviorMarkdownExporter
BehaviorDiagramKind
BehaviorNodeKind
BehaviorEdgeKind
CSS behavior-diagram.css
iconos de actor, caso de uso, mensajes, estados, actividad, decisión, inicio y fin
comandos en MainShellCommandHandler
acciones en DefaultDiagramToolbarActionProvider
salida en ActiveOutputResolver
```

Pero en `MainShellView` no aparece:

```txt
import BehaviorDiagramEditorView
Parent behaviorRoot
new BehaviorDiagramEditorView(...)
behaviorRoot = behaviorEditor.getRoot()
behaviorWorkspaceActive()
selección de behaviorRoot en refreshWorkAreaPanels()
```

Por eso la familia BPMN/UML comportamiento es la brecha más crítica.

### 5.5 Exportación: `ActiveOutputResolver` sabe más que el menú

`ActiveOutputResolver` contempla salida para comportamiento y arquitectura. En cambio, `MainShellView.pngExportUnavailable()` y `markdownExportUnavailable()` tienen listas hardcodeadas que excluyen la familia de comportamiento.

Resultado probable:

```txt
Toolbar de UML/BPMN puede ofrecer PNG/Markdown.
ActiveOutputResolver podría resolver el output si el ViewModel está activo.
Menú superior Exportar puede dejar la acción apagada para esos tipos.
```

Conclusión: las acciones visibles deben consultar capacidades reales, no mantener listas dispersas.

### 5.6 Importación Markdown aplica layout Chen a todo

`ImportCommandHandler.importMarkdownFile()` ejecuta:

```txt
importMarkdownModelUseCase().importFile(...)
generateInitialChenLayoutUseCase().generate(result.project())
```

Eso es correcto para modelo conceptual, pero es conceptualmente incorrecto para:

```txt
BPMN
UML Casos de uso
UML Actividad
UML Secuencia
UML Estados
Wireframes
C4
Mapa de módulos
Roles y permisos
Flujo de pantallas
UML Clases
```

Aunque no siempre rompa, mezcla una regla de notación conceptual con todos los tipos. PF05 debe definir layout inicial por tipo.

### 5.7 Comandos globales siguen apuntando al canvas conceptual

`DiagramCommandHandler` valida, reorganiza y cambia notación usando `canvasViewModel.currentProject()`. Eso funciona para modelo conceptual, pero no para editores especializados. `MainShellCommandHandler.requestValidateProject()` ya hace varias derivaciones por editor activo, pero `requestRegenerateLayout()`, zoom, centrar y otras acciones todavía no tienen un contrato común por workspace.

Conclusión: PF03/PF04 deben extraer una fachada o contrato de workspace activo para no seguir sumando `if`.

### 5.8 Estado inferior incompleto

`MainShellState.showProjectState()` tiene tratamiento especial para:

```txt
Diccionario de datos
Mapa de módulos
UML Clases
```

Para los demás tipos cae a:

```txt
entityCount / relationshipCount del modelo conceptual
```

Eso produce mensajes falsos o pobres para:

```txt
Roles y permisos
Flujo de pantallas
Wireframes
BPMN
Flujo operativo
UML Casos de uso
UML Actividad
UML Secuencia
UML Estados
C4 Contexto
C4 Contenedores
Despliegue técnico
```

PF06 debe crear una política de resumen contextual por tipo.

### 5.9 Pruebas de cierre insuficientes

`ProductMinimumCoherenceTest` protege que existan capacidades, plantillas, ejemplos y documentación sin mensajes viejos. Pero no prueba:

```txt
que cada tipo visual tenga editor montado en MainShellView
que el menú Exportar habilite lo mismo que el catálogo
que la pestaña activa cambie workspace, toolbar y paneles juntos
que comportamiento tenga tests de parser/exporter similares a arquitectura, wireframes y UML clases
que la ayuda tenga una ficha robusta por tipo
```

PF09 debe cerrar esta brecha.

## 6. Inventario por familia

### 6.1 Modelo conceptual

**Estado:** cumple con deuda.

Cumple:

```txt
Canvas principal conectado.
Árbol de estructura conectado.
Inspector conectado.
Notaciones Chen/Pata de gallo.
Exportación SVG/PNG/Markdown.
Validación conceptual.
Edición manual.
Persistencia .dms.
Ejemplos importables.
```

Brechas:

```txt
Sigue siendo el camino por defecto cuando otro tipo no está ruteado.
Acciones globales de modelo/notación/reorganización están demasiado centradas en conceptual.
MainShellView y MainShellCommandHandler no deben crecer más alrededor de este caso.
```

Decisión de planificación:

```txt
Mantener conceptual como editor base, pero impedir que funcione como fallback silencioso para tipos no conceptuales.
```

### 6.2 Diccionario de datos

**Estado:** parcial.

Cumple:

```txt
Editor documental conectado.
ViewModel propio.
Exportación PDF/Markdown declarada.
Persistencia especializada en DiagramProject.
CSS propio.
```

Brechas:

```txt
No declara IMPORT_MARKDOWN, pero tiene plantilla/gramática; hay que aclarar en UI y ayuda si la plantilla es solo recurso IA o si luego será importable.
La barra de estado solo cubre conteo de entidades/campos cuando el proyecto está activo, pero falta contrato común de status.
```

Decisión de planificación:

```txt
No forzar importación si no está prometida. Documentar honestamente: salida documental editable y exportable.
```

### 6.3 Familia administrativa visual

Incluye:

```txt
Mapa de módulos
Roles y permisos
Flujo de pantallas
Wireframes administrativos
```

**Estado:** parcial.

Cumple en general:

```txt
Documentos de dominio especializados.
ViewModels especializados.
EditorViews especializados.
CSS propio.
Toolbars específicas.
Importación Markdown declarada para la mayoría.
Exportación en ActiveOutputResolver.
Persistencia dentro de DiagramProject.
```

Brechas:

```txt
Estado inferior incompleto para roles, flujo de pantallas y wireframes.
Comandos globales de vista y edición no están centralizados por workspace.
El concepto de wireframes debe quedar como maqueta/scaffolding, no diseñador visual complejo.
Ejemplos están fragmentados por dominios distintos, no como familia UENS/colegio.
```

Decisión de planificación:

```txt
Mantener estas vistas como producto real, pero alinearlas con estado contextual, menú por capacidad y ejemplos gorditos.
```

### 6.4 UML Clases

**Estado:** parcial.

Cumple:

```txt
Editor visual conectado.
ViewModel propio.
Toolbars para clases, interfaces, enums, métodos y relaciones.
Import/export Markdown.
Exportación PNG.
Persistencia propia.
```

Brechas:

```txt
Debe reforzarse el requisito posterior de agrupadores visuales por directorio/módulo/paquete.
Comandos globales de layout/vista/exportación deben depender del workspace activo.
La ayuda debe explicar cuándo usar UML Clases para leer proyectos reales y cómo pedir Markdown a una IA.
```

### 6.5 Familia de comportamiento

Incluye:

```txt
BPMN básico
Flujo operativo
UML Casos de uso
UML Actividad
UML Secuencia
UML Estados
```

**Estado:** roto de cara al usuario.

Cumple internamente:

```txt
ViewModel existe.
EditorView existe.
Documentos de dominio existen.
Parser y exporter Markdown existen.
Toolbars específicas existen.
Comandos de agregar/eliminar/validar existen.
ExportableOutput existe en ActiveOutputResolver.
Persistencia especializada existe.
CSS e iconos existen.
```

Roto:

```txt
MainShellView no monta BehaviorDiagramEditorView.
No hay behaviorRoot.
No hay behaviorWorkspaceActive().
refreshWorkAreaPanels() nunca escoge behaviorRoot.
genericSidePanelsAvailable() no excluye comportamiento como workspace propio.
productWorkspaceMessage() no tiene mensajes para comportamiento.
Menú Exportar no habilita PNG/Markdown para esta familia.
MainShellState no calcula conteos contextuales.
No se encontraron tests dedicados de BehaviorMarkdownParser/Exporter en src/test.
```

Decisión de planificación:

```txt
La primera implementación funcional debe conectar comportamiento, pero no con un parche gigante: PF02/PF03 deben diseñar router/contrato para que no se repita este fallo.
```

### 6.6 Familia arquitectura

Incluye:

```txt
C4 Contexto
C4 Contenedores
Despliegue técnico
```

**Estado:** parcial.

Cumple:

```txt
Editor visual conectado en MainShellView.
ViewModel propio.
Parser/exporter Markdown.
Toolbars específicas.
ExportableOutput.
Persistencia propia.
CSS propio.
```

Brechas:

```txt
Estado inferior cae a conteos conceptuales.
Exportaciones están hardcodeadas en menú, no derivadas del catálogo.
Comandos globales de vista/reorganización no tienen contrato común de workspace.
```

## 7. Brechas de lenguaje visible

Se detecta lenguaje aún demasiado centrado en modelo conceptual:

```txt
Menú Modelo
Validar modelo
Reorganizar diagrama conectado al canvas conceptual
Mostrar estructura del modelo
Importar modelo Markdown
Pantalla de inicio con énfasis conceptual
```

No todo debe cambiarse a la fuerza, pero PF02/PF04/PF07 deben distinguir:

```txt
Acciones generales de proyecto/app
Acciones específicas del diagrama activo
Acciones exclusivas de modelo conceptual
```

Regla:

```txt
Si una acción solo sirve para conceptual, no debe aparecer como si sirviera para todos los tipos.
```

## 8. Brechas de arquitectura de código

### 8.1 `MainShellCommandHandler` es demasiado grande

Tiene aproximadamente 1650 líneas y concentra:

```txt
sesiones de pestañas
creación de proyectos
sincronización de editores
validación
exportación
edición conceptual
edición especializada
batch export
comandos de comportamiento
comandos de arquitectura
mensajes de UI
```

No conviene agregar más casos ahí. La implementación debe extraer responsabilidades antes o durante la corrección.

### 8.2 `MainShellView` mezcla composición y decisión de tipo

`MainShellView` debería componer la pantalla, pero hoy también decide a mano:

```txt
qué workspace está activo
qué paneles se muestran
qué mensajes mostrar
qué exportaciones están habilitadas
qué root poner en el centro
```

PF02 debe mover esas decisiones a una política/ruteador.

### 8.3 Riesgo de mega-fachada

Se recomienda usar fachadas, pero con límites claros. No reemplazar un `MainShellCommandHandler` gigante por una única `WorkspaceFacade` gigante. Mejor separar:

```txt
WorkspaceRouter
WorkspaceSessionPresenter
WorkspaceExportPolicy
WorkspaceStatusSummaryProvider
WorkspaceActionDispatcher
WorkspacePanelPolicy
```

## 9. Orden de atención recomendado tras PF01

1. Diseñar shell multitipo real en PF02.
2. Definir contrato de workspace/editor activo en PF03.
3. Alinear menús/toolbars/exportaciones con capacidades en PF04.
4. Corregir importación/layout por tipo en PF05.
5. Corregir barra de estado y paneles contextuales en PF06.
6. Rehacer ayuda como micro-Wikipedia en PF07.
7. Planificar ejemplos UENS/colegio en PF08.
8. Blindar con pruebas en PF09.

## 10. Criterio de cierre de PF01

PF01 queda cerrada cuando este documento sirve para decidir qué implementar primero sin volver a revisar desde cero el proyecto. No implica que el programa esté corregido.

