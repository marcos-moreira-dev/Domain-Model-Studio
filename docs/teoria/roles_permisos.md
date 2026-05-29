# Roles y permisos

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Modelar qué tipos de usuarios pueden ejecutar qué acciones dentro del sistema.

## Cuándo usar

```txt
- cuando hay varios perfiles de usuario
- antes de diseñar menús y botones visibles
- antes de implementar seguridad
- para revisar permisos con el cliente
```

## Cuándo no usar

```txt
- como reemplazo de autenticación técnica
- para listar usuarios individuales
- para modelar estructura de datos general
```

## Elementos permitidos

```txt
Rol
Permiso
Módulo
Acción
Restricción
Alcance del permiso
```

## Relaciones permitidas

```txt
Rol tiene permiso.
Permiso pertenece a módulo.
Permiso habilita acción.
Rol puede heredar de otro rol si se decide explícitamente.
```

## Reglas visuales

Puede representarse como matriz o mapa simple:

```txt
Rol x Módulo x Acción
```

## Reglas semánticas

```txt
- Rol no es usuario individual.
- Permiso debe ser verificable.
- Acciones deben mapearse a funciones reales del sistema.
```

## Errores comunes

```txt
- crear roles vagos como usuario normal sin definición
- permitir todo a todos en el diseño
- mezclar permisos de UI con permisos reales de backend sin distinguir
```

## Ejemplo mínimo

```txt
Administrador: crear, editar, eliminar productos.
Vendedor: consultar productos, registrar venta.
Soporte: revisar incidencias, responder tickets.
```

## Relación con aplicaciones administrativas

Fundamental para menús, módulos, botones, endpoints, auditoría y seguridad de sistemas internos.
