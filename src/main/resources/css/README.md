# CSS de Domain Model Studio

La hoja principal de estilos es `app-light.css`. Funciona como ensamblador de hojas pequeñas por responsabilidad visual. La cascada debe mantenerse legible y con archivos vivos: no se conservan CSS vacíos o meramente documentales.

La intención visual vigente:

- aplicación desktop clásica, clara y formal;
- menús y barras de acciones sobrias;
- paneles delimitados con bordes;
- botones rectos y sin radius ornamental;
- tipografía legible tipo Segoe UI;
- uso moderado de color;
- área de diagrama clara para exportaciones y capturas;
- barra de estado inferior como herramienta de escritorio.

## Estructura viva

```txt
app-light.css                              hoja ensambladora
tokens.css                                 tokens visuales base
diagram-palette.css                        paleta semántica compartida de diagramas
shell-menu-toolbar-base.css                shell, menú y toolbar base
panels-sidebar.css                         paneles laterales y árbol
canvas-base.css                            lienzo, grilla y placeholders base
inspector-base.css                         inspector base
statusbar.css                              barra de estado
desktop-controls.css                       scrollbars y tabs
diagram-conceptual.css                     Chen y pata de gallo
welcome-classic.css                        bienvenida clásica
inspector-contextual.css                   inspector contextual
identity-foundation.css                    identidad base del shell
identity-canvas.css                        identidad del canvas y selección
identity-inspector.css                     identidad del inspector
identity-conceptual-details.css            detalles de notación conceptual
identity-welcome-status.css                bienvenida clásica, estado y tooltips
manual.css                                 guía académica/manual integrado
click-message-dialog.css                   diálogo reutilizable
module-operational-help.css                ayuda operativa por módulo
notation-and-selection.css                 selector de notación y selección
inspector-appearance.css                   controles de apariencia
welcome-projects.css                       bienvenida con proyectos y diálogos
welcome-editor.css                         viewport, iconografía PNG y footer
toolbar-viewport.css                       toolbar estable e inicio simple
welcome-start-layout.css                   inicio tipo IDE: layout y títulos
welcome-start-actions.css                  inicio tipo IDE: acciones
welcome-start-cards.css                    inicio tipo IDE: tarjetas y vacíos
editor-tabs.css                            pestañas escrolleables de proyectos
diagram-interaction.css                    interacciones visuales de relaciones
interactive-canvas-base-details.css        base del lienzo común reutilizable
interactive-canvas.css                     lienzo común reutilizable
diagram-surface.css                        superficie zoomable canónica
diagram-drawing.css                        primitivas de dibujo compartidas
workbench.css                              workbench común: header, slots y paneles
placeholder.css                            vista de planificación
toolbar-contextual.css                     toolbar contextual
data-dictionary.css                        diccionario de datos
logical-business.css                       ensamblador del Levantamiento lógico
logical-business-base.css                  base del Levantamiento lógico
logical-business-document.css              documento central del Levantamiento lógico
logical-business-side-panels.css           paneles principales del Levantamiento lógico
logical-business-side-panels-extra.css     tarjetas/familias/candidatos del Levantamiento lógico
logical-business-graph.css                 grafo lógico del negocio
module-map.css                             mapa de módulos
uml-class.css                              UML Clases
administrative-editors.css                 base común de editores administrativos
roles-permissions.css                      roles y permisos
roles-permissions-tabs.css                 pestañas de roles y permisos
screen-flow.css                            flujo de pantallas
wireframe.css                              wireframes administrativos
behavior-diagram.css                       BPMN básico y UML comportamiento
sequence-diagram.css                       UML Secuencia especializado
architecture-diagram.css                   C4 y despliegue técnico
free-graph.css                             grafo libre
diagram-academic-primitives.css            contrato visual académico primitivo
diagram-academic-primitives-shapes.css     símbolos y conectores académicos
interactive-canvas-selection-overrides.css selección transversal final
```

## Regla de mantenimiento

- Mantener `app-light.css` como ensamblador.
- Agregar estilos nuevos en el archivo del área correspondiente.
- Preferir clases compartidas antes que duplicar reglas por cada tipo de proyecto.
- Evitar volver a crear una sola hoja gigante.
- Mantener comentarios de sección cuando ayuden a leer el archivo.
- No introducir jerga interna en nombres visibles de UI.
- No conservar archivos CSS vacíos o meramente documentales: si una hoja fue reemplazada, se elimina y se actualiza este README.

Regla importante: el CSS no debe transformar la app en un dashboard web moderno. Si se agregan nuevas vistas, deben respetar la estética de herramienta técnica de escritorio.

## Regla IMP-02: CSS pequeño por responsabilidad

Evitar archivos CSS grandes. Como criterio práctico, una hoja nueva debe intentar mantenerse por debajo de 180 líneas. Si crece más, dividir por responsabilidad visual: tokens, shell, canvas, inspector, bienvenida, tabs o familia de diagrama.

## Regla Tanda 37: no-radius por alcance

La UI nueva o corregida debe preferir clases compartidas antes que duplicar estilos por cada familia de diagrama. Los patrones de panel, lista, tabla, botón, etiqueta, badge y header deben resolverse con tokens y clases compartidas siempre que sea posible.

Regla visual: sin radius ornamental en botones, paneles, tabs, listas, tablas, labels, headers exportables y contenedores rectangulares. Esta regla no elimina geometría semántica de notaciones formales: óvalos UML, eventos BPMN, estados iniciales/finales y entidades Chen conservan su forma cuando la teoría del diagrama lo exige.

La pantalla de inicio congelada y el modelo conceptual legacy no deben entrar en limpiezas globales de CSS salvo emergencia explícita.
