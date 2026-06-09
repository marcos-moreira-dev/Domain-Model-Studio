# Estado actual — documentación viva

Estado: **vigente después de Tanda 38A**  
Propósito: ofrecer una lectura corta del estado real del repositorio sin arrastrar bitácoras de tandas pasadas.

## Resumen del producto

Domain Model Studio es una aplicación JavaFX para crear y revisar artefactos de análisis de sistemas administrativos: modelos conceptuales, diccionarios, mapas de módulos, roles/permisos, flujos de pantalla, wireframes, UML, arquitectura C4/despliegue, procesos y Levantamiento lógico.

El flujo central vigente es:

```text
Plantilla o ejemplo Markdown
→ generación/revisión asistida por IA si aplica
→ importación por tipo de proyecto
→ edición humana en workspace especializado
→ validación, guardado .dms y exportación
```

## Estado técnico confirmado

- Java 21 con Maven.
- Scripts públicos reducidos a entry points vigentes.
- Levantamiento lógico alineado como fuente lógica canónica, no generador automático.
- Ejemplo oficial UENS actualizado y sincronizado con el espejo público.
- SideDock lógico sin Artefactos compatibles como módulo principal.
- Tests locales reportados verdes por el usuario después de P0, RIA-1 y RIA-1A.
- Refactor aplicado hasta Tanda 37: `ApplicationServices`, shell/comandos, catálogos oficiales, recursos IA, persistencia `.dms`, parsers Markdown, soporte común de cambios de proyecto para ViewModels visuales/documentales y selección segura de Resumen UML ya tienen coordinadores/utilidades comunes más pequeños.

## Documentación viva

La lectura vigente debe empezar por:

1. `README.md`
2. `docs/README.md`
3. `docs/documentacion/MAPA_DOCUMENTACION_VIVA.md`
4. `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md`
5. `docs/desarrollo/refactor/PLAN_REFACTOR_SOLID.md`
6. `docs/desarrollo/refactor/MAPA_SEGURO_REFACTOR.md`
7. `scripts/README.md`

## Política documental

La bitácora histórica ya no se conserva por acumulación. Si un Markdown solo registraba una tanda pasada, una hipótesis abandonada o un plan sustituido, se elimina salvo que tenga valor vigente de auditoría, compatibilidad o diagnóstico.

Este archivo reemplaza el antiguo `ESTADO_ACTUAL.md` legacy. Para trazabilidad de auditoría puntual, usar `docs/diagnostico/ESTADO_AUDITORIA_ACTUAL.md`.


## Refactor de persistencia `.dms`

La Tanda 31 dejó los readers/writers principales como coordinadores y separó modelo conceptual legacy/payloads especializados sin cambiar formato.


## Refactor Markdown

La Tanda 32 centraliza la separación de frontmatter y cuerpo con `MarkdownImportDocument`, sin cambiar gramáticas ni ejemplos oficiales.


## ViewModels visuales/documentales

La Tanda 33 agrega `ProjectChangeSupport` para centralizar listener de cambios, bloqueo durante carga y notificación del proyecto actualizado en ViewModels migrados. No se toca el canvas conceptual legacy ni el inspector.


## Refactor UML Clases

La Tanda 34 extrae `SourceCodeUmlSummarySelectionPolicy` para aislar la selección segura de la vista Resumen en importaciones código→UML. No cambia parsing Java/TypeScript, layout visual ni UX visible.


## Tanda 35

El modelo conceptual legacy recibió un refactor seguro: `DiagramCanvasViewModel` delega historial de edición y cálculo de anclas en clases pequeñas. No se cambió UX, Markdown, `.dms`, renderers ni dominio ER.


## Artefactos compatibles legacy

La Tanda 36 retira el foco de derivaciones de la UI del Levantamiento lógico: no hay panel, CSS ni selección `DERIVATION`. La infraestructura interna permanece como borradores compatibles revisables (`compatibleDrafts`) para mantener compatibilidad técnica sin prometer generación automática.


## CSS y recursos UI

La Tanda 37 elimina placeholders CSS heredados (`identity-polish.css` y `welcome-start.css`), actualiza el README de CSS para listar solo hojas vivas y agrega guardarraíles para que los iconos declarados por la toolbar existan como PNG internos.


## JavaDoc post-refactor

La Tanda 38A revisa JavaDoc de las fronteras tocadas por el refactor, actualiza guías para usar solo `scripts\31-generar-javadoc.bat` y depura lenguaje técnico obsoleto en canonización y recursos IA.


## RIA-1 Recursos IA

La auditoría editorial de Recursos IA alinea la plantilla conceptual como importable, sincroniza plantillas públicas con recursos IA, completa front matter de Levantamiento lógico y retira lenguaje viejo de Grafo lógico que podía sugerir automatización no garantizada.


## DOC-1 Documentación de release

La documentación visible de ejecución, validación y empaquetado queda alineada con la superficie pública vigente: `00`, `01`, `02`, `13`, `14`, `15`, `16` y `31`. El README raíz, guías técnicas de empaquetado, release notes y scripts de RC apuntan al reporte neutral del instalable Windows RC.


## PKG-1 Empaquetado auditable

El flujo app-image/MSI/RC valida JDK 21 + jpackage antes de empaquetar, guarda `maven-package.log`, `jpackage-app-image.log` y `jpackage-msi.log`, agrega tamaño/hash SHA-256 a los manifiestos y copia logs a `dist\release\logs` durante el cierre RC. El punto de entrada instalable usa `DomainModelStudioLauncher` para evitar el fallo JavaFX de clases principales que extienden directamente `Application` en empaquetados classpath.


## MDR-1 Importación Markdown recursiva

Abrir carpeta Markdown recorre subcarpetas por defecto, soporta paquetes de rubro organizados por categorías y muestra un reporte detallado/copiable de archivos Markdown no catalogados o rechazados para corregirlos con IA contra la gramática oficial. Se conserva una política plana heredada para pruebas específicas.

## VIS-COPY-000 Selección y transferencia visual

Queda documentado el contrato de la siguiente mejora: copiar o transferir selección visual entre proyectos compatibles debe conservar nodos, relaciones, propiedades y bendpoints. No se mezcla con MDR-1 porque requiere estrategias de copiado por dominio/familia para no perder semántica.

## Nota de compatibilidad documental

Estado vigente después de Tanda 27: documentación viva, no se conserva por acumulación. La auditoría extensa queda en `ESTADO_AUDITORIA_ACTUAL.md` cuando se necesite trazabilidad técnica.
