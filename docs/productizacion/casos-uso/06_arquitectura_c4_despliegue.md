# Casos de uso — arquitectura C4 y despliegue técnico

Estado: **matriz de control de diagramas de arquitectura**  
Alcance: C4 contexto, C4 contenedores y despliegue técnico.

---

## Resumen por tipo

| Tipo | % al ojo | Estado | Lectura rápida |
|---|---:|---|---|
| C4 Contexto | 62% | Funcional básico | Personas/sistemas/relaciones existen; falta layout C4 más fiel. |
| C4 Contenedores | 63% | Funcional básico | Aplicaciones/API/BD/servicios existen; falta legibilidad e interacción visual persistente. |
| Despliegue técnico | 58% | Funcional básico | Nodos de infraestructura existen; necesita semántica visual de ambientes/redes/hosting. |

---

# C4 Contexto

Promesa correcta: **representar personas, sistema principal, sistemas externos y relaciones de alto nivel**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| C4CTX-01 Crear contexto | Terminado funcional | 80% | `CreateArchitectureDiagramUseCase`, `ArchitectureDiagramKind.C4_CONTEXT` | Smoke desde `Nuevo`. |
| C4CTX-02 Importar Markdown | Terminado funcional | 80% | `ArchitectureMarkdownParser` | Probar UENS y mínimo. |
| C4CTX-03 Agregar persona/sistema/externo/límite | Terminado funcional | 75% | `AddArchitectureNodeUseCase`, `ArchitectureNodeKind` | Diferenciar personas/sistemas. |
| C4CTX-04 Agregar relaciones | Terminado funcional | 75% | `AddArchitectureEdgeUseCase` | Tabla y visual. |
| C4CTX-05 Editar/eliminar | Terminado funcional | 70% | `Update/RemoveArchitecture*UseCase` | Persistencia. |
| C4CTX-06 Validar/exportar | Terminado básico | 72% | `ValidateArchitectureDiagramUseCase`, PNG/Markdown | Smoke tab activa. |
| C4CTX-07 Layout C4 de contexto | Parcial | 35% | Plan AV-I07 | Ordenar actores/sistemas y reducir cruces. |

## Casos faltantes C4 Contexto

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| C4CTX-FALT-01 | Sistema principal destacado | Alta | En C4 contexto debe verse el sistema bajo estudio. |
| C4CTX-FALT-02 | Personas externas/internas diferenciadas | Media | Mejora lectura ejecutiva. |
| C4CTX-FALT-03 | Límites de sistema como contenedor real | Media/alta | Evita parecer mapa de cajas genérico. |
| C4CTX-FALT-04 | Arrastre y rutas persistentes | Alta | Necesario para diagramas de cliente. |

---

# C4 Contenedores

Promesa correcta: **representar aplicaciones, API/backend, base de datos, reportes, servicios externos y relaciones técnicas principales**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| C4CON-01 Crear contenedores | Terminado funcional | 82% | `ArchitectureDiagramKind.C4_CONTAINERS` | Smoke desde `Nuevo`. |
| C4CON-02 Importar Markdown | Terminado funcional | 80% | `ArchitectureMarkdownParser` | Probar UENS y mínimo. |
| C4CON-03 Agregar aplicación/API/BD/servicio | Terminado funcional | 75% | `AddArchitectureNodeUseCase` | Figuras/íconos diferenciados. |
| C4CON-04 Agregar llama/lee/escribe/integra | Terminado funcional | 75% | `AddArchitectureEdgeUseCase`, tipos de relación | Etiquetas visibles. |
| C4CON-05 Editar/eliminar | Terminado funcional | 70% | `Update/RemoveArchitecture*UseCase` | Round-trip `.dms`. |
| C4CON-06 Validar/exportar | Terminado básico | 72% | Validación + PNG/Markdown | Smoke tab activa. |
| C4CON-07 Layout técnico legible | Parcial | 35% | Plan AV-I07 | Evitar cruces y cajas cortadas. |

## Casos faltantes C4 Contenedores

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| C4CON-FALT-01 | Agrupar contenedores por sistema/límite | Alta | C4 usa boundaries para claridad. |
| C4CON-FALT-02 | Diferenciar BD/API/app visualmente | Alta | Fundamental para arquitectura. |
| C4CON-FALT-03 | Ruta de conexiones persistente | Alta | Las capturas muestran cruces. |
| C4CON-FALT-04 | Campo tecnología visible sin saturar | Media | Ej.: JavaFX, Spring Boot, PostgreSQL. |

---

# Despliegue técnico

Promesa correcta: **representar entornos, máquinas, clientes, servicios, red, artefactos y relaciones de despliegue**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| DEP-01 Crear despliegue | Terminado funcional | 78% | `ArchitectureDiagramKind.TECHNICAL_DEPLOYMENT` | Smoke desde `Nuevo`. |
| DEP-02 Importar Markdown | Terminado funcional | 78% | `ArchitectureMarkdownParser` | Probar UENS y piloto mínimo. |
| DEP-03 Agregar ambiente/servidor/cliente/servicio/red/artefacto | Terminado funcional | 72% | Toolbar despliegue, `ArchitectureNodeKind` | Figuras diferenciadas. |
| DEP-04 Agregar conexión/hosting/target | Terminado funcional | 70% | `ArchitectureEdgeKind` | Etiquetas claras. |
| DEP-05 Editar/eliminar | Terminado funcional | 68% | Use cases de arquitectura | Persistencia. |
| DEP-06 Validar/exportar | Terminado básico | 70% | Validación + PNG/Markdown | Smoke tab activa. |
| DEP-07 Semántica visual de infraestructura | Parcial fuerte | 30% | Plan AV-I07 | Ambientes/redes/servidores deberían organizarse visualmente. |

## Casos faltantes despliegue técnico

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| DEP-FALT-01 | Ambientes como contenedores | Alta | Dev/prod/local/remoto deben agrupar nodos. |
| DEP-FALT-02 | Red/subred como zona visual | Media | Evita que red sea solo una caja suelta. |
| DEP-FALT-03 | Diferenciar artefacto desplegado vs servicio ejecutándose | Media | Importante para despliegue técnico. |
| DEP-FALT-04 | Layout editable persistente | Alta | Igual que C4. |

---

## Riesgo común de arquitectura

Los tres tipos comparten `ArchitectureDiagramEditorView` y `ArchitectureDiagramViewModel`. Eso es bueno para no duplicar demasiado, pero también puede convertir C4 y despliegue en “cajas genéricas”. La alineación correcta es:

```text
mismo motor de edición
+ adaptadores visuales por tipo
+ figuras y layout propios según C4/despliegue
+ persistencia de posiciones y rutas
```
