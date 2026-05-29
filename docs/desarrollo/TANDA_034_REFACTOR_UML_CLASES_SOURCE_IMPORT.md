# Tanda 34 — Refactor focalizado de UML Clases: selección segura de Resumen

## Objetivo

Refactorizar una parte acotada del módulo UML Clases sin cambiar UX visible, gramática Markdown, persistencia `.dms` ni parsing de código fuente.

El módulo UML Clases es una de las zonas más sensibles del producto porque conecta importación de código fuente, vistas internas, layout visual, filtros, navegación a código y protección de rendimiento. Por eso esta tanda evita una abstracción grande y toca solo una responsabilidad concreta.

## Cambio aplicado

Se extrae la política que decide qué clases y relaciones entran en la vista **Resumen** cuando se importa un proyecto de código fuente grande.

Antes, `SourceCodeUmlClassViewBuilder` mezclaba dos responsabilidades:

1. construir objetos `UmlClassDiagramView`;
2. decidir ranking, límites visuales y prioridad semántica de la vista Resumen.

Ahora:

- `SourceCodeUmlClassViewBuilder` queda como constructor de vistas internas;
- `SourceCodeUmlSummarySelectionPolicy` decide la selección segura para Resumen;
- la política prioriza controladores, servicios, repositorios, entidades/modelos, DTOs, componentes frontend y relaciones API;
- el límite de Resumen conserva 120 clases y 180 relaciones como guardarraíl visual.

## Qué se preserva

- No cambia parsing Java.
- No cambia parsing TypeScript.
- No cambia la inferencia de relaciones.
- No cambia `SourceCodeToUmlClassDiagramMapper`.
- No cambia la navegación a código fuente.
- No cambia la protección PNG ni la recuperación ante fallos de render.
- No cambia UX visible ni textos de botones.
- No cambia `.dms`.

## Por qué este refactor sí conviene

La vista Resumen segura es una política de aplicación, no una tarea de presentación. Extraerla permite probarla directamente y evita que el constructor de vistas crezca con heurísticas de ranking.

El cambio reduce riesgo porque mantiene intactas las vistas generadas y deja el algoritmo de selección en una clase aislada.

## Tests agregados

- `SourceCodeUmlSummarySelectionPolicyTest`
- `UmlClassSourceImportRefactorSourceTest`

Los tests validan que:

- proyectos pequeños siguen completos en Resumen;
- proyectos grandes se limitan sin perder tipos importantes;
- relaciones API se preservan en la vista inicial segura;
- el builder delega la selección a la política;
- la política no depende de JavaFX ni de presentación.

## Fuera de alcance

Quedan fuera:

- refactor de `UmlClassDiagramViewModel` más allá de cambios previos de `ProjectChangeSupport`;
- refactor de `UmlClassCanvasAdapter`;
- refactor de `UmlClassStructurePanel`;
- parser Java/JDK AST;
- parser TypeScript heurístico;
- layout visual UML;
- exportación visual.

Estas zonas requieren diagnóstico propio si se decide continuar con UML Clases más adelante.
