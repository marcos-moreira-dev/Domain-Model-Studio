# PF07 — Matriz de contenido teórico por tipo de diagrama

Estado: **planificación ejecutada**  
Alcance: **contenido teórico que deberá alimentar el centro de ayuda**

## 1. Regla general

Cada ficha debe enseñar la teoría detrás del diagrama, no solo su uso dentro de Domain Model Studio. La interfaz puede simplificar algunos elementos, pero la ayuda debe explicar la idea correcta para que el usuario entienda qué está modelando.

## 2. Modelo conceptual

### Qué debe enseñar

El modelo conceptual representa conocimiento del dominio antes de convertirlo en tablas, clases o pantallas. Su objetivo es capturar entidades, atributos, relaciones y restricciones semánticas.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Entidad | rectángulo con nombre | Cosa, persona, evento o concepto relevante del dominio. |
| Atributo | óvalo conectado a entidad | Propiedad que describe una entidad o relación. |
| Relación | rombo o línea rotulada | Asociación semántica entre entidades. |
| Cardinalidad | marcas 1, N, 0..1, 1..N | Cantidad mínima/máxima de participaciones. |
| Identificador | atributo subrayado o clave | Dato que permite distinguir instancias. |

### Casos especiales

- entidad débil;
- relación identificadora;
- atributo compuesto;
- atributo multivaluado;
- atributo derivado;
- relación ternaria;
- cardinalidad mínima y máxima;
- diferencia entre modelo conceptual, modelo lógico relacional y modelo físico;
- diferencia entre Chen y pata de gallo.

### Errores comunes

- confundir entidad con tabla física;
- poner pantallas o botones como entidades;
- usar atributos que deberían ser entidades;
- olvidar cardinalidad mínima;
- convertir todo directamente a base de datos sin discutir significado del dominio.

## 3. Diccionario de datos

### Qué debe enseñar

El diccionario de datos documenta términos, campos, reglas, tipos, restricciones y observaciones de un sistema. Es más documental que gráfico, pero es clave para evitar ambigüedad.

### Elementos mínimos

| Elemento | Explicación esperada |
|---|---|
| Entidad/concepto | Unidad documentada. |
| Campo | Dato específico del concepto. |
| Tipo | Naturaleza del dato: texto, fecha, número, booleano, catálogo, etc. |
| Regla | Condición que debe cumplirse. |
| Ejemplo | Valor representativo. |
| Observación | Nota contextual. |

### Casos especiales

- campos calculados;
- campos obligatorios/opcionales;
- catálogos controlados;
- formatos de fecha/código;
- reglas de negocio contra validaciones de formato;
- equivalencias entre nombres del negocio y nombres técnicos.

## 4. BPMN básico

### Qué debe enseñar

BPMN básico representa procesos de negocio mediante eventos, actividades, decisiones y flujos. Debe enseñarse como notación para hablar con usuarios de negocio sin perder precisión.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Evento de inicio | círculo simple | Algo que dispara el proceso. |
| Actividad/tarea | rectángulo redondeado | Trabajo que se realiza. |
| Gateway | rombo | Decisión, bifurcación o sincronización de caminos. |
| Evento de fin | círculo final | Resultado o cierre del proceso. |
| Flujo de secuencia | flecha | Orden de ejecución. |
| Pool/lane | contenedor horizontal | Participante o área responsable. |

### Casos especiales

- gateway exclusivo frente a paralelo;
- evento intermedio;
- tarea manual frente a tarea de sistema;
- excepción o camino alternativo;
- proceso con varias áreas responsables;
- diferencia entre BPMN y diagrama de actividad UML;
- diferencia entre proceso de negocio y flujo de pantallas.

## 5. Flujo operativo

### Qué debe enseñar

