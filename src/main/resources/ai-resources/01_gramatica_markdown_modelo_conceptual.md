# Domain Model Studio — Gramática Markdown para IA

Este documento sirve para que una IA genere archivos Markdown importables por **Domain Model Studio**. El objetivo no es escribir Markdown libre, sino un Markdown semántico, estable y fácil de parsear.

## 1. Regla principal

El Markdown describe el **modelo conceptual**: entidades, atributos, relaciones, cardinalidades, participación y metadatos. No debe describir posiciones exactas del lienzo, zoom, colores, tamaño de nodos ni decisiones de exportación.

```txt
Dominio -> Markdown
Posición visual -> layout interno del proyecto .dms
Colores y apariencia -> estilos internos del proyecto .dms
Imagen final -> exportación SVG/PNG
```

## 2. Estructura mínima válida

Todo archivo debe tener:

```md
---
id: identificador_estable
title: Título humano del modelo
notation: chen
version: 1.0.0
status: draft
---

# Entidades

## Nombre de entidad
id: nombre_entidad
kind: strong
module: modulo
status: draft
description: Descripción humana de la entidad.

- pk id
- nombre

# Relaciones

## Nombre de relación
id: nombre_relacion
from: Entidad origen
to: Entidad destino
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: total
to_participation: partial
description: Descripción humana de la relación.
```

## 3. Front matter

Campos recomendados:

```yaml
id: supermercado_complejo_v1
title: Modelo conceptual complejo - Supermercado
notation: chen
project_type: CONCEPTUAL_MODEL
version: 1.0.0
status: draft
author: @programalobien
source: levantamiento_con_ia
business_area: administracion
```

### Campos importantes

| Campo | Obligatorio | Valores recomendados |
|---|---:|---|
| `id` | sí | minúsculas, sin tildes, con guion bajo |
| `title` | sí | texto humano claro |
| `notation` | recomendado | `chen`, `crows_foot`, `crow_foot`, `pata_de_gallo` |
| `project_type` | opcional | `CONCEPTUAL_MODEL` |
| `version` | recomendado | `1.0.0` |
| `status` | opcional | `draft`, `review`, `confirmed`, `deprecated` |

La notación visual puede cambiarse dentro del programa. Para la IA, lo importante es generar el mismo modelo semántico. `notation: chen` arranca en Chen; `notation: crows_foot` o `notation: pata_de_gallo` arranca en pata de gallo.

## 4. Entidades

Las entidades viven bajo `# Entidades` y cada una empieza con `##`.

```md
## Producto
id: producto
kind: strong
module: inventario
status: confirmed
description: Artículo vendible, almacenable o trazable dentro del negocio.

- pk id
- nombre
- codigo_barras [unique]
- precio_actual
- stock_actual [derived]
```

### Campos de entidad

| Campo | Uso |
|---|---|
| `id` | identificador estable; si falta se deriva del nombre |
| `kind` | `strong`, `weak`, `debil`, `débil` |
| `module` | agrupación lógica: inventario, ventas, seguridad, caja, etc. |
| `status` | estado documental |
| `description` | explicación humana |

## 5. Atributos

Los atributos se escriben como bullets debajo de su entidad.

```md
- pk id
- codigo_barras [unique]
- telefono [optional]
- edad [derived]
- correos [multivalued]
- password_hash [sensitive]
- direccion [composite]
```

### Tags soportados

| Tag | Significado conceptual | Visual sugerido en Chen |
|---|---|---|
| `[pk]` o `pk nombre` | clave principal | atributo clave |
| `[partial_key]` | clave parcial de entidad débil | clave parcial |
| `[unique]` | valor único | marca textual o estilo secundario |
| `[optional]` | dato opcional | estilo discreto |
| `[derived]` / `[computed]` | dato calculado | atributo derivado |
| `[multivalued]` | múltiples valores | atributo multivaluado |
| `[composite]` | atributo compuesto | atributo compuesto |
| `[sensitive]` | dato sensible | marca de cuidado |

## 6. Relaciones binarias

Las relaciones viven bajo `# Relaciones` y cada una empieza con `##`.

```md
## Realiza
id: realiza
from: Cliente
to: Pedido
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Un cliente puede realizar muchos pedidos; cada pedido pertenece a un cliente.
```

### Campos de relación

