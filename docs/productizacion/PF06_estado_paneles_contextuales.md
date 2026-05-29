# PF06 — Barra de estado, estructura y propiedades contextuales por tipo

Estado: **ejecutada como planificación**  
Tipo: **solo planificación, sin implementación**  
Prioridad: **alta**

## 1. Objetivo de esta tanda

Planificar la alineación de tres zonas visibles que deben depender del proyecto activo:

```txt
barra de estado inferior;
panel izquierdo de estructura/navegación;
panel derecho de propiedades/edición fina.
```

La regla funcional es:

```txt
pestaña activa → tipo activo → resumen de estado → paneles contextuales → selección contextual.
```

Esta tanda no implementa código. Define cómo corregir estas zonas sin inflar clases grandes ni mezclar responsabilidades.

## 2. Problema actual detectado

`MainShellState.showProjectState(...)` ya contempla algunos tipos especializados:

```txt
DATA_DICTIONARY
ADMIN_MODULE_MAP
UML_CLASS
```

Pero para los demás cae a la lógica conceptual:

```txt
entityCount / relationshipCount
notación Chen / pata de gallo
```

Eso deja fuera o mal representados:

```txt
ROLES_PERMISSIONS_MAP
SCREEN_FLOW
ADMIN_WIREFRAMES
BPMN_BASIC
OPERATIONAL_FLOW
UML_USE_CASE
UML_ACTIVITY
UML_SEQUENCE
UML_STATE
C4_CONTEXT
C4_CONTAINERS
TECHNICAL_DEPLOYMENT
```

Además, el panel izquierdo actual `ModelTreeViewModel` está centrado en:

```txt
entidades;
atributos;
relaciones;
vistas Chen / pata de gallo.
```

Y el inspector genérico está centrado en selección conceptual:

```txt
entidad;
atributo;
relación;
conector;
layout conceptual;
estilo de nodos/conectores.
```

Eso sirve para modelo conceptual, pero no debe presentarse como panel universal de todos los tipos.

## 3. Regla de producto

La interfaz visible debe usar lenguaje del dominio de la herramienta:

```txt
diagramas;
maquetas;
pantallas;
procesos;
roles;
permisos;
clases;
módulos;
actores;
casos;
estados;
contenedores;
exportaciones;
requerimientos.
```

Debe evitar jerga interna como:

```txt
viewmodel;
renderer;
backend;
handler;
null;
infraestructura;
implementado internamente.
```

## 4. Diseño propuesto: resumen contextual del workspace

Nombre sugerido, no obligatorio:

```java
WorkspaceStatusSummary
```

Responsabilidad:

```txt
representar la información breve que la barra inferior debe mostrar para el workspace activo.
```

Contrato conceptual:

```java
String projectState();
String typeLabel();
String elementSummary();
String viewSummary();
String saveState();
String message();
```

Ejemplos:

```txt
Tipo: UML Casos de uso
Elementos: 3 actores / 8 casos / 12 relaciones
Vista: Diagrama de comportamiento
Estado: Proyecto abierto
Guardado: Cambios sin guardar
```

```txt
Tipo: Wireframes administrativos
Elementos: 5 pantallas / 43 componentes
Vista: Maquetas
Estado: Proyecto abierto
Guardado: Guardado
```

## 5. Diseño propuesto: proveedor de resumen por tipo

Nombre sugerido:

```java
WorkspaceStatusSummaryProvider
```

Responsabilidad:

```txt
calcular el resumen correcto para un DiagramProject según DiagramTypeId.
```

No debe vivir dentro de `MainShellState` como un `if` gigante. `MainShellState` solo debería aplicar el resumen recibido a `StatusBarViewModel`.

Diseño sugerido:

```txt
MainShellState
→ recibe WorkspaceStatusSummary
→ actualiza StatusBarViewModel
```

```txt
WorkspaceStatusSummaryProvider
→ pregunta al proyecto/documento especializado
→ devuelve textos visibles
```

## 6. Matriz de barra de estado por tipo

| Tipo | Tipo/Vista visible | Conteo principal | Conteo secundario |
|---|---|---|---|
| Modelo conceptual | Modelo conceptual | entidades | relaciones |
| Diccionario de datos | Diccionario de datos | entidades | campos |
| Mapa de módulos | Mapa de módulos | módulos | dependencias |
| Roles y permisos | Roles y permisos | roles | permisos/vínculos |
| Flujo de pantallas | Flujo de pantallas | pantallas | transiciones |
| Wireframes administrativos | Maquetas administrativas | pantallas | componentes |
| UML Clases | UML Clases | clases | relaciones/módulos |
| BPMN básico | BPMN básico | eventos/tareas | flujos/decisiones |
| Flujo operativo | Flujo operativo | pasos | responsables/transiciones |
| UML Casos de uso | UML Casos de uso | actores/casos | relaciones |
| UML Actividad | UML Actividad | acciones | decisiones/flujos |
| UML Secuencia | UML Secuencia | participantes | mensajes |
| UML Estados | UML Estados | estados | transiciones |
| C4 Contexto | C4 Contexto | personas/sistemas | relaciones |
| C4 Contenedores | C4 Contenedores | contenedores | relaciones |
| Despliegue técnico | Despliegue técnico | nodos/servicios | conexiones |

