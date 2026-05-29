package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Exporta wireframes administrativos a Markdown oficial importable. */
public final class WireframeMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        WireframeDocument document = project.wireframe()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene wireframes administrativos."));
        StringBuilder markdown = new StringBuilder();
        appendFrontMatter(markdown, document);
        markdown.append("# Pantallas\n\n");
        for (WireframeScreen screen : document.screens()) {
            appendScreen(markdown, document, screen);
        }
        markdown.append("# Cierre de lectura\n\n");
        markdown.append("Este Markdown describe maquetas administrativas de baja fidelidad. ");
        markdown.append("Sirve como referencia para análisis, conversación con cliente y generación asistida de interfaz.\n");
        return markdown.toString();
    }

    private static void appendFrontMatter(StringBuilder markdown, WireframeDocument document) {
        markdown.append("---\n");
        markdown.append("dms_version: \"1\"\n");
        markdown.append("diagram_type: \"admin-wireframes\"\n");
        markdown.append("name: \"").append(escape(document.projectName())).append("\"\n");
        markdown.append("version: \"").append(escape(document.version())).append("\"\n");
        markdown.append("sample_kind: \"exported\"\n");
        markdown.append("status: \"importable\"\n");
        markdown.append("importable: true\n");
        markdown.append("intended_output: \"wireframe visual\"\n");
        markdown.append("---\n\n");
    }

    private static void appendScreen(StringBuilder markdown, WireframeDocument document, WireframeScreen screen) {
        markdown.append("## ").append(screen.displayName()).append("\n");
        markdown.append("id: ").append(screen.id()).append("\n");
        markdown.append("tipo: escritorio\n");
        if (!screen.moduleName().isBlank()) {
            markdown.append("módulo: ").append(screen.moduleName()).append("\n");
        }
        if (!screen.purpose().isBlank()) {
            markdown.append("propósito: ").append(screen.purpose()).append("\n");
        }
        if (!screen.notes().isBlank()) {
            markdown.append("notas: ").append(screen.notes()).append("\n");
        }
        List<WireframeComponent> screenComponents = document.components().stream()
                .filter(component -> component.screenId().equals(screen.id()))
                .sorted(Comparator.comparingInt(WireframeComponent::orderIndex))
                .toList();
        markdown.append("\n### Secciones\n");
        appendComponents(markdown, screenComponents, WireframeComponentKind.TOP_BAR, WireframeComponentKind.SIDEBAR,
                WireframeComponentKind.SECTION, WireframeComponentKind.PANEL, WireframeComponentKind.CARD,
                WireframeComponentKind.TABS, WireframeComponentKind.MODAL, WireframeComponentKind.ALERT,
                WireframeComponentKind.CHART, WireframeComponentKind.REPORT, WireframeComponentKind.DETAIL,
                WireframeComponentKind.STEPPER, WireframeComponentKind.BADGE, WireframeComponentKind.EMPTY_STATE,
                WireframeComponentKind.DOCUMENT_LIST, WireframeComponentKind.CALENDAR,
                WireframeComponentKind.APPROVAL_PANEL, WireframeComponentKind.SUMMARY);
        markdown.append("\n### Controles\n");
        appendComponents(markdown, screenComponents, WireframeComponentKind.FORM, WireframeComponentKind.FIELD,
                WireframeComponentKind.TABLE, WireframeComponentKind.BUTTON, WireframeComponentKind.FILTER,
                WireframeComponentKind.SEARCH, WireframeComponentKind.PAGINATION,
                WireframeComponentKind.MENU, WireframeComponentKind.OTHER);
        markdown.append("\n### Navegación ligera\n");
        appendNavigationHints(markdown, screenComponents);
        markdown.append("\n");
    }

    private static void appendComponents(StringBuilder markdown, List<WireframeComponent> components, WireframeComponentKind... kinds) {
        boolean any = false;
        for (WireframeComponent component : components) {
            if (!matches(component.kind(), kinds)) {
                continue;
            }
            any = true;
            markdown.append("- ").append(component.displayName());
            String detail = firstNonBlank(component.behavior(), component.notes(), component.dataBinding());
            if (!detail.isBlank()) {
                markdown.append(": ").append(detail);
            }
            markdown.append("\n");
            markdown.append("  tipo: ").append(component.kind().name()).append("\n");
            if (!component.dataBinding().isBlank()) {
                markdown.append("  dato: ").append(component.dataBinding()).append("\n");
            }
            if (!component.behavior().isBlank()) {
                markdown.append("  comportamiento: ").append(component.behavior()).append("\n");
            }
            if (!component.notes().isBlank()) {
                markdown.append("  notas: ").append(component.notes()).append("\n");
            }
        }
        if (!any) {
            markdown.append("- pendiente: completar esta parte del wireframe.\n");
        }
    }

    private static void appendNavigationHints(StringBuilder markdown, List<WireframeComponent> components) {
        List<WireframeComponent> navigation = components.stream()
                .filter(component -> component.kind() == WireframeComponentKind.BUTTON || component.kind() == WireframeComponentKind.MENU)
                .filter(component -> looksLikeNavigation(component.behavior()) || looksLikeNavigation(component.notes()))
                .toList();
        if (navigation.isEmpty()) {
            markdown.append("- pendiente: documentar navegación o confirmar que esta pantalla no cambia de vista.\n");
            return;
        }
        for (WireframeComponent component : navigation) {
            markdown.append("- ").append(component.displayName()).append(": ")
                    .append(firstNonBlank(component.behavior(), component.notes())).append("\n");
        }
    }

    private static boolean looksLikeNavigation(String value) {
        String text = value == null ? "" : value.toLowerCase();
        return text.contains("abre") || text.contains("navega") || text.contains("lleva")
                || text.contains("redirige") || text.contains("vuelve") || text.contains("detalle")
                || text.contains("formulario") || text.contains("pantalla");
    }

    private static boolean matches(WireframeComponentKind kind, WireframeComponentKind... kinds) {
        for (WireframeComponentKind candidate : kinds) {
            if (kind == candidate) {
                return true;
            }
        }
        return false;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
