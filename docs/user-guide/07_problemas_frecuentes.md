# 07 - Problemas frecuentes

Estado: **guía viva de usuario**  
Actualizado en: **Tanda 30 — Limpieza documental viva**

## El Markdown no importa

Revisar:

```txt
diagram_type ausente o mal escrito
ID de tipo no registrado
estructura distinta a la plantilla oficial
relaciones o referencias inexistentes
cardinalidades inválidas en modelo conceptual
secciones obligatorias incompletas
front matter incompleto
```

## El tipo es diccionario de datos

El diccionario de datos **sí importa Markdown estructurado** cuando el documento respeta:

```txt
diagram_type: "data-dictionary"
```

y la plantilla oficial.

Si falla:

```txt
revisar front matter;
revisar entidades;
revisar campos;
revisar tablas;
evitar encabezados documentales escritos como si fueran entidades.
```

El diccionario no abre canvas. Abre un documento técnico editable y exportable en Markdown/PDF.

## El tipo es Levantamiento lógico

`logical-business-intake` abre un expediente documental, no un diagrama visual.

Si el importador no reconoce atributos o relaciones candidatas, revisar que el Markdown use IDs canónicos como:

```txt
ENT-001
ATR-001
REL-001
```

Las derivaciones son borradores revisables. No sustituyen validación humana.

## El diagrama se ve como telaraña

Eso puede pasar con modelos grandes.

Soluciones:

```txt
mover elementos principales
revisar si el Markdown está demasiado cargado
separar el contenido en varios diagramas
exportar PNG o SVG documental para revisar con más espacio
```

## La exportación PNG no se ve completa

Revisa que la salida visual activa contenga todo lo importante antes de exportar. Para diagramas grandes, SVG documental suele ser mejor que PNG.

## La exportación SVG no se ve idéntica al canvas

Esto puede ser normal. El contrato vigente es:

```txt
SVG vectorial documental
```

No se promete copia WYSIWYG exacta del canvas JavaFX.

## Cambié de notación conceptual y la distribución no se parece

Es normal. Chen y pata de gallo tienen estructura visual distinta y pueden requerir distribuciones separadas.

## El `.dms` abre, pero algo falta

Revisar especialmente:

```txt
tipo de proyecto
Markdown fuente
salida central
payload especializado
layout
exportación aplicable
metadatos
```