Si un tipo aún no tiene contador fino, debe mostrar un resumen honesto:

```txt
5 elementos / 4 conexiones
```

pero no debe volver a decir “entidades / relaciones” si no corresponde.

## 7. Panel izquierdo: de “estructura del modelo” a navegación contextual

### 7.1 Regla visual

El panel izquierdo no siempre debe llamarse “Estructura del modelo”. Debe cambiar según el tipo activo:

```txt
Modelo conceptual → Estructura del modelo
Diccionario → Índice del diccionario
Mapa de módulos → Módulos
Roles y permisos → Roles y permisos
Flujo de pantallas → Pantallas
Wireframes → Pantallas y componentes
UML Clases → Paquetes y clases
BPMN/UML comportamiento → Elementos del diagrama
C4/despliegue → Nodos y relaciones
```

### 7.2 Responsabilidad futura

El panel izquierdo debe servir para:

```txt
navegar;
seleccionar elementos;
mostrar agrupaciones;
cambiar vista solo cuando el tipo realmente tenga vistas;
colapsar/expandir estructura.
```

No debe ser el mecanismo obligatorio de edición. La edición principal debe seguir en el lienzo y en el panel derecho.

### 7.3 Nueva pieza sugerida

Nombre sugerido:

```java
WorkspaceStructureProvider
```

Responsabilidad:

```txt
crear un árbol o lista navegable según el proyecto activo.
```

Contrato conceptual:

```java
WorkspaceStructure structureFor(DiagramProject project)
```

Donde `WorkspaceStructure` puede contener:

```txt
título del panel;
nodos agrupados;
ID seleccionable;
tipo de nodo;
acción al seleccionar;
```

## 8. Panel derecho: propiedades contextuales

### 8.1 Problema actual

El inspector actual está fuertemente orientado al modelo conceptual:

```txt
selección de entidad;
atributos;
relaciones;
cardinalidades;
anclas de conectores;
notación activa;
colores del diagrama conceptual.
```

Eso no debe mostrarse igual para wireframes, BPMN, UML secuencia, C4 o roles.

### 8.2 Regla futura

El panel derecho debe cambiar de contenido según:

```txt
tipo de proyecto activo;
elemento seleccionado;
capacidad de edición del workspace;
```

Ejemplos:

```txt
Wireframe seleccionado: nombre de pantalla, tipo de pantalla, componentes, notas de requerimiento.
Botón de wireframe: texto visible, acción esperada, destino de navegación.
UML Caso de uso: nombre, actor principal, precondición, flujo principal, relaciones include/extend.
BPMN tarea: responsable, descripción, entrada, salida.
C4 contenedor: tecnología, responsabilidad, comunicación, datos.
Rol: responsabilidades, permisos, restricciones.
```

### 8.3 Nueva pieza sugerida

Nombre sugerido:

```java
WorkspaceInspectorProvider
```

Responsabilidad:

```txt
entregar el panel de propiedades correcto para el workspace activo.
```

Posible diseño:

```txt
ConceptualInspectorAdapter
WireframeInspectorAdapter
BehaviorInspectorAdapter
ArchitectureInspectorAdapter
DocumentInspectorAdapter
```

No se recomienda meter todos los formularios en `InspectorView`.

## 9. Política de paneles por familia de tipos

| Familia | Panel izquierdo | Panel derecho | Observación |
|---|---|---|---|
| Conceptual | Árbol de entidades/relaciones/vistas | Inspector conceptual | Mantener Chen/pata de gallo aquí. |
| Documental | Índice/secciones | Propiedades del documento/campo | Diccionario puede ser más tabular/documental. |
| Administrativo visual | Navegación por módulos/pantallas/roles | Propiedades del elemento | Incluye módulos, roles, flujos y wireframes. |
| UML Clases | Paquetes/módulos/clases | Clase/relación/miembro | Debe soportar agrupadores por paquete/directorio. |
| Comportamiento | Elementos del diagrama | Propiedades de actor/caso/tarea/estado/mensaje | Debe diferenciar subtipo: BPMN, actividad, secuencia, etc. |
| Arquitectura | Personas/sistemas/contenedores/nodos | Propiedades técnicas/de responsabilidad | C4 y despliegue. |
| Inicio/sin proyecto | Sin paneles o acceso rápido | Sin paneles | No mostrar “Sin proyecto abierto” como si fuera error visual. |

## 10. Política de selección contextual

La selección no debe ser universalmente “entidad/atributo/relación”. Debe tener un identificador común y un tipo visible:

