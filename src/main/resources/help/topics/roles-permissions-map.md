# Roles y permisos

## Introducción
Roles y permisos es el diagrama que ayuda a responder una de las preguntas más delicadas de cualquier sistema administrativo: quién puede hacer qué. No basta con que el sistema tenga clientes, órdenes, pagos, reportes y usuarios. También debe existir una regla clara para decidir qué persona puede entrar a cada módulo, qué acciones puede ejecutar y qué información debe quedar protegida.

En un negocio pequeño, este tema suele parecer secundario porque todos se conocen y muchas decisiones se toman de palabra. Pero cuando el sistema empieza a registrar pagos, anulaciones, reportes financieros, usuarios, descuentos, estados de órdenes o información de clientes, los permisos se vuelven parte del diseño del negocio. Una aplicación que permite que cualquier usuario edite pagos, anule órdenes o vea reportes sensibles termina generando riesgo operativo, pérdida de trazabilidad y conflictos de responsabilidad.

Este diagrama no es un organigrama completo de la empresa. Tampoco es una lista de usuarios reales. Su objetivo es más preciso: representar perfiles funcionales, permisos concretos y restricciones que el sistema debe respetar. Por eso se relaciona directamente con módulos, pantallas, datos sensibles, estados de entidades, auditoría y reglas de backend.

## Pregunta que responde
El diagrama responde:

¿Quién puede hacer qué dentro del sistema,
sobre qué módulo, recurso o dato,
y bajo qué condiciones?

Ejemplo en una tienda de reparación de celulares:

Recepción puede registrar clientes y crear órdenes.
Técnico puede registrar diagnósticos y avances.
Caja puede registrar pagos.
Supervisor puede anular órdenes con motivo.
Administrador puede gestionar usuarios, roles y permisos.

Esta pregunta parece simple, pero define seguridad funcional, diseño de pantallas, reglas del backend y trazabilidad humana.

## Idea central
La idea central es:

Un rol representa una responsabilidad dentro del negocio.
Un permiso representa una acción autorizada sobre una parte del sistema.

Un usuario concreto puede tener uno o varios roles. Cada rol agrupa permisos. Cada permiso autoriza una acción sobre un recurso. El recurso puede ser un módulo, una entidad, una pantalla, un reporte, una operación o incluso un dato sensible.

![Cadena usuario, rol, permiso y recurso.](figure:roles-permissions-chain)

Una forma simple de leerlo es:

Ana tiene el rol Recepción.
Recepción tiene el permiso reparaciones.crear.
Ese permiso permite crear órdenes de reparación.

## Qué es
Un mapa de roles y permisos define quién puede ejecutar acciones sobre módulos, datos o estados del sistema.

## Para qué sirve
Sirve para transformar responsabilidades del negocio en autorizaciones claras, auditables y revisables.

## Qué representa
Representa:

usuarios o perfiles de uso
roles funcionales
permisos
acciones protegidas
recursos del sistema
matriz rol-permiso
restricciones por estado, sucursal o dato
operaciones críticas
auditoría asociada

Ejemplos de roles:

Administrador
Recepción
Técnico
Caja
Supervisor
Gerencia
Inventario

Ejemplos de permisos:

clientes.ver
clientes.crear
reparaciones.crear
reparaciones.registrarDiagnostico
pagos.registrar
pagos.anular
reportes.financieros.ver
usuarios.administrar

## Qué no representa
No representa necesariamente:

organigrama completo de la empresa
nombres de empleados reales
contratos laborales
jerarquía humana total
flujo del proceso
pantallas detalladas
clases de código
endpoints técnicos

Puede relacionarse con esas cosas, pero no las reemplaza. El rol del sistema debe venir de la responsabilidad operativa dentro de la aplicación, no solo del cargo formal escrito en una empresa.

## Elementos principales
Sus elementos principales son usuario, rol, permiso, acción, recurso, matriz rol-permiso, alcance, estado y auditoría.

## Usuario, rol, permiso, acción y recurso
Un usuario es una cuenta concreta que entra al sistema.

Ejemplos:

ana.recepcion
carlos.tecnico
maria.caja
admin

Un rol agrupa permisos según una responsabilidad funcional.

Ejemplos:

Recepción
Técnico
Caja
Supervisor
Administrador

Un permiso es una autorización concreta. Conviene nombrarlo como recurso más acción:

clientes.crear
reparaciones.ver
pagos.anular
usuarios.administrar

La acción indica qué se puede hacer:

ver
crear
editar
anular
eliminar
aprobar
exportar
imprimir
configurar
administrar

El recurso indica sobre qué parte se aplica:

clientes
reparaciones
pagos
reportes
usuarios
inventario
ordenes

