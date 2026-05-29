# Smoke UI MVP

Checklist manual mínimo después de cambios importantes.

## Entorno

- [ ] `scripts\03-verificar-entorno.bat` muestra Java 21, Maven y Git.
- [ ] `scripts\01-ejecutar-app.bat` abre la aplicación.
- [ ] La app no muestra jerga interna en la pantalla inicial ni en los menús principales.

## Importación por tipo

Probar al menos estos ejemplos:

- [ ] `examples\markdown\diagramas\conceptual_model_colegio_minimo_importable.md`
- [ ] `examples\markdown\diagramas\admin_module_map_restaurante_minimo.md`
- [ ] `examples\markdown\diagramas\roles_permissions_optica_minimo.md`
- [ ] `examples\markdown\diagramas\screen_flow_ventas_minimo.md`
- [ ] `examples\markdown\diagramas\admin_wireframes_ventas_minimo.md`
- [ ] `examples\markdown\diagramas\uml_class_restaurante_minimo.md`
- [ ] `examples\markdown\diagramas\c4_context_sistema_administrativo_minimo.md`
- [ ] `examples\markdown\diagramas\bpmn_basic_venta_minimo.md`

Para cada ejemplo:

- [ ] Se reconoce el tipo correcto.
- [ ] La salida central no está vacía.
- [ ] El contenido visible corresponde al tipo importado.
- [ ] Las tablas o formularios son apoyo, no la salida principal.

## Flujo modelo conceptual

- [ ] Crear o importar proyecto de Modelo conceptual.
- [ ] Ver diagrama en Chen.
- [ ] Cambiar a pata de gallo.
- [ ] Mover una figura.
- [ ] Guardar `.dms`.
- [ ] Cerrar y abrir el `.dms`.
- [ ] Exportar SVG.
- [ ] Exportar PNG.
- [ ] Exportar Markdown actualizado.

## Flujo diccionario de datos

- [ ] Crear proyecto de Diccionario de datos.
- [ ] Revisar que el centro sea documento/fichas, no solo tabla de mantenimiento.
- [ ] Editar un campo mínimo.
- [ ] Exportar PDF.
- [ ] Exportar Markdown.
- [ ] Guardar y reabrir `.dms`.


## Workbench canónico y zoom

Para cada diagrama visual migrado —Mapa de módulos, Flujo de pantallas, Wireframes, UML Clases, Comportamiento, C4 y Despliegue— validar:

- [ ] El área central usa el mismo patrón visual que Modelo conceptual: header superior, lienzo central, panel izquierdo y panel derecho.
- [ ] El header descriptivo del área de trabajo se puede ocultar/restaurar cuando aplique.
- [ ] Los paneles laterales pueden ocultarse/restaurarse sin romper el lienzo.
- [ ] El zoom con rueda acerca/aleja respecto al cursor o al centro visible, no respecto a la esquina superior izquierda.
- [ ] Ajustar/centrar contenido deja el diagrama visible y no perdido fuera del viewport.
- [ ] El paneo con clic derecho desplaza el viewport sin mover permanentemente los nodos.
- [ ] La exportación PNG contiene solo el diagrama, con fondo y margen, sin toolbar, tabs, header ni paneles laterales.
- [ ] Dentro del lienzo no aparecen controles interactivos reales como botones funcionales, listas, tablas o campos de formulario; en wireframes deben verse como figuras simuladas.

## Diagramas visuales reconstruidos

Para módulos, roles/permisos, pantallas, wireframes, UML, C4, despliegue y comportamiento:

- [ ] Importar ejemplo oficial.
- [ ] Ver salida visual central.
- [ ] Editar un elemento mínimo desde propiedades o panel auxiliar.
- [ ] Exportar PNG.
- [ ] Exportar Markdown actualizado.
- [ ] Guardar `.dms`.
- [ ] Reabrir `.dms`.
- [ ] Confirmar que el tipo y contenido se conservan.

## Exportación por lote

