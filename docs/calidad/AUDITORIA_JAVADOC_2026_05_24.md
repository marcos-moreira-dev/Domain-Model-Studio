# Auditoría JavaDoc — 2026-05-24

## Resumen ejecutivo

El proyecto tiene una base JavaDoc fuerte en tipos públicos, paquetes y contratos generales. Es suficiente para navegar la arquitectura por capas y entender la mayoría de clases principales.

La deuda principal está en JavaDoc de métodos públicos. Si el proyecto se usará como material de estudio de ingeniería de software, conviene abrir tandas específicas de documentación de código para explicar contratos, precondiciones, efectos, invariantes, casos de uso y responsabilidades.

## Métrica fuente

Escaneo estático sobre `src/main/java`.

| Métrica | Resultado |
|---|---:|
| Archivos Java en main escaneados | 1110 |
| `package-info.java` | 91 |
| Tipos públicos detectados | 750 |
| Tipos públicos con JavaDoc inmediato | 738 |
| Cobertura de tipos públicos | 98.4% |
| Miembros públicos detectados | 3719 |
| Miembros públicos con JavaDoc inmediato | 22 |
| Cobertura de miembros públicos | 0.6% |

## Lectura por capa

| Capa | Archivos | Tipos públicos con JavaDoc | Cobertura tipos | Miembros públicos con JavaDoc | Cobertura miembros |
|---|---:|---:|---:|---:|---:|
| `domain` | 168 | 143/143 | 100.0% | 2/799 | 0.3% |
| `application` | 317 | 260/264 | 98.5% | 9/577 | 1.6% |
| `infrastructure` | 128 | 53/54 | 98.1% | 0/107 | 0.0% |
| `presentation` | 489 | 275/282 | 97.5% | 11/2219 | 0.5% |
| `bootstrap` | 7 | 6/6 | 100.0% | 0/16 | 0.0% |

## Interpretación

### Lo bueno

```txt
- La documentación de tipos públicos es alta.
- Hay muchos package-info.java, lo cual ayuda a leer la arquitectura por paquetes.
- La intención de capas se entiende: domain, application, infrastructure, presentation y bootstrap.
- El proyecto ya sirve para estudiar diseño por responsabilidades, catálogos, workbenches, parsers, exportadores y validación.
```

### Deuda real

```txt
- Los métodos públicos casi no tienen JavaDoc individual.
- Muchos constructores/factories/accessors públicos se entienden por nombre, pero no explican contrato.
- Falta JavaDoc pedagógico en flujos de alto valor: persistencia, importación Markdown, exportación, canvas transversal, SideDock y grafo lógico.
```

## Criterio recomendado

No documentar todos los getters/setters. Priorizar métodos públicos que cumplen una de estas condiciones:

```txt
- orquestan un caso de uso;
- transforman estado;
- aplican validación semántica;
- importan/exportan formatos;
- guardan/leen persistencia;
- son puertos/adapters/contributors;
- tienen precondiciones, invariantes o postcondiciones no obvias;
- son API interna estable para otros módulos.
```

## Decisión

```txt
El instalable no queda bloqueado por JavaDoc.
Sí se recomienda abrir tandas JavaDoc si el proyecto será usado para estudiar ingeniería de software y desarrollo de software.
```

## Cierre ejecutivo

Para el objetivo pedagógico del proyecto, sí conviene planificar tandas de documentación de código, especialmente en contratos públicos no triviales.
