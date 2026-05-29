# C4 Contexto y C4 Contenedores

## Introducción
C4 Contexto es el primer nivel del modelo C4. Su propósito es ubicar el sistema dentro de su entorno antes de abrir la caja y hablar de backend, base de datos, frontend, servidores o clases. En este nivel el sistema se ve como una caja negra: importa saber quién lo usa, con qué sistemas externos se comunica y cuál es su límite de responsabilidad.

En un proyecto real, este diagrama evita una confusión muy común: creer que todos están hablando del mismo sistema cuando en realidad cada persona imagina un alcance distinto. Un cliente puede pensar que el sistema incluirá facturación electrónica completa, integración con WhatsApp, reportes avanzados, respaldo automático, portal de clientes y control de inventario. El desarrollador quizá solo está pensando en una aplicación administrativa para registrar órdenes y pagos. El diagrama de contexto obliga a poner esa conversación en una imagen simple.

C4 Contexto no está diseñado para explicar cómo está construido internamente el software. No muestra clases, tablas, pantallas, paquetes de código ni servidores. Muestra el sistema central, las personas que interactúan con él, los sistemas externos relevantes y las relaciones principales entre ellos.

![Vista básica de contexto: personas, sistema central y sistemas externos.](figure:c4-context-basic)

## Pregunta que responde
C4 Contexto responde:

- qué sistema estoy construyendo;
- quién lo usa o se beneficia de él;
- qué otros sistemas participan;
- qué queda dentro y fuera del alcance;
- qué relaciones externas son importantes.

Para una tienda de reparación de celulares, la pregunta se puede formular así: ¿el sistema administrativo solo registra clientes, equipos, órdenes y pagos, o también emite facturas, envía mensajes por WhatsApp, se conecta con una pasarela de pago y permite que el cliente consulte el estado desde fuera?

El diagrama no resuelve todos esos detalles, pero sí los hace visibles para que no queden como supuestos escondidos.

## Idea central
La idea central es:

- C4 Contexto muestra el sistema como una caja negra rodeada por personas y sistemas externos.

En este nivel no importa todavía si el sistema tendrá Angular, JavaFX, Spring Boot, NestJS, PostgreSQL o Docker. Eso pertenece a C4 Contenedores o a Despliegue técnico. En contexto solo importa entender el entorno y el límite.

Ejemplo de lectura:

- Recepción usa el sistema administrativo para registrar clientes y órdenes.
- Técnico usa el sistema para consultar órdenes asignadas y registrar diagnósticos.
- Caja usa el sistema para registrar pagos.
- El sistema puede enviar notificaciones al cliente por WhatsApp.
- El sistema puede integrarse en una fase futura con facturación electrónica.

## Qué es
C4 Contexto y C4 Contenedores son vistas de arquitectura que explican primero el sistema en su entorno y luego sus piezas principales de software.

## Para qué sirve
Sirve para separar alcance, usuarios, sistemas externos, aplicaciones, backend, base de datos y servicios sin mezclar niveles.

## Qué representa
Representa elementos externos y de alto nivel:

- sistema central;
- personas o grupos de usuarios;
- sistemas externos;
- relaciones entre ellos;
- límite del sistema;
- alcance actual, futuro o fuera de alcance.

El sistema central suele dibujarse como una caja destacada. Las personas aparecen alrededor. Los sistemas externos aparecen fuera del límite del sistema propio. Las flechas indican interacción, dependencia o intercambio de información.

![Sistema central y límite de responsabilidad.](figure:c4-context-boundary)

## Qué no representa
C4 Contexto no representa:

- tablas de base de datos;
- entidades del modelo conceptual;
- pantallas;
- wireframes;
- clases UML;
- métodos;
- endpoints;
- carpetas del código;
- contenedores internos del sistema;
- servidores físicos;
- puertos o certificados.

