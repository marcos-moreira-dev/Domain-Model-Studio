# Auditoría JavaDoc JD-2 — Aplicación y servicios

## Alcance

La tanda JD-2 refuerza la documentación de código en la capa de aplicación, especialmente en paquetes que explican cómo se coordinan casos de uso sin depender de JavaFX ni de infraestructura concreta.

## Paquetes cubiertos

```txt
application/catalog
application/logicalbusiness
application/logicalbusiness/derivation
application/export
application/project
application/visual
application/workspace
```

## Criterio de calidad

JavaDoc debe responder:

```txt
- qué contrato coordina la clase;
- qué precondiciones recibe;
- qué resultado entrega;
- qué responsabilidad no debe asumir;
- cómo se relaciona con dominio, infraestructura y presentación.
```

## Decisión de alcance

No se documentaron getters/setters triviales ni todos los métodos públicos. La tanda prioriza servicios de aplicación y contratos pedagógicos para estudiar arquitectura por capas.

## Resultado esperado

El onboarding de código ahora permite leer la secuencia:

```txt
dominio → aplicación → infraestructura → presentación
```

y entender que la aplicación coordina decisiones, no dibuja pantallas ni parsea archivos.
