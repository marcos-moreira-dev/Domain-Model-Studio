# Wireframes administrativos

## Introducción
Un wireframe administrativo es una maqueta de baja fidelidad para pensar pantallas de sistemas administrativos antes de programarlas. No busca verse bonito ni convertirse en una interfaz real. Su valor está en ordenar la estructura: qué información aparece, qué acción realiza el usuario, qué datos se consultan o capturan, qué errores pueden ocurrir y cómo se conecta esa pantalla con el trabajo del negocio.

En Domain Model Studio, este tema debe entenderse como una guía de estudio para diseñar pantallas con primitivas simples. Un rectángulo puede representar una pantalla, un panel, una tabla, un campo, un botón o un modal. Una flecha puede representar navegación o continuidad entre dos pantallas. El objetivo no es competir con Figma ni con un editor frontend, sino ayudarte a pensar la interfaz con suficiente claridad para conversar con un cliente, pedir una implementación a una IA o preparar una especificación para desarrollo.

![Estructura base de una pantalla administrativa con barra superior, menú lateral y área de trabajo.](figure:admin-wireframe-layout-base)

## Pregunta que responde
El wireframe administrativo responde:

¿Qué debe ver y hacer el usuario en esta pantalla para completar su trabajo con claridad y seguridad?

También ayuda a responder:

¿Qué datos aparecen?
¿Qué acción principal existe?
¿Qué botones secundarios hacen falta?
¿Qué errores o estados vacíos se deben considerar?
¿Qué pantalla se abre después de una acción importante?

Ejemplo para una tienda de reparación de celulares:

En el listado de órdenes, el usuario necesita ver número de orden, cliente, equipo, estado, fecha, técnico y acciones como abrir detalle o crear nueva orden.

## Idea central
La idea central es:

Un wireframe administrativo no diseña la apariencia final; diseña la estructura funcional de una pantalla.

Por eso, un wireframe útil puede ser sobrio, incluso feo, si responde bien a las preguntas del negocio. En una pantalla administrativa importa mucho que el usuario encuentre rápido lo que necesita, entienda qué acción debe ejecutar y no cometa errores peligrosos.

Un buen wireframe administrativo debe mostrar propósito, jerarquía, datos, acciones y feedback. Si solo muestra cajas sueltas, no está cumpliendo su función.

## Qué es
Un wireframe administrativo es una maqueta de baja fidelidad para estudiar la estructura de una pantalla o conjunto de pantallas de un sistema administrativo. Usa rectángulos, textos, campos simulados, tablas, paneles, tabs, modales y flechas para pensar qué debe ver y hacer el usuario antes de programar la interfaz.

## Para qué sirve
Sirve para aclarar pantallas, módulos visuales, acciones principales, estados, errores, permisos y patrones de interacción antes de construir frontend real. También ayuda a conversar con clientes, preparar especificaciones para IA y evitar pantallas sobrecargadas o ambiguas.

## Qué representa
Representa pantallas y estructuras visuales de trabajo como:

ventanas
pantallas
paneles
secciones
formularios
listados
tablas simuladas
botones simulados
filtros
modales
mensajes
estados vacíos
errores
acciones principales
acciones secundarias
relaciones simples entre pantallas

Ejemplo:

Pantalla: Listado de órdenes de reparación

Contiene:
- título del módulo;
- filtros por estado, técnico y fecha;
- tabla de órdenes;
- botón Nueva orden;
- acciones por fila;
- paginación;
- mensaje si no hay resultados.

## Qué no representa
No representa:

Figma completo
HTML real
JavaFX real
frontend programado
diseño visual final
branding definitivo
responsive detallado
animaciones
pruebas con usuarios
arquitectura backend
base de datos

Un wireframe puede inspirar el frontend, pero no lo reemplaza. Tampoco prueba que la experiencia de usuario ya esté terminada. Solo permite pensar la estructura antes de invertir tiempo en implementación.

![Límites del wireframe: sirve para estructura, no para prometer diseño final ni frontend real.](figure:admin-wireframe-common-boundaries)

## Elementos principales
Sus elementos principales son pantalla, panel, sección, campo simulado, etiqueta, tabla simulada, botón, acción principal, acción secundaria, modal, tab, mensaje, estado vacío, badge de estado y flecha de navegación.

## Wireframe de baja fidelidad
Un wireframe de baja fidelidad reduce la pantalla a sus piezas esenciales. No define colores finales, iconografía definitiva ni detalles de espaciado fino. Trabaja con bloques simples para concentrarse en decisiones funcionales:

¿Dónde va el listado?
¿Dónde aparecen los filtros?
¿Qué botón es principal?
¿Qué datos son visibles?
¿Qué acción abre otra pantalla?
¿Qué error puede ver el usuario?

Esta baja fidelidad es una ventaja. Permite corregir barato. Es más fácil mover un rectángulo en una maqueta que rehacer una interfaz ya programada.

## Maqueta administrativa como scaffolding visual
En este contexto, la maqueta funciona como scaffolding visual: una estructura temporal para pensar. Ayuda a ordenar la pantalla antes de diseñarla o programarla.

Ejemplo:

Módulo Reparaciones
├── Listado de órdenes
├── Detalle de orden
├── Registrar diagnóstico
├── Registrar pago
└── Entregar equipo

Cada pantalla puede tener un wireframe. Ese conjunto no es todavía una aplicación real, pero sí una guía para construirla.

## Primitivas visuales básicas
Las primitivas son el alfabeto del wireframe administrativo.

![Primitivas visuales básicas para maquetar pantallas administrativas.](figure:admin-wireframe-primitives)

Rectángulo grande:

pantalla, ventana o contenedor principal

Rectángulo mediano:

panel, sección, tarjeta o área funcional

Rectángulo pequeño:

botón, campo, badge o acción simulada

Texto:

título, etiqueta, descripción o valor visible

Línea:

separador, borde o división de zonas

