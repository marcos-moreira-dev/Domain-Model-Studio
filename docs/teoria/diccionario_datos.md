# Diccionario de datos

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Documentar el significado de entidades/tablas lógicas y campos del sistema, incluyendo tipos, obligatoriedad, reglas, validaciones, ejemplos y observaciones.

## Cuándo usar

```txt
- después de tener un modelo conceptual base
- antes de implementar base de datos y backend
- para generar documentación PDF entregable
- para revisar reglas de validación
- para mantener trazabilidad entre negocio, datos y código
```

## Cuándo no usar

```txt
- como reemplazo del modelo conceptual
- como ERD físico PostgreSQL
- como listado automático sin significado de negocio
- para documentar cada índice técnico menor si no aporta al usuario o al mantenimiento
```

## Elementos permitidos

```txt
Entidad o tabla lógica
Campo
Tipo lógico
Tipo físico sugerido
Obligatoriedad
Unicidad
Clave primaria lógica
Clave foránea lógica
Descripción
Regla de negocio
Validación
Ejemplo
Observación
Módulo relacionado
Visibilidad en formulario/reporte
```

## Relaciones permitidas

```txt
Campo pertenece a entidad.
Campo puede referenciar otra entidad.
Campo puede estar asociado a una regla de negocio.
Entidad puede pertenecer a un módulo administrativo.
```

## Reglas visuales

El diccionario de datos no necesita renderer visual complejo. Su salida principal futura debe ser tabular/documental:

```txt
Entidad
Campo
Tipo lógico
Obligatorio
Único
Descripción
Regla/validación
Ejemplo
```

## Reglas semánticas

```txt
- Debe explicar significado, no solo estructura.
- Puede cambiar con más frecuencia que el modelo conceptual.
- No debe atarse totalmente a PostgreSQL.
- Puede incluir tipo PostgreSQL sugerido, pero no debe depender de sintaxis específica.
```

## Errores comunes

```txt
- generar PDF con todos los diagramas y volverlo pesado de mantener
- documentar campos sin descripción ni reglas
- mezclar usuario final con usuario de base de datos
- tratar cada migración técnica como cambio conceptual obligatorio
```

## Ejemplo mínimo

```txt
Entidad: Cliente
Campo: cedula
Tipo lógico: texto corto
Tipo PostgreSQL sugerido: varchar(10)
Obligatorio: sí
Único: sí
Descripción: número de identificación del cliente
Validación: debe tener formato válido según el país
Ejemplo: 0912345678
```

## Relación con aplicaciones administrativas

Es uno de los entregables más útiles para sistemas reales. Sirve para backend, base de datos, validaciones, reportes, mantenimiento, QA y comunicación con cliente.
