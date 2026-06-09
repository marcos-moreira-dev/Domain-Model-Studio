# Tanda 9 — Sidebar único modular / SideDock

## Decisión

El sidebar de los workspaces autocontenidos se consolida como un **SideDock modular**: un carril lateral de botones más un único módulo activo. La estructura evita tener simultáneamente panel izquierdo y panel derecho dentro del mismo diagrama, dejando más aire al canvas o al documento central.

La regla principal es:

> El SideDock debe seguir al tab/pestaña activa y no depender de un estado global fijo.

Cuando cambia el tab activo, el workspace seleccionado refresca su `SideDockContext`, resuelve los módulos compatibles y mantiene el módulo activo solo si todavía aplica. Si no aplica, cae al primer módulo lógico del nuevo contexto.


## Rol dentro del shell

El SideDock no reemplaza al toolbar contextual ni al workspace. Su rol vigente es:

```txt
SideDock = navegación, estructura, inspección, propiedades, filtros y ayuda contextual.
```

Las acciones operativas frecuentes pertenecen al toolbar contextual. El workspace porta el resultado principal del artefacto activo. En módulos documentales como Levantamiento lógico, el inspector contextual debe tratarse como módulo del SideDock y no como una tercera columna fija dentro del workspace, salvo excepción explícita.

## Componentes agregados

- `SideDockModuleId`: IDs estables para módulos como estructura, propiedades, vista, ayuda, secciones, roles, permisos y matriz.
- `SideDockContext`: contexto activo derivado del workspace/tab actual, su tipo y su perfil de interacción.
- `SideDockStatePolicy`: conserva el módulo activo si sigue disponible; si no, escoge un módulo inicial coherente.
- `SideDockModule`: contrato para módulos inyectables.
- `StaticSideDockModule`: módulo que reutiliza vistas existentes.
- `SideDockModuleRegistry`: registro local de módulos compatibles con un contexto.
- `WorkbenchSideDock`: carcasa JavaFX del carril lateral y del host de contenido.
- `SideDockPlaceholderFactory`: paneles ligeros para módulos auxiliares como Vista o Ayuda contextual.

## Integraciones iniciales

### DiagramWorkbenchView

Los workspaces visuales migrados usan el SideDock con módulos:

- Estructura
- Propiedades
- Vista
- Ayuda

El panel lateral anterior con estructura a la izquierda y propiedades a la derecha deja de ser la forma principal dentro de estos workspaces. Ahora solo se muestra un módulo a la vez.

### StructuredWorkbenchView

Los documentos y matrices usan módulos adaptados a su naturaleza:

- Diccionario/documento: Secciones, Propiedades, Vista previa, Ayuda.
- Roles/permisos: Roles, Permisos, Matriz, Ayuda.

Esto evita que las matrices o documentos reciban lenguaje visual de canvas libre.

## Relación con perfiles de interacción

El SideDock se apoya en `DiagramInteractionProfile` indirectamente mediante `SideDockContext`:

- `GRAPH`: estructura, propiedades, vista y ayuda.
- `DOCUMENT`: secciones, propiedades, vista previa y ayuda.
- `MATRIX`: roles, permisos, matriz y ayuda.
- `WIREFRAME`: puede especializarse después con componentes y alineación.
- `READ_ONLY_REFERENCE`: queda reservado para la ayuda CHM académica.

## Estado de alcance

Esta tanda implementa la carcasa modular y la migración inicial de workbenches especializados. No implementa todavía todos los módulos especializados definitivos; eso queda para tandas posteriores de diccionario, roles/permisos y ayuda.

## Criterios de aceptación

- Existe una carcasa `WorkbenchSideDock`.
- Existe carril lateral con botones de módulo.
- Solo un módulo se abre a la vez.
- Los workspaces visuales usan SideDock.
- Los workspaces estructurados usan módulos acordes a documento/matriz.
- El SideDock conserva el módulo activo si sigue aplicando al nuevo contexto.
- El SideDock cambia según el contexto del tab activo.
- No se toca el modelo conceptual canónico de forma agresiva.
