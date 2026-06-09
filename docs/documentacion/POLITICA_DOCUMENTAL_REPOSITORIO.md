# Política documental del repositorio

Estado: **vigente desde Tanda 27**

## Principio

El repositorio no debe acumular Markdown por inercia. La documentación tiene que explicar el producto actual, sus contratos, sus fronteras, sus validaciones y su forma de ejecución.

## Se conserva Markdown cuando

- explica una capacidad que existe hoy;
- define una frontera arquitectónica vigente;
- documenta un contrato técnico vigente;
- sirve como guía operativa o de usuario;
- sostiene una matriz de pruebas, smoke o release actual;
- registra una limitación conocida que todavía afecta al producto;
- conserva una decisión con impacto real en mantenimiento o compatibilidad.

## Se elimina Markdown cuando

- registra una tanda pasada ya absorbida;
- describe una hipótesis abandonada;
- contradice código, tests o catálogo actual;
- conserva planes sustituidos;
- solo existe como bitácora sin uso operativo;
- obliga a leer historia para entender una capacidad actual.

## No se archiva por defecto

No se crea histórico automáticamente. Un documento histórico solo se conserva si hay razón concreta de auditoría, compatibilidad o diagnóstico. La regla base es eliminar deuda documental, no moverla de carpeta.

## README raíz

El README raíz es la presentación pública del producto: qué hace, cómo se usa, cómo se ejecuta, qué tipos de artefactos soporta y cómo se integra el flujo Markdown + IA.

## Tests documentales

Los tests fuente deben proteger documentación viva, no perpetuar bitácoras obsoletas. Si un test exige un Markdown histórico sin valor actual, el test debe actualizarse junto con la limpieza.


## Compatibilidad con guardarraíles fuente

No debe conservarse historial por acumulación. Se conserva Markdown si explica una capacidad vigente, una frontera arquitectónica vigente o un contrato técnico vigente. Se elimina Markdown si solo registra una etapa pasada, una hipótesis abandonada o un plan sustituido. No se archiva por defecto.
