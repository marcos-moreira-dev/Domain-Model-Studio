package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenFlowDocument;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenKind;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenNode;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransition;
import com.marcosmoreira.domainmodelstudio.domain.screenflow.ScreenTransitionKind;
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

/** Importa Markdown oficial de flujo de pantallas hacia un proyecto editable. */
public final class ScreenFlowMarkdownParser implements MarkdownModelParser {

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
        String title = frontMatter.valueOrDefault("name", "Flujo de pantallas importado");
        Map<String, ScreenNode> screens = new LinkedHashMap<>();
        List<ScreenTransition> transitions = new ArrayList<>();
        parseBody(importDocument.body(), screens, transitions);
        if (screens.isEmpty()) {
            throw new MarkdownModelParsingException("El flujo de pantallas no contiene pantallas reconocibles.");
        }
        ScreenFlowDocument document;
        try {
            document = new ScreenFlowDocument(
                    title,
                    frontMatter.valueOrDefault("version", "borrador"),
                    LocalDate.now(),
                    new ArrayList<>(screens.values()),
                    transitions);
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir el flujo de pantallas: " + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title), title, DiagramTypeId.SCREEN_FLOW).withScreenFlow(document),
                sourceName);
    }

    private static void parseBody(
            String body,
            Map<String, ScreenNode> screens,
            List<ScreenTransition> transitions
    ) throws MarkdownModelParsingException {
        Section section = Section.NONE;
        PendingScreen pending = null;
        int transitionCounter = 1;
        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank() || line.startsWith(">")) {
                continue;
            }
            String lower = line.toLowerCase(Locale.ROOT);
            if (line.startsWith("# ")) {
                if (pending != null) {
                    putScreen(screens, pending.toScreen());
                    pending = null;
                }
                if (lower.contains("pantalla")) {
                    section = Section.SCREENS;
                } else if (lower.contains("naveg") || lower.contains("transici")) {
                    section = Section.NAVIGATION;
                } else {
                    section = Section.NONE;
                }
                continue;
            }
            if (line.startsWith("## ") && section == Section.SCREENS) {
                if (pending != null) {
                    putScreen(screens, pending.toScreen());
                }
                pending = new PendingScreen(line.substring(3).strip());
                continue;
            }
            if (section == Section.SCREENS && pending != null && MarkdownTextUtils.isPropertyLine(line)) {
                String key = normalizeKey(MarkdownTextUtils.keyBeforeColon(line));
                String value = MarkdownTextUtils.valueAfterColon(line);
                if (key.equals("id")) {
                    pending.id = MarkdownTextUtils.toStableId(value);
                } else if (key.equals("tipo")) {
                    pending.kind = parseScreenKind(value);
                } else if (key.equals("modulo") || key.equals("módulo")) {
                    pending.moduleName = value;
                } else if (key.equals("ruta")) {
                    pending.route = value;
                } else if (key.equals("proposito") || key.equals("propósito")) {
                    pending.purpose = value;
                } else if (key.equals("notas")) {
                    pending.notes = value;
                }
                continue;
            }
            if (section == Section.NAVIGATION && line.startsWith("- ")) {
                transitions.add(parseTransition(line.substring(2).strip(), screens, transitionCounter++));
            }
        }
        if (pending != null) {
            putScreen(screens, pending.toScreen());
        }
    }

    private static ScreenTransition parseTransition(
            String text,
            Map<String, ScreenNode> screens,
            int counter
    ) throws MarkdownModelParsingException {
        int arrow = text.indexOf("->");
        if (arrow < 0) {
            throw new MarkdownModelParsingException("Navegación sin flecha '->': " + text);
        }
        String rawSource = text.substring(0, arrow).strip();
        String right = text.substring(arrow + 2).strip();
        int colon = right.indexOf(':');
        String rawTarget = colon >= 0 ? right.substring(0, colon).strip() : right;
        String trigger = colon >= 0 ? right.substring(colon + 1).strip() : "";
        String source = resolveScreenId(rawSource, screens);
        String target = resolveScreenId(rawTarget, screens);
        return new ScreenTransition("nav_" + counter, source, target, ScreenTransitionKind.NAVIGATES, trigger, "", "");
    }

    private static String resolveScreenId(String raw, Map<String, ScreenNode> screens) throws MarkdownModelParsingException {
        String stable = MarkdownTextUtils.toStableId(raw);
        if (screens.containsKey(stable)) {
            return stable;
        }
        for (ScreenNode screen : screens.values()) {
            if (screen.displayName().equalsIgnoreCase(raw.strip())) {
                return screen.id();
            }
        }
        throw new MarkdownModelParsingException("La navegación referencia una pantalla inexistente: " + raw);
    }

    private static ScreenKind parseScreenKind(String value) {
        String normalized = MarkdownTextUtils.toStableId(value);
        for (ScreenKind kind : ScreenKind.values()) {
            if (MarkdownTextUtils.toStableId(kind.name()).equals(normalized)
                    || MarkdownTextUtils.toStableId(kind.displayName()).equals(normalized)) {
                return kind;
            }
        }
        return ScreenKind.OTHER;
    }

    private static void putScreen(Map<String, ScreenNode> screens, ScreenNode screen) throws MarkdownModelParsingException {
        if (screens.containsKey(screen.id())) {
            throw new MarkdownModelParsingException("Pantalla duplicada en Markdown: " + screen.id());
        }
        screens.put(screen.id(), screen);
    }


    private static String stableProjectId(String title) {
        return "flujo_pantallas_" + MarkdownTextUtils.toStableId(title);
    }

    private static String normalizeKey(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }


    private enum Section {
        NONE,
        SCREENS,
        NAVIGATION
    }

    private static final class PendingScreen {
        private final String title;
        private String id;
        private ScreenKind kind = ScreenKind.OTHER;
        private String moduleName = "";
        private String route = "";
        private String purpose = "";
        private String notes = "";

        private PendingScreen(String title) {
            this.title = title == null || title.isBlank() ? "Pantalla" : title.strip();
            this.id = MarkdownTextUtils.toStableId(this.title);
        }

        private ScreenNode toScreen() {
            return new ScreenNode(id, title, kind, moduleName, route, purpose, notes);
        }
    }
}