![Relación entre usuario, rol, permiso y acción autorizada.](figure:roles-permissions-user-role-permission)

## Rol del negocio vs cargo laboral
Un rol del sistema no siempre coincide exactamente con un cargo laboral. Una persona puede trabajar como asistente general y, dentro del sistema, tener permisos de recepción y caja. El dueño del negocio puede tener rol de gerencia, supervisor y administrador del sistema. Un técnico senior puede tener permisos adicionales para cerrar garantías o aprobar diagnósticos especiales.

Por eso es mejor definir roles por responsabilidad operativa:

Recepción: registra clientes, equipos y órdenes.
Técnico: diagnostica y actualiza reparaciones.
Caja: registra pagos y comprobantes.
Supervisor: revisa operaciones críticas y anulaciones.
Administrador: configura usuarios, roles y catálogos.

Malos nombres de rol:

Usuario normal
Usuario especial
Rol 1
Empleado
Super usuario parcial

Buenos nombres:

Recepción
Técnico de reparación
Caja
Supervisor de sucursal
Administrador del sistema

## Permiso por módulo
Un permiso por módulo indica si un rol puede entrar o trabajar dentro de un área funcional.

Ejemplo:

Recepción puede entrar al módulo Reparaciones.
Técnico puede entrar a Órdenes asignadas.
Caja puede entrar a Pagos.
Gerencia puede entrar a Reportes.

Pero tener acceso a un módulo no significa poder hacer todo dentro de él. Un usuario puede ver reparaciones, pero no anularlas. Puede ver pagos, pero no modificarlos. Puede abrir reportes, pero no exportar información financiera.

## Permiso por acción
Los permisos por acción son más precisos. Separan operaciones que no deberían mezclarse.

Ejemplo en el módulo Reparaciones:

reparaciones.ver
reparaciones.crear
reparaciones.editarDatosIniciales
reparaciones.asignarTecnico
reparaciones.registrarDiagnostico
reparaciones.cambiarEstado
reparaciones.anular
reparaciones.entregar

![Permisos concretos dentro del módulo Reparaciones.](figure:roles-permissions-module-actions)

Esta separación evita frases vagas como:

Recepción puede manejar reparaciones.

Es mejor decir:

Recepción puede crear órdenes y editar datos iniciales,
pero no puede registrar diagnóstico técnico ni anular una orden entregada.

## Permiso por dato
Algunos permisos no se aplican solo a módulos o acciones, sino a datos específicos.

Ejemplos de datos sensibles:

costos internos
utilidad
reportes financieros
cédula del cliente
teléfono
dirección
historial de anulaciones
descuentos especiales

Un técnico puede necesitar ver el problema del equipo y el diagnóstico, pero quizá no necesita ver la ganancia de la orden. Caja puede registrar pagos, pero quizá no debe modificar el diagnóstico. Gerencia puede ver reportes financieros, pero recepción no.

Esto conecta directamente con el diccionario de datos. Si el diccionario marca un dato como sensible, la matriz de permisos debe indicar quién puede verlo, editarlo o exportarlo.

## Matriz rol-permiso
La matriz rol-permiso es una tabla que cruza roles con permisos.

![Matriz rol-permiso con roles y acciones protegidas.](figure:roles-permissions-matrix)

Ejemplo conceptual:

Permiso                            Admin   Recepción   Técnico   Caja
clientes.ver                       Sí      Sí          No        Sí
clientes.crear                     Sí      Sí          No        No
reparaciones.crear                 Sí      Sí          No        No
reparaciones.registrarDiagnostico  Sí      No          Sí        No
pagos.registrar                    Sí      No          No        Sí
pagos.anular                       Sí      No          No        No
reportes.financieros.ver           Sí      No          No        No
usuarios.administrar               Sí      No          No        No

La matriz obliga a tomar decisiones explícitas. También sirve para revisar con el cliente si hay permisos demasiado amplios o faltantes.

## Permisos críticos
No todos los permisos tienen el mismo riesgo. Algunos deben tratarse como críticos:

anular pago
anular orden
eliminar usuario
modificar precio
aplicar descuento alto
cerrar caja
exportar reporte financiero
cambiar permisos
reabrir orden cerrada

Estos permisos pueden requerir:

rol especial
motivo obligatorio
auditoría
confirmación
restricción por estado
autorización de supervisor

En sistemas administrativos, muchas veces es mejor anular que eliminar. Eliminar borra la evidencia. Anular conserva el registro y permite explicar qué ocurrió.

## Permisos por estado
Algunas acciones dependen del estado del registro. No basta con saber el rol.

Ejemplo:

Una orden Recibida puede editarse por Recepción.
Una orden En diagnóstico puede actualizarse por Técnico.
Una orden Entregada ya no debería editarse normalmente.
Una orden Anulada solo debería consultarse o auditarse.

