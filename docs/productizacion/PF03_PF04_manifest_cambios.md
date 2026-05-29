# PF03/PF04 — Manifest de cambios de planificación

Estado: **ejecutado**  
Tipo: **documentación de planificación**  
Alcance: **sin implementación de código**

## Archivos agregados

```txt
docs/productizacion/PF03_contrato_workspace_activo.md
docs/productizacion/PF04_menus_toolbars_capacidades.md
docs/productizacion/PF03_PF04_manifest_cambios.md
```

## Archivos actualizados

```txt
docs/productizacion/00_indice_productizacion.md
```

## Qué se planificó

### PF03

Se definió el contrato de workspace/editor activo para que el shell deje de depender de listas manuales de tipos. La planificación propone piezas pequeñas y trazables:

```txt
ActiveWorkspace
WorkspaceRegistry
WorkspaceRouter
ActiveWorkspaceFacade
WorkspaceMountPresenter
WorkspaceActionDispatcher
WorkspacePanelPolicy
WorkspaceStatusSummary
WorkspaceExportSupport
WorkspaceActionSupport
```

La intención es corregir la causa del bug de comportamiento sin inflar `MainShellView` ni convertir `MainShellCommandHandler` en una clase todavía más grande.

### PF04

Se planificó la alineación entre:

```txt
menús;
toolbars;
comandos globales;
acciones contextuales;
validaciones;
exportaciones;
capacidades declaradas;
workspace activo.
```

La regla central queda escrita:

```txt
Nada visible debe prometer una función que el workspace activo no pueda cumplir.
```

## Qué no se hizo

No se modificó:

```txt
código Java;
CSS;
recursos Markdown;
ejemplos;
scripts;
pruebas;
pom.xml.
```

## Riesgo reducido

Estas dos tandas reducen el riesgo de hacer una implementación tipo parche, especialmente:

```txt
agregar behaviorRoot directamente a MainShellView sin resolver el ruteo común;
seguir agregando if/else por cada tipo;
habilitar menús por listas manuales;
mostrar toolbars que no correspondan al workspace activo;
seguir con comandos globales apuntando al canvas conceptual.
```

## Próxima tanda recomendada

La siguiente tanda de planificación debe ser:

```txt
PF05 — Importación Markdown, layout inicial y persistencia por tipo.
```

Ahí se debe planificar cómo evitar que la importación aplique layout Chen a todo y cómo persistir/cargar cada documento especializado sin mezclar responsabilidades.
