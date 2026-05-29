# Flujo de pantallas

## Introducción
El flujo de pantallas es una herramienta para entender cómo una persona se mueve dentro de una aplicación. No describe el trabajo del negocio en sí, sino el recorrido que el usuario hace en la interfaz para consultar, registrar, corregir, confirmar o revisar información.

En un sistema administrativo, esta diferencia es importante. El flujo operativo puede decir que recepción registra una orden, el técnico diagnostica y caja cobra. El flujo de pantallas pregunta otra cosa: ¿desde qué pantalla se registra la orden?, ¿cómo se llega al detalle?, ¿qué pasa si el usuario cancela?, ¿qué pantalla aparece después de guardar?, ¿qué ve un técnico y qué ve caja?

Por eso, el flujo de pantallas es el puente entre el análisis funcional y las maquetas o wireframes. Primero se sabe qué pantallas existen y cómo se conectan. Luego, en otro nivel, se dibuja qué contiene cada pantalla.

## Pregunta que responde
El flujo de pantallas responde:

- qué pantallas existen en el sistema;
- cómo se conectan entre sí;
- qué acción provoca cada salto;
- qué caminos siguen distintos roles;
- qué ocurre cuando hay error, cancelación, confirmación o permiso insuficiente.

Ejemplo simple:

- Login.
- Dashboard.
- Listado de órdenes.
- Detalle de orden.
- Registrar diagnóstico.
- Confirmación.

La pregunta no es cómo está diseñada visualmente cada pantalla. Eso corresponde al wireframe. Tampoco es cómo trabaja el negocio fuera del sistema. Eso corresponde al flujo operativo o BPMN.

## Idea central
La idea central es:

- el flujo de pantallas representa navegación;
- cada nodo es una pantalla, modal, diálogo o estado de interfaz;
- cada flecha debe indicar una acción, evento o condición;
- el diagrama debe ayudar a saber cómo llega el usuario a cada parte del sistema.

Una flecha sin explicación es débil. Una flecha con acción clara convierte el diagrama en una guía real de uso.

Ejemplo:

- Listado de órdenes → Detalle de orden: seleccionar orden.
- Detalle de orden → Registrar pago: clic en Registrar pago.
- Formulario de pago → Confirmación: guardar correctamente.
- Formulario de pago → Formulario con errores: datos inválidos.

![Flujo básico entre pantallas principales.](figure:screen-flow-basic)

## Qué es
Un flujo de pantallas representa las pantallas de una aplicación y las transiciones de navegación entre ellas.

## Para qué sirve
Sirve para planificar recorridos de usuario, entradas, salidas, errores, confirmaciones y caminos por rol.

## Qué representa
Representa pantallas y recorridos de interfaz:

- login;
- dashboard;
- menú principal;
- listados;
- formularios;
- detalles;
- reportes;
- modales;
- confirmaciones;
- pantallas de error;
- estados vacíos;
- retornos y cancelaciones.

También puede representar variaciones por rol. Un administrador puede entrar a usuarios, configuración y reportes. Un técnico quizá entra directamente a órdenes asignadas. Caja puede ver pagos pendientes y comprobantes. Recepción puede crear clientes y órdenes.

## Qué no representa
No representa:

- procesos de negocio completos;
- estructura de datos;
- clases de software;
- diseño visual final;
- colores, tipografía o branding;
- layout detallado de botones y campos;
- llamadas entre backend y base de datos.

Ejemplo: “Cliente entrega equipo” no es una pantalla. Es un paso del negocio. Una pantalla sería “Crear orden de reparación” o “Recepción de equipo”.

## Elementos principales
Sus elementos principales son pantalla, modal, diálogo, transición, acción de navegación, condición, retorno y error.

## Qué es una pantalla
Una pantalla es una vista reconocible de la aplicación donde el usuario puede consultar, capturar o actuar sobre información.

Ejemplos:

- Login.
- Dashboard.
- Listado de clientes.
- Crear cliente.
- Detalle de orden.
- Registrar diagnóstico.
- Registrar pago.
- Reporte de reparaciones.
- Configuración de usuarios.

