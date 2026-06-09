# Tanda 2 — Perfiles light/detail

## Problema que resuelve

La importación desde código fuente puede producir clases con decenas o cientos de atributos y métodos. Si el canvas intenta dibujar todo desde el inicio, JavaFX puede quedar saturado al abrir el editor, incluso si el parseo ya terminó correctamente.

## Decisión

Definir perfiles explícitos de detalle visual:

- `LIGHT`: muestra hasta 5 atributos y 5 métodos.
- `MEDIUM`: muestra hasta 10 atributos y 10 métodos.
- `DETAILED`: muestra hasta 25 atributos y 25 métodos.
- `FULL`: muestra todo, pero debe quedar reservado para una acción explícita con advertencia.

## Implementación esperada

Crear:

- `UmlSourceImportRenderProfile`
- `UmlSourceImportRenderProfilePolicy`
- `UmlSourceImportRenderProfileRecommendation`

Actualizar `UmlClassMemberRenderPolicy` para aceptar un perfil y limitar atributos/métodos por sección.

## Regla clave

El perfil controla el detalle visible en el canvas. No debe borrar información del modelo. El modelo completo se conserva para panel derecho, tooltips, acciones contextuales, exportaciones controladas y futuras vistas bajo demanda.

## Validaciones esperadas

- Proyecto pequeño → perfil `DETAILED`.
- Proyecto mediano/grande → perfil `MEDIUM`.
- Proyecto enorme → perfil `LIGHT`.
- Una clase con 40 métodos en `DETAILED` muestra 25 y deja contador de ocultos.
- Una clase con 12 atributos en `LIGHT` muestra 5 y deja contador de ocultos.
