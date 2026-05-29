# PF04 — Alineación de menús, toolbars, comandos globales y capacidades reales

Estado: **ejecutada como planificación**  
Tipo: **solo planificación, sin implementación**  
Prioridad: **crítica**

## 1. Objetivo de esta tanda

Planificar cómo alinear todas las acciones visibles de la aplicación con capacidades reales del tipo/proyecto activo.

Esta tanda no implementa código. Define cómo deben comportarse:

```txt
barra superior fija;
menús Archivo / Editar / Vista / Exportar / Ayuda;
toolbars específicas inferiores;
acciones contextuales por tipo;
comandos globales;
validaciones;
exportaciones;
habilitado/deshabilitado de botones.
```

Regla central:

```txt
Nada visible debe prometer una función que el workspace activo no pueda cumplir.
```

## 2. Problema actual

Hay varias fuentes de verdad no sincronizadas:

```txt
DefaultDiagramCapabilityCatalog declara capacidades.
DefaultDiagramToolbarActionProvider muestra acciones por tipo.
MainToolbarViewModel.executeDiagramAction() enruta acciones con un switch grande.
MainShellCommandHandler implementa muchas acciones específicas.
MainShellView habilita/deshabilita menús con listas hardcodeadas.
ActiveOutputResolver resuelve salidas exportables reales.
ExportCommandHandler pregunta por ExportableOutput.
```

El caso crítico:

```txt
ActiveOutputResolver conoce BehaviorDiagramViewModel.
DefaultDiagramToolbarActionProvider muestra acciones para BPMN/UML comportamiento.
MainShellCommandHandler tiene acciones para comportamiento.
Pero MainShellView.pngExportUnavailable() y markdownExportUnavailable() no incluyen comportamiento.
Además BehaviorDiagramEditorView no está montado en el centro.
```

Conclusión:

```txt
La app tiene capacidades declaradas, acciones visibles y output real en piezas separadas. Falta una política común de disponibilidad.
```

## 3. Principio de diseño

La app debe diferenciar cuatro niveles:

```txt
1. Capacidad de producto declarada.
2. Workspace registrado para el tipo activo.
3. Acción visible para ese tipo.
4. Acción ejecutable en el estado actual.
```

Ejemplo:

```txt
Exportar PNG puede estar declarado para Wireframes.
Wireframes puede tener workspace registrado.
El botón PNG puede aparecer en toolbar y menú.
Pero si no hay documento activo, debe estar deshabilitado o mostrar mensaje claro.
```

## 4. Fuente de verdad recomendada

### 4.1 Catálogo de capacidades

`DefaultDiagramCapabilityCatalog` puede seguir declarando la promesa funcional por tipo:

```txt
CREATE_PROJECT
IMPORT_MARKDOWN
SHOW_VISUAL_OUTPUT
SHOW_DOCUMENT_OUTPUT
MANUAL_EDITING
EXPORT_PNG
EXPORT_SVG
EXPORT_PDF
EXPORT_MARKDOWN
SAVE_DMS
LOAD_DMS
AI_RESOURCES
THEORY_HELP
```

Pero esa promesa no debe bastar para habilitar botones. Debe cruzarse con el workspace activo.

### 4.2 Workspace activo

El workspace activo debe responder:

```txt
qué acciones soporta;
qué formatos puede exportar;
si hay proyecto/documento activo;
si la selección actual permite eliminar/duplicar/centrar;
si puede validar;
si puede reorganizar layout;
si tiene ayuda contextual.
```

### 4.3 Política de disponibilidad

Nombre sugerido:

```txt
WorkspaceActionAvailability
```

Responsabilidad:

```txt
recibir acción + workspace activo + estado actual;
devolver visible/habilitada/motivo.
```

Contrato conceptual:

```txt
ActionAvailability availabilityFor(DiagramToolbarActionId actionId)
ExportAvailability availabilityFor(ExportFormat format)
```

Donde `ActionAvailability` podría tener:

```txt
visible
enabled
reasonWhenDisabled
```

