# Diccionario de datos

## Introducción
Un diccionario de datos es una guía de precisión sobre la información del sistema. Mientras el modelo conceptual ayuda a descubrir qué cosas existen en el negocio, el diccionario explica qué significa cada dato, cómo debe nombrarse, qué tipo de valor acepta, dónde aparece, qué reglas debe cumplir y qué consecuencias tiene usarlo mal.

En un sistema administrativo, muchos errores nacen de datos mal definidos. Un campo llamado fecha puede parecer suficiente durante una conversación rápida, pero después aparecen preguntas: ¿fecha de recepción, fecha de diagnóstico, fecha de entrega estimada, fecha real de entrega, fecha de pago o fecha de anulación? Todas son fechas, pero no significan lo mismo, no se capturan en el mismo momento y no tienen las mismas reglas.

Por eso el diccionario de datos no es una tabla decorativa ni un documento exclusivo para programadores. Es una herramienta de comunicación entre negocio, análisis, diseño, backend, base de datos, frontend, pruebas, reportes y mantenimiento futuro.

![Estructura mínima de un diccionario de datos verificable.](figure:data-dictionary-table)

## Pregunta que responde
El diccionario de datos responde:

¿Qué significa exactamente cada dato del sistema, cómo se llama, qué tipo de valor acepta, qué regla debe cumplir, dónde se usa y quién debe entenderlo?

Ejemplo:

- imeiEquipo: ¿es obligatorio?
- telefonoPrincipal: ¿puede repetirse?
- estadoOrden: ¿qué valores permite?
- saldoPendiente: ¿se ingresa o se calcula?
- motivoAnulacion: ¿quién puede escribirlo y cuándo?

La pregunta no es solo técnica. También es de negocio. Un dato ambiguo produce formularios ambiguos, validaciones incompletas, reportes poco confiables y discusiones con el cliente.

## Idea central
La idea central es:

El modelo conceptual dice qué cosas existen. El diccionario de datos dice qué significa cada dato con precisión.

Si el modelo conceptual identifica la entidad OrdenReparacion, el diccionario puede detallar campos como numeroOrden, fechaRecepcion, problemaReportado, estadoOrden, tecnicoAsignado, totalEstimado, totalPagado, saldoPendiente, fechaEntregaReal y motivoAnulacion.

Cada uno de esos campos necesita definición propia. No basta con saber que existe una orden. Hay que saber qué datos la describen, cuáles son obligatorios, cuáles se calculan, cuáles se muestran en reportes, cuáles son sensibles y cuáles deben auditarse.

![Del concepto al dato documentado.](figure:data-dictionary-from-concept)

## Qué es
El diccionario de datos es una referencia organizada que define con precisión los datos importantes del sistema: qué significan, cómo se nombran, qué valores aceptan, dónde se usan y qué reglas deben cumplir.

## Para qué sirve
Sirve para evitar ambigüedades entre análisis, formularios, base de datos, reportes, backend y conversaciones con el cliente. Cuando un dato queda definido, el equipo sabe cómo capturarlo, validarlo, protegerlo y explicarlo.

## Qué representa
Representa datos importantes del sistema junto con su significado, tipo lógico, reglas y uso. Puede documentar campos de entidades, formularios, reportes, pantallas, APIs, documentos o procesos administrativos.

Un buen diccionario puede indicar:

- entidad o módulo al que pertenece el dato;
- nombre lógico entendible;
- nombre técnico sugerido;
- descripción humana;
- tipo lógico;
- obligatoriedad;
- unicidad;
- valores permitidos;
- validaciones;
- valor por defecto;
- origen del dato;
- dónde se usa;
- ejemplo de valor;
- sensibilidad;
- auditoría;
- observaciones.

Ejemplo:

telefonoPrincipal no debería definirse solo como texto. Debe explicarse como número principal usado para contactar al cliente por reparaciones, pagos o entregas. Puede ser obligatorio si no existe otro medio de contacto. Puede aparecer en recepción, detalle del cliente, comprobantes internos y búsquedas. Además, debe tratarse como dato personal.

## Qué no representa
No reemplaza al modelo conceptual, aunque se alimenta de él. Tampoco reemplaza el diseño físico de base de datos, aunque puede orientar nombres de columnas y restricciones. No reemplaza el flujo de pantallas, aunque puede indicar dónde se captura o muestra un campo. No reemplaza roles y permisos, aunque ayuda a detectar datos sensibles y restringidos.

Un diccionario pobre se limita a esto:

- nombre VARCHAR(255)
- fecha DATE
- estado VARCHAR(50)

Eso no enseña el significado del dato. Un diccionario útil explica qué nombre, qué fecha y qué estado.

