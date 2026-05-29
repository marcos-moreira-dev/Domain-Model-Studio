# PF08 — Matriz de ejemplos oficiales UENS/colegio

Estado: **planificado**  
Tipo: **matriz de contenido**

## 1. Matriz principal

| Tipo visible | Archivo futuro sugerido | Dominio | Propósito del ejemplo | Relación con otros ejemplos |
|---|---|---|---|---|
| Modelo conceptual | `uens_conceptual_model_completo.md` | Colegio/UENS | Mostrar entidades, atributos y relaciones principales del dominio escolar. | Base semántica para diccionario, clases, módulos y reportes. |
| Diccionario de datos | `uens_data_dictionary_completo.md` | Colegio/UENS | Documentar campos, validaciones y reglas de datos. | Deriva del modelo conceptual y alimenta requerimientos de BD/API. |
| Mapa de módulos | `uens_admin_module_map_completo.md` | Colegio/UENS | Organizar módulos administrativos y responsabilidades. | Relaciona dominio con navegación y permisos. |
| Roles y permisos | `uens_roles_permissions_completo.md` | Colegio/UENS | Mostrar límites operativos por rol. | Conecta actores de casos de uso con módulos y pantallas. |
| Flujo de pantallas | `uens_screen_flow_completo.md` | Colegio/UENS | Representar navegación administrativa. | Conecta mapa de módulos con wireframes. |
| Wireframes administrativos | `uens_admin_wireframes_completo.md` | Colegio/UENS | Mostrar maquetas de pantallas operativas. | Concreta flujo de pantallas y requerimientos. |
| UML Clases | `uens_uml_class_completo.md` | Colegio/UENS | Mostrar estructura de clases agrupadas por módulo/paquete. | Traduce dominio a diseño de software sin ser BD física. |
| BPMN básico | `uens_bpmn_matricula_completo.md` | Colegio/UENS | Modelar proceso formal de matrícula. | Conecta reglas de negocio, casos de uso y actividad. |
| Flujo operativo | `uens_operational_flow_secretaria_completo.md` | Colegio/UENS | Explicar operación diaria de secretaría en lenguaje menos formal. | Sirve para levantar información antes de BPMN/UML. |
| UML Casos de uso | `uens_uml_use_case_completo.md` | Colegio/UENS | Representar actores y funcionalidades observables. | Conecta roles, módulos y requerimientos funcionales. |
| UML Actividad | `uens_uml_activity_registrar_matricula.md` | Colegio/UENS | Detallar el flujo de una operación específica. | Baja un caso de uso o proceso a pasos internos. |
| UML Secuencia | `uens_uml_sequence_registrar_calificacion.md` | Colegio/UENS | Mostrar interacción temporal entre usuario, UI, backend, dominio y BD. | Conecta wireframe, API y clases. |
| UML Estados | `uens_uml_state_solicitud_reporte.md` | Colegio/UENS | Mostrar ciclo de vida de una solicitud de reporte. | Conecta reportes, backend y operación. |
| C4 Contexto | `uens_c4_context_completo.md` | Colegio/UENS | Ubicar el sistema frente a usuarios y sistemas externos. | Base de arquitectura de alto nivel. |
| C4 Contenedores | `uens_c4_containers_completo.md` | Colegio/UENS | Mostrar aplicaciones, backend, BD y servicios. | Baja el contexto a arquitectura técnica. |
| Despliegue técnico | `uens_technical_deployment_completo.md` | Colegio/UENS | Mostrar dónde corre cada parte y cómo se opera. | Conecta arquitectura con entrega real. |

## 2. Niveles de ejemplo

Conviene manejar dos niveles:

### 2.1 Plantilla oficial

Archivo vacío o semi-vacío que enseña la gramática del tipo.

Uso:

```text
Quiero crear mi propio diagrama desde cero con estructura correcta.
```

### 2.2 Ejemplo completo UENS

Archivo con contenido realista y suficiente.

Uso:

```text
Quiero ver cómo se modela un sistema administrativo escolar realista.
```

## 3. Metadatos esperados

Cada ejemplo debería declarar metadatos compatibles con el catálogo actual y ampliables para el selector.

Front matter futuro sugerido:

```yaml
---
diagram_type: "uml-use-case"
importable: "true"
example_family: "uens"
domain: "Sistema administrativo escolar"
level: "completo"
title: "UENS — Casos de uso del sistema escolar"
summary: "Actores y funcionalidades observables de una aplicación administrativa escolar."
teaches:
  - "actores"
  - "casos de uso"
  - "include/extend"
  - "límites del sistema"
related_examples:
  - "uens_roles_permissions_completo.md"
  - "uens_admin_module_map_completo.md"
---
```

La implementación puede simplificar este esquema si el parser actual no soporta listas YAML. La regla importante es no perder metadatos clave.

## 4. Criterios de consistencia de familia

Un ejemplo pertenece a la familia UENS si:

- usa el mismo vocabulario de dominio;
- no mezcla restaurante, óptica, ventas genéricas o soporte ajeno;
- reutiliza actores compatibles;
- reutiliza módulos compatibles;
- no contradice reglas principales del dominio escolar;
- puede explicarse como otra vista del mismo sistema.

## 5. Riesgos

| Riesgo | Mitigación |
|---|---|
| Ejemplos demasiado grandes | Mantener tamaño “gordito pero legible”; no hacer mega-diagramas inmanejables. |
| Mezclar modelo conceptual con BD física | Mantener lenguaje conceptual; no convertir entidades en tablas salvo en diccionario si aplica. |
| Sobrecargar UML Clases | Usar agrupadores por paquete/módulo y clases representativas. |
| Wireframes demasiado visuales | Mantenerlos como maquetas estructurales, no diseño gráfico final. |
| Selector lleno de ifs | Usar catálogo de metadatos y clases pequeñas. |
| Prometer importación falsa | Distinguir importable, referencia y plantilla. |
