# C4 Contexto y Contenedores

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Explicar arquitectura de software en niveles entendibles. En esta fase se consideran dos niveles:

```txt
C4 Contexto: personas, sistema principal y sistemas externos.
C4 Contenedores: aplicaciones, backend, base de datos, servicios y relaciones técnicas principales.
```

## Cuándo usar

```txt
- para explicar una aplicación autocontenida
- para separar desktop, backend y base de datos
- para definir si habrá backend local o remoto
- para ubicar sistemas externos
- antes de discutir despliegue
```

## Cuándo no usar

```txt
- para detallar cada clase
- para describir campos de datos
- para representar el flujo exacto de un proceso de negocio
- para reemplazar un diagrama de casos de uso
```

## Elementos permitidos

Nivel contexto:

```txt
Persona
Sistema de software
Sistema externo
Relación
Límite del sistema
```

Nivel contenedores:

```txt
Aplicación desktop
Aplicación web
Backend/API
Base de datos
Servicio externo
Sistema de archivos
Cola/worker, si aplica
```

## Relaciones permitidas

```txt
Persona -> Sistema
Sistema -> Sistema externo
Aplicación -> Backend
Backend -> Base de datos
Backend -> Servicio externo
```

## Reglas visuales

```txt
Persona: actor o caja identificada
Sistema/contenedor: rectángulo
Relación: flecha con verbo/descripción
Límite: contenedor visual del sistema
```

## Reglas semánticas

```txt
- El nivel contexto no debe entrar en detalles internos.
- El nivel contenedores debe explicar piezas ejecutables o almacenamientos principales.
- Las relaciones deben decir qué se intercambia o por qué existe la comunicación.
```

## Errores comunes

```txt
- usar C4 como UML de clases
- meter tablas dentro del diagrama de contexto
- confundir módulo funcional con contenedor ejecutable
- no diferenciar sistema externo de componente interno
```

## Ejemplo mínimo

```txt
Usuario administrador -> Sistema de gestión óptica
Sistema de gestión óptica -> Servicio de correo
Sistema de gestión óptica -> Base de datos PostgreSQL
```

## Relación con aplicaciones administrativas

Muy útil para explicar sistemas desktop + backend + base de datos local/remota, especialmente en aplicaciones autocontenidas.
