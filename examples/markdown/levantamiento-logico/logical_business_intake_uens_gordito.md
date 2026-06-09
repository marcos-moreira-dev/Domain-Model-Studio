---
dms_version: "1"
diagram_type: "logical-business-intake"
name: "UENS — levantamiento lógico completo"
sample_kind: "full-example"
domain: "unidad educativa"
status: "importable"
canonical_contract: "logical-business-master-v1"
importable: true
intended_output: "expediente lógico documental"
---

# Levantamiento lógico completo — Unidad Educativa Niñitos Soñadores

> **Tipo de recurso:** ejemplo oficial gordito para el módulo de Levantamiento lógico.  
> **Dominio:** aplicación administrativa escolar para secretaría, estudiantes, representantes, docentes, secciones, clases, calificaciones, reportes y auditoría.  
> **Estado:** ejemplo importable y expediente lógico canónico UENS.  
> **Nota de alcance:** este levantamiento usa la app UENS real como contexto. No trata `matricula` como tabla persistente; cuando aparece, se entiende como proceso operativo de asignación vigente estudiante-sección.

---

## 0. Portada lógica del levantamiento

- **Negocio:** Unidad Educativa Niñitos Soñadores
- **Proyecto / sistema:** UENS — Sistema administrativo escolar de fase 1
- **Versión del levantamiento:** `v0.1`
- **Fecha:** `<AAAA-MM-DD>`
- **Responsable del levantamiento:** `<analista>`
- **Estado del documento:** `validado parcialmente`
- **Fuente principal:** `código real UENS | documentación de negocio | inferencia controlada`

### Propósito específico del levantamiento

```text
Convertir la operación administrativa escolar de UENS en una estructura lógica estable basada en estados, acciones, reglas, precondiciones, invariantes, postcondiciones, evidencias, riesgos y preguntas pendientes. El alcance cubre autenticación administrativa, gestión de estudiantes, representantes, docentes, secciones, asignaturas, clases, calificaciones, reportes asíncronos y auditoría operativa.
```

### Indicadores iniciales

- [x] El negocio está identificado.
- [x] El problema principal está descrito.
- [x] El alcance inicial está delimitado.
- [x] Las dudas importantes están registradas.
- [x] El documento distingue hechos implementados, reglas de negocio y supuestos.
- [x] El documento evita tratar matrícula como entidad persistente.
- [ ] El documento fue validado completo con el cliente final.

### Contexto técnico verificado contra la app UENS

- **Base de datos oficial V2:** `usuario_sistema_administrativo`, `representante_legal`, `seccion`, `docente`, `asignatura`, `estudiante`, `clase`, `calificacion`, `reporte_solicitud_queue`, `auditoria_evento`.
- **Roles implementados:** `ADMIN` y `SECRETARIA`. Docente, representante y estudiante son actores/entidades del negocio, no roles de login de fase 1.
- **Backend real:** `auth`, `usuario`, `system`, `dashboard`, `consultaacademica`, `representante`, `seccion`, `docente`, `asignatura`, `estudiante`, `clase`, `calificacion`, `reporte`, `auditoria`.
- **Desktop real:** `LOGIN`, `DASHBOARD`, `ESTUDIANTES`, `REPRESENTANTES`, `DOCENTES`, `SECCIONES`, `ASIGNATURAS`, `CLASES`, `CALIFICACIONES`, `REPORTES`, `AUDITORIA`.
- **Reportes reales:** cola `reporte_solicitud_queue`, estados `PENDIENTE`, `EN_PROCESO`, `COMPLETADA`, `ERROR`, formatos `XLSX`, `PDF`, `DOCX` y tipos `LISTADO_ESTUDIANTES_POR_SECCION`, `CALIFICACIONES_POR_SECCION_Y_PARCIAL`, `AUDITORIA_ADMIN_OPERACIONES`.
- **Auditoría real:** `auditoria_evento` y pantalla `AUDITORIA`, visible para `ADMIN`.

---


### Responsabilidad de alineación semántica

Este Levantamiento lógico define IDs, nombres, reglas y contratos semánticos canónicos del negocio. Cuando el usuario o una IA genere otros Markdown compatibles con Domain Model Studio, debe reutilizar los IDs y nombres definidos aquí para mantener consistencia entre artefactos.

Domain Model Studio no garantiza automáticamente la alineación entre proyectos independientes. Cada tipo de proyecto mantiene su propio alcance, parser, validación, edición y exportación.

No es obligatorio generar todos los tipos de proyecto. El usuario decide qué artefactos necesita para el caso de negocio.

## 1. Principio lógico central

Este levantamiento interpreta la institución como un sistema administrativo donde los estados escolares se transforman mediante acciones controladas. El sistema no se entiende primero como tablas ni pantallas, sino como operaciones que deben preservar verdades del negocio: estudiantes con representante, cupos de sección, clases coherentes con asignatura/sección, calificaciones únicas por estudiante-clase-parcial, reportes con estado explícito y trazabilidad de acciones administrativas.

```text
estado inicial
→ precondiciones
→ acción transformadora
→ reglas aplicadas
→ invariantes protegidas
→ postcondiciones
→ evidencia o auditoría
```

### Regla rectora del ejemplo

El levantamiento usa teoría interna del producto: MF/FL/CU/ACC/RN/PRE/INV/POST/ENT/EST/REP/RISK/PEND. La app UENS real sirve como caso aplicado, pero las tablas, endpoints y pantallas se contrastan con acciones y reglas; no sustituyen el razonamiento lógico.

### Advertencia semántica sobre matrícula

```text
En UENS fase 1 no existe tabla matricula.
La asignación escolar vigente se representa operativamente como estudiante.seccion_id.
Si se usa la palabra matrícula, debe leerse como proceso administrativo/orquestación, no como entidad persistente fuerte.
```

---

## 2. Contexto observado del negocio

### 2.1 Descripción general

UENS es una unidad educativa pequeña/mediana que necesita centralizar su operación administrativa escolar. La gestión observada cubre estudiantes, representantes legales, docentes, secciones, asignaturas, clases, calificaciones, reportes y auditoría. La operación anterior se apoyaba en cuadernos, listados sueltos, archivos informales y comunicación por WhatsApp, lo que aumenta el riesgo de duplicidad, baja trazabilidad y consultas lentas.

### 2.2 Problemas actuales observados

- Duplicidad posible de estudiantes por falta de validación centralizada.
- Cupos por sección difíciles de controlar manualmente.
- Información dispersa entre cuadernos, archivos y mensajes.
- Baja trazabilidad de cambios administrativos.
- Dificultad para consultar calificaciones por clase, asignatura, sección y parcial.
- Control de acceso no centralizado.
- Reportes potencialmente lentos si se generan en una petición síncrona.

### 2.3 Objetivos esperados del sistema

- Centralizar datos escolares básicos.
- Evitar registros duplicados evidentes.
- Controlar cupos por sección.
- Gestionar la asignación vigente estudiante-sección.
- Registrar calificaciones por estudiante, clase y parcial.
- Generar reportes administrativos en XLSX/PDF/DOCX mediante cola asíncrona.
- Conservar trazabilidad operativa mediante auditoría.
- Separar permisos de ADMIN y SECRETARIA según operación real.

### ACT-001 — Administrador del sistema

- Estado: validado parcialmente
- Fuente: código real y documentación API
- Descripción humana: Usuario administrativo con rol `ADMIN`, autorizado para configurar catálogos académicos, cambiar estados sensibles, consultar auditoría y reintentar reportes.
- Lectura: si una acción modifica configuración académica o auditoría, normalmente requiere rol ADMIN.
- Relacionada con: RN-014, RN-015, CU-001, CU-010, CU-011

### ACT-002 — Secretaría

- Estado: validado parcialmente
- Fuente: levantamiento de negocio y documentación desktop
- Descripción humana: Actor operativo principal de fase 1. Registra y consulta estudiantes, representantes, docentes, calificaciones y reportes administrativos permitidos.
- Lectura: Secretaría opera la gestión diaria pero no administra auditoría ni todos los cambios sensibles.
- Relacionada con: CU-002, CU-003, CU-004, CU-008, RN-014

