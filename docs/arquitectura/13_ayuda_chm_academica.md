# Tanda 13 — Ayuda CHM académica

## Objetivo

El módulo de ayuda queda tratado como una referencia documental académica, no como lluvia de ideas ni como descripción interna de programación. Su función es explicar la teoría, notación, ejemplos, casos especiales y errores comunes de las herramientas visibles de Domain Model Studio.

## Decisión de producto

- La referencia académica se abre desde el menú **Ayuda → Guía académica**.
- La experiencia visual se inspira en la ayuda clásica tipo CHM/Windows Help.
- El panel izquierdo usa pestañas de **Contenido**, **Buscar** e **Índice**.
- El árbol de contenidos usa iconos vectoriales de libro morado y página.
- El contenido visible habla de diagramas, documentos, matrices, maquetas, notación y ejemplos; no habla de adapters, renderers, JavaFX ni infraestructura interna.

## Estructura funcional

```txt
Guía académica
├── Contenido
│   └── Árbol jerárquico por familias y tipos
├── Buscar
│   └── Búsqueda por título, categoría, resumen y cuerpo textual
└── Índice
    └── Lista alfabética de temas
```

## Componentes agregados o reforzados

- `ManualDialog`: ventana principal de ayuda académica.
- `ManualSearchIndex`: índice local de búsqueda.
- `ManualSearchResult`: resultado navegable de ayuda.
- `ManualIconFactory`: iconos vectoriales simples de libro morado/página.
- `ManualContent`: mantiene la carga de categorías y fichas desde recursos teóricos.

## Contenido

La ayuda reutiliza el catálogo teórico ya versionado en:

```txt
src/main/resources/help/topics/*.md
```

Cada ficha debe sostener prosa conceptual clara sobre:

- qué es la herramienta;
- para qué sirve;
- elementos principales;
- reglas de lectura;
- casos especiales;
- cuándo usarla;
- cuándo no usarla;
- errores comunes.

Los diagramas y figuras didácticas se renderizan desde primitivas simples mediante `ManualFigureNodeFactory`, evitando imágenes externas sin control de licencia.

## Frontera con la ayuda operativa

`ManualDialog.showForDiagramType(DiagramTypeId)` queda disponible para abrir directamente el tema académico asociado a un tipo de diagrama. La ayuda operativa del SideDock se mantiene separada y explica cómo usar la herramienta activa.

## Reglas de estilo

- Iconos de libro morado para temas/categorías.
- Panel izquierdo compacto y nostálgico.
- Texto principal negro y legible.
- Figuras didácticas sobrias.
- Sin jerga de implementación en UI.

## Criterios de aceptación

- El menú abre la referencia como **Guía académica**.
- Hay árbol de contenidos.
- Hay búsqueda local.
- Hay índice alfabético.
- Hay iconos tipo libro morado/página.
- Los temas cargan contenido académico desde recursos.
- La ayuda contextual por tipo queda preparada.
- No se introduce jerga interna visible.