Puede mencionar una integración externa, pero no debe detallar todavía cómo se implementará. Por ejemplo, puede mostrar “Servicio de facturación electrónica” como sistema externo, pero no necesita explicar sus endpoints, tokens, XML, certificados o reglas técnicas.

## Elementos principales
Sus elementos principales son persona, sistema central, sistema externo, límite, relación, contenedor, aplicación, API, base de datos y servicio.

## Sistema central
El sistema central es el software que se quiere construir, explicar o delimitar. Debe tener un nombre claro y una descripción breve.

Malos nombres:

- sistema;
- aplicación;
- plataforma;
- software;
- programa.

Mejores nombres:

- Sistema administrativo de reparaciones y ventas;
- Sistema de gestión para tienda de celulares;
- Sistema de control de órdenes técnicas;
- Plataforma interna de inventario y atención.

Un buen nombre ayuda a que el diagrama sea comprensible sin explicación excesiva.

Ejemplo:

- Sistema administrativo de reparaciones y ventas: permite registrar clientes, equipos, órdenes de reparación, pagos, usuarios y reportes básicos del negocio.

## Personas
En C4, una persona representa un grupo humano que interactúa con el sistema o recibe valor de él. No siempre significa una cuenta exacta del sistema. Puede ser un usuario interno, un actor externo, un cliente o una persona que recibe información indirectamente.

Ejemplos:

- Administrador;
- Recepción;
- Técnico;
- Caja;
- Dueño del negocio;
- Cliente;
- Contador externo;
- Supervisor de sucursal.

Una persona C4 se parece a un actor de casos de uso, pero no se usa con el mismo nivel de detalle. En contexto basta con mostrar quién se relaciona con el sistema y para qué.

![Diferencia entre personas y sistemas externos.](figure:c4-context-people-systems)

## Sistemas externos
Un sistema externo es una pieza de software, plataforma, servicio o sistema ajeno al sistema central, pero relevante para su operación.

Ejemplos:

- WhatsApp;
- correo electrónico;
- sistema de facturación electrónica;
- pasarela de pago;
- sistema contable;
- banco;
- servicio de respaldo;
- almacenamiento en la nube;
- API de notificaciones.

Un sistema externo no se controla completamente desde el proyecto. El sistema central puede usarlo, enviarle información, recibir información de él o depender de él, pero sigue estando fuera de su límite.

Ejemplo: el sistema administrativo puede registrar una orden y luego enviar una notificación por WhatsApp. WhatsApp aparece como sistema externo, no como módulo interno del sistema administrativo.

## Límite del sistema
El límite del sistema responde: qué está dentro, qué está fuera y qué solo se integra.

Dentro del sistema central pueden estar:

- gestión de clientes;
- gestión de equipos;
- órdenes de reparación;
- pagos;
- usuarios y permisos;
- reportes internos.

Fuera del sistema pueden estar:

- WhatsApp;
- facturación electrónica oficial;
- banco;
- correo electrónico;
- sistema contable externo;
- hojas de cálculo antiguas.

El límite es una conversación de alcance. Si no se define, el cliente puede asumir que una integración externa está incluida cuando realmente solo era una idea futura.

Ejemplo de frase útil:

- En la primera fase, el sistema registra pagos y genera reportes internos, pero no emite facturación electrónica oficial. Esa integración queda marcada como futura.

## Relaciones
Las relaciones indican cómo interactúan personas y sistemas con el sistema central. Deben tener significado, no ser líneas decorativas.

Malas etiquetas:

- usa;
- conecta;
- relación;
- datos.

Mejores etiquetas:

- registra órdenes;
- consulta reportes;
- registra diagnóstico;
- recibe notificaciones;
- envía datos de facturación;
- confirma pagos;
- exporta información contable.

![Relaciones de contexto con propósito claro.](figure:c4-context-relationships)

Ejemplo:

- Recepción usa el sistema para registrar clientes y órdenes.
- Técnico usa el sistema para registrar diagnósticos.
- Caja usa el sistema para registrar pagos.
- Dueño consulta reportes de operación.
- Sistema administrativo envía notificaciones al cliente por WhatsApp.
- Sistema administrativo podría enviar datos a facturación electrónica en una fase futura.

## Alcance actual, futuro y fuera de alcance
C4 Contexto permite aclarar el estado de una relación o sistema externo:

- actual;
- futuro;
- opcional;
- fuera de alcance;
- manual;
- pendiente de confirmar.

Esto es muy importante en proyectos reales. No todo lo que aparece alrededor del sistema debe implementarse desde el primer contrato.

Ejemplo:

- WhatsApp manual: actual, porque el negocio ya lo usa, pero no hay integración automática.
- Facturación electrónica: futura, porque puede venir en una segunda fase.
- Banco: externo, porque procesa pagos fuera del sistema.
- Sistema contable: fuera de alcance inicial, pero puede recibir reportes exportados.

![Alcance actual, futuro y fuera de alcance.](figure:c4-context-scope-future)

## Cómo leer un C4 Contexto
Una buena lectura debe sonar así:

- El sistema administrativo de reparaciones y ventas es usado por Recepción, Técnico, Caja y Administrador.
- Recepción registra clientes, equipos y órdenes.
- Técnico consulta órdenes asignadas y registra diagnósticos.
- Caja registra pagos y consulta saldos.
- El cliente no necesariamente inicia sesión, pero puede recibir notificaciones y aprobar cotizaciones por canales externos.
- WhatsApp, facturación electrónica y pasarela de pago son sistemas externos, no módulos internos.
- La facturación electrónica queda como integración futura si no se implementa en la primera fase.

Si al leer el diagrama aparecen dudas como “¿eso lo construimos nosotros o lo hace otro sistema?”, el diagrama está cumpliendo su función: está mostrando una conversación pendiente.

## Parte 2 — C4 Contenedores
C4 Contenedores es el segundo nivel del modelo C4. Después de ver el sistema como una caja negra en el diagrama de contexto, este nivel abre esa caja y muestra sus piezas principales de software. Aquí aparecen aplicaciones de interfaz, backend, API, base de datos, almacenamiento de archivos, servicios auxiliares e integraciones técnicas relevantes.

La palabra contenedor en C4 puede confundir. No significa necesariamente Docker. En C4, un contenedor es una unidad principal de software que ejecuta código o almacena datos. Una aplicación desktop, un frontend web, una aplicación móvil, un backend, una base de datos y un servicio de reportes pueden ser contenedores C4. Docker puede usarse para desplegarlos, pero eso pertenece más al despliegue técnico.

![Aplicación desktop conectada a backend/API y base de datos.](figure:c4-containers-basic-desktop-api-db)

## Pregunta que responde C4 Contenedores
C4 Contenedores responde:

- de qué aplicaciones o servicios principales se compone el sistema;
- qué contenedor se encarga de la interfaz;
- qué contenedor concentra reglas, permisos y validaciones;
- dónde se almacena la información;
- qué servicios auxiliares existen;
- cómo se comunican esas piezas entre sí.

Para una tienda de reparación de celulares, una respuesta razonable podría ser: la aplicación desktop la usa el personal del negocio; el backend/API aplica reglas y permisos; PostgreSQL guarda clientes, equipos, órdenes y pagos; un servicio de reportes genera PDF; y una integración futura podría enviar notificaciones o emitir facturación electrónica.

## Qué es un contenedor en C4
Un contenedor C4 es una pieza principal del sistema con responsabilidad técnica clara. No debe confundirse con una clase, un módulo funcional, una pantalla ni una tabla. Es más grande que una clase y más técnico que un módulo administrativo.

Ejemplos de contenedores:

- Aplicación desktop administrativa.
- Frontend web.
- App móvil.
- Backend/API.
- Base de datos PostgreSQL.
- Servicio de reportes.
- Almacenamiento de archivos.
- Servicio de notificaciones.
- Servicio de autenticación.

