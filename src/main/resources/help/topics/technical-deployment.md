# Despliegue técnico

## Introducción
El despliegue técnico explica cómo las piezas de software llegan a un entorno real de uso. No describe solo arquitectura lógica; describe máquinas, servidores, redes, ambientes, respaldos y condiciones operativas para que el sistema funcione fuera del entorno de desarrollo.

## Pregunta que responde
¿Dónde corre cada parte del sistema, cómo se conectan entre sí, qué ambiente representa cada nodo y qué condiciones operativas se necesitan para mantenerlo funcionando?

## Idea central
C4 Contenedores explica qué piezas principales existen. El despliegue técnico explica dónde viven, cómo se comunican y qué responsabilidades operativas aparecen en producción.

## Qué es
![Nodos físicos y lógicos conectados para ejecutar el sistema en la realidad.](figure:technical-deployment-nodes)

Un despliegue técnico representa cómo un sistema existe fuera del código fuente: en computadoras, servidores, bases de datos, redes, servicios externos, carpetas de archivos, respaldos y ambientes de ejecución. Mientras C4 Contenedores explica las piezas principales de software, el despliegue técnico explica dónde corren esas piezas, cómo se conectan y qué necesita el sistema para operar con usuarios reales.

En un sistema administrativo, esta vista es especialmente importante porque el programa no termina cuando compila. También debe instalarse, conectarse, respaldarse, actualizarse y recuperarse cuando algo falla. Una tienda de reparación de celulares puede tener una PC en recepción, otra en caja, una laptop del administrador, un backend centralizado, una base PostgreSQL, reportes PDF, archivos adjuntos y respaldos. El despliegue técnico ayuda a ordenar todo eso.

La pregunta central es:

- ¿Dónde se ejecuta cada parte del sistema y qué infraestructura necesita para funcionar de forma segura y mantenible?

Este capítulo no busca enseñar nube avanzada, DevOps completo ni administración profesional de servidores. Busca dar una base clara para pensar cómo llevar un sistema administrativo desde “funciona en mi máquina” hasta “puede trabajar en un negocio real”.

## Para qué sirve
Sirve para planificar instalación, operación, mantenimiento, soporte y recuperación del sistema. También permite detectar riesgos antes de entregar el software.

Un despliegue técnico ayuda a responder preguntas como:

- ¿La aplicación se instala en una sola computadora o en varias?
- ¿La base de datos vive localmente, en un servidor del negocio o en la nube?
- ¿El backend corre en un servidor propio, VPS o PaaS?
- ¿Qué pasa si se daña la PC principal?
- ¿Dónde se guardan los respaldos?
- ¿Cómo se conectan dos sucursales?
- ¿Qué ambiente es de desarrollo y cuál es producción?
- ¿Qué partes deben tener credenciales, certificados, dominio o conexión segura?

En proyectos reales, esta vista ayuda a conversar con el cliente sin entrar todavía en comandos específicos. Por ejemplo: si el negocio tiene dos sucursales, una base local incrustada en una sola PC puede ser cómoda al inicio, pero peligrosa para crecer. El despliegue técnico permite explicar por qué conviene pensar en una base centralizada, un backend y acceso seguro por red.

## Qué representa
Representa nodos físicos o virtuales, aplicaciones instaladas, backend desplegado, base de datos, archivos, redes, dominios, certificados, ambientes, respaldos y relaciones técnicas de ejecución.

## Qué no representa
No representa clases, casos de uso, pantallas, reglas internas de negocio ni diseño visual. Tampoco reemplaza documentación de infraestructura detallada, manuales de operación o scripts reales de despliegue.

## Elementos principales
![Despliegue web mínimo con usuario, backend y base de datos.](figure:technical-deployment-basic-web)

Los elementos principales son:

- Nodo físico: computadora, laptop, servidor local, router o equipo real.
- Nodo virtual: VPS, contenedor, servicio cloud, base administrada o ambiente virtual.
- Artefacto desplegable: instalador, JAR, EXE, backend, frontend compilado, imagen Docker o script.
- Servicio: proceso que queda corriendo, como backend/API, servicio de reportes o tarea programada.
- Base de datos: almacenamiento persistente de información del negocio.
- Almacenamiento de archivos: lugar donde viven PDFs, imágenes, comprobantes, adjuntos o respaldos.
- Conexión: canal por donde se comunican las piezas, por ejemplo HTTPS, SQL, red local o API externa.
- Ambiente: desarrollo, pruebas, staging o producción.
- Respaldo: copia planificada de datos, archivos y configuración crítica.

Un nodo puede contener varios artefactos. Por ejemplo, un servidor puede ejecutar backend, tareas programadas y almacenamiento temporal. Una PC cliente puede ejecutar una aplicación desktop. Una base administrada puede vivir fuera del servidor principal.

