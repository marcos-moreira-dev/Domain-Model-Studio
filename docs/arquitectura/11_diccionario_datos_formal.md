# Tanda 11 — Diccionario de datos formal

## Decisión de producto

El diccionario de datos se trata como documento técnico-administrativo, no como canvas. Su salida natural es PDF/Markdown formal y una vista previa documental dentro de la aplicación.

## Estructura documental

El documento queda organizado con estas secciones:

1. Portada con proyecto, cliente, organización, autor, versión, fecha, estado y referencia de logo opcional.
2. Introducción, usando el texto del usuario o una introducción base cuando esté vacío.
3. Resumen ejecutivo con conteos de entidades, campos, estado y entidades con clave primaria.
4. Tabla general de entidades.
5. Detalle por entidad con propiedades y tabla de campos.
6. Detalle ampliado por campo cuando existan reglas, validaciones, ejemplos, visibilidad u observaciones.
7. Observaciones generales.

## PDF

El PDF usa `SimplePdfDocument` como escritor mínimo sin dependencias externas. En esta tanda se reforzó:

- texto negro forzado antes de cada bloque textual;
- restauración de color después de fondos grises de tablas/callouts;
- bordes de tabla más controlados;
- filas alternadas suaves;
- padding y alto de fila más legibles;
- tabla general de entidades;
- detalle de campos menos apretado.

La referencia de logo queda como dato formal de portada y puede seleccionarse desde el formulario. La incrustación binaria de PNG/JPG en el PDF no se fuerza en esta tanda para no convertir el escritor PDF mínimo en motor gráfico pesado.

## Markdown

El Markdown se alinea con la estructura documental del PDF:

- portada/metadata;
- introducción;
- resumen ejecutivo;
- tabla general de entidades;
- detalle por entidad;
- tabla de campos;
- detalles ampliados por campo.

## Vista previa

La vista previa ahora se acerca más al documento final:

- resumen ejecutivo;
- tabla general de entidades;
- tarjetas de entidad con texto negro y alto contraste;
- referencia de logo opcional;
- detalle de campos con visibilidad y edición.

## Regla de lenguaje

La vista visible y los documentos exportados deben hablar de entidades, campos, reglas, documentos, tablas y observaciones. No deben explicar infraestructura interna de implementación.
