# Roles de toolbar, SideDock y workspace

Estado: **criterio arquitectónico vigente**  
Alcance: shell JavaFX, workbenches visuales, documentos estructurados, matrices y artefactos derivados.

## Decisión

Domain Model Studio no debe pensarse únicamente como editor de diagramas. El producto trabaja con **artefactos de modelado**: diagramas, documentos estructurados, matrices, wireframes, expedientes lógicos, diccionarios y artefactos compatibles revisables.

Por eso las zonas principales de la ventana tienen roles estables:

```txt
Toolbar contextual = acciones operativas frecuentes.
SideDock / sidebar = navegación, estructura, inspección, propiedades, filtros y ayuda contextual.
Workspace = resultado principal del artefacto activo.
```

## Toolbar contextual

La toolbar contextual concentra acciones operativas del artefacto activo. Debe responder al tipo de proyecto abierto y a sus capacidades reales.

Debe contener, según aplique:

```txt
crear elementos;
insertar plantillas;
eliminar elementos o puntos;
validar;
reorganizar;
cambiar vista o notación;
centrar / ajustar;
derivar;
importar o exportar acciones frecuentes.
```

Debe evitar:

```txt
texto largo;
explicaciones académicas;
propiedades detalladas;
listas extensas de estructura;
formularios pesados;
acciones duplicadas con el SideDock.
```

Regla anti-redundancia:

> Una acción primaria debe tener un lugar principal. El SideDock puede mostrar resultados, detalles o propiedades de esa acción, pero no debe repetirla como botonera equivalente.

## SideDock / sidebar

El SideDock orienta e inspecciona. Reacciona al tab activo y muestra módulos compatibles con el artefacto.

Puede contener:

```txt
Estructura / árbol;
Propiedades;
Validación;
Trazas internas;
Artefactos compatibles;
Apariencia;
Ayuda contextual;
Filtros y navegación.
```

Debe evitar convertirse en la pantalla principal del artefacto o duplicar todas las acciones del toolbar. Los botones dentro del SideDock son aceptables cuando pertenecen a edición fina de propiedades, navegación contextual o apertura de detalle.

## Workspace

El workspace porta el resultado principal del artefacto activo.

Ejemplos:

```txt
Modelo conceptual       → canvas Chen / pata de gallo.
UML Clases              → diagrama visual.
Wireframes              → lienzo de maquetas.
Diccionario de datos    → documento/tablas estructuradas.
Roles y permisos        → matriz o vista documental.
Levantamiento lógico    → ficha/expediente estructurado del nodo seleccionado.
Ayuda académica         → lectura documental/búsqueda.
```

Debe evitar llenarse de botoneras redundantes o inspectores persistentes que reduzcan el área principal. El inspector contextual pertenece preferentemente al SideDock como módulo alternable. Una columna fija dentro del workspace solo debe usarse como excepción explícita del artefacto.

## Criterio visual

Para mantener coherencia con la estética de escritorio del producto:

```txt
usar separadores finos;
preferir bordes rectos;
evitar paneles redondeados innecesarios dentro del workspace y del SideDock;
conservar iconografía coherente con los PNG existentes;
documentar CSS por bloques cuando se agreguen estilos nuevos.
```

## Aplicación en Levantamiento lógico

En `logical-business-intake`, la toolbar contextual abre módulos del SideDock para acciones frecuentes de revisión:

```txt
Validar → SideDock / Validación
Trazas internas → SideDock / Trazas internas
Artefactos compatibles → SideDock / Artefactos compatibles
Estructura → SideDock / Estructura
Propiedades → SideDock / Propiedades
Ayuda → SideDock / Ayuda
Markdown → exportación Markdown canónica
```

Estas acciones no agregan una tercera columna al workspace. El workspace sigue portando la ficha amplia del nodo seleccionado. La edición CRUD documental queda separada para una fase posterior.

## Nota de transición de nombres

Algunos contratos siguen llamándose `DiagramToolbar*` porque nacieron cuando la aplicación era principalmente diagramática. El rol vigente es más amplio. La vista JavaFX central de esa región se renombra gradualmente a `ContextualToolbarView` para reflejar que depende del artefacto activo y no solo de diagramas.
