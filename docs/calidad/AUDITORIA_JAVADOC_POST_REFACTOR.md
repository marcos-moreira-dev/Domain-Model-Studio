# Auditoría JavaDoc post-refactor

Estado: aplicada en Tanda 38A.

## Resultado

La revisión JavaDoc post-refactor se concentra en documentación técnica útil para mantenimiento. No intenta reabrir la línea pedagógica JD-1 a JD-9 ni convertir comentarios triviales en deuda nueva.

## Zonas revisadas

| Zona | Criterio de revisión |
|---|---|
| `application.services` | Explicar fachadas por familia y fachada de compatibilidad. |
| `application.catalog` | Explicar definiciones por familia y agregador público. |
| `application.resources` | Explicar recursos IA por familia y ausencia de hardcodeo UI. |
| `infrastructure.json` | Explicar persistencia `.dms` sin migraciones ni cambio de claves. |
| `infrastructure.markdown` | Explicar `MarkdownImportDocument` sin cambiar gramáticas ni ejemplos oficiales. |
| `presentation.workbench` | Explicar `ProjectChangeSupport` sin forzar superclase visual. |
| `presentation.shell` | Explicar coordinadores de creación/apertura sin mezclar ciclo de vida completo. |
| `presentation.canvas` | Explicar historial y anclas del canvas conceptual legacy sin migración forzada. |
| `application.umlclass` | Explicar selección segura de Resumen para proyectos grandes. |
| `application.logicalbusiness.derivation` | Explicar borradores compatibles internos sin módulo visible. |

## Script vigente

El único entry point público de JavaDoc es:

```bat
scripts\31-generar-javadoc.bat
```

La validación completa sigue siendo:

```bat
scripts\02-ejecutar-tests.bat
```

## Cierre

El JavaDoc queda suficiente para el cierre post-refactor. Nuevas mejoras deben hacerse solo cuando una frontera pública cambie o cuando un paquete nuevo necesite onboarding técnico.