Tabla simulada:

listado administrativo con columnas y filas principales

Flecha:

navegación, continuidad o relación entre pantallas

Grupo:

bloque lógico de elementos relacionados

Modal:

rectángulo sobre una pantalla base para confirmación o acción puntual

## Pantalla, panel, sección y componente simulado
Una pantalla representa una vista completa de trabajo: listado de órdenes, crear cliente, detalle de pago, reporte mensual o configuración de usuarios.

Un panel agrupa información o acciones relacionadas. Por ejemplo:

Datos del cliente
Datos del equipo
Diagnóstico
Pagos
Historial
Documentos

Una sección es una parte interna de un panel o pantalla. Un componente simulado es una representación primitiva de algo que luego podría implementarse como campo, botón, tabla, tab, alerta o modal.

La guía debe evitar confundir componente simulado con componente real. Un botón dibujado en un wireframe no es todavía un botón JavaFX, HTML o Angular. Es una intención funcional.

## Diferencia con flujo de pantallas
El flujo de pantallas y los wireframes se complementan, pero no son lo mismo.

Flujo de pantallas:
indica qué pantallas existen y cómo se conectan.

Wireframe administrativo:
indica qué contiene cada pantalla y cómo se organiza internamente.

Ejemplo:

Flujo de pantallas:
Login → Dashboard → Reparaciones → Detalle de orden

Wireframes:
dibujan cómo se ve Dashboard, cómo se ve Reparaciones y cómo se ve Detalle de orden.

![Diferencia entre flujo de pantallas y wireframe administrativo.](figure:admin-wireframe-vs-screen-flow)

## Flechas entre pantallas como apoyo
Un wireframe puede usar flechas cuando varias maquetas aparecen juntas. Esto sirve para indicar navegación ligera o continuidad entre pantallas.

Ejemplo:

[Listado de órdenes] -- seleccionar orden --> [Detalle de orden]
[Detalle de orden] -- registrar pago --> [Formulario de pago]

![Uso de flechas entre maquetas como apoyo al flujo de pantallas.](figure:admin-wireframe-screen-linking)

Pero hay una regla importante: si el objetivo principal es estudiar rutas completas, conviene usar Flujo de pantallas. Si el objetivo es estudiar el contenido interno de cada pantalla, conviene usar Wireframes administrativos.

Las flechas en wireframes son apoyo, no reemplazo del Diagrama 7.

## Estructura mínima de una pantalla administrativa
Muchas pantallas administrativas tienen cinco zonas conceptuales:

1. Contexto
   título, módulo, estado, ruta o cabecera.

2. Área de trabajo
   tabla, formulario, detalle, dashboard o expediente.

3. Acciones
   guardar, cancelar, crear, editar, exportar, aprobar, anular.

4. Feedback
   errores, confirmaciones, estados vacíos, carga, bloqueo.

5. Navegación
   volver, abrir detalle, avanzar, cancelar, cerrar modal.

Ejemplo aplicado a una pantalla de órdenes:

Contexto:
Órdenes de reparación.

Área de trabajo:
tabla de órdenes.

Acciones:
Nueva orden, exportar, abrir detalle.

Feedback:
sin resultados, error al consultar, cargando.

Navegación:
volver al dashboard, abrir detalle, crear orden.

## Primeros errores comunes
Error 1: confundir wireframe con diseño visual final.

Un wireframe no decide la estética final. Decide estructura y comportamiento visible.

Error 2: confundir wireframe con flujo de pantallas.

El flujo conecta pantallas. El wireframe organiza el contenido de una pantalla.

Error 3: dibujar pantallas sin propósito.

Una pantalla debe responder qué tarea ayuda a completar.

Error 4: hacer cajas bonitas pero sin relación con datos reales.

Si el wireframe no se conecta con datos, permisos o procesos, se vuelve decoración.

Error 5: no indicar acción principal.

Toda pantalla administrativa debería dejar claro cuál es la acción dominante.

Error 6: no considerar errores o estados vacíos.

No hay datos, no hay permisos, error de validación y confirmación también forman parte de la experiencia.

## Principios UX/UI aplicados a wireframes administrativos
Un wireframe administrativo no es solamente una colección de rectángulos. También debe reflejar criterios básicos de UX/UI aplicados al trabajo real del usuario. En este contexto, UX significa experiencia de usuario: qué tan claro, seguro y fluido resulta completar una tarea. UI significa interfaz de usuario: cómo se organizan visualmente los elementos que permiten realizar esa tarea.

En una maqueta de baja fidelidad no se decide todavía la estética final, pero sí se toman decisiones importantes: qué aparece primero, qué se agrupa, qué acción se destaca, dónde se muestra un error, qué se oculta hasta que haga falta y qué acciones peligrosas se separan para evitar accidentes.

Los principios siguientes se pueden aplicar usando primitivas simples: rectángulos, paneles, campos, tablas simuladas, botones, separadores, tabs, modales, mensajes y flechas.

## Claridad y propósito de pantalla
La pantalla debe decir rápidamente qué es, para qué sirve y qué espera del usuario. Un listado de órdenes, un formulario de pago o una pantalla de configuración no deberían sentirse iguales ni ambiguos.

Una pantalla clara suele tener:

un título visible
una acción principal reconocible
un área de trabajo dominante
textos cortos de contexto
acciones secundarias separadas

Ejemplo:

Pantalla: Órdenes de reparación
Acción principal: Nueva orden
Área principal: tabla de órdenes
Apoyo: filtros por estado, técnico y fecha

Si el usuario no puede responder “¿qué hago aquí?” en pocos segundos, el wireframe necesita simplificación.

## Jerarquía visual
La jerarquía visual indica qué debe mirar primero el usuario, qué es secundario y qué es excepcional. Aunque la maqueta sea primitiva, el tamaño y la posición de los rectángulos ya comunican importancia.

![Jerarquía visual: título, filtros, acción principal y tabla dominante.](figure:admin-wireframe-hierarchy)

