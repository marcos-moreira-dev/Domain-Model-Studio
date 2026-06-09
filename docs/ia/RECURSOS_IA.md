# Recursos IA

Estado: **documentación viva**  
Actualizado en: **RIA-1 — Auditoría editorial de Recursos IA**

Los recursos exportables de IA viven en:

```txt
src/main/resources/ai-resources/
```

Desde la aplicación, el botón de recursos IA copia una carpeta con índice, gramáticas, plantillas y ejemplos oficiales.

## Uso recomendado

1. Abrir una plantilla o ejemplo oficial del tipo de diagrama/documento necesario.
2. Pasarlo a una IA junto con la información levantada del cliente.
3. Pedir un Markdown equivalente respetando `diagram_type`, `importable` y la estructura del ejemplo.
4. Importarlo en Domain Model Studio cuando el índice exportado diga que el recurso es importable por la aplicación.
5. Revisar la salida central, corregir lo mínimo, guardar `.dms` y exportar el formato real del tipo activo.

## Regla de honestidad

Una gramática, plantilla o ejemplo para IA no equivale automáticamente a una función cerrada de la aplicación. Solo debe marcarse como importable cuando exista la cadena completa:

```txt
Markdown → importación → salida visual/documental/matriz → edición mínima → exportación → guardado/carga .dms
```

El índice generado por `ExportAiResourcesUseCase` es la fuente inmediata para saber si un archivo exportado es importable. Desde Tanda 31, ese índice también declara `Tipo de recurso`, `Uso recomendado` y `Contrato de importación`.

## Biblioteca oficial

```txt
examples/markdown/plantillas/
examples/markdown/diagramas/
src/main/resources/ai-resources/official-markdown/plantillas/
src/main/resources/ai-resources/official-markdown/diagramas/
src/main/resources/ai-resources/official-markdown/levantamiento-logico/
```

## Familias exportadas

El paquete de recursos IA incluye estos grupos:

```txt
gramáticas base por tipo, incluyendo `operational-flow`;
plantillas oficiales por tipo visible;
ejemplos mínimos por dominio sencillo;
ejemplos UENS gorditos para comparar varios diagramas del mismo sistema escolar;
plantilla y ejemplos de Levantamiento lógico como fuente lógica canónica con `sample_kind` explícito; gramática, prompt maestro, plantilla, ejemplo mínimo histórico y UENS del Grafo lógico del negocio.
```

UENS se usa como unidad educativa oficial de ejemplo. La intención es que el usuario pueda comparar modelo conceptual, diccionario, módulos, pantallas, wireframes, roles, UML, BPMN, C4, despliegue y grafo libre sobre un mismo dominio.

## Estado de importación

Importables desde Markdown según capacidades actuales del catálogo:

```txt
conceptual-model
data-dictionary
admin-module-map
roles-permissions-map
screen-flow
admin-wireframes
uml-class
c4-context
c4-containers
technical-deployment
bpmn-basic
operational-flow
uml-use-case
uml-activity
uml-sequence
uml-state
free-graph
logical-business-intake
logical-business-graph
```

Notas:

- `data-dictionary` se trata como documento técnico estructurado, no como canvas libre.
- `roles-permissions-map` se trata como matriz estructurada.
- `free-graph` se trata como grafo flexible para nodos y relaciones libres, no como reemplazo de diagramas especializados.
- `logical-business-intake` se trata como expediente lógico documental y fuente lógica canónica para uso revisable por usuario e IA.
- `logical-business-graph` se trata como vista visual compatible con el levantamiento lógico; usa nodos tipados, relaciones lógicas y leyenda obligatoria.

## Levantamiento lógico y GPT

La plantilla canónica del Levantamiento lógico debe enseñar explícitamente al menos:

```txt
ENT-XXX
ATR-XXX
REL-XXX
```

Los ejemplos oficiales deben demostrar IDs reales como:

```txt
ENT-001
ATR-001
REL-001
SUP-001
CALC-001
```

Esto evita que GPT produzca solamente narrativa sin atributos ni relaciones importables.

El uso del Levantamiento lógico como fuente produce **borradores o Markdown revisables** cuando el usuario o la IA los construyen. No sustituye revisión humana, no sincroniza proyectos ni importa automáticamente un proyecto final.

## Guardarraíles

- No inventar tipos fuera de `DefaultDiagramTypeRegistry`.
- No cambiar `diagram_type` por nombres visibles traducidos.
- No pedirle a la IA una app o pantalla final; pedirle Markdown del tipo seleccionado.
- No asumir que un PNG/SVG/PDF está cerrado sin smoke de la pestaña activa.
- Mantener sincronizados casos de uso, ejemplos UENS, recursos IA, parsers y exportadores cuando se agregue o cambie un tipo.
- No marcar como importable una plantilla que contiene placeholders capaces de crear nodos/roles/entidades falsos.


## Tipos de recurso del paquete exportado

El índice descargado clasifica los archivos así:

```txt
Gramática
Plantilla IA
Ejemplo mínimo
Ejemplo completo
Plantilla lógica maestra
Referencia
```

Esta clasificación evita que una plantilla con placeholders se confunda con un ejemplo listo, o que una gramática se use como si fuera un proyecto final.



## Cierre editorial RIA-1

La auditoría RIA-1 deja como contrato vivo que:

- la plantilla `conceptual_model.md` es importable y coincide con el parser activo de modelo conceptual;
- las plantillas públicas en `examples/markdown/plantillas/` espejan las plantillas exportables de `src/main/resources/ai-resources/official-markdown/plantillas/`;
- la plantilla pública `logical_business_intake.md` espeja la plantilla canónica exportable `logical_business_intake_template.md`;
- los recursos de Grafo lógico usan lenguaje de vista compatible sustentada en el levantamiento, no promesas de derivación automática;
- el front matter de Levantamiento lógico incluye metadatos de producto (`dms_version`, `name`, `domain`, `status`, `canonical_contract`, `importable`, `intended_output`).

## Levantamiento lógico UENS vigente

UENS se describe como fuente lógica canónica y expediente lógico canónico. No hay jerarquía automática ni generación automática; otros artefactos reutilizan IDs y nombres bajo revisión.
