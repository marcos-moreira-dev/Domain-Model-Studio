# 21 - Gramática Markdown estructurada


> Nota de investigación: esta gramática está basada en una versión práctica de la notación Chen para modelo conceptual. La base teórica y las decisiones de alcance están resumidas en `docs/20_investigacion_chen_modelo_conceptual.md`.

## 1. Propósito de este documento

Este documento define la primera gramática de entrada de **Domain Model Studio**.

La aplicación no debe intentar interpretar cualquier Markdown libre. El objetivo es usar un Markdown humano, legible para una persona y fácil de generar por IA, pero suficientemente estructurado para que un parser pueda convertirlo en un modelo interno sin adivinar.

La gramática inicial está pensada para diagramas conceptuales con notación **Chen**:

- entidades;
- atributos;
- relaciones;
- cardinalidades;
- metadatos;
- notas de trazabilidad;
- etiquetas simples para distinguir atributos clave, derivados, opcionales, únicos, sensibles o multivaluados.

El Markdown es **entrada semántica**. No es el archivo editable definitivo del proyecto. El archivo editable será `.dms`, que guardará modelo, layout, estilos y estado visual.

## 2. Principio rector

La gramática debe obedecer este principio:

```txt
Lo que pertenece al dominio debe estar en Markdown.
Lo que pertenece al acomodo visual debe estar en layout.
Lo que pertenece al estilo debe estar en style.
Lo que pertenece a exportación debe estar en SVG/PNG.
```

Por eso el Markdown debe enfocarse en **qué existe** y **cómo se relaciona**, no en posiciones exactas de pantalla.

## 3. Estructura general del archivo

Todo archivo válido debe seguir esta forma:

```md
---
id: supermercado_v1
title: Modelo conceptual - Supermercado
notation: chen
version: 1.0.0
status: draft
---

# Entidades

## Producto
id: producto
module: inventario
description: Producto vendido o administrado por el negocio.

- pk id
- nombre
- codigo_barras [unique]
- precio_actual
- stock_actual [derived]
- estado

# Relaciones

## Pertenece
id: pertenece
from: Producto
to: Categoria
from_cardinality: 0..M
to_cardinality: 1
description: Cada producto pertenece a una categoría; una categoría puede agrupar muchos productos.
```

Secciones obligatorias:

```txt
Front matter
# Entidades
# Relaciones
```

Secciones futuras posibles:

```txt
# Vistas
# Notas
# Reglas de negocio
# Dudas pendientes
# Convenciones
```

Para el MVP, solo son estrictamente necesarias `# Entidades` y `# Relaciones`.

## 4. Front matter

El front matter usa sintaxis YAML simple entre `---`.

Campos recomendados:

```yaml
id: supermercado_v1
title: Modelo conceptual - Supermercado
notation: chen
version: 1.0.0
status: draft
author: @programalobien
source: levantamiento_reunion_01
migration_ref: V1__initial_schema
```

### 4.1 Campos obligatorios iniciales

| Campo | Obligatorio | Ejemplo | Descripción |
|---|---:|---|---|
| `id` | sí | `supermercado_v1` | Identificador estable del modelo. |
| `title` | sí | `Modelo conceptual - Supermercado` | Título humano. |
| `notation` | sí | `chen` | Notación solicitada. En MVP: `chen`. |
| `version` | sí | `1.0.0` | Versión documental del modelo. |

### 4.2 Campos opcionales útiles

| Campo | Ejemplo | Uso |
|---|---|---|
| `status` | `draft` | Estado documental. |
| `author` | `@programalobien` | Autor o responsable. |
| `source` | `reunion_01` | Fuente de información. |
| `migration_ref` | `V1__initial_schema` | Referencia a migración o hito. |
| `business_area` | `inventario` | Área del negocio. |
| `notes` | `Modelo preliminar` | Nota general. |

### 4.3 Estados documentales sugeridos

```txt
draft       = borrador
review      = en revisión
confirmed   = validado por usuario/cliente/tutor
deprecated  = reemplazado por una versión posterior
```

## 5. Sección de entidades

La sección inicia con:

```md
# Entidades
```

Cada entidad se define con un título de nivel 2:

```md
## Producto
```

Luego puede incluir metadatos simples con formato `clave: valor`:

```md
id: producto
module: inventario
description: Producto vendido o administrado por el negocio.
status: confirmed
```

Después se listan los atributos con bullets:

```md
- pk id
- nombre
- codigo_barras [unique]
- stock_actual [derived]
```

## 6. Entidad: campos recomendados

| Campo | Obligatorio | Ejemplo | Descripción |
|---|---:|---|---|
| título `##` | sí | `## Producto` | Nombre visible de la entidad. |
| `id` | recomendado | `producto` | ID estable. Si falta, se puede derivar del nombre. |
| `module` | opcional | `inventario` | Agrupación lógica. |
| `description` | opcional | `Producto vendido...` | Explicación humana. |
| `status` | opcional | `confirmed` | Estado documental. |
| `alias` | opcional | `Artículo` | Nombre alternativo. |

Regla práctica:

```txt
El nombre visible puede cambiar.
El id debe intentar permanecer estable.
```