Regla práctica:

Lo frecuente y seguro debe estar más visible.
Lo secundario debe acompañar sin competir.
Lo peligroso debe estar separado.

En un listado de órdenes, la tabla debe dominar. El botón “Nueva orden” debe verse rápido. En cambio, “Anular” no debería competir visualmente con “Ver detalle” o “Registrar pago”.

## Agrupación, proximidad y alineación
Los elementos relacionados deben estar juntos. Los elementos no relacionados deben separarse. Esto reduce esfuerzo mental porque el usuario no tiene que reconstruir la lógica de la pantalla.

![Agrupación de campos por secciones administrativas.](figure:admin-wireframe-grouping)

Ejemplo para crear una orden:

Datos del cliente:
nombre, cédula, teléfono

Datos del equipo:
marca, modelo, IMEI, accesorios

Problema reportado:
descripción, observaciones

La alineación también importa. Campos y etiquetas desordenados hacen que una pantalla administrativa se sienta poco confiable, especialmente cuando hay muchos datos.

Mal:

Nombre:  ______
      Cédula: ______
Teléfono:      ______

Mejor:

Nombre:    ______
Cédula:    ______
Teléfono:  ______

## Consistencia visual y funcional
Si dos pantallas hacen tareas parecidas, deberían organizarse de forma parecida. La consistencia reduce aprendizaje, errores y cansancio.

Ejemplo: listados de clientes, órdenes y pagos pueden compartir una estructura base:

título
filtros
tabla
acción principal
acciones por fila
paginación
estado vacío

Esto no significa que todas las pantallas sean idénticas, sino que el usuario reconoce patrones. Si el botón principal aparece siempre en una zona predecible, el usuario no lo busca cada vez desde cero.

## Acciones primarias, secundarias y peligrosas
Toda pantalla debe tener clara su acción principal. Las acciones secundarias deben existir, pero sin robar atención. Las acciones peligrosas deben estar separadas y normalmente deben pedir confirmación.

![Acciones primarias, secundarias y peligrosas separadas.](figure:admin-wireframe-primary-secondary-actions)

Ejemplo:

Formulario de cliente
Acción primaria: Guardar cliente
Acciones secundarias: Cancelar, limpiar, volver
Acción peligrosa: Desactivar cliente

No conviene poner “Guardar”, “Eliminar”, “Cancelar” y “Exportar” con el mismo peso visual. Si todo parece igual, el usuario puede ejecutar una acción equivocada.

## Feedback, errores y validaciones
La interfaz debe responder a lo que el usuario hace. Un wireframe serio debe prever éxito, error, carga, bloqueo y ausencia de datos.

![Feedback y validación cerca del campo afectado.](figure:admin-wireframe-validation-feedback)

Ejemplo:

Después de registrar un pago:
Pago registrado correctamente.

Si el monto está mal:
El monto no puede superar el saldo pendiente.

Un error útil debe explicar:

qué pasó
por qué pasó
qué puede hacer el usuario

Mal:

Error.

Mejor:

No se puede registrar el pago porque el monto supera el saldo pendiente.
Corrige el monto o revisa los pagos anteriores.

## Visibilidad del estado
En sistemas administrativos, muchos registros tienen estado: recibido, pendiente, diagnosticado, pagado, entregado, anulado, vencido. Ese estado debe verse sin obligar al usuario a adivinar.

![Estado visible de un registro y acciones condicionadas por ese estado.](figure:admin-wireframe-state-visibility)

Ejemplo:

Orden #00045
Estado: En reparación

Acciones disponibles:
registrar avance, agregar observación

Acción bloqueada:
entregar equipo, porque falta pago

Esto conecta directamente con UML Estados y con roles/permisos. Una buena maqueta muestra qué se puede hacer en el estado actual y qué no.

## Prevención de errores y acciones delicadas
Una buena pantalla evita errores antes de que ocurran. En administración, las acciones delicadas suelen ser anular, cerrar, rechazar, eliminar, desactivar, aprobar o modificar valores críticos.

![Acción peligrosa separada, con confirmación y motivo.](figure:admin-wireframe-danger-action)

Con primitivas se puede representar:

zona de peligro
botón separado
modal de confirmación
campo de motivo
mensaje de advertencia

Ejemplo:

Anular orden
→ pedir motivo
→ confirmar
→ registrar usuario responsable

Mientras más irreversible o delicada sea la acción, más explícita debe ser la confirmación.

## Carga cognitiva y revelación progresiva
La carga cognitiva es el esfuerzo mental que exige una pantalla. Una maqueta sobrecargada obliga al usuario a pensar demasiado al mismo tiempo.

Para reducirla se pueden usar:

tabs
secciones
wizard
resumen lateral
bloques plegables
opciones avanzadas

![Revelación progresiva mediante resumen visible y pestañas de detalle.](figure:admin-wireframe-progressive-disclosure)

Ejemplo: crear una orden de reparación puede ser demasiado si se muestra todo junto:

cliente + equipo + problema + accesorios + diagnóstico + pago + entrega

Mejor dividir:

Paso 1: Cliente
Paso 2: Equipo
Paso 3: Problema
Paso 4: Accesorios
Paso 5: Resumen

La revelación progresiva consiste en mostrar primero lo necesario y dejar detalles avanzados para tabs, secciones o modales.

## Lectura rápida de tablas
Las tablas son centrales en sistemas administrativos. Deben permitir encontrar información rápido.

![Tabla administrativa con filtros, columnas principales, estado y acciones.](figure:admin-wireframe-table-reading)

Una tabla útil suele tener:

filtros arriba
columnas principales primero
estado visible
acciones por fila
paginación
ordenamiento
búsqueda

Ejemplo para órdenes:

Número | Cliente | Equipo | Estado | Técnico | Fecha | Acciones

No conviene iniciar con columnas internas que no ayudan al usuario:

idInterno | hash | flag | código técnico

## Accesibilidad básica
La accesibilidad también puede pensarse desde el wireframe. No hace falta resolver todos los detalles visuales, pero sí evitar errores de base.

Principios mínimos:

textos claros
no depender solo del color
botones con nombres entendibles
orden visual lógico
tamaños legibles
mensajes de error explícitos

Ejemplo: no basta con pintar una orden en rojo. También debe decir:

Pago pendiente
Orden vencida
Falta aprobación

## Navegación predecible
El usuario debe saber cómo volver, cancelar, continuar o cerrar. Esto se puede indicar con botones, breadcrumbs, tabs, flechas o controles de wizard.

Ejemplos:

Detalle de orden → Volver al listado
Crear orden → Cancelar → Listado de órdenes
Wizard → Atrás / Siguiente / Finalizar
Modal → Confirmar / Cancelar

Una navegación predecible reduce miedo y evita que el usuario sienta que puede “perderse” dentro del sistema.

## Resultado de aplicar UX/UI al wireframe
Al aplicar estos principios, el wireframe deja de ser una pantalla dibujada al azar y se convierte en una hipótesis razonada de interfaz. Todavía no es diseño final, pero ya expresa decisiones importantes:

qué ve primero el usuario
qué acción importa más
qué datos se agrupan
qué errores se previenen
qué estados se muestran
qué tareas se simplifican
qué acciones se separan por seguridad

## Patrones administrativos principales
Un wireframe administrativo no debería comenzar con la pregunta “¿qué cajas dibujo?”, sino con una pregunta más importante:

¿Qué tipo de trabajo administrativo representa esta pantalla?

No todas las pantallas sirven para lo mismo. Una pantalla para administrar clientes no tiene la misma estructura que una bandeja de pendientes, un expediente de reparación, un dashboard o un asistente paso a paso. Por eso, antes de dibujar campos y botones, conviene reconocer el patrón dominante de la pantalla.

Los patrones administrativos ayudan a elegir una estructura inicial razonable. No son moldes rígidos, pero evitan empezar desde cero cada vez. También ayudan a pedirle a una IA una pantalla más precisa: no es lo mismo pedir “haz una pantalla de órdenes” que pedir “haz un expediente de orden con cabecera de estado, tabs, historial, pagos y acciones según rol”.

![Patrones administrativos: cada pantalla responde a un tipo de trabajo distinto.](figure:admin-wireframe-crud-pattern)

## CRUD / catálogo
El patrón CRUD o catálogo sirve para administrar registros relativamente estables. CRUD significa crear, consultar, actualizar y eliminar, aunque en sistemas administrativos muchas veces “eliminar” debería reemplazarse por desactivar, archivar o anular.

Se usa para elementos como:

clientes
productos
repuestos
proveedores
técnicos
categorías
marcas
métodos de pago
catálogos de estado

Una pantalla de este tipo suele tener:

título del módulo
búsqueda
filtros simples
tabla principal
botón nuevo
acciones por fila
paginación
estado vacío
formulario crear/editar

Ejemplo:

Clientes
Buscar: [________] Estado: [Activo]
Tabla: nombre, cédula, teléfono, estado, acciones
Acciones: nuevo cliente, ver, editar, desactivar

El error común es tratar como CRUD algo que en realidad tiene ciclo de vida, historial o acciones por estado. Por ejemplo, una orden de reparación no debería verse solo como una tabla editable, porque tiene diagnóstico, pagos, estados, documentos, entrega y posiblemente garantía.

## Wizard / flujo guiado
El patrón wizard sirve cuando el usuario debe capturar información por etapas. Es útil cuando una sola pantalla con todos los campos produciría demasiado ruido o riesgo de error.

![Wizard administrativo: pasos visibles, avance, retroceso y resumen final.](figure:admin-wireframe-wizard-pattern)

Se usa para:

crear orden de reparación
registrar ingreso de equipo
configurar sistema por primera vez
crear cotización compleja
registrar compra con varios detalles

Estructura típica:

indicador de pasos
pantalla por etapa
botones atrás / siguiente
validación por paso
resumen final
confirmación

Ejemplo para crear una orden:

Paso 1: Cliente
Paso 2: Equipo
Paso 3: Problema reportado
Paso 4: Accesorios entregados
Paso 5: Resumen y confirmación

El error común es usar wizard para algo demasiado simple. Si solo se capturan tres campos, un formulario normal puede ser mejor. El wizard sirve cuando separar en pasos reduce carga cognitiva o previene errores.

## Expediente / caso vivo
El patrón expediente o caso vivo sirve cuando una entidad tiene estado, historial, documentos, responsables, acciones por etapa y varias secciones internas.

![Expediente administrativo: cabecera, estado, tabs, historial y acciones según avance.](figure:admin-wireframe-expediente-pattern)

Ejemplos:

orden de reparación
ticket de soporte
caso médico
solicitud administrativa
garantía
caso legal

Estructura típica:

cabecera con identificador
estado visible
resumen del caso
acciones según estado
tabs o secciones
historial
observaciones
documentos
pagos o movimientos

Ejemplo:

Orden #00045 · Estado: En reparación
Cliente: Juan Pérez · Equipo: iPhone 11
Tabs: Resumen | Diagnóstico | Pagos | Historial | Documentos
Acciones: registrar avance, registrar pago, marcar como lista, anular

Este patrón es clave para Reparaciones. Una orden no es solo un registro: es un caso que vive, cambia y deja trazabilidad.

## Bandeja / cola operativa
La bandeja o cola operativa sirve para gestionar trabajo pendiente. No solo muestra datos: ayuda a priorizar y actuar.

![Bandeja operativa: pendientes, filtros rápidos, detalle lateral y acciones inmediatas.](figure:admin-wireframe-bandeja-pattern)

Se usa para:

órdenes pendientes de diagnóstico
pagos por confirmar
garantías por revisar
solicitudes por aprobar
tickets sin asignar
tareas vencidas

