package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UmlClassDiagramFilterEngineTest {

    private final UmlClassDiagramFilterEngine engine = new UmlClassDiagramFilterEngine();

    @Test
    void filtersByInternalViewWithoutMutatingDocument() {
        UmlClassDiagramDocument document = document();

        UmlClassDiagramFilterResult result = engine.apply(document,
                new UmlClassDiagramFilterState("backend", "", null, null));

        assertEquals(List.of("backend"), result.modules().stream().map(UmlModuleGroup::id).toList());
        assertEquals(List.of("producto-controller", "producto-service"), result.classes().stream().map(UmlClassNode::id).toList());
        assertEquals(1, result.relations().size());
        assertEquals(4, document.classCount());
    }

    @Test
    void searchFindsMembersAndKeepsContainingClass() {
        UmlClassDiagramDocument document = document();

        UmlClassDiagramFilterResult result = engine.apply(document,
                new UmlClassDiagramFilterState("", "buscarProducto", null, null));

        assertEquals(List.of("producto-service"), result.classes().stream().map(UmlClassNode::id).toList());
        assertEquals(List.of("backend"), result.modules().stream().map(UmlModuleGroup::id).toList());
    }

    @Test
    void relationSearchKeepsEndpointsVisible() {
        UmlClassDiagramDocument document = document();

        UmlClassDiagramFilterResult result = engine.apply(document,
                new UmlClassDiagramFilterState("", "api", null, UmlRelationKind.DEPENDENCY));

        assertEquals(List.of("producto-controller", "producto-api-service"), result.classes().stream().map(UmlClassNode::id).toList());
        assertEquals(List.of("api-call"), result.relations().stream().map(UmlClassRelation::id).toList());
        assertTrue(result.modules().stream().map(UmlModuleGroup::id).toList().containsAll(List.of("backend", "frontend")));
    }

    @Test
    void filtersByClassAndRelationKind() {
        UmlClassDiagramDocument document = document();

        UmlClassDiagramFilterResult result = engine.apply(document,
                new UmlClassDiagramFilterState("", "", UmlClassKind.SERVICE, UmlRelationKind.ASSOCIATION));

        assertEquals(List.of("producto-service"), result.classes().stream().map(UmlClassNode::id).toList());
        assertEquals(List.of(), result.relations());
    }


    @Test
    void derivesModulesForClassOnlyView() {
        UmlClassDiagramDocument document = documentWithClassOnlySummaryView();

        UmlClassDiagramFilterResult result = engine.apply(document,
                new UmlClassDiagramFilterState("resumen", "", null, null));

        assertEquals(List.of("producto-controller", "producto-api-service"),
                result.classes().stream().map(UmlClassNode::id).toList());
        assertEquals(List.of("backend", "frontend"),
                result.modules().stream().map(UmlModuleGroup::id).toList());
    }

    private UmlClassDiagramDocument document() {
        UmlModuleGroup backend = new UmlModuleGroup("backend", "Backend Java", "backend/src/main/java", "", "");
        UmlModuleGroup frontend = new UmlModuleGroup("frontend", "Frontend TS", "frontend/src/app", "", "");
        UmlClassNode controller = node("producto-controller", "backend", "ProductoController", UmlClassKind.CONTROLLER);
        UmlClassNode service = new UmlClassNode("producto-service", "backend", "ProductoService", "com.acme.productos",
                UmlClassKind.SERVICE, UmlVisibility.PUBLIC, "", "", List.of(
                new UmlClassMember("m1", UmlMemberKind.METHOD, "buscarProducto", "ProductoDto", "buscarProducto(id)",
                        UmlVisibility.PUBLIC, false, "")), "");
        UmlClassNode apiService = node("producto-api-service", "frontend", "ProductoApiService", UmlClassKind.SERVICE);
        UmlClassNode component = node("producto-component", "frontend", "ProductoComponent", UmlClassKind.COMPONENT);
        UmlClassRelation backendRelation = new UmlClassRelation("controller-service", "producto-controller", "producto-service",
                UmlRelationKind.ASSOCIATION, "usa", "", "");
        UmlClassRelation apiRelation = new UmlClassRelation("api-call", "producto-api-service", "producto-controller",
                UmlRelationKind.DEPENDENCY, "api", "", "Endpoint: GET /api/productos");
        UmlClassDiagramView backendView = new UmlClassDiagramView("backend", UmlClassDiagramViewKind.BACKEND,
                "Backend", "", List.of(), List.of("backend"),
                List.of("producto-controller", "producto-service"), List.of("controller-service"), "");
        return new UmlClassDiagramDocument("Test", "borrador", LocalDate.now(), List.of(backend, frontend),
                List.of(controller, service, apiService, component), List.of(backendRelation, apiRelation),
                List.of(backendView), "");
    }


    private UmlClassDiagramDocument documentWithClassOnlySummaryView() {
        UmlModuleGroup backend = new UmlModuleGroup("backend", "Backend Java", "backend/src/main/java", "", "");
        UmlModuleGroup frontend = new UmlModuleGroup("frontend", "Frontend TS", "frontend/src/app", "", "");
        UmlClassNode controller = node("producto-controller", "backend", "ProductoController", UmlClassKind.CONTROLLER);
        UmlClassNode service = node("producto-service", "backend", "ProductoService", UmlClassKind.SERVICE);
        UmlClassNode apiService = node("producto-api-service", "frontend", "ProductoApiService", UmlClassKind.SERVICE);
        UmlClassNode component = node("producto-component", "frontend", "ProductoComponent", UmlClassKind.COMPONENT);
        UmlClassRelation apiRelation = new UmlClassRelation("api-call", "producto-api-service", "producto-controller",
                UmlRelationKind.DEPENDENCY, "api", "", "Endpoint: GET /api/productos");
        UmlClassDiagramView summaryView = new UmlClassDiagramView("resumen", UmlClassDiagramViewKind.SUMMARY,
                "Resumen", "", List.of(), List.of(),
                List.of("producto-controller", "producto-api-service"), List.of("api-call"), "");
        return new UmlClassDiagramDocument("Test", "borrador", LocalDate.now(), List.of(backend, frontend),
                List.of(controller, service, apiService, component), List.of(apiRelation), List.of(summaryView), "");
    }

    private UmlClassNode node(String id, String moduleId, String displayName, UmlClassKind kind) {
        return new UmlClassNode(id, moduleId, displayName, "", kind, UmlVisibility.PUBLIC, "", "", List.of(), "");
    }
}