## Nodo de despliegue
Un nodo de despliegue es el lugar donde algo corre, se instala o se almacena. Puede ser físico o virtual.

Ejemplos físicos:

- PC de recepción.
- PC de caja.
- Laptop del administrador.
- Servidor local.
- Router de la red.

Ejemplos virtuales:

- VPS.
- PaaS.
- Base de datos administrada.
- Contenedor Docker.
- Bucket o almacenamiento de archivos.

Lo importante no es solo dibujar cajas. Cada nodo debe tener responsabilidad clara. Si una caja se llama “servidor”, hay que saber qué hace: ¿ejecuta backend?, ¿guarda base de datos?, ¿sirve archivos?, ¿hace respaldos?, ¿expone la API al exterior?

## Artefacto desplegable
Un artefacto desplegable es la pieza concreta que se instala o ejecuta.

Ejemplos:

- Aplicación desktop empaquetada.
- Archivo JAR.
- Instalador MSI o EXE.
- Backend Spring Boot o NestJS.
- Frontend Angular compilado.
- Imagen Docker.
- Script de migración.
- Script de backup.

Un sistema puede tener varios artefactos. Por ejemplo, una solución administrativa podría tener una app desktop, un backend, migraciones de base de datos y scripts de respaldo. El despliegue técnico muestra dónde vive cada artefacto y cómo se actualiza.

## Ambientes: desarrollo, pruebas y producción
![Separación entre ambiente de desarrollo y ambiente de producción.](figure:technical-deployment-dev-vs-prod)

Una práctica profesional mínima es separar ambientes:

- Desarrollo: donde el programador trabaja, prueba rápido y rompe cosas sin afectar al cliente.
- Pruebas o staging: donde se valida una versión antes de usarla con datos reales.
- Producción: donde trabaja el cliente con información real.

No conviene probar cambios peligrosos directamente en producción. Si una migración falla, si una validación borra datos o si un cambio rompe el login, el negocio puede quedarse detenido.

Ejemplo:

- Desarrollo: laptop del programador, backend local, PostgreSQL local con Docker Compose.
- Producción: servidor real, base real, usuarios reales, respaldos reales.

Docker Compose puede ser excelente para desarrollo porque levanta servicios de forma reproducible, pero no reemplaza el diseño de despliegue, los respaldos ni la seguridad.

## Red y comunicación
Las flechas de un despliegue técnico representan comunicación real o dependencia operativa.

Ejemplos:

- PC cliente → Backend/API por HTTPS.
- Backend/API → PostgreSQL por conexión interna.
- Backend/API → almacenamiento de archivos.
- Backend/API → servicio de correo.
- Backend/API → facturación electrónica.

Conviene etiquetar las flechas con protocolo o propósito:

- HTTPS para comunicación segura entre cliente y backend.
- SQL para acceso del backend a base de datos.
- API externa para servicios de terceros.
- Backup diario para copias programadas.

HTTP significa Hypertext Transfer Protocol. HTTPS significa Hypertext Transfer Protocol Secure. API significa Application Programming Interface. DNS significa Domain Name System. TLS significa Transport Layer Security.

No hace falta volver el diagrama un curso de redes, pero sí debe quedar claro qué se conecta con qué.

## Base de datos en producción
La base de datos es uno de los elementos más delicados. Puede vivir en varias formas:

- Base local en una PC.
- Base en servidor local.
- Base en VPS.
- Base administrada en nube.
- Base en contenedor.

Para un negocio de una sola PC, una base local puede funcionar si el alcance es pequeño y los respaldos están claros. Para varias sucursales, una base local separada por sucursal suele crear problemas de sincronización, duplicidad o pérdida de información.

En sistemas administrativos con varias estaciones o sucursales, suele ser más razonable:

- aplicación cliente o frontend;
- backend centralizado;
- base de datos centralizada;
- permisos y reglas validadas en backend;
- respaldos programados.

La base de datos no debe verse como “un archivo cualquiera”. Contiene clientes, pagos, órdenes, usuarios, permisos, estados e historial. Su pérdida puede afectar directamente la operación del negocio.

## Archivos y respaldos
![Respaldo desde producción hacia almacenamiento separado y restauración de prueba.](figure:technical-deployment-backups)

El sistema puede manejar más que registros de base de datos:

- PDFs.
- comprobantes.
- imágenes de equipos.
- reportes.
- logs.
- exportaciones.
- contratos.
- archivos adjuntos.

El despliegue debe indicar dónde viven esos archivos y cómo se respaldan.

Un respaldo serio debe responder:

- ¿Qué se respalda?
- ¿Cada cuánto?
- ¿Dónde se guarda?
- ¿Quién puede acceder?
- ¿Cómo se restaura?
- ¿Se ha probado la restauración?

Una frase útil:

- Un respaldo que nunca se ha probado restaurar es solo una esperanza.

No basta con decir “hay backup”. Hay que pensar en restauración.

## Seguridad operativa básica
Sin volver esto un curso de ciberseguridad, el capítulo debe enseñar criterios mínimos:

- Usar HTTPS cuando haya comunicación por red.
- No exponer la base de datos directamente sin necesidad.
- Limitar credenciales de producción.
- Separar usuarios del sistema y usuarios de infraestructura.
- Proteger respaldos.
- Registrar errores y logs.
- Mantener dependencias actualizadas.
- Aplicar permisos en backend, no solo en la interfaz.

Una aplicación desktop o frontend puede ocultar botones, pero la seguridad real debe validarse también del lado del backend. Si un usuario no tiene permiso para anular una orden, el backend debe rechazar la operación aunque alguien intente forzarla.

## Relaciones y lectura
![Aplicación desktop en dos sucursales conectada a backend centralizado.](figure:technical-deployment-desktop-centralized)

El diagrama se lee como una historia operativa:

- El usuario trabaja en una PC cliente.
- La aplicación se comunica con el backend.
- El backend aplica reglas, permisos y validaciones.
- El backend guarda información en la base de datos.
- Los archivos importantes van a almacenamiento controlado.
- Los respaldos se ejecutan periódicamente.

Ejemplo de lectura:

- La PC de recepción de la sucursal A ejecuta la aplicación desktop.
- La PC de caja de la sucursal B ejecuta la misma aplicación.
- Ambas se conectan al backend central por HTTPS.
- El backend se comunica con PostgreSQL por una conexión interna.
- Los respaldos se guardan en un almacenamiento separado.

Esta lectura permite detectar riesgos. Si todas las PCs se conectan directamente a la base de datos, quizá se están exponiendo credenciales y reglas críticas. Si no aparece ningún respaldo, el sistema depende de la suerte.

## Casos especiales
Una sola computadora

Puede existir un despliegue simple:

- PC única.
- Aplicación desktop.
- Base local.
- Carpeta local de respaldos.

Sirve para negocios pequeños, uso individual o prototipos controlados. Sus riesgos son claros: si se daña la PC y no hay respaldo externo, se puede perder todo.

Varias PCs en red local

Un local puede tener varias PCs conectadas a un servidor interno.

Ventajas:

- No depende tanto de Internet.
- Puede ser rápido dentro del local.
- Centraliza datos en una máquina interna.

Riesgos:

- Requiere mantenimiento físico.
- Necesita respaldo externo.
- Es sensible a cortes eléctricos.
- Puede complicarse si aparece otra sucursal.

Varias sucursales

![Comparación entre base local, servidor local y backend centralizado.](figure:technical-deployment-local-vs-cloud)

Con dos o más sucursales, conviene pensar en acceso centralizado:

- Sucursal A → Internet → Backend central.
- Sucursal B → Internet → Backend central.
- Backend central → Base central.

Aquí aparecen temas como permisos por sucursal, conectividad, disponibilidad, latencia y respaldo.

PaaS

PaaS significa Platform as a Service. Puede simplificar despliegues iniciales porque la plataforma se encarga de parte de la infraestructura.

Hay que revisar:

- costos;
- límites;
- base de datos;
- logs;
- backups;
- dominio;
- certificados;
- escalabilidad.

VPS

VPS significa Virtual Private Server. Da más control, pero exige más responsabilidad.

Puede ser útil para backend, base y archivos, pero requiere:

- actualizaciones;
- seguridad;
- backups;
- monitoreo;
- administración.

Docker y Docker Compose

Docker puede empaquetar servicios. Docker Compose puede levantar varios servicios juntos.

Pero Docker no reemplaza:

- backups;
- seguridad;
- monitoreo;
- documentación;
- buena arquitectura;
- criterio de despliegue.

## Cuándo usarlo
Usa un despliegue técnico cuando:

- vas a entregar el sistema a un cliente;
- hay varias máquinas;
- hay servidor o base de datos;
- hay backend/API;
- hay archivos y reportes;
- hay respaldos;
- hay varias sucursales;
- necesitas explicar instalación, operación o recuperación.

También conviene usarlo antes de estimar costos de infraestructura. Un sistema para una sola PC no tiene las mismas necesidades que un sistema con dos sucursales y backend centralizado.

## Cuándo no usarlo
No lo uses para explicar:

- entidades del negocio;
- campos del diccionario;
- pantallas;
- wireframes;
- casos de uso;
- clases;
- flujo de proceso;
- permisos funcionales detallados.