Estructura típica:

filtros rápidos
lista de pendientes
prioridad
estado
responsable
detalle lateral o preview
acciones rápidas

Ejemplo:

Bandeja de diagnóstico pendiente
Filtros: hoy, urgentes, sin técnico
Lista: #0045 iPhone 11, #0046 Samsung A32
Detalle lateral: cliente, problema, fecha, acciones asignar/abrir

El error común es usar una tabla genérica cuando el usuario necesita priorizar, seleccionar y actuar rápido.

## Dashboard
El dashboard sirve para resumen, orientación y monitoreo. No reemplaza los módulos operativos; ayuda a ver situación general y alertas.

![Dashboard administrativo: tarjetas resumen, alertas y accesos rápidos.](figure:admin-wireframe-dashboard-pattern)

Se usa para:

ventas del día
órdenes pendientes
pagos pendientes
stock bajo
reparaciones atrasadas
entregas próximas

Estructura típica:

tarjetas resumen
alertas
listas cortas
grÁficos simples si aportan valor
accesos rápidos
filtros de periodo

Ejemplo:

Órdenes pendientes: 12
Listas para entrega: 4
Pagos hoy: $340
Stock bajo: 5
Alertas: orden atrasada, repuesto agotado

El error común es llenar el dashboard con métricas bonitas pero irrelevantes para la operación diaria.

## Configuración
El patrón configuración sirve para parámetros que se cambian con poca frecuencia.

![Configuración: menú lateral de secciones y formulario de parámetros del sistema.](figure:admin-wireframe-config-pattern)

Ejemplos:

usuarios
roles
permisos
catálogos
sucursales
datos del negocio
formatos de comprobante
preferencias

Estructura típica:

menú lateral de secciones
formularios pequeños
tablas de parámetros
botón guardar cambios
advertencias para cambios delicados

El error común es mezclar configuración crítica con operación diaria. Lo que se cambia poco debe estar separado para no distraer ni poner en riesgo el trabajo cotidiano.

## Aprobación / revisión
El patrón aprobación o revisión sirve cuando una acción requiere validación humana antes de continuar.

![Pantalla de aprobación: datos del caso, motivo, evidencia y acciones aprobar/rechazar.](figure:admin-wireframe-approval-pattern)

Ejemplos:

aprobar descuento
aprobar cotización
revisar garantía
autorizar anulación
validar pago
aprobar compra

Estructura típica:

datos del caso
motivo
comparación antes/después
evidencia o documentos
historial de revisión
botones aprobar / rechazar / pedir información

El error común es poner botones de aprobar y rechazar sin contexto suficiente. Si alguien debe decidir, la pantalla debe darle información para decidir bien.

## Reportes
El patrón reportes sirve para consultar información agregada, filtrada o exportable.

![Reporte administrativo: filtros, resumen, tabla o gráfico y exportación.](figure:admin-wireframe-report-pattern)

Ejemplos:

ventas mensuales
reparaciones por técnico
pagos pendientes
inventario bajo
historial de cliente
utilidad por periodo

Estructura típica:

filtros de periodo
filtros por módulo
resumen
tabla o gráfico
fecha de generación
botones exportar / imprimir

El error común es hacer reportes sin filtros claros, sin periodo visible o sin indicar qué significan los datos.

## Búsqueda especializada
La búsqueda especializada sirve cuando buscar no es solo escribir una palabra. Requiere criterios combinados.

![Búsqueda especializada: filtros avanzados, criterios activos y resultados.](figure:admin-wireframe-search-pattern)

Ejemplos:

buscar orden por IMEI
buscar cliente por cédula
buscar equipo por serie
buscar pago por comprobante
buscar historial por rango de fechas y estado

Estructura típica:

panel de filtros avanzados
criterios activos
resultados
acciones sobre resultado
estado sin resultados
botón limpiar filtros

El error común es mostrar demasiados filtros sin agruparlos o esconder los filtros que realmente usa el negocio.

## Documental
El patrón documental sirve cuando el sistema administra archivos, evidencias o documentos relacionados con un caso.

![Patrón documental: lista de documentos, tipo, fecha, responsable y acciones.](figure:admin-wireframe-document-pattern)

Ejemplos:

fotos del equipo
comprobantes
contratos
garantías
reportes PDF
documentos del cliente

Estructura típica:

lista de documentos
tipo
fecha
responsable
vista previa
subir archivo
descargar
eliminar o anular con permiso

El error común es tratar documentos como notas sueltas. Un documento administrativo necesita tipo, origen, fecha, responsable y permisos.

## Agenda / calendario
El patrón agenda o calendario sirve cuando el tiempo y la planificación son centrales.

![Agenda administrativa: eventos por día, responsable, estado y acciones de reprogramación.](figure:admin-wireframe-calendar-pattern)

Ejemplos:

citas
entregas programadas
visitas técnicas
turnos
recordatorios
seguimiento de reparaciones

Estructura típica:

vista día / semana / mes
eventos
estado
responsable
filtros
detalle de evento
acciones reprogramar / cancelar

El error común es usar calendario cuando una simple lista ordenada por fecha sería suficiente. El calendario conviene cuando la distribución temporal importa visualmente.

## Cómo decidir qué patrón usar
Una regla práctica:

Si administras registros simples, usa CRUD / catálogo.
Si capturas información por etapas, usa wizard.
Si una entidad tiene estado, historial y varias secciones, usa expediente.
Si gestionas pendientes, usa bandeja.
Si necesitas resumen y alertas, usa dashboard.
Si ajustas parámetros, usa configuración.
Si alguien debe autorizar, usa aprobación / revisión.
Si analizas datos, usa reportes.
Si necesitas filtrar con precisión, usa búsqueda especializada.
Si manejas archivos o evidencias, usa documental.
Si el tiempo manda, usa agenda / calendario.

