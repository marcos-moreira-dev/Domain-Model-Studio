# Tanda 38A — Revisión JavaDoc post-refactor

Estado: aplicada.

## Objetivo

Revisar JavaDoc únicamente donde el refactor de Tandas 28–37 cambió fronteras arquitectónicas reales. Esta tanda no embellece comentarios ni documenta getters triviales.

## Alcance

Se actualizan package-info y JavaDoc de zonas tocadas por el refactor:

- fachadas de aplicación por familia;
- catálogos oficiales por familia;
- recursos IA por familia;
- persistencia `.dms` separada en coordinadores y payloads especializados;
- importación Markdown con `MarkdownImportDocument`;
- `ProjectChangeSupport` en workbench;
- UML Clases con `SourceCodeUmlSummarySelectionPolicy`;
- canvas conceptual legacy con historial y resolución de anclas;
- shell con coordinadores de creación/apertura;
- borradores compatibles internos del Levantamiento lógico.

## Criterio

JavaDoc debe explicar contrato, intención y frontera. No debe repetir nombres de métodos, prometer comportamiento no implementado ni conservar lenguaje histórico como contrato vigente.

## Qué se corrigió

- Se agregó `application.services/package-info.java` para explicar las fachadas por familia.
- Se reforzaron package-info de catálogo, recursos, Markdown, JSON, workbench, shell, canvas y UML Clases.
- Se documentó que `MarkdownImportDocument` no cambia gramáticas, ejemplos oficiales ni comportamiento visible.
- Se documentó que la persistencia `.dms` no cambia claves JSON ni introduce migraciones.
- Se aclaró que `ProjectChangeSupport` no es superclase ni decide dirty state, layout o comandos.
- Se actualizó la guía del sitio JavaDoc para usar solo scripts públicos vigentes.

## Validación esperada

```bat
scripts\31-generar-javadoc.bat
scripts\02-ejecutar-tests.bat
```

## No modifica lógica funcional

No cambia UX visible, runtime, `.dms`, gramáticas Markdown, recursos IA, ejemplos oficiales, renderers, canvas ni comandos. Es una tanda documental de código.

## Próximo paso

La siguiente validación debe ser el smoke integral post-refactor.