### ACT-003 — Docente como actor del negocio

- Estado: validado parcialmente
- Fuente: reglas de negocio UENS
- Descripción humana: Participa en el dominio académico como persona que imparte clases, pero no opera el sistema administrativo como usuario en fase 1.
- Lectura: docente existe como entidad académica, no como rol de login implementado.
- Relacionada con: SUP-002, RN-010, ENT-004, ENT-007

### ACT-004 — Representante legal

- Estado: validado parcialmente
- Fuente: levantamiento de negocio UENS
- Descripción humana: Adulto responsable del estudiante para fines institucionales y de contacto.
- Lectura: un representante puede asociarse a uno o varios estudiantes; cada estudiante requiere un representante principal en fase 1.
- Relacionada con: RN-002, RN-003, ENT-002, ENT-006

### EVID-001 — Registro administrativo guardado

- Estado: validado parcialmente
- Fuente: inferencia desde módulos CRUD y auditoría
- Descripción humana: Evidencia de que una acción de registro o actualización quedó persistida y puede consultarse después.
- Lectura: toda acción transformadora crítica debe dejar datos persistidos o evento auditable.
- Relacionada con: INV-009, ACC-002, ACC-003, ACC-004, ACC-005

### EVID-002 — Evento de auditoría

- Estado: validado parcialmente
- Fuente: tabla `auditoria_evento`
- Descripción humana: Registro operativo de una acción administrativa con módulo, acción, entidad, resultado, actor, rol y fecha.
- Lectura: las acciones sensibles deben dejar una traza consultable.
- Relacionada con: RN-016, ACC-010, ENT-010

### EVID-003 — Archivo de reporte generado

- Estado: validado parcialmente
- Fuente: módulo de reportes y carpeta `reportes-output`
- Descripción humana: Archivo XLSX, PDF o DOCX generado a partir de una solicitud de reporte completada.
- Lectura: un reporte solo debe descargarse cuando la solicitud esté completada y tenga resultado preparado.
- Relacionada con: ACC-009, REP-001, REP-002, REP-003, EST-004

### SUP-001 — Fase 1 usa asignación vigente única

- Estado: validado parcialmente
- Fuente: reglas de negocio UENS
- Descripción humana: La relación estudiante-sección se maneja como una sola asignación vigente, sin historial formal de cambios.
- Lectura: cambiar de sección actualiza la asignación vigente, no crea una matrícula histórica.
- Relacionada con: RN-006, ACC-003, ENT-006, REL-002

### SUP-002 — Docente no es usuario administrativo en fase 1

- Estado: validado parcialmente
- Fuente: reglas de negocio UENS
- Descripción humana: El docente participa en el dominio académico, pero no inicia sesión ni opera módulos administrativos.
- Lectura: Docente puede aparecer como actor externo de negocio, no como rol implementado de permisos.
- Relacionada con: ACT-003, RN-013, ENT-004

### SUP-003 — Escala de calificación institucional 0 a 10

- Estado: validado parcialmente
- Fuente: esquema SQL V2
- Descripción humana: La nota se valida en rango 0 a 10, aunque el documento de negocio deja margen de parametrización.
- Lectura: para la app actual, la restricción técnica ya fija el rango 0..10.
- Relacionada con: RN-012, INV-007, ACC-008, ENT-008

---

## 3. Sistema de estados del negocio

### Propósito de la sección

Identificar los estados relevantes que cambian durante la operación escolar y las condiciones que no deben romperse aunque el sistema transforme datos.

### 3.1 Estados generales observables

- Usuario administrativo activo/inactivo.
- Estudiante activo/inactivo.
- Sección activa/inactiva.
- Docente activo/inactivo.
- Asignatura activa/inactiva.
- Clase activa/inactiva.
- Solicitud de reporte pendiente, en proceso, completada o con error.
- Evento de auditoría registrado con resultado EXITO, ERROR, INFO o ADVERTENCIA.

### EST-001 — ACTIVO

- Estado: validado parcialmente
- Fuente: esquema SQL V2 y reglas de negocio
- Descripción humana: Estado que permite operar con la entidad en procesos nuevos.
- Lectura: una entidad activa puede participar en registros, asignaciones o consultas funcionales según su tipo.
- Relacionada con: RN-005, RN-009, INV-004

### EST-002 — INACTIVO

- Estado: validado parcialmente
- Fuente: esquema SQL V2 y reglas de negocio
- Descripción humana: Estado que conserva la entidad para consulta pero bloquea nuevas operaciones que requieren condición activa.
- Lectura: inactivo no significa borrado; significa no elegible para operaciones nuevas.
- Relacionada con: RN-005, RN-009, INV-004

### EST-003 — ASIGNACION_VIGENTE

- Estado: validado parcialmente
- Fuente: estudiante.seccion_id y reglas de negocio
- Descripción humana: Estado relacional del estudiante cuando tiene una sección actual asignada.
- Lectura: la sección vigente es una referencia actual, no historial de matrícula.
- Relacionada con: SUP-001, RN-006, ACC-003, REL-002

### EST-004 — REPORTE_PENDIENTE

- Estado: validado parcialmente
- Fuente: tabla `reporte_solicitud_queue`
- Descripción humana: Solicitud de reporte creada pero aún no tomada por el worker.
- Lectura: el reporte existe como solicitud, pero todavía no tiene resultado disponible.
- Relacionada con: ACC-009, REP-001, REP-002, REP-003

### EST-005 — REPORTE_EN_PROCESO

- Estado: validado parcialmente
- Fuente: tabla `reporte_solicitud_queue`
- Descripción humana: Solicitud tomada para procesamiento por el backend.
- Lectura: el usuario debe consultar estado o esperar; no debe descargar archivo final todavía.
- Relacionada con: ACC-009, INV-010

### EST-006 — REPORTE_COMPLETADO

- Estado: validado parcialmente
- Fuente: tabla `reporte_solicitud_queue`
- Descripción humana: Solicitud procesada con resultado o archivo disponible.
- Lectura: la descarga del archivo o consulta del resultado ya es válida.
- Relacionada con: POST-009, EVID-003

### EST-007 — REPORTE_ERROR

- Estado: validado parcialmente
- Fuente: tabla `reporte_solicitud_queue`
- Descripción humana: Solicitud de reporte que terminó con error y conserva detalle para diagnóstico.
- Lectura: ADMIN puede evaluar reintento; Secretaría puede consultar estado si el flujo lo permite.
- Relacionada con: RISK-006, CU-011

---

## 4. Vocabulario lógico del dominio

### CON-001 — Asignación vigente estudiante-sección

- Estado: validado parcialmente
- Fuente: reglas de negocio y SQL V2
- Descripción humana: Relación actual entre un estudiante y una sección para la fase 1. Se implementa con `estudiante.seccion_id`.
- Lectura: no es una matrícula histórica; es el estado administrativo vigente del estudiante.
- Relacionada con: SUP-001, RN-006, ACC-003, ENT-006, REL-002

### CON-002 — Clase escolar

- Estado: validado parcialmente
- Fuente: levantamiento de negocio y SQL V2
- Descripción humana: Oferta concreta de una asignatura dentro de una sección, con día, hora de inicio, hora de fin, estado y docente opcional.
- Lectura: una clase conecta sección, asignatura y horario; las calificaciones se registran contra clase.
- Relacionada con: RN-010, RN-011, ENT-007, REL-004

### CON-003 — Reporte asíncrono

- Estado: validado parcialmente
- Fuente: backend de reportes
- Descripción humana: Solicitud persistida que separa pedir un reporte de generarlo y descargarlo.
- Lectura: el usuario no espera todo en una sola petición; la cola guarda estado y resultado.
- Relacionada con: ACC-009, REP-001, REP-002, REP-003, ENT-009

### CON-004 — Auditoría operativa

- Estado: validado parcialmente
- Fuente: tabla `auditoria_evento`
- Descripción humana: Registro trazable de acciones administrativas o técnicas relevantes.
- Lectura: permite saber qué se hizo, en qué módulo, con qué resultado y bajo qué actor.
- Relacionada con: ACC-010, RN-016, ENT-010

