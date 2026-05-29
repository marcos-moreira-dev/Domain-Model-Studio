# ADRs pedagógicos y decisiones de diseño

## Propósito

Este documento reúne registros breves de decisión arquitectónica para estudiar el proyecto como ingeniería de software aplicada. No reemplaza el JavaDoc ni los tests: explica el **porqué** detrás de ciertas fronteras del diseño.

La intención pedagógica es leer cada decisión con cuatro preguntas:

```txt
1. ¿Qué problema arquitectónico existía?
2. ¿Qué alternativas eran posibles?
3. ¿Qué decisión se tomó?
4. ¿Qué consecuencias técnicas y de aprendizaje deja?
```

## Cómo leer estos ADRs

Un ADR no es una bitácora larga. Es una cápsula de decisión. Para estudiar el sistema, conviene leer primero el JavaDoc del paquete o clase involucrada, luego el caso de uso completo y finalmente el ADR correspondiente.

Ruta sugerida:

```txt
JavaDoc del paquete → guía de caso de uso → ADR → test de guardarraíl
```

## ADR-001 — SideDock transversal y sidebar conceptual separado

**Estado:** aceptada.

**Contexto.** La aplicación necesitaba paneles laterales para muchos tipos de proyecto: estructura, propiedades, validación, ayuda y módulos específicos. El modelo conceptual ya tenía un sidebar legacy estable y protegido.

**Alternativas consideradas.**

```txt
A) Reutilizar el sidebar conceptual para todos los tipos.
B) Reescribir el modelo conceptual para que use SideDock.
C) Crear SideDock transversal para proyectos especializados y mantener el sidebar conceptual aislado.
```

**Decisión.** Se eligió la alternativa C: `presentation/sidebar` queda como zona conceptual legacy y `presentation/sidedock` como carcasa transversal de proyectos especializados.

**Consecuencias.**

```txt
- El modelo conceptual no se contamina.
- Los nuevos workspaces comparten módulos laterales.
- La ayuda operativa vive cerca del tipo activo.
- La frontera debe protegerse con documentación y tests.
```

**Para estudiar.** Revisar `WorkbenchSideDock`, `DiagramWorkbenchView`, `StructuredWorkbenchView` y la guía de onboarding de arquitectura.

## ADR-002 — Grafo lógico con dominio propio, no como FreeGraphDocument

**Estado:** aceptada.

**Contexto.** El Grafo lógico del negocio visualmente parece un grafo, pero semánticamente representa macroflujos, flujos, casos de uso, acciones, reglas, condiciones, entidades, estados, reportes, riesgos y preguntas pendientes.

**Alternativas consideradas.**

```txt
A) Reutilizar FreeGraphDocument con etiquetas libres.
B) Usar DiagramModel genérico.
C) Crear LogicalBusinessGraphDocument con nodos y relaciones semánticas propias.
```

**Decisión.** Se eligió la alternativa C: dominio propio en `domain/logicalbusinessgraph`.

**Consecuencias.**

```txt
- El grafo puede validar reglas de negocio.
- El Markdown oficial tiene gramática semántica.
- La UI reutiliza canvas, pero no reutiliza el documento de grafo libre.
- La trazabilidad con levantamiento lógico es más fuerte.
```

**Para estudiar.** Comparar `LogicalBusinessGraphDocument`, `LogicalBusinessGraphNodeKind`, `LogicalBusinessGraphRelationKind` y `FreeGraphDocument`.

## ADR-003 — Ayuda académica separada de ayuda operativa

**Estado:** aceptada.

**Contexto.** La aplicación tiene teoría de diagramas y también instrucciones de uso. Mezclarlas confundía el propósito del menú Ayuda y del botón Ayuda del SideDock.

**Alternativas consideradas.**

```txt
A) Una sola ayuda con todo mezclado.
B) Menú Ayuda solo para operación.
C) Menú Ayuda para teoría académica y SideDock Ayuda para operación.
```

**Decisión.** Se eligió la alternativa C.

**Consecuencias.**

```txt
- El menú Ayuda explica teoría, notación, cuándo usar y errores comunes.
- El SideDock explica capacidades concretas de la herramienta activa.
- El usuario puede estudiar conceptos sin depender de botones.
- La ayuda operativa queda contextual al proyecto activo.
```

**Para estudiar.** Revisar `DefaultTheoryCatalog`, `ManualContent`, `OperationalHelpCatalog` y `StandardSideDockModules`.

## ADR-004 — Canvas transversal mediante adaptadores

**Estado:** aceptada.

**Contexto.** Varios tipos visuales necesitaban selección, zoom, paneo, arrastre, etiquetas, puntos intermedios y exportación PNG. Repetir esa lógica en cada tipo era caro y riesgoso.

**Alternativas consideradas.**

```txt
A) Un canvas diferente por tipo.
B) Un único modelo visual genérico para todos.
C) Canvas transversal con adaptadores por tipo.
```

**Decisión.** Se eligió la alternativa C: `InteractiveCanvasSurfaceView` trabaja con `InteractiveCanvasAdapter` y render kits específicos.