## 7. Atributos

Los atributos se declaran dentro de una entidad con bullets.

Forma simple:

```md
- nombre
```

Forma con tags:

```md
- codigo_barras [unique]
- fecha_nacimiento [optional]
- edad [derived]
- password_hash [sensitive]
```

Forma más explícita:

```md
- id: codigo_barras
  name: código de barras
  tags: [unique]
  description: Código usado para escaneo en caja.
```

El MVP puede aceptar primero la forma simple. La forma explícita puede quedar documentada para expansión futura.

## 8. Tags de atributos

Tags iniciales:

| Tag | Significado conceptual | Representación Chen sugerida |
|---|---|---|
| `[pk]` | Identificador principal | Óvalo subrayado o etiqueta `pk`. |
| `[unique]` | Valor único | Texto o marca visual secundaria. |
| `[optional]` | No obligatorio | Línea o borde punteado, según convención. |
| `[derived]` | Calculado, no necesariamente almacenado | Óvalo punteado. |
| `[multivalued]` | Puede tener varios valores | Óvalo doble. |
| `[sensitive]` | Dato sensible | Marca textual o estilo discreto. |
| `[computed]` | Calculado por sistema | Similar a derivado, pero más técnico. |

Ejemplos:

```md
- pk id
- codigo_barras [unique]
- telefono [optional]
- edad [derived]
- correos [multivalued]
- password_hash [sensitive]
```

## 9. Relaciones

La sección inicia con:

```md
# Relaciones
```

Cada relación se define con título de nivel 2:

```md
## Pertenece
```

Campos mínimos:

```md
id: pertenece
from: Producto
to: Categoria
from_cardinality: 0..M
to_cardinality: 1
```

Campo recomendado:

```md
description: Cada producto pertenece a una categoría; una categoría puede agrupar muchos productos.
```

## 10. Relaciones: campos

| Campo | Obligatorio | Ejemplo | Descripción |
|---|---:|---|---|
| título `##` | sí | `## Pertenece` | Nombre visible de la relación. |
| `id` | recomendado | `pertenece` | ID estable. |
| `from` | sí | `Producto` | Entidad de origen. |
| `to` | sí | `Categoria` | Entidad de destino. |
| `from_cardinality` | sí | `0..M` | Cardinalidad cerca del extremo de `from`. |
| `to_cardinality` | sí | `1` | Cardinalidad cerca del extremo de `to`. |
| `description` | recomendado | `Cada producto...` | Aclaración humana. |
| `status` | opcional | `confirmed` | Estado documental. |

## 11. Cardinalidades válidas iniciales

Cardinalidades permitidas en el MVP:

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

También se aceptan rangos acotados `mínimo..máximo`, siempre que ambos sean enteros válidos y el mínimo no sea mayor que el máximo.

También podrían aceptarse equivalentes en una fase futura:

```txt
0..*
1..*
*
```

Pero para evitar ambigüedad inicial, se recomienda usar `M`.

## 12. Convención semántica de cardinalidad

Para evitar confusión, el documento debe usar esta convención:

```txt
from_cardinality = cuántas instancias de la entidad from pueden asociarse a una instancia del lado contrario.
to_cardinality   = cuántas instancias de la entidad to pueden asociarse a una instancia del lado contrario.
```

Ejemplo:

```md
## Pertenece
from: Producto
to: Categoria
from_cardinality: 0..M
to_cardinality: 1
```

Lectura humana:

```txt
Una categoría puede tener muchos productos.
Un producto pertenece a una categoría.
```

## 13. Metadatos de trazabilidad

Entidades y relaciones pueden incluir:

```md
source: reunion_01
confidence: high
status: confirmed
notes: Validado con el usuario durante la reunión.
```

Valores sugeridos de `confidence`:

```txt
low
medium
high
```

Uso:

```txt
confirmed + high   = validado con alguien del negocio.
draft + medium     = inferido razonablemente.
draft + low        = duda pendiente.
```

## 14. Dudas pendientes

La IA nunca debe inventar silenciosamente.

Se permite agregar una sección:

```md
# Dudas pendientes

- No está claro si un producto puede pertenecer a más de una categoría.
- No está claro si el stock se maneja por sucursal o globalmente.
- No está claro si el precio histórico debe conservarse en DetalleVenta.
```

Esta sección no necesariamente se renderiza como diagrama, pero sí debe conservarse en el modelo documental o en metadatos.

## 15. Reglas de negocio

También puede agregarse:

```md
# Reglas de negocio

- Una venta no puede cerrarse si no tiene al menos un detalle.
- El precio usado en una venta debe quedar congelado en el detalle.
- El stock disponible no debe quedar negativo salvo permiso administrativo especial.
```

En el MVP estas reglas pueden ser texto documental. En el futuro podrían vincularse a entidades o relaciones.

## 16. Estilos opcionales

Aunque el estilo debe vivir principalmente en `.dms`, el Markdown puede sugerir estilos iniciales.

Ejemplo:

```md
## Producto
id: producto
module: inventario
fill: "#FFF7CC"
stroke: "#555555"
text_color: "#222222"
```

