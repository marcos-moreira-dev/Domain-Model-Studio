# Decisiones de producto vigentes

Estado: **vigente después de Tanda 27**

## Identidad del producto

Domain Model Studio es una herramienta de escritorio para modelar y documentar sistemas administrativos antes de programarlos. El flujo principal combina plantillas Markdown, generación/revisión asistida por IA, importación por tipo de proyecto, edición humana, validación y exportación.

## Levantamiento lógico

El tipo **Levantamiento lógico** es un proyecto documental estructurado. Su función es actuar como **fuente lógica canónica** del negocio, no como generador automático ni sincronizador entre proyectos.

Decisiones vigentes:

- ID oficial: `logical-business-intake`.
- Contrato Markdown: `logical-business-master-v1`.
- Ejemplo oficial automático: UENS.
- Entidades, atributos y relaciones son candidatos lógicos.
- Trazas internas son relaciones dentro del mismo expediente.
- La alineación con otros artefactos depende de IDs/nombres canónicos y revisión humana/IA.

## Catálogo y ejemplos

Cada tipo visible debe tener capacidades reales trazables. Si un tipo promete importación, exportación, validación o ayuda, debe existir código, recurso o test que lo respalde.

## Documentación

La documentación vigente no conserva bitácoras de tandas pasadas por defecto. Se conserva Markdown solo si explica una capacidad actual, frontera, contrato, guía, matriz, smoke, release o decisión activa.


## Decisión vigente sobre SVG

SVG vectorial documental: salida documental revisable, no WYSIWYG universal.


## Levantamiento lógico vigente

El Levantamiento lógico se conserva como fuente lógica canónica y expediente documental. La continuidad de refactor vigente ya limpió Derivaciones como módulo visible y deja solo borradores compatibles internos cuando aportan compatibilidad.