## 5. Barra superior fija

La barra superior general se mantiene siempre. Sus acciones son transversales:

```txt
Nuevo
Abrir
Guardar
Cerrar
Importar Markdown
Ejemplos
Recursos IA
Exportar
Ayuda
```

Pero su disponibilidad debe depender de la sesión activa.

| Acción global | Regla de disponibilidad |
|---|---|
| Nuevo | Siempre disponible. |
| Abrir | Siempre disponible. |
| Guardar | Solo si hay proyecto guardable activo. |
| Cerrar | Solo si hay pestaña/proyecto cerrable activo. |
| Importar Markdown | Disponible si el tipo elegido o flujo de importación tiene parser real. |
| Ejemplos | Disponible cuando exista selector oficial de ejemplos. Antes puede abrir carpeta o diálogo simple. |
| Recursos IA | Disponible si hay recursos exportables. |
| Ayuda | Siempre disponible; idealmente contextual según tipo activo. |

No debe depender solo de `projectOpen`. Debe usar una propiedad más precisa:

```txt
activeSessionSaveable
activeWorkspacePresent
activeWorkspaceSupports(action)
```

## 6. Menú Exportar

### 6.1 Problema actual

`MainShellView` tiene reglas como:

```txt
conceptualExportUnavailable()
pngExportUnavailable()
markdownExportUnavailable()
dataDictionaryExportUnavailable()
```

Estas reglas repiten listas de tipos. Eso ya produjo desfase.

### 6.2 Regla nueva

El menú Exportar debe preguntar al output activo:

```txt
¿la salida activa soporta SVG?
¿la salida activa soporta PNG?
¿la salida activa soporta Markdown?
¿la salida activa soporta PDF?
```

La fuente real puede ser:

```txt
ActiveOutputProvider / ActiveOutputResolver
```

o, si se aplica PF03:

```txt
ActiveWorkspaceFacade.currentExportSupport()
```

Pero no deben existir listas manuales duplicadas en `MainShellView`.

### 6.3 Matriz inicial de exportación esperada

| Tipo | SVG | PNG | PDF | Markdown |
|---|---:|---:|---:|---:|
| Modelo conceptual | Sí | Sí | No | Sí |
| Diccionario de datos | No | No | Sí | Sí |
| Mapa de módulos | No | Sí | No | Sí |
| Roles y permisos | No | Sí | No | Sí |
| Flujo de pantallas | No | Sí | No | Sí |
| Wireframes administrativos | No | Sí | No | Sí |
| UML Clases | No | Sí | No | Sí |
| BPMN básico | No | Sí | No | Sí |
| Flujo operativo | No | Sí | No | Sí |
| UML Casos de uso | No | Sí | No | Sí |
| UML Actividad | No | Sí | No | Sí |
| UML Secuencia | No | Sí | No | Sí |
| UML Estados | No | Sí | No | Sí |
| C4 Contexto | No | Sí | No | Sí |
| C4 Contenedores | No | Sí | No | Sí |
| Despliegue técnico | No | Sí | No | Sí |

Esta matriz debe validarse contra implementación real, no asumirse como cerrada si falta renderer/exporter.

## 7. Toolbars específicas inferiores

### 7.1 Regla visual

Las toolbars inferiores cambian según la pestaña activa, no según un estado global.

```txt
pestaña activa → tipo activo → acciones del tipo → disponibilidad por workspace activo
```

### 7.2 Problema actual

`DefaultDiagramToolbarActionProvider` ya devuelve acciones por tipo, incluso para comportamiento. Pero la toolbar no verifica por sí misma si el workspace está montado.

Entonces puede pasar:

```txt
toolbar de UML Casos de uso visible
workspace visual ausente
botones ejecutan ViewModel en memoria
pantalla central sigue en inicio
```

### 7.3 Regla nueva

El proveedor de acciones debe responder “qué acciones pertenecen al tipo”. La disponibilidad final debe resolverla otra capa:

```txt
DiagramToolbarActionProvider → define acciones por tipo.
WorkspaceActionAvailability → habilita/deshabilita según workspace activo y selección.
WorkspaceActionDispatcher → ejecuta si procede.
```

Así se conserva SRP.

## 8. Comandos globales de vista

Acciones actuales:

```txt
Acercar
Alejar
Tamaño real
Ajustar vista al diagrama
Centrar selección
Mostrar estructura
Mostrar propiedades
```

### 8.1 Regla por tipo

| Acción | Modelo conceptual | Especializados visuales | Diccionario | Home |
|---|---|---|---|---|
| Zoom | Sí | Sí, si el editor lo soporta | No o N/A | No |
| Ajustar vista | Sí | Sí, si hay lienzo | No o N/A | No |
| Centrar selección | Sí, si hay selección | Sí, si hay selección | No o N/A | No |
| Estructura | Sí genérica | No inicialmente o panel propio futuro | No | No |
| Propiedades | Sí genérica | No inicialmente o panel propio futuro | No | No |

No deben estar simplemente atadas a `projectClosed()`.

### 8.2 Recomendación de implementación posterior

Crear un soporte de vista por workspace:

```txt
WorkspaceViewActions
- canZoomIn
- zoomIn
- canZoomOut
- zoomOut
- canFitToContent
- fitToContent
- canCenterSelection
- centerSelection
```

Si un workspace no lo implementa, el menú queda deshabilitado con mensaje de estado claro.

## 9. Comandos de edición

Acciones:

```txt
Deshacer
Rehacer
Eliminar
Duplicar
Validar
Reorganizar
Cambiar notación
```

### 9.1 Deshacer/Rehacer

Actualmente debe revisarse si existe historial común o si solo aplica al canvas conceptual.

Regla de producto:

```txt
No mostrar deshacer/rehacer como promesa universal si no hay historial por workspace.
```

Opciones:

```txt
A. Deshabilitar para workspaces sin historial.
B. Implementar historial mínimo por documento.
C. Mantener visible pero con mensaje claro “no disponible para este tipo todavía”.
```

Para productización, se recomienda A hasta que exista historial real.

### 9.2 Eliminar

No debe ser solo `requestRemoveSelectedElement()` conceptual.

Debe depender de selección del workspace:

```txt
Conceptual → eliminar entidad/atributo/relación/punto intermedio si aplica.
UML Clases → eliminar módulo/clase/miembro/relación.
Wireframes → eliminar pantalla/componente.
Comportamiento → eliminar nodo/relación.
Arquitectura → eliminar nodo/relación.
```

Esto se conecta con la mejora posterior de eliminar puntos intermedios en líneas.

### 9.3 Reorganizar

`Regenerar layout` no debe llamar siempre a layout conceptual/Chen. Debe preguntar:

```txt
¿workspace activo soporta reorganizar?
```

Ejemplos:

```txt
Modelo conceptual → Chen/pata de gallo.
BPMN → flujo izquierda-derecha o arriba-abajo.
UML Secuencia → participantes arriba, mensajes verticales.
Wireframes → ordenar pantallas en grilla.
Diccionario → no aplica.
```

PF05 detallará layout por tipo.

### 9.4 Cambiar notación

Chen / pata de gallo solo aplica a modelo conceptual. No debe estar disponible para:

```txt
UML Clases
BPMN
Wireframes
C4
Diccionario
Roles y permisos
```

Si aparece en menú o toolbar fuera de conceptual, es ruido de producto.

## 10. Validación

Hay validaciones específicas:

```txt
validateDataDictionary()
validateModuleMap()
validateUmlClassDiagram()
validateRolesPermissions()
validateScreenFlow()
validateWireframe()
validateBehaviorDiagram()
validateArchitectureDiagram()
```

Pero el comando global `Validar` debe resolverse por workspace activo.

Diseño recomendado:

```txt
WorkspaceValidationSupport
- boolean supportsValidation()
- ValidationResult validate()
```

Regla:

```txt
Menú/toolbar Validar aparece o se habilita si el workspace activo soporta validación.
```

No debe haber una cadena manual interminable en `requestValidateProject()`.

