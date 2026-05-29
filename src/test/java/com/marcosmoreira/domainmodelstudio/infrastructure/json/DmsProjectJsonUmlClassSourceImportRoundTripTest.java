package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
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

/** Regresión para conservar vistas internas y metadatos UML generados desde código fuente. */
class DmsProjectJsonUmlClassSourceImportRoundTripTest {

    @Test
    void sourceGeneratedUmlViewsAndOriginMetadataSurviveDmsRoundTrip() {
        UmlModuleGroup backend = new UmlModuleGroup(
                "module-backend-productos",
                "productos",
                "backend/src/main/java/com/cedro/productos",
                "sourceRoot=backend-java; language=JAVA; package=com.cedro.productos",
                "Origen: backend Java / Spring Boot");
        UmlModuleGroup frontend = new UmlModuleGroup(
                "module-frontend-productos",
                "productos",
                "frontend/src/app/features/productos",
                "sourceRoot=frontend-typescript; language=TYPESCRIPT; folder=src/app/features/productos",
                "Origen: frontend TypeScript / Angular");
        UmlClassNode controller = new UmlClassNode(
                "class-producto-controller",
                backend.id(),
                "ProductoController",
                "com.cedro.productos",
                UmlClassKind.CONTROLLER,
                UmlVisibility.PUBLIC,
                "Expone API REST de productos.",
                "path=backend/src/main/java/com/cedro/productos/ProductoController.java; annotation=@RestController",
                List.of(new UmlClassMember("method-listar", UmlMemberKind.METHOD, "listar", "List<ProductoDto>",
                        "public List<ProductoDto> listar()", UmlVisibility.PUBLIC, false, "GET /api/productos")),
                "sourceRoot=backend-java; sourceLanguage=JAVA");
        UmlClassNode apiService = new UmlClassNode(
                "class-producto-api-service",
                frontend.id(),
                "ProductoApiService",
                "src.app.features.productos",
                UmlClassKind.SERVICE,
                UmlVisibility.PUBLIC,
                "Consume la API de productos.",
                "path=frontend/src/app/features/productos/producto-api.service.ts; decorator=@Injectable",
                List.of(),
                "sourceRoot=frontend-typescript; sourceLanguage=TYPESCRIPT");
        UmlClassRelation api = new UmlClassRelation(
                "rel-producto-api",
                apiService.id(),
                controller.id(),
                UmlRelationKind.DEPENDENCY,
                "GET /api/productos",
                "apiHttpMethod=GET; apiPath=/api/productos; apiMatchKind=SUFFIX",
                "inferred=true; inferenceReason=frontend-backend-api-match");
        UmlClassDiagramView backendView = new UmlClassDiagramView(
                "view-backend-java",
                UmlClassDiagramViewKind.BACKEND,
                "Backend Java",
                "Vista interna del backend Java detectado.",
                List.of("backend-java"),
                List.of(backend.id()),
                List.of(controller.id()),
                List.of(),
                "sourceRoot=backend-java");
        UmlClassDiagramView integrationView = new UmlClassDiagramView(
                "view-integracion-api",
                UmlClassDiagramViewKind.INTEGRATION,
                "Integración API",
                "Relaciones frontend-backend inferidas desde rutas HTTP.",
                List.of("backend-java", "frontend-typescript"),
                List.of(backend.id(), frontend.id()),
                List.of(controller.id(), apiService.id()),
                List.of(api.id()),
                "api-relations=true");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "Cedro Damasco desde código",
                "borrador",
                LocalDate.of(2026, 5, 17),
                List.of(backend, frontend),
                List.of(controller, apiService),
                List.of(api),
                List.of(backendView, integrationView),
                "Generado desde importación multi-raíz Java/TypeScript.");
        DiagramProject project = DiagramProject.blank("cedro-uml", "Cedro UML", DiagramTypeId.UML_CLASS)
                .withUmlClassDiagram(document);

        String json = new DmsProjectJsonWriter().write(project);
        DiagramProject reopened = new DmsProjectJsonReader().read(json);

        assertTrue(json.contains("\"views\""));
        assertTrue(reopened.umlClassDiagram().isPresent());
        UmlClassDiagramDocument loaded = reopened.umlClassDiagram().orElseThrow();
        assertEquals(2, loaded.viewCount());
        assertEquals(UmlClassDiagramViewKind.INTEGRATION, loaded.viewById("view-integracion-api").orElseThrow().kind());
        assertEquals(List.of("backend-java", "frontend-typescript"),
                loaded.viewById("view-integracion-api").orElseThrow().sourceRootIds());
        assertEquals("backend/src/main/java/com/cedro/productos", loaded.moduleById(backend.id()).orElseThrow().path());
        assertEquals("sourceRoot=frontend-typescript; sourceLanguage=TYPESCRIPT",
                loaded.classById(apiService.id()).orElseThrow().notes());
        assertEquals("inferred=true; inferenceReason=frontend-backend-api-match",
                loaded.relationById(api.id()).orElseThrow().notes());
    }
}