Para esas cosas existen otros diagramas.

Tampoco conviene llenarlo de detalles de bajo nivel que no ayuden a operar el sistema. Si el diagrama se vuelve una lista interminable de puertos, comandos, carpetas y secretos, quizá necesita separarse en documentación técnica de instalación.

## Errores comunes
![Errores comunes: cliente conectado directo a base, sin backups o ambientes mezclados.](figure:technical-deployment-common-errors)

Errores frecuentes:

- Confundir C4 Contenedores con despliegue.
- No diferenciar desarrollo y producción.
- Conectar clientes directamente a la base de datos sin criterio.
- No planear respaldos.
- Planear respaldos pero nunca probar restauración.
- Guardar archivos importantes en carpetas improvisadas.
- No considerar varias sucursales.
- No documentar dominio, HTTPS o certificados.
- Instalar todo en una sola PC sin explicar riesgos.
- No dejar claro cómo levantar el sistema en otra máquina.

Un error grave es pensar que “funciona en mi laptop” equivale a “está listo para producción”. Producción implica datos reales, usuarios reales, fallos reales y responsabilidad real.

## Relación con otros diagramas
- C4 Contexto: muestra usuarios y sistemas externos que necesitan conectividad.
- C4 Contenedores: define las piezas de software que luego se despliegan.
- Modelo conceptual: identifica datos que serán persistidos.
- Diccionario de datos: ayuda a decidir qué datos requieren respaldo, seguridad o auditoría.
- Roles y permisos: define accesos por usuario, sucursal o acción crítica.
- Flujo operativo: revela qué tan crítico es el sistema para la operación diaria.
- BPMN: muestra procesos que pueden depender de disponibilidad del sistema.
- Flujo de pantallas: indica qué interfaces usan los usuarios.
- UML Secuencia: detalla llamadas reales entre cliente, backend, base y servicios.
- UML Estados: ayuda a detectar datos críticos que no deben perderse.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

- A partir de este C4 Contenedores, genera un despliegue técnico con PCs cliente, backend, base de datos, almacenamiento y backups.
- Propón un despliegue mínimo para un sistema administrativo usado por dos sucursales.
- Compara base local, servidor local, VPS y PaaS para este sistema.
- Revisa este despliegue y detecta riesgos de respaldo, seguridad, conectividad o mantenimiento.
- Propón una arquitectura de producción mínima con backend centralizado, PostgreSQL y aplicación desktop.
- Diferencia qué corre en desarrollo y qué corre en producción.
- Indica qué datos, archivos y configuraciones deben respaldarse.
- Genera una lista de pasos para levantar el sistema en una máquina nueva.

## Ficha rápida
Despliegue técnico:

- Representa: nodos, servidores, PCs, servicios, bases de datos, conexiones, ambientes y respaldos.
- No representa: reglas de negocio, pantallas, clases ni procesos.
- Sirve para: instalación, operación, soporte, mantenimiento y recuperación.
- Pregunta clave: ¿dónde corre cada pieza y cómo se conecta en producción?
- Error clásico: confundir arquitectura lógica con infraestructura real.
- Regla práctica: si hay datos reales, debe haber respaldo real y restauración probada.

## Auditoría de frontera teórica

El despliegue técnico pertenece a la infraestructura y operación del sistema. No define qué piezas de software existen; eso lo hace C4 Contenedores. Define dónde corren, cómo se conectan, cómo se protegen y cómo se respaldan.

Si estás hablando de frontend, backend, base de datos y almacenamiento como piezas lógicas, estás en C4 Contenedores. Si estás hablando de VPS, PaaS, red local, dominio, HTTPS, backups, ambientes y restauración, estás en despliegue técnico.

Tampoco debe confundirse con DevOps completo. Este capítulo debe dar criterio para entender despliegues, riesgos y ambientes, sin prometer automatización, CI/CD o operación avanzada si el proyecto no la tiene.

## Checklist final de estudio
- Puedo ubicar dónde corre cada pieza: PC, servidor, nube, base de datos o almacenamiento.
- Puedo diferenciar arquitectura lógica de despliegue físico o virtual.
- Puedo separar desarrollo, pruebas y producción.
- Puedo considerar red, HTTPS, dominio, certificados, backups y restauración.
- Puedo evaluar despliegue local, VPS, PaaS o varias sucursales con criterio.

## Referencias / base teórica
Este capítulo se apoya en conceptos generales de despliegue de software, arquitectura operativa, separación de ambientes, infraestructura cliente-servidor, respaldos y operación de sistemas administrativos. En la guía se usa con un enfoque práctico: suficiente para razonar sistemas reales sin convertir el módulo de ayuda en un manual de administración de servidores.
