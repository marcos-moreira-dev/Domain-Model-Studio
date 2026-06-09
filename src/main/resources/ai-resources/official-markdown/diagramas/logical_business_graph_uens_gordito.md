---
dms_version: "1"
diagram_type: "logical-business-graph"
name: "UENS — grafo lógico del negocio escolar"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
contract: "logical-business-graph-v1"
importable: true
intended_output: "diagrama visual"
description: "Vista lógica compatible sustentada en el levantamiento lógico UENS. Usa teoría interna del producto: MF, FL, CU, ACC, RN, PRE, INV, POST, ENT, EST, REP, RISK y PEND."
---
# Leyenda

| Abreviación | Significado | Uso dentro del grafo |
|---|---|---|
| MF | Macroflujo | Agrupa una gran operación del negocio escolar. |
| FL | Flujo o microflujo | Describe una variante operativa concreta dentro de un macroflujo. |
| CU | Caso de uso | Funcionalidad observable y reutilizable del sistema. |
| ACC | Acción transformadora | Acción que cambia datos, estados o evidencias. |
| RN | Regla de negocio | Restricción, política o criterio lógico del dominio. |
| PRE | Precondición | Verdad necesaria antes de ejecutar un flujo, caso o acción. |
| INV | Invariante | Verdad que debe mantenerse durante la operación. |
| POST | Postcondición | Verdad verificable al cerrar una acción, caso o flujo. |
| ENT | Entidad candidata | Concepto persistible identificado desde el levantamiento lógico. |
| EST | Estado | Situación válida del negocio o de una entidad. |
| REP | Reporte | Salida informativa construida desde datos y reglas. |
| RISK | Riesgo | Riesgo lógico u operativo detectado. |
| PEND | Pregunta pendiente | Duda que bloquea validación o diseño definitivo. |

# Nodos

