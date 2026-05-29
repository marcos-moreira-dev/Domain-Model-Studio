# BPMN básico

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Representar procesos de negocio mediante eventos, actividades, decisiones, responsables y flujos.

## Cuándo usar

```txt
- cuando hay pasos operativos claros
- cuando participan varios roles o áreas
- cuando existen aprobaciones, rechazos o excepciones
- para entender ventas, compras, soporte, citas, órdenes o garantías
```

## Cuándo no usar

```txt
- para definir campos de base de datos
- para dibujar pantallas
- para modelar clases de código
- para procesos tan simples que una lista de pasos basta
```

## Elementos permitidos en versión básica

```txt
Evento de inicio
Actividad o tarea
Gateway/decisión
Evento de fin
Pool
Lane
Flujo de secuencia
Flujo de mensaje, si aplica
```

## Relaciones permitidas

```txt
Evento -> Actividad
Actividad -> Actividad
Actividad -> Gateway
Gateway -> Actividad
Actividad -> Evento de fin
Pool/Lane agrupa actividades por responsable
```

## Reglas visuales

```txt
Evento: círculo
Actividad: rectángulo redondeado
Gateway: rombo
Flujo: flecha direccional
Pool/Lane: contenedor horizontal o vertical
```

## Reglas semánticas

```txt
- Debe existir al menos un inicio y un fin.
- Las decisiones deben tener salidas con condición clara.
- Las tareas deben representar acciones, no datos.
- Los lanes representan responsables, no módulos técnicos.
```

## Errores comunes

```txt
- usar BPMN para dibujar pantallas
- crear decisiones sin condiciones
- mezclar flujo de usuario con estructura de base de datos
- crear símbolos inventados sin explicar que son simplificaciones
```

## Ejemplo mínimo

```txt
Inicio -> Recibir orden -> Validar datos -> ¿Orden válida?
  Sí -> Registrar orden -> Fin
  No -> Solicitar corrección -> Fin
```

## Relación con aplicaciones administrativas

Ayuda a descubrir estados, módulos, responsables, reglas de aprobación y validaciones antes de programar.