**Consecuencias.**

```txt
- Se comparte interacción sin perder semántica del dominio.
- Cada tipo decide cómo traducir su documento al canvas.
- El modelo conceptual protegido no se obliga a migrar.
- Bugs transversales, como puntos intermedios, pueden corregirse una vez.
```

**Para estudiar.** Revisar `InteractiveCanvasAdapter`, `InteractiveCanvasSurfaceView`, `CanvasBendPointController` y los adapters de Grafo lógico/Grafo libre/UML.

## ADR-005 — Markdown oficial y .dms como formatos con responsabilidades distintas

**Estado:** aceptada.

**Contexto.** El sistema necesita importar documentos creados por humanos o IA y también guardar proyectos editables con layout y metadatos.

**Alternativas consideradas.**

```txt
A) Usar solo Markdown como formato de proyecto.
B) Usar solo .dms y no permitir Markdown importable.
C) Usar Markdown como contrato humano/IA y .dms como persistencia editable completa.
```

**Decisión.** Se eligió la alternativa C.

**Consecuencias.**

```txt
- Markdown es legible, revisable e importable.
- .dms conserva estado del proyecto, layout y documentos especializados.
- La IA trabaja con plantillas y gramáticas, no con detalles internos JSON.
- Los tests de roundtrip deben cubrir ambos formatos.
```

**Para estudiar.** Revisar `DiagramMarkdownImportDispatcher`, parsers/exporters Markdown, `DmsProjectJsonReader` y `DmsProjectJsonWriter`.

## ADR-006 — Regla visual de esquinas rectas en UI nueva o corregida

**Estado:** aceptada.

**Contexto.** La interfaz evolucionó con varios estilos heredados. Para el cierre de producto se definió una regla visual: la UI nueva o corregida debe evitar bordes redondeados ornamentales.

**Alternativas consideradas.**

```txt
A) Dejar cada módulo con estilos propios.
B) Limpiar todo el sistema visual de una sola vez.
C) Aplicar regla a UI nueva/corregida, respetando zonas protegidas y formas semánticas de diagramas.
```

**Decisión.** Se eligió la alternativa C.

**Consecuencias.**

```txt
- No se toca pantalla de inicio ni modelo conceptual salvo emergencia.
- Paneles, tabs, badges y botones nuevos deben ser rectos.
- Círculos, óvalos y figuras notacionales siguen permitidos cuando son semánticos.
- La regla puede reforzarse con guardarraíles acotados.
```

**Para estudiar.** Revisar `tokens.css`, `workbench.css`, CSS de Grafo lógico y tests de cobertura de tokens.

## ADR-007 — Release Candidate con evidencia automatizada y smoke manual separado

**Estado:** aceptada.

**Contexto.** Los tests automatizados pueden confirmar compilación, catálogos, parsers, persistencia y arquitectura, pero no reemplazan la aprobación visual/manual del instalable.

**Alternativas consideradas.**

```txt
A) Declarar release solo con tests unitarios.
B) Declarar release solo con pruebas manuales.
C) Separar base automatizada verde, app-image/MSI y smoke manual de instalación.
```

**Decisión.** Se eligió la alternativa C.

**Consecuencias.**

```txt
- BUILD SUCCESS es condición necesaria.
- app-image y MSI tienen scripts propios.
- El reporte RC documenta evidencia y decisión final.
- La documentación anti-fachada evita declarar capacidades no probadas.
```

**Para estudiar.** Revisar `scripts/14-app-image-completa.bat`, `scripts/15-msi-completo.bat`, `scripts/16-release-candidate.bat` y reportes RC.

## Tabla de decisiones y zonas asociadas

| ADR | Decisión | Zona de código/documentación |
|---|---|---|
| ADR-001 | SideDock transversal separado del sidebar conceptual | `presentation/sidedock`, `presentation/sidebar` |
| ADR-002 | Grafo lógico con dominio propio | `domain/logicalbusinessgraph` |
| ADR-003 | Ayuda académica vs operativa | `application/theory`, `presentation/help`, `presentation/sidedock` |
| ADR-004 | Canvas común con adaptadores | `presentation/interactivecanvas` |
| ADR-005 | Markdown y `.dms` con roles distintos | `infrastructure/markdown`, `infrastructure/json` |
| ADR-006 | UI nueva/corregida sin radius ornamental | `src/main/resources/css` |
| ADR-007 | RC con evidencia automatizada y smoke manual | `scripts`, `docs/testeo`, `docs/release` |

## Preguntas de estudio

```txt
1. ¿Qué hubiera pasado si el Grafo lógico usaba FreeGraphDocument?
2. ¿Por qué la ayuda académica no debe explicar botones concretos?
3. ¿Qué ventaja ofrece el canvas por adaptadores frente a un canvas por tipo?
4. ¿Por qué Markdown y .dms no cumplen la misma función?
5. ¿Cuándo un borde redondeado es ornamental y cuándo una forma es semántica?
6. ¿Qué evidencia mínima debería existir antes de declarar una capacidad como real?
```
