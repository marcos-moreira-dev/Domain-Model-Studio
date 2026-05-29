package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import java.util.Objects;

/** Exporta flujo de pantallas a Markdown oficial importable. */
public final class ScreenFlowMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        ScreenFlowDocument document = project.screenFlow()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene flujo de pantallas."));
        StringBuilder markdown = new StringBuilder();
        markdown.append("---\n");
        markdown.append("dms_version: \"1\"\n");
        markdown.append("diagram_type: \"screen-flow\"\n");
        markdown.append("name: \"").append(escape(document.projectName())).append("\"\n");
        markdown.append("version: \"").append(escape(document.version())).append("\"\n");
        markdown.append("sample_kind: \"exported\"\n");
        markdown.append("status: \"importable\"\n");
        markdown.append("importable: true\n");
        markdown.append("intended_output: \"diagrama visual\"\n");
        markdown.append("---\n\n");
        markdown.append("# Pantallas\n\n");
        for (ScreenNode screen : document.screens()) {
            markdown.append("## ").append(screen.displayName()).append("\n");
            markdown.append("id: ").append(screen.id()).append("\n");
            markdown.append("tipo: ").append(screen.kind().displayName()).append("\n");
            if (!screen.moduleName().isBlank()) {
                markdown.append("módulo: ").append(screen.moduleName()).append("\n");
            }
            if (!screen.route().isBlank()) {
                markdown.append("ruta: ").append(screen.route()).append("\n");
            }
            if (!screen.purpose().isBlank()) {
                markdown.append("propósito: ").append(screen.purpose()).append("\n");
            }
            if (!screen.notes().isBlank()) {
                markdown.append("notas: ").append(screen.notes()).append("\n");
            }
            markdown.append("\n");
        }
        markdown.append("# Navegación\n\n");
        for (ScreenTransition transition : document.transitions()) {
            markdown.append("- ").append(transition.sourceScreenId())
                    .append(" -> ").append(transition.targetScreenId());
            if (!transition.trigger().isBlank()) {
                markdown.append(": ").append(transition.trigger());
            }
            markdown.append("\n");
        }
        return markdown.toString();
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