Cada contenedor debería tener nombre, responsabilidad, tecnología, relaciones y tipo de datos que maneja.

## Aplicaciones de interfaz
La interfaz es el contenedor que el usuario toca directamente. Puede ser una aplicación desktop, un frontend web o una app móvil. En sistemas administrativos pequeños y medianos, una interfaz desktop puede ser útil cuando se busca estabilidad, operación interna y una experiencia más cerrada que un navegador.

Ejemplos:

- Aplicación desktop JavaFX para administración.
- Frontend Angular para acceso web.
- Panel web de reportes.
- App móvil para técnicos.

La interfaz no debería contener toda la lógica crítica. Puede validar cosas básicas para ayudar al usuario, pero permisos, reglas fuertes y persistencia deberían vivir en el backend o en una capa equivalente.

![Frontend web conectado a backend/API y PostgreSQL.](figure:c4-containers-web-api-db)

## Backend/API
El backend/API es el contenedor que procesa reglas de negocio, permisos, validaciones, operaciones de escritura y comunicación con la base de datos. En un sistema profesional, suele ser el punto donde se protege la consistencia del negocio.

Ejemplo de responsabilidades:

- validar usuario y permisos;
- registrar órdenes de reparación;
- calcular saldos;
- cambiar estados;
- registrar pagos;
- generar reportes;
- consultar y actualizar base de datos;
- coordinar integraciones externas.

Si el sistema tiene varias sucursales, el backend centralizado ayuda a evitar bases locales separadas y datos inconsistentes.

## Base de datos
La base de datos es el contenedor que almacena información persistente. En un sistema administrativo puede guardar clientes, equipos, órdenes, pagos, usuarios, roles, permisos, inventario, auditoría y reportes generados.

La base de datos no es el modelo conceptual. El modelo conceptual explica el significado del negocio. La base de datos implementa una forma persistente de guardar parte de ese significado.

Ejemplo:

- Modelo conceptual: Cliente registra OrdenReparacion.
- Diccionario de datos: Cliente.cedula es texto, único y obligatorio.
- Base de datos: tabla clientes con columna cedula y restricción de unicidad.

## Servicios auxiliares
No todo debe vivir en un único backend si el sistema crece, pero tampoco conviene sobredividir temprano. Los servicios auxiliares aparecen cuando una responsabilidad merece aislarse.

Ejemplos:

- Servicio de reportes para PDF y exportaciones.
- Servicio de archivos para fotos, comprobantes o documentos.
- Servicio de notificaciones para correo o WhatsApp.
- Servicio de autenticación.
- Servicio de IA local para redactar informes futuros.

Para un MVP, muchos de estos pueden empezar dentro del backend modular. C4 Contenedores permite mostrar visión futura sin obligar a implementar microservicios desde el inicio.

![Backend/API coordinando base de datos, reportes, archivos y notificaciones.](figure:c4-containers-services)

## Relaciones entre contenedores
Las flechas entre contenedores deben explicar comunicación real. No basta con decir usa o conecta.

Mejor:

- Desktop administrativo envía solicitudes HTTPS al Backend/API.
- Backend/API lee y escribe datos en PostgreSQL.
- Backend/API solicita al servicio de reportes generar un PDF.
- Backend/API guarda comprobantes en almacenamiento de archivos.
- Backend/API envía notificaciones mediante un servicio externo.

Las flechas pueden incluir protocolo o estilo de comunicación cuando aporte claridad: HTTPS, HTTP/JSON, SQL, cola de mensajes, archivo, evento o llamada interna.

## Diferencia entre C4 Contexto y C4 Contenedores
Contexto responde con quién se relaciona el sistema. Contenedores responde de qué piezas principales está hecho el sistema.

En contexto se dibuja:

- Administrador.
- Técnico.
- Cliente.
- Sistema administrativo.
- WhatsApp.
- Facturación electrónica.

