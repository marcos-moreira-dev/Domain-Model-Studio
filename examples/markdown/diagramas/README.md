# Diagramas oficiales Markdown

Esta carpeta contiene ejemplos por tipo visible. Hay dos familias principales:

```txt
*_minimo.md       → ejemplo pequeño para aprender la gramática.
*_uens_gordito.md → ejemplo escolar UENS para comparar varios diagramas del mismo sistema.
```

## Uso recomendado

1. Elegir un ejemplo del tipo de diagrama que se quiere producir.
2. Pasarlo a una IA junto con la información levantada del cliente.
3. Pedir un Markdown equivalente que conserve `diagram_type` e `importable`.
4. Importarlo en Domain Model Studio.
5. Revisar visual/documentalmente y exportar desde la pestaña activa.

## Regla

No mezclar teorías: un `screen-flow` no debe convertirse en wireframe, un `data-dictionary` no debe tratarse como canvas, un `uml-sequence` debe conservar participantes/mensajes temporales y un `free-graph` debe reservarse para relaciones libres sin semántica especializada.
