package com.marcosmoreira.domainmodelstudio.presentation.statusbar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Protege el vocabulario contextual de la barra de estado. */
class ProjectStatusSummaryFormatterTest {

    private final ProjectStatusSummaryFormatter formatter = new ProjectStatusSummaryFormatter();

    @Test
    void summarizesWireframesWithoutConceptualVocabulary() {
        DiagramProject project = DiagramProject.blank("wire", "Wireframes", DiagramTypeId.ADMIN_WIREFRAMES)
                .withWireframe(WireframeDocument.blank("Wireframes"));

        ProjectStatusSummary summary = formatter.summarize(project);

        assertEquals("Wireframes", summary.viewLabel());
        assertEquals("0 pantallas / 0 componentes", summary.elementSummary());
    }

    @Test
    void summarizesRolesPermissionsWithSecurityVocabulary() {
        DiagramProject project = DiagramProject.blank("roles", "Roles", DiagramTypeId.ROLES_PERMISSIONS_MAP)
                .withRolesPermissions(RolesPermissionsDocument.blank("Roles"));

        ProjectStatusSummary summary = formatter.summarize(project);

        assertEquals("Roles y permisos", summary.viewLabel());
        assertEquals("0 roles / 0 permisos / 0 asignaciones", summary.elementSummary());
    }

    @Test
    void summarizesScreenFlowWithNavigationVocabulary() {
        DiagramProject project = DiagramProject.blank("flow", "Flujo", DiagramTypeId.SCREEN_FLOW)
                .withScreenFlow(ScreenFlowDocument.blank("Flujo"));

        ProjectStatusSummary summary = formatter.summarize(project);

        assertEquals("Flujo de pantallas", summary.viewLabel());
        assertEquals("0 pantallas / 0 transiciones", summary.elementSummary());
    }

    @Test
    void summarizesUseCaseAsBehaviorDiagram() {
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Casos de uso",
                "borrador",
                LocalDate.now(),
                BehaviorDiagramKind.UML_USE_CASE,
                List.of(new BehaviorNode("actor-1", BehaviorNodeKind.ACTOR, "Secretaría", "", "", "", 0)),
                List.of(),
                "");
        DiagramProject project = DiagramProject.blank("usecase", "Casos", DiagramTypeId.UML_USE_CASE)
                .withBehaviorDiagram(document);

        ProjectStatusSummary summary = formatter.summarize(project);

        assertEquals("UML Casos de uso", summary.viewLabel());
        assertEquals("1 elementos / 0 relaciones", summary.elementSummary());
    }
}