### CON-005 — Rol administrativo implementado

- Estado: validado parcialmente
- Fuente: enum/constraint de roles
- Descripción humana: Rol de login reconocido por el sistema: ADMIN o SECRETARIA.
- Lectura: otros actores pueden existir en el negocio, pero no son roles implementados salvo extensión futura.
- Relacionada con: ACT-001, ACT-002, RN-014, RN-015

---

## 5. Predicados, proposiciones y símbolos permitidos

### Convenciones lógicas

- `Activo(x)`: x está habilitado para operar.
- `Inactivo(x)`: x existe para consulta, pero no debe usarse en operaciones nuevas.
- `Estudiante(e)`, `Representante(r)`, `Seccion(s)`, `Clase(c)`, `Calificacion(k)`: predicados de tipo lógico.
- `TieneRepresentante(e, r)`: el estudiante e posee representante legal principal r.
- `AsignadoA(e, s)`: el estudiante e tiene sección vigente s.
- `CupoDisponible(s)`: la sección s no supera su cupo máximo.
- `ClaseCoherente(c)`: la clase c conecta sección y asignatura compatibles.
- `ReporteCompletado(rep)`: la solicitud de reporte tiene resultado disponible.
- `PuedeOperar(u, accion)`: el usuario u tiene rol y estado adecuados para ejecutar una acción.

### Lectura humana obligatoria

Las fórmulas son apoyo. Si una regla no puede explicarse en lenguaje administrativo, no debe promoverse como regla validada.

---

## 6. Reglas lógicas del negocio

### RN-001 — Rango operativo de edad estudiantil

- Estado: validado parcialmente
- Fuente: documentos de negocio UENS
- Descripción humana: La fase 1 considera como población objetivo estudiantes de 6 a 13 años.
- Lectura: al registrar o validar estudiante, la fecha de nacimiento debe permitir calcular una edad dentro del rango operativo.
- Relacionada con: PRE-002, INV-002, ACC-002, ENT-001, ATR-003

### RN-002 — Representante legal principal obligatorio

- Estado: validado parcialmente
- Fuente: reglas de negocio UENS
- Descripción humana: Cada estudiante debe quedar asociado a un representante legal principal.
- Lectura: no se confirma el estudiante si no existe representante legal asociado.
- Relacionada con: PRE-003, INV-003, POST-002, ENT-001, ENT-002, REL-001

### RN-003 — Representante puede agrupar varios estudiantes

- Estado: validado parcialmente
- Fuente: levantamiento de negocio
- Descripción humana: Un representante legal puede estar asociado a uno o varios estudiantes.
- Lectura: la relación representante-estudiante no es uno a uno estricta.
- Relacionada con: ENT-002, REL-001

### RN-004 — Detección de posible duplicado estudiantil

- Estado: validado parcialmente
- Fuente: problema operativo detectado
- Descripción humana: Antes de confirmar un estudiante nuevo, el sistema debe advertir posibles duplicados por nombres, apellidos y fecha de nacimiento.
- Lectura: la advertencia no necesariamente bloquea, pero debe reducir registros duplicados evidentes.
- Relacionada con: PRE-004, ACC-002, RISK-001

### RN-005 — Entidades inactivas no participan en nuevas operaciones

- Estado: validado parcialmente
- Fuente: reglas transversales UENS
- Descripción humana: Una entidad en estado INACTIVO no debe usarse para nuevas asignaciones, registros o planificación operativa.
- Lectura: inactivo conserva consulta administrativa, pero bloquea acciones nuevas.
- Relacionada con: INV-004, EST-002, RISK-002

### RN-006 — Asignación vigente única estudiante-sección

- Estado: validado parcialmente
- Fuente: reglas de negocio y SQL V2
- Descripción humana: En fase 1, cada estudiante puede tener una sola sección vigente.
- Lectura: cambiar la sección actualiza la referencia vigente; no crea historial de matrícula.
- Relacionada con: SUP-001, CON-001, ACC-003, REL-002

### RN-007 — Cupo máximo institucional por sección

- Estado: validado parcialmente
- Fuente: reglas de negocio y SQL V2
- Descripción humana: El cupo máximo permitido por sección es 35 estudiantes.
- Lectura: ninguna asignación debe hacer que una sección supere su cupo máximo.
- Relacionada con: PRE-005, INV-005, ACC-003, ENT-003

### RN-008 — Sección única por año, grado y paralelo

- Estado: validado parcialmente
- Fuente: constraint SQL `uq_seccion_unica`
- Descripción humana: No deben existir dos secciones con el mismo año lectivo, grado y paralelo.
- Lectura: la sección 2025-2026 / 4 / A debe existir una sola vez.
- Relacionada con: ENT-003, RISK-003

### RN-009 — Asignatura pertenece a un grado

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: La asignatura se define por nombre y grado; no almacena franja horaria.
- Lectura: Matemática de grado 4 y Matemática de grado 5 son catálogos distintos si su planificación lo requiere.
- Relacionada con: ENT-005, REL-003

### RN-010 — Clase como oferta concreta de sección y asignatura

- Estado: validado parcialmente
- Fuente: reglas de negocio y SQL V2
- Descripción humana: Una clase representa una asignatura ofertada en una sección, con horario y docente opcional.
- Lectura: las calificaciones usan clase como referencia principal.
- Relacionada con: CON-002, ENT-007, REL-004, REL-005

### RN-011 — Coherencia entre grado de sección y grado de asignatura

- Estado: validado parcialmente
- Fuente: reglas de negocio UENS
- Descripción humana: La asignatura asociada a una clase debe corresponder al grado de la sección.
- Lectura: no se debe crear una clase de asignatura de 7mo dentro de sección de 4to.
- Relacionada con: PRE-006, INV-006, ACC-006, ACC-007

### RN-012 — Calificación por estudiante, clase y parcial

- Estado: validado parcialmente
- Fuente: SQL V2 y requerimientos
- Descripción humana: Una calificación se registra por estudiante, clase y parcial; cada combinación debe ser única.
- Lectura: el sistema evita duplicar nota del mismo estudiante en la misma clase y parcial.
- Relacionada con: PRE-007, INV-007, ACC-008, ENT-008, REL-006

### RN-013 — Parciales permitidos en fase 1

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: La fase 1 permite únicamente Parcial 1 y Parcial 2.
- Lectura: cualquier otro parcial queda fuera del alcance actual.
- Relacionada con: PRE-007, INV-007, ACC-008

### RN-014 — Roles administrativos implementados

- Estado: validado parcialmente
- Fuente: SQL V2 y API
- Descripción humana: Los roles implementados son ADMIN y SECRETARIA.
- Lectura: Docente, Dirección, Soporte o Representante no son roles de login implementados en fase 1.
- Relacionada con: ACT-001, ACT-002, CON-005, ENT-011

### RN-015 — ADMIN concentra configuración sensible y auditoría

- Estado: validado parcialmente
- Fuente: documentación API
- Descripción humana: ADMIN ejecuta cambios sensibles, configuración académica, auditoría y reintento de reportes.
- Lectura: Secretaría puede operar gestión diaria, pero no toda administración sensible.
- Relacionada con: CU-010, CU-011, ACC-010

### RN-016 — Auditoría de operaciones relevantes

- Estado: validado parcialmente
- Fuente: tabla `auditoria_evento`
- Descripción humana: Las operaciones relevantes deben conservar trazabilidad con módulo, acción, entidad, resultado, actor y fecha.
- Lectura: una acción crítica sin trazabilidad debilita diagnóstico y control institucional.
- Relacionada con: EVID-002, ACC-010, ENT-010, INV-009

### RN-017 — Reportes mediante cola asíncrona

- Estado: validado parcialmente
- Fuente: backend de reportes
- Descripción humana: Los reportes se solicitan y procesan mediante `reporte_solicitud_queue` para no bloquear la interfaz.
- Lectura: el sistema separa solicitar, procesar, consultar estado y descargar resultado.
- Relacionada con: CON-003, ACC-009, ENT-009, EST-004, EST-005, EST-006, EST-007

### RN-018 — Resultado de reporte descargable solo al completar