```txt
SelectedWorkspaceItem
- id
- label
- itemKind
- diagramTypeId
- editable
```

Ejemplos de `itemKind`:

```txt
Entidad
Atributo
Relación
Pantalla
Componente
Actor
Caso de uso
Tarea
Decisión
Participante
Mensaje
Estado
Contenedor
Nodo
Permiso
Rol
```

La selección común puede existir, pero la interpretación debe ser contextual.

## 11. Mensajes visibles recomendados

Mensajes buenos:

```txt
UML Casos de uso abierto: 3 actores / 8 casos / 12 relaciones.
Wireframe abierto: 5 pantallas / 43 componentes.
Selecciona una pantalla o componente para editar sus propiedades.
La vista actual no usa panel de propiedades.
El panel de estructura muestra pantallas y componentes.
```

Mensajes a evitar:

```txt
Selecciona una entidad, atributo, relación o conector.
Notación Chen activa.
Proyecto vacío.
Workspace null.
ViewModel sin datos.
```

“Proyecto vacío” solo debe usarse si realmente el documento activo no tiene elementos, no como fallback conceptual.

## 12. Impacto en `StatusBarViewModel`

`StatusBarViewModel` puede conservar propiedades simples:

```txt
message
projectState
type/view label
zoom
elementCount
saveState
```

Pero el nombre `notation` puede quedar conceptualmente limitado. Opciones futuras:

```txt
renombrar visualmente a “Vista” sin cambiar de inmediato la propiedad interna;
o renombrar propiedad en código cuando se haga refactor.
```

Para una implementación prudente:

```txt
primero cambiar textos visibles a “Vista”;
luego, si no rompe demasiado, renombrar internamente en tanda posterior.
```

## 13. Impacto en `MainShellState`

`MainShellState.showProjectState(...)` no debe seguir creciendo con más `else if` por tipo.

Debe delegar:

```txt
WorkspaceStatusSummary summary = statusSummaryProvider.summaryFor(project);
apply(summary);
```

Responsabilidad de `MainShellState`:

```txt
estado global de ventana;
pestañas;
marcas de guardado;
aplicar resumen ya calculado;
no calcular conteos de cada dominio.
```

## 14. Impacto en `MainShellView`

`MainShellView` no debe decidir manualmente si hay paneles genéricos mediante listas sueltas.

Debe delegar:

```txt
WorkspacePanelPolicy panelPolicy = activeWorkspace.panelPolicy();
```

Con política posible:

```txt
leftPanel: hidden / generic / custom
rightPanel: hidden / generic / custom
leftTitle
rightTitle
restoreTooltip
```

Esto evita frases equivocadas como:

```txt
La guía de preparación no usa panel de estructura.
```

cuando el tipo activo no es guía sino un diagrama real.

## 15. Criterios de cierre de PF06 para la futura implementación

Una implementación derivada de esta planificación se considerará cerrada cuando:

```txt
La barra inferior muestre conteos correctos para todos los tipos visibles.
Los tipos de comportamiento ya no aparezcan como “entidades / relaciones”.
Wireframes muestre pantallas/componentes.
C4/despliegue muestre nodos/relaciones o contenedores/relaciones.
El panel izquierdo cambie título y estructura según el tipo activo.
El panel derecho no muestre formularios conceptuales para tipos no conceptuales.
Ocultar/restaurar paneles use textos contextuales.
La pestaña de inicio no muestre paneles laterales engañosos.
MainShellState no crezca con un `if` por cada tipo.
MainShellView no crezca con paneles especiales hardcodeados sin política común.
```

## 16. Orden recomendado de implementación posterior

```txt
1. Crear pruebas de resumen/status por tipo.
2. Crear WorkspaceStatusSummary y provider por familia.
3. Cambiar MainShellState para aplicar resumen, no calcularlo todo.
4. Crear WorkspacePanelPolicy.
5. Cambiar MainShellView para montar paneles según política.
6. Crear estructura contextual mínima por familia.
7. Crear inspector contextual mínimo por familia.
8. Ajustar textos visibles y tooltips de paneles.
9. Probar cambio de pestaña entre tipos diferentes.
```

## 17. Archivos que deberán revisarse en implementación

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellState.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/statusbar/StatusBarViewModel.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidebar/ModelTreeView.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidebar/ModelTreeViewModel.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorView.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorViewModel.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellViewModel.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ViewCommandHandler.java
```

## 18. Nota de arquitectura

PF06 no propone destruir los paneles existentes. Propone encapsularlos:

```txt
Conceptual usa el árbol e inspector actuales.
Otros tipos usan adaptadores o paneles propios mínimos.
El shell pregunta qué panel corresponde.
La barra inferior recibe un resumen ya preparado.
```

Así se evita el error típico:

```txt
agregar más ifs a MainShellView y MainShellState hasta que sean imposibles de mantener.
```
