# Manifest de cambios — PF05 y PF06

Estado: **planificación agregada**  
Tipo: **sin implementación de código**

## Archivos agregados

```txt
docs/productizacion/PF05_importacion_layout_persistencia_por_tipo.md
docs/productizacion/PF06_estado_paneles_contextuales.md
docs/productizacion/PF05_PF06_manifest_cambios.md
```

## Archivos actualizados

```txt
docs/productizacion/00_indice_productizacion.md
```

## Alcance real de esta tanda

Se agregaron dos tandas de planificación:

```txt
PF05 — Importación Markdown, layout inicial y persistencia por tipo.
PF06 — Barra de estado, estructura y propiedades contextuales por tipo.
```

No se modificaron:

```txt
archivos Java;
archivos CSS;
plantillas Markdown;
ejemplos oficiales;
scripts;
recursos gráficos;
configuración Maven.
```

## Decisiones reforzadas

```txt
La importación no debe aplicar layout Chen a todos los tipos.
Chen/pata de gallo pertenecen solo al modelo conceptual.
Cada tipo visible debe abrir el documento/workspace que corresponde.
Guardar y abrir .dms debe conservar el DiagramTypeId y documento especializado.
La barra de estado debe mostrar conteos contextuales, no siempre entidades/relaciones.
Los paneles izquierdo y derecho deben depender del workspace activo.
MainShellState y MainShellView no deben crecer con cadenas largas de if/else por tipo.
```

## Siguiente planificación pendiente

```txt
PF07 — Centro de ayuda como micro-Wikipedia.
PF08 — Ejemplos gorditos UENS/colegio y selector de ejemplos oficiales.
PF09 — Plan de pruebas de cierre funcional y smoke test visual real.
```
