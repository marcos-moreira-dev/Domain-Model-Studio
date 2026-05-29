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
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassDeferredViewLayoutTest {

    @Test
    void loadProjectPreparesOnlyInitialSummaryViewInsteadOfWholeUmlDocument() {
        UmlClassDiagramViewModel viewModel = newViewModel();
        DiagramProject project = DiagramProject.blank("uml-grande", "UML Grande", DiagramTypeId.UML_CLASS)
                .withUmlClassDiagram(largeDocumentWithSmallSummary());

        viewModel.loadProject(project);

        assertEquals(180, viewModel.currentDocument().classCount(), "El modelo completo debe seguir intacto.");
        assertEquals(5, viewModel.classes().size(), "La vista inicial debe publicar solo el resumen visible.");
        assertTrue(viewModel.currentProject().layouts().activeLayout()
                        .nodeFor(VisualElementLayoutIds.umlClass("clase-179")).isEmpty(),
                "La apertura inicial no debe materializar layout para clases fuera del resumen.");
        assertTrue(viewModel.currentProject().layouts().activeLayout()
                        .nodeFor(VisualElementLayoutIds.umlClass("clase-0")).isPresent(),
                "La apertura inicial sí debe preparar las clases visibles del resumen.");
        assertTrue(viewModel.currentProject().layouts().activeLayout().nodeCount() <= 6,
                "El layout inicial debe contener solo módulo + clases visibles, no las 180 clases.");
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

    private static UmlClassDiagramDocument largeDocumentWithSmallSummary() {
        UmlModuleGroup module = new UmlModuleGroup("mod", "Módulo", "mod", "", "");
        List<UmlClassNode> classes = new ArrayList<>();
        for (int i = 0; i < 180; i++) {
            classes.add(new UmlClassNode("clase-" + i, module.id(), "Clase" + i, "mod",
                    UmlClassKind.CLASS, UmlVisibility.PUBLIC, "", "", List.of(), ""));
        }
        List<String> summaryIds = classes.stream().limit(5).map(UmlClassNode::id).toList();
        List<String> fullIds = classes.stream().map(UmlClassNode::id).toList();
        UmlClassDiagramView summary = new UmlClassDiagramView("resumen", UmlClassDiagramViewKind.SUMMARY,
                "Resumen", "Vista inicial segura", List.of(), List.of(), summaryIds, List.of(), "");
        UmlClassDiagramView full = new UmlClassDiagramView("mega", UmlClassDiagramViewKind.FULL,
                "Mega vista", "Vista completa", List.of(), List.of(), fullIds, List.of(), "");
        return new UmlClassDiagramDocument("UML Grande", "borrador", LocalDate.of(2026, 5, 17),
                List.of(module), classes, List.of(), List.of(summary, full), "");
    }
}