## Elementos principales
Sus elementos principales son el nombre lógico, el nombre técnico sugerido, la descripción, el tipo lógico, la obligatoriedad, la unicidad, los valores permitidos, las validaciones, el origen, el uso, la sensibilidad, la auditoría y las observaciones.

## Relación con el modelo conceptual
El modelo conceptual identifica entidades y atributos principales. El diccionario toma esos atributos y los convierte en definiciones más precisas.

Ejemplo:

- Modelo conceptual: Cliente tiene teléfono.
- Diccionario de datos: telefonoPrincipal es el número usado para contactar al cliente; es obligatorio si no se registra correo; puede contener formato local o internacional; se muestra en recepción y detalle de cliente; es dato personal.

También ocurre lo contrario: al construir el diccionario aparecen señales para mejorar el modelo conceptual. Si un cliente puede tener varios teléfonos, cada uno con tipo, autorización de WhatsApp, observación y estado, quizá teléfono ya no debería ser solo atributo. Podría convertirse en TelefonoCliente.

![El diccionario retroalimenta el modelo conceptual.](figure:data-dictionary-traceability)

## Dato, atributo, campo y columna
Conviene separar términos porque se suelen mezclar.

Un dato es una pieza de información con significado. Un atributo es una característica conceptual de una entidad. Un campo es una forma concreta de capturar, mostrar o transportar ese dato dentro del sistema. Una columna es una posible implementación física en una base de datos relacional.

Ejemplo:

- Entidad conceptual: Cliente.
- Atributo conceptual: teléfono.
- Campo documentado: telefonoPrincipal.
- Columna física posible: telefono_principal.

La guía prioriza el nivel conceptual-lógico. Primero importa entender significado, regla y uso. El tipo SQL exacto viene después.

## Niveles del diccionario
Un diccionario puede tener varios niveles.

Nivel conceptual: explica el significado del dato para el negocio. Por ejemplo, fechaRecepcion es la fecha en que el negocio recibe el equipo.

Nivel lógico: define tipo esperado, obligatoriedad, valores permitidos y validaciones. Por ejemplo, fechaRecepcion es fecha obligatoria al crear orden y no debería ser posterior a la fecha actual, salvo regla especial.

Nivel físico: define columna, tipo SQL, longitud, índice, restricción o nombre técnico definitivo. Por ejemplo, fecha_recepcion DATE NOT NULL.

Para estudiar y levantar sistemas administrativos, el nivel más importante al inicio es el conceptual-lógico. Ahí se evitan los errores de significado.

## Columnas recomendadas
Un diccionario práctico puede empezar con columnas mínimas:

- Entidad o módulo.
- Nombre del dato.
- Nombre técnico sugerido.
- Descripción.
- Tipo lógico.
- Obligatorio.
- Único.
- Valores permitidos.
- Regla de validación.
- Origen del dato.
- Dónde se usa.
- Ejemplo.
- Observaciones.

Columnas avanzadas:

- Sensibilidad.
- Permiso requerido.
- Auditable.
- Editable después de creado.
- Aparece en reportes.
- Pantalla relacionada.
- API relacionada.
- Regla de negocio asociada.

No todas deben estar visibles siempre. Una tabla demasiado grande puede volverse difícil de leer. Pero la guía debe enseñar que esas dimensiones existen y que se agregan cuando aportan claridad.

![Columnas recomendadas para documentar un campo.](figure:data-dictionary-columns)

## Nombre lógico y nombre técnico
El nombre lógico debe ser comprensible para el negocio. El nombre técnico sugerido debe ser consistente para implementación.

Ejemplo:

- Nombre lógico: Fecha de recepción de la orden.
- Nombre técnico sugerido: fechaRecepcion.

Buenas prácticas:

- Usar nombres específicos: fechaRecepcion, fechaDiagnostico, fechaEntregaEstimada.
- Evitar nombres genéricos: fecha, dato, valor, info.
- Mantener consistencia: si se usa fechaRecepcion, no alternar con recepcionFecha sin razón.
- Usar lenguaje del dominio antes que términos internos de programación.

Un campo llamado estado casi siempre está incompleto. Lo correcto sería estadoOrden, estadoPago, estadoUsuario, estadoGarantia o estadoEquipo, según corresponda.

## Tipos de dato y valores permitidos
El tipo lógico describe la naturaleza del dato, no necesariamente su tipo SQL final.

Tipos lógicos frecuentes:

- texto;
- número;
- dinero;
- fecha;
- fecha y hora;
- booleano;
- estado;
- catálogo;
- referencia a otra entidad;
- archivo;
- imagen;
- correo;
- teléfono;
- documento de identidad.

