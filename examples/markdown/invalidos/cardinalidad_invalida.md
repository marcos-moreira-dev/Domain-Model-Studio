---
id: invalido_cardinalidad
title: Ejemplo inválido - cardinalidad inválida
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
from_cardinality: muchos
to_cardinality: uno
description: Esta relación debe fallar porque las cardinalidades no usan formato permitido.
