# Índice de casos de uso por categoría y porcentaje de cierre

Estado: **documentación de control funcional / anti-fachada**  
Base de lectura: **inspección estática del código, documentación existente y capturas recientes**  
Fecha de corte: **2026-05-13**

> Nota importante: aquí “fachada” se usa en el sentido de **característica visible sin utilidad real para el usuario**. El patrón de diseño `Facade` sí puede ser válido dentro del código cuando sirve para coordinar subsistemas sin romper responsabilidad única. Lo que se evita es una herramienta que muestre botones, menús o pantallas que no estén respaldados por dominio, casos de uso, persistencia, exportación y prueba verificable.

---

## Documentos de esta carpeta

| Documento | Propósito |
|---|---|
| `01_controles_generales_gestion_proyectos.md` | Casos de uso transversales: shell, pestañas, proyecto, dirty state, zoom, toolbars, ayuda, ejemplos y recursos IA. |
| `02_entrada_salida_exportacion.md` | Importación Markdown, ejemplos oficiales, exportación activa, batch export, formatos y reglas anti-promesa falsa. |
| `03_datos_y_administrativos.md` | Diccionario, roles/permisos, mapa de módulos, flujo de pantallas y wireframes administrativos. |
| `04_modelado_conceptual_y_uml_estructural.md` | Modelo conceptual y UML clases, con foco en edición, layout, notación y exportación. |
| `05_comportamiento_bpmn_uml.md` | BPMN básico, flujo operativo y diagramas UML de comportamiento/interacción. |
| `06_arquitectura_c4_despliegue.md` | C4 contexto, C4 contenedores y despliegue técnico. |
| `07_resumen_porcentajes_y_brechas.md` | Matriz ejecutiva de porcentaje estimado por tipo y brechas faltantes. |
| `08_canvas_exportacion_interaccion_por_diagrama.md` | Casos de uso transversales de canvas, interacción, drag, puntos intermedios, PNG/SVG y pestañas escrolleables. |
| `09_matriz_cobertura_casos_uso_por_tipo.md` | Registro vivo que cruza cada tipo visible con su subcategoría, documento de casos de uso, ejemplo UENS, plantilla IA y recursos IA. |

---

## Criterio de porcentaje “al ojo”

Los porcentajes no salen de cobertura automática. Son estimaciones humanas basadas en estos ejes:

| Eje | Peso aproximado | Qué se revisa |
|---|---:|---|
| Dominio/documento real | 15% | Existe estructura semántica, no solo nodos JavaFX. |
| Casos de uso de aplicación | 15% | Crear, agregar, editar, eliminar, validar pasan por servicios/use cases claros. |
| Workspace/vista adecuada | 15% | El tipo abre una vista coherente con su promesa. |
| Importación Markdown / ejemplos | 10% | El tipo reconoce gramática o declara honestamente que no importa. |
| Persistencia `.dms` | 10% | Guardar/abrir conserva tipo y documento. |
| Exportación | 10% | PNG/SVG/PDF/Markdown según corresponda al tipo activo. |
| Edición visual/estructurada real | 15% | El usuario puede modificar contenido útil, no solo verlo. |
| Pruebas/smoke/trazabilidad | 10% | Hay tests o checklist concreto para corroborar. |

Un tipo puede ser **alto en funcionalidad documental** pero bajo en **interacción visual fina**. Por ejemplo, roles/permisos no necesita canvas libre; UML secuencia sí necesita un layout temporal especializado.

---

## Resumen ejecutivo por familias

| Familia | Porcentaje estimado | Estado general | Lectura rápida |
|---|---:|---|---|
| Gestión de proyectos y shell | 78% | Funcional con riesgos | Crear/abrir/guardar/cerrar y tabs existen; undo/redo y batch export no están igual de maduros para todos los tipos. |
| Entrada, ejemplos y recursos IA | 82% | Bastante sólido | Ejemplos oficiales y gramáticas existen; desde T01 el diccionario se declara importable como documento editable. |
| Exportación y entrega | 73% | Funcional, requiere smoke | Exportación activa por tipo existe; falta probar todos los formatos por tab activa en Windows. |
| Modelo conceptual | 88% | Más maduro | Es la referencia visual del proyecto. |
| Editores estructurados | 75% | Buenos si no se venden como canvas | Diccionario y roles/permisos deben seguir siendo documental/matriz. |
| Diagramas administrativos visuales | 69% | Reales pero incompletos visualmente | Mapa, flujo de pantallas y wireframes existen; falta lienzo común/interacción persistente. |
| UML y comportamiento | 59% | Funcional básico | Varios tipos usan editor genérico de comportamiento; UML secuencia es el caso más débil visualmente. |
| Arquitectura C4/despliegue | 61% | Funcional básico | Nodos y relaciones existen; falta layout C4/despliegue más fiel e interactivo. |
| Trazas internas técnica / SRP | 64% | Útil pero con deuda | Hay guardarraíles y tests, pero varias clases superan límites humanos de tamaño. |

---

## Regla de cierre por tipo

Un tipo visible solo debería marcarse como cerrado si pasa este circuito:

```text
Crear desde Nuevo
→ importar ejemplo oficial si aplica
→ editar contenido real
→ validar o recibir feedback
→ guardar .dms
→ cerrar y reabrir
→ exportar formatos ofrecidos
→ cambiar de pestaña sin contaminar toolbar/status/exportación
→ registrar smoke manual o test automático
```

Si falla uno de esos pasos, el tipo queda como **parcial**, aunque “se vea bonito”.


## Documento añadido por alineación visual

Se agrega `08_canvas_exportacion_interaccion_por_diagrama.md` para congelar la regla de que todo tipo vendido como diagrama debe poder comportarse como lienzo editable y exportar PNG/SVG real. También aclara que el clic derecho queda reservado para paneo y que eliminar puntos intermedios debe resolverse por toolbar/atajo, no por menú contextual.
- `10_checklist_smoke_visual_por_tipo.md`: checklist de smoke visual y estructurado por tipo, usando UENS como unidad educativa de ejemplo.

## Planificación viva de exportación

Los casos de uso de entrada/salida siguen siendo base funcional. Para decisiones actuales sobre formatos PNG, SVG, Markdown, PDF, salida activa y smoke por tipo, leer además:

```txt
docs/implementacion/08_tanda_06_exportacion_profesional_por_tipo.md
```

Los porcentajes históricos de esta carpeta no deben usarse como cierre definitivo si contradicen capturas, consola o smoke reciente.

## Planificación viva de teoría mínima

Además de los casos de uso, cada tipo debe respetar su piso teórico mínimo. Para decisiones actuales sobre diferencias entre modelo conceptual, diccionario, roles/permisos, wireframes, UML, BPMN, C4 y despliegue, leer:

```txt
docs/implementacion/01_contratos_transversales.md
```

Regla viva: si una tanda funcional agrega, elimina o cambia una acción visible de un tipo, debe revisarse esta carpeta de casos de uso y el canon teórico correspondiente. Los casos de uso registran qué puede hacer el usuario; la teoría mínima fija qué debe significar correctamente cada tipo.

## Registro vivo por tipo visible

La matriz `09_matriz_cobertura_casos_uso_por_tipo.md` cruza cada tipo del catálogo con su documento de casos de uso, ejemplo UENS, plantilla IA y recursos exportables. Debe revisarse cuando cambien catálogo, capacidades, importación, ejemplos o exportación.