| Código | Tipo | Título | Descripción | Estado | Referencias |
|---|---|---|---|---|---|
| MF-001 | MF | Gestión académica administrativa | Agrupa registro de estudiantes, representantes, secciones, clases y asignación vigente estudiante-sección. | validado parcialmente | levantamiento UENS |
| MF-002 | MF | Evaluación y reportes | Agrupa calificaciones, reportes asíncronos y consulta institucional. | en revisión | backend reportes |
| MF-003 | MF | Seguridad y trazabilidad | Agrupa usuarios, roles implementados y auditoría. | validado parcialmente | backend seguridad |
| FL-001 | FL | Registro y actualización de estudiante | Mantiene expediente básico del estudiante y su representante. | validado parcialmente | MF-001 |
| FL-002 | FL | Asignación vigente a sección | Actualiza la sección vigente del estudiante usando estudiante.seccion_id. | validado parcialmente | MF-001 |
| FL-003 | FL | Planificación académica base | Mantiene docentes, asignaturas y clases. | en revisión | MF-001 |
| FL-004 | FL | Registro de calificaciones | Registra notas por estudiante, clase y parcial. | en revisión | MF-002 |
| FL-005 | FL | Generación de reportes | Solicita, procesa y descarga reportes XLSX/PDF/DOCX. | en revisión | MF-002 |
| FL-006 | FL | Auditoría administrativa | Registra y consulta eventos relevantes. | validado parcialmente | MF-003 |
| CU-001 | CU | Gestionar estudiante | Crear, buscar, actualizar e inactivar estudiante. | validado parcialmente | FL-001 |
| CU-002 | CU | Gestionar representante legal | Crear, buscar y actualizar responsable. | validado parcialmente | FL-001 |
| CU-003 | CU | Asignar sección vigente | Asociar estudiante con sección activa y cupo disponible. | validado parcialmente | FL-002 |
| CU-004 | CU | Gestionar clase | Crear o actualizar clase con sección, asignatura, horario y docente opcional. | en revisión | FL-003 |
| CU-005 | CU | Registrar calificación | Guardar nota por estudiante, clase y parcial. | en revisión | FL-004 |
| CU-006 | CU | Solicitar reporte | Crear solicitud en reporte_solicitud_queue. | en revisión | FL-005 |
| CU-007 | CU | Descargar reporte | Obtener archivo generado cuando la solicitud está COMPLETADA. | en revisión | FL-005 |
| CU-008 | CU | Revisar auditoría | Consultar eventos por fecha, módulo, acción, actor o resultado. | validado parcialmente | FL-006 |
| CU-009 | CU | Administrar usuarios | Crear, bloquear o actualizar usuarios ADMIN/SECRETARIA. | validado parcialmente | MF-003 |
| ACC-001 | ACC | Guardar estudiante | Crea o actualiza datos del estudiante. | validado parcialmente | CU-001 |
| ACC-002 | ACC | Vincular representante | Asocia estudiante con representante legal. | validado parcialmente | CU-002 |
| ACC-003 | ACC | Actualizar sección vigente | Modifica estudiante.seccion_id. | validado parcialmente | CU-003 |
| ACC-004 | ACC | Guardar clase | Persiste oferta de asignatura por sección y horario. | en revisión | CU-004 |
| ACC-005 | ACC | Guardar calificación | Persiste nota y observación. | en revisión | CU-005 |
| ACC-006 | ACC | Crear solicitud de reporte | Inserta solicitud PENDIENTE con parámetros JSON. | en revisión | CU-006 |
| ACC-007 | ACC | Registrar evento de auditoría | Guarda evidencia de acción relevante. | validado parcialmente | CU-008 |
| RN-001 | RN | Representante obligatorio | Todo estudiante debe tener representante legal en fase actual. | validado parcialmente | estudiante |
| RN-002 | RN | Sección vigente única | Un estudiante apunta como máximo a una sección vigente mediante seccion_id. | validado parcialmente | estudiante.seccion_id |
| RN-003 | RN | Cupo de sección | La sección debe respetar cupo_maximo. | en revisión | seccion |
| RN-004 | RN | Nota válida | Toda nota debe estar en rango 0..10 y parcial 1/2. | validado parcialmente | calificacion |
| RN-005 | RN | Roles implementados | Solo ADMIN y SECRETARIA son roles de login actuales. | validado parcialmente | usuario |
| RN-006 | RN | Reporte por estados | La solicitud de reporte solo usa PENDIENTE, EN_PROCESO, COMPLETADA o ERROR. | validado parcialmente | reporte_solicitud_queue |
| PRE-001 | PRE | Datos mínimos presentes | Antes de guardar estudiante deben existir nombres, apellidos y fecha de nacimiento. | validado parcialmente | CU-001 |
| PRE-002 | PRE | Representante identificado | Antes de asignar sección debe existir representante legal. | validado parcialmente | CU-003 |
| PRE-003 | PRE | Sección activa con cupo | Antes de asignar sección debe existir sección ACTIVA con cupo disponible. | en revisión | CU-003 |
| PRE-004 | PRE | Clase y parcial válidos | Antes de calificar debe existir clase y número parcial válido. | en revisión | CU-005 |
| PRE-005 | PRE | Usuario autorizado | Antes de operar debe existir sesión con ADMIN o SECRETARIA. | validado parcialmente | seguridad |
| INV-001 | INV | Sin tabla matrícula | El flujo no debe introducir una entidad persistente matrícula inexistente en V2. | validado | estándar UENS |
| INV-002 | INV | Calificación única | No puede duplicarse calificación por estudiante, clase y parcial. | validado parcialmente | calificacion |
| INV-003 | INV | Reporte trazable | Toda solicitud conserva solicitante, estado, parámetros e intentos cuando aplique. | en revisión | reportes |
| INV-004 | INV | Auditoría consultable | Las operaciones sensibles deben dejar evento consultable. | en revisión | auditoria |
| POST-001 | POST | Estudiante guardado | Al cerrar registro existe estudiante con datos mínimos. | validado parcialmente | CU-001 |
| POST-002 | POST | Sección vigente actualizada | Al cerrar asignación, estudiante.seccion_id refleja la sección vigente. | validado parcialmente | CU-003 |
| POST-003 | POST | Calificación registrada | Al cerrar registro de nota existe calificación válida. | en revisión | CU-005 |
| POST-004 | POST | Reporte solicitado | Al cerrar solicitud existe registro PENDIENTE en la cola. | en revisión | CU-006 |
| ENT-001 | ENT | Estudiante | Entidad persistente de datos del alumno. | validado | BD V2 |
| ENT-002 | ENT | Representante legal | Entidad persistente del responsable legal. | validado | BD V2 |
| ENT-003 | ENT | Sección | Entidad persistente de grado, paralelo y año lectivo. | validado | BD V2 |
| ENT-004 | ENT | Clase | Entidad persistente de oferta académica. | validado | BD V2 |
| ENT-005 | ENT | Calificación | Entidad persistente de nota por estudiante/clase/parcial. | validado | BD V2 |
| ENT-006 | ENT | Reporte solicitud queue | Entidad persistente de reportes asíncronos. | validado | BD V2 |
| ENT-007 | ENT | Auditoría evento | Entidad persistente de trazabilidad. | validado | BD V2 |
| ENT-008 | ENT | Usuario sistema administrativo | Entidad persistente de cuenta ADMIN/SECRETARIA. | validado | BD V2 |
| EST-001 | EST | Estudiante ACTIVO | Estado operativo para estudiante habilitado. | validado parcialmente | estudiante |
| EST-002 | EST | Reporte PENDIENTE | Solicitud creada pero no procesada. | validado | reporte_solicitud_queue |
| EST-003 | EST | Reporte EN_PROCESO | Worker tomó la solicitud. | validado | reporte_solicitud_queue |
| EST-004 | EST | Reporte COMPLETADA | Archivo generado. | validado | reporte_solicitud_queue |
| EST-005 | EST | Reporte ERROR | Generación fallida con detalle. | validado | reporte_solicitud_queue |
| REP-001 | REP | Listado de estudiantes por sección | Reporte operativo para Secretaría/Dirección. | en revisión | reportes |
| REP-002 | REP | Calificaciones por sección y parcial | Reporte académico principal. | en revisión | reportes |
| REP-003 | REP | Auditoría de operaciones administrativas | Reporte sensible para ADMIN. | en revisión | auditoría |
| RISK-001 | RISK | Duplicidad de estudiante | Riesgo por captura manual o nombres similares. | en revisión | secretaría |
| RISK-002 | RISK | Sección sobre cupo | Riesgo de asignar estudiante a sección llena. | en revisión | seccion |
| RISK-003 | RISK | Reporte sin archivo | Riesgo de solicitud COMPLETADA sin archivo descargable. | en revisión | reportes |
| PEND-001 | PEND | Confirmar política de cupos | Definir si SECRETARIA puede sobrepasar cupo con autorización. | bloqueado | dirección |
| PEND-002 | PEND | Confirmar escala final | Confirmar si la escala 0..10 y dos parciales se mantiene. | bloqueado | coordinación |

