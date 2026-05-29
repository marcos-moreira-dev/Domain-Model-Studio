package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
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
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassExportSafetyPolicyTest {

    private final UmlClassExportSafetyPolicy policy = new UmlClassExportSafetyPolicy();

    @Test
    void allowsSmallActiveViewSvgExport() {
        DiagramProject project = projectWith(40, 20, 2);

        assertDoesNotThrow(() -> policy.ensureSvgExportAllowed(project));
    }

    @Test
    void allowsCriticalMegaViewSvgExportAndLetsExporterReportRealFailures() {
        DiagramProject project = projectWith(320, 900, 4);

        assertDoesNotThrow(() -> policy.ensureSvgExportAllowed(project));
        assertTrue(policy.estimateForSvg(project).level() == UmlClassVisualCostLevel.CRITICAL);
    }

    @Test
    void allowsCriticalPngCostWhenRuntimeMemoryIsAvailable() {
        UmlClassVisualCostEstimate critical = new UmlClassVisualCostEstimate(
                4, 700, 1400, 7000, 6000, 1000, 8100,
                UmlSourceImportRenderProfile.LIGHT,
                UmlClassVisualCostLevel.CRITICAL);

        assertDoesNotThrow(() -> policy.ensurePngExportAllowed(critical, UmlClassRuntimeMemorySnapshot.empty()));
    }

    private DiagramProject projectWith(int classCount, int relationCount, int methodsPerClass) {
        List<UmlModuleGroup> modules = List.of(new UmlModuleGroup("backend", "Backend", "src/main/java", "", ""));
        List<UmlClassNode> classes = new ArrayList<>();
        for (int index = 0; index < classCount; index++) {
            classes.add(classWithMembers("clase-" + index, "Clase" + index, methodsPerClass));
        }
        List<UmlClassRelation> relations = new ArrayList<>();
        for (int index = 0; index < relationCount && classCount > 1; index++) {
            String source = "clase-" + (index % classCount);
            String target = "clase-" + ((index + 1) % classCount);
            relations.add(new UmlClassRelation("rel-" + index, source, target,
                    UmlRelationKind.DEPENDENCY, "usa", "", ""));
        }
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "UML", "test", LocalDate.now(), modules, classes, relations, List.of(), "");
        return DiagramProject.blank("uml", "UML", DiagramTypeId.UML_CLASS).withUmlClassDiagram(document);
    }

    private UmlClassNode classWithMembers(String id, String name, int methods) {
        List<UmlClassMember> members = new ArrayList<>();
        for (int index = 0; index < methods; index++) {
            members.add(new UmlClassMember("m" + index, UmlMemberKind.METHOD, "metodo" + index,
                    "void", "metodo" + index + "()", UmlVisibility.PUBLIC, false, ""));
        }
        return new UmlClassNode(id, "backend", name, "com.acme", UmlClassKind.CLASS,
                UmlVisibility.PUBLIC, "", "", members, "");
    }
}