![Permisos que cambian según el estado de la orden.](figure:roles-permissions-state-based)

Esto se puede expresar como:

Rol + acción + estado del registro = autorización final

Ejemplo:

Técnico puede registrar diagnóstico solo si la orden está En diagnóstico.
Caja puede registrar pago si la orden no está Anulada.
Supervisor puede anular si la orden no está Entregada o si existe regla especial.

## Permisos por sucursal o alcance
En negocios con varias sucursales aparece otro factor: el alcance.

Ejemplo:

Recepción de Sucursal Norte solo ve órdenes de Sucursal Norte.
Gerencia puede ver todas las sucursales.
Caja solo registra pagos de su sucursal.
Administrador puede gestionar usuarios de todas las sucursales.

Esto no se resuelve solo con rol. Se necesita combinar:

usuario + rol + sucursal + permiso

En sistemas pequeños quizá no haga falta desde el inicio, pero conviene documentarlo si el negocio tiene varias sedes o planea crecer.

## Auditoría y trazabilidad
Los permisos críticos deben conectarse con auditoría.

Auditoría significa registrar quién hizo qué, cuándo y por qué.

Ejemplos:

usuarioResponsable
fechaHora
acción realizada
registro afectado
estado anterior
estado nuevo
motivo
observación

Esto es muy importante para:

anulaciones
cambios de estado
pagos
descuentos
modificación de datos sensibles
cierre de caja
cambios de permisos

Sin auditoría, el sistema puede permitir acciones, pero no deja responsabilidad humana clara.

## Relaciones y lectura
Se lee relacionando roles con permisos: Recepción puede crear órdenes, Técnico puede registrar diagnósticos y Caja puede registrar pagos.

## Cómo leer una matriz de permisos
Una matriz se lee preguntando:

¿Qué rol está en la columna?
¿Qué permiso está en la fila?
¿Tiene autorización?
¿Hay condición adicional?
¿El permiso aplica siempre o depende del estado?
¿El permiso afecta datos sensibles?

Ejemplo:

Fila: pagos.anular
Columna: Caja
Valor: No

Lectura:

Caja puede registrar pagos, pero no puede anularlos.
La anulación queda reservada para Administrador o Supervisor.

## Cómo construirla paso a paso
Receta práctica:

1. Listar módulos principales del sistema.
2. Identificar acciones importantes por módulo.
3. Separar ver, crear, editar, anular, eliminar, exportar y administrar.
4. Identificar roles reales del negocio.
5. Construir una matriz rol-permiso inicial.
6. Marcar permisos críticos.
7. Revisar datos sensibles del diccionario de datos.
8. Revisar estados que bloquean o permiten acciones.
9. Revisar si hay sucursales o alcances.
10. Validar con escenarios reales.

Escenario de validación:

Un técnico intenta anular una orden entregada.
Una cajera intenta modificar un diagnóstico.
Recepción intenta ver reportes financieros.
Un supervisor anula un pago con motivo.

El modelo debe responder qué pasa en cada caso.

## Microejemplo administrativo
Tienda de reparación de celulares.

Roles:

Administrador
Recepción
Técnico
Caja
Supervisor

Permisos por módulo:

Clientes:
- clientes.ver
- clientes.crear
- clientes.editar

Reparaciones:
- reparaciones.ver
- reparaciones.crear
- reparaciones.asignarTecnico
- reparaciones.registrarDiagnostico
- reparaciones.cambiarEstado
- reparaciones.anular

Pagos:
- pagos.ver
- pagos.registrar
- pagos.anular

Reportes:
- reportes.operativos.ver
- reportes.financieros.ver

Usuarios:
- usuarios.ver
- usuarios.administrar

Lectura del diseño:

Recepción crea clientes y órdenes, pero no registra pagos.
Técnico registra diagnóstico, pero no anula pagos.
Caja registra pagos, pero no modifica diagnósticos.
Supervisor anula operaciones críticas con motivo.
Administrador gestiona usuarios y permisos.

Este modelo ayuda a diseñar backend, pantallas, auditoría y pruebas.

## Casos especiales
Usuario con varios roles

Una persona puede tener más de un rol.

Ejemplo:

María trabaja en recepción y caja.

Puede tener permisos combinados de ambos roles. Eso debe documentarse porque aumenta el poder del usuario dentro del sistema.

Rol temporal

Un usuario puede necesitar permisos por tiempo limitado.

Ejemplo:

Supervisor encargado durante una semana.

Aunque no se implemente al inicio, conviene saber si el negocio lo necesita.

Doble control

Algunas acciones pueden requerir revisión adicional.

Ejemplo:

Descuento mayor al 30% requiere aprobación de supervisor.
Anulación de pago requiere motivo y usuario autorizado.

Permisos heredados

Un rol puede heredar permisos de otro, pero esto puede complicar la lectura.

Ejemplo:

Supervisor hereda permisos de Recepción y Técnico,
pero además puede anular órdenes.

Si se usa herencia, debe documentarse con claridad.

## Cuándo usarlo
Úsalo cuando existan varios perfiles de trabajo, datos sensibles, acciones críticas o sucursales con distintos alcances.

## Cuándo no usarlo
No lo uses para modelar navegación, clases, procesos completos ni organigramas laborales sin relación con el sistema.

## Errores comunes
![Errores comunes al definir roles y permisos.](figure:roles-permissions-common-errors)

Usar solo Admin y Usuario

Dos roles suelen ser insuficientes. Terminan dando demasiado poder a usuarios comunes o concentrando todo en el administrador.

Nombrar roles con nombres de personas

Mal:

Rol: Ana
Rol: Carlos

Mejor:

Recepción
Técnico
Caja

Dar permisos demasiado amplios

Mal:

Recepción puede administrar reparaciones.

Mejor:

Recepción puede crear órdenes y editar datos iniciales.

No separar anular y eliminar

Eliminar borra. Anular conserva evidencia. En sistemas administrativos, anular suele ser más profesional.

No proteger reportes financieros

Los reportes pueden revelar ventas, costos, utilidad, deudas o rendimiento del negocio. No todos los roles deben verlos.

No considerar estados

Una orden entregada, anulada o cerrada no debería permitir las mismas acciones que una orden recién recibida.

## Relación con otros diagramas
Mapa de módulos:
define sobre qué áreas funcionales se aplican permisos.

Diccionario de datos:
identifica datos sensibles, auditables o restringidos.

Modelo conceptual:
muestra entidades sobre las que se ejecutan acciones.

Flujo operativo:
indica quién participa en cada paso del trabajo.

BPMN:
puede mostrar responsables mediante lanes.

Flujo de pantallas:
define qué pantallas puede visitar cada rol.

Wireframes:
pueden mostrar u ocultar botones según permiso.

UML Casos de uso:
relaciona actores con funcionalidades del sistema.

UML Estados:
define qué acciones son posibles según el estado.

UML Secuencia:
puede mostrar validación de autorización antes de ejecutar una operación.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

A partir de estos módulos, propón roles y permisos para un sistema administrativo.

Genera una matriz rol-permiso para una tienda de reparación de celulares con Administrador, Recepción, Técnico, Caja y Supervisor.

Revisa esta matriz y detecta permisos demasiado amplios, permisos faltantes o roles mal definidos.

Separa permisos de ver, crear, editar, anular, eliminar, exportar y administrar para cada módulo.

Identifica permisos críticos que deberían requerir auditoría o motivo obligatorio.

Relaciona estos permisos con estados de una orden de reparación.

Detecta qué datos del diccionario deberían tener permisos especiales de visualización o edición.

## Ficha rápida
Roles y permisos

Representa:
usuarios, roles, permisos, acciones, recursos, restricciones y auditoría.

No representa:
organigrama completo, nombres de empleados, flujo de proceso ni código interno.

Sirve para:
definir quién puede ver, crear, editar, anular, exportar o administrar cada parte del sistema.

Elemento clave:
matriz rol-permiso.

Error clásico:
crear solo Admin y Usuario, o dar permisos demasiado amplios.

Pregunta clave:
¿Quién puede hacer qué, sobre qué recurso y bajo qué condiciones?


## Auditoría de frontera teórica

Roles y permisos define autorización funcional. No debe confundirse con organigrama de empresa, actores de casos de uso ni simple menú visible.

Un rol describe una responsabilidad operativa dentro del sistema. Un permiso describe una acción autorizada sobre un recurso. Si estás describiendo quién usa una funcionalidad como meta observable, probablemente estás en UML Casos de uso. Si estás decidiendo qué botón se muestra o se bloquea en una pantalla, estás aplicando permisos dentro de wireframes.

Este diagrama debe mantenerse cerca de módulos, estados y datos sensibles. Una acción como anular pago, entregar equipo o ver reportes financieros no se decide solo por pantalla; depende de rol, permiso, estado del registro y trazabilidad requerida.

## Checklist final de estudio
- Puedo diferenciar usuario, rol, permiso, acción y recurso.
- Puedo armar una matriz rol-permiso básica.
- Puedo detectar permisos críticos, por dato, por estado y por sucursal.
- Puedo relacionar permisos con botones, pantallas, procesos y auditoría.
- Puedo evitar el modelo pobre de solo administrador y usuario normal.

