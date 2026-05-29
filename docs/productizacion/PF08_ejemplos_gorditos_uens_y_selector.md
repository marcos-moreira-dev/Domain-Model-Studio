# PF08 — Ejemplos gorditos UENS/colegio y selector de ejemplos oficiales

Estado: **planificado**  
Tipo: **solo documentación de planificación**  
Alcance: **no implementa ejemplos, no modifica Java, no modifica recursos**

## 1. Propósito

PF08 planifica una familia coherente de ejemplos oficiales para Domain Model Studio. El objetivo no es agregar archivos sueltos, sino construir un **ecosistema didáctico y productivo** donde los tipos de diagrama visibles se entiendan como distintas vistas de un mismo sistema.

El dominio recomendado para esta familia es:

```text
Sistema administrativo escolar / UENS / colegio
```

La razón es práctica: el proyecto UENS ya contiene contexto de negocio, levantamiento de información, requerimientos, modelo conceptual, reglas de negocio, glosario, backend, desktop, reportes y capturas. Ese material permite crear ejemplos más realistas sin inventar desde cero.

## 2. Problema actual

Los ejemplos oficiales actuales cumplen una función mínima de prueba, pero están fragmentados por dominio:

```text
colegio
restaurante
óptica
ventas
soporte
login
piloto
sistema administrativo genérico
```

Esa variedad sirve para validar parsers, pero no ayuda a que el usuario vea cómo un mismo sistema se analiza desde varios ángulos. Para productización, conviene tener una familia de ejemplos integrada.

## 3. Principio de diseño

Cada ejemplo oficial debe responder a esta regla:

```text
mismo dominio base → distinta vista del sistema → mismo lenguaje de negocio
```

No se busca que todos los diagramas digan lo mismo. Se busca que todos hablen del mismo caso de estudio: una institución educativa que necesita ordenar estudiantes, representantes, docentes, secciones, clases, calificaciones, usuarios, reportes, auditoría y operación administrativa.

## 4. Fuentes internas de referencia

PF08 toma como referencias de contenido, sin copiarlas automáticamente en esta tanda:

```text
UENS-App-Administrativa-Colegio-main
Admin-Patterns-lab-main
```

### 4.1 Uso de UENS

UENS debe usarse como dominio base para:

- entidades académicas;
- reglas de negocio;
- flujos de matrícula;
- módulos administrativos;
- reportes;
- auditoría;
- pantallas desktop administrativas;
- despliegue mínimo de un sistema escolar.

### 4.2 Uso de Admin Patterns Lab

Admin Patterns Lab debe usarse como biblioteca de patrones para:

- levantamiento de información;
- descubrimiento de flujos;
- categorías de módulos administrativos;
- arquitectura de interacción;
- patrones de navegación;
- formularios, tablas, filtros y reportes;
- criterios de calidad, trazabilidad y pruebas.

## 5. Regla de tamaño: ejemplos gorditos, no mínimos

Los nuevos ejemplos UENS no deben ser meros “hola mundo”. Deben ser suficientemente grandes para mostrar valor, pero sin volverse inmanejables.

Criterios orientativos:

| Tipo | Tamaño recomendado |
|---|---|
| Modelo conceptual | 8 a 14 entidades, 12 a 24 relaciones, atributos principales |
| Diccionario de datos | 8 a 12 entidades, campos clave, validaciones y observaciones |
| Mapa de módulos | 6 a 10 módulos, submódulos y dependencias |
| Roles y permisos | 5 a 8 roles, 20 a 40 permisos/acciones |
| Flujo de pantallas | 8 a 14 pantallas, transiciones principales |
| Wireframes | 6 a 10 pantallas administrativas con componentes principales |
| UML Clases | 12 a 24 clases, agrupadas por módulo/paquete |
| BPMN básico | 8 a 16 nodos de proceso, decisiones y eventos |
| Flujo operativo | 8 a 14 pasos operativos con responsables |
| UML Casos de uso | 5 a 7 actores, 12 a 24 casos de uso |
| UML Actividad | 10 a 18 acciones, decisiones y bifurcaciones simples |
| UML Secuencia | 4 a 8 participantes, 10 a 20 mensajes |
| UML Estados | 5 a 10 estados, transiciones y condiciones |
| C4 Contexto | 4 a 8 actores/sistemas externos |
| C4 Contenedores | 4 a 8 contenedores y relaciones |
| Despliegue técnico | 5 a 10 nodos, servicios y rutas de operación |

## 6. Familia oficial propuesta de ejemplos UENS

### 6.1 Modelo conceptual

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_conceptual_model_completo.md
```

Contenido propuesto:

- estudiante;
- representante legal;
- sección;
- docente;
- asignatura;
- clase;
- calificación;
- usuario administrativo;
- reporte solicitado;
- auditoría operativa;
- asignación estudiante-sección, si el modelo la necesita como entidad asociativa;
- rol o perfil de acceso, si el alcance lo requiere.

Regla: este ejemplo debe conservar la claridad del modelo conceptual. No debe convertirse en modelo físico PostgreSQL ni en diagrama de tablas.

### 6.2 Diccionario de datos

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_data_dictionary_completo.md
```

