# PF07 — Manifest de cambios

Estado: **ejecutado**  
Tipo: **solo documentación de planificación**

## Archivos agregados

```text
docs/productizacion/PF07_centro_ayuda_micro_wikipedia.md
docs/productizacion/PF07_matriz_contenidos_teoricos.md
docs/productizacion/PF07_recursos_graficos_didacticos.md
docs/productizacion/PF07_manifest_cambios.md
```

## Archivos actualizados

```text
docs/productizacion/00_indice_productizacion.md
```

## Qué se planificó

- Se redefinió el centro de ayuda como micro-Wikipedia teórica.
- Se dejó claro que su rol principal no es enseñar botones, sino teoría de diagramas.
- Se propuso migrar contenido desde Java hardcodeado hacia recursos `help/topics` y `help/figures`.
- Se definió una estructura de navegación por fundamentos, familias de diagramas, glosario y fuentes.
- Se creó una matriz de contenido teórico para los tipos visibles actuales.
- Se planificó una política de figuras didácticas, incluyendo símbolos simples como actor UML, entidad, gateway BPMN, clase, estado, contenedor C4 y wireframes administrativos.
- Se reforzó la separación de responsabilidades para evitar una clase gigante de ayuda.

## Qué NO se hizo

- No se modificó código Java.
- No se modificó CSS.
- No se agregaron imágenes reales.
- No se creó todavía `src/main/resources/help`.
- No se reemplazó `ManualContent.java`.
- No se implementó buscador ni navegación nueva.

## Cierre

PF07 queda como planificación fina de la futura ventana de ayuda. La implementación deberá convertir esta planificación en un centro real, navegable y con contenido teórico suficiente.