Los valores permitidos son cruciales en campos de estado, categoría, tipo o método. Por ejemplo, estadoOrden podría permitir Recibida, En diagnóstico, Diagnosticada, Aprobada, En reparación, Lista para entrega, Entregada, Anulada y Rechazada.

Si no se documenta, el sistema puede terminar aceptando pendiente, PEND, En espera, esperando, Pendiente de revisión y otros valores parecidos que rompen reportes y reglas.

![Valores permitidos para campos de estado o catálogo.](figure:data-dictionary-allowed-values)

## Obligatoriedad y validaciones
La obligatoriedad indica cuándo un dato debe existir. No siempre es absoluta. Puede depender del momento, del estado, del rol o de otro campo.

Ejemplos:

- nombreCompleto es obligatorio al registrar cliente.
- diagnosticoTecnico puede no ser obligatorio al recibir la orden, pero sí antes de pasarla a Diagnosticada.
- fechaEntregaReal solo existe cuando la orden está Entregada.
- motivoAnulacion debe ser obligatorio al anular una orden.

Las validaciones deben ser verificables. No basta con decir “dato correcto”. Es mejor decir: montoPago debe ser mayor que cero y no debe superar el saldo pendiente, salvo permiso especial documentado.

Validaciones típicas:

- requerido;
- longitud mínima o máxima;
- formato;
- rango;
- unicidad;
- valor permitido;
- coherencia con otro campo;
- coherencia con estado;
- permiso requerido para editar.

![Obligatoriedad y validación según el momento del proceso.](figure:data-dictionary-validation)

## Datos calculados
Un dato calculado se obtiene a partir de otros datos.

Ejemplos:

- totalOrden = manoObra + repuestos - descuento.
- totalPagado = suma de pagos confirmados.
- saldoPendiente = totalOrden - totalPagado.
- diasEnReparacion = fechaActual - fechaRecepcion.

El diccionario debe explicar la fórmula, los datos base, cuándo se calcula, si se almacena o no, y qué ocurre si cambian los datos originales.

saldoPendiente puede aparecer en pantallas y reportes aunque no se guarde físicamente. Aun así debe documentarse porque afecta reglas como entregar equipo solo si el pago está completo.

![Dato calculado a partir de otros datos.](figure:data-dictionary-calculated-field)

## Datos sensibles
Algunos datos requieren cuidado especial.

Ejemplos:

- cédula;
- teléfono;
- dirección;
- correo;
- costos internos;
- utilidad;
- reportes financieros;
- historial de anulaciones;
- datos de acceso.

El diccionario debe indicar si el dato es sensible y qué restricciones puede requerir. Un técnico puede necesitar ver problema reportado y diagnóstico, pero no necesariamente utilidad, reportes financieros o todos los datos personales del cliente.

Esto conecta directamente con roles y permisos. El diccionario no define toda la seguridad, pero sí avisa qué datos merecen control especial.

![Dato sensible con acceso restringido.](figure:data-dictionary-sensitive-data)

## Datos auditables
Un dato auditable es aquel cuyo cambio conviene registrar. También existen campos generados por el sistema para trazabilidad.

Ejemplos:

- creadoPor;
- fechaCreacion;
- actualizadoPor;
- fechaActualizacion;
- anuladoPor;
- fechaAnulacion;
- motivoAnulacion.

En sistemas administrativos, la auditoría evita discusiones y pérdida de responsabilidad. Si alguien anula una orden, cambia un pago o modifica un diagnóstico, el sistema debería poder indicar quién lo hizo, cuándo y por qué.

El diccionario ayuda a decidir qué campos requieren historial y qué cambios deben quedar registrados.

![Campos de auditoría y trazabilidad.](figure:data-dictionary-audit-fields)

## Trazabilidad del dato
La trazabilidad responde de dónde viene el dato, dónde se usa y qué depende de él.

Ejemplo: estadoOrden puede aparecer en listado de órdenes, detalle de orden, reportes de productividad, permisos por acción, flujo operativo y UML Estados. Si ese dato cambia, varias partes del sistema se ven afectadas.

Una cadena útil de lectura:

- campo del diccionario;
- formulario donde se captura;
- entidad a la que pertenece;
- regla que lo valida;
- pantalla que lo muestra;
- reporte que lo usa;
- permiso que controla su edición;
- estado o transición que lo modifica.

Un dato bien trazado es más fácil de mantener, probar y explicar.