- Estado: validado parcialmente
- Fuente: contrato de reportes
- Descripción humana: Un archivo de reporte solo debe descargarse cuando la solicitud esté COMPLETADA.
- Lectura: si el reporte está pendiente, en proceso o con error, el usuario debe ver estado, no archivo final.
- Relacionada con: POST-009, EVID-003, REP-001, REP-002, REP-003

---

## 7. Condiciones iniciales / precondiciones

### PRE-001 — Usuario autenticado y activo

- Estado: validado parcialmente
- Fuente: API auth
- Descripción humana: Para operar módulos protegidos, el usuario debe autenticarse y estar ACTIVO.
- Lectura: sin usuario activo no hay operación administrativa protegida.
- Relacionada con: RN-014, RN-015, ACC-001, CU-001

### PRE-002 — Fecha de nacimiento disponible para estudiante

- Estado: validado parcialmente
- Fuente: RN-001
- Descripción humana: El registro de estudiante requiere fecha de nacimiento para calcular edad operativa.
- Lectura: sin fecha de nacimiento no puede validarse rango 6–13.
- Relacionada con: RN-001, ACC-002, ATR-003

### PRE-003 — Representante identificado antes de confirmar estudiante

- Estado: validado parcialmente
- Fuente: RN-002
- Descripción humana: Debe existir o registrarse representante legal principal antes de confirmar el estudiante.
- Lectura: el estudiante no queda completo sin responsable asociado.
- Relacionada con: RN-002, ACC-002, ENT-002, REL-001

### PRE-004 — Revisión de duplicado antes de alta estudiantil

- Estado: validado parcialmente
- Fuente: RN-004
- Descripción humana: Antes de confirmar un estudiante nuevo, se buscan coincidencias relevantes para advertir duplicidad.
- Lectura: el usuario decide o corrige con información preventiva.
- Relacionada con: RN-004, ACC-002, RISK-001

### PRE-005 — Sección activa y con cupo antes de asignar estudiante

- Estado: validado parcialmente
- Fuente: RN-007
- Descripción humana: La sección debe estar activa y no superar cupo al asignar o cambiar estudiante.
- Lectura: no se asigna estudiante a sección inactiva o llena.
- Relacionada con: RN-005, RN-007, ACC-003, INV-005

### PRE-006 — Sección y asignatura compatibles antes de crear clase

- Estado: validado parcialmente
- Fuente: RN-011
- Descripción humana: La sección y la asignatura deben pertenecer al mismo grado operativo.
- Lectura: la clase solo es válida si su asignatura corresponde al grado de la sección.
- Relacionada con: RN-011, ACC-006, ACC-007, INV-006

### PRE-007 — Estudiante, clase, parcial y nota válidos antes de registrar calificación

- Estado: validado parcialmente
- Fuente: reglas de calificaciones
- Descripción humana: Se requiere estudiante válido, clase válida, parcial 1 o 2 y nota 0..10.
- Lectura: no se registra calificación sin base académica válida.
- Relacionada con: RN-012, RN-013, ACC-008, INV-007

### PRE-008 — Tipo y parámetros válidos antes de solicitar reporte

- Estado: validado parcialmente
- Fuente: backend reportes
- Descripción humana: El usuario debe indicar tipo de reporte y parámetros compatibles con ese reporte.
- Lectura: la cola no debe recibir solicitudes ambiguas o imposibles de procesar.
- Relacionada con: RN-017, ACC-009, REP-001, REP-002, REP-003

---

## 8. Invariantes del negocio

### INV-001 — Identidad administrativa de usuario única

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: No puede haber dos usuarios administrativos con el mismo nombre de login.
- Lectura: el login identifica una cuenta administrativa única.
- Relacionada con: ENT-011, RN-014

### INV-002 — Edad operativa dentro de rango

- Estado: validado parcialmente
- Fuente: RN-001
- Descripción humana: Todo estudiante operativo debe pertenecer al rango 6–13 según fecha de nacimiento y fecha de registro.
- Lectura: la población objetivo se mantiene acotada a educación básica del alcance.
- Relacionada con: RN-001, PRE-002, ACC-002

### INV-003 — Estudiante con representante principal

- Estado: validado parcialmente
- Fuente: RN-002
- Descripción humana: Todo estudiante registrado debe tener representante legal principal.
- Lectura: no existe estudiante operativo sin responsable de contacto.
- Relacionada con: RN-002, PRE-003, POST-002, REL-001

### INV-004 — Inactivos bloquean operaciones nuevas

- Estado: validado parcialmente
- Fuente: RN-005
- Descripción humana: Registros inactivos no participan en nuevas asignaciones, planificación o registro de calificaciones.
- Lectura: el estado INACTIVO se respeta transversalmente.
- Relacionada con: RN-005, EST-002, RISK-002

### INV-005 — Cupo de sección no excedido

- Estado: validado parcialmente
- Fuente: RN-007
- Descripción humana: La cantidad de estudiantes asignados a una sección no debe superar su cupo máximo.
- Lectura: `cantidadAsignada(s) <= cupoMaximo(s)`.
- Relacionada con: RN-007, PRE-005, ACC-003, CALC-001

### INV-006 — Clase coherente con grado

- Estado: validado parcialmente
- Fuente: RN-011
- Descripción humana: La asignatura de una clase debe corresponder al grado de la sección.
- Lectura: la clase no debe mezclar niveles académicos incompatibles.
- Relacionada con: RN-011, PRE-006, ACC-006, ACC-007

### INV-007 — Unicidad de calificación por estudiante-clase-parcial

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: No puede duplicarse una calificación para el mismo estudiante, clase y parcial.
- Lectura: la nota del parcial se actualiza o consulta, no se duplica.
- Relacionada con: RN-012, RN-013, ACC-008, ENT-008

### INV-008 — Reporte con estado explícito

- Estado: validado parcialmente
- Fuente: backend reportes
- Descripción humana: Toda solicitud de reporte debe tener estado PENDIENTE, EN_PROCESO, COMPLETADA o ERROR.
- Lectura: no se infiere estado por datos incompletos ni timestamps raros.
- Relacionada con: RN-017, ENT-009, EST-004, EST-005, EST-006, EST-007

### INV-009 — Acción crítica con evidencia o auditoría

- Estado: validado parcialmente
- Fuente: auditoría operativa
- Descripción humana: Las operaciones relevantes deben dejar registro persistido o evento auditable.
- Lectura: el sistema debe poder explicar qué ocurrió y con qué resultado.
- Relacionada con: RN-016, EVID-001, EVID-002, ACC-010

### INV-010 — Una solicitud de reporte no debe procesarse dos veces al mismo tiempo

- Estado: pendiente de validar
- Fuente: diseño backend DB queue
- Descripción humana: El worker debe reclamar una solicitud de forma segura para evitar procesamiento concurrente duplicado.
- Lectura: si hay más de un worker, el claim transaccional debe evitar doble generación.
- Relacionada con: RN-017, RISK-006, ACC-009

---

## 9. Condiciones de cierre / postcondiciones

### POST-001 — Sesión administrativa iniciada

- Estado: validado parcialmente
- Fuente: auth API
- Descripción humana: El usuario autenticado recibe tokens y datos de rol/estado para operar.
- Lectura: después del login válido, el sistema conoce identidad y permisos básicos.
- Relacionada con: ACC-001, CU-001

### POST-002 — Estudiante registrado con representante

- Estado: validado parcialmente
- Fuente: reglas de negocio
- Descripción humana: El estudiante queda persistido con datos mínimos, representante principal y estado inicial.
- Lectura: el alta estudiantil queda completa y consultable.
- Relacionada con: ACC-002, RN-002, INV-003, ENT-001

### POST-003 — Asignación vigente actualizada

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: El campo `seccion_id` del estudiante refleja su sección actual.
- Lectura: la sección vigente reemplaza cualquier valor anterior, sin historial formal.
- Relacionada con: ACC-003, SUP-001, CON-001

### POST-004 — Sección creada o actualizada sin duplicidad lógica

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: La sección queda definida por año lectivo, grado y paralelo único.
- Lectura: la oferta administrativa no tiene secciones duplicadas para el mismo grupo.
- Relacionada con: ACC-004, RN-008, ENT-003

