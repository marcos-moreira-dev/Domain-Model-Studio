# Tanda 14 — Limpieza visual, smoke y cierre de Levantamiento lógico

Estado: **cierre funcional del módulo documental**  
Tipo: **limpieza, verificación y continuidad**

## Objetivo

Cerrar la fase de implementación del módulo **Levantamiento lógico** dejando claro qué está terminado, qué debe verificarse manualmente y qué no debe prometer la herramienta todavía.

El módulo queda definido como un **expediente documental estructurado**:

```txt
Markdown canónico importado
→ lectura humana en workspace
→ edición controlada con campos
→ validación/trazabilidad/derivaciones
→ exportación Markdown revisable
```

No es un canvas visual, no es un diagrama falso y no reemplaza la revisión humana del levantamiento.

## Decisiones cerradas

- El workspace se comporta como formulario documental amplio, no como conjunto de tarjetas web.
- Los campos editables deben aparecer precargados con el contenido importado desde Markdown.
- Los campos vacíos deben indicar que el dato no vino en Markdown, no simular contenido inexistente.
- Los datos automáticos, como conteos y madurez calculada, se mantienen como solo lectura.
- Los elementos vinculados se tratan como referencias navegables dentro del expediente.
- Las derivaciones son borradores Markdown revisables hacia otros artefactos; no se importan automáticamente.
- La ayuda del SideDock es operativa y específica del módulo; no sustituye la referencia académica.
- La ayuda operativa transversal aplica a los tipos nuevos o migrados, excepto al modelo conceptual canónico.

## Flujo esperado de uso

1. Exportar recursos IA desde el toolbar general.
2. Tomar la plantilla oficial de Levantamiento lógico.
3. Pedir a la IA que rellene esa plantilla con entrevistas, observaciones o texto del cliente.
4. Importar el Markdown resultante en Domain Model Studio.
5. Navegar el expediente desde el árbol.
6. Corregir campos desde el workspace.
7. Usar validación, trazabilidad y derivaciones como apoyo.
8. Exportar una versión Markdown mejorada.
9. Guardar `.dms` para continuar luego.

## Smoke manual obligatorio

La guía focalizada queda en:

```txt
docs/testeo/SMOKE_LEVANTAMIENTO_LOGICO_TANDA_14.md
```

Debe ejecutarse después de la suite automatizada:

```bat
scripts\02-ejecutar-tests.bat
scripts\11-smoke-levantamiento-logico.bat
```

## Riesgos que quedan explícitos

- La edición es controlada, pero no sustituye todavía un editor Markdown avanzado.
- El CRUD documental permite crear y eliminar elementos principales, pero todavía debe usarse con revisión humana.
- Las derivaciones requieren revisión antes de importarlas como nuevos proyectos.
- La automatización UI/E2E con fakes de FileChooser y diálogos queda para una tanda futura.

## Cierre

Esta tanda deja el módulo listo para prueba manual formal y uso controlado. La siguiente fase razonable ya no debería crecer el módulo sin antes revisar el smoke, registrar hallazgos y decidir si se corrigen detalles UX o se pasa a otro tipo de proyecto.