## Relaciones y lectura
Un diccionario se lee relacionando cada dato con su entidad, módulo, pantalla, reporte, regla de negocio y posible almacenamiento. Por ejemplo, fechaRecepcion pertenece a OrdenReparacion, aparece en el formulario de recepción, se usa en reportes de tiempos y puede alimentar cálculos de días en reparación.

## Cómo leer un diccionario de datos
Para leer un diccionario, no mires solo el nombre del campo. Lee descripción, tipo lógico, obligatoriedad, valores permitidos, validación, origen y uso.

Preguntas útiles:

- ¿El nombre es específico?
- ¿La descripción aclara el significado?
- ¿El dato pertenece a la entidad correcta?
- ¿Es ingresado, calculado o generado automáticamente?
- ¿Es obligatorio siempre o solo bajo condición?
- ¿Tiene valores permitidos?
- ¿Aparece en reportes?
- ¿Es sensible?
- ¿Debe auditarse?

Un campo como estado parece obvio hasta que se pregunta: ¿estado de qué?, ¿quién lo cambia?, ¿cuáles valores permite?, ¿qué acciones bloquea?, ¿puede volver atrás?, ¿aparece en reportes?

## Cómo construirlo paso a paso
Un proceso práctico:

1. Partir del modelo conceptual.
2. Elegir una entidad o módulo.
3. Listar campos candidatos.
4. Renombrar campos ambiguos.
5. Escribir una descripción humana.
6. Definir tipo lógico.
7. Definir obligatoriedad.
8. Definir valores permitidos y validaciones.
9. Agregar ejemplos.
10. Marcar datos calculados, sensibles y auditables.
11. Indicar dónde se usa el dato.
12. Revisar si algún campo debería ser entidad separada.
13. Validar con casos reales del negocio.

Ejemplo de validación:

Juan dejó un teléfono para reparación. Recepción registró el problema, el técnico diagnosticó cambio de pantalla, el cliente pagó en dos partes y finalmente retiró el equipo. El diccionario debería permitir explicar todos los datos importantes que aparecen en esa historia.

## Microejemplo administrativo: tienda de reparación de celulares
Entidad: Equipo.

Campos:

- marca: texto que identifica el fabricante o marca comercial.
- modelo: texto que identifica el modelo visible o indicado por el cliente.
- imei: texto opcional; si se registra, debe ser único.
- estadoFisicoRecepcion: descripción del estado visible al recibir el equipo.
- accesoriosEntregados: lista o descripción de cargador, funda, memoria u otros elementos entregados.
- observacionesRecepcion: notas adicionales registradas por recepción.

Entidad: OrdenReparacion.

Campos:

- numeroOrden: identificador único interno.
- fechaRecepcion: fecha en que se recibe el equipo.
- problemaReportado: descripción inicial indicada por el cliente.
- estadoOrden: valor controlado del ciclo de vida.
- diagnosticoTecnico: descripción emitida por el técnico.
- totalEstimado: monto estimado de reparación.
- totalPagado: suma de pagos confirmados.
- saldoPendiente: dato calculado.
- fechaEntregaReal: fecha registrada al entregar el equipo.
- motivoAnulacion: obligatorio si la orden se anula.

Este ejemplo muestra que el diccionario no solo enumera campos. Obliga a decidir qué significa cada campo, cuándo se llena, quién lo llena, qué reglas tiene y dónde se usa.

## Casos especiales
Los campos únicos requieren revisión. cedulaCliente, correoUsuario, imeiEquipo o numeroOrden pueden ser únicos, pero cada caso depende del negocio. El IMEI puede ser opcional, pero si se registra no debe repetirse.

Los estados merecen cuidado especial. estadoOrden no debe ser texto libre. Debe tener valores permitidos y reglas de transición. Si los estados son parte crítica del sistema, también conviene modelarlos con UML Estados.

Los datos calculados deben distinguirse de los ingresados. totalOrden puede calcularse, pero precioManoObra quizá se ingresa. saldoPendiente puede mostrarse, pero no necesariamente almacenarse.

Los datos sensibles y auditables deben marcarse desde el análisis. No conviene descubrir al final que cualquier rol podía exportar teléfonos, direcciones o reportes financieros.

Los catálogos también deben documentarse. MetodoPago, TipoEquipo, Marca, EstadoOrden y Prioridad pueden ser listas cerradas, catálogos editables o entidades con reglas propias.

## Cuándo usarlo
Úsalo cuando el sistema empieza a tener campos importantes, reglas de validación, reportes, formularios, datos sensibles o varias personas interpretando la misma información. Es especialmente útil antes de diseñar formularios, APIs, base de datos y reportes.

## Cuándo no usarlo
No lo uses para reemplazar el modelo conceptual, el diseño físico completo de base de datos ni la documentación de código. Tampoco conviene volverlo una lista infinita de detalles técnicos si todavía no se entiende el significado del negocio.