### POST-005 — Clase creada con oferta académica válida

- Estado: validado parcialmente
- Fuente: reglas de clase
- Descripción humana: La clase conecta sección, asignatura, horario, estado y docente opcional coherentes.
- Lectura: la clase ya puede usarse para consulta y, si está activa, para calificaciones.
- Relacionada con: ACC-006, RN-010, RN-011, ENT-007

### POST-006 — Docente asociado a clase cuando corresponde

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Una clase puede quedar con docente asignado o pendiente de asignación según fase 1.
- Lectura: la planificación admite clase inicialmente sin docente.
- Relacionada con: ACC-007, RN-010, SUP-002

### POST-007 — Calificación registrada o actualizada

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: La nota queda asociada a estudiante, clase y parcial sin duplicar combinación.
- Lectura: la consulta académica puede obtener nota por clase, parcial y asignatura obtenida desde la clase.
- Relacionada con: ACC-008, RN-012, INV-007

### POST-008 — Solicitud de reporte encolada

- Estado: validado parcialmente
- Fuente: backend reportes
- Descripción humana: El sistema crea una fila en `reporte_solicitud_queue` con estado inicial PENDIENTE.
- Lectura: la petición de reporte fue aceptada sin procesar pesadamente en la misma llamada.
- Relacionada con: ACC-009, EST-004, ENT-009

### POST-009 — Reporte completado con resultado descargable

- Estado: validado parcialmente
- Fuente: backend reportes
- Descripción humana: La solicitud cambia a COMPLETADA y conserva resultado o archivo generado.
- Lectura: el usuario ya puede consultar o descargar el resultado.
- Relacionada con: RN-018, EST-006, EVID-003

### POST-010 — Evento de auditoría disponible

- Estado: validado parcialmente
- Fuente: tabla `auditoria_evento`
- Descripción humana: Una operación relevante queda registrada para consulta administrativa.
- Lectura: el sistema conserva trazabilidad mínima.
- Relacionada con: ACC-010, INV-009, ENT-010

---

## 10. Acciones transformadoras

### ACC-001 — Autenticar usuario administrativo

- Estado: validado parcialmente
- Fuente: API auth
- Descripción humana: Valida credenciales, estado activo y devuelve tokens de sesión.
- Lectura: transforma credenciales en sesión administrativa operable.
- Relacionada con: PRE-001, POST-001, CU-001, RN-014

### ACC-002 — Registrar estudiante con representante

- Estado: validado parcialmente
- Fuente: negocio y endpoints estudiantes/representantes
- Descripción humana: Identifica o registra representante, valida datos del estudiante, advierte duplicado y persiste estudiante.
- Lectura: transforma información inicial en estudiante administrativo consultable.
- Relacionada con: RN-001, RN-002, RN-004, PRE-002, PRE-003, PRE-004, POST-002

### ACC-003 — Asignar o cambiar sección vigente de estudiante

- Estado: validado parcialmente
- Fuente: endpoint `/api/v1/estudiantes/{id}/seccion-vigente`
- Descripción humana: Actualiza la sección actual del estudiante respetando estado y cupo.
- Lectura: transforma la situación escolar vigente del estudiante sin crear historial de matrícula.
- Relacionada con: SUP-001, RN-006, RN-007, PRE-005, POST-003

### ACC-004 — Crear o actualizar sección

- Estado: validado parcialmente
- Fuente: endpoints secciones y SQL V2
- Descripción humana: Define grado, paralelo, año lectivo, cupo máximo y estado.
- Lectura: transforma una necesidad organizativa en sección administrable.
- Relacionada con: RN-007, RN-008, POST-004, ENT-003

### ACC-005 — Registrar o actualizar representante legal

- Estado: validado parcialmente
- Fuente: endpoints representantes
- Descripción humana: Persiste datos de contacto del adulto responsable.
- Lectura: actualiza la información usada para contacto institucional.
- Relacionada con: RN-002, RN-003, ENT-002

### ACC-006 — Crear o actualizar clase

- Estado: validado parcialmente
- Fuente: endpoints clases
- Descripción humana: Define oferta concreta de asignatura en sección, horario y docente opcional.
- Lectura: transforma planificación académica en clase operable.
- Relacionada con: RN-010, RN-011, PRE-006, POST-005

### ACC-007 — Asignar docente a clase

- Estado: validado parcialmente
- Fuente: regla docente-clase
- Descripción humana: Vincula un docente con una clase cuando la planificación ya lo permite.
- Lectura: completa la planificación puntual sin obligar a que toda clase nazca con docente.
- Relacionada con: RN-010, POST-006, ENT-004, ENT-007

### ACC-008 — Registrar o actualizar calificación

- Estado: validado parcialmente
- Fuente: endpoints calificaciones
- Descripción humana: Persiste nota de estudiante para una clase y parcial.
- Lectura: transforma evaluación académica en registro consultable y único.
- Relacionada con: RN-012, RN-013, PRE-007, POST-007, ENT-008

### ACC-009 — Solicitar y procesar reporte asíncrono

- Estado: validado parcialmente
- Fuente: endpoints reportes y DB queue
- Descripción humana: Encola solicitud, permite polling, procesa resultado y habilita descarga.
- Lectura: separa comando rápido de procesamiento pesado.
- Relacionada con: RN-017, RN-018, PRE-008, POST-008, POST-009, ENT-009

### ACC-010 — Registrar evento de auditoría

- Estado: validado parcialmente
- Fuente: auditoría backend
- Descripción humana: Persiste evento con módulo, acción, entidad, actor, rol, resultado y fecha.
- Lectura: transforma una acción relevante en evidencia auditable.
- Relacionada con: RN-016, POST-010, EVID-002, ENT-010

### ACC-011 — Consultar dashboard/resumen administrativo

- Estado: validado parcialmente
- Fuente: endpoint dashboard
- Descripción humana: Obtiene indicadores agregados para la vista inicial del sistema.
- Lectura: transforma datos operativos en resumen de control.
- Relacionada con: CU-002, REP-004

---

## 11. Árbol operativo de macroflujos, flujos y casos de uso

### MF-001 — Administración escolar diaria

- Estado: validado parcialmente
- Fuente: levantamiento UENS
- Descripción humana: Macroflujo que concentra la operación regular de Secretaría y ADMIN sobre estudiantes, representantes, secciones, clases y calificaciones.
- Lectura: agrupa los flujos principales de gestión académica-administrativa.
- Relacionada con: FL-001, FL-002, FL-003, FL-004, FL-005

### FL-001 — Acceso y sesión administrativa

- Estado: validado parcialmente
- Fuente: auth API y desktop
- Descripción humana: Flujo de login, renovación de sesión, consulta de usuario actual y salida.
- Lectura: habilita o bloquea la operación protegida.
- Relacionada con: CU-001, ACC-001

### FL-002 — Gestión de estudiantes y representantes

- Estado: validado parcialmente
- Fuente: documentos de negocio
- Descripción humana: Flujo para registrar, actualizar, consultar estudiantes y representantes, y asociar representante principal.
- Lectura: protege la coherencia base estudiante-representante.
- Relacionada con: CU-002, CU-003, ACC-002, ACC-005

### FL-003 — Organización por secciones y planificación académica

- Estado: validado parcialmente
- Fuente: reglas de secciones, asignaturas y clases
- Descripción humana: Flujo para crear secciones, asignaturas, clases y asignar docentes cuando corresponde.
- Lectura: organiza la oferta académica operativa.
- Relacionada con: CU-004, CU-005, CU-006, CU-007, ACC-004, ACC-006, ACC-007

### FL-004 — Registro y consulta de calificaciones

- Estado: validado parcialmente
- Fuente: reglas de calificaciones
- Descripción humana: Flujo para registrar o actualizar notas por estudiante, clase y parcial.
- Lectura: conserva evaluación académica consultable por clase y asignatura asociada.
- Relacionada con: CU-008, ACC-008

### FL-005 — Reportes y auditoría administrativa

