# Guía para agregar un nuevo tipo de artefacto, diagrama o herramienta

## Propósito

Esta guía evita que un nuevo tipo se agregue con parches dispersos. Todo tipo visible debe tener naturaleza clara, perfil de interacción, ejemplo oficial, salida real y ayuda/documentación mínima.

## Paso 1 — Definir naturaleza

Antes de escribir código, clasificar el tipo:

```txt
Diagrama visual
Documento
Matriz
Maqueta / wireframe
Referencia de ayuda
```

Preguntas obligatorias:

- ¿La salida principal es un dibujo, una tabla, una matriz o un documento?
- ¿El usuario debe mover nodos/conectores o editar contenido estructurado?
- ¿Qué exportaciones son razonables?
- ¿Existe teoría o notación que deba respetarse?


## Paso 1.1 — Ubicar el rol visual del artefacto

Antes de crear vistas o botones, definir qué zona de la aplicación porta cada responsabilidad:

```txt
Toolbar contextual = acciones operativas frecuentes.
SideDock / sidebar = navegación, estructura, inspección, propiedades, filtros y ayuda contextual.
Workspace = resultado principal del artefacto activo.
```

No duplicar acciones primarias entre toolbar, SideDock y workspace. Si el artefacto necesita inspector, preferir un módulo del SideDock antes que una columna fija que comprima el resultado principal.

## Paso 2 — Registrar tipo y capacidades

Actualizar el catálogo correspondiente, normalmente en la familia de definiciones de tipos/capacidades.

El tipo debe declarar de forma honesta:

- importación Markdown si aplica;
- salida visual/documental/matricial;
- exportaciones soportadas;
- familia de workspace;
- capacidades reales, no aspiracionales.

Regla:

> No declarar como disponible una exportación o herramienta que no produzca una salida real.

## Paso 3 — Definir perfil de interacción

Elegir o crear perfil:

```txt
GRAPH
SEQUENCE
WIREFRAME
MATRIX
DOCUMENT
READ_ONLY_REFERENCE
```

No forzar el tipo a `GRAPH` si es matriz, documento o secuencia temporal.

## Paso 4 — Modelo y contrato de datos

Definir el modelo mínimo:

- entidades principales;
- identificadores;
- layout si aplica;
- reglas de validación;
- persistencia en `.dms` si aplica.

Evitar mezclar modelo con JavaFX.

## Paso 5 — Importación Markdown

Si el tipo acepta Markdown:

- agregar plantilla oficial en `examples/markdown/plantillas/`;
- agregar ejemplo mínimo en `examples/markdown/diagramas/`;
- agregar ejemplo gordito si es un tipo importante;
- documentar gramática o contrato.

## Paso 6 — Adapter / RenderKit / ShapeKit

Para diagrama visual:

```txt
Adapter    → traduce modelo ↔ canvas
RenderKit  → arma nodos y conectores visuales
ShapeKit   → crea símbolos/primitivas si hacen falta
Profile    → permite o bloquea interacción
```

No poner interacción dentro del ShapeKit.

## Paso 7 — SideDock contextual

Registrar módulos compatibles con el contexto:

- estructura/propiedades/vista/ayuda para diagramas;
- secciones/vista previa para documentos;
- roles/permisos/matriz/filtros para matrices;
- componentes/alineación para wireframes.

El SideDock debe actualizarse al cambiar el tab activo.

## Paso 8 — Exportación

Definir ruta de exportación según naturaleza:

- diagramas: PNG/SVG/PDF visual si aplica;
- documentos: Markdown/PDF formal;
- matrices: Markdown/SVG/PNG tabular, PDF solo si está implementado;
- ayuda: no exportar salvo necesidad explícita.

Evitar usar snapshot de canvas para matrices/documentos.

## Paso 9 — Ayuda académica

Agregar tema en la ayuda si el tipo tiene teoría o uso propio:

- concepto;
- para qué sirve;
- elementos;
- reglas de notación;
- ejemplo;
- casos especiales;
- errores comunes;
- relación con otros tipos.

## Paso 10 — Pruebas y smoke

Agregar o actualizar:

- test de catálogo/capacidades;
- test de parser/exporter si aplica;
- ejemplo mínimo oficial;
- checklist smoke manual;
- validación de exportación.

## Checklist de aceptación

- [ ] Naturaleza clasificada.
- [ ] Perfil de interacción asignado.
- [ ] Catálogo actualizado con capacidades reales.
- [ ] Ejemplo oficial mínimo presente.
- [ ] Plantilla presente si aplica.
- [ ] UI no muestra jerga interna.
- [ ] SideDock muestra módulos correctos.
- [ ] Exportación real funciona o no se declara.
- [ ] Guardado/carga conserva información relevante.
- [ ] Ayuda/documentación actualizada.