El flujo operativo es una representación simple de pasos de trabajo. No intenta ser BPMN formal. Sirve para explicar rápidamente cómo opera un negocio o módulo.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Inicio | círculo o cápsula | Punto de arranque. |
| Paso | rectángulo | Acción operativa. |
| Decisión | rombo | Pregunta que divide el flujo. |
| Documento/dato | hoja o rectángulo inclinado | Información producida o consultada. |
| Fin | círculo o cápsula | Cierre operativo. |

### Errores comunes

- usarlo para modelar arquitectura técnica;
- mezclar pasos del usuario con pantallas específicas sin distinguirlos;
- no indicar responsables;
- no mostrar caminos alternativos.

## 6. UML Casos de uso

### Qué debe enseñar

Un caso de uso describe una funcionalidad observable desde el punto de vista de un actor externo. No describe pantallas internas ni clases.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Actor | persona de palitos | Rol externo que interactúa con el sistema. |
| Caso de uso | óvalo con verbo | Objetivo funcional observable. |
| Límite del sistema | rectángulo contenedor | Frontera de lo que pertenece al sistema. |
| Asociación | línea | Participación de actor en caso de uso. |
| Include | flecha punteada | Comportamiento obligatorio reutilizado. |
| Extend | flecha punteada | Comportamiento opcional o condicional. |
| Generalización | flecha triangular | Especialización de actor o caso. |

### Casos especiales

- actor primario y actor secundario;
- sistema externo como actor;
- include contra extend;
- casos de uso demasiado pequeños;
- casos de uso con nombres de pantalla;
- relación entre caso de uso y requerimiento funcional.

## 7. UML Clases

### Qué debe enseñar

El diagrama de clases representa estructura estática: clases, atributos, operaciones y relaciones. Puede usarse para diseño de software, pero también para razonar sobre dominio si se mantiene conceptual.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Clase | rectángulo en tres compartimentos | Tipo con nombre, atributos y operaciones. |
| Asociación | línea | Relación estructural entre clases. |
| Multiplicidad | 1, 0..1, *, 1..* | Cantidad de objetos relacionados. |
| Herencia | flecha triangular | Especialización. |
| Interfaz | caja o estereotipo | Contrato de operaciones. |
| Dependencia | flecha punteada | Uso débil o temporal. |
| Agregación | rombo blanco | Todo-parte débil. |
| Composición | rombo negro | Todo-parte fuerte. |

### Casos especiales

- asociación bidireccional o unidireccional;
- composición contra agregación;
- clase abstracta;
- interfaz;
- enum;
- paquetes o módulos;
- visibilidad pública/privada/protegida;
- no confundir clase con tabla ni con pantalla.

## 8. UML Actividad

### Qué debe enseñar

Un diagrama de actividad representa flujo de acciones, decisiones, concurrencia y finalización de un proceso o comportamiento.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Nodo inicial | círculo sólido | Inicio del flujo. |
| Acción | rectángulo redondeado | Paso ejecutable. |
| Decisión | rombo | Camino condicional. |
| Merge | rombo | Reunión de caminos alternativos. |
| Fork/join | barra gruesa | Paralelismo y sincronización. |
| Swimlane | carril | Responsable o área. |
| Nodo final | círculo final | Fin del flujo. |

### Casos especiales

- decisión contra merge;
- fork contra join;
- flujo paralelo;
- guardas en transiciones;
- actividad de negocio frente a comportamiento de sistema;
- diferencia con BPMN.

## 9. UML Secuencia

### Qué debe enseñar

Un diagrama de secuencia representa interacciones en el tiempo. El eje vertical indica avance temporal y las líneas de vida muestran participantes.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Participante | caja superior | Actor, objeto, servicio o componente. |
| Línea de vida | línea vertical punteada | Existencia del participante durante la interacción. |
| Mensaje | flecha horizontal | Comunicación entre participantes. |
| Activación | barra vertical | Periodo de ejecución o control. |
| Retorno | flecha punteada | Respuesta opcional. |
| Fragmento | marco `alt`, `opt`, `loop` | Condición, opción o repetición. |

### Casos especiales