## 11. Acciones de importación y ejemplos

Aunque PF05/PF08 detallarán el tema, PF04 deja reglas generales:

```txt
Importar Markdown debe consultar si existe parser para el tipo detectado o seleccionado.
Ejemplos debe abrir ejemplos que correspondan a tipos realmente disponibles.
No debe abrir ejemplo visual de un tipo cuyo workspace no esté registrado.
```

Para evitar falsos positivos:

```txt
Ejemplo oficial + tipo disponible + workspace registrado + parser/exporter real
```

## 12. Acciones de ayuda

Ayuda siempre disponible, pero debe poder usar tipo activo:

```txt
F1 sin proyecto → Inicio rápido.
F1 con UML Casos de uso activo → ficha de UML Casos de uso.
F1 con Wireframes activo → ficha de maquetas/wireframes administrativos.
```

PF07 detallará contenido. PF04 solo deja la regla de integración con acciones.

## 13. Propuesta de clases pequeñas

### 13.1 `GlobalActionAvailabilityProvider`

Responsabilidad:

```txt
calcular disponibilidad de acciones globales: guardar, cerrar, importar, exportar, validar, vista.
```

No ejecuta acciones.

### 13.2 `DiagramActionAvailabilityProvider`

Responsabilidad:

```txt
calcular disponibilidad de acciones de toolbar contextual.
```

No decide qué acciones pertenecen al tipo. Eso sigue siendo del provider de acciones.

### 13.3 `WorkspaceActionDispatcher`

Responsabilidad:

```txt
ejecutar acciones contra el workspace activo.
```

No decide cómo se dibuja la toolbar.

### 13.4 `ExportMenuStatePresenter`

Responsabilidad:

```txt
vincular elementos del menú Exportar con ExportSupport del workspace activo.
```

Reemplaza métodos como:

```txt
pngExportUnavailable()
markdownExportUnavailable()
```

### 13.5 `ToolbarActionBinder`

Responsabilidad:

```txt
crear botones y enlazarlos a disponibilidad/ejecución.
```

Esto evitaría que `DiagramToolbarView` tenga que saber demasiado de bindings futuros.

## 14. Matriz de acciones por familia

### 14.1 Modelo conceptual

```txt
Agregar entidad
Agregar atributo
Agregar relación
Duplicar
Eliminar
Validar
Reorganizar
Chen
Pata de gallo
Zoom
Ajustar vista
Centrar selección
Exportar SVG
Exportar PNG
Exportar Markdown
```

### 14.2 Diccionario de datos

```txt
Agregar entidad de diccionario
Agregar campo
Eliminar
Validar
Exportar PDF
Exportar Markdown
```

No requiere zoom ni notaciones.

### 14.3 Diagramas administrativos

Mapa de módulos:

```txt
Agregar módulo
Agregar submódulo
Agregar dependencia
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

Roles y permisos:

```txt
Agregar rol
Agregar permiso
Asignar permiso
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

Flujo de pantallas:

```txt
Agregar pantalla
Agregar transición
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

Wireframes:

```txt
Agregar pantalla
Agregar sección/panel
Agregar formulario
Agregar tabla
Agregar campo
Agregar botón
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

### 14.4 UML Clases

```txt
Agregar módulo/paquete
Agregar clase
Agregar interfaz
Agregar enum
Agregar atributo
Agregar método
Agregar relación
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

### 14.5 Comportamiento

BPMN / flujo operativo:

```txt
Inicio
Actividad
Decisión
Fin
Carril
Flujo
Nota
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

UML casos de uso:

```txt
Actor
Caso de uso
Límite del sistema
Asociación
Include
Extend
Generalización
Nota
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

UML actividad:

```txt
Inicio
Acción
Decisión
Fin
Flujo
Nota
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

UML secuencia:

```txt
Participante
Activación
Mensaje
Retorno
Nota
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

UML estados:

```txt
Estado inicial
Estado
Transición
Estado final
Nota
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

### 14.6 Arquitectura y despliegue