- Estado: validado parcialmente
- Fuente: backend reportes y auditoría
- Descripción humana: Flujo para solicitar reportes, monitorear cola, descargar archivo y consultar auditoría.
- Lectura: desacopla generación documental y refuerza trazabilidad.
- Relacionada con: CU-009, CU-010, CU-011, ACC-009, ACC-010

---

## 12. Catálogo único de casos de uso

### CU-001 — Iniciar sesión administrativa

- Estado: validado parcialmente
- Fuente: `POST /api/v1/auth/login`
- Descripción humana: Permite a ADMIN o SECRETARIA ingresar al sistema con credenciales válidas.
- Lectura: si el usuario está activo y las credenciales son correctas, se inicia sesión.
- Relacionada con: ACT-001, ACT-002, ACC-001, PRE-001, POST-001

### CU-002 — Consultar dashboard administrativo

- Estado: validado parcialmente
- Fuente: `GET /api/v1/dashboard/resumen`
- Descripción humana: Presenta resumen operativo de estudiantes, secciones, calificaciones y otros indicadores.
- Lectura: el dashboard orienta la gestión diaria sin reemplazar los módulos de detalle.
- Relacionada con: ACC-011, REP-004

### CU-003 — Registrar estudiante con representante legal

- Estado: validado parcialmente
- Fuente: endpoints estudiantes/representantes
- Descripción humana: Registra estudiante asegurando datos mínimos, representante principal y revisión de duplicidad.
- Lectura: Secretaría o ADMIN crean el expediente básico del estudiante.
- Relacionada con: ACC-002, RN-001, RN-002, RN-004

### CU-004 — Asignar sección vigente a estudiante

- Estado: validado parcialmente
- Fuente: endpoint sección vigente
- Descripción humana: Asocia o cambia la sección actual del estudiante respetando cupo y estado.
- Lectura: representa la matrícula operativa de fase 1 sin historial formal.
- Relacionada con: ACC-003, RN-006, RN-007, SUP-001

### CU-005 — Gestionar secciones

- Estado: validado parcialmente
- Fuente: endpoints secciones
- Descripción humana: ADMIN crea, actualiza o cambia estado de secciones; SECRETARIA consulta.
- Lectura: la estructura por grado/paralelo/año lectivo queda gobernada.
- Relacionada con: ACC-004, RN-007, RN-008

### CU-006 — Gestionar asignaturas

- Estado: validado parcialmente
- Fuente: endpoints asignaturas
- Descripción humana: ADMIN crea y mantiene el catálogo de asignaturas por grado; SECRETARIA consulta.
- Lectura: el catálogo académico sirve de base para clases.
- Relacionada con: RN-009, ENT-005

### CU-007 — Gestionar clases y docentes

- Estado: validado parcialmente
- Fuente: endpoints clases/docentes
- Descripción humana: Crea clases, consulta docentes y asigna docente a clase cuando está definido.
- Lectura: la oferta académica concreta queda planificada.
- Relacionada con: ACC-006, ACC-007, RN-010, RN-011

### CU-008 — Registrar calificaciones por parcial

- Estado: validado parcialmente
- Fuente: endpoints calificaciones
- Descripción humana: Registra o actualiza nota por estudiante, clase y parcial.
- Lectura: la evaluación académica queda normalizada y consultable.
- Relacionada con: ACC-008, RN-012, RN-013, INV-007

### CU-009 — Solicitar reporte administrativo

- Estado: validado parcialmente
- Fuente: endpoints reportes
- Descripción humana: Crea una solicitud en cola para listado de estudiantes, calificaciones o auditoría.
- Lectura: el reporte se prepara fuera de la petición principal.
- Relacionada con: ACC-009, RN-017, REP-001, REP-002, REP-003

### CU-010 — Consultar auditoría operativa

- Estado: validado parcialmente
- Fuente: `GET /api/v1/auditoria/eventos`
- Descripción humana: ADMIN consulta eventos de auditoría filtrables.
- Lectura: permite seguimiento y diagnóstico de operaciones.
- Relacionada con: ACC-010, RN-016, ACT-001

### CU-011 — Reintentar reporte fallido

- Estado: validado parcialmente
- Fuente: endpoint reintento reportes
- Descripción humana: ADMIN solicita reintento de una solicitud de reporte que terminó en error.
- Lectura: la recuperación operativa requiere rol administrativo fuerte.
- Relacionada con: RN-015, RN-017, EST-007, RISK-006

---

## 13. Grafo lógico del negocio

### Backbone textual inicial

```text
MF-001 Administración escolar diaria
├─ FL-001 Acceso y sesión administrativa
│  └─ CU-001 ejecuta ACC-001
├─ FL-002 Gestión de estudiantes y representantes
│  ├─ CU-003 ejecuta ACC-002
│  └─ CU-004 ejecuta ACC-003
├─ FL-003 Organización por secciones y planificación académica
│  ├─ CU-005 ejecuta ACC-004
│  ├─ CU-006 usa ENT-005
│  └─ CU-007 ejecuta ACC-006 y ACC-007
├─ FL-004 Registro y consulta de calificaciones
│  └─ CU-008 ejecuta ACC-008
└─ FL-005 Reportes y auditoría administrativa
   ├─ CU-009 ejecuta ACC-009
   ├─ CU-010 consulta ENT-010
   └─ CU-011 reintenta ACC-009 cuando EST-007
```

### Relaciones semánticas candidatas

- MF-001 contiene FL-001, FL-002, FL-003, FL-004 y FL-005.
- FL-002 usa RN-001, RN-002, RN-004 y RN-006.
- CU-003 ejecuta ACC-002.
- ACC-002 requiere PRE-002, PRE-003 y PRE-004.
- ACC-003 protege INV-005.
- ACC-008 protege INV-007.
- ACC-009 crea ENT-009 y sustenta REP-001, REP-002 o REP-003.
- ACC-010 alimenta ENT-010.

---

## 14. Entidades candidatas

### ENT-001 — Estudiante

- Estado: validado parcialmente
- Fuente: SQL V2 y documentos UENS
- Descripción humana: Niño registrado en la institución, con datos personales, estado, representante legal y sección vigente opcional.
- Lectura: estudiante es el centro de la gestión académica y administrativa.
- Relacionada con: RN-001, RN-002, RN-006, ACC-002, ACC-003

### ENT-002 — Representante legal

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Adulto responsable de uno o varios estudiantes.
- Lectura: provee contacto y responsabilidad institucional.
- Relacionada con: RN-002, RN-003, ACC-005, REL-001

### ENT-003 — Sección

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Grupo escolar identificado por grado, paralelo y año lectivo, con cupo máximo y estado.
- Lectura: agrupa estudiantes y sirve de base para clases.
- Relacionada con: RN-007, RN-008, ACC-004, REL-002

### ENT-004 — Docente

- Estado: validado parcialmente
- Fuente: SQL V2 y reglas de negocio
- Descripción humana: Persona docente registrada para planificación académica.
- Lectura: puede impartir clases, pero no es usuario administrativo en fase 1.
- Relacionada con: SUP-002, ACC-007, REL-005

### ENT-005 — Asignatura

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Materia escolar definida por nombre y grado.
- Lectura: se usa para construir clases, no para registrar horarios directamente.
- Relacionada con: RN-009, RN-010, REL-004

### ENT-006 — Asignación vigente estudiante-sección

- Estado: usable como fuente
- Fuente: `estudiante.seccion_id`
- Descripción humana: Relación actual entre estudiante y sección en fase 1.
- Lectura: no es tabla independiente; es vista lógica del estado vigente.
- Relacionada con: SUP-001, CON-001, RN-006, ACC-003, REL-002

### ENT-007 — Clase

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Oferta concreta de una asignatura dentro de una sección y horario.
- Lectura: conecta sección, asignatura y docente opcional; sirve como referencia para calificaciones.
- Relacionada con: RN-010, RN-011, ACC-006, REL-004, REL-005

### ENT-008 — Calificación

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Nota por estudiante, clase y parcial.
- Lectura: representa evaluación académica consultable y única por combinación.
- Relacionada con: RN-012, RN-013, ACC-008, REL-006

### ENT-009 — Reporte solicitud queue

