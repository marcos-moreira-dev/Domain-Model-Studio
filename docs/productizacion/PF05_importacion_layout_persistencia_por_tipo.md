# PF05 — Importación Markdown, layout inicial y persistencia por tipo

Estado: **ejecutada como planificación**  
Tipo: **solo planificación, sin implementación**  
Prioridad: **crítica**

## 1. Objetivo de esta tanda

Planificar la alineación de tres rutas que deben funcionar de forma coherente para cada tipo visible de Domain Model Studio:

```txt
Importar Markdown → construir proyecto/documento correcto → asignar layout inicial correcto → abrir workspace correcto → guardar/abrir .dms sin perder información.
```

Esta tanda no implementa código. Define la ruta futura para evitar parches grandes y mantener:

```txt
principio de responsabilidad única;
trazabilidad humana;
clases y métodos moderados;
contratos pequeños por tipo/familia;
fachadas/adaptadores donde ayuden a leer el flujo.
```

## 2. Problema actual detectado

La importación ya reconoce varios tipos, y `DiagramProject` ya puede contener documentos especializados:

```txt
DataDictionaryDocument
ModuleMapDocument
UmlClassDiagramDocument
RolesPermissionsDocument
ScreenFlowDocument
WireframeDocument
BehaviorDiagramDocument
ArchitectureDiagramDocument
```

Pero la ruta de importación desde la interfaz todavía arrastra una decisión conceptual:

```java
ImportCommandHandler.importMarkdownFile(...)
→ importMarkdownModelUseCase.importFile(markdownFile)
→ generateInitialChenLayoutUseCase.generate(result.project())
→ openProjectInNewTab(...)
```

Ese `generateInitialChenLayoutUseCase` tiene sentido para modelo conceptual, pero no para todos los tipos.

Riesgo principal:

```txt
Un Markdown de BPMN, UML casos de uso, wireframes, C4 o diccionario puede entrar por una ruta que intenta tratarlo como modelo conceptual.
```

Aunque no siempre rompa, es una señal arquitectónica equivocada.

## 3. Regla funcional nueva

La importación no debe aplicar una política única. Debe elegir una estrategia por tipo:

```txt
Markdown importado
→ detectar DiagramTypeId
→ construir DiagramProject con documento correcto
→ aplicar política de layout inicial del tipo
→ abrir workspace registrado para ese tipo
→ actualizar estado/paneles/toolbars/exportaciones según la pestaña activa
```

La regla crítica:

```txt
Chen/pata de gallo solo pertenecen al modelo conceptual.
```

## 4. Responsabilidades que deben separarse

### 4.1 `ImportCommandHandler`

Responsabilidad futura:

```txt
abrir diálogo;
recibir archivo;
delegar importación;
abrir pestaña;
mostrar mensaje de resultado.
```

No debería decidir:

```txt
qué layout corresponde;
si se aplica Chen;
qué workspace se monta;
cómo se corrige cada tipo;
cómo se persiste internamente cada documento.
```

### 4.2 `ImportMarkdownModelUseCase`

Responsabilidad futura:

```txt
leer Markdown;
validar contrato de importación;
crear DiagramProject semánticamente correcto;
reportar errores/advertencias.
```

No debería hacer montaje visual de JavaFX.

### 4.3 Nueva pieza sugerida: `ImportedProjectNormalizer`

Nombre sugerido, no obligatorio.

Responsabilidad:

```txt
recibir el proyecto importado;
identificar tipo;
aplicar normalizaciones mínimas;
asegurar documento especializado presente;
asegurar estado inicial consistente;
retornar proyecto listo para abrir.
```

Contrato conceptual:

```java
DiagramProject normalize(ImportMarkdownModelResult result)
```

Debe ser de aplicación, no de presentación.

### 4.4 Nueva pieza sugerida: `InitialLayoutPolicy`

Responsabilidad:

```txt
decidir qué layout inicial se aplica según DiagramTypeId.
```

Contrato conceptual:

```java
DiagramProject applyInitialLayout(DiagramProject project)
```

Variantes posibles:

```txt
ConceptualInitialLayoutPolicy
BehaviorInitialLayoutPolicy
WireframeInitialLayoutPolicy
ArchitectureInitialLayoutPolicy
NoOpDocumentLayoutPolicy
```

No debe convertirse en una clase gigante con todo mezclado. Si hay un router, que solo enrute.

### 4.5 Nueva pieza sugerida: `ProjectPersistenceReadinessValidator`

Responsabilidad:

```txt
verificar antes de guardar/abrir que el proyecto tiene el documento esperado para su DiagramTypeId.
```

Ejemplos:

