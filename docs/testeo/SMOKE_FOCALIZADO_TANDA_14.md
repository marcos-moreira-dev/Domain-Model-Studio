# Smoke focalizado — Tanda 14

Estado: **manual focalizado posterior a Tandas 10–13**  
Tipo: **validación humana con evidencia**  
Alcance: **SideDock, workspace activo, FreeGraph, UML Clases, toolbar/exportación y protección del modelo conceptual**

## Objetivo

Validar en UI real que las correcciones y guardarraíles recientes no introdujeron regresiones en los flujos sensibles:

```txt
modelo conceptual congelado
workspaces especializados con SideDock
cambio entre pestañas heterogéneas
fit/center/exportación sobre proyecto activo
FreeGraph cubierto como diagrama visual activo
UML Clases con filtros/búsqueda/selección consistente
Technical Deployment sin promesa de autoorganizar
Logical Business sin promesas de canvas visual
```

Este smoke no reemplaza `mvn test`, `smoke-render` ni la validación de release candidate. Sirve para confirmar comportamientos que los tests fuente no pueden verificar visualmente.

## Precondiciones

Ejecutar desde la raíz del proyecto:

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\12-smoke-render-automatico.bat
scripts\01-ejecutar-app.bat
```

Completar el reporte en:

```txt
docs\testeo\reportes\REPORTE_SMOKE_FOCALIZADO_TANDA_14.md
```

## Ejemplos foco

Usar estos ejemplos incluidos en el repositorio:

```txt
examples\markdown\diagramas\conceptual_model_colegio_minimo_importable.md
examples\markdown\diagramas\admin_module_map_restaurante_minimo.md
examples\markdown\diagramas\free_graph_minimo.md
examples\markdown\diagramas\free_graph_uens_gordito.md
examples\markdown\diagramas\uml_class_restaurante_minimo.md
examples\markdown\diagramas\uml_class_uens_gordito.md
examples\markdown\diagramas\roles_permissions_optica_minimo.md
examples\markdown\diagramas\data_dictionary_colegio_minimo.md
examples\markdown\diagramas\logical_business_intake.md
examples\markdown\diagramas\technical_deployment_piloto_minimo.md
```

Si `logical_business_intake.md` no existe en `examples\markdown\diagramas`, usar la plantilla oficial:

```txt
examples\markdown\plantillas\logical_business_intake.md
```

o el recurso oficial:

```txt
src\main\resources\ai-resources\official-markdown\levantamiento-logico\logical_business_intake_uens_gordito.md
```

## T14-SMOKE-001 — Línea base y arranque

- [ ] `scripts\02-ejecutar-tests.bat` termina en `BUILD SUCCESS`.
- [ ] `scripts\12-smoke-render-automatico.bat` genera `target\smoke-render\contact_sheet.html`.
- [ ] La aplicación abre con `scripts\01-ejecutar-app.bat`.
- [ ] La pantalla inicial no muestra excepciones ni textos internos de arquitectura.
- [ ] El usuario puede abrir/importar ejemplos desde la UI.

## T14-SMOKE-002 — Modelo conceptual protegido

Importar:

```txt
examples\markdown\diagramas\conceptual_model_colegio_minimo_importable.md
```

Validar:

- [ ] Abre como modelo conceptual, no como workspace especializado.
- [ ] Mantiene su canvas y paneles conceptuales clásicos.
- [ ] No adopta el SideDock especializado de los workbenches nuevos.
- [ ] Selección, árbol/estructura conceptual e inspector siguen funcionando.
- [ ] Guardar y reabrir `.dms` conserva el contenido.
- [ ] Ninguna acción de SideDock nuevo afecta el flujo conceptual.

## T14-SMOKE-003 — SideDock por tipo activo

Abrir en pestañas separadas:

```txt
admin_module_map_restaurante_minimo.md
uml_class_restaurante_minimo.md
free_graph_minimo.md
data_dictionary_colegio_minimo.md
roles_permissions_optica_minimo.md
logical_business_intake.md o equivalente
```

Validar al cambiar de pestaña:

- [ ] Mapa de módulos muestra SideDock especializado con estructura/propiedades/apariencia/ayuda si aplica.
- [ ] UML Clases muestra estructura, propiedades, apariencia y ayuda operativa.
- [ ] FreeGraph muestra SideDock como diagrama visual activo.
- [ ] Diccionario usa panel lateral estructurado/documental, no canvas libre.
- [ ] Roles y permisos se comporta como matriz/documento, no como grafo visual.
- [ ] Levantamiento lógico muestra módulos documentales propios: estructura, propiedades, validación, trazabilidad, derivaciones y ayuda.
- [ ] Al volver a otra pestaña, el SideDock no conserva contenido ni título del proyecto anterior.

## T14-SMOKE-004 — Cambio entre pestañas y proyecto activo

Con al menos cuatro pestañas abiertas: conceptual, UML, FreeGraph y Logical Business.

Validar:

- [ ] La toolbar contextual cambia según el proyecto activo.
- [ ] El SideDock cambia según el proyecto activo.
- [ ] Los formatos de exportación visibles cambian según el proyecto activo.
- [ ] El indicador de cambios pendientes pertenece a la pestaña editada.
- [ ] `Ajustar al contenido` actúa sobre la pestaña activa, no sobre una pestaña anterior.
- [ ] `Centrar diagrama` actúa sobre la pestaña activa, no sobre una pestaña anterior.
- [ ] Al volver al Home, no queda SideDock, toolbar ni exportación residual de un proyecto previo.

## T14-SMOKE-005 — FreeGraph como diagrama visual activo

Usar:

```txt
examples\markdown\diagramas\free_graph_minimo.md
examples\markdown\diagramas\free_graph_uens_gordito.md
```

Validar:

- [ ] Abre en `DiagramWorkbenchView`, no en canvas conceptual.
- [ ] El SideDock aparece y responde.
- [ ] Seleccionar nodo cambia propiedades/estado visible.
- [ ] Mover un nodo actualiza el canvas sin parpadeo grave.
- [ ] Crear o conectar desde fondo sigue funcionando si la acción está disponible.
- [ ] Ajustar y centrar dejan el diagrama visible.
- [ ] Exportar SVG, PNG y Markdown produce archivos revisables.
- [ ] Al cambiar a UML y volver, FreeGraph conserva comportamiento y no recibe comandos de la otra pestaña.

## T14-SMOKE-006 — UML Clases: búsqueda, filtros y selección

Usar:

```txt
examples\markdown\diagramas\uml_class_restaurante_minimo.md
examples\markdown\diagramas\uml_class_uens_gordito.md
```

Validar:

- [ ] Seleccionar una clase resalta el elemento correcto en canvas y panel de estructura/propiedades.
- [ ] Escribir en búsqueda no congela la UI ni produce saltos visuales por cada tecla.
- [ ] Presionar `Enter` aplica la búsqueda pendiente inmediatamente.
- [ ] Si la búsqueda/filtro oculta la clase seleccionada, el canvas no conserva una selección visual obsoleta.
- [ ] Al limpiar búsqueda/filtros, el canvas vuelve a estado consistente.
- [ ] Cambiar vista interna aplica búsqueda/filtros sin dejar selección stale.
- [ ] Buscar siguiente funciona con la búsqueda actual.
- [ ] Exportar PNG/SVG/Markdown después de filtrar no genera salida vacía accidental.

## T14-SMOKE-007 — Toolbar, acciones y promesas reales

Validar con varios tipos activos:

- [ ] Un diagrama visual muestra `Ajustar`, `Centrar`, `SVG`, `PNG` y `Markdown` cuando tiene canvas visual real.
- [ ] Diccionario de datos no muestra navegación de canvas.
- [ ] Logical Business no muestra `SVG`, `PNG`, `Ajustar` ni `Centrar`.
- [ ] Logical Business sí muestra exportación Markdown y acciones laterales documentales.
- [ ] Roles y permisos no muestra acciones de grafo libre.
- [ ] Wireframe no se trata como grafo genérico de conectores.
- [ ] Sequence no se trata como grafo genérico de bendpoints.

## T14-SMOKE-008 — Technical Deployment explícito

Usar:

```txt
examples\markdown\diagramas\technical_deployment_piloto_minimo.md
```

Validar:

- [ ] Abre como workspace de arquitectura/despliegue técnico.
- [ ] Muestra `Ajustar` y `Centrar` si tiene canvas visual.
- [ ] Permite exportar SVG, PNG y Markdown si están visibles.
- [ ] No muestra `Autoorganizar` en esta fase.
- [ ] Si por alguna ruta se dispara autoorganización, muestra mensaje de no disponible y no cae al canvas conceptual.

## T14-SMOKE-009 — Exportación focalizada

Crear una carpeta temporal para evidencias, por ejemplo:

```txt
target\manual-smoke\tanda14
```

Validar:

- [ ] UML Clases exporta SVG revisable.
- [ ] UML Clases exporta PNG no vacío ni recortado de forma grave.
- [ ] FreeGraph exporta SVG revisable.
- [ ] FreeGraph exporta PNG no vacío ni recortado de forma grave.
- [ ] Technical Deployment exporta SVG/PNG/Markdown si las acciones están visibles.
- [ ] Logical Business exporta Markdown y no promete SVG/PNG.
- [ ] Diccionario exporta PDF solo si la acción está visible y soportada.
- [ ] El SVG no usa `<image`/`data:image` como sustituto raster del diagrama.

## T14-SMOKE-010 — Guardado, reapertura y cierre con cambios

Validar al menos con UML Clases, FreeGraph y Logical Business:

- [ ] Editar algo marca la pestaña como modificada.
- [ ] Guardar `.dms` limpia el indicador de cambios.
- [ ] Cerrar con cambios pendientes muestra advertencia.
- [ ] Reabrir `.dms` conserva tipo, contenido y layout razonable.
- [ ] Cambiar de pestañas después de reabrir no mezcla toolbar, SideDock ni exportación activa.

## Criterio de aprobación

La Tanda 14 queda aprobada cuando:

```txt
- T14-SMOKE-001 a T14-SMOKE-010 están completados o tienen hallazgo registrado.
- No hay hallazgos Bloqueantes sin resolver.
- El modelo conceptual sigue protegido.
- Ningún workspace especializado cayó al canvas conceptual.
- FreeGraph queda validado manualmente como workspace visual activo.
- UML Clases no conserva selección visual stale al filtrar.
- Technical Deployment queda explícito sin Autoorganizar.
- Logical Business no finge canvas/exportaciones visuales.
- Las exportaciones focalizadas fueron abiertas visualmente.
```

## Criterio de no aprobación

No aprobar si ocurre cualquiera de estos casos:

```txt
- falla la suite automatizada;
- la app no abre;
- un workspace especializado cae al canvas conceptual;
- al cambiar pestaña se mantiene SideDock/toolbar/exportación de otro proyecto;
- UML conserva selección visual obsoleta después de ocultar el elemento;
- FreeGraph no abre como diagrama visual activo;
- Technical Deployment muestra Autoorganizar sin soporte real;
- Logical Business promete SVG/PNG o navegación de canvas;
- exportaciones SVG/PNG salen vacías, negras o ilegibles sin registrar hallazgo.
```
