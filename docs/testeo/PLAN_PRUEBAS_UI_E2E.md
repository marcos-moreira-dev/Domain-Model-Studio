# Plan de pruebas UI/E2E para JavaFX

Estado: **plan técnico de aceptación**  
Tanda: **21**  
Herramienta sugerida: **TestFX** o robot JavaFX equivalente.

## Principio

No se deben simular todos los clics posibles. Se deben simular los flujos que representan promesas reales de producto:

```txt
abrir app → crear/importar → interactuar → guardar/exportar → verificar salida
```

Los detalles visuales finos siguen necesitando smoke humano, especialmente cuando el resultado es un PNG/SVG que debe verse claro.

## Qué sí conviene automatizar con UI/E2E

- creación de proyecto desde catálogo;
- selección de pestaña activa;
- toolbar contextual según tipo;
- selección de elemento en canvas;
- arrastre de elementos soportados;
- exportación desde acción visible;
- activación/desactivación de acciones contextuales;
- apertura de fuente UML con launcher falso;
- persistencia mínima guardar/reabrir con rutas temporales.

## Qué no conviene automatizar con clics reales

- elegir archivos en diálogos nativos de Windows;
- depender de rutas reales del usuario;
- abrir de verdad Visual Studio Code, Notepad u otro editor externo;
- comparar pixeles exactos de PNG salvo pruebas muy acotadas;
- probar todos los botones decorativos o variaciones de estilo.

## Servicios que deben aislarse para test

Para que los UI tests sean estables, estos puntos deben depender de puertos/adaptadores falsos en pruebas:

| Servicio | Motivo |
|---|---|
| Selector de archivo/carpeta | Los diálogos nativos son difíciles de controlar con robot. |
| Exportador con destino | El test debe escribir a carpeta temporal conocida. |
| Lanzador de editor externo | no se debe abrir una aplicación real durante test. |
| Confirmaciones/alertas | Deben poder verificarse sin depender del sistema operativo. |
| Catálogo de ejemplos | Debe poder usar ejemplos pequeños y deterministas. |

## Convención de nombres

| Tipo | Convención |
|---|---|
| UI smoke general | `ApplicationLaunchUiTest` |
| UI por caso de uso | `CreateProjectFromCatalogUiTest` |
| UI por diagrama | `FreeGraphSelectionDragUiTest` |
| UI por exportación | `ExportActiveDiagramUiTest` |
| UI por fuente UML | `UmlClassOpenSourceUiTest` |


## Smoke UI mínimo previo al UI/E2E automatizado

Antes de introducir TestFX o robot JavaFX, ejecutar y registrar el smoke manual formal:

```txt
docs\testeo\UI_SMOKE_MINIMO_EJECUTABLE.md
docs\testeo\reportes\REPORTE_SMOKE_UI_MINIMO.md
scripts\09-smoke-ui-minimo.bat
```

Este smoke valida shell, pestañas, toolbar contextual, SideDock contextual, canvas, guardado/reapertura, exportaciones, ayuda y recursos IA sin depender todavía de diálogos nativos automatizados.

## Criterio de cierre futuro

Un tipo visual puede considerarse aceptado cuando al menos tenga:

1. parser/importación cubiertos;
2. persistencia `.dms` cubierta;
3. exportación declarada cubierta;
4. una prueba o smoke de selección visual;
5. una prueba o smoke de exportación abierta externamente.

