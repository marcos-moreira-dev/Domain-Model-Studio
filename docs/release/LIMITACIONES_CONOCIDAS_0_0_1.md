# Limitaciones conocidas — Domain Model Studio 0.0.1 RC

## Exportaciones

- SVG es vectorial documental. No promete ser WYSIWYG; no WYSIWYG significa que prioriza una salida documental legible sobre copiar cada detalle del canvas JavaFX.
- PNG es salida visual rápida; diagramas muy grandes pueden requerir SVG o vistas filtradas.
- PDF formal aplica a salidas documentales con exportador registrado: diccionario de datos y levantamiento lógico.

## Diagramas

- BPMN básico no cubre BPMN industrial completo.
- UML se mantiene en alcance documental/académico básico por tipo.
- C4 y despliegue son vistas simplificadas de documentación arquitectónica.
- Wireframes administrativos no reemplazan Figma ni prototipos interactivos.

## Levantamiento lógico

- El Levantamiento lógico es una fuente lógica canónica/revisable, no un generador automático total de otros proyectos.
- Los borradores compatibles internos deben revisarse humanamente antes de convertirse en artefactos finales.
- La revisión humana sigue siendo obligatoria para validar reglas reales del negocio.

## Grafo lógico del negocio

- Es una vista semántica compatible/revisable del negocio; no reemplaza el expediente lógico.
- No sustituye BPMN, UML, modelo conceptual ni diccionario de datos.
- La validación integral detecta inconsistencias lógicas y estructurales, pero la revisión humana sigue siendo obligatoria.
- Las relaciones densas pueden requerir reorganización visual manual para una lectura cómoda.

## Empaquetado

- MSI depende de que `jpackage` y las herramientas MSI del entorno Windows estén disponibles.
- La validación final debe realizarse en Windows mediante app-image y MSI reales.
- Los reportes manuales siguen siendo parte del criterio de aprobación.

## Base técnica

- El modelo conceptual conserva canvas propio y no debe ser migrado por refactor transversal.
- Algunas mejoras de interacción avanzada del canvas común quedan como evolución futura, no bloqueo de RC.
