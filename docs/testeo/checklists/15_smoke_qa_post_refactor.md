# Checklist Tanda 15 — Smoke QA post-refactor

Este checklist se usa después de las tandas 0–14. No reemplaza `smoke_ui_mvp.md`; lo enfoca en los cambios recientes: perfiles, SideDock, exportaciones, documentos, ayuda y limpieza SOLID.

## 1. Entorno y arranque

- [ ] `scripts\03-verificar-entorno.bat` confirma Java 21, Maven y Git.
- [ ] `scripts\02-ejecutar-tests.bat` termina sin errores.
- [ ] `scripts\01-ejecutar-app.bat` abre la aplicación.
- [ ] La ventana principal no muestra errores de consola al arrancar.
- [ ] No aparece jerga visible de desarrollo interno: `tanda`, `placeholder`, `mock`, `stub`, `GPT`, `ChatGPT`, `implementación pendiente`.

## 2. SideDock según tab activo

- [ ] En un diagrama visual aparecen módulos como Estructura, Propiedades, Vista y Ayuda.
- [ ] En Diccionario de datos aparecen módulos documentales: Secciones, Propiedades, Vista previa y Ayuda.
- [ ] En Roles y permisos aparecen módulos de matriz: Roles, Permisos, Matriz y Ayuda.
- [ ] Al cambiar de tab, el SideDock se actualiza desde el contexto activo.
- [ ] Si el módulo abierto no existe en el nuevo contexto, se cierra o cambia a uno válido.
- [ ] Si el módulo abierto sí existe en el nuevo contexto, puede mantenerse.
- [ ] Abrir/cerrar el SideDock no deja el canvas perdido.

## 3. Modelo conceptual como canon

- [ ] Abrir o importar modelo conceptual.
- [ ] Cambiar entre Chen y pata de gallo.
- [ ] Mover entidades.
- [ ] Seleccionar relaciones.
- [ ] Mover etiquetas de relaciones.
- [ ] Agregar, mover y eliminar puntos intermedios.
- [ ] Exportar PNG/SVG/Markdown.
- [ ] Guardar y reabrir `.dms` conservando layout.

## 4. Diagramas UML/BPMN/C4

- [ ] UML Casos de uso: actor de palitos, elipses, límite de sistema y `<<include>>`/`<<extend>>` legibles.
- [ ] UML Clases: clases con compartimentos y relaciones legibles.
- [ ] UML Actividad: inicio, final, acción, decisión y fork/join reconocibles.
- [ ] UML Estados: estados, inicio/final y transiciones legibles.
- [ ] UML Secuencia: participantes, líneas de vida, mensajes y activaciones legibles; no se comporta como grafo libre normal.
- [ ] BPMN básico: eventos, tareas, gateways y flujos como primitivas, no tarjetas con iconitos.
- [ ] C4/despliegue: cajas válidas, sobrias y legibles.

## 5. Módulos administrativos y wireframes

- [ ] Mapa de módulos abre centrado y exporta sin espacio blanco absurdo.
- [ ] Flujo de pantallas conserva transiciones legibles.
- [ ] Wireframes se comportan como maqueta visual, no como UML.
- [ ] Los componentes compuestos se seleccionan y arrastran como unidad.

## 6. Diccionario de datos

- [ ] Se presenta como documento/tablas, no como canvas.
- [ ] La vista previa tiene texto negro y contraste suficiente.
- [ ] El PDF tiene portada, introducción, resumen y tablas legibles.
- [ ] El Markdown conserva estructura formal.
- [ ] La referencia de logo opcional no rompe la exportación.

## 7. Roles y permisos

- [ ] Se presenta como matriz/documento, no como grafo libre.
- [ ] La matriz distingue permitido, condicionado, denegado y sin asignación.
- [ ] Markdown exporta resumen, roles, permisos, asignaciones, matriz y leyenda.
- [ ] PNG de matriz, si se usa, no sale blanco.
- [ ] No aparecen herramientas de conectores o nodos para este módulo.

## 8. Ayuda CHM académica

- [ ] Menú Ayuda muestra “Guía académica”.
- [ ] La ayuda abre con Contenido, Buscar e Índice.
- [ ] Los iconos tipo libro morado/página se ven correctamente.
- [ ] La búsqueda encuentra temas por título, palabra clave o contenido.
- [ ] La ayuda contextual abre el tema correspondiente cuando aplique.
- [ ] La prosa explica teoría, ejemplos, casos especiales y errores comunes.
- [ ] No documenta implementación interna, adapters, renderers ni infraestructura.

## 9. Exportaciones

- [ ] Exportar usa el tab activo, no un estado global anterior.
- [ ] PNG de canvas usa bounds de contenido y no viewport accidental.
- [ ] Las etiquetas de conectores movidas salen exportadas.
- [ ] No salen handles, hitboxes ni selección en exportación limpia.
- [ ] UML Secuencia exporta legible.
- [ ] Diccionario exporta PDF/Markdown formal.
- [ ] Roles/permisos exporta como matriz/documento.

## 10. Guardado y carga

- [ ] Guardar proyecto con diagramas visuales.
- [ ] Reabrir y confirmar posiciones.
- [ ] Reabrir y confirmar offsets de etiquetas.
- [ ] Reabrir y confirmar puntos intermedios.
- [ ] Reabrir y confirmar diccionario.
- [ ] Reabrir y confirmar roles/permisos.

## 11. Registro de hallazgos

Anotar aquí errores encontrados antes de Tanda 16:

```txt
- 
```