- mensaje síncrono contra asíncrono;
- creación/destrucción de objeto;
- fragmento alternativo `alt`;
- fragmento opcional `opt`;
- bucle `loop`;
- no usarlo como diagrama de arquitectura general.

## 10. UML Estados

### Qué debe enseñar

Un diagrama de estados representa los estados posibles de una entidad, proceso u objeto y los eventos que causan transiciones.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Estado | rectángulo redondeado | Situación estable del objeto/proceso. |
| Estado inicial | círculo sólido | Punto inicial. |
| Estado final | círculo final | Cierre del ciclo. |
| Transición | flecha | Cambio de estado. |
| Evento | texto sobre flecha | Disparador de la transición. |
| Guarda | condición entre corchetes | Condición necesaria. |
| Acción | texto después de `/` | Efecto de la transición. |

### Casos especiales

- estado compuesto;
- transición interna;
- evento con guarda;
- acción de entrada/salida;
- diferencia entre estado y actividad;
- no modelar pasos secuenciales simples si no hay estados reales.

## 11. C4 Contexto

### Qué debe enseñar

C4 Contexto muestra el sistema de software como una caja dentro de su entorno: personas, organizaciones y sistemas externos con los que interactúa.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Persona | ícono/persona | Usuario o rol que interactúa. |
| Sistema de software | caja | Sistema bajo estudio o sistema externo. |
| Relación | flecha con verbo | Uso, envío, consulta, integración. |
| Límite | contenedor opcional | Organización, frontera o ecosistema. |

### Casos especiales

- sistema propio contra sistema externo;
- relación con verbo claro;
- no meter clases, tablas ni endpoints;
- mantener bajo nivel de detalle;
- diferencia con C4 Contenedores.

## 12. C4 Contenedores

### Qué debe enseñar

C4 Contenedores descompone un sistema en aplicaciones o almacenes de datos ejecutables/desplegables. Un contenedor en C4 no significa Docker necesariamente.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Contenedor | caja | Aplicación, backend, base de datos, app móvil, etc. |
| Persona | figura | Usuario que usa contenedores. |
| Relación | flecha | Comunicación o dependencia. |
| Tecnología | etiqueta | Tecnología relevante cuando ayuda a entender. |
| Límite del sistema | caja grande | Agrupación del sistema estudiado. |

### Casos especiales

- contenedor C4 contra contenedor Docker;
- base de datos como contenedor;
- backend/API como contenedor;
- tecnología como detalle útil, no como protagonista;
- diferencia con despliegue técnico.

## 13. Despliegue técnico

### Qué debe enseñar

El despliegue técnico muestra dónde corre cada parte del sistema: máquinas, servidores, clientes, base de datos, servicios, red y dependencias de instalación.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Nodo físico/virtual | caja 3D o servidor | Máquina, VM, nube, PC, red local. |
| Artefacto desplegado | caja interna | App, backend, base de datos, worker. |
| Conexión | línea/flecha | Comunicación entre nodos. |
| Entorno | etiqueta | Local, pruebas, producción. |
| Dependencia externa | nube/caja | Servicio externo. |

### Casos especiales

- despliegue local contra remoto;
- backup;
- base de datos local/remota;
- red interna;
- servicios externos;
- diferencia entre arquitectura lógica y despliegue físico.

## 14. Mapa de módulos

### Qué debe enseñar

El mapa de módulos organiza responsabilidades funcionales de una aplicación administrativa. No es diagrama UML formal, pero ayuda a dividir el sistema por áreas comprensibles.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Módulo | caja | Área funcional grande. |
| Submódulo | caja anidada | Parte concreta de un módulo. |
| Responsabilidad | etiqueta | Qué resuelve ese módulo. |
| Dependencia | flecha | Relación funcional entre módulos. |
| Límite | contenedor | Agrupación por área o rol. |

### Casos especiales

