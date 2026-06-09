package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import java.util.Comparator;
import java.util.Objects;

/** Exporta UML Clases como Markdown oficial e importable. */
public final class UmlClassMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        if (!DiagramTypeId.UML_CLASS.equals(project.metadata().diagramTypeId())) {
            throw new IllegalArgumentException("La salida activa no es UML Clases.");
        }
        UmlClassDiagramDocument document = project.umlClassDiagram()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene UML Clases."));
        return export(document);
    }

    public String export(UmlClassDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        StringBuilder markdown = new StringBuilder(8192);
        markdown.append("---\n")
                .append("dms_version: \"1\"\n")
                .append("diagram_type: \"").append(DiagramTypeId.UML_CLASS.value()).append("\"\n")
                .append("name: \"").append(escape(document.projectName())).append("\"\n")
                .append("sample_kind: \"project\"\n")
                .append("status: \"importable\"\n")
                .append("importable: true\n")
                .append("intended_output: \"diagrama visual\"\n")
                .append("---\n\n")
                .append("# Paquetes\n\n");
        if (document.modules().isEmpty()) {
            markdown.append("> Sin paquetes registrados todavía.\n\n");
        } else {
            document.modules().stream()
                    .sorted(Comparator.comparing(UmlModuleGroup::displayName))
                    .forEach(module -> writeModule(markdown, module));
        }
        markdown.append("# Clases\n\n");
        document.classes().stream()
                .sorted(Comparator.comparing(UmlClassNode::displayName))
                .forEach(node -> writeClass(markdown, document, node));
        writeViews(markdown, document);
        markdown.append("# Relaciones\n\n");
        if (document.relations().isEmpty()) {
            markdown.append("> Sin relaciones registradas todavía.\n");
        } else {
            document.relations().forEach(relation -> writeRelation(markdown, document, relation));
        }
        if (!document.notes().isBlank()) {
            markdown.append("\n# Observaciones\n\n").append(document.notes()).append('\n');
        }
        return markdown.toString();
    }

    private static void writeModule(StringBuilder markdown, UmlModuleGroup module) {
        markdown.append("## ").append(module.displayName()).append("\n")
                .append("id: ").append(module.id()).append("\n");
        if (!module.path().isBlank()) {
            markdown.append("ruta: ").append(module.path()).append("\n");
        }
        if (!module.description().isBlank()) {
            markdown.append("propósito: ").append(module.description()).append("\n");
        }
        if (!module.notes().isBlank()) {
            markdown.append("notas: ").append(module.notes()).append("\n");
        }
        markdown.append('\n');
    }

    private static void writeClass(StringBuilder markdown, UmlClassDiagramDocument document, UmlClassNode node) {
        markdown.append("## ").append(node.displayName()).append("\n")
                .append("id: ").append(node.id()).append("\n");
        if (!node.moduleId().isBlank()) {
            markdown.append("paquete: ")
                    .append(document.moduleById(node.moduleId()).map(UmlModuleGroup::id).orElse(node.moduleId()))
                    .append("\n");
        }
        markdown.append("tipo: ").append(node.kind().displayName()).append("\n")
                .append("visibilidad: ").append(node.visibility().displayName()).append("\n");
        if (!node.responsibility().isBlank()) {
            markdown.append("responsabilidad: ").append(node.responsibility()).append("\n");
        }
        if (!node.description().isBlank()) {
            markdown.append("descripción: ").append(node.description()).append("\n");
        }
        markdown.append("atributos:\n");
        node.members().stream()
                .filter(member -> member.kind() == UmlMemberKind.ATTRIBUTE)
                .forEach(member -> markdown.append("- ").append(member.displayText()).append("\n"));
        markdown.append("métodos:\n");
        node.members().stream()
                .filter(member -> member.kind() == UmlMemberKind.METHOD || member.kind() == UmlMemberKind.CONSTRUCTOR)
                .forEach(member -> markdown.append("- ").append(member.displayText()).append("\n"));
        if (!node.notes().isBlank()) {
            markdown.append("notas: ").append(node.notes()).append("\n");
        }
        markdown.append('\n');
    }

    private static void writeViews(StringBuilder markdown, UmlClassDiagramDocument document) {
        if (document.views().isEmpty()) {
            return;
        }
        markdown.append("# Vistas internas\n\n");
        document.views().forEach(view -> writeView(markdown, view));
    }

    private static void writeView(StringBuilder markdown, UmlClassDiagramView view) {
        markdown.append("## ").append(view.displayName()).append("\n")
                .append("id: ").append(view.id()).append("\n")
                .append("tipo: ").append(view.kind().name().toLowerCase(java.util.Locale.ROOT)).append("\n");
        if (!view.description().isBlank()) {
            markdown.append("descripción: ").append(view.description()).append("\n");
        }
        appendCsv(markdown, "source_roots", view.sourceRootIds());
        appendCsv(markdown, "módulos", view.moduleIds());
        appendCsv(markdown, "clases", view.classIds());
        appendCsv(markdown, "relaciones", view.relationIds());
        if (!view.notes().isBlank()) {
            markdown.append("notas: ").append(view.notes()).append("\n");
        }
        markdown.append('\n');
    }

    private static void appendCsv(StringBuilder markdown, String key, java.util.List<String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        markdown.append(key).append(": ").append(String.join(", ", values)).append("\n");
    }

    private static void writeRelation(StringBuilder markdown, UmlClassDiagramDocument document, UmlClassRelation relation) {
        String source = document.classById(relation.sourceClassId()).map(UmlClassNode::id).orElse(relation.sourceClassId());
        String target = document.classById(relation.targetClassId()).map(UmlClassNode::id).orElse(relation.targetClassId());
        markdown.append("- ").append(source).append(' ').append(symbol(relation.kind())).append(' ').append(target);
        if (!relation.label().isBlank()) {
            markdown.append(": ").append(relation.label());
        }
        markdown.append("\n");
    }

    private static String symbol(UmlRelationKind kind) {
        return switch (kind == null ? UmlRelationKind.DEPENDENCY : kind) {
            case INHERITANCE -> "<|--";
            case IMPLEMENTATION -> "<|..";
            case COMPOSITION -> "*--";
            case AGGREGATION -> "o--";
            case ASSOCIATION -> "--";
            case DEPENDENCY -> "-->";
        };
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