# Relaciones

| ID | Origen | Relación | Destino | Descripción |
|---|---|---|---|---|
| rel-001 | MF-001 | contiene | FL-001 | La gestión académica contiene registro de estudiante. |
| rel-002 | MF-001 | contiene | FL-002 | La gestión académica contiene asignación vigente. |
| rel-003 | MF-001 | contiene | FL-003 | La gestión académica contiene planificación de clases. |
| rel-004 | MF-002 | contiene | FL-004 | Evaluación contiene calificaciones. |
| rel-005 | MF-002 | contiene | FL-005 | Evaluación contiene reportes. |
| rel-006 | MF-003 | contiene | FL-006 | Seguridad y trazabilidad contienen auditoría. |
| rel-007 | FL-001 | usa | CU-001 | El flujo usa gestión de estudiante. |
| rel-008 | FL-001 | usa | CU-002 | El flujo usa gestión de representante. |
| rel-009 | FL-002 | usa | CU-003 | Asignación vigente usa el caso de sección. |
| rel-010 | FL-003 | usa | CU-004 | Planificación usa gestión de clase. |
| rel-011 | FL-004 | usa | CU-005 | Calificaciones usa registro de nota. |
| rel-012 | FL-005 | usa | CU-006 | Reportes usa solicitud asíncrona. |
| rel-013 | FL-005 | usa | CU-007 | Reportes usa descarga. |
| rel-014 | FL-006 | usa | CU-008 | Auditoría usa revisión de eventos. |
| rel-015 | FL-006 | usa | CU-009 | Auditoría administrativa usa administración de usuarios como soporte de seguridad. |
| rel-016 | CU-001 | ejecuta | ACC-001 | Gestionar estudiante guarda datos. |
| rel-017 | CU-002 | ejecuta | ACC-002 | Gestionar representante vincula responsable. |
| rel-018 | CU-003 | ejecuta | ACC-003 | Asignar sección actualiza estudiante.seccion_id. |
| rel-019 | CU-004 | ejecuta | ACC-004 | Gestionar clase guarda oferta académica. |
| rel-020 | CU-005 | ejecuta | ACC-005 | Registrar calificación guarda nota. |
| rel-021 | CU-006 | ejecuta | ACC-006 | Solicitar reporte crea solicitud. |
| rel-022 | CU-008 | ejecuta | ACC-007 | Revisar auditoría depende de eventos registrados. |
| rel-023 | RN-001 | aplica | CU-002 | Representante obligatorio aplica al responsable legal. |
| rel-024 | RN-002 | aplica | ACC-003 | Sección vigente única aplica al cambio de seccion_id. |
| rel-025 | RN-003 | aplica | ACC-003 | Cupo aplica antes de asignar sección. |
| rel-026 | RN-004 | aplica | ACC-005 | Nota válida aplica al registro de calificación. |
| rel-027 | RN-005 | aplica | CU-009 | Roles implementados aplican a administración de usuarios. |
| rel-028 | RN-006 | aplica | ACC-006 | Estados de reporte aplican al alta de solicitud. |
| rel-029 | ACC-001 | requiere | PRE-001 | Guardar estudiante requiere datos mínimos. |
| rel-030 | ACC-003 | requiere | PRE-002 | Asignar sección requiere representante. |
| rel-031 | ACC-003 | requiere | PRE-003 | Asignar sección requiere cupo. |
| rel-032 | ACC-005 | requiere | PRE-004 | Guardar nota requiere clase/parcial válidos. |
| rel-033 | CU-009 | requiere | PRE-005 | Administración requiere usuario autorizado. |
| rel-034 | ACC-003 | protege | INV-001 | La acción protege contra inventar tabla matrícula. |
| rel-035 | ACC-005 | protege | INV-002 | Guardar nota protege unicidad de calificación. |
| rel-036 | ACC-006 | protege | INV-003 | Solicitar reporte protege trazabilidad de la cola. |
| rel-037 | ACC-007 | protege | INV-004 | Auditoría mantiene eventos consultables. |
| rel-038 | ACC-001 | garantiza | POST-001 | Guardar estudiante garantiza registro mínimo. |
| rel-039 | ACC-003 | garantiza | POST-002 | Asignar sección garantiza seccion_id actualizado. |
| rel-040 | ACC-005 | garantiza | POST-003 | Guardar nota garantiza calificación registrada. |
| rel-041 | ACC-006 | garantiza | POST-004 | Solicitar reporte garantiza solicitud pendiente. |
| rel-042 | ACC-001 | crea | ENT-001 | Guardar estudiante crea/modifica estudiante. |
| rel-043 | ACC-002 | crea | ENT-002 | Vincular representante crea/modifica representante. |
| rel-044 | ACC-003 | modifica | ENT-001 | Asignar sección modifica estudiante. |
| rel-045 | ACC-004 | crea | ENT-004 | Guardar clase crea clase. |
| rel-046 | ACC-005 | crea | ENT-005 | Guardar nota crea calificación. |
| rel-047 | ACC-006 | crea | ENT-006 | Solicitar reporte crea cola de reporte. |
| rel-048 | ACC-007 | crea | ENT-007 | Auditoría crea evento. |
| rel-049 | CU-009 | modifica | ENT-008 | Administrar usuarios modifica cuentas. |
| rel-050 | CU-003 | consulta | ENT-003 | Asignar sección consulta sección activa y cupo. |
| rel-051 | CU-005 | consulta | ENT-004 | Registrar calificación consulta clase válida. |
| rel-052 | ENT-005 | alimenta | REP-002 | Calificación alimenta reporte académico. |
| rel-053 | ENT-006 | alimenta | REP-001 | La cola alimenta reportes solicitados. |
| rel-054 | ENT-006 | alimenta | REP-002 | La cola alimenta reportes académicos. |
| rel-055 | ENT-007 | alimenta | REP-003 | Auditoría alimenta reporte de operaciones. |
| rel-056 | EST-002 | alimenta | REP-001 | El estado PENDIENTE alimenta monitoreo de solicitudes. |
| rel-057 | EST-004 | habilita | CU-007 | El estado COMPLETADA habilita descarga de reporte. |
| rel-058 | EST-005 | alimenta | REP-003 | El estado ERROR alimenta auditoría de operaciones. |
| rel-059 | RISK-001 | bloquea | FL-001 | Duplicidad bloquea registro confiable. |
| rel-060 | RISK-002 | bloquea | FL-002 | Cupo excedido bloquea asignación. |
| rel-061 | RISK-003 | bloquea | CU-007 | Sin archivo no se puede descargar reporte. |
| rel-062 | PEND-001 | bloquea | RN-003 | Política de cupos pendiente bloquea regla final. |
| rel-063 | PEND-002 | bloquea | RN-004 | Escala final pendiente bloquea cierre académico. |

# Observaciones

Este grafo se alinea con el levantamiento lógico UENS. No pretende ser UML ni BPMN: muestra trazabilidad lógica entre macroflujos, casos de uso, acciones, reglas, estados, entidades, reportes, riesgos y preguntas pendientes.
