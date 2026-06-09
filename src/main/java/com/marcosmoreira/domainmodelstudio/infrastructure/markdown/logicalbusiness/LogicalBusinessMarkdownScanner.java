package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class LogicalBusinessMarkdownScanner {

    private static final Pattern SECTION = Pattern.compile("^##\\s+(.+)$");
    private static final Pattern ITEM_HEADING = Pattern.compile(
            "^#{3,4}\\s+([A-Za-z]+-\\d{3,})\\s*(?:[—-]\\s*(.+))?$", Pattern.CASE_INSENSITIVE);

    ParsedLogicalBusinessMarkdown scan(String markdown) {
        LogicalBusinessMarkdownMetadata metadata = new LogicalBusinessMarkdownMetadataParser().parse(markdown);
        ScanState state = new ScanState(metadata);
        for (String rawLine : safe(markdown).split("\\R")) {
            state.accept(rawLine);
        }
        return state.finish();
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static final class ScanState {
        private final LogicalBusinessMarkdownMetadata metadata;
        private final List<ParsedLogicalBusinessItem> items = new ArrayList<>();
        private final Map<String, SectionBuilder> sections = new LinkedHashMap<>();
        private SectionBuilder currentSection;
        private ItemBuilder currentItem;
        private boolean insideFence;
        private int fallbackSectionCounter;

        private ScanState(LogicalBusinessMarkdownMetadata metadata) {
            this.metadata = metadata;
        }

        private void accept(String rawLine) {
            String line = rawLine.strip();
            if (line.startsWith("```")) {
                insideFence = !insideFence;
                if (currentItem != null) {
                    currentItem.append(rawLine);
                }
                return;
            }
            if (insideFence) {
                if (currentItem != null) {
                    currentItem.append(rawLine);
                }
                return;
            }
            Matcher sectionMatcher = SECTION.matcher(line);
            Matcher itemMatcher = ITEM_HEADING.matcher(line);
            if (sectionMatcher.matches()) {
                flushItem();
                openSection(sectionMatcher.group(1));
                return;
            }
            if (itemMatcher.matches()) {
                flushItem();
                openItem(itemMatcher.group(1).toUpperCase(), itemMatcher.group(2));
                return;
            }
            if (currentItem != null) {
                currentItem.append(rawLine);
            }
            if (currentItem == null && LogicalBusinessMarkdownTables.looksLikeDataRow(line)) {
                addTableItem(line);
            }
        }

        private void openSection(String title) {
            String id = sectionId(title);
            currentSection = sections.computeIfAbsent(id, key -> new SectionBuilder(id, title));
        }

        private void openItem(String id, String title) {
            ensureSection();
            currentItem = new ItemBuilder(id, cleanTitle(title, id), currentSection.id);
            currentSection.addItem(id);
        }

        private void addTableItem(String line) {
            ensureSection();
            if (!currentSection.acceptsTableItems()) {
                return;
            }
            List<String> cells = LogicalBusinessMarkdownTables.cells(line);
            if (cells.isEmpty() || cells.stream().anyMatch(cell -> cell.contains("<"))) {
                return;
            }
            String id = LogicalBusinessMarkdownIds.firstId(cells.get(0)).orElse("");
            if (id.isBlank() || containsItem(id)) {
                return;
            }
            String title = cells.size() > 1 ? cells.get(1) : id;
            items.add(new ParsedLogicalBusinessItem(id, title, currentSection.id, line, cells));
            currentSection.addItem(id);
        }

        private ParsedLogicalBusinessMarkdown finish() {
            flushItem();
            return new ParsedLogicalBusinessMarkdown(metadata, buildSections(), items, "");
        }

        private void flushItem() {
            if (currentItem == null) {
                return;
            }
            if (!containsItem(currentItem.id)) {
                items.add(currentItem.build());
            }
            currentItem = null;
        }

        private boolean containsItem(String id) {
            return items.stream().anyMatch(item -> item.id().equalsIgnoreCase(id));
        }

        private void ensureSection() {
            if (currentSection == null) {
                openSection("Contenido general");
            }
        }

        private List<ParsedLogicalBusinessSection> buildSections() {
            return sections.values().stream().map(SectionBuilder::build).toList();
        }

        private String sectionId(String title) {
            Matcher matcher = Pattern.compile("^(\\d+)").matcher(title.strip());
            if (matcher.find()) {
                return "sec-" + matcher.group(1);
            }
            return "sec-auto-" + (++fallbackSectionCounter);
        }
    }

    private static final class SectionBuilder {
        private final String id;
        private final String title;
        private final List<String> itemIds = new ArrayList<>();

        private SectionBuilder(String id, String title) {
            this.id = id;
            this.title = title.strip();
        }

        private void addItem(String itemId) {
            if (!itemIds.contains(itemId)) {
                itemIds.add(itemId);
            }
        }

        private boolean acceptsTableItems() {
            String normalized = LogicalBusinessMarkdownStatusMapper.stable(title);
            return normalized.contains("pregunta")
                    || normalized.contains("contexto")
                    || normalized.contains("supuesto")
                    || normalized.contains("actor")
                    || normalized.contains("evidencia")
                    || normalized.contains("estado")
                    || normalized.contains("entidad")
                    || normalized.contains("atributo")
                    || normalized.contains("relacion")
                    || normalized.contains("reporte")
                    || normalized.contains("algoritmo")
                    || normalized.contains("calculo")
                    || normalized.contains("riesgo");
        }

        private ParsedLogicalBusinessSection build() {
            return new ParsedLogicalBusinessSection(id, title, "", itemIds);
        }
    }

    private static final class ItemBuilder {
        private final String id;
        private final String title;
        private final String sectionId;
        private final StringBuilder body = new StringBuilder();

        private ItemBuilder(String id, String title, String sectionId) {
            this.id = id;
            this.title = title;
            this.sectionId = sectionId;
        }

        private void append(String rawLine) {
            body.append(rawLine).append('\n');
        }

        private ParsedLogicalBusinessItem build() {
            return new ParsedLogicalBusinessItem(id, title, sectionId, body.toString().strip(), List.of());
        }
    }

    private static String cleanTitle(String title, String fallback) {
        String cleaned = title == null ? "" : title.strip();
        return cleaned.isBlank() ? fallback : cleaned;
    }
}