```txt
DATA_DICTIONARY requiere dataDictionary presente.
UML_CLASS requiere umlClassDiagram presente.
BPMN_BASIC requiere behaviorDiagram presente con kind correcto.
C4_CONTEXT requiere architectureDiagram presente con kind correcto.
CONCEPTUAL_MODEL requiere modelo conceptual y layouts conceptuales coherentes.
```

## 5. Matriz de política por tipo

| Tipo visible | Documento base esperado | Layout inicial esperado | Persistencia esperada |
|---|---|---|---|
| Modelo conceptual | `DiagramModel` + `DiagramLayouts` | Chen o pata de gallo según metadata | Guardar entidades, atributos, relaciones, layouts, estilos y vista activa. |
| Diccionario de datos | `DataDictionaryDocument` | No aplica lienzo; vista documental/tabular | Guardar entidades, campos, reglas, estado y notas. |
| Mapa de módulos | `ModuleMapDocument` | Layout de módulos/dependencias | Guardar módulos, dependencias, grupos y posiciones si existen. |
| Roles y permisos | `RolesPermissionsDocument` | Layout de roles/permisos o matriz visual | Guardar roles, permisos, responsabilidades, vínculos y estado. |
| Flujo de pantallas | `ScreenFlowDocument` | Layout de pantallas y transiciones | Guardar pantallas, navegación, acciones y posiciones. |
| Wireframes administrativos | `WireframeDocument` | Layout de pantallas/maquetas/componentes | Guardar pantallas, componentes, posiciones y agrupaciones. |
| UML Clases | `UmlClassDiagramDocument` | Layout por paquetes/módulos/clases | Guardar módulos, clases, atributos, métodos y relaciones. |
| BPMN básico | `BehaviorDiagramDocument` | Layout horizontal/vertical de proceso | Guardar eventos, tareas, decisiones, flujos y posiciones. |
| Flujo operativo | `BehaviorDiagramDocument` | Layout por etapas/actores operativos | Guardar pasos, responsables, transiciones y decisiones. |
| UML Casos de uso | `BehaviorDiagramDocument` | Actores fuera del límite, casos dentro del sistema | Guardar actores, casos, límite del sistema y relaciones. |
| UML Actividad | `BehaviorDiagramDocument` | Inicio → acciones → decisiones → fin | Guardar acciones, bifurcaciones, uniones y flujo. |
| UML Secuencia | `BehaviorDiagramDocument` | Participantes horizontales, mensajes verticales | Guardar lifelines, mensajes, activaciones y orden. |
| UML Estados | `BehaviorDiagramDocument` | Estados y transiciones por ciclo | Guardar estados, eventos, transiciones e inicial/final. |
| C4 Contexto | `ArchitectureDiagramDocument` | Personas/sistemas alrededor del sistema central | Guardar actores, sistemas, relaciones y límites. |
| C4 Contenedores | `ArchitectureDiagramDocument` | Contenedores internos y relaciones | Guardar apps, backend, BD, servicios y conexiones. |
| Despliegue técnico | `ArchitectureDiagramDocument` | Nodos, servicios y enlaces técnicos | Guardar nodos, artefactos, servicios, backups y conexiones. |

## 6. Política fina de importación Markdown

### 6.1 Detección de tipo

El tipo debe salir de una sola fuente clara:

```txt
frontmatter del Markdown o metadata equivalente → DiagramTypeId
```

No debe depender del nombre de archivo salvo como respaldo explícito para ejemplos antiguos.

### 6.2 Validación temprana

Antes de abrir pestaña:

```txt
si el tipo no existe → error claro;
si el tipo existe pero no tiene parser real → error honesto;
si el parser crea documento incompleto → advertencia o error según gravedad;
si el documento no corresponde al DiagramTypeId → bloquear apertura o normalizar explícitamente.
```

### 6.3 Normalización del título

Al importar:

```txt
si el Markdown trae título → usarlo;
si no trae título → derivarlo del nombre del archivo;
si tampoco hay nombre útil → usar “Proyecto importado”.
```

La pestaña no debería quedar con nombres genéricos engañosos como “Modelo conceptual” cuando el tipo real es otro.

### 6.4 Layout inicial

Debe aplicarse una política por tipo:

```txt
CONCEPTUAL_MODEL → GenerateInitialChenLayoutUseCase o equivalente conceptual.
DATA_DICTIONARY → no layout visual; preparar documento.
ADMIN_WIREFRAMES → organizar pantallas y componentes.
UML_USE_CASE → separar actores / límite / casos.
UML_SEQUENCE → ordenar participantes y mensajes.
C4_* → organizar nodos por nivel arquitectónico.
```