- [ ] Abrir al menos tres proyectos de tipos distintos.
- [ ] Usar botón Exportar abiertos.
- [ ] Verificar carpetas `input/`, `editable/`, `output/`.
- [ ] Verificar `.dms` por proyecto.
- [ ] Verificar Markdown actualizado donde aplique.
- [ ] Verificar PNG pendiente/generado desde salida visual activa donde aplique.
- [ ] Verificar SVG solo para modelo conceptual.
- [ ] Verificar PDF solo para diccionario de datos.

## Recursos IA y ejemplos

- [ ] Exportar recursos IA desde la app.
- [ ] Confirmar que se copian gramáticas, plantillas y ejemplos oficiales.
- [ ] Confirmar que los IDs `diagram_type` coinciden con el catálogo.
- [ ] Confirmar que el usuario puede entregar una plantilla/ejemplo a una IA para generar Markdown equivalente del cliente.

## Tanda 15 — Smoke QA post-refactor

Además de este checklist general, completar:

```txt
docs\testeo\checklists\15_smoke_qa_post_refactor.md
```

Ese checklist revisa específicamente SideDock por tab activo, exportaciones post-refactor, roles/permisos como matriz, diccionario formal, ayuda CHM académica y guardado/carga después de las tandas 0–14.

## Cierre

- [ ] No hay botones de exportación que no correspondan al tipo activo.
- [ ] No hay pantallas de inicio usadas como sustituto de diagrama.
- [ ] No hay dramatización GUI usada como resultado final del diagrama.
- [ ] No aparece ERD físico PostgreSQL como tipo seleccionable.
- [ ] No hay mensajes visibles que hablen de clases, parsers, handlers, providers o infraestructura interna.

## Cierre profesional T17 — recorrido por los 16 tipos

Este bloque se agregó como cierre de las tandas 00–17. No reemplaza las secciones anteriores; las vuelve más explícitas para el paquete profesionalizado.

### Modelado de datos

- [ ] Modelo conceptual: importar ejemplo mínimo, mover entidad, cambiar notación, exportar PNG/SVG/Markdown y reabrir `.dms`.
- [ ] Diccionario de datos: editar metadatos, introducción y campo; exportar PDF tabular y Markdown; reabrir `.dms`.

### Procesos de negocio

- [ ] BPMN básico: verificar eventos, tareas, gateways, carriles y flujo legible.
- [ ] Flujo operativo: verificar pasos, responsables, documentos/evidencias y orden humano.

### Arquitectura de software

- [ ] C4 Contexto: verificar persona, sistema central, externos y boundary sin mezclar contenedores técnicos.
- [ ] C4 Contenedores: verificar app/API/BD/servicios y tecnologías visibles sin saturar.
- [ ] Despliegue técnico: verificar ambientes, servidor, red, servicio, artefacto y BD sin confundirse con C4 Contenedores.

### UML estructural

- [ ] UML Clases: verificar cajas autoajustables, atributos/métodos visibles, paquetes como contenedores y relaciones UML.

### UML de comportamiento

- [ ] UML Casos de uso: verificar actor de palito, casos en óvalo, sistema como contenedor, include/extend/generalización.
- [ ] UML Actividad: verificar inicio/final, acciones, decisiones, fork/join y guardas cuando existan.
- [ ] UML Estados: verificar inicio sólido, final doble, estados, transiciones, evento/guarda/acción si aplica.

### UML de interacción

- [ ] UML Secuencia: verificar participantes arriba, líneas de vida, mensajes numerados, retornos, activaciones y fragmentos `alt`/`opt`/`loop`.

### Aplicaciones administrativas

- [ ] Mapa de módulos: verificar módulos raíz como agrupadores y dependencias legibles.
- [ ] Roles y permisos: verificar matriz rol × permiso, no canvas libre forzado.
- [ ] Flujo de pantallas: verificar pantalla inicial, módulos funcionales y transiciones con acción.
- [ ] Wireframes administrativos: verificar pantalla, topbar, sidebar, tabla, campos y botones como figuras primitivas no interactivas.

### Cierre manual

- [ ] Completar también `docs\implementacion\tanda_17_smoke_qa_cierre_release\03_checklist_cierre_release_manual.md`.
- [ ] Registrar en un issue o documento cualquier brecha visual detectada antes de crear instalador.
