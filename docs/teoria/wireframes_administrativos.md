# Wireframes administrativos

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Representar la estructura básica de pantallas administrativas sin entrar en diseño gráfico final.

## Cuándo usar

```txt
- para planificar pantallas de CRUD
- para definir navegación interna
- para ordenar formularios, tablas, filtros y acciones
- antes de implementar frontend o JavaFX
```

## Cuándo no usar

```txt
- para diseñar landing pages promocionales
- para definir branding final
- para reemplazar casos de uso o procesos
- para simular pixel perfect si aún se está levantando alcance
```

## Elementos permitidos

```txt
Pantalla
Barra superior
Menú lateral
Tabla
Formulario
Filtro
Botón de acción
Modal
Panel de detalle
Estado vacío
Mensaje de error
Tarjeta/resumen
```

## Relaciones permitidas

```txt
Pantalla contiene componente.
Botón navega a pantalla.
Acción abre modal.
Formulario guarda entidad.
Filtro afecta tabla.
```

## Reglas visuales

```txt
Usar cajas simples.
Priorizar jerarquía funcional.
No usar decoración excesiva.
No confundir wireframe con diseño gráfico final.
```

## Reglas semánticas

```txt
- Cada pantalla debe tener propósito operativo.
- Cada acción visible debe responder a una necesidad del usuario.
- El lenguaje visible debe pertenecer al dominio de la aplicación, no al backend.
```

## Errores comunes

```txt
- diseñar pantallas sin roles ni casos de uso
- mostrar términos técnicos internos al usuario final
- crear botones sin acción definida
- convertir wireframe en mockup de marketing
```

## Ejemplo mínimo

```txt
Pantalla: Gestión de clientes
Contiene: tabla de clientes, buscador, botón Nuevo cliente, botón Editar, panel de detalle.
```

## Relación con aplicaciones administrativas

Muy importante para sistemas internos, desktop, JavaFX, Angular administrativo y apps autocontenidas.