Si todavía no existe layout fino para un tipo, debe usarse una política simple pero honesta:

```txt
SimpleGridLayoutPolicy
SimpleFlowLayoutPolicy
NoOpLayoutPolicy
```

Pero no se debe aplicar Chen por accidente.

## 7. Política fina de persistencia `.dms`

### 7.1 Guardado

Guardar debe conservar:

```txt
metadata del proyecto;
DiagramTypeId real;
documento especializado real;
layout/posiciones propias del tipo;
estilo visual si aplica;
estado de vista;
Markdown fuente si se conserva como referencia;
marcas de cambios.
```

### 7.2 Apertura

Abrir `.dms` debe:

```txt
leer metadata;
resolver DiagramTypeId;
validar documento esperado;
registrar sesión/pestaña activa;
montar workspace correcto;
actualizar toolbars, paneles, statusbar y exportaciones.
```

### 7.3 Compatibilidad con archivos antiguos

Si un `.dms` antiguo no trae `DiagramTypeId`, política sugerida:

```txt
asumir CONCEPTUAL_MODEL solo si contiene DiagramModel conceptual reconocible;
mostrar advertencia discreta;
guardar luego con metadata actualizada.
```

Si trae datos mezclados:

```txt
no adivinar silenciosamente;
mostrar mensaje claro de archivo inconsistente.
```

## 8. Ejemplos oficiales e importación

El comando actual de ejemplo abre de forma fija:

```txt
examples/markdown/colegio_chen.md
```

Eso debe evolucionar después de PF08, pero PF05 deja la regla:

```txt
Ejemplo oficial → selector de tipo → archivo Markdown oficial → importación normal → workspace correcto.
```

No debe existir una ruta especial que “haga trampa” y abra ejemplos de una forma distinta al usuario real. Los ejemplos deben probar la importación real.

## 9. Mensajes visibles esperados

La importación debe hablar con jerga de producto, no de programación.

Mensajes recomendados:

```txt
Markdown importado: UML Casos de uso — 3 actores, 8 casos y 12 relaciones.
Markdown importado con 2 advertencias: revisa relaciones sin destino.
No se pudo importar: el archivo declara Wireframes administrativos, pero no contiene pantallas.
El ejemplo “Matrícula escolar” fue abierto como BPMN básico.
```

Mensajes a evitar:

```txt
ViewModel cargado.
Renderer no disponible.
Backend de importación falló.
Documento interno null.
```

## 10. Criterios de cierre de PF05 para la futura implementación

Una implementación derivada de esta planificación se considerará cerrada cuando:

```txt
ImportCommandHandler ya no aplique Chen a todos los tipos.
Cada DiagramTypeId tenga política de importación/layout documentada o NoOp honesto.
Abrir un Markdown de comportamiento monte BehaviorDiagramEditorView.
Abrir un Markdown de wireframes monte WireframeEditorView.
Abrir un Markdown C4/despliegue monte ArchitectureDiagramEditorView.
Guardar y abrir .dms conserve el tipo y documento especializado.
Los ejemplos oficiales usen la misma ruta de importación que el usuario final.
Los mensajes visibles hablen de diagramas, maquetas, vistas, requerimientos y exportaciones.
```

## 11. Orden recomendado de implementación posterior

```txt
1. Crear pruebas de regresión para impedir Chen global en importación.
2. Extraer política de layout inicial por tipo.
3. Cambiar ImportCommandHandler para delegar normalización/layout.
4. Validar presencia del documento especializado esperado.
5. Ajustar guardado/apertura .dms por DiagramTypeId.
6. Conectar apertura importada con WorkspaceRouter de PF02/PF03.
7. Actualizar mensajes visibles de importación.
8. Probar con un Markdown mínimo por cada tipo visible.
```

## 12. Archivos que deberán revisarse en implementación

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java
src/main/java/com/marcosmoreira/domainmodelstudio/application/importmodel/ImportMarkdownModelUseCase.java
src/main/java/com/marcosmoreira/domainmodelstudio/application/layout/GenerateInitialChenLayoutUseCase.java
src/main/java/com/marcosmoreira/domainmodelstudio/domain/diagram/DiagramProject.java
src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectFileRepository.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellViewModel.java
examples/markdown/plantillas/
examples/markdown/diagramas/
```

## 13. Nota de arquitectura

Esta tanda no busca crear una abstracción pesada. Busca evitar una ruta peligrosa:

```txt
“importar cualquier cosa y luego aplicarle Chen”.
```

La solución debe ser legible:

```txt
un router pequeño;
políticas por familia;
validadores simples;
mensajes claros;
pruebas que protejan el contrato.
```
