package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Importa Markdown oficial de wireframes administrativos hacia un proyecto editable. */
public final class WireframeMarkdownParser implements MarkdownModelParser {

    @Override
    public DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "markdownFile");
        return parse(Files.readString(markdownFile, StandardCharsets.UTF_8), markdownFile.toString());
    }

    @Override
    public DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException {
        Objects.requireNonNull(markdownContent, "markdownContent");
        MarkdownImportDocument importDocument = MarkdownImportDocument.parse(markdownContent);
        MarkdownFrontMatter frontMatter = importDocument.frontMatter();
        String title = frontMatter.valueOrDefault("name", "Wireframes administrativos importados");
        Map<String, WireframeScreen> screens = new LinkedHashMap<>();
        List<WireframeComponent> components = new ArrayList<>();
        parseBody(importDocument.body(), screens, components);
        if (screens.isEmpty()) {
            throw new MarkdownModelParsingException("El wireframe no contiene pantallas reconocibles.");
        }
        WireframeDocument document;
        try {
            document = new WireframeDocument(
                    title,
                    frontMatter.valueOrDefault("version", "borrador"),
                    LocalDate.now(),
                    new ArrayList<>(screens.values()),
                    components);
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir el wireframe: " + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title), title, DiagramTypeId.ADMIN_WIREFRAMES).withWireframe(document),
                sourceName);
    }

    private static void parseBody(
            String body,
            Map<String, WireframeScreen> screens,
            List<WireframeComponent> components
    ) throws MarkdownModelParsingException {
        Section section = Section.NONE;
        PendingScreen pending = null;
        PendingComponent pendingComponent = null;
        String currentScreenId = "";
        int componentCounter = 1;
        int orderInScreen = 0;
        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank() || line.startsWith(">")) {
                continue;
            }
            String lower = line.toLowerCase(Locale.ROOT);
            if (line.startsWith("# ")) {
                pendingComponent = flushComponent(components, pendingComponent);
                if (pending != null) {
                    currentScreenId = putScreen(screens, pending.toScreen());
                    pending = null;
                    orderInScreen = 0;
                }
                section = lower.contains("pantalla") || lower.contains("wireframe") ? Section.SCREENS : Section.NONE;
                continue;
            }
            if (line.startsWith("## ") && section != Section.COMPONENTS) {
                pendingComponent = flushComponent(components, pendingComponent);
                if (pending != null) {
                    currentScreenId = putScreen(screens, pending.toScreen());
                }
                pending = new PendingScreen(line.substring(3).strip());
                section = Section.SCREENS;
                orderInScreen = 0;
                continue;
            }
            if (line.startsWith("### ")) {
                pendingComponent = flushComponent(components, pendingComponent);
                if (pending != null) {
                    currentScreenId = putScreen(screens, pending.toScreen());
                    pending = null;
                    orderInScreen = 0;
                }
                if (currentScreenId.isBlank()) {
                    throw new MarkdownModelParsingException("Se declararon componentes antes de una pantalla.");
                }
                if (lower.contains("navegacion") || lower.contains("navegación")) {
                    section = Section.NAVIGATION;
                } else if (lower.contains("seccion") || lower.contains("sección")) {
                    section = Section.SECTIONS;
                } else if (lower.contains("control") || lower.contains("componente")) {
                    section = Section.CONTROLS;
                } else {
                    section = Section.COMPONENTS;
                }
                continue;
            }
            if (section == Section.SCREENS && pending != null && MarkdownTextUtils.isPropertyLine(line)) {
                String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
                String value = MarkdownTextUtils.valueAfterColon(line);
                if (key.equals("id")) {
                    pending.id = MarkdownTextUtils.toStableId(value);
                } else if (key.equals("modulo") || key.equals("módulo")) {
                    pending.moduleName = value;
                } else if (key.equals("proposito") || key.equals("propósito")) {
                    pending.purpose = value;
                } else if (key.equals("notas")) {
                    pending.notes = value;
                }
                continue;
            }
            if (pendingComponent != null && isComponentPropertyLine(line)) {
                pendingComponent.apply(line);
                continue;
            }
            if ((section == Section.SECTIONS || section == Section.CONTROLS || section == Section.COMPONENTS) && line.startsWith("- ")) {
                pendingComponent = flushComponent(components, pendingComponent);
                PendingComponent parsed = parseComponent(line.substring(2).strip(), currentScreenId, section, componentCounter, orderInScreen);
                if (parsed != null) {
                    pendingComponent = parsed;
                    componentCounter++;
                    orderInScreen++;
                }
            }
        }
        flushComponent(components, pendingComponent);
        if (pending != null) {
            putScreen(screens, pending.toScreen());
        }
    }

    private static PendingComponent parseComponent(
            String text,
            String screenId,
            Section section,
            int counter,
            int orderIndex
    ) throws MarkdownModelParsingException {
        int colon = text.indexOf(':');
        String rawName = colon >= 0 ? text.substring(0, colon).strip() : text.strip();
        String description = colon >= 0 ? text.substring(colon + 1).strip() : "";
        if (rawName.isBlank()) {
            throw new MarkdownModelParsingException("Componente sin nombre en pantalla " + screenId + ".");
        }
        if (isPlaceholderComponent(rawName, description)) {
            return null;
        }
        String id = MarkdownTextUtils.toStableId(screenId + "_" + rawName + "_" + counter);
        WireframeComponentKind kind = inferKind(rawName, description, section);
        String displayName = displayName(rawName);
        String binding = inferBinding(rawName, description, kind);
        return new PendingComponent(id, screenId, kind, displayName, orderIndex, binding, description, "");
    }

    private static PendingComponent flushComponent(List<WireframeComponent> components, PendingComponent pendingComponent) {
        if (pendingComponent != null) {
            components.add(pendingComponent.toComponent());
        }
        return null;
    }

    private static boolean isComponentPropertyLine(String line) {
        if (!MarkdownTextUtils.isPropertyLine(line) || line.startsWith("- ") || line.startsWith("|")) {
            return false;
        }
        String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
        return key.equals("tipo") || key.equals("dato") || key.equals("binding")
                || key.equals("comportamiento") || key.equals("notas");
    }

    private static boolean isPlaceholderComponent(String rawName, String description) {
        String text = MarkdownTextUtils.toStableId(rawName + " " + description);
        return text.startsWith("pendiente")
                && (text.contains("completar") || text.contains("documentar") || text.contains("confirmar"));
    }

    private static WireframeComponentKind inferKind(String rawName, String description, Section section) {
        String text = MarkdownTextUtils.toStableId(rawName + " " + description);
        if (text.contains("barra_superior") || text.contains("topbar") || text.contains("encabezado")) {
            return WireframeComponentKind.TOP_BAR;
        }
        if (text.contains("menu_lateral") || text.contains("sidebar") || text.contains("navegacion")) {
            return WireframeComponentKind.SIDEBAR;
        }
        if (text.contains("pestana") || text.contains("pestanas") || text.contains("pestaña")
                || text.contains("pestañas") || text.contains("tabs") || text.contains("tab_")) {
            return WireframeComponentKind.TABS;
        }
        if (text.contains("paso") || text.contains("stepper") || text.contains("wizard")) {
            return WireframeComponentKind.STEPPER;
        }
        if (text.contains("badge") || text.contains("etiqueta_estado") || text.contains("estado_actual") || text.contains("prioridad")) {
            return WireframeComponentKind.BADGE;
        }
        if (text.contains("estado_vacio") || text.contains("sin_resultados") || text.contains("sin_pendientes")) {
            return WireframeComponentKind.EMPTY_STATE;
        }
        if (text.contains("documentos") || text.contains("evidencias") || text.contains("adjuntos") || text.contains("lista_documental")) {
            return WireframeComponentKind.DOCUMENT_LIST;
        }
        if (text.contains("calendario") || text.contains("agenda") || text.contains("cita") || text.contains("evento")) {
            return WireframeComponentKind.CALENDAR;
        }
        if (text.contains("aprobacion") || text.contains("aprobación") || text.contains("rechazo") || text.contains("decision")) {
            return WireframeComponentKind.APPROVAL_PANEL;
        }
        if (text.contains("resumen")) {
            return WireframeComponentKind.SUMMARY;
        }
        if (text.contains("alerta") || text.contains("error") || text.contains("advertencia") || text.contains("validacion")) {
            return WireframeComponentKind.ALERT;
        }
        if (text.contains("paginacion") || text.contains("pagina_actual")) {
            return WireframeComponentKind.PAGINATION;
        }
        if (text.contains("busqueda") || text.contains("buscar")) {
            return WireframeComponentKind.SEARCH;
        }
        if (text.contains("filtro") || text.contains("criterio")) {
            return WireframeComponentKind.FILTER;
        }
        if (text.contains("boton") || text.contains("accion") || text.contains("guardar") || text.contains("exportar")) {
            return WireframeComponentKind.BUTTON;
        }
        if (text.contains("tabla") || text.contains("listado") || text.contains("resultados")) {
            return WireframeComponentKind.TABLE;
        }
        if (text.contains("formulario") || text.contains("form")) {
            return WireframeComponentKind.FORM;
        }
        if (text.contains("modal") || text.contains("dialogo") || text.contains("diálogo")) {
            return WireframeComponentKind.MODAL;
        }
        if (text.contains("grafico") || text.contains("gráfico") || text.contains("chart")) {
            return WireframeComponentKind.CHART;
        }
        if (text.contains("reporte") || text.contains("documento")) {
            return WireframeComponentKind.REPORT;
        }
        if (text.contains("detalle")) {
            return WireframeComponentKind.DETAIL;
        }
        if (text.contains("campo") || text.contains("fecha") || text.contains("cliente") || text.contains("estado")) {
            return WireframeComponentKind.FIELD;
        }
        if (text.contains("tarjeta") || text.contains("card")) {
            return WireframeComponentKind.CARD;
        }
        if (text.contains("menu") || text.contains("menú")) {
            return WireframeComponentKind.MENU;
        }
        if (text.contains("panel")) {
            return WireframeComponentKind.PANEL;
        }
        if (section == Section.SECTIONS) {
            return WireframeComponentKind.SECTION;
        }
        return WireframeComponentKind.OTHER;
    }

    private static String inferBinding(String rawName, String description, WireframeComponentKind kind) {
        if (kind == WireframeComponentKind.BUTTON || kind == WireframeComponentKind.SECTION || kind == WireframeComponentKind.PANEL
                || kind == WireframeComponentKind.TOP_BAR || kind == WireframeComponentKind.SIDEBAR
                || kind == WireframeComponentKind.MENU || kind == WireframeComponentKind.ALERT
                || kind == WireframeComponentKind.MODAL || kind == WireframeComponentKind.PAGINATION
                || kind == WireframeComponentKind.STEPPER || kind == WireframeComponentKind.BADGE
                || kind == WireframeComponentKind.EMPTY_STATE || kind == WireframeComponentKind.DOCUMENT_LIST
                || kind == WireframeComponentKind.CALENDAR || kind == WireframeComponentKind.APPROVAL_PANEL
                || kind == WireframeComponentKind.SUMMARY) {
            return "";
        }
        String stable = MarkdownTextUtils.toStableId(rawName);
        return stable.isBlank() ? MarkdownTextUtils.toStableId(description) : stable;
    }

    private static String putScreen(Map<String, WireframeScreen> screens, WireframeScreen screen)
            throws MarkdownModelParsingException {
        if (screens.containsKey(screen.id())) {
            throw new MarkdownModelParsingException("Pantalla duplicada en wireframe Markdown: " + screen.id());
        }
        screens.put(screen.id(), screen);
        return screen.id();
    }


    private static String stableProjectId(String title) {
        return "wireframes_" + MarkdownTextUtils.toStableId(title);
    }

    private static String displayName(String value) {
        String text = value == null ? "" : value.strip().replace('_', ' ');
        if (text.isBlank()) {
            return "Componente";
        }
        return text.substring(0, 1).toUpperCase(Locale.ROOT) + text.substring(1);
    }

    private static String normalizeKey(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }

    private enum Section {
        NONE,
        SCREENS,
        SECTIONS,
        CONTROLS,
        COMPONENTS,
        NAVIGATION
    }

    private static WireframeComponentKind parseKind(String value, WireframeComponentKind fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String normalized = MarkdownTextUtils.toStableId(value);
        for (WireframeComponentKind candidate : WireframeComponentKind.values()) {
            if (candidate.name().equalsIgnoreCase(value.strip())
                    || MarkdownTextUtils.toStableId(candidate.name()).equals(normalized)
                    || MarkdownTextUtils.toStableId(candidate.displayName()).equals(normalized)) {
                return candidate;
            }
        }
        return fallback;
    }

    private static final class PendingScreen {
        private final String title;
        private String id;
        private String moduleName = "";
        private String purpose = "";
        private String notes = "";

        private PendingScreen(String title) {
            this.title = title == null || title.isBlank() ? "Pantalla" : title.strip();
            this.id = MarkdownTextUtils.toStableId(this.title);
        }

        private WireframeScreen toScreen() {
            return new WireframeScreen(id, title, moduleName, purpose, notes);
        }
    }

    private static final class PendingComponent {
        private final String id;
        private final String screenId;
        private final String displayName;
        private final int orderIndex;
        private WireframeComponentKind kind;
        private String dataBinding;
        private String behavior;
        private String notes;

        private PendingComponent(
                String id,
                String screenId,
                WireframeComponentKind kind,
                String displayName,
                int orderIndex,
                String dataBinding,
                String behavior,
                String notes
        ) {
            this.id = id;
            this.screenId = screenId;
            this.kind = kind;
            this.displayName = displayName;
            this.orderIndex = orderIndex;
            this.dataBinding = dataBinding == null ? "" : dataBinding.strip();
            this.behavior = behavior == null ? "" : behavior.strip();
            this.notes = notes == null ? "" : notes.strip();
        }

        private void apply(String line) {
            String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
            String value = MarkdownTextUtils.valueAfterColon(line);
            if (key.equals("tipo")) {
                this.kind = parseKind(value, this.kind);
            } else if (key.equals("dato") || key.equals("binding")) {
                this.dataBinding = value;
            } else if (key.equals("comportamiento")) {
                this.behavior = value;
            } else if (key.equals("notas")) {
                this.notes = value;
            }
        }

        private WireframeComponent toComponent() {
            return new WireframeComponent(id, screenId, kind, displayName, orderIndex, dataBinding, behavior, notes);
        }
    }

}