Una pantalla debe tener un propósito claro. Si no se puede explicar para qué existe, quizá no debería estar en el flujo.

## Pantallas principales y secundarias
No todas las pantallas tienen el mismo peso.

Las pantallas principales suelen ser puntos de trabajo estables:

- dashboard;
- listado de clientes;
- listado de órdenes;
- reportes;
- configuración.

Las pantallas secundarias apoyan una acción concreta:

- crear cliente;
- editar orden;
- registrar pago;
- confirmar anulación;
- ver historial;
- mostrar error.

Esta distinción ayuda a evitar diagramas desordenados. Un módulo como Reparaciones puede tener una pantalla principal de listado y varias pantallas secundarias para detalle, diagnóstico, pago, garantía o historial.

## Transiciones de navegación
Una transición es la flecha que indica el paso de una pantalla a otra. Toda transición debería tener una causa.

Buenas etiquetas:

- iniciar sesión;
- seleccionar orden;
- clic en Nuevo cliente;
- guardar correctamente;
- cancelar;
- error de validación;
- permiso insuficiente;
- sesión expirada.

Malas etiquetas:

- ir;
- conectar;
- siguiente;
- relación.

La transición debe explicar qué hizo el usuario o qué ocurrió en la interfaz.

![Pantalla con varias salidas según acciones del usuario.](figure:screen-flow-branching)

## Acciones que provocan navegación
Las acciones más comunes son:

- clic en botón;
- selección de una fila;
- envío de formulario;
- cancelación;
- confirmación;
- cierre de modal;
- cambio de pestaña;
- selección de menú;
- error del sistema;
- redirección por permisos.

En un sistema administrativo, muchas transiciones nacen desde tablas y formularios. Por ejemplo, desde el listado de órdenes se puede crear una orden nueva, abrir el detalle de una orden existente, exportar resultados o volver al dashboard.

## Pantallas por módulo
El flujo de pantallas debe conectarse con el mapa de módulos.

Ejemplo:

- Módulo Clientes: listado, crear cliente, editar cliente, detalle, historial.
- Módulo Reparaciones: listado de órdenes, crear orden, detalle, diagnóstico, pago, entrega.
- Módulo Inventario: listado de productos, movimientos, ajuste de stock, alerta de mínimos.
- Módulo Reportes: filtros, vista previa, exportación.

Agrupar por módulo reduce la sensación de maraña. El usuario entiende que no todas las pantallas están al mismo nivel, sino que pertenecen a áreas funcionales del sistema.

## Pantallas por rol
Distintos roles pueden recorrer caminos distintos.

Ejemplo:

- Recepción: Dashboard → Reparaciones → Crear orden.
- Técnico: Dashboard → Órdenes asignadas → Registrar diagnóstico.
- Caja: Dashboard → Pagos pendientes → Registrar pago.
- Administrador: Dashboard → Usuarios → Permisos → Reportes.

![Recorridos distintos para recepción, técnico y caja.](figure:screen-flow-by-role)

Este enfoque ayuda a detectar pantallas innecesarias, permisos mal definidos o recorridos demasiado largos para un rol que trabaja todos los días con el sistema.

## Errores, cancelaciones y retornos
Un flujo profesional no muestra solo el camino feliz. También debe considerar:

- cancelar un formulario;
- volver al listado;
- mostrar errores de validación;
- bloquear una acción por permiso insuficiente;
- mostrar pantalla vacía cuando no hay datos;
- confirmar acciones peligrosas;
- manejar sesión expirada.

Ejemplo:

- Formulario de cliente → Confirmación: datos válidos.
- Formulario de cliente → Formulario con errores: cédula inválida.
- Formulario de cliente → Listado: cancelar.

![Flujo con validación, error y confirmación.](figure:screen-flow-error-validation)

## Modales, diálogos y confirmaciones
Un modal o diálogo es una interacción temporal encima de una pantalla base. Puede servir para confirmar, seleccionar, advertir o capturar datos breves.

Ejemplos:

- confirmar anulación de orden;
- registrar pago rápido;
- seleccionar cliente;
- mostrar error;
- confirmar cierre de caja.

No todo modal debe convertirse en pantalla completa. Pero si el modal tiene muchos campos, pasos o reglas, quizá conviene convertirlo en pantalla secundaria.

## Diferencia con flujo operativo
El flujo operativo representa pasos reales del trabajo.

Ejemplo:

- Cliente entrega equipo.
- Recepción registra datos.
- Técnico diagnostica.
- Caja cobra.

El flujo de pantallas representa navegación.

Ejemplo:

- Login.
- Dashboard.
- Crear orden.
- Detalle de orden.
- Registrar pago.

Ambos se relacionan, pero no son lo mismo.

## Diferencia con BPMN
BPMN modela procesos de negocio con una notación más formal: eventos, tareas, compuertas, lanes y finales.

El flujo de pantallas no intenta reemplazar BPMN. Solo muestra cómo el usuario recorre la interfaz para ejecutar parte de esos procesos.

Ejemplo: en BPMN puede existir la tarea “Registrar pago”. En flujo de pantallas esa tarea puede involucrar “Pagos pendientes → Formulario de pago → Confirmación”.

## Diferencia con wireframes
El flujo de pantallas responde: qué pantallas existen y cómo se conectan.

El wireframe responde: qué hay dentro de cada pantalla.

![Comparación entre navegación y contenido interno de pantalla.](figure:screen-flow-vs-wireframe)

Ejemplo:

- Flujo: Listado de clientes → Crear cliente → Confirmación.
- Wireframe: dentro de Crear cliente hay campos de nombre, cédula, teléfono, dirección y botones Guardar/Cancelar.

## Relaciones y lectura
Se lee como navegación: Login lleva a Dashboard, Dashboard lleva a Reparaciones, y Detalle de orden puede llevar a Registrar pago.

## Cómo leer un flujo de pantallas
Para leerlo, conviene convertir las flechas en frases:

- El usuario inicia sesión y llega al dashboard.
- Desde el dashboard entra al módulo Reparaciones.
- En el listado selecciona una orden.
- Desde el detalle registra diagnóstico o pago.
- Si guarda correctamente, ve confirmación.
- Si hay error, vuelve al formulario con mensajes.

Si el diagrama no se puede leer así, probablemente tiene flechas mudas o pantallas mal nombradas.

## Cómo construirlo paso a paso
Una receta práctica:

1. Identificar módulos principales.
2. Para cada módulo, listar pantallas principales.
3. Agregar pantallas secundarias necesarias.
4. Conectar pantallas con acciones claras.
5. Agregar retornos, cancelaciones y confirmaciones.
6. Agregar errores importantes.
7. Revisar recorridos por rol.
8. Agrupar visualmente por módulo.
9. Quitar pantallas redundantes.
10. Validar el recorrido con un caso real.

Caso real de prueba:

- Recepción crea una orden.
- Técnico registra diagnóstico.
- Caja registra pago.
- Recepción entrega equipo.

El flujo debe permitir recorrer ese escenario sin saltos absurdos.

## Microejemplo administrativo
Para una tienda de reparación de celulares:

- Login.
- Dashboard.
- Reparaciones.
- Listado de órdenes.
- Crear orden.
- Detalle de orden.
- Registrar diagnóstico.
- Registrar pago.
- Confirmación.
- Entrega de equipo.

Lectura:

- Recepción entra al dashboard.
- Abre Reparaciones.
- Crea una orden nueva.
- Luego puede abrir el detalle.
- El técnico registra diagnóstico desde el detalle o desde órdenes asignadas.
- Caja registra pago.
- Al final se confirma la entrega.

## Casos especiales
Dashboard como punto de entrada

El dashboard puede enviar al usuario a varios módulos. No debe convertirse en una pantalla mágica que hace todo. Debe orientar.

Pantalla de detalle con muchas acciones

Una pantalla de detalle puede tener varias salidas: editar, pagar, diagnosticar, imprimir, anular o ver historial. Hay que ordenar esas salidas para que el flujo no se vuelva caótico.