C4 Contexto:

```txt
Persona
Sistema
Sistema externo
Límite
Relación/uso
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

C4 Contenedores:

```txt
Contenedor
Aplicación
API
Base de datos
Servicio externo
Límite
Relaciones
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

Despliegue técnico:

```txt
Entorno
Servidor
Cliente
Servicio
Red
Artefacto
Conexión
Alojamiento
Despliegue
Eliminar
Validar
Exportar PNG
Exportar Markdown
```

## 15. Relación con capacidades declaradas

La implementación posterior debe agregar una prueba o auditoría que revise:

```txt
Toda acción EXPORT_PNG visible requiere DiagramCapability.EXPORT_PNG y ExportSupport PNG real.
Toda acción EXPORT_MARKDOWN visible requiere DiagramCapability.EXPORT_MARKDOWN y exportador real.
Toda acción VALIDATE_* visible requiere ValidationSupport real.
Todo tipo con MANUAL_EDITING debe tener acciones de edición mínimas o declarar claramente que la edición es limitada.
Todo tipo con SHOW_VISUAL_OUTPUT debe tener workspace visual montado.
```

## 16. Mensajes de estado

Cuando una acción no esté disponible, el mensaje debe ser de producto, no de código.

Correcto:

```txt
Este tipo de diagrama todavía no ofrece exportación SVG.
Selecciona una clase para agregar atributos.
Abre un proyecto para exportar.
```

Incorrecto:

```txt
ViewModel null.
No active output provider.
Renderer no registrado.
```

## 17. Orden recomendado de implementación derivada de PF04

1. Crear pruebas de disponibilidad de exportación por tipo.
2. Crear `ExportMenuStatePresenter` o equivalente.
3. Reemplazar listas hardcodeadas de exportación en `MainShellView` por consulta a output/workspace activo.
4. Crear `WorkspaceActionAvailability` mínimo.
5. Hacer que toolbar contextual deshabilite acciones no ejecutables según workspace activo.
6. Reducir progresivamente el switch grande de `MainToolbarViewModel.executeDiagramAction()` mediante dispatcher.
7. Alinear validación global con `WorkspaceValidationSupport`.
8. Alinear comandos de vista con `WorkspaceViewActions`.
9. Revisar textos de estado para que sean de dominio de producto.
10. Agregar prueba que falle si una acción visible no tiene handler real.

## 18. Riesgos

### 18.1 Romper acciones que ya funcionan

No se debe reescribir toda la toolbar en una sola tanda. Conviene introducir disponibilidad/dispatcher por capas.

### 18.2 Crear una mega-tabla inmantenible

La matriz de acciones por tipo debe estar organizada por familia, no como un método gigante.

### 18.3 Duplicar capacidad y disponibilidad

El catálogo declara promesa; el workspace declara ejecución real. Ambas cosas deben cruzarse, no sustituirse ciegamente.

### 18.4 Ocultar errores reales

Si un tipo disponible no tiene workspace, no se debe esconder deshabilitando todo. Debe existir ErrorWorkspace o prueba de arranque que lo detecte.

## 19. Criterios de cierre de PF04

La implementación posterior se considerará alineada cuando:

```txt
El menú Exportar se base en ExportSupport/ActiveOutput, no en listas hardcodeadas.
La toolbar contextual muestre acciones por tipo activo y las habilite según workspace/selección.
Validar, eliminar, reorganizar, zoom y centrar selección se resuelvan contra el workspace activo.
Chen/pata de gallo solo aparezcan para modelo conceptual.
Comportamiento tenga PNG/Markdown disponibles cuando su workspace esté activo.
Los mensajes visibles usen jerga de producto, no jerga de código.
Exista prueba de no regresión para acción visible sin handler real.
```

## 20. Relación con las siguientes tandas

PF05 usará esta alineación para definir importación, layout inicial y persistencia por tipo.

PF06 usará las mismas capacidades para estado inferior, estructura y propiedades contextuales.

PF07 conectará ayuda contextual al tipo activo y a las acciones visibles.
