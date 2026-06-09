# Casos de uso — Canvas, exportación e interacción por tipo de diagrama

Estado: **control funcional añadido después de revisar capturas y código**  
Base: capturas UENS, `DiagramCanvasView`, editores especializados y `ActiveOutputResolver`.

## 1. Diferencia importante

Aquí “fachada” no se refiere al patrón de diseño `Facade`. Una facade interna puede ser útil si coordina subsistemas y reduce acoplamiento. Lo que se evita es una **utilidad visible sin comportamiento real**: botón, menú, exportación o editor que aparenta estar terminado pero no permite trabajar.

## 2. Casos de uso transversales obligatorios para diagramas

| Código | Caso de uso | Estado actual al ojo | Criterio de cierre |
|---|---|---:|---|
| CU-CANVAS-01 | Hacer zoom con scroll | 70% | Todos los diagramas visuales usan el mismo comportamiento. |
| CU-CANVAS-02 | Hacer paneo con clic derecho | 65% | Clic derecho queda reservado para paneo; sin menús contextuales conflictivos. |
| CU-CANVAS-03 | Seleccionar con clic izquierdo | 70% | Elementos seleccionables en cada tipo visual. |
| CU-CANVAS-04 | Seleccionar región rectangular | 45% | Fondo + arrastre izquierdo selecciona varios nodos donde aplique. |
| CU-CANVAS-05 | Mover recuadros/nodos | 35% | Mover nodo actualiza layout y marca cambios sin perder conexiones. |
| CU-CANVAS-06 | Mover varios seleccionados | 25% | Selección múltiple arrastrable en diagramas de cajas/nodos. |
| CU-CANVAS-07 | Recalcular conectores al mover nodos | 35% | Las líneas siguen conectadas a cajas movidas. |
| CU-CANVAS-08 | Agregar puntos intermedios | 55% | Disponible para conectores que lo necesiten. |
| CU-CANVAS-09 | Eliminar punto intermedio desde toolbar | 20% | Botón explícito de toolbar elimina vértice/punto seleccionado. |
| CU-CANVAS-10 | Eliminar punto intermedio con Suprimir | 60% | Atajo disponible y documentado. |
| CU-CANVAS-11 | Exportar PNG | 75% | Exporta el área visual activa completa. |
| CU-CANVAS-12 | Exportar SVG real | 25% | SVG vectorial por diagrama, no raster incrustado. |
| CU-CANVAS-13 | Guardar y reabrir posiciones | 35% | `.dms` conserva layout manual por tipo. |
| CU-CANVAS-14 | Cambiar color/borde de elementos | 35% | Propiedades visuales persistentes por elemento. |
| CU-CANVAS-15 | Pestañas de proyectos escrolleables | 45% | La fila de pestañas no se rompe con muchos proyectos abiertos. |

## 3. Estado por tipo de diagrama

| Tipo | ¿Debe usar canvas? | Estado visual actual al ojo | Brecha principal |
|---|---|---:|---|
| Modelo conceptual | Sí | 88% | Extraer infraestructura sin romperlo; quitar conflicto de clic derecho en puntos. |
| UML casos de uso | Sí | 70% | Buen referente visual; falta canvas común, drag persistente y SVG. |
| Mapa de módulos | Sí | 60% | Render bonito pero no interactivo como Lucidchart; falta mover recuadros y SVG. |
| Flujo de pantallas | Sí | 58% | Falta drag de pantallas, rutas de transición y SVG. |
| Wireframes administrativos | Sí, como maqueta simple | 64% | Falta edición visual más directa y export SVG de geometrías. |
| UML clases | Sí | 60% | Falta mover clases/agrupadores y SVG fiel. |
| UML actividad | Sí | 55% | Falta canvas común, conectores editables y SVG. |
| UML estados | Sí | 58% | Se ve funcional; falta edición visual persistente y SVG. |
| UML secuencia | Sí, especializado | 45% | Necesita eje temporal, participantes y mensajes; no cajas genéricas. |
| BPMN básico | Sí | 58% | Falta mover elementos/carriles, conectores editables y SVG. |
| Flujo operativo | Sí | 60% | Falta interacción persistente y SVG. |
| C4 contexto | Sí | 58% | Falta límites/agrupadores y SVG. |
| C4 contenedores | Sí | 60% | Falta drag/layout persistente y SVG. |
| Despliegue técnico | Sí | 55% | Falta símbolos técnicos, posiciones persistentes y SVG. |
| Roles/permisos | No canvas libre; matriz | 78% | Si se exporta visualmente, debe tener PNG/SVG de matriz. |
| Diccionario de datos | No canvas; documento/tabla | 72% | Mantener honestamente como documento; PDF/Markdown prioritarios. |

