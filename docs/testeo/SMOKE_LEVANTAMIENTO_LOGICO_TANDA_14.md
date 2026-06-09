# Smoke focalizado — Levantamiento lógico

Estado: **manual formal**  
Tanda: **14**  
Tipo: **verificación UX/documental del módulo logical-business-intake**

## Objetivo

Validar que el módulo **Levantamiento lógico** funcione como expediente documental editable, importable y exportable, sin prometer canvas visual, sincronización externa ni generación automática de otros artefactos.

## Precondiciones

Ejecutar primero:

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
```

Abrir la aplicación:

```bat
scripts\01-ejecutar-app.bat
```

## Archivo recomendado

Usar uno de estos recursos oficiales:

```txt
examples\markdown\plantillas\logical_business_intake.md
src\main\resources\ai-resources\official-markdown\levantamiento-logico\logical_business_intake_template.md
src\main\resources\ai-resources\official-markdown\levantamiento-logico\logical_business_intake_uens_gordito.md
examples\markdown\levantamiento-logico\logical_business_intake_uens_gordito.md
```

## LG-SMOKE-001 — Importación y reconocimiento

- [ ] Importar Markdown de Levantamiento lógico.
- [ ] El tipo reconocido es `logical-business-intake`.
- [ ] Se abre una pestaña documental, no un canvas visual.
- [ ] La toolbar contextual muestra acciones de modelo, vista y exportación PDF/Markdown.

## LG-SMOKE-002 — SideDock y árbol del expediente

- [ ] El SideDock muestra Estructura, Ficha rápida, Elementos lógicos, Entidades y relaciones, Validación, Impacto y dependencias, Exportar y Ayuda y glosario.
- [ ] El árbol respeta orden natural de secciones: `0, 1, 2, 3...`, no `1, 10, 11...`.
- [ ] Expandir y Contraer funcionan.
- [ ] Un clic sobre una categoría cerrada la expande.
- [ ] La ayuda rápida por `?` abre un diálogo legible.

## LG-SMOKE-003 — Workspace como formulario documental

- [ ] Al seleccionar documento, sección, elemento, entidad, atributo, relación o pregunta, el workspace muestra un formulario claro.
- [ ] Los controles editables aparecen precargados con el contenido importado desde Markdown.
- [ ] Si un campo está vacío, se entiende que no vino en Markdown.
- [ ] Los datos automáticos aparecen como solo lectura.
- [ ] No aparecen tarjetas web pesadas ni panel derecho fijo.

## LG-SMOKE-004 — Edición mínima

- [ ] Editar título/descripción/justificación de un nodo compatible.
- [ ] Presionar **Actualizar documento**.
- [ ] Cambiar a otro nodo y volver: el dato actualizado se conserva en memoria.
- [ ] Exportar Markdown y confirmar que el cambio aparece en el archivo exportado.
- [ ] Exportar PDF y confirmar que genera un expediente formal sin controles de UI.
- [ ] Confirmar que la pestaña queda marcada con `*` al editar y que se limpia al guardar.

## LG-SMOKE-005 — Navegación por referencias

- [ ] Seleccionar una sección con elementos vinculados.
- [ ] Hacer clic en una referencia automática.
- [ ] El workspace navega al contenido del elemento vinculado.

## LG-SMOKE-006 — Validación y trazabilidad

- [ ] Abrir Validación desde toolbar o SideDock.
- [ ] Revisar resumen global.
- [ ] Seleccionar un nodo con hallazgos y confirmar que se filtra el contexto.
- [ ] Abrir Impacto y dependencias y confirmar relaciones entrantes/salientes o ausencia explicada.

## LG-SMOKE-007 — Artefactos compatibles

- [ ] Abrir Artefactos compatibles.
- [ ] Confirmar que se muestran como registro formal, no como tarjetas grandes.
- [ ] Seleccionar una derivación desde el árbol.
- [ ] El workspace muestra el borrador seleccionado.
- [ ] Confirmar que se comunica como Markdown revisable, no como importación automática.

## LG-SMOKE-008 — CRUD documental controlado

- [ ] Crear elemento lógico desde Elementos.
- [ ] Crear pregunta pendiente.
- [ ] Crear entidad candidata desde Entidades.
- [ ] Crear atributo o relación cuando aplique.
- [ ] Eliminar una selección compatible con confirmación.
- [ ] Confirmar que no se pide Markdown crudo en formularios.

## LG-SMOKE-009 — Guardar/reabrir `.dms`

- [ ] Guardar proyecto `.dms`.
- [ ] Cerrar el proyecto.
- [ ] Reabrir `.dms`.
- [ ] Verificar que documento, selección navegable y cambios principales se conservan.

## LG-SMOKE-010 — Ayuda operativa

- [ ] Abrir Ayuda del SideDock.
- [ ] Confirmar que explica cómo usar el módulo, no teoría matemática extensa.
- [ ] Abrir menú Ayuda → Guía académica / referencia teórica general.
- [ ] Confirmar que la ayuda académica sigue separada.

## Criterio de aprobación

El smoke queda aprobado si:

```txt
- No hay excepción visible.
- Importar/editar/exportar funciona.
- Las ayudas y derivaciones no prometen más de lo implementado.
- Los hallazgos visuales quedan registrados para una tanda futura.
```
