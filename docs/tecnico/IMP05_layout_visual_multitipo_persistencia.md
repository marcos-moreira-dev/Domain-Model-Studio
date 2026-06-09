# IMP-05 — Layout visual multitipo y persistencia

## Decisión técnica

Se agregó una capa de aplicación para layout visual especializado en lugar de duplicar coordenadas dentro de cada documento de dominio.

La decisión mantiene separado:

```text
Documento semántico = qué existe y qué significa
DiagramLayouts = dónde se ve y cómo se enruta visualmente
```

## Clases nuevas

```text
application/visual/VisualElementLayoutIds.java
application/visual/VisualNodeReference.java
application/visual/VisualConnectorReference.java
application/visual/VisualLayoutSpecification.java
application/visual/VisualLayoutSpecificationFactory.java
application/visual/DefaultVisualLayoutGenerator.java
application/visual/VisualLayoutService.java
```

## Flujo interno

```text
DiagramProject
→ VisualLayoutSpecificationFactory
→ VisualLayoutSpecification
→ VisualLayoutService.ensureVisualLayout(...)
→ DiagramLayouts.withLayout(...)
```

La reconciliación hace esto:

```text
1. conserva nodos existentes si el ID visual sigue existiendo
2. genera nodos faltantes con posición inicial sobria
3. conserva conectores existentes y sus bend points
4. actualiza endpoints de conectores si cambió la relación semántica
5. elimina del layout activo elementos que ya no existen en la especificación visual
```

## Integración inicial: ModuleMapViewModel

Se agregaron métodos de consulta y movimiento:

```text
layoutForModule(ModuleNode)
layoutForDependency(ModuleDependency)
moveModuleTo(String moduleId, double x, double y)
```

Esto permite que la vista deje de depender de constantes rígidas como `ROOT_X`, `ROOT_Y`, `COLUMN_GAP` como fuente única de verdad.

## Nota de compatibilidad

El formato actual solo conoce notaciones `CHEN` y `CROWS_FOOT`. Para no romper compatibilidad, los diagramas especializados usan temporalmente `project.layouts().activeLayout()` como layout visual activo.

No se agregó una nueva enum de notación ni se cambió el JSON.

## Riesgos controlados

```text
Riesgo: mezclar layout conceptual con layout especializado.
Control: IDs visuales con prefijo por familia.

Riesgo: contaminar modelos semánticos con x/y.
Control: no se modificaron ModuleNode, ScreenNode, BehaviorNode, ArchitectureNode, etc.

Riesgo: re-layout automático destructivo.
Control: VisualLayoutService conserva posiciones existentes al asegurar layout.
```

## Siguiente paso natural

IMP-06 debe conectar esta base con exportación real PNG/SVG/Markdown/PDF sin fachada.
IMP-07 debe migrar mapa de módulos al canvas común completo usando esta base de layout.
