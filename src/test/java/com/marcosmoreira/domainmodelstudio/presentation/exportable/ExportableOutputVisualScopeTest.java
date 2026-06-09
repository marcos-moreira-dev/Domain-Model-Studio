package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExportableOutputVisualScopeTest {

    @Test
    void visualScopedKeepsCompleteProjectSeparateFromVisualExportProject() {
        DiagramProject complete = DiagramProject.blank("complete", "Completo", DiagramTypeId.UML_CLASS);
        DiagramProject visual = DiagramProject.blank("visual", "Vista activa", DiagramTypeId.UML_CLASS);
        ExportableOutputDescriptor descriptor = ExportableOutputDescriptor.visualDiagram(
                DiagramTypeId.UML_CLASS,
                "UML Clases",
                "uml_clases",
                Set.of(ExportFormat.SVG, ExportFormat.PNG, ExportFormat.MARKDOWN));

        ExportableOutput output = ExportableOutput.visualScoped(descriptor, complete, visual, target -> { });

        assertEquals(complete, output.project().orElseThrow());
        assertEquals(visual, output.visualProject().orElseThrow());
        assertTrue(output.supports(ExportFormat.SVG));
    }
}
