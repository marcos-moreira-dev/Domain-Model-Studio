# Tanda JD-6 — JavaDoc con ejemplos pedagógicos

## Objetivo

Agregar ejemplos JavaDoc en clases clave para que el proyecto sea más útil como material de estudio de ingeniería de software y desarrollo de software.

## Cambios realizados

Se agregaron ejemplos en:

```txt
src/main/java/.../domain/logicalbusinessgraph/LogicalBusinessGraphValidationPolicy.java
src/main/java/.../domain/logicalbusinessgraph/LogicalBusinessGraphDocument.java
src/main/java/.../domain/logicalbusinessgraph/LogicalBusinessGraphRelationKind.java
src/main/java/.../infrastructure/markdown/LogicalBusinessGraphMarkdownParser.java
src/main/java/.../infrastructure/markdown/LogicalBusinessGraphMarkdownExporter.java
src/main/java/.../infrastructure/json/DmsProjectJsonReader.java
src/main/java/.../infrastructure/json/DmsProjectJsonWriter.java
src/main/java/.../presentation/interactivecanvas/InteractiveCanvasAdapter.java
src/main/java/.../presentation/interactivecanvas/CanvasBendPointController.java
src/main/java/.../presentation/sidedock/WorkbenchSideDock.java
```

También se agregó la guía:

```txt
docs/desarrollo/JAVADOC_EJEMPLOS_PEDAGOGICOS.md
```

## Contratos explicados

```txt
- Backbone del Grafo lógico.
- Inmutabilidad del documento especializado.
- Validación de relaciones semánticas.
- Markdown oficial como entrada importable.
- Exportación Markdown reimportable incluso en grafo vacío.
- Roundtrip .dms con semántica y layout separados.
- Canvas transversal mediante adaptadores.
- Inserción de bendpoints sin deformar conectores.
- SideDock como carcasa común con módulos específicos.
```

## Validación

Script focalizado:

```bat
scripts\02-ejecutar-tests.bat
```

Prueba directa:

```bat
mvn -Dtest=Jd6PedagogicalJavadocExamplesSourceTest test
```

Generación recomendada del sitio:

```bat
scripts\31-generar-javadoc.bat
```

## Resultado

La tanda queda cerrada como documentación pedagógica aplicada. No se modificó lógica funcional.


## Nota de lectura pedagógica

Ejemplo pedagógico conservado como guía de estudio. No se modificó lógica funcional.
