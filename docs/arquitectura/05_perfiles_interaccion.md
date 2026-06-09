# Tanda 5 — Perfiles de interacción por tipo de herramienta

## Objetivo

Formalizar que Domain Model Studio comparte infraestructura, pero no comparte las mismas reglas de interacción para todos los tipos visibles.

La regla base queda así:

```txt
misma carcasa técnica cuando conviene,
perfiles de interacción distintos según la naturaleza de la herramienta.
```

Esto evita que una matriz, un documento, una maqueta o UML Secuencia reciban herramientas propias de un grafo libre.

## Perfiles estándar

| Perfil | Uso | Capacidades principales | Restricciones |
|---|---|---|---|
| `GRAPH` | Diagramas de nodos y conectores | mover nodos, seleccionar relaciones, puntos intermedios, etiquetas, endpoints y preview | no aplica a documentos ni matrices |
| `SEQUENCE` | UML Secuencia | participantes, mensajes, etiquetas controladas, orden temporal | no usa bendpoints ni endpoints libres de grafo |
| `WIREFRAME` | Maquetas administrativas | mover y redimensionar componentes | conectores no son la operación central |
| `MATRIX` | Roles y permisos | edición tabular/matricial | sin nodos, conectores ni canvas libre |
| `DOCUMENT` | Diccionario de datos | edición documental/tabular | sin herramientas de grafo |
| `READ_ONLY_REFERENCE` | Ayuda académica | lectura, navegación y búsqueda | sin edición del modelo |

## Resolución por tipo y workspace

`DiagramInteractionProfileResolver` ahora resuelve el perfil desde:

1. `DiagramTypeId`, cuando existe el tipo exacto.
2. `DiagramWorkspaceKind`, cuando el contexto viene desde una pestaña/workspace sin tipo exacto.
3. `DiagramTypeId + DiagramWorkspaceKind`, usando el tipo como prioridad.

La prioridad del tipo es importante porque `UML_SEQUENCE` pertenece al workspace de comportamiento, pero no debe comportarse como un grafo libre normal.

## Contexto de interacción

Se agregó `InteractionProfileContext` para que tabs, toolbars, sidebars y exportaciones puedan recibir una decisión compacta:

```txt
DiagramTypeId + DiagramWorkspaceKind + DiagramInteractionProfile
```

Esto prepara el SideDock modular: el sidebar deberá actualizarse al cambiar la pestaña activa y resolver módulos desde el contexto activo, no desde un estado global fijo.

## Matriz por tipo visible

| Tipo | Workspace | Perfil |
|---|---|---|
| Modelo conceptual | `CONCEPTUAL_CANVAS` | `GRAPH` |
| Diccionario de datos | `DATA_DICTIONARY_DOCUMENT` | `DOCUMENT` |
| BPMN básico | `BEHAVIOR_DIAGRAM` | `GRAPH` |
| Flujo operativo | `BEHAVIOR_DIAGRAM` | `GRAPH` |
| C4 Contexto | `ARCHITECTURE_DIAGRAM` | `GRAPH` |
| C4 Contenedores | `ARCHITECTURE_DIAGRAM` | `GRAPH` |
| Despliegue técnico | `ARCHITECTURE_DIAGRAM` | `GRAPH` |
| UML Casos de uso | `BEHAVIOR_DIAGRAM` | `GRAPH` |
| UML Clases | `UML_CLASS_DIAGRAM` | `GRAPH` |
| UML Actividad | `BEHAVIOR_DIAGRAM` | `GRAPH` |
| UML Secuencia | `BEHAVIOR_DIAGRAM` | `SEQUENCE` |
| UML Estados | `BEHAVIOR_DIAGRAM` | `GRAPH` |
| Mapa de módulos | `MODULE_MAP_DIAGRAM` | `GRAPH` |
| Roles y permisos | `ROLES_PERMISSIONS_MATRIX` | `MATRIX` |
| Flujo de pantallas | `SCREEN_FLOW_DIAGRAM` | `GRAPH` |
| Wireframes administrativos | `WIREFRAME_DIAGRAM` | `WIREFRAME` |

## Guardarraíl de toolbar

Se agregó `DiagramToolbarInteractionPolicy` para que la barra contextual no dependa únicamente de listas manuales por tipo.

La política permite:

- acciones documentales solo en perfiles documentales;
- acciones matriciales solo en perfiles matriciales;
- acciones de wireframe solo en perfiles redimensionables de maqueta;
- acciones de conectores solo cuando el perfil permite selección de conectores;
- eliminación de puntos intermedios solo cuando el perfil permite bendpoints.

Esto no reemplaza el catálogo de capacidades; lo complementa con reglas de interacción.

## Decisiones

- UML Secuencia conserva perfil temporal especializado.
- Roles/permisos queda protegido como matriz, no como canvas libre.
- Diccionario queda protegido como documento, no como diagrama.
- Wireframes queda protegido como maqueta, no como UML.
- El SideDock futuro debe consumir `InteractionProfileContext` desde el tab activo.

## Riesgos controlados

- No se migró visualmente ningún diagrama.
- No se cambió la estética del canvas.
- No se implementó todavía el SideDock.
- No se alteró la persistencia `.dms`.
- La política de toolbar es conservadora y actúa como filtro adicional.
