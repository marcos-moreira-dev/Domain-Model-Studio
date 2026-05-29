# Métricas Tanda 17 — Workbench/SideDock/adapters

## Archivos reducidos

| Archivo | Antes | Después | Cambio |
|---|---:|---:|---:|
| `WorkbenchSideDock.java` | 194 | 121 | -73 |
| `DiagramWorkbenchView.java` | 137 | 115 | -22 |
| `StructuredWorkbenchView.java` | 122 | 91 | -31 |

## Clases nuevas

| Clase | Responsabilidad |
|---|---|
| `SideDockRailFactory` | Construcción de pestañas verticales del SideDock. |
| `SideDockModuleFrameFactory` | Marco, encabezado y scroll del módulo activo. |
| `SideDockSizingPolicy` | Anchura abierta/cerrada del SideDock. |
| `WorkbenchSideDockModules` | Registro de módulos reales por tipo de workbench. |
| `WorkbenchSideDockLayout` | Montaje SideDock + contenido central en `SplitPane`. |
| `WorkbenchSideDockVisibility` | Mostrar/ocultar módulos activos. |
| `StandardSideDockModules` | Fábrica de módulos transversales, empezando por Apariencia. |

## Deuda que sigue

- `DefaultDiagramToolbarActionProvider.java` sigue como deuda conocida de toolbar.
- `UmlClassDiagramViewModel.java` sigue como hotspot de UML Clases.
- `DmsProjectJsonReader.java` y `DmsProjectJsonWriter.java` siguen como deuda de persistencia.
- `MainShellCommandHandler.java` sigue como deuda de coordinación general.