- módulo contra pantalla;
- módulo contra tabla;
- dependencia funcional contra dependencia técnica;
- módulo transversal como seguridad, reportes o auditoría.

## 15. Roles y permisos

### Qué debe enseñar

El mapa de roles y permisos muestra quién puede hacer qué, sobre qué recurso y bajo qué límites. Sirve para aclarar responsabilidades, seguridad funcional y operación diaria.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Rol | caja/persona | Perfil operativo. |
| Permiso | caja pequeña | Acción autorizada. |
| Recurso | caja | Módulo, entidad o pantalla afectada. |
| Asignación | línea | Relación rol-permiso. |
| Restricción | etiqueta | Límite contextual. |

### Casos especiales

- rol contra usuario individual;
- permiso de lectura, creación, edición, eliminación y aprobación;
- permiso por sucursal/área;
- separación de funciones;
- permisos peligrosos o administrativos;
- matriz RACI como referencia conceptual opcional.

## 16. Flujo de pantallas

### Qué debe enseñar

El flujo de pantallas representa navegación entre pantallas. No reemplaza BPMN ni UML actividad: explica cómo se mueve un usuario dentro de la interfaz.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Pantalla | rectángulo | Vista o ventana. |
| Acción de navegación | flecha rotulada | Botón, enlace o evento que cambia de pantalla. |
| Entrada | marcador | Pantalla inicial del flujo. |
| Salida | marcador | Finalización o retorno. |
| Condición | etiqueta | Regla para ir por un camino. |

### Casos especiales

- flujo principal y alternativo;
- navegación con modal;
- retorno a listado;
- navegación según rol;
- no confundir con wireframe ni proceso de negocio.

## 17. Wireframes administrativos

### Qué debe enseñar

Un wireframe administrativo es una maqueta de baja fidelidad que muestra estructura, contenido y acciones de una pantalla. No representa el diseño gráfico final. Sirve para discutir requerimientos de interfaz antes de invertir en UI detallada.

### Elementos mínimos

| Elemento | Figura didáctica | Explicación esperada |
|---|---|---|
| Pantalla | marco grande | Límite de una vista. |
| Barra superior | franja | Acciones globales o identificación. |
| Menú lateral | columna | Navegación principal. |
| Panel/tarjeta | rectángulo | Grupo de información. |
| Campo | caja pequeña | Entrada de datos. |
| Botón | rectángulo corto | Acción. |
| Tabla | grilla | Listado de registros. |
| Filtro/buscador | caja | Búsqueda o refinamiento. |
| Modal | caja superpuesta | Interacción temporal. |
| Alerta | banda/caja | Mensaje de estado. |

### Casos especiales

- login;
- dashboard;
- listado CRUD;
- formulario de registro;
- detalle de registro;
- pantalla de reportes;
- pantalla de configuración;
- pantalla de roles/permisos;
- diferencia entre wireframe, mockup y prototipo interactivo;
- no convertir la herramienta en un clon de Figma.

## 18. Relación entre fichas

La ayuda debe cruzar temas. Ejemplos:

| Si el usuario lee... | Debe sugerir también... |
|---|---|
| Modelo conceptual | Diccionario de datos, UML Clases, C4 Contenedores. |
| BPMN básico | Flujo operativo, UML Actividad, Roles y permisos. |
| UML Casos de uso | Requerimientos funcionales, Flujo de pantallas, Wireframes. |
| UML Clases | Modelo conceptual, C4 Contenedores. |
| UML Secuencia | C4 Contenedores, UML Clases. |
| UML Estados | BPMN, UML Actividad. |
| C4 Contexto | C4 Contenedores, Despliegue técnico. |
| Wireframes | Flujo de pantallas, Roles y permisos, Casos de uso. |

## 19. Cierre de esta matriz

Esta matriz no implementa el contenido completo, pero deja claro qué debe existir en la micro-Wikipedia. En implementación posterior, cada fila debe convertirse en una ficha real y navegable dentro del centro de ayuda.