En contenedores se dibuja:

- Aplicación desktop.
- Backend/API.
- PostgreSQL.
- Servicio de reportes.
- Almacenamiento de archivos.

![Diferencia entre C4 Contexto y C4 Contenedores.](figure:c4-containers-context-vs-containers)

## MVP y arquitectura futura
C4 Contenedores ayuda a separar lo mínimo viable de la visión futura.

MVP razonable:

- Aplicación desktop o frontend web.
- Backend/API modular.
- PostgreSQL.
- Exportación básica de reportes.

Versión futura:

- Servicio de reportes más avanzado.
- Integración con facturación electrónica.
- Notificaciones por WhatsApp o correo.
- Portal externo para clientes.
- BI o analítica.
- IA local para informes narrativos.

La guía debe insistir en que mostrar una pieza futura en el diagrama no significa que ya esté implementada. Debe marcarse como futura, opcional o fuera de alcance.

## Casos especiales de C4 Contenedores
Un caso frecuente es la aplicación desktop con backend centralizado. La app corre en las computadoras del negocio y se conecta al backend por red. El backend guarda datos en una base central. Esto es útil cuando hay varias sucursales.

Otro caso es la aplicación desktop con base local. Puede servir para un negocio de una sola máquina, pero tiene riesgos de respaldo, multiusuario, sincronización y crecimiento.

También existe la variante web: navegador, frontend, backend y base de datos. Es útil cuando se necesita acceso desde varios lugares sin instalar aplicación desktop.

Un caso más avanzado es separar reportes, archivos, notificaciones o autenticación como servicios auxiliares. Esto debe hacerse por necesidad real, no por moda.

## Errores comunes de C4 Contenedores
Un error común es creer que contenedor C4 significa Docker. Docker puede ejecutar o empaquetar software, pero el contenedor C4 es una unidad conceptual de arquitectura.

Otro error es meter clases dentro del diagrama. Cliente, PagoService o OrdenRepository pueden ser clases o componentes internos, no contenedores principales.

También es un error meter pantallas específicas. Login, Dashboard o FormularioCliente pertenecen al flujo de pantallas o wireframes, no al nivel de contenedores.

Otro error frecuente es dibujar microservicios innecesarios. Para un sistema administrativo pequeño o mediano, un backend modular con buena arquitectura suele ser mejor que varios servicios prematuros.

![Errores comunes al mezclar contenedores con clases, pantallas o Docker.](figure:c4-containers-common-errors)

## Ficha rápida de C4 Contenedores
Representa:

- aplicaciones principales;
- backend/API;
- base de datos;
- servicios auxiliares;
- almacenamiento;
- relaciones técnicas entre piezas.

No representa:

- pantallas específicas;
- clases;
- métodos;
- tablas físicas detalladas;
- servidores concretos;
- Docker obligatoriamente.

Pregunta clave:

- ¿De qué piezas principales de software está compuesto el sistema y cómo se comunican?

## Relaciones y lectura
Se lee por niveles: contexto muestra quién se relaciona con el sistema; contenedores muestra qué piezas internas colaboran.

## Cómo construirlo paso a paso
Una receta práctica:

- nombrar el sistema central con precisión;
- escribir una descripción breve de su propósito;
- identificar personas que lo usan directamente;
- identificar personas que reciben valor indirectamente;
- listar sistemas externos ya existentes;
- listar integraciones futuras o deseadas;
- dibujar el límite del sistema;
- escribir relaciones con verbos claros;
- marcar qué es actual, futuro o fuera de alcance;
- revisar si se filtraron detalles internos que pertenecen a otro diagrama;
- validar el alcance con el cliente o con el equipo.

El paso más importante es evitar abrir la caja demasiado pronto. Si empiezas a dibujar base de datos, backend, controladores o clases, ya te pasaste al nivel de contenedores o diseño interno.

## Microejemplo administrativo: tienda de reparación de celulares
Sistema central:

- Sistema administrativo de reparaciones y ventas.

Personas:

- Administrador;
- Recepción;
- Técnico;
- Caja;
- Cliente;
- Dueño del negocio.

Sistemas externos:

- WhatsApp;
- correo electrónico;
- facturación electrónica;
- pasarela de pago;
- servicio de respaldo;
- sistema contable externo.

Relaciones:

- Recepción registra clientes, equipos y órdenes.
- Técnico registra diagnóstico y avance.
- Caja registra pagos.
- Dueño consulta reportes.
- Cliente recibe notificaciones de estado.
- Sistema administrativo puede enviar mensajes por WhatsApp.
- Sistema administrativo puede enviar datos a facturación electrónica en una fase futura.
- Sistema administrativo puede exportar reportes para el contador.

![Contexto de negocio pequeño con usuarios internos, cliente y sistemas externos.](figure:c4-context-small-business)

Este contexto ya ayuda a conversar con el cliente. Por ejemplo, si hay dos sucursales, el diagrama puede abrir la pregunta: ¿ambas sucursales usarán el mismo sistema central? Si el cliente pide facturación, el diagrama ayuda a separar si se trata de registro interno de pagos o emisión oficial integrada.

## Casos especiales
Una persona puede no usar directamente el sistema, pero igual aparecer. El cliente quizá no inicia sesión, pero recibe notificaciones, aprueba cotizaciones o entrega información.

Un sistema externo puede ser manual. WhatsApp puede aparecer aunque todavía no haya API. En ese caso se puede marcar como canal manual actual.

Una integración puede estar planificada pero no incluida. Facturación electrónica, pasarela de pago o inteligencia artificial para reportes pueden aparecer como futuro, no como promesa inmediata.

Un sistema central puede incluir varias aplicaciones. Por ejemplo, una app desktop, un panel web y una app móvil podrían verse como un solo sistema en contexto. La separación interna se explica después en C4 Contenedores.

Un límite puede ser difuso. Si no está claro si el sistema emitirá facturas oficiales o solo registrará ventas, el diagrama debe marcar esa duda, no esconderla.

## Cuándo usarlo
Úsalo cuando necesites explicar arquitectura de forma visual antes de entrar en clases, servidores o despliegue detallado.

## Cuándo no usarlo
No lo uses para modelar campos, pantallas, procesos BPMN, clases UML ni infraestructura física detallada.

## Errores comunes
Error 1: meter detalles internos.

- Base de datos, backend, clases, pantallas y tablas no van en C4 Contexto.

Error 2: no definir el sistema central.

- Si el centro del diagrama se llama “Sistema”, nadie sabe realmente qué se está construyendo.

Error 3: confundir persona con sistema externo.

- Cliente, técnico y administrador son personas. WhatsApp, banco y facturación electrónica son sistemas externos.

Error 4: usar flechas sin significado.

- Una línea que solo dice “usa” aporta poco. Mejor escribir “registra órdenes”, “consulta reportes” o “envía notificaciones”.

Error 5: mostrar módulos internos como sistemas externos.

- Clientes, pagos y reparaciones pueden ser módulos internos del sistema, no sistemas externos.

Error 6: prometer integraciones futuras sin marcarlas.

- Si facturación electrónica no entra en la primera fase, debe decirse.

![Errores comunes: detalles internos y relaciones vagas en el nivel de contexto.](figure:c4-context-common-errors)

## Relación con otros diagramas
C4 Contenedores abre la caja negra del sistema y muestra sus piezas principales: app desktop, frontend web, backend/API, base de datos y servicios.

Despliegue técnico muestra dónde corre cada pieza: PC cliente, servidor, nube, base de datos administrada, red local o varias sucursales.

Mapa de módulos muestra áreas funcionales internas como Clientes, Reparaciones, Pagos, Inventario y Reportes.

Roles y permisos detalla qué pueden hacer las personas dentro del sistema.