Contenido propuesto:

- entidades del modelo conceptual;
- campos principales;
- tipo conceptual o tipo lógico aproximado cuando ayude;
- obligatoriedad;
- reglas de validación;
- ejemplos de valores;
- observaciones de negocio.

Debe servir como puente entre levantamiento de información y diseño posterior de base de datos.

### 6.3 Mapa de módulos

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_admin_module_map_completo.md
```

Módulos sugeridos:

- seguridad y usuarios;
- estudiantes;
- representantes;
- docentes;
- secciones;
- asignaturas y clases;
- calificaciones;
- reportes;
- auditoría;
- configuración académica.

Cada módulo debe indicar responsabilidades, límites y dependencias funcionales.

### 6.4 Roles y permisos

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_roles_permissions_completo.md
```

Roles sugeridos:

- Administrador;
- Secretaría;
- Dirección;
- Docente;
- Soporte/Mantenimiento;
- Consulta/Auditoría.

Acciones sugeridas:

- crear estudiante;
- editar estudiante;
- consultar representante;
- registrar docente;
- configurar sección;
- asignar clase;
- registrar calificación;
- solicitar reporte;
- revisar auditoría;
- administrar usuarios.

Regla: no confundir rol de negocio con clase de programación ni con tabla de base de datos.

### 6.5 Flujo de pantallas

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_screen_flow_completo.md
```

Flujo sugerido:

```text
Login → Dashboard → Estudiantes → Crear/Editar estudiante → Representante → Matrícula/Sección → Clases → Calificaciones → Reportes → Auditoría
```

Debe mostrar navegación, no lógica interna de código.

### 6.6 Wireframes administrativos

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_admin_wireframes_completo.md
```

Pantallas sugeridas:

- login;
- dashboard;
- listado de estudiantes con filtros;
- formulario de estudiante;
- detalle de representante;
- gestión de calificaciones;
- solicitud de reporte;
- auditoría;
- configuración de usuarios/roles.

Regla: los wireframes son maquetas o andamiaje visual. No deben intentar ser Figma, ni diseño final, ni UI pixel-perfect. Deben representar estructura, información, acciones y navegación.

### 6.7 UML Clases

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_uml_class_completo.md
```

Agrupadores sugeridos:

```text
academico.estudiantes
academico.representantes
academico.docentes
academico.clases
academico.calificaciones
seguridad.usuarios
reportes
shared
```

Clases sugeridas:

- Estudiante;
- RepresentanteLegal;
- Docente;
- Seccion;
- Asignatura;
- Clase;
- Calificacion;
- UsuarioAdministrativo;
- RolUsuario;
- SolicitudReporte;
- EventoAuditoria;
- servicios/casos de uso representativos, si la gramática lo permite.

Regla: debe poder representar proyectos de código reales a partir de Markdown generado o asistido por IA. Los agrupadores por módulo/paquete son importantes para evitar maraña visual.

### 6.8 BPMN básico

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_bpmn_matricula_completo.md
```

Proceso sugerido: **matrícula de estudiante**.

Nodos sugeridos:

- inicio: solicitud de matrícula;
- recibir datos del representante;
- verificar estudiante existente;
- registrar o actualizar representante;
- registrar estudiante;
- validar edad;
- seleccionar sección;
- verificar cupo;
- confirmar matrícula;
- emitir constancia o registro;
- fin.

Gateway sugerido:

- estudiante ya existe;
- sección tiene cupo;
- datos completos.

### 6.9 Flujo operativo

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_operational_flow_secretaria_completo.md
```

Proceso sugerido: **atención de secretaría para registro o consulta escolar**.

Diferencia con BPMN: este flujo puede ser menos formal y más cercano a cómo lo explicaría el personal administrativo.

### 6.10 UML Casos de uso

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_uml_use_case_completo.md
```

Actores sugeridos:

- Administrador;
- Secretaría;
- Dirección;
- Docente;
- Representante;
- Soporte/Mantenimiento.

Casos de uso sugeridos:

- iniciar sesión;
- gestionar estudiantes;
- gestionar representantes;
- gestionar docentes;
- configurar secciones;
- gestionar clases;
- registrar calificaciones;
- consultar reportes;
- revisar auditoría;
- administrar usuarios;
- reportar problema.

Relaciones sugeridas:

- include para pasos obligatorios como autenticarse o validar datos;
- extend para variantes como detectar duplicado, cupo agotado o corrección administrativa.

### 6.11 UML Actividad

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_uml_activity_registrar_matricula.md
```

Actividad sugerida: **registrar matrícula**.

Debe incluir:

- inicio;
- acciones;
- decisiones;
- final;
- posibles carriles si la gramática lo soporta después.

### 6.12 UML Secuencia

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_uml_sequence_registrar_calificacion.md
```

Secuencia sugerida: **registrar calificación**.

Participantes sugeridos:

- Docente o Secretaría;
- Pantalla de calificaciones;
- Backend/API;
- Servicio de calificaciones;
- Base de datos;
- Auditoría.