También puede haber combinaciones. Por ejemplo, el módulo Reparaciones puede usar listado, bandeja, expediente, wizard, reportes y documentos. El patrón dominante depende de la tarea concreta.

## Microejemplo completo: módulo Reparaciones
El ejemplo central para estudiar wireframes administrativos puede ser el módulo Reparaciones de una tienda de celulares. Este módulo no debe pensarse como un CRUD simple, porque una orden de reparación tiene estado, historial, pagos, diagnóstico, documentos, responsables y acciones permitidas según su avance.

Reparaciones es un expediente o caso vivo.
La orden no solo se crea y se edita: avanza, cambia de estado, recibe pagos, acumula evidencias y puede terminar entregada, anulada o rechazada.

Pantallas mínimas del módulo:

Listado de órdenes
Detalle de orden
Crear orden
Registrar diagnóstico
Registrar pago
Entregar equipo
Historial de orden
Documentos / evidencias

![Ejemplo integrado del módulo Reparaciones con listado, detalle y acciones principales.](figure:admin-wireframe-repair-module-example)

## Listado de órdenes
El listado permite encontrar y abrir órdenes de reparación. Debe priorizar búsqueda, filtros y lectura rápida.

Debe incluir:

Título: Órdenes de reparación
Filtros: estado, técnico, fecha, cliente, número de orden
Tabla: número, cliente, equipo, estado, técnico, fecha de ingreso, acciones
Acciones generales: Nueva orden, exportar, limpiar filtros
Acciones por fila: ver detalle, registrar pago rápido si aplica
Estados: cargando, sin resultados, error al consultar

El error común es tratar el listado como una tabla plana sin estado ni prioridad. En un negocio real, el usuario necesita saber rápido qué está pendiente, qué está atrasado, qué está listo para entrega y qué requiere acción.

## Detalle de orden
El detalle de orden es el corazón del módulo. Debe mostrar contexto, estado, secciones y acciones relevantes.

![Detalle de orden como expediente: cabecera, estado, pestañas, pagos, historial y documentos.](figure:admin-wireframe-order-detail-example)

Estructura recomendada:

Cabecera:
Orden #00045
Estado actual
Cliente
Equipo
Fecha de ingreso

Acciones principales:
Registrar diagnóstico
Registrar pago
Marcar como lista
Entregar equipo

Acción peligrosa:
Anular orden

Tabs:
Resumen
Diagnóstico
Pagos
Historial
Documentos
Observaciones

La cabecera debe responder: qué orden es, de quién es, en qué estado está y qué se puede hacer ahora. Las pestañas evitan una pantalla interminable y ayudan a separar información de distinta naturaleza.

## Crear orden como wizard
Crear una orden puede requerir demasiados datos para una sola pantalla. Por eso puede funcionar mejor como wizard.

Paso 1: Cliente
Paso 2: Equipo
Paso 3: Problema reportado
Paso 4: Accesorios entregados
Paso 5: Resumen y confirmación

Este patrón reduce carga cognitiva y permite validar por etapas. También ayuda cuando recepción está atendiendo rápido y no debe perderse entre campos técnicos que todavía no corresponden.

## Acciones según rol y estado
Un wireframe administrativo debe considerar que no todos los botones aparecen para todos los usuarios ni en todos los estados.

![Acciones visibles según rol y estado: recepción crea, técnico diagnostica, caja cobra, supervisor anula.](figure:admin-wireframe-role-state-actions)

Ejemplo:

Recepción:
crear orden, editar datos iniciales, entregar equipo.

Técnico:
registrar diagnóstico, registrar avance, marcar como lista.

Caja:
registrar pago, imprimir comprobante, revisar saldo.

Supervisor:
anular orden, autorizar descuentos, revisar auditoría.

También cambia por estado:

Orden recibida:
permite editar datos iniciales y asignar técnico.

Orden diagnosticada:
permite aprobar, rechazar o registrar pago.

Orden lista para entrega:
permite entregar solo si las condiciones se cumplen.

Orden entregada:
normalmente solo permite consulta, impresión e historial.

## Estados vacíos, errores y carga
Una maqueta administrativa profesional no solo dibuja el caso feliz.

![Estados vacíos y errores recuperables en pantallas administrativas.](figure:admin-wireframe-empty-error-states)

Debe considerar:

No hay órdenes registradas.
No se encontraron resultados con esos filtros.
Cargando órdenes.
Error al consultar el servidor.
No tienes permiso para anular esta orden.
El monto supera el saldo pendiente.

Un buen estado vacío orienta al usuario. No debe decir solo “sin datos”, sino explicar qué ocurre y qué puede hacer.

Ejemplo:

No hay órdenes pendientes de diagnóstico.
Cuando recepción registre una nueva orden, aparecerá aquí.

## Cómo construir un wireframe administrativo paso a paso
Una receta práctica:

1. Identifica el módulo.
2. Define qué trabajo hará el usuario en ese módulo.
3. Decide el patrón principal de pantalla.
4. Lista las pantallas necesarias.
5. Define la acción principal de cada pantalla.
6. Identifica datos visibles.
7. Identifica filtros, campos, tablas o secciones.
8. Identifica errores, estados vacíos y confirmaciones.
9. Marca acciones peligrosas.
10. Conecta pantallas con flechas solo cuando ayuden.
11. Revisa permisos y estados.
12. Simplifica antes de programar.

Ejemplo aplicado:

Módulo: Reparaciones
Patrón dominante: expediente / caso vivo
Patrones secundarios: listado, bandeja, wizard, documental, reportes
Pantallas: listado, detalle, crear orden, diagnóstico, pago, entrega
Acción principal del listado: abrir orden o crear nueva
Acción principal del detalle: avanzar la orden según estado

## Preparar especificaciones para IA o frontend
El wireframe puede convertirse en una especificación textual para pedir ayuda a una IA o a un desarrollador.

![De wireframe a especificación: pantalla, datos, acciones, permisos, estados y feedback.](figure:admin-wireframe-ai-specification)

