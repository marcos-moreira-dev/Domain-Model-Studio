package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.RemoveUmlClassDiagramItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.ValidateUmlClassDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassLayoutPersistenceContractTest {

    @Test
    void viewModelMovesUmlClassThroughVisualLayoutInsteadOfDomainCoordinates() {
        List<DiagramProject> changes = new ArrayList<>();
        UmlClassDiagramViewModel viewModel = newViewModel();
        viewModel.registerProjectChangeListener(changes::add);
        viewModel.loadProject(project());

        viewModel.moveClassTo("estudiante", 420, 260);

        assertTrue(!changes.isEmpty());
        DiagramProject changed = changes.get(changes.size() - 1);
        var layout = changed.layouts().activeLayout().nodeFor(VisualElementLayoutIds.umlClass("estudiante")).orElseThrow();
        assertEquals(420, layout.x());
        assertEquals(260, layout.y());
        assertEquals("estudiante", changed.umlClassDiagram().orElseThrow().classes().get(0).id());
    }

    @Test
    void viewModelPersistsBendPointForUmlRelation() {
        List<DiagramProject> changes = new ArrayList<>();
        UmlClassDiagramViewModel viewModel = newViewModel();
        viewModel.registerProjectChangeListener(changes::add);
        viewModel.loadProject(project());

        viewModel.addConnectorBendPoint(VisualElementLayoutIds.umlRelation("rel-estudiante-seccion"), 300, 210);

        DiagramProject changed = changes.get(changes.size() - 1);
        var layout = changed.layouts().activeLayout().connectorById(VisualElementLayoutIds.umlRelation("rel-estudiante-seccion")).orElseThrow();
        assertEquals(1, layout.bendPoints().size());
        assertEquals(300, layout.bendPoints().get(0).x());
        assertEquals(210, layout.bendPoints().get(0).y());
    }

    private static UmlClassDiagramViewModel newViewModel() {
        return new UmlClassDiagramViewModel(
                new AddUmlModuleUseCase(),
                new AddUmlClassUseCase(),
                new AddUmlMemberUseCase(),
                new AddUmlRelationUseCase(),
                new UpdateUmlModuleUseCase(),
                new UpdateUmlClassUseCase(),
                new UpdateUmlMemberUseCase(),
                new UpdateUmlRelationUseCase(),
                new RemoveUmlClassDiagramItemUseCase(),
                new ValidateUmlClassDiagramUseCase(),
                ignored -> { }
        );
    }

    private static DiagramProject project() {
        UmlModuleGroup academico = new UmlModuleGroup("academico", "Académico", "uens.academico", "", "");
        UmlClassNode estudiante = new UmlClassNode("estudiante", academico.id(), "Estudiante", "uens.academico",
                UmlClassKind.CLASS, UmlVisibility.PUBLIC, "Persona matriculada", "", List.of(), "");
        UmlClassNode seccion = new UmlClassNode("seccion", academico.id(), "Seccion", "uens.academico",
                UmlClassKind.CLASS, UmlVisibility.PUBLIC, "Grupo académico", "", List.of(), "");
        UmlClassRelation relation = new UmlClassRelation("rel-estudiante-seccion", estudiante.id(), seccion.id(),
                UmlRelationKind.ASSOCIATION, "pertenece", "", "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "UENS",
                "borrador",
                LocalDate.of(2026, 5, 1),
                List.of(academico),
                List.of(estudiante, seccion),
                List.of(relation),
                "");
        return DiagramProject.blank("uml-uens", "UML UENS", DiagramTypeId.UML_CLASS)
                .withUmlClassDiagram(document);
    }
}
