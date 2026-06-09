# Mapa de módulos administrativos

## Introducción
Un mapa de módulos administrativos ayuda a ordenar un sistema empresarial antes de diseñar pantallas, tablas, clases o despliegues. Su objetivo es responder una pregunta muy práctica: ¿en qué partes funcionales se divide el sistema para que el negocio pueda trabajar mejor?

En sistemas administrativos reales, el problema no suele ser solamente programar pantallas. El problema inicial es entender qué áreas de trabajo existen, qué responsabilidades tiene cada una, qué información usan, qué procesos atraviesan y qué partes deben quedar para una primera versión. Si esa división no está clara, el sistema crece como una mezcla de formularios, menús, botones, reportes y tablas sin una arquitectura funcional entendible.

El mapa de módulos sirve como puente entre el lenguaje del cliente y el diseño del sistema. Permite decir: este sistema tendrá un módulo de clientes, un módulo de reparaciones, un módulo de pagos, un módulo de inventario, un módulo de reportes y un módulo de usuarios y permisos. Luego cada módulo podrá tener submódulos, funciones, pantallas, datos, permisos y procesos asociados.

![Mapa general de módulos alrededor del sistema administrativo.](figure:admin-module-overview)

La idea no es dibujar un menú final ni una carpeta de código. La idea es crear una vista funcional del sistema: qué zonas tiene, para qué sirve cada una y cómo se relacionan.

## Pregunta que responde
El mapa de módulos responde:

¿Qué partes funcionales necesita el sistema y cómo se agrupan según el trabajo real del negocio?

Ejemplo para una tienda de reparación de celulares:

Clientes
Equipos
Reparaciones
Diagnóstico técnico
Pagos
Inventario
Garantías
Reportes
Usuarios y permisos
Configuración

Cada uno de esos nombres representa una zona de responsabilidad. El mapa ayuda a decidir qué entra en la primera versión, qué depende de qué, qué puede esperar y qué debe quedar claro antes de programar.

## Idea central
La idea central es:

Un módulo no es una pantalla, una tabla ni una carpeta de código.
Un módulo es una zona funcional del sistema con una responsabilidad reconocible para el negocio.

Por ejemplo, el módulo Reparaciones puede contener recepción de equipos, diagnóstico técnico, cotización, aprobación del cliente, seguimiento, entrega y garantías. Eso no es una sola pantalla ni una sola tabla. Es un área funcional completa.

Un buen módulo debe tener un propósito claro. Debe ser posible explicarlo con una frase:

Clientes: administra la información de las personas atendidas por el negocio.
Reparaciones: controla el ciclo de vida de las órdenes de reparación.
Pagos: registra abonos, saldos, comprobantes y cancelaciones.
Inventario: controla repuestos, productos, entradas, salidas y stock.

Si no se puede explicar qué responsabilidad tiene un módulo, probablemente está mal definido.

## Qué es
Un mapa de módulos administrativos es una vista funcional del sistema que organiza sus áreas de trabajo en módulos, submódulos y funciones reconocibles por el negocio.

## Para qué sirve
Sirve para conversar alcance, prioridad y fases con el cliente sin confundir todavía pantallas, tablas o carpetas de código.

## Qué representa
Representa:

módulos funcionales
submódulos
funciones principales
dependencias entre áreas
fases de implementación
módulos núcleo
módulos de soporte
módulos transversales
alcance del sistema

También puede ayudar a distinguir qué pertenece al MVP y qué queda para una versión futura.

## Qué no representa
No representa:

pantallas finales
menú exacto de la aplicación
tablas de base de datos
clases de programación
paquetes Java o TypeScript
servidores
endpoints
wireframes detallados

Puede inspirar esas cosas, pero no las reemplaza.

Ejemplo:

Módulo: Clientes

no significa automáticamente:

pantalla Clientes
tabla clientes
paquete /clientes
endpoint /clientes

Puede terminar conectado con todas esas piezas, pero el mapa de módulos está en el nivel funcional.

## Elementos principales
Sus elementos principales son módulo, submódulo, función, dependencia, fase, módulo núcleo, módulo transversal y módulo futuro.

## Qué es un módulo administrativo
Un módulo administrativo es una agrupación de funciones relacionadas con una responsabilidad del negocio.

Ejemplo:

Módulo Inventario

puede incluir:

productos
repuestos
categorías
stock
movimientos
entradas
salidas
ajustes
alertas
proveedores

El módulo existe porque el negocio necesita controlar existencias. No existe porque haya una tabla o una pantalla bonita. Primero está la responsabilidad funcional.

Un módulo normalmente se reconoce porque:

tiene usuarios interesados
tiene datos principales
tiene procesos asociados
tiene pantallas probables
tiene permisos propios
tiene reportes o consultas
puede planificarse por fase

## Módulo, submódulo y función
La guía debe separar tres niveles.

Módulo:
Reparaciones

Submódulos:
- Recepción
- Diagnóstico
- Cotización
- Seguimiento
- Entrega
- Garantías

Funciones:
- Crear orden de reparación
- Registrar diagnóstico
- Aprobar cotización
- Cambiar estado
- Registrar entrega
- Consultar historial

![Jerarquía entre módulo, submódulo y función.](figure:admin-module-hierarchy)

Un error común es llamar módulo a cualquier cosa pequeña. Por ejemplo:

Módulo: botón guardar
Módulo: campo cédula
Módulo: tabla clientes

Eso no ayuda. Un botón es una acción de interfaz. Un campo es un dato. Una tabla es una estructura de almacenamiento. Un módulo es una zona funcional del sistema.

## Diferencia entre módulo y pantalla
Un módulo puede tener varias pantallas.

Ejemplo:

Módulo Clientes

puede tener:

Listado de clientes
Formulario de creación
Formulario de edición
Detalle de cliente
Historial de cliente
Reportes de clientes

La pantalla es una vista de interfaz. El módulo es una responsabilidad funcional.

Una pantalla pertenece a un módulo, pero un módulo no se reduce a una pantalla.

![Diferencia entre módulo funcional y pantalla de interfaz.](figure:admin-module-vs-screen)

Esto es importante porque si el análisis empieza por pantallas, el sistema puede parecer completo visualmente, pero quedar mal organizado funcionalmente.

## Diferencia entre módulo y entidad
A veces un módulo y una entidad comparten nombre, pero no son lo mismo.

Ejemplo:

Entidad: Cliente
Módulo: Clientes

La entidad Cliente representa un concepto del dominio. El módulo Clientes representa el área funcional encargada de registrar, consultar, editar y analizar clientes.

Un módulo puede manejar varias entidades:

Módulo Clientes
- Cliente
- ContactoCliente
- DireccionCliente
- HistorialCliente

Y una entidad puede aparecer en varios módulos:

Cliente aparece en:
- Clientes
- Ventas
- Reparaciones
- Pagos
- Reportes

Por eso el mapa de módulos debe dialogar con el modelo conceptual, pero no copiarlo mecánicamente.

## Diferencia entre módulo y paquete de código
Un módulo funcional tampoco es automáticamente una carpeta de código.

Ejemplo funcional:

Módulo Inventario

Posible estructura técnica posterior:

inventory/domain
inventory/application
inventory/infrastructure
inventory/presentation

El mapa de módulos puede inspirar arquitectura, pero no debe confundirse con ella. Primero se piensa la responsabilidad del negocio; después se decide cómo organizar el código.

## Módulos núcleo, secundarios y transversales
No todos los módulos tienen el mismo peso.

En una tienda de reparación de celulares, módulos núcleo podrían ser:

Clientes
Equipos
Reparaciones
Pagos

porque sostienen la operación diaria.

Módulos secundarios o de crecimiento podrían ser:

Inventario avanzado
Garantías
Reportes avanzados
Marketing
Notificaciones
BI

Módulos transversales son los que atraviesan a varios módulos:

Usuarios y permisos
Auditoría
Configuración
Catálogos
Reportes
Notificaciones

![Módulos núcleo, soporte y transversales dentro del sistema.](figure:admin-module-transversal)

Los módulos transversales suelen olvidarse porque no siempre aparecen en la conversación inicial con el cliente, pero son importantes para un sistema serio.

## Módulos por fase: MVP, versión inicial y futuro
Un mapa de módulos también sirve para no prometer todo de golpe.

Ejemplo:

MVP:
- Clientes
- Equipos
- Reparaciones
- Pagos básicos

Versión 1:
- Inventario básico
- Garantías
- Reportes operativos

Versión futura:
- Facturación electrónica
- Notificaciones automáticas
- BI
- IA para informes

MVP significa producto mínimo viable: lo mínimo que permite resolver el problema central sin ahogarse en alcance. No significa sistema mal hecho. Significa versión inicial controlada.

![Módulos organizados por MVP, versión inicial y futuro.](figure:admin-module-phases)

La fase ayuda a negociar con el cliente, planificar entregas y evitar sistemas eternos que nunca llegan a una versión usable.

## Dependencias entre módulos
Los módulos pueden depender unos de otros.

Ejemplo:

Reparaciones depende de Clientes.
Reparaciones depende de Equipos.
Reparaciones puede depender de Inventario si usa repuestos.
Pagos depende de Reparaciones si cobra órdenes.
Reportes depende de varios módulos.

![Dependencias funcionales entre módulos administrativos.](figure:admin-module-dependencies)

Una dependencia no significa necesariamente una dependencia técnica de código. Significa que, funcionalmente, un módulo necesita información o resultados de otro.

Si Reparaciones necesita registrar el cliente, entonces Clientes debe existir de alguna forma. Si Pagos necesita consultar saldo de una orden, entonces Reparaciones y Pagos deben estar coordinados.

## Relaciones y lectura
Se lee como una red de responsabilidades: Reparaciones depende de Clientes, Equipos, Inventario y Pagos; Reportes consume datos de varios módulos.

## Cómo leer un mapa de módulos
Un mapa de módulos debe leerse como una explicación funcional del sistema:

El sistema tiene un módulo de Clientes para registrar personas atendidas.
El módulo de Reparaciones usa Clientes y Equipos para crear órdenes.
El módulo de Pagos registra abonos asociados a órdenes.
El módulo de Reportes consulta información de Reparaciones, Pagos e Inventario.
Usuarios y permisos controla el acceso transversal.

Si el mapa no puede leerse así, puede estar demasiado técnico, demasiado visual o demasiado ambiguo.

## Cómo construirlo paso a paso
Una receta práctica:

1. Escuchar qué áreas de trabajo menciona el negocio.
2. Listar funcionalidades sin ordenarlas todavía.
3. Agrupar funcionalidades relacionadas.
4. Nombrar cada grupo como módulo candidato.
5. Separar módulos núcleo, soporte y transversales.
6. Detectar submódulos y funciones principales.
7. Marcar dependencias funcionales.
8. Separar MVP, versión inicial y futuro.
9. Revisar si algún módulo está demasiado grande.
10. Revisar si algún módulo es realmente una pantalla, tabla o botón.
11. Validar el mapa con ejemplos reales de trabajo.

Ejemplo de validación:

Un cliente llega con un celular dañado.
Recepción registra cliente y equipo.
Se crea una orden.
Técnico diagnostica.
Caja registra pago.
El dueño consulta reporte.

Ese caso debería recorrer módulos claros.

## Microejemplo administrativo: tienda de reparación de celulares
Mapa inicial:

Sistema administrativo de reparaciones y ventas

Módulos núcleo:
- Clientes
- Equipos
- Reparaciones
- Pagos

Módulos de soporte:
- Inventario
- Garantías
- Reportes

Módulos transversales:
- Usuarios y permisos
- Configuración
- Catálogos
- Auditoría

Submódulos de Reparaciones:

Recepción
Diagnóstico
Cotización
Seguimiento
Entrega
Garantías

Funciones de Pagos:

Registrar pago
Consultar saldo
Anular pago
Imprimir comprobante
Ver historial de pagos

Dependencias:

Reparaciones usa Clientes y Equipos.
Reparaciones puede usar Inventario.
Pagos usa Reparaciones.
Reportes usa Reparaciones, Pagos e Inventario.
Usuarios y permisos atraviesa todos los módulos.

Este mapa ayuda a conversar con el cliente sin decir todavía qué framework, base de datos o patrón de arquitectura se usará.

## Casos especiales
Módulos transversales

Usuarios, permisos, auditoría, configuración y reportes suelen atravesar todo el sistema. No siempre son módulos de negocio puros, pero sí deben existir como áreas funcionales claras.

Módulos demasiado grandes

Mal ejemplo:

Administración general

Ese nombre puede esconder clientes, ventas, pagos, inventario, usuarios y reportes. Conviene dividirlo.

Módulos demasiado pequeños

Mal ejemplo:

Botón guardar
Filtro por fecha
Campo teléfono

Eso no merece nivel de módulo.

Módulos que pueden empezar juntos y separarse luego

Ejemplo:

Pagos y Caja

En un negocio pequeño pueden estar juntos al inicio. En una versión más seria podrían separarse en pagos, caja, cuentas por cobrar, cierres y conciliación.

Módulos futuros

Algunas ideas son valiosas, pero no para la primera versión:

Facturación electrónica
BI
IA local para informes
notificaciones automáticas
integración con proveedores

El mapa permite dejarlas visibles sin comprometer el MVP.

## Cuándo usarlo
Úsalo al inicio de un sistema administrativo para partir el alcance, definir MVP y ordenar conversaciones con cliente o equipo.

## Cuándo no usarlo
No lo uses para reemplazar flujo de pantallas, modelo conceptual, permisos detallados ni arquitectura técnica.

