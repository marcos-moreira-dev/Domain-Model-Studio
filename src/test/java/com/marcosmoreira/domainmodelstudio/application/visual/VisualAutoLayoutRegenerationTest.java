package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class VisualAutoLayoutRegenerationTest {

    private final VisualLayoutService service = new VisualLayoutService();

    @Test
    void regenerateVisualLayoutResetsManualModuleMapPosition() {
        DiagramProject moved = service.moveNodeTo(
                moduleMapProject(),
                VisualElementLayoutIds.module("ventas"),
                980,
                720);

        DiagramProject regenerated = service.regenerateVisualLayout(moved);
        var ventas = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.module("ventas"))
                .orElseThrow();

        assertTrue(ventas.x() < 400, "autoorganizar debe recuperar una ubicación inicial legible");
        assertTrue(ventas.y() < 180, "autoorganizar debe volver al carril superior inicial");
    }

    @Test
    void moduleMapAutoLayoutOrdersDependentRootModulesFromLeftToRight() {
        DiagramProject regenerated = service.regenerateVisualLayout(moduleMapProject());
        double ventasX = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.module("ventas"))
                .orElseThrow()
                .x();
        double reportesX = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.module("reportes"))
                .orElseThrow()
                .x();

        assertTrue(ventasX < reportesX, "el módulo que alimenta dependencias debe quedar antes del consumidor");
    }

    @Test
    void umlClassAutoLayoutKeepsRelatedClassesInsideTheirModule() {
        DiagramProject regenerated = service.regenerateVisualLayout(umlClassProject());
        var modulo = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.umlModule("academico"))
                .orElseThrow();
        var estudiante = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.umlClass("estudiante"))
                .orElseThrow();
        var matricula = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.umlClass("matricula"))
                .orElseThrow();

        assertTrue(estudiante.x() > modulo.x());
        assertTrue(matricula.x() > modulo.x());
        assertTrue(estudiante.x() + estudiante.width() < modulo.x() + modulo.width());
        assertTrue(matricula.y() + matricula.height() < modulo.y() + modulo.height());
    }

    @Test
    void useCaseAutoLayoutPlacesActorsOutsideSystemBoundary() {
        DiagramProject regenerated = service.regenerateVisualLayout(useCaseProject());
        var actor = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("secretaria"))
                .orElseThrow();
        var useCase = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("registrar-matricula"))
                .orElseThrow();
        var boundary = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("sistema-uens"))
                .orElseThrow();
        var external = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("correo-institucional"))
                .orElseThrow();

        assertTrue(actor.x() + actor.width() < boundary.x(), "los actores primarios deben quedar fuera del sistema");
        assertTrue(useCase.x() > boundary.x(), "los casos de uso deben quedar dentro del límite");
        assertTrue(useCase.x() + useCase.width() < boundary.x() + boundary.width());
        assertTrue(external.x() > boundary.x() + boundary.width(), "actores/sistemas externos deben quedar al lado externo");
    }


    @Test
    void bpmnAutoLayoutPlacesTasksInsideTheirLaneAndInFlowOrder() {
        DiagramProject regenerated = service.regenerateVisualLayout(bpmnProject());
        var lane = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("secretaria"))
                .orElseThrow();
        var start = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("inicio"))
                .orElseThrow();
        var task = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("revisar"))
                .orElseThrow();
        var gateway = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("datos-completos"))
                .orElseThrow();

        assertTrue(task.x() > lane.x(), "la tarea BPMN debe quedar dentro del carril");
        assertTrue(task.y() > lane.y(), "la tarea BPMN debe quedar dentro del carril");
        assertTrue(start.x() < task.x(), "el flujo debe leerse de izquierda a derecha");
        assertTrue(task.x() < gateway.x(), "la compuerta debe quedar después de la tarea que la alimenta");
    }

    @Test
    void operationalFlowAutoLayoutPlacesStepsInsideResponsibleLane() {
        DiagramProject regenerated = service.regenerateVisualLayout(operationalFlowProject());
        var lane = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("tecnico"))
                .orElseThrow();
        var receive = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("recibir-equipo"))
                .orElseThrow();
        var evidence = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("orden-trabajo"))
                .orElseThrow();

        assertTrue(receive.x() > lane.x(), "el paso debe quedar dentro del responsable");
        assertTrue(receive.y() > lane.y(), "el paso debe quedar dentro del responsable");
        assertTrue(receive.x() < evidence.x(), "la evidencia posterior debe seguir la secuencia operativa");
    }


    @Test
    void umlActivityAutoLayoutOrdersActionFlowTopDown() {
        DiagramProject regenerated = service.regenerateVisualLayout(umlActivityProject());
        var start = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("inicio-actividad"))
                .orElseThrow();
        var action = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("validar-datos"))
                .orElseThrow();
        var decision = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("datos-validos"))
                .orElseThrow();
        var end = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("fin-actividad"))
                .orElseThrow();

        assertTrue(start.y() < action.y(), "la actividad UML debe iniciar arriba");
        assertTrue(action.y() < decision.y(), "la decisión debe quedar después de la acción que la alimenta");
        assertTrue(decision.y() < end.y(), "el final debe quedar al cierre del flujo");
    }

    @Test
    void umlStateAutoLayoutOrdersLifecycleLeftToRight() {
        DiagramProject regenerated = service.regenerateVisualLayout(umlStateProject());
        var initial = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("inicio-estado"))
                .orElseThrow();
        var draft = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("borrador"))
                .orElseThrow();
        var active = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("activa"))
                .orElseThrow();
        var closed = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("cerrada"))
                .orElseThrow();

        assertTrue(initial.x() < draft.x(), "la máquina de estados debe iniciar a la izquierda");
        assertTrue(draft.x() < active.x(), "los estados deben seguir el ciclo de vida principal");
        assertTrue(active.x() < closed.x(), "el estado final debe quedar al cierre del ciclo");
    }


    @Test
    void umlSequenceAutoLayoutOrdersParticipantsByFirstTemporalMessage() {
        DiagramProject regenerated = service.regenerateVisualLayout(umlSequenceProject());
        var usuario = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("usuario"))
                .orElseThrow();
        var pantalla = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("pantalla"))
                .orElseThrow();
        var servicio = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.behaviorNode("servicio"))
                .orElseThrow();

        assertTrue(usuario.x() < pantalla.x(), "el emisor inicial debe quedar antes del primer receptor");
        assertTrue(pantalla.x() < servicio.x(), "los participantes deben seguir el primer orden temporal de mensajes");
        assertTrue(usuario.height() > 300, "las lifelines deben cubrir el tramo temporal de mensajes");
    }

    @Test
    void c4ContextAutoLayoutPlacesSystemBetweenPeopleAndExternalSystems() {
        DiagramProject regenerated = service.regenerateVisualLayout(c4ContextProject());
        var persona = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.architectureNode("secretaria"))
                .orElseThrow();
        var sistema = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.architectureNode("sistema-uens"))
                .orElseThrow();
        var externo = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.architectureNode("correo"))
                .orElseThrow();

        assertTrue(persona.x() + persona.width() < sistema.x(), "las personas deben quedar fuera y a la izquierda");
        assertTrue(sistema.x() < externo.x(), "los sistemas externos deben quedar fuera y a la derecha");
    }

    @Test
    void c4ContainersAutoLayoutPlacesApplicationsBeforeApiAndDatabases() {
        DiagramProject regenerated = service.regenerateVisualLayout(c4ContainersProject());
        var app = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.architectureNode("desktop"))
                .orElseThrow();
        var api = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.architectureNode("api"))
                .orElseThrow();
        var db = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.architectureNode("postgres"))
                .orElseThrow();
        var external = regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.architectureNode("correo"))
                .orElseThrow();

        assertTrue(app.x() < api.x(), "la aplicación cliente debe quedar antes del backend/API");
        assertTrue(api.x() < db.x(), "la base de datos debe quedar después de la API");
        assertTrue(db.x() < external.x(), "los servicios externos deben quedar fuera de la solución propia");
    }


    private static DiagramProject moduleMapProject() {
        ModuleNode ventas = ModuleNode.root("ventas", "Ventas");
        ModuleNode reportes = ModuleNode.root("reportes", "Reportes");
        ModuleNode facturacion = ModuleNode.child("facturacion", "Facturación", ventas.id());
        ModuleDependency dependency = new ModuleDependency(
                "dep-ventas-reportes",
                ventas.id(),
                reportes.id(),
                DependencyKind.REPORTS_FROM,
                "Reportes consume ventas.",
                "");
        ModuleMapDocument document = new ModuleMapDocument(
                "Sistema administrativo",
                "borrador",
                LocalDate.of(2026, 5, 16),
                List.of(reportes, ventas, facturacion),
                List.of(dependency),
                "");
        return DiagramProject.blank("mapa", "Mapa", DiagramTypeId.ADMIN_MODULE_MAP).withModuleMap(document);
    }

    private static DiagramProject useCaseProject() {
        BehaviorNode boundary = new BehaviorNode("sistema-uens", BehaviorNodeKind.SYSTEM_BOUNDARY,
                "Sistema administrativo UENS", "", "", "", 0);
        BehaviorNode secretaria = new BehaviorNode("secretaria", BehaviorNodeKind.ACTOR,
                "Secretaría", "", "actor principal", "", 0);
        BehaviorNode correo = new BehaviorNode("correo-institucional", BehaviorNodeKind.ACTOR,
                "Correo institucional externo", "", "sistema externo", "", 0);
        BehaviorNode registrar = new BehaviorNode("registrar-matricula", BehaviorNodeKind.USE_CASE,
                "Registrar matrícula", "", "", "", 0);
        BehaviorNode notificar = new BehaviorNode("notificar", BehaviorNodeKind.USE_CASE,
                "Enviar notificación", "", "", "", 0);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "UENS", "borrador", LocalDate.of(2026, 5, 16), BehaviorDiagramKind.UML_USE_CASE,
                List.of(correo, registrar, secretaria, boundary, notificar),
                List.of(
                        new BehaviorEdge("a1", secretaria.id(), registrar.id(), BehaviorEdgeKind.ASSOCIATION, "", "", ""),
                        new BehaviorEdge("i1", registrar.id(), notificar.id(), BehaviorEdgeKind.INCLUDE, "include", "", ""),
                        new BehaviorEdge("a2", correo.id(), notificar.id(), BehaviorEdgeKind.ASSOCIATION, "", "", "")
                ), "");
        return DiagramProject.blank("use-case", "Casos de uso", DiagramTypeId.UML_USE_CASE).withBehaviorDiagram(document);
    }

    private static DiagramProject umlClassProject() {
        UmlModuleGroup module = new UmlModuleGroup("academico", "Académico", "uens.academico", "", "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "UENS",
                "borrador",
                LocalDate.of(2026, 5, 16),
                List.of(module),
                List.of(node("matricula", module.id()), node("estudiante", module.id())),
                List.of(new UmlClassRelation("rel-est-mat", "estudiante", "matricula",
                        UmlRelationKind.ASSOCIATION, "tiene", "", "")),
                "");
        return DiagramProject.blank("uml", "UML", DiagramTypeId.UML_CLASS).withUmlClassDiagram(document);
    }


    private static DiagramProject bpmnProject() {
        BehaviorNode lane = new BehaviorNode("secretaria", BehaviorNodeKind.LANE,
                "Secretaría", "", "", "", 0);
        BehaviorNode start = new BehaviorNode("inicio", BehaviorNodeKind.START_EVENT,
                "Solicitud recibida", "Secretaría", "", "", 0);
        BehaviorNode revisar = new BehaviorNode("revisar", BehaviorNodeKind.ACTIVITY,
                "Revisar documentos", "Secretaría", "", "", 0);
        BehaviorNode gateway = new BehaviorNode("datos-completos", BehaviorNodeKind.DECISION,
                "¿Datos completos?", "Secretaría", "", "", 0);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Matrícula", "borrador", LocalDate.of(2026, 5, 16), BehaviorDiagramKind.BPMN_BASIC,
                List.of(gateway, lane, revisar, start),
                List.of(
                        new BehaviorEdge("f1", start.id(), revisar.id(), BehaviorEdgeKind.FLOW, "", "", ""),
                        new BehaviorEdge("f2", revisar.id(), gateway.id(), BehaviorEdgeKind.FLOW, "", "", "")
                ), "");
        return DiagramProject.blank("bpmn", "BPMN", DiagramTypeId.BPMN_BASIC).withBehaviorDiagram(document);
    }

    private static DiagramProject operationalFlowProject() {
        BehaviorNode lane = new BehaviorNode("tecnico", BehaviorNodeKind.LANE,
                "Técnico", "", "", "", 0);
        BehaviorNode receive = new BehaviorNode("recibir-equipo", BehaviorNodeKind.ACTIVITY,
                "Recibir equipo", "Técnico", "", "", 0);
        BehaviorNode evidence = new BehaviorNode("orden-trabajo", BehaviorNodeKind.NOTE,
                "Orden de trabajo", "Técnico", "", "", 0);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Reparación", "borrador", LocalDate.of(2026, 5, 16), BehaviorDiagramKind.OPERATIONAL_FLOW,
                List.of(evidence, lane, receive),
                List.of(new BehaviorEdge("f1", receive.id(), evidence.id(), BehaviorEdgeKind.FLOW, "genera", "", "")), "");
        return DiagramProject.blank("operativo", "Operativo", DiagramTypeId.OPERATIONAL_FLOW)
                .withBehaviorDiagram(document);
    }



    private static DiagramProject umlSequenceProject() {
        BehaviorNode servicio = new BehaviorNode("servicio", BehaviorNodeKind.PARTICIPANT,
                "Servicio de matrícula", "", "", "", 0);
        BehaviorNode usuario = new BehaviorNode("usuario", BehaviorNodeKind.PARTICIPANT,
                "Usuario", "", "", "", 0);
        BehaviorNode pantalla = new BehaviorNode("pantalla", BehaviorNodeKind.PARTICIPANT,
                "Pantalla de matrícula", "", "", "", 0);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Secuencia", "borrador", LocalDate.of(2026, 5, 16), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(servicio, usuario, pantalla),
                List.of(
                        new BehaviorEdge("m1", usuario.id(), pantalla.id(), BehaviorEdgeKind.MESSAGE, "solicita registro", "", ""),
                        new BehaviorEdge("m2", pantalla.id(), servicio.id(), BehaviorEdgeKind.ASYNC_MESSAGE, "envía comando", "", ""),
                        new BehaviorEdge("m3", servicio.id(), pantalla.id(), BehaviorEdgeKind.RETURN_MESSAGE, "resultado", "", "")
                ), "");
        return DiagramProject.blank("secuencia", "Secuencia", DiagramTypeId.UML_SEQUENCE).withBehaviorDiagram(document);
    }

    private static DiagramProject umlActivityProject() {
        BehaviorNode start = new BehaviorNode("inicio-actividad", BehaviorNodeKind.INITIAL_STATE,
                "Inicio", "", "", "", 0);
        BehaviorNode action = new BehaviorNode("validar-datos", BehaviorNodeKind.ACTION,
                "Validar datos", "", "", "", 0);
        BehaviorNode decision = new BehaviorNode("datos-validos", BehaviorNodeKind.DECISION,
                "¿Datos válidos?", "", "", "", 0);
        BehaviorNode end = new BehaviorNode("fin-actividad", BehaviorNodeKind.FINAL_STATE,
                "Fin", "", "", "", 0);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Actividad", "borrador", LocalDate.of(2026, 5, 16), BehaviorDiagramKind.UML_ACTIVITY,
                List.of(decision, end, action, start),
                List.of(
                        new BehaviorEdge("f1", start.id(), action.id(), BehaviorEdgeKind.FLOW, "", "", ""),
                        new BehaviorEdge("f2", action.id(), decision.id(), BehaviorEdgeKind.FLOW, "", "", ""),
                        new BehaviorEdge("f3", decision.id(), end.id(), BehaviorEdgeKind.FLOW, "", "[sí]", "")
                ), "");
        return DiagramProject.blank("actividad", "Actividad", DiagramTypeId.UML_ACTIVITY).withBehaviorDiagram(document);
    }

    private static DiagramProject umlStateProject() {
        BehaviorNode initial = new BehaviorNode("inicio-estado", BehaviorNodeKind.INITIAL_STATE,
                "Inicio", "", "", "", 0);
        BehaviorNode draft = new BehaviorNode("borrador", BehaviorNodeKind.STATE,
                "Borrador", "", "", "", 0);
        BehaviorNode active = new BehaviorNode("activa", BehaviorNodeKind.STATE,
                "Activa", "", "", "", 0);
        BehaviorNode closed = new BehaviorNode("cerrada", BehaviorNodeKind.FINAL_STATE,
                "Cerrada", "", "", "", 0);
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Estados", "borrador", LocalDate.of(2026, 5, 16), BehaviorDiagramKind.UML_STATE,
                List.of(active, closed, initial, draft),
                List.of(
                        new BehaviorEdge("t1", initial.id(), draft.id(), BehaviorEdgeKind.TRANSITION, "crear", "", ""),
                        new BehaviorEdge("t2", draft.id(), active.id(), BehaviorEdgeKind.TRANSITION, "aprobar", "", ""),
                        new BehaviorEdge("t3", active.id(), closed.id(), BehaviorEdgeKind.TRANSITION, "cerrar", "", "")
                ), "");
        return DiagramProject.blank("estados", "Estados", DiagramTypeId.UML_STATE).withBehaviorDiagram(document);
    }

    private static DiagramProject c4ContextProject() {
        ArchitectureNode boundary = archNode("uens", ArchitectureNodeKind.BOUNDARY, "UENS");
        ArchitectureNode secretaria = archNode("secretaria", ArchitectureNodeKind.PERSON, "Secretaría");
        ArchitectureNode sistema = archNode("sistema-uens", ArchitectureNodeKind.SOFTWARE_SYSTEM, "Sistema administrativo UENS");
        ArchitectureNode correo = archNode("correo", ArchitectureNodeKind.EXTERNAL_SYSTEM, "Correo institucional");
        ArchitectureDiagramDocument document = new ArchitectureDiagramDocument(
                "UENS", "borrador", LocalDate.of(2026, 5, 16), ArchitectureDiagramKind.C4_CONTEXT,
                List.of(correo, boundary, sistema, secretaria),
                List.of(new ArchitectureEdge("r1", secretaria.id(), sistema.id(), ArchitectureEdgeKind.USES, "registra matrículas", "", ""),
                        new ArchitectureEdge("r2", sistema.id(), correo.id(), ArchitectureEdgeKind.INTEGRATES_WITH, "envía avisos", "SMTP", "")), "");
        return DiagramProject.blank("c4ctx", "C4 Contexto", DiagramTypeId.C4_CONTEXT).withArchitectureDiagram(document);
    }

    private static DiagramProject c4ContainersProject() {
        ArchitectureNode boundary = archNode("sistema", ArchitectureNodeKind.BOUNDARY, "Sistema administrativo");
        ArchitectureNode app = archNode("desktop", ArchitectureNodeKind.APPLICATION, "App JavaFX");
        ArchitectureNode api = archNode("api", ArchitectureNodeKind.API, "API Spring Boot");
        ArchitectureNode db = archNode("postgres", ArchitectureNodeKind.DATABASE, "PostgreSQL");
        ArchitectureNode correo = archNode("correo", ArchitectureNodeKind.EXTERNAL_SERVICE, "Correo institucional");
        ArchitectureDiagramDocument document = new ArchitectureDiagramDocument(
                "UENS", "borrador", LocalDate.of(2026, 5, 16), ArchitectureDiagramKind.C4_CONTAINERS,
                List.of(correo, db, api, boundary, app),
                List.of(new ArchitectureEdge("r1", app.id(), api.id(), ArchitectureEdgeKind.CALLS, "consume API", "HTTP", ""),
                        new ArchitectureEdge("r2", api.id(), db.id(), ArchitectureEdgeKind.READS_WRITES, "persistencia", "JDBC", ""),
                        new ArchitectureEdge("r3", api.id(), correo.id(), ArchitectureEdgeKind.DEPENDS_ON, "notificaciones", "SMTP", "")), "");
        return DiagramProject.blank("c4containers", "C4 Contenedores", DiagramTypeId.C4_CONTAINERS)
                .withArchitectureDiagram(document);
    }

    private static ArchitectureNode archNode(String id, ArchitectureNodeKind kind, String name) {
        return new ArchitectureNode(id, kind, name, "", "", "", "", "", 0);
    }

    private static UmlClassNode node(String id, String moduleId) {
        return new UmlClassNode(id, moduleId, id, "uens.academico", UmlClassKind.CLASS,
                UmlVisibility.PUBLIC, "", "", List.of(member("id")), "");
    }

    private static UmlClassMember member(String name) {
        return new UmlClassMember("m-" + name, UmlMemberKind.ATTRIBUTE, name, "String", "",
                UmlVisibility.PRIVATE, false, "");
    }
}