Mensajes sugeridos:

- solicitar listado de clases;
- seleccionar estudiante;
- enviar calificación;
- validar parcial y rango;
- guardar;
- registrar auditoría;
- confirmar operación.

### 6.13 UML Estados

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_uml_state_solicitud_reporte.md
```

Entidad sugerida: **solicitud de reporte**.

Estados sugeridos:

- pendiente;
- en proceso;
- completada;
- error;
- cancelada.

Transiciones sugeridas:

- solicitar;
- iniciar procesamiento;
- completar;
- fallar;
- reintentar;
- cancelar.

### 6.14 C4 Contexto

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_c4_context_completo.md
```

Contenido sugerido:

- sistema administrativo escolar;
- personal administrativo;
- dirección;
- docentes;
- representante legal como actor externo de consulta si aplica;
- servicio de correo o exportación si aplica;
- almacenamiento de reportes si aplica.

### 6.15 C4 Contenedores

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_c4_containers_completo.md
```

Contenedores sugeridos:

- aplicación desktop JavaFX;
- backend Spring Boot;
- base de datos PostgreSQL;
- módulo/worker de reportes;
- almacenamiento de archivos exportados;
- sistema de autenticación interno, si se modela separado.

### 6.16 Despliegue técnico

Archivo futuro sugerido:

```text
examples/markdown/diagramas/uens_technical_deployment_completo.md
```

Nodos sugeridos:

- PC administrativa;
- servidor local o VPS;
- PostgreSQL;
- backend;
- carpeta de reportes;
- backups;
- red local/internet;
- usuario operador.

## 7. Selector de ejemplos oficiales

El selector de ejemplos debe permitir abrir ejemplos desde la app sin que el usuario busque archivos manualmente.

Flujo propuesto:

```text
Barra superior → Ejemplos → elegir categoría → elegir tipo → elegir ejemplo → vista previa → abrir/importar
```

### 7.1 Categorías visibles

- Datos y dominio;
- Procesos de negocio;
- UML estructural;
- UML comportamiento/interacción;
- Aplicaciones administrativas;
- Arquitectura de software;
- Despliegue;
- Todos los ejemplos.

### 7.2 Información que debe mostrar cada ejemplo

Cada ejemplo debe tener metadatos visibles:

```text
Título
Tipo de diagrama
Dominio
Tamaño aproximado
Importable por la app: sí/no
Qué enseña
Cuándo usarlo
Archivos relacionados
```

### 7.3 Estados del selector

El selector debe distinguir claramente:

```text
Importable ahora
Disponible como referencia Markdown
Disponible como plantilla vacía
No disponible todavía
```

No debe fingir que un ejemplo se importa si solo sirve como material de lectura.

## 8. Catálogo de ejemplos como recurso, no como if gigante

La implementación futura debería evitar listas hardcodeadas dentro de una ventana enorme. Se recomienda crear un catálogo de recursos con metadatos.

Posible estructura futura:

```text
src/main/resources/examples/catalog/examples.json
src/main/resources/examples/markdown/uens/...
```

Clases pequeñas sugeridas para la implementación posterior:

```text
OfficialExampleDescriptor
OfficialExampleCatalog
ClasspathOfficialExampleCatalog
ExampleSelectionViewModel
ExampleSelectionDialog
OpenOfficialExampleUseCase
```

Regla de responsabilidad única:

- el catálogo conoce metadatos;
- el diálogo muestra y filtra;
- el caso de uso abre/importa;
- el workspace activo decide cómo montar el resultado;
- el shell solo coordina.

## 9. Relación con PF07

PF07 definió la micro-Wikipedia teórica. PF08 debe conectarse con ella así:

```text
ficha teórica del diagrama → ejemplo oficial UENS relacionado → abrir ejemplo
```

La ayuda enseña teoría; el ejemplo muestra una aplicación concreta.

## 10. Relación con PF09

PF09 debe probar que:

- cada tipo visible tenga al menos una plantilla oficial;
- cada tipo visible tenga al menos un ejemplo oficial;
- los ejemplos importables realmente se puedan importar;
- los ejemplos visuales se abran en su workspace correcto;
- los ejemplos no usen dominios mezclados cuando pertenezcan a la familia UENS;
- el selector no muestre capacidades falsas.

## 11. Criterios de cierre de PF08

La implementación futura de PF08 se podrá considerar cerrada cuando:

- exista una familia UENS completa o razonablemente completa;
- el selector de ejemplos esté disponible desde la barra superior fija;
- el usuario pueda abrir/importar ejemplos sin buscar archivos;
- los metadatos sean claros;
- los ejemplos importables pasen pruebas automáticas;
- cada ejemplo se abra en el workspace correcto;
- el contenido no ensucie la interfaz con jerga interna de programación.

## 12. Qué NO se implementa en esta tanda

- No se crean los ejemplos UENS todavía.
- No se modifica el catálogo Java.
- No se implementa el selector.
- No se agregan recursos en `src/main/resources`.
- No se modifican pruebas.
- No se reemplazan ejemplos mínimos existentes.