## Errores comunes
Confundir módulo con pantalla

Mal:

Formulario de cliente

Mejor:

Clientes

Confundir módulo con tabla

Mal:

tabla_ordenes

Mejor:

Reparaciones

Crear módulos genéricos

Mal:

Gestión
Administración
Procesos
Datos

Mejor:

Clientes
Inventario
Pagos
Reportes

No separar MVP de futuro

Si todo entra en primera versión, el proyecto se vuelve inmanejable.

Olvidar módulos transversales

Usuarios, permisos, catálogos, auditoría y configuración parecen aburridos, pero sostienen la operación profesional.

Copiar módulos de otro sistema sin entender el negocio

Un restaurante, una óptica, una tienda de celulares y una cooperativa de taxis no necesitan exactamente el mismo mapa.

![Errores comunes al confundir módulo con pantalla, tabla o botón.](figure:admin-module-common-errors)

## Relación con otros diagramas
Modelo conceptual:
ayuda a descubrir entidades que pueden agruparse por módulo.

Diccionario de datos:
documenta los datos principales usados por cada módulo.

Roles y permisos:
define qué roles acceden a cada módulo y qué acciones pueden realizar.

Flujo operativo:
muestra cómo el trabajo real atraviesa varios módulos.

BPMN:
formaliza procesos importantes entre módulos y responsables.

Flujo de pantallas:
convierte módulos en recorridos de navegación.

Wireframes:
bajan cada módulo a pantallas, formularios, listados y dashboards.

C4 Contenedores:
puede convertir algunas responsabilidades en piezas técnicas, pero no equivale uno a uno al mapa funcional.

UML Casos de uso:
detalla funcionalidades ofrecidas por cada módulo.

UML Clases:
puede organizar clases alrededor de responsabilidades funcionales.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

A partir de esta descripción del negocio, identifica los módulos administrativos principales, submódulos y funciones.

Organiza estos requerimientos en un mapa de módulos por fases: MVP, versión inicial y versión futura.

Revisa este mapa de módulos y detecta módulos demasiado grandes, demasiado pequeños o mezclados.

A partir de este modelo conceptual, sugiere módulos funcionales para un sistema administrativo.

Diferencia qué partes deberían ser módulos, submódulos, pantallas o funciones.

Propón un mapa de módulos para una tienda de reparación de celulares con clientes, equipos, reparaciones, pagos, inventario y reportes.

Clasifica estos módulos como núcleo, soporte, transversal o futuro.

## Ficha rápida
Mapa de módulos administrativos

Representa:
áreas funcionales, submódulos, funciones, dependencias y fases.

No representa:
pantallas finales, tablas, clases, endpoints ni carpetas de código.

Sirve para:
ordenar alcance, conversar con cliente y planificar entregas.

Pregunta clave:
¿Qué partes funcionales necesita el sistema?

Error clásico:
confundir módulo con pantalla, tabla o menú.

Buena práctica:
separar módulos núcleo, soporte, transversales y futuros.

## Auditoría de frontera teórica

El mapa de módulos pertenece a la organización funcional del sistema. Su tarea es agrupar capacidades del negocio, no dibujar pantallas, tablas ni paquetes de código.

Si estás preguntando qué áreas funcionales tendrá el sistema, estás en mapa de módulos. Si preguntas quién puede ejecutar cada acción, estás en roles y permisos. Si preguntas qué pantallas concretas tiene cada módulo, estás en flujo de pantallas o wireframes. Si preguntas qué clases o carpetas tendrá el proyecto, estás en diseño técnico, no en mapa de módulos.

Un módulo puede inspirar una familia de pantallas y varias entidades, pero no se reduce a ninguna de ellas. Reparaciones puede contener órdenes, diagnósticos, estados, pagos, reportes y acciones; por eso no debe confundirse con una sola pantalla llamada Reparaciones.

## Checklist final de estudio
- Puedo identificar módulos, submódulos y funciones sin confundirlos con pantallas o tablas.
- Puedo separar módulos núcleo, secundarios y transversales.
- Puedo marcar dependencias funcionales entre módulos.
- Puedo proponer qué entra en MVP y qué queda para fases futuras.
- Puedo usar el mapa para conversar alcance con cliente o equipo.

## Referencias / base teórica
El mapa de módulos administrativos no es una notación formal universal como UML o BPMN. En Domain Model Studio se usa como herramienta práctica de análisis funcional para sistemas empresariales. Se apoya en principios de levantamiento de requerimientos, modularización funcional, separación de responsabilidades, planificación por fases y diseño incremental de productos administrativos.
