# IMP-14 — Checklist smoke visual por tipo

UENS se documenta como **unidad educativa** usada como dominio escolar oficial de ejemplo. La intención es que todos los diagramas comparables hablen del mismo negocio y permitan revisar si la herramienta cumple lo que promete.

## Regla anti-fachada

Un tipo marcado como visual no queda cerrado solo porque se vea bonito. Para considerarlo operativo debe abrir ejemplo oficial, permitir edición visual razonable, guardar/reabrir layout y exportar en formatos que realmente existan.

## Matriz de smoke

| Tipo | Ejemplo UENS | Abre ejemplo | Mueve nodo/recuadro | Guarda layout `.dms` | PNG | SVG vectorial | Markdown | PDF | Estado al ojo |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| Modelo conceptual | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 92% |
| Mapa de módulos | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 86% |
| Flujo de pantallas | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 84% |
| Wireframes administrativos | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 82% |
| UML Clases | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 84% |
| UML Casos de uso | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 82% |
| UML Actividad | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 80% |
| UML Estados | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 80% |
| UML Secuencia | Unidad educativa UENS | Sí | Participantes horizontalmente | Sí | Sí | Sí especializado | Sí | No aplica | 78% |
| BPMN básico | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 80% |
| Flujo operativo | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 82% |
| C4 Contexto | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 80% |
| C4 Contenedores | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 80% |
| Despliegue técnico | Unidad educativa UENS | Sí | Sí | Sí | Sí | Sí | Sí | No aplica | 78% |
| Roles y permisos | Unidad educativa UENS | Sí | No canvas libre; matriz estructurada | Guarda documento | Sí | Sí, matriz vectorial | Sí | No aplica | 82% |
| Diccionario de datos | Unidad educativa UENS | Referencia documental | No aplica | Guarda documento | No aplica | No aplica | Sí | Sí | 75% |
| Grafo lógico del negocio | Unidad educativa UENS | Sí | Sí, nodos tipados | Sí | Sí | Sí | Sí | No aplica | 80% |

## Casos mínimos manuales por familia visual

1. Abrir ejemplo oficial UENS.
2. Seleccionar un nodo o recuadro.
3. Moverlo con clic izquierdo.
4. Arrastrar selección rectangular en el fondo.
5. Hacer paneo con clic derecho.
6. Hacer zoom con scroll.
7. Exportar PNG desde el proyecto activo.
8. Exportar SVG y confirmar que no sea una imagen incrustada.
9. Guardar `.dms`, cerrar y reabrir.
10. Confirmar que la posición manual se mantuvo.

## Casos estructurados

### Roles y permisos

- Abrir ejemplo UENS.
- Seleccionar rol y permiso desde panel estructural.
- Editar asignación desde controles propios.
- Exportar Markdown.
- Exportar SVG como matriz vectorial.
- Guardar/reabrir `.dms`.

### Diccionario de datos

- Abrir recurso de referencia UENS.
- Editar entidad/campo en editor documental cuando se cree desde proyecto.
- Exportar Markdown.
- Exportar PDF.
- Guardar/reabrir `.dms`.

## Evidencia automática agregada

```text
src/test/java/com/marcosmoreira/domainmodelstudio/regression/VisualExportCapabilityCoherenceTest.java
src/test/java/com/marcosmoreira/domainmodelstudio/application/examples/OfficialExampleUensFamilyTest.java
```

La prueba de regresión valida que los ejemplos importables oficiales puedan importarse, preparar layout, mover un nodo, persistir/reabrir layout y exportar SVG vectorial sin `data:image` ni `<image>`.
