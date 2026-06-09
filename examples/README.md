# Ejemplos oficiales de Domain Model Studio

Esta carpeta contiene material de referencia para validar el flujo:

```txt
plantilla o ejemplo oficial
→ IA genera Markdown equivalente del cliente
→ la aplicación importa cuando el tipo ya tiene soporte
→ se renderiza la salida visual/documental/matriz
→ el usuario corrige detalles mínimos
→ se guarda .dms
→ se exporta el resultado
```

## Carpetas principales

- `markdown/plantillas/`: plantillas por tipo visible del catálogo.
- `markdown/diagramas/`: ejemplos mínimos y ejemplos UENS por tipo.
- `markdown/invalidos/`: casos negativos para pruebas del importador.
- `markdown/regresion/`: ejemplos grandes para pruebas de regresión.
- `projects/`: archivos `.dms` oficiales.
- `exports/`: salidas generadas por la app, separadas por formato.

## Regla de honestidad

Un Markdown con `importable: false` sirve para IA, documentación y preparación, pero no debe presentarse como archivo cargable por la aplicación.

Los tipos visibles actuales tienen ejemplos oficiales importables cuando el catálogo declare `IMPORT_MARKDOWN`. La disponibilidad final de PNG/SVG/PDF/Markdown debe validarse con la pestaña activa y el smoke correspondiente.

## Familia UENS

Los archivos con sufijo `uens_gordito` usan el mismo dominio escolar: UENS como unidad educativa. Sirven para revisar coherencia entre diagramas distintos del mismo sistema:

```txt
modelo conceptual;
diccionario;
mapa de módulos;
flujo de pantallas;
wireframes;
roles y permisos;
BPMN y flujo operativo;
UML casos de uso, clases, actividad, secuencia y estados;
C4 contexto, C4 contenedores y despliegue técnico.
```

La matriz viva de cobertura está en:

```txt
docs/productizacion/casos-uso/09_matriz_cobertura_casos_uso_por_tipo.md
```
