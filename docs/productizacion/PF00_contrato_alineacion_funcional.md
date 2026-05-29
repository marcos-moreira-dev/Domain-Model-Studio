# PF00 — Contrato de alineación funcional para productización

Estado: **ejecutada como planificación**  
Tipo: **solo planificación, sin implementación**  
Prioridad: **crítica**

## 1. Objetivo de esta tanda

Congelar la promesa real del producto antes de seguir implementando. Domain Model Studio no debe avanzar agregando más botones, ejemplos o documentación si antes no queda escrito qué significa que un tipo de diagrama esté realmente disponible.

La meta de esta tanda no es corregir código todavía. La meta es dejar un contrato fino para que las siguientes tandas de planificación e implementación no se conviertan en parches grandes dentro del shell.

## 2. Lectura preliminar realizada

Se revisaron estas zonas del proyecto para fundamentar la planificación:

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/DefaultDiagramTypeRegistry.java
src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/DefaultDiagramCapabilityCatalog.java
src/main/java/com/marcosmoreira/domainmodelstudio/application/workspace/DefaultCreateWorkspaceUseCase.java
src/main/java/com/marcosmoreira/domainmodelstudio/domain/catalog/DiagramTypeId.java
src/main/java/com/marcosmoreira/domainmodelstudio/domain/catalog/DiagramCapability.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellViewModel.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ActiveOutputResolver.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DefaultDiagramToolbarActionProvider.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs/ManualContent.java
docs/estado/I12_estado_despues_pruebas_documentacion_release.md
docs/implementacion/03_tanda_01_fuente_unica_tipos_capacidades.md
docs/implementacion/00_indice_implementacion.md
```

## 3. Diagnóstico ejecutivo

El producto ya tiene una base amplia de dominio, casos de uso, editores especializados, catálogo, persistencia, exportadores y ejemplos. El problema principal no es ausencia total de código, sino **desalineación entre lo que el catálogo/documentación declaran y lo que el shell realmente muestra o enruta**.

Riesgo principal:

```txt
Catálogo + documentación + toolbars: declaran producto multitipo.
Shell + menús + ayuda + estado inferior: conservan restos fuertes de app centrada en modelo conceptual.
```

La captura de `UML Casos de uso` confirma el síntoma: cambia la toolbar específica, pero el área central permanece en la pantalla de inicio y los paneles laterales dicen “Sin proyecto abierto”. Eso indica que el tipo activo no está arrastrando de forma completa su workspace, paneles, estado y exportaciones.

## 4. Definición de “tipo disponible”

Un tipo de diagrama solo puede figurar como disponible si cumple este flujo mínimo:

```txt
crear proyecto del tipo
→ abrir pestaña propia
→ mostrar salida visual o documental real
→ activar toolbar específica coherente
→ editar manualmente lo mínimo prometido
→ validar si el tipo lo promete
→ guardar .dms
→ reabrir .dms
→ exportar en los formatos declarados
→ tener ejemplo oficial
→ tener ayuda teórica/práctica suficiente
```

Si un tipo tiene infraestructura interna pero no cumple el flujo visible, no debe considerarse “cerrado”.

## 5. Contrato de barras y pestañas

La interfaz debe obedecer esta regla:

```txt
pestaña activa
→ proyecto activo
→ tipo de diagrama activo
→ workspace/editor activo
→ toolbar específica activa
→ panel izquierdo activo o no aplicable
→ panel derecho activo o no aplicable
→ comandos globales habilitados/deshabilitados
→ exportaciones disponibles
→ barra de estado contextual
```

### 5.1 Barra superior fija

La barra superior general puede mantenerse estable para acciones transversales:

```txt
Nuevo
Abrir
Guardar
Cerrar
Importar
Ejemplo
Recursos IA
Deshacer
Rehacer
Exportar
Ayuda
```

Pero cada acción debe resolver contra el proyecto de la pestaña activa, no contra un estado global ambiguo.

### 5.2 Toolbars específicas

Las toolbars inferiores deben depender del tipo de proyecto de la pestaña activa.

Ejemplos:

```txt
Modelo conceptual → Entidad, Atributo, Relación, Chen, Pata de gallo.
UML Casos de uso → Actor, Caso, Sistema, Asociación, Include, Extend, Generalización.
Wireframes → Pantalla, Panel, Campo, Botón, Tabla, Modal, Tarjeta, Filtro.
BPMN básico → Inicio, Actividad, Decisión, Fin, Carril, Flujo.
C4 → Persona, Sistema, Contenedor, Base de datos, Servicio externo, Relación.
```

## 6. Contrato de lenguaje visible

El frontend/desktop debe hablar en lenguaje de producto y levantamiento de información. En textos visibles al usuario se permiten términos como:

```txt
diagrama
modelo
vista
maqueta
pantalla
requerimiento
proceso
rol
permiso
flujo
ejemplo
plantilla
validación
exportación
entrega
```

Se deben evitar términos internos como lenguaje de usuario final:

```txt
renderer
viewmodel
handler
provider
backend interno
infraestructura interna
placeholder
parser
workspace técnico
```

Excepción: esos términos pueden existir en código, documentación técnica o dentro de un diagrama cuando el usuario modela una arquitectura real, por ejemplo “Backend API” dentro de C4.

## 7. Principios de corrección de programación

Las futuras tandas de implementación deben respetar estos criterios:

### 7.1 Responsabilidad única

No concentrar más lógica en `MainShellView` ni en `MainShellCommandHandler`. En la lectura preliminar, `MainShellCommandHandler.java` tiene aproximadamente 1650 líneas y mezcla sesiones, creación de proyectos, sincronización de editores, comandos de cada familia, validación, exportación y cierre de aplicación.

Esa clase debe dejar de crecer. Las futuras implementaciones deben extraer responsabilidades.

### 7.2 Trazas internas humana

Cada clase debe poder responder claramente:

```txt
Qué decisión toma.
Qué datos recibe.
Qué efecto produce.
Qué no debe hacer.
```

Evitar métodos que mezclen UI, decisión de tipo, persistencia, validación y exportación al mismo tiempo.

### 7.3 Fachadas útiles, no sobreingeniería

Se permite introducir fachadas si reducen ruido y centralizan reglas de producto. Candidatas conceptuales:

```txt
ActiveWorkspaceFacade
EditorSessionFacade
WorkspaceRouter
ExportCapabilityPresenter
DiagramActionDispatcher
StatusSummaryPresenter
ContextualPanelsPresenter
HelpTopicCatalog
```

Estas fachadas no deben ocultar todo en una mega-clase nueva. Deben separar responsabilidades pequeñas.

### 7.4 Métodos y clases razonables

Criterios orientativos para implementación futura:

```txt
Evitar clases de más de 500-700 líneas salvo vistas complejas justificadas.
Evitar métodos largos que mezclen varias fases de decisión.
Preferir métodos privados pequeños con nombres de dominio.
No duplicar listas de DiagramTypeId en varios lugares.
No hardcodear capacidades en menús si ya existe un catálogo de capacidades.
```

## 8. Brechas críticas detectadas en esta tanda

### 8.1 Familia de comportamiento sin workspace conectado al shell

Existe infraestructura para diagramas de comportamiento:

```txt
BehaviorDiagramViewModel
BehaviorDiagramEditorView
BehaviorDiagramDocument
BehaviorMarkdownExporter
casos de uso Add/Update/Remove/Validate
persistencia JSON .dms
acciones de toolbar
```

Pero `MainShellView` no instancia ni monta `BehaviorDiagramEditorView` en el área central. Esto afecta:

```txt
BPMN básico
Flujo operativo
UML Casos de uso
UML Actividad
UML Secuencia
UML Estados
```

Resultado visible: toolbar específica activa, pero lienzo incorrecto o pantalla de inicio.

### 8.2 Menú Exportar no deriva de capacidades reales

`DefaultDiagramCapabilityCatalog` declara `EXPORT_PNG` y `EXPORT_MARKDOWN` para múltiples tipos. Sin embargo, `MainShellView` mantiene listas manuales para habilitar/deshabilitar exportación PNG/Markdown. Esa duplicación permite que un tipo figure como exportable en catálogo, pero quede excluido del menú.

Regla futura:

```txt
El menú Exportar debe consultar capacidades del tipo activo o una fachada derivada del catálogo.
No debe mantener listas paralelas de tipos.
```

### 8.3 Shell multiproyecto incompleto

Ya existen pestañas de editor, pero la activación de una pestaña debe reconstruir completamente el contexto visible. No basta con cambiar el título o la toolbar.

Debe actualizar:

```txt
tipo activo
workspace central
paneles laterales
selección
estado inferior
exportaciones
validaciones
acciones de edición
notación disponible o no disponible
```

### 8.4 Pantalla de inicio y ayuda todavía débiles

La pantalla de inicio y el manual integrado no deben parecer una lluvia de ideas ni una app centrada únicamente en modelo conceptual. La ayuda debe evolucionar a micro-Wikipedia interna, pero eso se planificará en PF07.

## 9. Contrato preliminar por familia de tipos

| Familia | Tipos | Salida esperada | Observación preliminar |
|---|---|---|---|
| Modelo conceptual | `conceptual-model` | Lienzo visual con entidades, atributos, relaciones y notación | Base más madura; debe quedar como referencia, pero sin contaminar otros tipos. |
| Documento de datos | `data-dictionary` | Vista documental/tabular exportable | No debe forzarse a lienzo si su salida principal es documental. |
| Administración | `admin-module-map`, `roles-permissions-map`, `screen-flow`, `admin-wireframes` | Diagramas/maquetas visuales administrativas | Deben hablar de módulos, roles, pantallas y maquetas, no de infraestructura interna. |
| UML estructural | `uml-class` | Diagrama visual con clases y agrupadores | Debe priorizar agrupación por módulo/paquete/carpeta. |
| Comportamiento | `bpmn-basic`, `operational-flow`, `uml-use-case`, `uml-activity`, `uml-sequence`, `uml-state` | Diagrama visual de comportamiento | Infraestructura existe, pero falta conexión real en el shell. |
| Arquitectura | `c4-context`, `c4-containers`, `technical-deployment` | Diagrama visual de arquitectura/despliegue | Ya aparece conectado como familia; requiere revisar exportación y estado contextual. |

## 10. Wireframes como maquetas, no diseñador visual complejo

Los wireframes administrativos deben tratarse como scaffolding visual para levantamiento de requerimientos.

Elementos mínimos deseables:

```txt
Pantalla
Barra superior
Menú lateral
Panel
Tarjeta
Botón
Campo
Selector
Tabla
Filtro/buscador
Pestañas
Modal
Alerta
Bloque de reporte
Nota/anotación
Flecha de navegación
```

No se debe convertir esta función en un clon de Figma. El valor está en comunicar estructura funcional de pantallas administrativas.

## 11. Reglas de no implementación para esta fase

Durante las tandas PF no se debe:

```txt
crear nuevas clases Java funcionales;
conectar BehaviorDiagramEditorView;
modificar handlers;
cambiar exportadores;
cambiar gramáticas;
agregar ejemplos oficiales finales;
reescribir la ayuda;
tocar CSS de producto.
```

Sí se permite:

```txt
crear documentos de planificación;
crear matrices de brechas;
nombrar clases candidatas;
definir criterios de cierre;
definir pruebas futuras;
ordenar implementación posterior.
```

## 12. Salida obligatoria de las siguientes tandas

La siguiente planificación no debe saltar a implementación. Debe producir inventarios verificables.

Cada documento posterior debe incluir:

```txt
archivos a leer;
brecha funcional;
riesgo de arquitectura;
responsabilidad correcta;
clases candidatas a crear/dividir;
criterio de cierre;
pruebas futuras;
qué NO se debe tocar en esa tanda.
```

## 13. Criterio de cierre de PF00

PF00 se considera cerrada cuando queda aceptado este principio:

```txt
Domain Model Studio no se productiza agregando más botones.
Se productiza alineando pestaña activa, tipo activo, workspace, comandos, exportaciones, ayuda y pruebas bajo un contrato único de producto.
```