Estados vacíos

Si no hay órdenes pendientes, el usuario debe ver un estado vacío. Ese estado puede tener una acción: Crear primera orden.

Permiso insuficiente

Si un rol intenta entrar a una pantalla prohibida, el flujo puede mostrar bloqueo, redirección o mensaje. Esto conecta con roles y permisos.

## Cuándo usarlo
Úsalo cuando ya conoces módulos o casos de uso y quieres diseñar cómo los recorrerá el usuario.

## Cuándo no usarlo
No lo uses para reemplazar BPMN, flujo operativo, wireframes detallados, clases o base de datos.

## Errores comunes
- Confundir pantalla con paso operativo.
- Dibujar pantallas sin acción de navegación.
- Olvidar cancelar, volver o confirmar.
- No considerar errores de validación.
- No distinguir pantalla, modal y estado.
- Dibujar demasiados detalles internos de la pantalla.
- No agrupar por módulo.
- No revisar recorridos por rol.
- Convertir el flujo en un mapa enorme sin jerarquía.

![Errores comunes al mezclar proceso, pantalla y wireframe.](figure:screen-flow-common-errors)

## Relación con otros diagramas
- Mapa de módulos: ayuda a agrupar pantallas por área funcional.
- Roles y permisos: define qué pantallas o acciones ve cada rol.
- Flujo operativo: indica qué trabajo necesita apoyo de interfaz.
- BPMN: muestra procesos donde las pantallas apoyan tareas humanas.
- Wireframes: detallan el contenido interno de cada pantalla.
- Modelo conceptual: aporta entidades que suelen aparecer como listados, detalles o formularios.
- Diccionario de datos: define campos de formularios, filtros y tablas.
- UML Casos de uso: cada caso de uso puede convertirse en uno o varios recorridos.
- UML Secuencia: detalla qué ocurre técnicamente cuando el usuario ejecuta una acción.
- UML Estados: condiciona acciones visibles según el estado de un registro.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

- A partir de estos módulos, propón un flujo de pantallas para un sistema administrativo.
- Convierte este flujo operativo en un flujo de pantallas, separando pantallas principales, modales y confirmaciones.
- Revisa este flujo de pantallas y detecta flechas sin acción, pantallas faltantes y caminos sin retorno.
- Propón el flujo de pantallas para recepción, técnico, caja y administrador.
- A partir de este diccionario de datos, sugiere qué formularios, listados y detalles necesita el sistema.
- Diferencia qué parte corresponde a flujo de pantallas y qué parte debería ir como wireframe.

## Ficha rápida
Flujo de pantallas.

Representa: pantallas, modales, confirmaciones, errores y navegación.

No representa: proceso de negocio completo, estructura de datos, código ni diseño visual final.

Sirve para: ordenar recorridos de usuario antes de diseñar wireframes o programar interfaz.

Error clásico: conectar pantallas sin nombrar la acción que produce la transición.

Pregunta clave: ¿cómo se mueve el usuario por la aplicación para lograr una tarea?


## Auditoría de frontera teórica

El flujo de pantallas pertenece a la navegación de la interfaz. Responde qué pantallas existen, cómo se conectan y qué acción lleva de una a otra.

Si estás describiendo el procedimiento real del negocio, estás en flujo operativo o BPMN. Si estás dibujando qué contiene una pantalla por dentro, estás en wireframes administrativos. Si estás indicando que Listado de órdenes lleva a Detalle de orden y luego a Registrar pago, estás en flujo de pantallas.

Una buena frontera práctica es esta: el flujo de pantallas dibuja rutas; el wireframe dibuja composición interna. La ruta dice a dónde vas. El wireframe dice qué ves y qué puedes hacer cuando llegas.

## Checklist final de estudio
- Puedo identificar pantallas principales, secundarias, modales y confirmaciones.
- Puedo representar navegación sin mezclarla con proceso de negocio.
- Puedo etiquetar transiciones con acciones claras.
- Puedo considerar errores, cancelaciones, retornos y acceso por rol.
- Puedo usarlo como puente hacia wireframes administrativos.

