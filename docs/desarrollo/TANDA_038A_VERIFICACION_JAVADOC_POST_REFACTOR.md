# Tanda 38A — Verificación JavaDoc post-refactor

## Objetivo

Revisar la documentación técnica del código después del refactor integral de las Tandas 28–37 sin abrir una nueva línea de refactor funcional.

La tanda es deliberadamente corta: JavaDoc debe explicar contratos, límites y puntos de lectura vigentes. No se modifica UX, persistencia, Markdown, recursos IA, scripts ni lógica de aplicación.

## Alcance aplicado

Se revisaron y ajustaron JavaDocs/package-info en las zonas tocadas por el refactor:

- `application.ApplicationServices` y `application.services`.
- `bootstrap.ApplicationServicesFactory`.
- `application.catalog.definitions`.
- `infrastructure.resources.definitions`.
- `infrastructure.json`.
- `infrastructure.markdown` y `MarkdownImportDocument`.
- `presentation.workbench.ProjectChangeSupport`.
- `application.umlclass.SourceCodeUmlSummarySelectionPolicy`.
- `presentation.canvas` para el canvas conceptual legacy.
- `domain.logicalbusinessgraph`.
- `application.logicalbusiness.derivation` como infraestructura interna de borradores compatibles.

## Correcciones semánticas

Se eliminaron de JavaDoc y textos productivos cercanos expresiones que ya no son contrato vigente:

- `fuente madre`.
- `vistas derivadas`.
- `artefactos derivados` como promesa general.

Se reemplazaron por lenguaje vigente:

- `fuente lógica canónica`.
- `vista visual semántica compatible`.
- `artefactos compatibles revisables`.
- `borradores compatibles internos`.

## Verificación de generación

El entry point vigente sigue siendo:

```bat
scripts\31-generar-javadoc.bat
```

El script genera:

```text
target\site\apidocs\index.html
```

En este entorno no se ejecutó Maven. La validación local completa debe hacerse en Windows con:

```bat
scripts\31-generar-javadoc.bat
scripts\02-ejecutar-tests.bat
```

## Guardarraíles

Se agregó `JavadocPostRefactorSourceTest` para proteger que:

- las fachadas por familia queden documentadas;
- `ApplicationServices` siga siendo fachada de compatibilidad;
- `ApplicationServicesFactory` quede documentada como ensamblador;
- catálogos y recursos oficiales expliquen la modularización;
- persistencia `.dms` declare que no cambia formato;
- Markdown declare que no cambia gramáticas, ejemplos oficiales ni comportamiento visible;
- `ProjectChangeSupport` no se interprete como superclase visual;
- UML Clases documente los límites 120/180 de la vista Resumen;
- el canvas conceptual legacy quede documentado como zona sensible;
- el código main no vuelva a usar `fuente madre`, `vistas derivadas` ni `artefactos derivados` como lenguaje rector.

## Resultado

JavaDoc queda suficientemente alineado para pasar al smoke integral post-refactor. No se justifica una tanda grande de embellecimiento de comentarios.
