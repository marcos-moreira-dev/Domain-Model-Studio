# 02 - Importar Markdown

Estado: **guía viva de usuario**  
Actualizado en: **Tanda 30 — Limpieza documental viva**

## Qué es el Markdown de entrada

El Markdown describe el contenido del tipo de salida que quieres generar:

```txt
modelo conceptual
diccionario de datos
levantamiento lógico
mapa de módulos
roles y permisos
flujo de pantallas
wireframes
UML
C4
despliegue técnico
BPMN / comportamiento
grafo libre
```

Todo Markdown nuevo debe declarar el tipo en el encabezado:

```yaml
---
diagram_type: "admin-module-map"
importable: true
---
```

El valor debe coincidir con un ID oficial del catálogo.

## Qué debe pasar al importar

Al importar un Markdown válido, la aplicación debe:

1. leer `diagram_type`;
2. reconocer el tipo de salida;
3. usar el importador correspondiente;
4. crear el proyecto editable;
5. mostrar la salida central;
6. permitir corrección manual mínima;
7. permitir guardado `.dms` y exportación aplicable.

## Tipos con importación activa

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

## Casos especiales

### Diccionario de datos

`data-dictionary` sí importa Markdown estructurado cuando el documento respeta la plantilla oficial. Abre un documento técnico editable y exportable en Markdown/PDF; no abre un canvas.

### Levantamiento lógico

`logical-business-intake` importa un expediente documental. No abre canvas, no exporta PNG/SVG/PDF y no reemplaza la revisión humana. Su salida principal es Markdown revisable y `.dms` editable.

### Roles y permisos

`roles-permissions-map` abre una matriz rol/permiso. No es un grafo libre ni un canvas de nodos.

### SVG

Cuando el tipo visual exporta SVG, el contrato es **SVG vectorial documental**, no una copia WYSIWYG universal del canvas JavaFX.

## Qué revisar después de importar

```txt
- que el tipo sea el correcto;
- que la salida central no esté vacía;
- que los nombres visibles coincidan con el cliente;
- que relaciones, permisos, pantallas o transiciones tengan sentido;
- que las secciones documentales no se hayan convertido en entidades/roles/nodos falsos;
- que el Markdown exportado pueda servir como versión actualizada del proyecto.
```

## Regla de trabajo

Si el Markdown no representa correctamente el caso del cliente, corrige el Markdown o la plantilla oficial antes de forzar una edición manual extensa.