Una especificación mínima debe incluir:

Nombre de pantalla
Propósito
Patrón usado
Datos visibles
Acción principal
Acciones secundarias
Acciones peligrosas
Estados vacíos
Errores esperados
Permisos relevantes
Pantallas relacionadas

Esto evita pedir “hazme una pantalla bonita” y ayuda a pedir una interfaz con sentido operativo.

## Casos especiales finales
Un wireframe administrativo debe considerar situaciones que no siempre aparecen en la primera versión de una pantalla, pero que suelen aparecer en un sistema real. Estos casos especiales ayudan a evitar diseños incompletos.

Pantalla por rol:
una misma pantalla puede mostrar acciones diferentes para Recepción, Técnico, Caja, Supervisor o Administrador. El wireframe debe indicar qué zonas dependen del rol cuando eso afecte la operación.

Pantalla por estado:
una orden Recibida, Diagnosticada, En reparación, Lista para entrega o Entregada no debería mostrar exactamente las mismas acciones. El estado actual debe aparecer visible y las acciones permitidas deben tener sentido con ese estado.

Estado vacío:
una lista sin datos no debe verse rota. Debe explicar qué ocurre y qué puede hacer el usuario: crear un registro, limpiar filtros o revisar permisos.

Error recuperable:
si una acción falla, el mensaje debe decir qué pasó, por qué pasó y qué puede hacer el usuario. Un simple “Error” no alcanza para una pantalla administrativa.

Acción peligrosa:
anular, eliminar, rechazar, cerrar, desactivar o borrar debe aparecer separado de las acciones normales. Puede requerir confirmación, motivo, permiso especial y registro de auditoría.

Pantalla sobrecargada:
si una pantalla acumula demasiados campos, conviene dividirla con tabs, secciones, panel lateral, wizard o resumen. La maqueta debe servir para reducir carga cognitiva, no para esconder desorden.

Datos sensibles:
si aparecen cédulas, teléfonos, datos financieros, documentos o información privada, el wireframe debe sugerir visibilidad controlada, permisos o agrupación cuidadosa.

## Errores comunes finales
Confundir wireframe con diseño final:
el wireframe no define colores finales, branding completo ni frontend real. Define estructura, prioridad, relación entre datos y acciones.

Dibujar pantallas bonitas pero inútiles:
una maqueta puede verse ordenada y aun así no resolver el trabajo del usuario. Primero debe existir propósito operativo.

No definir acción principal:
toda pantalla debe dejar claro qué acción domina: crear, guardar, buscar, revisar, aprobar, pagar, entregar o consultar.

Mezclar demasiadas responsabilidades:
si una pantalla intenta crear, diagnosticar, cobrar, entregar, reportar y configurar al mismo tiempo, probablemente necesita dividirse.

Ignorar estados vacíos y errores:
las pantallas reales también se usan cuando no hay datos, cuando falla una consulta o cuando el usuario no tiene permiso.

Poner acciones peligrosas junto a acciones normales:
Anular, Eliminar o Rechazar no deben competir visualmente con Guardar o Ver detalle.

Olvidar permisos:
un botón puede ser correcto para Administrador y peligroso para Recepción. El wireframe debe dialogar con Roles y permisos.

Tratar expedientes vivos como CRUD simples:
una orden de reparación con estado, historial, pagos, documentos y responsables no es solo una tabla con crear, editar y borrar.

Diseñar antes de entender datos y procesos:
si no se conoce el modelo conceptual, el diccionario de datos, el flujo operativo y los permisos, el wireframe puede inventar pantallas que no corresponden al negocio.

## Relaciones y lectura
Un wireframe se lee preguntando qué tarea resuelve la pantalla, qué información aparece primero, qué datos se agrupan, qué acción domina, qué acciones quedan como secundarias y qué ocurre si hay error, vacío, carga o bloqueo por permisos. Cuando varias maquetas se conectan con flechas, esas flechas se leen como navegación o continuidad de trabajo, no como proceso de negocio completo.

## Casos especiales
Hay casos especiales cuando una pantalla cambia por rol, por estado del registro, por permisos, por datos sensibles o por una acción peligrosa. También son especiales los estados vacíos, errores recuperables, modales de confirmación, dashboards con alertas, expedientes con historial y wizards que dividen captura compleja en pasos.

## Cuándo usarlo
Úsalo cuando necesites pensar una pantalla administrativa antes de programarla, comparar alternativas de estructura, explicar una interfaz a un cliente, preparar prompts para una IA o conectar datos, procesos, permisos y estados con una experiencia visual concreta.

## Cuándo no usarlo
No lo uses como reemplazo de diseño visual final, Figma, frontend real, HTML, JavaFX, responsive detallado, pruebas con usuarios, BPMN, flujo operativo o flujo de pantallas. El wireframe ayuda a estructurar la interfaz, pero no demuestra que la interfaz ya esté lista.

## Errores comunes
Errores comunes: confundir wireframe con diseño final, dibujar pantallas bonitas pero inútiles, no definir acción principal, mezclar demasiadas responsabilidades, ignorar estados vacíos, no mostrar errores, poner acciones peligrosas junto a acciones normales, olvidar permisos, tratar expedientes vivos como CRUD simples y diseñar pantallas antes de entender datos y procesos.

## Relación con otros diagramas
Modelo conceptual

El modelo conceptual indica qué entidades aparecen en la pantalla.

Cliente, Equipo, OrdenReparacion, Pago, Técnico, Repuesto, Documento

En el wireframe, esos conceptos se transforman en cabeceras, campos, tablas, tarjetas, secciones y pestañas.

Diccionario de datos

El diccionario de datos indica qué campos aparecen, cuáles son obligatorios, qué validaciones tienen y cuáles son sensibles.

telefonoCliente
imeiEquipo
estadoOrden
montoPago
fechaRecepcion
motivoAnulacion