- Estado: validado parcialmente
- Fuente: SQL V2 y backend reportes
- Descripción humana: Solicitud persistida de generación de reporte.
- Lectura: permite manejo asíncrono de reportes.
- Relacionada con: RN-017, ACC-009, EST-004, EST-005, EST-006, EST-007

### ENT-010 — Auditoría evento

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Evento trazable de operación administrativa o técnica.
- Lectura: guarda módulo, acción, entidad, resultado, actor, rol y fecha.
- Relacionada con: RN-016, ACC-010, EVID-002

### ENT-011 — Usuario sistema administrativo

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Cuenta de acceso al sistema con login, credencial, rol y estado.
- Lectura: separa acceso administrativo del dominio académico.
- Relacionada con: RN-014, RN-015, ACC-001, ACT-001, ACT-002

### ATR-001 — Estudiante.nombres

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Nombres del estudiante.
- Lectura: parte de identidad humana del estudiante.
- Relacionada con: ENT-001

### ATR-002 — Estudiante.apellidos

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Apellidos del estudiante.
- Lectura: parte de identidad humana y detección de duplicados.
- Relacionada con: ENT-001, RN-004

### ATR-003 — Estudiante.fecha_nacimiento

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Fecha usada para calcular edad operativa.
- Lectura: no se persiste edad calculada en V2; se calcula cuando se necesita.
- Relacionada con: RN-001, PRE-002, CALC-002

### ATR-004 — Seccion.cupo_maximo

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Límite operativo de estudiantes por sección.
- Lectura: no debe superar 35 en fase 1.
- Relacionada con: RN-007, INV-005, CALC-001

### ATR-005 — Clase.hora_inicio/hora_fin

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Rango horario atómico de una clase.
- Lectura: hora_fin debe ser posterior a hora_inicio.
- Relacionada con: ENT-007

### ATR-006 — Calificacion.numero_parcial

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Parcial 1 o 2.
- Lectura: fase 1 no admite otros parciales.
- Relacionada con: RN-013, ENT-008

### ATR-007 — ReporteSolicitudQueue.estado

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Estado explícito de solicitud de reporte.
- Lectura: PENDIENTE, EN_PROCESO, COMPLETADA o ERROR.
- Relacionada con: INV-008, ENT-009

### REL-001 — Representante legal representa estudiantes

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Un representante puede estar asociado a varios estudiantes; cada estudiante requiere uno principal.
- Lectura: representante_legal 1..N estudiante.
- Relacionada con: RN-002, RN-003, ENT-001, ENT-002

### REL-002 — Estudiante tiene sección vigente

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Un estudiante puede tener una sección vigente; una sección agrupa muchos estudiantes.
- Lectura: estudiante.seccion_id expresa asignación actual, no historial.
- Relacionada con: SUP-001, ENT-001, ENT-003, ENT-006

### REL-003 — Asignatura pertenece a grado

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: La asignatura está definida para un grado.
- Lectura: ayuda a validar coherencia de clases.
- Relacionada con: RN-009, ENT-005

### REL-004 — Clase une sección y asignatura

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Cada clase pertenece a una sección y una asignatura.
- Lectura: la clase materializa oferta académica operativa.
- Relacionada con: RN-010, RN-011, ENT-003, ENT-005, ENT-007

### REL-005 — Clase puede tener docente

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Una clase puede tener docente asignado o estar pendiente de asignación.
- Lectura: docente_id es opcional en fase 1.
- Relacionada con: ENT-004, ENT-007, ACC-007

### REL-006 — Calificación pertenece a estudiante y clase

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Cada calificación referencia estudiante y clase.
- Lectura: la asignatura se consulta desde la clase.
- Relacionada con: RN-012, ENT-001, ENT-007, ENT-008

---

## 15. Estados y transiciones

### Transiciones principales

```text
Usuario administrativo INACTIVO → ACTIVO: habilitación administrativa.
Estudiante sin sección → ASIGNACION_VIGENTE: asignación a sección.
Sección ACTIVA → INACTIVA: cierre o baja administrativa.
Clase sin docente → clase con docente asignado: planificación completada.
Reporte PENDIENTE → EN_PROCESO → COMPLETADA: procesamiento exitoso.
Reporte PENDIENTE → EN_PROCESO → ERROR: procesamiento fallido.
Reporte ERROR → PENDIENTE: reintento administrativo permitido a ADMIN.
```

### EST-008 — CLASE_PLANIFICADA_SIN_DOCENTE

- Estado: validado parcialmente
- Fuente: docente_id nullable
- Descripción humana: Clase creada sin docente asignado todavía.
- Lectura: es válido temporalmente en fase 1.
- Relacionada con: ACC-006, ACC-007, REL-005

### EST-009 — CLASE_PLANIFICADA_CON_DOCENTE

- Estado: validado parcialmente
- Fuente: reglas de clase
- Descripción humana: Clase con docente definido.
- Lectura: la planificación puntual está más completa.
- Relacionada con: ACC-007, REL-005

---

## 16. Reportes y algoritmos internos

### REP-001 — Listado de estudiantes por sección

- Estado: validado parcialmente
- Fuente: módulo reportes
- Descripción humana: Reporte que lista estudiantes filtrados por sección.
- Lectura: permite a Secretaría verificar composición de secciones.
- Relacionada con: ACC-009, ENT-001, ENT-003, CALC-001

### REP-002 — Calificaciones por sección y parcial

- Estado: validado parcialmente
- Fuente: módulo reportes
- Descripción humana: Reporte de notas por sección, clase/asignatura y parcial.
- Lectura: usa calificaciones registradas por clase y consulta la asignatura desde la clase.
- Relacionada con: ACC-009, ENT-008, RN-012

### REP-003 — Auditoría administrativa de operaciones

- Estado: validado parcialmente
- Fuente: módulo auditoría/reportes
- Descripción humana: Reporte de eventos administrativos para control y diagnóstico.
- Lectura: solo ADMIN debe solicitar o consultar auditoría sensible.
- Relacionada con: ACC-010, ENT-010, RN-015, RN-016

### REP-004 — Resumen de dashboard

- Estado: validado parcialmente
- Fuente: endpoint dashboard
- Descripción humana: Agregados para vista principal: conteos, estados y señales operativas.
- Lectura: resume la salud básica de la operación escolar.
- Relacionada con: ACC-011, CU-002

### CALC-001 — Cantidad de estudiantes por sección

- Estado: validado parcialmente
- Fuente: regla de cupo y SQL V2
- Descripción humana: Conteo calculado de estudiantes cuya sección vigente apunta a una sección dada.
- Lectura: no se persiste como atributo en V2; se calcula para validar cupo y reportes.
- Relacionada con: INV-005, RN-007, REP-001

### CALC-002 — Edad operativa del estudiante

- Estado: validado parcialmente
- Fuente: regla de edad y SQL V2
- Descripción humana: Edad calculada desde fecha de nacimiento en relación con fecha de registro o fecha de referencia.
- Lectura: no se persiste edad; se calcula para validar rango 6–13.
- Relacionada con: RN-001, ATR-003, INV-002

### CALC-003 — Asignatura consultada desde calificación

- Estado: validado parcialmente
- Fuente: relación calificación-clase-asignatura
- Descripción humana: La asignatura visible de una calificación se obtiene desde la clase asociada.
- Lectura: la calificación no necesita FK directa a asignatura.
- Relacionada con: RN-012, REL-006, REP-002

---

## 17. Indicadores para diseño de base de datos

### Decisiones de modelado sugeridas

- `estudiante.edad` no debe persistirse; usar `fecha_nacimiento` y cálculo.
- `seccion.cantidad_estudiantes_registrados` no debe persistirse; usar conteo por `estudiante.seccion_id`.
- `matricula` no debe aparecer como tabla en fase 1; usar asignación vigente estudiante-sección.
- `calificacion` debe tener unicidad por estudiante, clase y parcial.
- `clase` debe conectar sección, asignatura, horario, estado y docente opcional.
- `reporte_solicitud_queue` debe guardar estado explícito, parámetros, resultado/error e intentos.
- `auditoria_evento` debe registrar actor, rol, módulo, acción, entidad, resultado y fecha.

