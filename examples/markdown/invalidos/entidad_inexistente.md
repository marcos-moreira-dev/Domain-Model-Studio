---
id: invalido_entidad_inexistente
title: Ejemplo inválido - entidad inexistente
notation: chen
version: 1.0.0
---

# Entidades

## Producto
id: producto

- pk id
- nombre

# Relaciones

## Pertenece
id: pertenece
from: Producto
to: Categoria
from_cardinality: 0..M
to_cardinality: 1
description: Esta relación debe fallar porque Categoria no existe.