Esto ayuda a decidir campos, mensajes de error, restricciones y datos que no deberían mostrarse a todos.

Mapa de módulos

El mapa de módulos ayuda a agrupar pantallas.

Clientes
Reparaciones
Pagos
Inventario
Reportes
Configuración

Cada módulo puede tener una familia de pantallas y uno o varios patrones dominantes.

Roles y permisos

Los permisos indican qué botones, acciones o secciones debe ver cada rol.

Recepción puede crear orden.
Técnico puede registrar diagnóstico.
Caja puede registrar pago.
Supervisor puede anular.

Esto afecta directamente el wireframe: no todos ven lo mismo.

Flujo operativo y BPMN

El flujo operativo y BPMN muestran el trabajo real que la pantalla debe apoyar.

Cliente entrega equipo → Recepción registra orden → Técnico diagnostica → Caja cobra → Recepción entrega.

Cada paso humano puede necesitar una pantalla, acción, confirmación o estado visible.

Flujo de pantallas

El flujo de pantallas indica cómo se conectan las pantallas. El wireframe indica qué contiene cada pantalla.

Flujo de pantallas:
Listado de órdenes → Detalle de orden → Registrar pago

Wireframe:
qué datos, botones, mensajes y secciones tiene cada pantalla.

C4 y despliegue

C4 no diseña pantallas, pero indica en qué aplicación viven.

Aplicación desktop administrativa
Frontend web
Portal del cliente
App móvil para técnicos

Cada contenedor de interfaz puede necesitar wireframes diferentes.

UML Casos de uso

Cada caso de uso puede necesitar una o varias pantallas.

Registrar pago
→ pantalla de pago
→ validación
→ confirmación

UML Estados

Los estados determinan qué acciones se muestran o bloquean.

Si la orden está Entregada, no debería mostrar Registrar diagnóstico.
Si está Lista para entrega pero falta pago, Entregar equipo puede aparecer bloqueado con explicación.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

A partir de estos módulos, propón las pantallas administrativas necesarias y clasifícalas por patrón: CRUD, expediente, bandeja, dashboard, wizard, reporte o configuración.

Diseña un wireframe textual de baja fidelidad para el módulo Reparaciones de una tienda de celulares. Incluye listado, detalle, diagnóstico, pagos, historial y documentos.

Revisa este wireframe y detecta problemas de jerarquía visual, carga cognitiva, acciones peligrosas, estados vacíos y errores no considerados.

A partir de este diccionario de datos, indica qué campos deben aparecer en el formulario de creación de orden. Separa campos obligatorios, opcionales, calculados y sensibles.

A partir de estos roles y permisos, indica qué botones debería ver Recepción, Técnico, Caja, Supervisor y Administrador.

Convierte este flujo operativo en una propuesta de pantallas y wireframes administrativos. No inventes reglas fuera del proceso dado.

Propón una versión simple para MVP y una versión futura más completa del módulo Reparaciones.

Genera una especificación para frontend a partir de este wireframe administrativo, incluyendo propósito, datos visibles, acciones, validaciones, estados vacíos, errores y permisos.

## Ficha rápida
Wireframes administrativos

Representan:
estructura interna de pantallas administrativas, módulos visuales, formularios, listados, tablas simuladas, paneles, acciones, feedback y navegación ligera.

No representan:
diseño visual final, Figma completo, frontend real, HTML, JavaFX real, responsive definitivo ni lógica backend.

Sirven para:
pensar la interfaz antes de programar, reducir ambigüedad, comunicar pantallas, preparar prompts para IA y conectar datos, procesos, permisos y estados con la experiencia del usuario.

Primitivas:
rectángulos, texto, campos, botones, tablas simuladas, paneles, tabs, modales, flechas, separadores, badges y tarjetas.

Principios clave:
claridad, jerarquía visual, agrupación, consistencia, feedback, prevención de errores, carga cognitiva, accesibilidad básica y navegación predecible.

Patrones:
CRUD, wizard, expediente, bandeja, dashboard, configuración, aprobación, reportes, búsqueda, documental y agenda.

Pregunta clave:
¿Qué debe ver y hacer el usuario en esta pantalla para completar su trabajo con claridad y seguridad?

## Auditoría de frontera teórica

Wireframes administrativos pertenece a la arquitectura de interacción de la interfaz. Su foco es la estructura interna de pantallas administrativas, no el proceso completo del negocio ni el diseño visual final.

Si estás decidiendo qué pantallas existen y cómo se conectan, usa flujo de pantallas. Si estás decidiendo qué contiene una pantalla, qué acción principal tiene, cómo agrupar campos, cómo mostrar errores y qué patrón administrativo usar, estás en wireframes. Si estás decidiendo colores finales, iconografía comercial, responsive detallado o componentes reales de frontend, ya saliste del nivel de baja fidelidad.

Las flechas entre wireframes son útiles como apoyo, pero no deben convertir este capítulo en una copia del flujo de pantallas. Aquí lo central es la composición visual y operativa de cada pantalla.

## Checklist final de estudio
- Puedo distinguir wireframe administrativo de diseño final, frontend real y flujo de pantallas.
- Puedo aplicar claridad, jerarquía visual, agrupación, feedback y prevención de errores.
- Puedo escoger patrones como CRUD, wizard, expediente, bandeja, dashboard o reporte.
- Puedo conectar campos, acciones y estados con datos, permisos y procesos.
- Puedo preparar una especificación visual de baja fidelidad para pedir frontend o maquetas a una IA.

## Referencias / base teórica
Este capítulo se apoya en principios generales de diseño de interacción, arquitectura de información, usabilidad administrativa, patrones de aplicaciones empresariales y modelado progresivo de sistemas. Dentro de Domain Model Studio, debe leerse junto con Modelo conceptual, Diccionario de datos, Mapa de módulos, Roles y permisos, Flujo operativo, BPMN básico, Flujo de pantallas y UML Estados.
