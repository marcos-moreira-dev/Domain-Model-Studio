# JavaDoc: ejemplos pedagógicos de lectura

## Objetivo

Esta guía acompaña la tanda JD-6. Su propósito es usar JavaDoc como material de estudio: no solo saber qué clase existe, sino leer un ejemplo pequeño que conecte contrato, dominio y flujo de ejecución.

## Regla editorial

```txt
Un ejemplo JavaDoc pedagógico debe explicar una decisión o un contrato.
No debe repetir el nombre del método ni narrar una obviedad.
```

## Ejemplos incorporados en código

| Zona | Clases con ejemplo pedagógico | Qué se estudia |
|---|---|---|
| Dominio del Grafo lógico | `LogicalBusinessGraphValidationPolicy`, `LogicalBusinessGraphDocument`, `LogicalBusinessGraphRelationKind` | Backbone MF → FL → CU → ACC, inmutabilidad, gramática semántica. |
| Markdown oficial | `LogicalBusinessGraphMarkdownParser`, `LogicalBusinessGraphMarkdownExporter` | Conversión Markdown ↔ dominio y caso de grafo vacío reimportable. |
| Persistencia `.dms` | `DmsProjectJsonReader`, `DmsProjectJsonWriter` | Separación entre documento especializado y layout visual. |
| Canvas transversal | `InteractiveCanvasAdapter`, `CanvasBendPointController` | Adaptadores, comandos de layout y puntos intermedios sin deformar conectores. |
| SideDock | `WorkbenchSideDock` | Carcasa común con módulos específicos por workspace. |

## Ruta de lectura sugerida

1. Abrir `target\site\apidocs\index.html`.
2. Buscar `LogicalBusinessGraphDocument` y leer el ejemplo sobre nodos semánticos y layout externo.
3. Buscar `LogicalBusinessGraphRelationKind` y revisar por qué `ACC-001 garantiza POST-001` es válido, pero un reporte no garantiza una postcondición.
4. Buscar `LogicalBusinessGraphMarkdownParser` y observar cómo una fila tabular se convierte en dominio tipado.
5. Buscar `DmsProjectJsonWriter` y `DmsProjectJsonReader` para estudiar roundtrip.
6. Buscar `InteractiveCanvasAdapter` y `CanvasBendPointController` para entender por qué el canvas común no toca dominio directamente.
7. Buscar `WorkbenchSideDock` para ver cómo una misma carcasa cambia módulos entre proyectos.

## Preguntas de estudio

```txt
- ¿Qué diferencia hay entre semántica del grafo y layout visual?
- ¿Por qué el parser debe fallar ante inconsistencias en vez de arreglar silenciosamente?
- ¿Por qué un adaptador de canvas protege al dominio de JavaFX?
- ¿Cómo ayuda un ejemplo pequeño a entender una frontera arquitectónica?
- ¿Cuándo un comentario JavaDoc sí aporta valor pedagógico?
```

## Qué no se hizo

No se agregaron comentarios masivos a getters/setters ni a métodos triviales. Esta tanda prioriza ejemplos en contratos donde un estudiante puede aprender diseño de software.
