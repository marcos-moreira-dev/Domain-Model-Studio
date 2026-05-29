> **Nota Tanda 30:** este documento se conserva como soporte histórico/técnico. Para estado vigente y continuidad inmediata leer primero `docs/diagnostico/ESTADO_AUDITORIA_ACTUAL.md` y `docs/raiz/PLAN_TANDAS_RESTANTES.md`. Si contradice el log actual, el código o el catálogo Java, manda la evidencia actual.

# Macro diagnóstico del estado funcional y documental

Estado: **diagnóstico de cierre parcial antes de Javadocs**  
Tanda: **22**  
Objetivo: dejar notificado qué está cubierto, qué falta verificar, qué riesgos quedan y qué debe corregirse antes de declarar el producto como cerrado.

## Contexto de entrada

El usuario reportó que la última base probada pasó todos los tests. Esta tanda no intenta agregar funcionalidades ni continuar refactors. Su objetivo es dejar el proyecto entendible para continuar después, incluso en otro chat.

## Decisión de alcance

### Se hace en esta tanda

- Limpieza documental ligera mediante mapas, no borrado masivo.
- Macro diagnóstico de pendientes y riesgos.
- Matriz categorizada de casos de uso.
- Guardarraíles de prueba documental para que el diagnóstico no desaparezca.
- Actualización del plan de tandas.

### No se hace en esta tanda

- Javadocs pedagógicos.
- Nueva funcionalidad.
- Refactor adicional.
- Cambios en el modelo conceptual.
- Cambios en el `README.md` raíz.
- Eliminación masiva de documentación histórica.

## Estado macro por área

| Área | Estado | Diagnóstico |
|---|---|---|
| Compilación/tests base | Verde reportado por usuario | La base más reciente reportada por usuario pasó todos los tests. |
| Persistencia `.dms` | Estable | Hubo fallos JSON previos, pero fueron corregidos y los round-trips quedaron cubiertos por tests. |
| Grafo libre | Funcional, requiere smoke visual | Las etiquetas/drag/exportaciones fueron reforzadas; falta validar manualmente en runtime final. |
| UML Clases | Funcional, requiere smoke UI | Se corrigió selección, drag, jerarquías, abrir código y exportación; falta UI/E2E con fakes. |
| Exportaciones visuales | Parcialmente verificadas | Hay tests e infraestructura; falta abrir PNG/SVG por tipo y revisar visualmente. |
| Markdown | Fuerte | Importación/exportación tiene más cobertura que PNG/SVG/PDF. |
| PDF | Limitado por tipo | No debe prometerse PDF universal. Principalmente aplica a documentación/tablas como diccionario de datos. |
| SideDock/toolbar/capacidades | Mejorado | Hubo ajustes por guardarraíles; conviene no tocar sin tests. |
| Documentación | Grande, mixta | Hay mucha bitácora histórica; ahora se agrega mapa de lectura y diagnóstico macro. |
| Javadocs | Aplazado | Debe tratarse en otro chat/tanda para no saturar contexto. |

## Riesgos pendientes

| Riesgo | Severidad | Motivo | Acción recomendada |
|---|---|---|---|
| Botones o acciones visibles sin smoke manual | Media | Los tests fuente pueden pasar aunque una interacción real sea incómoda. | Tanda futura UI/E2E y smoke visual. |
| Exportación visual vacía o poco legible en algún tipo | Media | PNG/SVG requieren revisión visual, no solo archivo existente. | Abrir export por tipo y registrar evidencia. |
| Prometer UML/BPMN/C4 como especificación completa | Alta de producto | La app implementa diagramación funcional básica, no herramientas profesionales completas. | Mantener lenguaje de producto honesto. |
| Diálogos nativos y editor externo en UI tests | Media técnica | Son frágiles en automatización. | Usar servicios fake para FileChooser y editor launcher. |
| Tocar modelo conceptual por accidente | Alta técnica | Está funcionando como módulo canónico/protegido. | Mantener guardarraíles y no usarlo como zona de refactor. |
| Documentación histórica confundida con plan vigente | Media | `docs/estado/` es grande y mezcla etapas. | Usar `MAPA_DOCUMENTACION_VIVA.md` como entrada. |
| Regresiones por refactor posterior | Media | Ya hubo varios hotfixes pequeños. | Antes de refactor nuevo, correr tests y revisar matriz. |

## Pendientes reales antes de cierre final

Estos pendientes no son nuevas funcionalidades; son verificación de promesas existentes.

| Pendiente | Tipo | Prioridad | Evidencia esperada |
|---|---|---|---|
| Smoke visual de cada tipo visible | Manual | Alta | Captura o nota por tipo: abre, importa, exporta, reabre. |
| UI/E2E mínimo de app | Test futuro | Alta | Lanzar app, crear proyecto, importar ejemplo, seleccionar, exportar. |
| UI/E2E de UML Clases | Test futuro | Alta | Selección directa, drag, abrir código con fake launcher. |
| UI/E2E de Grafo libre | Test futuro | Media | Crear/seleccionar/arrastrar nodo y verificar etiqueta de relación. |
| Revisión de exportaciones PNG/SVG | Manual + test integración | Alta | Archivo no vacío y visualmente correcto por tipo. |
| Revisión de PDF donde aplique | Manual | Media | Diccionario de datos/documentos tabulares legibles. |
| Revisión de ayuda/teoría por tipo | Manual | Media | Contenido visible no contradice alcance real. |
| Empaquetado/App Image Windows | Técnico | Media | Validar script y arranque en máquina limpia. |
| Limpieza documental profunda | Documental | Baja/Media | Archivar o indexar histórico sin borrar evidencia usada por tests. |
| Javadocs pedagógicos | Pedagógico | Aplazado | Otro chat/tanda. |

## Qué está suficientemente cubierto por ahora

- Existencia de tipos visibles en matriz de aceptación.
- Capacidades reales básicas y guardarraíles anti-fachada.
- Persistencia JSON `.dms` después de hotfixes.
- Importadores/exportadores Markdown principales.
- Reglas de arquitectura y límites de capas principales.
- Refactors principales de canvas, SideDock/workbench, UML/Grafo y persistencia/exportación.
- Onboarding/scripts base.

## Qué no debe venderse como completo

- UML completo a nivel especificación formal.
- BPMN completo a nivel Camunda/engine/spec.
- C4 completo a nivel suite profesional.
- Editor wireframe tipo Figma.
- Editor gráfico general tipo Draw.io.
- Sincronización viva bidireccional Markdown ↔ canvas en tiempo real, salvo que se implemente explícitamente.
- PDF universal para todos los diagramas.
- Abrir código en clases que no provienen de importación de código fuente.

## Criterio de cierre recomendado

Antes de declarar cierre final del proyecto base:

1. correr `scripts\02-ejecutar-tests.bat`;
2. correr `scripts\08-validacion-local-completa.bat` si el entorno lo permite;
3. abrir app;
4. para cada tipo visible, importar ejemplo oficial o Markdown mínimo;
5. guardar `.dms`;
6. reabrir `.dms`;
7. exportar formatos declarados;
8. abrir PNG/SVG/PDF/MD resultante;
9. registrar si algo falla en una tabla de smoke.

## Próxima tanda sugerida

La siguiente tanda en este chat no debería ser Javadocs. Si se continúa aquí, conviene que sea una de estas dos:

1. **Cierre final liviano**: actualizar plan, empaquetar y dejar instrucciones de smoke manual.
2. **UI/E2E mínimo**: preparar infraestructura para TestFX/fakes sin intentar cubrir todo.

Los Javadocs pedagógicos quedan explícitamente para otro chat por límite de contexto.
