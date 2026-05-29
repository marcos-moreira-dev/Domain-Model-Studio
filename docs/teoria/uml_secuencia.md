# UML Secuencia

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Representar mensajes ordenados en el tiempo entre actores, pantallas, servicios, repositorios y sistemas.

## Cuándo usar

```txt
- para explicar interacción frontend/backend
- para diseñar una operación técnica puntual
- para entender orden de llamadas
- para revisar responsabilidades entre capas
```

## Cuándo no usar

```txt
- para describir estructura de datos
- para mapear módulos generales
- para procesos de negocio muy amplios
```

## Elementos permitidos

```txt
Participante
Línea de vida
Mensaje síncrono
Mensaje asíncrono, si aplica
Retorno
Activación
Bloque alternativo, opcional
```

## Relaciones permitidas

```txt
Participante -> Participante: mensaje
Participante --> Participante: retorno
Bloque alt agrupa caminos condicionales
```

## Reglas visuales

```txt
Participantes arriba
Tiempo de arriba hacia abajo
Mensajes como flechas horizontales
Retornos como línea punteada o simplificada
```

## Reglas semánticas

```txt
- El orden vertical importa.
- Cada mensaje debe tener intención clara.
- No debe usarse como diagrama de clases.
```

## Errores comunes

```txt
- meter todo el sistema en un solo diagrama
- usar nombres vagos como procesar sin contexto
- dibujar llamadas imposibles entre capas
```

## Ejemplo mínimo

```txt
Usuario -> PantallaVentas: guardar venta
PantallaVentas -> API: POST /ventas
API -> VentaService: registrarVenta
VentaService -> VentaRepository: guardar
VentaRepository --> VentaService: venta guardada
API --> PantallaVentas: resultado exitoso
```

## Relación con aplicaciones administrativas

Ayuda a diseñar operaciones críticas entre UI, backend, base de datos y servicios externos.
