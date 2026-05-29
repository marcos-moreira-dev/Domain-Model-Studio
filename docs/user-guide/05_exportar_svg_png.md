# 05 - Exportar salidas

Estado: **guía de usuario alineada con planificación viva**  
Fuente viva de reglas por formato: `docs/implementacion/08_tanda_06_exportacion_profesional_por_tipo.md`

## Formatos principales

```txt
Modelo conceptual      → SVG, PNG, Markdown
Diccionario de datos   → PDF, Markdown
Diagramas visuales     → SVG, PNG, Markdown, siempre que el tipo activo pase smoke real
Roles y permisos       → SVG, PNG, Markdown como matriz visual/estructurada
```

## Regla práctica

La app exporta la **salida activa**: el proyecto, pestaña o workspace que está seleccionado en ese momento.

Antes de considerar una exportación como válida, revisar:

```txt
- el archivo fue creado;
- abre correctamente;
- no está vacío;
- corresponde al tipo activo;
- el texto es legible;
- las conexiones principales se ven;
- no aparecen paneles laterales ni controles de interfaz;
- el nombre del archivo corresponde al proyecto actual.
```

## SVG

SVG es una **salida vectorial documental**. Sirve para documentación con zoom, edición externa ligera y entregas donde conviene conservar formas y texto como elementos escalables.

Contrato vigente:

```txt
SVG vectorial documental
no promete ser una copia WYSIWYG exacta del canvas JavaFX.
```

Esto significa que el archivo debe representar el artefacto activo con formas y textos vectoriales, pero puede usar una composición documental propia del exportador. Para revisar fidelidad visual exacta del lienzo, usar PNG y smoke visual.

Criterios mínimos:

```txt
- debe abrir en navegador o visor SVG común;
- no debe ser una imagen PNG incrustada;
- debe tener formas/textos vectoriales;
- debe representar el tipo activo.
```

El modelo conceptual es la referencia más madura. Los demás diagramas visuales deben verificarse por smoke antes de declararse cerrados.

## PNG

PNG es útil para:

```txt
Word
presentaciones
entregas rápidas
capturas para README
informes donde SVG no conviene
```

Debe salir como imagen del contenido, no como captura de la interfaz completa. No debería incluir sidebars, toolbar, pestañas ni controles de edición.

## PDF

PDF se usa para documentos formales. En el cierre actual, su uso principal es el diccionario de datos.

```txt
Diccionario de datos → PDF profesional
Diagramas visuales   → no prometer PDF general hasta tener renderer o reporte definido
```

## Markdown

Markdown sirve como salida actualizada del proyecto y como puente de trabajo con IA.

Debe conservar:

```txt
diagram_type;
título o metadatos mínimos;
elementos principales;
relaciones principales;
cambios editados;
estructura compatible con la gramática del tipo.
```


## Nota de implementación 7

La disponibilidad visible de SVG, PNG, PDF y Markdown debe salir de la salida activa real.

```txt
Tipo de proyecto + documento interno presente + acción exportable
→ formatos habilitados.
```

Si un proyecto está incompleto o no contiene el documento interno esperado, la app no debería ofrecer formatos para esa salida aunque el tipo nominal los tenga en la promesa de producto.

## Revisión después de exportar

```txt
- el archivo no debe salir vacío;
- el texto debe ser legible;
- las conexiones principales deben verse;
- el formato debe corresponder al tipo activo;
- el Markdown actualizado debe conservar diagram_type;
- SVG no debe ser raster disfrazado;
- PNG no debe salir negro ni incompleto;
- PDF debe ser legible e imprimible cuando aplique.
```

## Nota de vigencia

Si esta guía contradice documentos históricos de estado o porcentajes anteriores, manda la planificación viva:

```txt
docs/implementacion/08_tanda_06_exportacion_profesional_por_tipo.md
```


## Contrato SVG documental

SVG vectorial documental: no promete ser una copia WYSIWYG exacta del canvas interactivo.