UML Casos de uso detalla funcionalidades observables por actor.

Flujo operativo y BPMN muestran procesos donde participan personas y sistemas.

Modelo conceptual explica los datos y conceptos que el sistema administra.

Flujo de pantallas y wireframes muestran cómo los usuarios navegan y usan la interfaz.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

- A partir de esta descripción del proyecto, genera un C4 Contexto con sistema central, personas, sistemas externos y relaciones.
- Revisa este diagrama de contexto y detecta si estoy metiendo detalles internos que deberían ir en C4 Contenedores.
- Identifica qué está dentro, fuera o en fase futura del sistema.
- Propón personas y sistemas externos para una tienda de reparación de celulares con dos sucursales.
- Separa integraciones actuales, manuales, futuras y fuera de alcance.
- Convierte este levantamiento de información en un C4 Contexto claro para explicarlo a un cliente.
- Revisa si las flechas tienen verbos claros o si son relaciones vagas.

## Qué pedirle a la IA después de entender C4 Contenedores
Prompts útiles:

- A partir de este C4 Contexto, genera un C4 Contenedores con frontend, backend, base de datos y servicios auxiliares.
- Propón una arquitectura C4 Contenedores para una tienda de reparación de celulares con app desktop, backend centralizado y PostgreSQL.
- Revisa este diagrama y detecta si estoy mezclando clases, pantallas, tablas o despliegue físico.
- Separa qué contenedores pertenecen al MVP y cuáles son futuros.
- Explica qué contenedor debe aplicar permisos, validaciones y reglas de negocio.
- Convierte esta arquitectura en una lista de responsabilidades por contenedor.

## Ficha rápida
C4 Contexto representa:

- sistema central;
- personas;
- sistemas externos;
- límite del sistema;
- relaciones de alto nivel;
- alcance actual o futuro.

No representa:

- clases;
- tablas;
- pantallas;
- backend interno;
- base de datos interna;
- servidores físicos;
- despliegue detallado.

Sirve para:

- aclarar alcance;
- conversar con clientes;
- identificar integraciones;
- separar lo interno de lo externo;
- preparar el paso hacia C4 Contenedores.

Error clásico:

- abrir la caja demasiado pronto y llenar el contexto con detalles internos.

## Auditoría de frontera teórica

C4 Contexto y C4 Contenedores pertenecen a arquitectura de software, pero trabajan en niveles distintos. Contexto mira el sistema desde fuera. Contenedores mira las grandes piezas internas de software.

En C4 Contexto no deberían aparecer tablas, clases, pantallas internas ni servicios demasiado detallados. Deben aparecer personas, sistemas externos, el sistema central y relaciones de alto nivel. En C4 Contenedores sí aparecen aplicaciones, backend, bases de datos, colas, almacenamiento o servicios principales, pero todavía no clases ni métodos.

El despliegue técnico responde otra pregunta: dónde corre cada contenedor, en qué ambiente, con qué red, backups y operación. Por eso un contenedor C4 puede existir aunque todavía no hayas decidido si corre en una VPS, PaaS, red local o máquina del cliente.

## Checklist final de estudio
- Puedo distinguir C4 Contexto de C4 Contenedores dentro del mismo tema.
- Puedo identificar sistema central, personas y sistemas externos sin meter detalles internos.
- Puedo identificar contenedores C4 sin confundirlos con Docker.
- Puedo separar arquitectura actual, MVP y arquitectura futura.
- Puedo usar C4 como puente entre negocio, software e infraestructura.

## Referencias / base teórica
Este capítulo se basa en la idea del modelo C4 como herramienta de arquitectura visual por niveles de abstracción. Para el nivel de contexto, lo esencial es mantener el sistema como caja negra, mostrar personas y sistemas externos, y explicar relaciones con lenguaje claro. En Domain Model Studio se usa como guía práctica para sistemas administrativos y levantamiento técnico inicial.