## Errores comunes
Un error común es usar nombres vagos: fecha, estado, valor, dato, observacion. Otro error es describir un campo repitiendo su nombre, por ejemplo “fecha: fecha del registro”, sin explicar qué evento representa.

Otros errores frecuentes:

- Documentar solo tipos SQL y olvidar significado.
- No indicar obligatoriedad.
- No listar valores permitidos.
- No diferenciar dato calculado de dato ingresado.
- No indicar dónde se usa el dato.
- No marcar datos sensibles.
- No registrar campos auditables.
- Documentar campos que ya no existen.
- Hacer el diccionario después de programar todo.
- Usar el diccionario como basurero de campos sin relación con entidades o módulos.

![Errores comunes en diccionarios de datos.](figure:data-dictionary-common-errors)

## Relación con otros diagramas
Modelo conceptual: aporta entidades y atributos iniciales.

Mapa de módulos: indica en qué área funcional se usa cada dato.

Roles y permisos: define quién puede ver, crear, editar, anular o exportar datos.

Flujo operativo y BPMN: muestran en qué momento del trabajo se crea o modifica información.

Flujo de pantallas: muestra dónde se capturan, consultan o confirman los datos.

Wireframes: muestran campos, filtros, tablas, formularios y reportes.

UML Clases: puede transformar datos en atributos, DTOs, clases de valor o enums.

UML Estados: define valores y transiciones para campos de estado.

UML Secuencia: muestra cómo los datos viajan entre pantalla, backend, repositorio y base de datos.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

- A partir de este modelo conceptual, genera un diccionario de datos inicial con entidad, campo, descripción, tipo lógico, obligatoriedad y validaciones.
- Revisa este diccionario y detecta nombres ambiguos, campos duplicados, campos calculados y datos sensibles.
- Propón valores permitidos para los estados de una orden de reparación.
- Indica qué campos deberían ser auditables y por qué.
- Separa campos ingresados por usuario, calculados por el sistema y generados automáticamente.
- Detecta si estos campos pertenecen realmente a la entidad indicada o deberían moverse a otra entidad.
- Convierte este diccionario en una base para formularios, reportes y validaciones.
- Revisa si faltan campos para trazabilidad humana.

## Ficha rápida
Diccionario de datos.

Representa: datos del sistema con significado, tipo lógico, reglas, uso y trazabilidad.

No representa: modelo conceptual completo, diseño visual, SQL físico definitivo ni permisos completos.

Sirve para: evitar ambigüedad, diseñar formularios, preparar validaciones, crear reportes, orientar backend y base de datos.

Debe cuidar especialmente: estados, datos sensibles, datos calculados, campos auditables y valores permitidos.

Error clásico: escribir nombres de columnas sin explicar qué significan.

Pregunta clave: ¿qué significa este dato y qué reglas debe cumplir?

## Auditoría de frontera teórica

El diccionario de datos complementa al modelo conceptual, pero trabaja con mayor precisión. Su tarea no es descubrir el dominio completo, sino definir con claridad qué significa cada dato, de dónde sale, qué reglas tiene y dónde se usa.

Si el documento está explicando entidades y relaciones generales, todavía estás en modelo conceptual. Si está describiendo campos, formatos, obligatoriedad, valores permitidos, sensibilidad, auditoría y trazabilidad, estás en diccionario de datos. Si empieza a hablar de columnas físicas, índices, tipos SQL concretos o migraciones, ya está entrando en diseño físico de base de datos.

El diccionario tampoco reemplaza los wireframes. Puede decir que fechaRecepcion es obligatoria y que montoPago debe ser mayor que cero, pero el wireframe decide dónde aparece el campo, cómo se muestra el error y qué acción ve el usuario.

## Checklist final de estudio
- Puedo explicar qué significa cada dato y para qué se usa.
- Puedo distinguir atributo conceptual, campo de formulario, columna física y dato calculado.
- Puedo marcar obligatoriedad, valores permitidos, validaciones, sensibilidad y trazabilidad.
- Puedo relacionar datos con pantallas, reportes, API y base de datos sin mezclarlos.
- Puedo revisar si faltan reglas antes de programar formularios o persistencia.

## Referencias / base teórica
El capítulo se apoya en prácticas de análisis de sistemas, documentación de datos y gestión de metadatos. Normas como ISO/IEC 11179 inspiran la idea de definir datos con significado, estructura y trazabilidad, aunque esta guía usa un enfoque práctico orientado a sistemas administrativos, levantamiento de información y comunicación con clientes.