| Campo | Obligatorio | Uso |
|---|---:|---|
| `id` | recomendado | identificador estable |
| `from` | sí | entidad origen, por nombre visible o id |
| `to` | sí | entidad destino, por nombre visible o id |
| `from_cardinality` | sí | cardinalidad del lado `from` |
| `to_cardinality` | sí | cardinalidad del lado `to` |
| `kind` | opcional | `regular`, `identifying`, `identificadora`, `associative`, `asociativa` |
| `from_participation` | opcional | `partial`, `parcial`, `total`, `unspecified` |
| `to_participation` | opcional | `partial`, `parcial`, `total`, `unspecified` |
| `description` | opcional | regla humana de la relación |

## 7. Cardinalidades soportadas

```txt
0..1
1
0..M
1..M
M
N
0..35
2..5
```

Recomendación para IA: usar `1`, `0..1`, `0..M` y `1..M` cuando sea posible. Usar rangos numéricos solo cuando haya una regla de negocio real.

## 8. Participación en Chen

| Valor | Significado |
|---|---|
| `total` | toda instancia de ese lado debe participar |
| `partial` / `parcial` | puede existir sin participar |
| `unspecified` | no se especifica |

Ejemplo:

```md
## Tiene detalle
id: tiene_detalle
from: Venta
to: Detalle de venta
from_cardinality: 1
to_cardinality: 1..M
kind: identifying
from_participation: total
to_participation: total
description: Una venta debe tener al menos un detalle; cada detalle existe por una venta.
```

## 9. Notación Chen vs pata de gallo

El Markdown describe la semántica. La diferencia visual la decide el programa:

| Concepto | Chen | Pata de gallo |
|---|---|---|
| Entidad | rectángulo | tabla/caja de entidad |
| Atributo | óvalo conectado | fila/campo dentro o asociado a la entidad |
| Relación | rombo | línea con cardinalidad en extremos |
| Cardinalidad | texto cerca de la conexión | pata de gallo, barra, círculo o etiqueta |
| Participación total/parcial | estilo de línea o marca | normalmente se interpreta con opcionalidad/cardinalidad |

Para generar archivos compatibles, la IA debe priorizar `from`, `to`, `from_cardinality`, `to_cardinality`, `kind` y participación. El programa se encarga de representarlo.

## 10. Relaciones complejas y ternarias

La gramática actual del importador trabaja con relaciones binarias `from/to`. Cuando el dominio tiene una relación ternaria real, se recomienda modelarla como una **entidad asociativa** y conectarla con relaciones binarias.

Ejemplo conceptual: “Proveedor suministra Producto a Sucursal bajo un Contrato”.

Forma recomendada:

```md
## Suministro contratado
id: suministro_contratado
kind: strong
module: compras
description: Entidad asociativa que representa el hecho de que un proveedor suministra un producto a una sucursal bajo condiciones pactadas.

- pk id
- fecha_inicio
- precio_pactado
- cantidad_minima

# Relaciones

## Proveedor participa en suministro
id: proveedor_participa_suministro
from: Proveedor
to: Suministro contratado
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total

## Producto participa en suministro
id: producto_participa_suministro
from: Producto
to: Suministro contratado
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total

## Sucursal participa en suministro
id: sucursal_participa_suministro
from: Sucursal
to: Suministro contratado
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
```

Esto mantiene compatibilidad con el parser actual y permite representar lógicas de tres o más participantes sin romper el archivo.

## 11. Entidades débiles

Usar `kind: weak` y relaciones `kind: identifying`.

```md
## Detalle de venta
id: detalle_venta
kind: weak
module: ventas
description: Línea de venta dependiente de una venta principal.

- numero_linea [partial_key]
- cantidad
- precio_unitario
- subtotal [derived]

## Venta identifica detalle
id: venta_identifica_detalle
from: Venta
to: Detalle de venta
from_cardinality: 1
to_cardinality: 1..M
kind: identifying
from_participation: total
to_participation: total
description: El detalle no existe sin la venta.
```

## 12. Reglas para que la IA genere bien

1. Crear primero todas las entidades.
2. Usar IDs estables en minúsculas y con guion bajo.
3. No inventar sintaxis fuera de este documento.
4. No usar tablas Markdown para entidades ni relaciones.
5. No poner código SQL dentro del archivo importable.
6. Usar descripciones humanas breves pero útiles.
7. Para relaciones complejas, crear entidades asociativas.
8. Evitar relaciones sin cardinalidad.
9. Evitar nombres ambiguos como “Datos”, “Registro”, “Cosa” o “Información”.
10. Cuando haya duda, preferir `status: draft` y describir la incertidumbre en `description`.
