# Tanda 32 — Tests anti-fachada

## Objetivo

Impedir que vuelva a aparecer una capacidad visible sin implementación, prueba o aclaración de alcance.

## Alcance detallado

- Validar catálogo vs toolbar vs menú global.
- Validar recursos IA vs parsers reales.
- Validar matriz de capacidades vs catálogo de capacidades.
- Validar documentación viva contra estado esperado.
- Validar que SVG se nombre como vectorial documental, no WYSIWYG universal.

## Criterios de aceptación

- Si se agrega un botón, formato o recurso, debe existir evidencia.
- Los tests fallan cuando una promesa queda solo en UI o documentación.
- No se ocultan capacidades rotas detrás de texto comercial.

## Archivos o zonas probables

- src/test/java/.../productization/
- src/test/java/.../architecture/
- src/test/java/.../presentation/
- docs/producto/

## Pruebas recomendadas

- Ejecutar `scripts\02-ejecutar-tests.bat` después de la implementación.
- Agregar o ajustar guardarraíles de fuente cuando la tanda cambie contratos visibles.
- Hacer smoke manual si la tanda toca UI, canvas, exportaciones o empaquetado.

## Zonas protegidas

- No tocar el canvas conceptual salvo que la tanda lo indique explícitamente.
- No romper el carril vertical del SideDock.
- No prometer capacidades sin test, smoke o aclaración documental.

## Dependencias previas

- La tanda anterior debe quedar con `BUILD SUCCESS` antes de avanzar.
- Si aparece un fallo nuevo de compilación o arquitectura, se corrige primero y no se mezclan cambios.
- Se debe conservar el principio anti-fachada: capacidad visible = implementación + prueba + documentación o aclaración de alcance.

## Riesgos específicos

- Mezclar demasiados objetivos en una sola tanda puede ocultar regresiones.
- Cambiar UI sin smoke manual puede dejar una promesa visible rota.
- Cambiar parsers/exportadores sin round-trip puede crear pérdida silenciosa de datos.
- Cambiar shell/canvas sin guardarraíles puede romper pestañas, dirty state o exportaciones.

## Entregables esperados

- ZIP del proyecto con la tanda aplicada.
- Patch de la tanda.
- Documento de desarrollo en `docs/desarrollo/` cuando la tanda cambie contrato, UI o arquitectura.
- Tests o guardarraíles nuevos cuando se agregue o cambie una promesa del producto.
- Lista de smoke manual recomendado si toca UI, canvas, exportaciones, batch o empaquetado.

## Criterio de no avance

No avanzar a la siguiente tanda si ocurre cualquiera de estos casos:

- `scripts\02-ejecutar-tests.bat` no termina en `BUILD SUCCESS`.
- Maven falla por compilación.
- Un guardarraíl arquitectónico queda rojo.
- La tanda cambia una promesa visible y no deja test, smoke o documentación.
- La tanda toca canvas conceptual sin una autorización explícita.

## Nota de continuidad

Este archivo existe para que el trabajo pueda retomarse aunque se pierda la conversación. La implementación real debe apoyarse siempre en el código actual, el log más reciente y los patches aplicados, no solo en este plan.

## Estado tras implementación

Tanda 32 agrega guardarraíles anti-fachada en tres frentes:

1. **Toolbar vs capacidades:** las exportaciones prometidas por el catálogo deben aparecer en la toolbar contextual correspondiente.
2. **Recursos IA vs importabilidad:** los recursos exportables deben existir y los marcados como importables deben apuntar a tipos con parser/capacidad `IMPORT_MARKDOWN`.
3. **Documentación viva:** la matriz de capacidades debe cubrir todos los tipos visibles y mantener el contrato de SVG vectorial documental.

También se corrigió la incoherencia del descriptor `diccionario-datos-gramatica`, que ahora queda marcado como importable de acuerdo con el parser activo de diccionario de datos.

Resultado esperado al ejecutar `scripts\02-ejecutar-tests.bat`: `BUILD SUCCESS`.