Esto no debe ser obligatorio. El usuario podrá cambiar estilos en el editor.

## 17. Layout opcional

No se recomienda poner layout en Markdown para el MVP.

Razón:

```txt
El Markdown debe ser fuente semántica.
El layout debe guardarse en .dms.
```

Podría permitirse a futuro:

```md
layout:
  x: 200
  y: 300
```

Pero no debe ser parte del contrato inicial obligatorio.

## 18. Comentarios

El parser puede ignorar comentarios HTML:

```md
<!-- Este comentario no debe generar elemento visual. -->
```

También puede ignorar párrafos de explicación dentro de secciones si no rompen el formato, pero el MVP debe preferir reglas estrictas.

## 19. Reglas de validación mínimas

El parser debe detectar:

```txt
front matter faltante;
id de modelo faltante;
notation distinta de chen en MVP;
entidad duplicada;
relación duplicada;
relación apuntando a entidad inexistente;
cardinalidad inválida;
atributo vacío;
tag desconocido;
sección obligatoria faltante.
```

Debe devolver errores humanos, no fallar con excepciones crípticas.

Ejemplo de error:

```txt
Error en relación "Pertenece": la entidad "Categoria" no existe. ¿Quiso decir "Categoría"?
```

## 20. Reglas para IA

Cuando una IA genere Markdown para Domain Model Studio, debe:

```txt
usar nombres claros;
crear IDs estables en minúsculas sin espacios;
no inventar relaciones no confirmadas;
marcar dudas pendientes;
usar cardinalidades explícitas;
explicar relaciones con description;
separar reglas de negocio de estructura del diagrama;
evitar layout manual salvo que se solicite.
```

## 21. Ejemplo mínimo válido

```md
---
id: ejemplo_minimo
title: Ejemplo mínimo
notation: chen
version: 1.0.0
---

# Entidades

## Producto
id: producto

- pk id
- nombre

## Categoria
id: categoria

- pk id
- nombre

# Relaciones

## Pertenece
id: pertenece
from: Producto
to: Categoria
from_cardinality: 0..M
to_cardinality: 1
description: Una categoría agrupa productos; cada producto pertenece a una categoría.
```

## 22. Criterio de cierre de esta gramática

Esta gramática es suficiente para comenzar cuando permita:

```txt
parsear entidades;
parsear atributos simples;
parsear tags;
parsear relaciones;
validar cardinalidades;
construir un modelo interno;
generar un diagrama Chen inicial;
mostrar errores humanos.
```


## 18. Relación entre gramática y notación Chen

La gramática no intenta representar un archivo Markdown cualquiera. Representa un subconjunto controlado de conceptos de Chen.

| Concepto Chen | Forma visual | Representación Markdown |
|---|---|---|
| Entidad regular | Rectángulo | `## Producto` |
| Entidad débil | Rectángulo doble | `weak: true` |
| Atributo regular | Óvalo | `- nombre` |
| Atributo clave | Óvalo con texto subrayado | `- id [pk]` o `- pk id` |
| Atributo derivado | Óvalo punteado | `- edad [derived]` |
| Atributo multivaluado | Óvalo doble | `- telefonos [multivalued]` |
| Relación | Rombo | `## Pertenece` en sección Relaciones |
| Relación identificadora | Rombo doble | `type: identifying` |
| Cardinalidad | Etiqueta junto al conector | `from_cardinality` / `to_cardinality` |
| Participación total | Línea doble | `from_participation: total` |
| Participación parcial | Línea simple | `from_participation: partial` |

Para el MVP, algunas propiedades pueden quedar como metadatos aunque la primera versión del canvas no las dibuje todavía con todos los símbolos avanzados.

## 19. Campos extendidos opcionales para Chen

En entidades:

```md
## DetalleVenta
id: detalle_venta
module: ventas
weak: true
owner: venta
status: confirmed
description: Línea de detalle que depende de una venta.
```

En relaciones:

```md
## Identifica
id: identifica_detalle_venta
from: Venta
to: DetalleVenta
from_cardinality: 1
to_cardinality: 1..M
from_participation: partial
to_participation: total
type: identifying
```

Estos campos no son obligatorios en el MVP, pero quedan reservados para no romper compatibilidad cuando el editor visual soporte entidad débil, rombo doble y doble línea de participación.

## 20. Regla de oro para IA

Cuando una IA genere Markdown para Domain Model Studio, debe preferir claridad y trazabilidad antes que adivinar.

Si no está claro si una relación es obligatoria o si una entidad es débil, la IA debe usar:

```md
status: review
confidence: medium
```

y registrar una duda en la sección `# Dudas pendientes`.


## Notaciones disponibles

El front matter puede declarar varias notaciones disponibles aunque el MVP implemente solo Chen.

```yaml
notation: chen
default_notation: chen
available_notations:
  - chen
  - crows_foot
```

Regla:

- `notation` indica la notación inicial del Markdown.
- `default_notation` indica la vista preferida al abrir el proyecto.
- `available_notations` documenta qué vistas se espera poder generar desde el mismo modelo semántico.

Actualmente `chen` y `crows_foot` son vistas del mismo modelo semántico.
