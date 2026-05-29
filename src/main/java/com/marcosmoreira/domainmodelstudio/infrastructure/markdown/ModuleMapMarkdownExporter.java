package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import java.util.Comparator;
import java.util.Objects;

/** Exporta el mapa de módulos como Markdown oficial e importable. */
public final class ModuleMapMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        if (!DiagramTypeId.ADMIN_MODULE_MAP.equals(project.metadata().diagramTypeId())) {
            throw new IllegalArgumentException("La salida activa no es un mapa de módulos.");
        }
        ModuleMapDocument document = project.moduleMap()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene mapa de módulos."));
        return export(document);
    }

    public String export(ModuleMapDocument document) {
        Objects.requireNonNull(document, "document");
        StringBuilder markdown = new StringBuilder(4096);
        markdown.append("---\n")
                .append("dms_version: \"1\"\n")
                .append("diagram_type: \"").append(DiagramTypeId.ADMIN_MODULE_MAP.value()).append("\"\n")
                .append("name: \"").append(escape(document.projectName())).append("\"\n")
                .append("sample_kind: \"project\"\n")
                .append("status: \"importable\"\n")
                .append("importable: true\n")
                .append("intended_output: \"diagrama visual\"\n")
                .append("---\n\n")
                .append("# Módulos\n\n");
        document.rootModules().stream()
                .sorted(Comparator.comparing(ModuleNode::displayName))
                .forEach(module -> writeModule(markdown, document, module));
        markdown.append("# Dependencias\n\n");
        if (document.dependencies().isEmpty()) {
            markdown.append("> Sin dependencias registradas todavía.\n");
        } else {
            document.dependencies().forEach(dependency -> writeDependency(markdown, document, dependency));
        }
        if (!document.notes().isBlank()) {
            markdown.append("\n# Observaciones\n\n").append(document.notes()).append('\n');
        }
        return markdown.toString();
    }

    private static void writeModule(StringBuilder markdown, ModuleMapDocument document, ModuleNode module) {
        markdown.append("## ").append(module.displayName()).append("\n")
                .append("id: ").append(module.id()).append("\n");
        if (!module.responsibility().isBlank()) {
            markdown.append("responsabilidad: ").append(module.responsibility()).append("\n");
        }
        if (!module.description().isBlank()) {
            markdown.append("descripción: ").append(module.description()).append("\n");
        }
        markdown.append('\n');
        var children = document.childrenOf(module.id());
        if (!children.isEmpty()) {
            markdown.append("### Submódulos\n");
            children.stream()
                    .sorted(Comparator.comparing(ModuleNode::displayName))
                    .forEach(child -> markdown.append("- ")
                            .append(shortChildId(module, child))
                            .append(": ")
                            .append(child.responsibility().isBlank() ? child.description() : child.responsibility())
                            .append("\n"));
            markdown.append('\n');
        }
    }

    private static void writeDependency(StringBuilder markdown, ModuleMapDocument document, ModuleDependency dependency) {
        String source = document.moduleById(dependency.sourceModuleId()).map(ModuleNode::id).orElse(dependency.sourceModuleId());
        String target = document.moduleById(dependency.targetModuleId()).map(ModuleNode::id).orElse(dependency.targetModuleId());
        markdown.append("- ").append(source).append(" -> ").append(target).append(": ")
                .append(dependency.description().isBlank() ? dependency.kind().displayName() : dependency.description())
                .append("\n");
    }

    private static String shortChildId(ModuleNode parent, ModuleNode child) {
        String prefix = parent.id() + "_";
        return child.id().startsWith(prefix) ? child.id().substring(prefix.length()) : child.id();
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
