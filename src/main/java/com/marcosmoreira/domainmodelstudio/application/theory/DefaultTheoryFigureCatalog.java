package com.marcosmoreira.domainmodelstudio.application.theory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Catálogo textual de figuras didácticas disponibles para la referencia académica. */
public final class DefaultTheoryFigureCatalog {

    private static final List<TheoryFigureReference> FIGURES = List.of(
            figure("conceptual-chen-symbols", "Entidad, atributo y relación en notación Chen."),
            figure("conceptual-entity-symbol", "Entidad como cosa relevante del dominio."),
            figure("conceptual-attribute-symbol", "Atributo como dato que describe una entidad."),
            figure("conceptual-relationship-symbol", "Relación como vínculo con significado entre entidades."),
            figure("conceptual-cardinality-symbol", "Cardinalidades y opcionalidad en lectura entidad-relación."),
            figure("conceptual-weak-entity-symbol", "Entidad débil dependiente de una entidad principal."),
            figure("conceptual-associative-entity-symbol", "Entidad asociativa para una relación muchos a muchos con datos propios."),
            figure("conceptual-multivalued-attribute-symbol", "Atributo multivaluado con varias ocurrencias para una entidad."),
            figure("conceptual-derived-attribute-symbol", "Atributo derivado calculado desde otros datos."),
            figure("conceptual-chen-vs-crow-foot", "La misma relación vista en Chen y pata de gallo."),
            figure("conceptual-admin-example", "Microejemplo administrativo de cliente, equipo, orden, pago y repuesto."),
            figure("conceptual-common-errors", "Errores comunes al confundir entidad con pantalla, acción o tabla física."),
            figure("data-dictionary-table", "Tabla didáctica de entidad, campo, tipo lógico y regla."),
            figure("data-dictionary-from-concept", "Del modelo conceptual al campo documentado con nombre, tipo y regla."),
            figure("data-dictionary-traceability", "Trazabilidad del dato desde formulario hasta reporte y regla de negocio."),
            figure("data-dictionary-columns", "Columnas recomendadas para documentar un dato con precisión."),
            figure("data-dictionary-allowed-values", "Valores permitidos para estados, categorías y catálogos."),
            figure("data-dictionary-validation", "Obligatoriedad y validación según momento, estado o condición."),
            figure("data-dictionary-calculated-field", "Dato calculado a partir de otros datos base."),
            figure("data-dictionary-sensitive-data", "Dato sensible con acceso restringido por rol o permiso."),
            figure("data-dictionary-audit-fields", "Campos de auditoría para trazabilidad humana."),
            figure("data-dictionary-common-errors", "Errores comunes en nombres vagos, reglas ausentes y campos sin trazabilidad."),
            figure("admin-module-map", "Módulos administrativos y dependencia funcional entre áreas."),
            figure("admin-module-overview", "Sistema administrativo organizado en módulos funcionales principales."),
            figure("admin-module-hierarchy", "Jerarquía entre módulo, submódulo y función."),
            figure("admin-module-dependencies", "Dependencias funcionales entre módulos administrativos."),
            figure("admin-module-phases", "Módulos organizados por MVP, versión inicial y futuro."),
            figure("admin-module-transversal", "Módulos núcleo, de soporte y transversales."),
            figure("admin-module-vs-screen", "Diferencia entre módulo funcional y pantalla de interfaz."),
            figure("admin-module-common-errors", "Errores comunes al confundir módulo con pantalla, tabla o botón."),
            figure("roles-permissions-chain", "Rol, permiso y recurso como cadena de autorización."),
            figure("roles-permissions-user-role-permission", "Usuario concreto asignado a roles que agrupan permisos."),
            figure("roles-permissions-matrix", "Matriz rol-permiso para comparar autorizaciones por perfil."),
            figure("roles-permissions-module-actions", "Permisos concretos dentro de un módulo administrativo."),
            figure("roles-permissions-state-based", "Autorizaciones que dependen del estado del registro."),
            figure("roles-permissions-common-errors", "Errores frecuentes al definir roles vagos o permisos demasiado amplios."),
            figure("screen-flow-navigation", "Flujo de pantallas con navegación principal y alternativa."),
            figure("screen-flow-basic", "Flujo básico entre pantallas principales de una aplicación."),
            figure("screen-flow-branching", "Pantalla con varias salidas según acciones de navegación."),
            figure("screen-flow-by-role", "Recorridos de pantalla diferentes según rol de usuario."),
            figure("screen-flow-error-validation", "Validación, error y confirmación dentro de un flujo de pantallas."),
            figure("screen-flow-vs-wireframe", "Diferencia entre flujo de pantallas y contenido interno de un wireframe."),
            figure("screen-flow-common-errors", "Errores comunes al confundir proceso, pantalla y maqueta visual."),
            figure("admin-wireframe-layout", "Wireframe administrativo con barra superior, menú, tabla y formulario."),
            figure("admin-wireframe-layout-base", "Estructura base de una pantalla administrativa con barra superior, menú lateral y área de trabajo."),
            figure("admin-wireframe-primitives", "Primitivas visuales básicas para maquetar pantallas administrativas."),
            figure("admin-wireframe-screen-linking", "Flechas entre maquetas como apoyo de navegación, sin reemplazar el flujo de pantallas."),
            figure("admin-wireframe-vs-screen-flow", "Diferencia entre flujo de pantallas y wireframe administrativo."),
            figure("admin-wireframe-common-boundaries", "Límites del wireframe: estructura funcional, no diseño final ni frontend real."),
            figure("admin-wireframe-hierarchy", "Jerarquía visual con título, filtros, acción principal y tabla dominante."),
            figure("admin-wireframe-grouping", "Agrupación de campos por proximidad, sección y propósito."),
            figure("admin-wireframe-primary-secondary-actions", "Acciones primarias, secundarias y peligrosas con pesos distintos."),
            figure("admin-wireframe-validation-feedback", "Feedback y validación cerca del campo afectado."),
            figure("admin-wireframe-danger-action", "Acción peligrosa separada con confirmación y motivo."),
            figure("admin-wireframe-progressive-disclosure", "Revelación progresiva mediante resumen, pestañas y detalles."),
            figure("admin-wireframe-table-reading", "Lectura rápida de tablas con filtros, estado y acciones."),
            figure("admin-wireframe-state-visibility", "Estado visible de un registro y acciones condicionadas."),

            figure("admin-wireframe-crud-pattern", "Patrón CRUD/catálogo con filtros, tabla, acción principal y acciones por fila."),
            figure("admin-wireframe-wizard-pattern", "Wizard administrativo con pasos, validación por etapa y resumen final."),
            figure("admin-wireframe-expediente-pattern", "Expediente o caso vivo con cabecera, estado, tabs, historial y acciones."),
            figure("admin-wireframe-bandeja-pattern", "Bandeja operativa para pendientes, prioridad, detalle lateral y acciones rápidas."),
            figure("admin-wireframe-dashboard-pattern", "Dashboard con tarjetas resumen, alertas y accesos rápidos."),
            figure("admin-wireframe-config-pattern", "Pantalla de configuración con menú de secciones y parámetros editables."),
            figure("admin-wireframe-approval-pattern", "Pantalla de aprobación con contexto suficiente para decidir."),
            figure("admin-wireframe-report-pattern", "Reporte administrativo con filtros, resumen, tabla y exportación."),
            figure("admin-wireframe-search-pattern", "Búsqueda especializada con filtros avanzados y resultados accionables."),
            figure("admin-wireframe-document-pattern", "Patrón documental con archivos, tipo, fecha, responsable y acciones."),
            figure("admin-wireframe-calendar-pattern", "Agenda o calendario cuando el tiempo organiza el trabajo."),
            figure("admin-wireframe-repair-module-example", "Módulo Reparaciones como conjunto de listado, detalle y acciones principales."),
            figure("admin-wireframe-order-detail-example", "Detalle de orden como expediente administrativo con estado, pestañas y acciones."),
            figure("admin-wireframe-role-state-actions", "Acciones visibles según rol y estado de la orden."),
            figure("admin-wireframe-empty-error-states", "Estados vacíos, carga y errores recuperables en pantallas administrativas."),
            figure("admin-wireframe-ai-specification", "Conversión del wireframe en especificación textual para IA o frontend."),
            figure("bpmn-basic-symbols", "Evento, tarea, compuerta y flujo de secuencia en BPMN básico."),
            figure("bpmn-basic-linear-process", "Proceso BPMN lineal con inicio, tareas y fin."),
            figure("bpmn-basic-gateway", "Gateway BPMN con caminos alternativos nombrados."),
            figure("bpmn-basic-lanes", "Lanes BPMN para mostrar responsables del proceso."),
            figure("bpmn-basic-as-is-to-be", "Comparación entre proceso actual AS-IS y proceso propuesto TO-BE."),
            figure("bpmn-basic-common-errors", "Errores comunes al modelar tareas vagas, gateways sin condición o pantallas como proceso."),
            figure("operational-flow-swimlane", "Flujo operativo con carriles de responsabilidad."),
            figure("operational-flow-linear", "Flujo operativo lineal con pasos claros de trabajo."),
            figure("operational-flow-decision", "Decisión operativa con caminos alternativos nombrados."),
            figure("operational-flow-current-vs-proposed", "Comparación entre flujo actual AS-IS y flujo propuesto TO-BE."),
            figure("operational-flow-vs-formal", "Diferencia entre flujo operativo, BPMN y flujo de pantallas."),
            figure("operational-flow-common-errors", "Errores comunes al usar pasos vagos, sin responsable o sin decisión."),
            figure("c4-context-containers", "Persona, sistema, contenedor y base de datos en C4."),
            figure("c4-context-basic", "Vista básica de contexto con personas, sistema central y sistemas externos."),
            figure("c4-context-boundary", "Límite del sistema y separación entre lo interno y lo externo."),
            figure("c4-context-people-systems", "Diferencia entre personas usuarias y sistemas externos."),
            figure("c4-context-relationships", "Relaciones de contexto con verbos claros y propósito explícito."),
            figure("c4-context-scope-future", "Alcance actual, futuro y fuera de alcance en un contexto C4."),
            figure("c4-context-small-business", "Contexto de un negocio pequeño con usuarios internos, cliente y sistemas externos."),
            figure("c4-context-common-errors", "Errores comunes al mezclar detalles internos o relaciones vagas en contexto."),
            figure("c4-containers-basic-desktop-api-db", "Aplicación desktop conectada a backend/API y base de datos."),
            figure("c4-containers-web-api-db", "Frontend web conectado a backend/API y PostgreSQL."),
            figure("c4-containers-services", "Backend/API coordinando base de datos, reportes, archivos y notificaciones."),
            figure("c4-containers-context-vs-containers", "Diferencia entre C4 Contexto y C4 Contenedores."),
            figure("c4-containers-common-errors", "Errores comunes al mezclar contenedores con clases, pantallas o Docker."),
            figure("technical-deployment-nodes", "Nodos físicos/lógicos conectados para despliegue técnico."),
            figure("technical-deployment-basic-web", "Despliegue web mínimo con usuario, backend/API y base de datos."),
            figure("technical-deployment-desktop-centralized", "Aplicaciones desktop en varias sucursales conectadas a backend centralizado."),
            figure("technical-deployment-dev-vs-prod", "Separación entre ambiente de desarrollo y ambiente de producción."),
            figure("technical-deployment-backups", "Respaldo de producción hacia almacenamiento separado y restauración de prueba."),
            figure("technical-deployment-local-vs-cloud", "Comparación entre base local, servidor local y backend centralizado."),
            figure("technical-deployment-common-errors", "Errores comunes en despliegue: sin backups, ambientes mezclados o clientes directos a la base."),
            figure("uml-use-case-actor", "Actor, límite del sistema y caso de uso UML."),
            figure("uml-use-case-symbols", "Símbolos básicos de UML Casos de uso: actor, caso de uso y límite del sistema."),
            figure("uml-use-case-system-boundary", "Límite del sistema con actores externos y funcionalidades internas."),
            figure("uml-use-case-basic-admin-example", "Ejemplo administrativo con Recepción, Técnico, Caja y casos de uso principales."),
            figure("uml-use-case-include-extend", "Diferencia entre include obligatorio y extend condicional."),
            figure("uml-use-case-external-system-actor", "Sistema externo tratado como actor en un caso de uso."),
            figure("uml-use-case-textual-spec", "Ficha textual complementaria para detallar un caso de uso importante."),
            figure("uml-use-case-common-errors", "Errores comunes al confundir casos de uso con módulos, botones o pantallas."),
            figure("uml-class-compartments", "Clase UML con compartimentos de nombre, atributos y operaciones."),
            figure("uml-class-anatomy", "Anatomía de una clase UML con nombre, atributos y operaciones."),
            figure("uml-class-association-multiplicity", "Asociación entre clases con multiplicidad y lectura verbal."),
            figure("uml-class-composition", "Composición todo-parte fuerte entre una orden y sus detalles."),
            figure("uml-class-inheritance-interface", "Diferencia entre herencia e interfaz en diseño de clases."),
            figure("uml-class-packages", "Agrupación de clases por paquete o módulo funcional."),
            figure("uml-class-domain-dto-service", "Diferencia entre dominio, DTO, servicio y repositorio."),
            figure("uml-class-common-errors", "Errores comunes al confundir clases con tablas, pantallas o clases gigantes."),
            figure("uml-activity-flow", "Actividad UML con inicio, acción, decisión y final."),
            figure("uml-activity-symbols", "Símbolos básicos de UML Actividad: inicio, acción, decisión, fork, join y final."),
            figure("uml-activity-linear", "Actividad lineal con inicio, acciones concretas y final."),
            figure("uml-activity-decision", "Decisión con camino exitoso y camino de error."),
            figure("uml-activity-swimlanes", "Carriles para separar acciones de usuario, sistema o área responsable."),
            figure("uml-activity-fork-join", "Fork y join para representar paralelismo controlado."),
            figure("uml-activity-vs-sequence", "Diferencia entre flujo lógico de actividad y mensajes temporales de secuencia."),
            figure("uml-activity-common-errors", "Errores comunes al usar acciones vagas, decisiones sin condición o pantallas como acciones."),
            figure("uml-sequence-lifeline", "Líneas de vida y mensajes temporales en UML secuencia."),
            figure("uml-sequence-anatomy", "Anatomía de lifelines, mensajes, retornos y lectura temporal."),
            figure("uml-sequence-frontend-backend-db", "Secuencia típica entre usuario, pantalla, servicio, repositorio y base de datos."),
            figure("uml-sequence-alt-error", "Fragmento alt para separar camino exitoso y camino de error."),
            figure("uml-sequence-combined-fragment-parts", "Operador, guarda, operandos y rango temporal dentro de un fragmento combinado."),
            figure("uml-sequence-opt-guard", "Fragmento opt como comportamiento opcional condicionado por una guarda."),
            figure("uml-sequence-loop-guard", "Fragmento loop para repetir mensajes sobre varios registros."),
            figure("uml-sequence-par-operands", "Fragmento par con operandos que representan trabajos paralelos o independientes."),
            figure("uml-sequence-critical-region", "Región crítica para proteger una invariante de consistencia."),
            figure("uml-sequence-ref-interaction", "Fragmento ref para reutilizar una interacción definida aparte."),
            figure("uml-sequence-nested-fragments", "Anidación controlada de fragmentos loop y alt."),
            figure("uml-sequence-external-system", "Interacción con un sistema externo y respuesta controlada."),
            figure("uml-sequence-sync-async", "Diferencia entre mensaje síncrono y mensaje asíncrono."),
            figure("uml-sequence-common-errors", "Errores comunes al usar mensajes vagos o participantes sin responsabilidad clara."),
            figure("uml-state-transition", "Estado inicial, estados intermedios, transición y estado final."),
            figure("uml-state-symbols", "Símbolos básicos de UML Estados: inicio, estado, transición, guarda y final."),
            figure("uml-state-order-lifecycle", "Ciclo de vida de una orden de reparación desde recepción hasta entrega."),
            figure("uml-state-alternative-paths", "Caminos alternativos como rechazo, anulación o no reparable."),
            figure("uml-state-guard-condition", "Transición con condición, acción y permiso requerido."),
            figure("uml-state-terminal-states", "Estados terminales que cierran el ciclo de vida normal."),
            figure("uml-state-history", "Historial de estados para auditoría y trazabilidad humana."),
            figure("uml-state-common-errors", "Errores comunes al confundir estados con acciones o pantallas."),
            figure("free-graph-overview", "Grafo libre con nodos, relaciones dirigidas, asociaciones y etiquetas."),
            figure("free-graph-node-content", "Nodo libre con título visible y contenido descriptivo editable."),
            figure("free-graph-edge-types", "Relación dirigida y relación no dirigida dentro de un grafo mixto."),
            figure("free-graph-knowledge-map", "Grafo libre como mapa de conocimiento o relaciones informales."),
            figure("free-graph-common-errors", "Errores comunes en grafos libres: nodos gigantes, relaciones sin etiqueta y mezcla de notaciones."),
            figure("logical-business-state-action-cycle", "Ciclo de estado, acción transformadora, reglas, invariantes, cierre verificable y uso como fuente."),
            figure("logical-business-graph-backbone", "Backbone semántico MF → FL → CU → ACC con reglas, condiciones y entidades alrededor."),
            figure("logical-business-graph-traceability", "Trazabilidad desde levantamiento lógico hacia grafo semántico y artefactos compatibles.")
    );

    private final Map<String, TheoryFigureReference> figuresById;

    public DefaultTheoryFigureCatalog() {
        this.figuresById = FIGURES.stream()
                .collect(Collectors.toUnmodifiableMap(TheoryFigureReference::figureId, Function.identity()));
    }

    public List<TheoryFigureReference> findAll() {
        return FIGURES;
    }

    public Optional<TheoryFigureReference> findById(String figureId) {
        Objects.requireNonNull(figureId, "figureId");
        return Optional.ofNullable(figuresById.get(figureId));
    }

    public boolean contains(String figureId) {
        return findById(figureId).isPresent();
    }

    private static TheoryFigureReference figure(String id, String caption) {
        return new TheoryFigureReference(id, caption);
    }
}
