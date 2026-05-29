# Tanda 1 — Auditoría del cuello de botella y puntos de congelamiento

## Objetivo

Ubicar con precisión en qué fase se congela la importación UML desde código fuente. Esta tanda no intenta todavía resolver todos los problemas de render pesado. Su objetivo es que el programa informe fases y tiempos para diferenciar si el bloqueo ocurre en escaneo, parseo, normalización, mapeo UML, apertura de pestaña, layout o render JavaFX.

## Implementación aplicada

Se agregaron clases livianas de auditoría en `application/sourcecode`:

- `SourceCodeImportAuditEvent`
- `SourceCodeImportAuditTrail`
- `SourceCodeImportPhaseReporter`

El caso de uso `GenerateUmlClassDiagramFromSourceCodeUseCase` ahora reporta fases explícitas:

1. `Escaneo y parseo`
2. `Mapeo UML`
3. `Documento UML listo`

Los mensajes incluyen tiempo transcurrido y memoria usada por la JVM.

El `ImportCommandHandler` ahora informa explícitamente cuando pasa a:

```text
Abriendo editor UML Clases y preparando lienzo visual...
```

Esto ayuda a comprobar si el congelamiento ocurre después de generar el documento, justo al abrir/renderizar el editor.

## Qué observar manualmente

Durante una importación real, revisar el último mensaje visible antes del congelamiento:

- Si se queda en `Escaneando`, el problema está en el scanner.
- Si se queda en `Parseando Java`, el problema está en parser/AST.
- Si se queda en `Normalizando`, el problema está en relaciones/modelo neutral.
- Si se queda en `Mapeo UML`, el problema está en el mapper.
- Si llega a `Documento UML listo` y luego se congela, el problema está en apertura, layout o render JavaFX.
- Si llega a `Abriendo editor UML...` y se congela, el cuello está casi confirmado en el editor visual/lienzo.

## Archivos tocados

- `src/main/java/com/marcosmoreira/domainmodelstudio/application/sourcecode/SourceCodeImportAuditEvent.java`
- `src/main/java/com/marcosmoreira/domainmodelstudio/application/sourcecode/SourceCodeImportAuditTrail.java`
- `src/main/java/com/marcosmoreira/domainmodelstudio/application/sourcecode/SourceCodeImportPhaseReporter.java`
- `src/main/java/com/marcosmoreira/domainmodelstudio/application/umlclass/GenerateUmlClassDiagramFromSourceCodeUseCase.java`
- `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java`

## Resultado esperado

La próxima prueba debe mostrar si el bloqueo ocurre antes o después de `Documento UML listo`. Si el bloqueo ocurre al abrir el editor, la siguiente tanda debe enfocarse en modo light y render seguro.
