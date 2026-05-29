package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import java.util.List;

/** Parser auxiliar de la sección de vistas internas del Markdown UML Clases. */
final class UmlClassMarkdownViewSectionParser {

    private UmlClassMarkdownViewSectionParser() { }

    static PendingView pending(String title) {
        return new PendingView(title);
    }

    static void applyProperty(PendingView pendingView, String line) {
        String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
        String value = MarkdownTextUtils.valueAfterColon(line);
        if (key.equals("id")) {
            pendingView.id = MarkdownTextUtils.toStableId(value);
        } else if (key.equals("tipo") || key.equals("kind")) {
            pendingView.kind = parseViewKind(value);
        } else if (key.equals("descripcion") || key.equals("descripción")) {
            pendingView.description = value;
        } else if (key.equals("source_roots") || key.equals("source roots") || key.equals("raices") || key.equals("raíces")) {
            pendingView.sourceRootIds = splitCsv(value);
        } else if (key.equals("modulos") || key.equals("módulos") || key.equals("paquetes")) {
            pendingView.moduleIds = splitCsv(value);
        } else if (key.equals("clases")) {
            pendingView.classIds = splitCsv(value);
        } else if (key.equals("relaciones")) {
            pendingView.relationIds = splitCsv(value);
        } else if (key.equals("notas")) {
            pendingView.notes = value;
        }
    }

    static void putView(List<UmlClassDiagramView> views, PendingView pendingView) throws MarkdownModelParsingException {
        UmlClassDiagramView view = pendingView.toView();
        if (views.stream().anyMatch(existing -> existing.id().equals(view.id()))) {
            throw new MarkdownModelParsingException("Vista duplicada en UML Clases: " + view.id());
        }
        views.add(view);
    }

    private static UmlClassDiagramViewKind parseViewKind(String value) {
        String normalized = MarkdownTextUtils.toStableId(value);
        for (UmlClassDiagramViewKind kind : UmlClassDiagramViewKind.values()) {
            if (MarkdownTextUtils.toStableId(kind.name()).equals(normalized)) {
                return kind;
            }
        }
        return UmlClassDiagramViewKind.CUSTOM;
    }

    private static List<String> splitCsv(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return java.util.Arrays.stream(value.split(","))
                .map(String::strip)
                .filter(item -> !item.isBlank())
                .toList();
    }

    private static String normalizeKey(String value) {
        return value == null ? "" : value.strip().toLowerCase(java.util.Locale.ROOT);
    }

    static final class PendingView {
        private final String title;
        private String id;
        private UmlClassDiagramViewKind kind = UmlClassDiagramViewKind.CUSTOM;
        private String description = "";
        private List<String> sourceRootIds = List.of();
        private List<String> moduleIds = List.of();
        private List<String> classIds = List.of();
        private List<String> relationIds = List.of();
        private String notes = "";

        private PendingView(String title) {
            this.title = title;
            this.id = MarkdownTextUtils.toStableId(title);
        }

        private UmlClassDiagramView toView() {
            return new UmlClassDiagramView(id, kind, title, description, sourceRootIds, moduleIds, classIds, relationIds, notes);
        }
    }
}
