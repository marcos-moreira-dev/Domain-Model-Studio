# Contrato de importación Markdown

Estado: **documentación viva**  
Actualizado en: **Tanda MDR-001 — Importación Markdown recursiva y reporte de no catalogados**

## 1. Objetivo

Este documento define cómo debe comportarse la importación Markdown de Domain Model Studio.

La intención es evitar una importación mágica e impredecible: cada archivo debe declarar qué tipo de proyecto quiere generar y debe ser procesado por el importador real de ese tipo.

## 2. Entrada

Entrada principal:

```txt
archivo .md
```

Codificación esperada:

```txt
UTF-8
```

## 3. Front matter esperado

Todo Markdown nuevo debe declarar:

```yaml
---
diagram_type: "conceptual-model"
title: "Nombre visible"
version: "1.0"
importable: true
---
```

`diagram_type` debe coincidir con un ID oficial del catálogo.

## 4. Comportamiento general

### 4.1 Archivo individual

```txt
1. La aplicación lee diagram_type.
2. Verifica que el tipo exista en el catálogo.
3. Verifica que el tipo tenga importación real activa.
4. Delega al importador correspondiente.
5. Crea un proyecto editable con salida visual, documental o matricial.
6. Permite guardado .dms y exportación aplicable según el tipo.
```

Si el tipo no tiene importación directa, debe mostrar un mensaje humano. No debe tratarlo como modelo conceptual por accidente.

### 4.2 Carpeta Markdown

Al abrir una carpeta Markdown desde la aplicación, la lectura debe ser **recursiva por defecto**.

Esto permite paquetes reales organizados por rubro o categoría personal, por ejemplo:

```txt
ferreteria-ec/
  01-comercial/
  02-inventario/
  03-facturacion-sri/
  04-operaciones/
```

La importación por carpeta debe:

```txt
1. Recorrer subcarpetas.
2. Abrir en pestañas solo documentos Markdown catalogados como proyectos DMS importables.
3. Omitir README, prompts, gramáticas, plantillas no rellenas y archivos no Markdown.
4. Rechazar sin bloquear el lote los Markdown con tipo desconocido o gramática inválida.
5. Mostrar un reporte copiable de archivos Markdown no catalogados para corregirlos con IA.
```

## 5. Tipos con importación activa

La lista viva de importables Markdown es:

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
```

Notas de alcance:

- `data-dictionary` importa Markdown estructurado y abre un documento técnico, no un canvas.
- `roles-permissions-map` importa una matriz rol/permiso, no un grafo libre.
- `logical-business-intake` importa un expediente documental lógico, no un canvas visual.
- `free-graph` importa nodos y relaciones libres, no reemplaza diagramas especializados.

## 6. Plantillas, ejemplos y documentos exportados

No todo Markdown del repositorio tiene la misma función.

| Tipo de archivo | Uso | Puede tener `importable: true` |
|---|---|---:|
| Plantilla IA | Guía para que GPT produzca un documento del cliente | Solo si no contiene placeholders estructurales que creen contenido falso |
| Ejemplo oficial mínimo/gordito | Documento demostrativo importable | Sí, si pasa por parser real |
| Markdown exportado por la app | Salida actualizada/revisable | Sí, si round-trip está cubierto |
| Derivación desde Levantamiento lógico | Borrador revisable | Sí solo si el parser destino lo acepta |

## 7. Compatibilidad heredada del modelo conceptual

Para no romper ejemplos previos, un Markdown conceptual heredado puede importarse si contiene estructura clara de entidades y relaciones aunque no declare `diagram_type`.

Esta tolerancia **no** aplica a roles, módulos, wireframes, C4, UML, comportamiento, diccionario, grafo libre ni levantamiento lógico. Esos tipos deben declarar `diagram_type`.

## 8. Reglas del modelo conceptual

### 8.1 Secciones

El modelo conceptual reconoce, entre otras:

```txt
# Entidades
# Relaciones
# Reglas de negocio
# Dudas pendientes
```

`# Entidades` y `# Relaciones` son obligatorias en el formato conceptual.

### 8.2 Entidades

Dentro de `# Entidades`, cada `##` inicia una entidad. Los bullets `-` se consideran atributos cuando el formato no usa una tabla más específica.

### 8.3 Relaciones

Dentro de `# Relaciones`, cada `##` inicia una relación.

Campos esperados:

```txt
from
to
from_cardinality
to_cardinality
```

## 9. Tipos documentales especiales

### 9.1 Diccionario de datos

`data-dictionary` usa Markdown estructurado para entidades y campos. El importador debe ignorar secciones documentales como resúmenes o tablas generales cuando no sean entidades reales.

### 9.2 Levantamiento lógico

`logical-business-intake` usa un contrato canónico propio. Debe reconocer, según el caso:

```txt
RN, SUP, PRE, INV, POST, ACC, CU, ACT, EST, CON, ENT, ATR, REL, REP, CALC, RISK, PEND, EVID
```

El levantamiento lógico es fuente lógica canónica documental; las derivaciones son vistas o borradores revisables.

## 10. Tolerancia permitida

La importación puede tolerar:

```txt
líneas en blanco;
comentarios HTML;
acentos en nombres visibles;
metadatos opcionales desconocidos como advertencia;
orden variable de metadatos.
```

## 11. Tolerancia no permitida

La importación no debe tolerar silenciosamente:

```txt
diagram_type no registrado;
relaciones con entidades inexistentes;
cardinalidades inválidas en modelo conceptual;
front matter incompleto en tipos nuevos;
IDs duplicados cuando el formato exige unicidad;
secciones documentales reimportadas como entidades/roles/nodos falsos.
```

## 12. Reporte de carpeta Markdown

Después de abrir una carpeta, la interfaz debe mostrar un resumen y un detalle copiables con:

```txt
- proyectos importados;
- archivos Markdown omitidos por no ser documentos de proyecto;
- archivos Markdown rechazados por gramática, tipo no registrado o tipo no soportado;
- archivos no Markdown omitidos;
- ruta relativa de cada archivo frente a la carpeta raíz;
- mensaje de error o advertencia cuando exista.
```

El objetivo del reporte es que el usuario pueda copiar la lista de errores y pedir a la IA que corrija los Markdown contra la gramática oficial.

## 13. Ruta Markdown fuente

Cuando el proyecto nace desde un Markdown importado, el `.dms` debe conservar la ruta fuente en metadatos para facilitar exportación por lote y trazabilidad.

## 14. Regla de producto

Markdown importable significa que existe la cadena completa:

```txt
Markdown → importación → salida visual/documental/matriz → edición mínima → exportación → guardado/carga .dms
```

Una plantilla para IA no debe marcarse como importable si todavía no cumple esa cadena o si contiene placeholders que puedan crear contenido falso.