### Indicadores de normalización

- Datos de representante se separan de estudiante.
- Asignatura se separa de clase para no duplicar catálogo académico.
- Clase concentra la oferta académica concreta.
- Reporte se modela como solicitud persistida, no como archivo suelto sin estado.

---

## 18. Riesgos de inconsistencia

### RISK-001 — Duplicidad de estudiantes

- Estado: validado parcialmente
- Fuente: problema observado
- Descripción humana: Registrar dos veces al mismo estudiante por variaciones de nombre o fecha.
- Lectura: afecta reportes, cupos y calificaciones.
- Relacionada con: RN-004, PRE-004, ACC-002

### RISK-002 — Uso de entidad inactiva en operación nueva

- Estado: validado parcialmente
- Fuente: RN-005
- Descripción humana: Usar estudiante, sección, asignatura, docente o clase inactiva en una operación nueva.
- Lectura: rompe la intención operativa del estado INACTIVO.
- Relacionada con: INV-004, RN-005

### RISK-003 — Sobrecupo de sección

- Estado: validado parcialmente
- Fuente: RN-007
- Descripción humana: Asignar más estudiantes que el cupo permitido.
- Lectura: afecta planificación y control institucional.
- Relacionada con: INV-005, ACC-003, CALC-001

### RISK-004 — Confundir matrícula operativa con tabla persistente

- Estado: validado parcialmente
- Fuente: estándar UENS
- Descripción humana: Diseñar una entidad Matrícula inexistente en fase 1 como si fuera tabla real.
- Lectura: puede contaminar ejemplos, diccionario y UML con una entidad falsa.
- Relacionada con: SUP-001, CON-001, ENT-006

### RISK-005 — Calificación duplicada por parcial

- Estado: validado parcialmente
- Fuente: SQL V2
- Descripción humana: Registrar dos notas para el mismo estudiante, clase y parcial.
- Lectura: distorsiona consulta académica y reportes.
- Relacionada con: INV-007, RN-012, ACC-008

### RISK-006 — Reporte procesado dos veces o quedado bloqueado

- Estado: pendiente de validar
- Fuente: diseño de DB queue
- Descripción humana: Concurrencia o error de worker puede duplicar procesamiento o dejar solicitud estancada.
- Lectura: requiere claim transaccional, reintentos y visibilidad de errores.
- Relacionada con: INV-010, ACC-009, CU-011

### RISK-007 — Permisos documentados como implementados sin existir

- Estado: validado parcialmente
- Fuente: estándar UENS
- Descripción humana: Tratar Docente, Dirección, Soporte o Representante como roles de login implementados.
- Lectura: genera matriz de permisos falsa y confusión en ejemplos oficiales.
- Relacionada con: RN-014, CON-005, ACT-003

---

## 19. Uso como fuente para otros artefactos

### Artefactos compatibles sugeridos

1. **Grafo lógico del negocio:** usar MF-001, FL-001..FL-005, CU-001..CU-011, ACC-001..ACC-011, RN/PRE/INV/POST/ENT/REP/RISK/PEND.
2. **Diccionario de datos:** partir de ENT/ATR/REL, pero alinear nombres físicos con SQL V2.
3. **Roles/permisos:** usar ADMIN y SECRETARIA como roles implementados; otros actores solo como stakeholders.
4. **Flujo de pantallas:** usar vistas reales LOGIN, DASHBOARD, ESTUDIANTES, REPRESENTANTES, DOCENTES, SECCIONES, ASIGNATURAS, CLASES, CALIFICACIONES, REPORTES y AUDITORIA.
5. **Wireframes:** maquetar módulos administrativos reales, no pantallas inventadas fuera del shell desktop.
6. **UML Estados:** usar `reporte_solicitud_queue` como ejemplo fuerte de estados PENDIENTE/EN_PROCESO/COMPLETADA/ERROR.
7. **C4/Despliegue:** representar Desktop JavaFX, Backend Spring Boot, PostgreSQL y storage local de reportes.
8. **BPMN/Flujo operativo:** modelar registrar estudiante, asignar sección, registrar calificación y solicitar reporte.

### Nota de importación

Este documento debe poder importarse como `logical-business-intake` y servir como fuente lógica revisable, no como generador silencioso de todos los diagramas.

---

## 20. Preguntas pendientes para el cliente

### PEND-001 — ¿La edad 6–13 debe bloquear o solo advertir?

- Estado: pendiente
- Fuente: regla de edad
- Descripción humana: Falta confirmar si fuera de rango bloquea registro o genera advertencia administrativa.
- Afecta a: RN-001, PRE-002, INV-002
- Prioridad: alta

### PEND-002 — ¿Se necesita historial formal de cambios de sección?

- Estado: pendiente
- Fuente: SUP-001
- Descripción humana: La fase 1 usa asignación vigente única, pero podría requerirse historial futuro.
- Afecta a: SUP-001, ENT-006, REL-002
- Prioridad: media

### PEND-003 — ¿Cuál es la escala definitiva de calificaciones?

- Estado: pendiente
- Fuente: regla de calificación
- Descripción humana: El SQL V2 usa 0..10, pero debe confirmarse si la institución usa otra escala o equivalencias.
- Afecta a: RN-012, SUP-003, ACC-008
- Prioridad: media

### PEND-004 — ¿Qué reportes exactos necesita Dirección?

- Estado: pendiente
- Fuente: módulo reportes
- Descripción humana: Hay reportes implementados, pero conviene validar prioridades, formatos y filtros finales.
- Afecta a: REP-001, REP-002, REP-003
- Prioridad: media

### PEND-005 — ¿Qué operaciones exactas deben auditarse obligatoriamente?

- Estado: pendiente
- Fuente: auditoría operativa
- Descripción humana: Se debe definir si todos los CRUD, solo sensibles o errores deben generar auditoría obligatoria.
- Afecta a: RN-016, ACC-010, ENT-010
- Prioridad: media

### PEND-006 — ¿SECRETARIA puede crear docentes o solo consultarlos?

- Estado: pendiente
- Fuente: API actual permite creación por ADMIN y SECRETARIA
- Descripción humana: Debe validarse si la operación real permite a Secretaría crear docentes o si eso debe restringirse.
- Afecta a: RN-015, CU-007, ACT-002
- Prioridad: baja

---

## 21. Nivel de madurez del levantamiento

### Fortalezas

- El dominio escolar de fase 1 está bien delimitado.
- Las entidades físicas reales están identificadas en SQL V2.
- Las reglas de cupo, representante, calificaciones, estados y roles están expresadas con claridad.
- La cola de reportes aporta un caso fuerte para estados y procesamiento asíncrono.
- La auditoría permite trazabilidad operativa.
- La distinción entre roles implementados y actores de negocio evita una matriz de permisos falsa.

### Bloqueadores

- Falta validación cliente final sobre edad, historial de sección, escala de notas y alcance de auditoría.
- Algunos ejemplos oficiales previos pueden haber usado “Matrícula” como entidad demasiado fuerte.
- Los stakeholders no implementados deben marcarse como planificados si aparecen en artefactos compatibles.

### Siguientes pasos

1. Importar este Markdown como Levantamiento lógico.
2. Construir un grafo lógico UENS revisable.
3. Ajustar ejemplos oficiales UENS existentes para respetar este documento.
4. Sincronizar `examples/markdown` y `ai-resources/official-markdown`.
5. Agregar tests de coherencia para no volver a tratar matrícula como tabla real ni roles no implementados como roles del sistema.

---

## 22. Cierre del documento

Este levantamiento lógico convierte la operación administrativa de UENS en una fuente lógica canónica y trazable. La estructura prioriza acciones, reglas, estados y evidencias antes que tablas o pantallas. La base real del sistema se mantiene: ADMIN y SECRETARIA son los roles implementados; la asignación vigente estudiante-sección reemplaza cualquier idea de matrícula persistente en fase 1; los reportes funcionan con cola asíncrona; y la auditoría conserva trazabilidad operativa.

El documento queda listo para ser usado como ejemplo oficial gordito del Levantamiento lógico y como fuente de alineación para otros artefactos UENS revisables.
