# Matriz de smoke por los tipos oficiales

Esta matriz define qué debe probarse por cada tipo antes de considerar cerrada una entrega. La intención es evitar que un tipo aparezca como disponible solo porque abre una pantalla. Un tipo disponible debe tener una salida principal real: diagrama, documento, matriz o wireframe.

| Categoría | Tipo | Smoke principal | Exportación esperada | Revisión visual clave |
|---|---|---|---|---|
| Modelado de datos | Modelo conceptual | Importar ejemplo mínimo y UENS, mover entidad, cambiar Chen/pata de gallo, guardar y reabrir `.dms`. | PNG, SVG, Markdown, `.dms`. | Debe seguir siendo canon visual: figuras primitivas sobrias, relaciones legibles y rutas editables. |
| Modelado de datos | Diccionario de datos | Crear documento, editar metadatos, agregar entidad/campo, exportar PDF y Markdown, reimportar Markdown. | PDF, Markdown, `.dms`. | PDF con tablas reales, portada/metadatos, introducción opcional y logo opcional como referencia relativa. |
| Procesos de negocio | BPMN básico | Importar proceso, agregar tarea/gateway/carril, conectar flujo, validar. | PNG, SVG, Markdown, `.dms`. | Deben distinguirse eventos, tareas, gateways y carriles; no debe parecer grafo genérico. |
| Procesos de negocio | Flujo operativo | Importar procedimiento, revisar pasos/responsables/documentos, mover y conectar. | PNG, SVG, Markdown, `.dms`. | Debe leerse como procedimiento humano: quién hace qué, en qué orden y con qué evidencia. |
| Arquitectura de software | C4 Contexto | Importar contexto, revisar sistema central, personas y externos, validar nivel. | PNG, SVG, Markdown, `.dms`. | No debe bajar a APIs/BD/servidores; debe responder qué sistema se mira y con quién se comunica. |
| Arquitectura de software | C4 Contenedores | Importar contenedores, revisar app/API/BD/servicios, validar relaciones. | PNG, SVG, Markdown, `.dms`. | Debe diferenciar clientes, APIs, contenedores, base de datos y servicios externos. |
| Arquitectura de software | Despliegue técnico | Importar despliegue, revisar ambientes, nodos, red, servicios y artefactos. | PNG, SVG, Markdown, `.dms`. | Debe mostrar dónde corre cada cosa y no confundirse con C4 Contenedores. |
| UML estructural | UML Clases | Importar ejemplo, revisar módulos, clases, atributos/métodos y relaciones. | PNG, SVG, Markdown, `.dms`. | Cajas autoajustables; paquetes como contenedores; relaciones UML distinguibles. |
| UML de comportamiento | UML Casos de uso | Importar ejemplo, revisar actores, casos, boundary, include/extend/generalización. | PNG, SVG, Markdown, `.dms`. | Actor de palito, casos como óvalos, sistema como contenedor, relaciones con notación UML. |
| UML de comportamiento | UML Actividad | Importar actividad, revisar inicio/final, acciones, decisiones, fork/join si aplica. | PNG, SVG, Markdown, `.dms`. | Debe diferenciarse de BPMN: acciones y nodos UML, guardas visibles cuando existan. |
| UML de comportamiento | UML Estados | Importar estados, revisar inicio/final, estados, transiciones y notas. | PNG, SVG, Markdown, `.dms`. | Inicio sólido, final doble, estados redondeados y transiciones expresivas. |
| UML de interacción | UML Secuencia | Importar secuencia, reordenar mensajes, agregar retorno/activación/fragmento. | PNG, SVG, Markdown, `.dms`. | Participantes arriba, tiempo vertical, mensajes horizontales numerados y fragmentos `alt`, `opt`, `loop`. |
| Aplicaciones administrativas | Mapa de módulos | Importar mapa, revisar módulos raíz, submódulos y dependencias. | PNG, SVG, Markdown, `.dms`. | Módulos raíz como agrupadores funcionales, no cajas tiradas. |
| Aplicaciones administrativas | Roles y permisos | Importar matriz, agregar rol/permiso/asignación, exportar. | SVG/Markdown o salida matricial declarada, `.dms`. | Debe ser matriz operativa rol × permiso; no forzar canvas libre. |
| Aplicaciones administrativas | Flujo de pantallas | Importar flujo, revisar pantalla inicial, módulos y transiciones. | PNG, SVG, Markdown, `.dms`. | Debe leerse como navegación entre pantallas, no como mapa de módulos. |
| Aplicaciones administrativas | Wireframes administrativos | Importar wireframes, revisar pantallas, topbar, sidebar, tablas, campos y botones simulados. | PNG, SVG, Markdown, `.dms`. | Debe ser scaffolding primitivo; no Figma, no UI final, no controles interactivos reales. |
| Levantamiento y análisis | Levantamiento lógico | Crear o importar ejemplo Óptica Horizonte, revisar estructura documental, Validación, Trazas internas y Ayuda y glosario, exportar Markdown y reabrir `.dms`. | Markdown, `.dms`. | Debe sentirse como expediente lógico/documental; no debe prometer PNG/SVG/PDF ni comprimirse con inspector fijo fuera del SideDock. |
| Levantamiento y análisis | Grafo lógico del negocio | Importar ejemplo UENS, revisar leyenda MF/FL/CU/ACC/RN/PRE/INV/POST/ENT/EST/REP/RISK/PEND, seleccionar relaciones, mover nodos, guardar y reabrir `.dms`. | PNG, SVG, Markdown, `.dms`. | Debe leerse como grafo semántico trazable derivado del levantamiento lógico; no como grafo libre ni árbol rígido. |
| Modelado libre | Grafo libre | Importar ejemplo mínimo y UENS, crear nodo, crear relación dirigida/no dirigida, crear autorrelación y mover selección múltiple. | PNG, SVG, Markdown, `.dms`. | Nodos rectangulares legibles, autorrelaciones curvas, etiquetas centradas y SideDock común. |

## Regla común de aceptación

Para cada tipo se debe confirmar:

1. El ejemplo mínimo abre.
2. El ejemplo UENS/gordito abre cuando existe.
3. El toolbar contextual corresponde al tipo activo.
4. No aparece un botón visible sin efecto conocido.
5. La salida central no está vacía.
6. Guardar y reabrir `.dms` conserva el tipo.
7. La exportación visible corresponde a capacidades oficiales.
8. La jerga visible pertenece al dominio del usuario final, no al código interno.
