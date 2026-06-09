# Estilo visual — Tokens, paleta sobria y deuda del fondo gris/café

Estado: **diagnóstico + criterio visual**  
Prioridad: **alta**  
Base: capturas y CSS (`tokens.css`, `identity-foundation.css`, `identity-canvas.css`, `identity-inspector.css`, `module-map.css`, `behavior-diagram.css`, `architecture-diagram.css`, `roles-permissions.css`, `wireframe.css`, `data-dictionary.css`).

## 1. Dirección estética

La estética deseada es:

```text
Herramienta desktop técnica.
Sobria, clara y operativa.
Inspirada en IDEs y herramientas de modelado.
No dashboard web.
No UI excesivamente brillante.
No colores de fantasía.
```

La referencia interna debe ser el modelo conceptual / casos de uso: colores suaves, bordes sólidos, selección azul, fondo claro y componentes legibles.

## 2. Paleta base recomendada

Usar pocos tokens y repetirlos en todos los editores.

```text
App background:       #ece9d8 o #ececec, pero no ambos sin intención.
Workspace background: #cfd3d7 o gris frío equivalente.
Panel background:     #f7f7f7.
Canvas background:    #f5f7f9 / #ffffff para exportación.
Border:               #bebeb8 / #c4c4c4.
Text:                 #222222.
Muted text:           #5b5b55.
Accent:               #2b579a / #2f6db3.
Accent soft:          #dcebfa / #dce9f8.
Warning:              #b85c00 solo para advertencias.
Danger:               #b7352c solo para eliminación/errores.
Success:              #2e7d42 solo para validación correcta.
```

## 3. Deuda de tokens detectada

Hay tokens usados en algunos CSS que no pertenecen al vocabulario base de `tokens.css`.

Detectado:

```text
-dms-surface
-dms-panel
-dms-canvas
-dms-panel-bg
-app-workspace-bg
```

Algunos tokens se definen indirectamente o no aparecen en el archivo base. Esto puede provocar diferencias visuales, fallback extraño o fondo gris/café inconsistente según la cascada.

## 4. Acción recomendada sobre tokens

Definir aliases explícitos en `tokens.css` y, cuando aplique, en la familia viva `identity-*.css`:

```css
.root {
    -dms-surface: -dms-bg-panel-soft;
    -dms-panel: -dms-bg-panel;
    -dms-canvas: -dms-bg-canvas;
    -dms-panel-bg: -dms-bg-panel;
    -app-workspace-bg: -dms-bg-workspace;
}
```

Luego migrar CSS especializados a tokens oficiales.

## 5. Inconsistencia de paletas especializadas

Se observan varias mini-paletas por editor:

```text
module-map.css usa azules propios #0B2F5B, #516F8E, #6F91B6.
data-dictionary.css usa otros azules #0B3768, #3A78B8.
behavior-diagram.css usa grises/azules #1f2937, #3c6fb6.
architecture-diagram.css usa otra gama #475569, #1d4ed8.
wireframe.css usa tokens pero también colores tipo #eef2ff, #fff7ed.
```

No está mal que cada diagrama tenga acento semántico, pero sí debe haber una base común.

Criterio:

```text
80% tokens globales.
20% colores semánticos por tipo de nodo cuando aporten teoría visual.
```

## 6. Colores por tipo de diagrama

Mantener colores sólidos y suaves.

```text
Modelo conceptual: entidades/atributos/relaciones con colores actuales.
Casos de uso: actores, sistema y casos con blanco/azul suave.
BPMN: inicio/fin/actividad/decisión/carril con neutralidad y borde azul.
C4: personas/sistemas/contenedores con acento moderado, no saturado.
Wireframes: componentes administrativos con grises y azul suave.
Roles/permisos: matriz blanca/gris con check azul sobrio.
Diccionario: documento blanco con chips suaves.
```

## 7. Fondo gris/café visto en capturas

Posibles causas:

```text
1. Algún alias de identidad puede cambiar `-dms-bg-app` a #ece9d8, tono beige clásico, si no queda centralizado en tokens vivos.
2. Algunos roots especializados usan tokens no definidos o aliases no centralizados.
3. La ventana puede verse más opaca cuando hay diálogos/modales o foco externo.
4. Los editores especializados usan fondos distintos entre sí.
```

Corrección recomendada:

```text
Unificar fondo de workbench principal.
Usar el mismo fondo para todos los editores visuales.
Reservar blanco para canvas/exportación.
Evitar beige/café en áreas centrales de trabajo si compite con el diagrama.
```

## 8. Estilo de nodos/recuadros

Regla visual:

```text
Rectángulos claros.
Borde gris o azul cuando seleccionado.
Sombra ligera solo si mejora lectura.
Texto principal en negrita moderada.
Texto secundario gris.
Etiquetas de conectores con fondo blanco semitransparente.
```

No usar tarjetas web grandes ni colores alardeados.

## 9. Criterio de cierre visual

Una vista pasa revisión visual si:

```text
se parece a la familia visual del modelo conceptual;
no usa fondo café/gris accidental;
usa tokens compartidos;
la selección es visible;
los nodos no parecen botones de formulario;
las relaciones no se pierden sobre el fondo;
el PNG exportado se ve limpio en fondo blanco;
el SVG conserva formas, texto, bordes y colores sólidos.
```