## 4. Prueba manual obligatoria por tipo visual

Para cada tipo marcado como canvas:

```text
1. Abrir ejemplo oficial UENS o mínimo.
2. Seleccionar un nodo/recuadro.
3. Moverlo.
4. Confirmar que conectores se reajustan.
5. Hacer selección rectangular en una zona del diagrama.
6. Hacer paneo con clic derecho.
7. Hacer zoom con scroll.
8. Editar una propiedad semántica.
9. Editar una propiedad visual si aplica.
10. Exportar PNG.
11. Exportar SVG y abrirlo en navegador.
12. Guardar .dms.
13. Cerrar/reabrir.
14. Confirmar posiciones manuales.
```

Si un tipo falla en mover, persistir o exportar SVG, queda como parcial aunque visualmente se vea bien.

## 5. Observaciones de código que justifican esta matriz

```text
presentation/canvas/DiagramCanvasView.java
```

Contiene la interacción más madura: zoom, paneo, selección rectangular, drag, puntos intermedios y exportación conceptual.

```text
presentation/modulemap/ModuleMapEditorView.java
presentation/screenflow/ScreenFlowEditorView.java
presentation/wireframe/WireframeEditorView.java
presentation/behavior/BehaviorDiagramEditorView.java
presentation/architecture/ArchitectureDiagramEditorView.java
presentation/umlclass/UmlClassDiagramEditorView.java
```

Estas vistas dibujan con JavaFX y exportan PNG por snapshot, pero todavía no comparten un contrato fuerte de canvas interactivo ni exportación SVG real por tipo.

```text
presentation/exportable/ActiveOutputResolver.java
```

Actualmente diferencia bien la salida activa, pero registra SVG real principalmente para el modelo conceptual. Debe evolucionar para que cada tipo de diagrama tenga capacidades reales y no botones decorativos.

## 6. Decisiones de implementación

```text
- Reutilizar la infraestructura del modelo conceptual todo lo posible.
- No copiar/pegar DiagramCanvasView en cada editor.
- Crear adaptadores por familia de diagrama.
- Mantener diccionario de datos como documento técnico.
- Mantener roles/permisos como matriz estructurada.
- Usar toolbar para acciones delicadas como eliminar punto intermedio.
- Reservar clic derecho para paneo.
- Usar colores sólidos, bordes claros y geometrías simples.
- Respetar teoría de cada diagrama, sin convertir todo en la misma caja genérica.
```
---

## Nota de vigencia frente a planificación viva 6

Este documento conserva valor como matriz histórica de control funcional y casos de uso. Sin embargo, la planificación viva de exportación queda ahora centralizada en:

```txt
docs/implementacion/08_tanda_06_exportacion_profesional_por_tipo.md
```

Si aquí aparecen porcentajes o frases anteriores sobre SVG/PNG/Markdown/PDF que contradicen el estado objetivo actual, no se deben borrar sin auditoría; se deben tratar como diagnóstico anterior hasta la limpieza documental de la Planificación 9.

Regla vigente:

```txt
La promesa visible de exportación debe obedecer al workspace activo y pasar smoke real por tipo.
Si un formato no pasa smoke, debe ocultarse/deshabilitarse o corregirse antes de considerarse cerrado.
```

## Nota de vigencia — Implementación 7

La Implementación 7 agrega una política explícita de formatos reales (`ProjectExportFormatPolicy`).
Los porcentajes históricos de esta matriz siguen sirviendo como diagnóstico, pero la disponibilidad visible actual debe obedecer a:

```txt
proyecto activo + documento interno presente + formato soportado por la salida activa
```

Por tanto, SVG/Markdown/PDF no deben entenderse como simples promesas por tipo nominal, sino como formatos condicionados por la salida activa real y por smoke pendiente en Windows.

