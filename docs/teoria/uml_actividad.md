# UML Actividad

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Representar flujo de acciones, decisiones y ramas dentro de una operación del sistema.

## Cuándo usar

```txt
- para describir una operación funcional concreta
- para detallar lógica de validación
- para explicar flujos internos de una función
- para bajar un caso de uso a pasos más específicos
```

## Cuándo no usar

```txt
- para procesos de negocio con múltiples áreas donde BPMN sea más claro
- para definir datos
- para diseñar pantallas finales
```

## Elementos permitidos

```txt
Nodo inicial
Acción
Decisión
Merge
Fork/Join, si aplica
Nodo final
Flujo de control
```

## Relaciones permitidas

```txt
Inicio -> Acción
Acción -> Acción
Acción -> Decisión
Decisión -> Acción con condición
Acción -> Fin
```

## Reglas visuales

```txt
Inicio: círculo sólido
Acción: rectángulo redondeado
Decisión: rombo
Fin: círculo final
Flecha: flujo de control
```

## Reglas semánticas

```txt
- Las acciones deben ser verbos.
- Las ramas deben tener condición.
- Debe existir inicio y fin cuando el flujo sea cerrado.
```

## Errores comunes

```txt
- usarlo para datos estáticos
- mezclar cada pantalla como si fuera acción lógica
- crear decisiones sin condiciones
```

## Ejemplo mínimo

```txt
Inicio -> Validar formulario -> ¿Datos correctos?
  Sí -> Guardar registro -> Fin
  No -> Mostrar errores -> Fin
```

## Relación con aplicaciones administrativas

Útil para operaciones como registrar venta, aprobar solicitud, generar reporte o procesar garantía.
