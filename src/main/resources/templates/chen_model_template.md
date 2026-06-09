---
id: nuevo_modelo_v1
title: Modelo conceptual - Nombre del dominio
notation: chen
version: 1.0.0
status: draft
author: @programalobien
source: pendiente
---

# Entidades

## EntidadPrincipal
id: entidad_principal
module: modulo_principal
description: Describir qué representa esta entidad dentro del negocio.
status: draft
confidence: medium

- pk id
- nombre
- estado

## EntidadRelacionada
id: entidad_relacionada
module: modulo_principal
description: Describir la entidad relacionada.
status: draft
confidence: medium

- pk id
- nombre
- estado

# Relaciones

## Relaciona
id: relaciona
from: EntidadPrincipal
to: EntidadRelacionada
from_cardinality: 1
to_cardinality: 0..M
description: Describir con lenguaje humano qué significa esta relación.
status: draft
confidence: medium

# Reglas de negocio

- Agregar reglas confirmadas o inferidas con claridad.

# Dudas pendientes

- Agregar dudas que deban validarse con cliente, tutor o usuario del negocio.
