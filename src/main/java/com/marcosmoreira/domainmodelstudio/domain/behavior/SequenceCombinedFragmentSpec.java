package com.marcosmoreira.domainmodelstudio.domain.behavior;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lectura semántica de un nodo {@link BehaviorNodeKind#FRAGMENT} de UML Secuencia.
 *
 * <p>Por compatibilidad con los archivos .dms existentes, la Tanda 29 conserva el
 * nodo FRAGMENT como ancla visual, pero interpreta su texto como un fragmento
 * combinado UML con operador, guardas, operandos, rango temporal y anidación.</p>
 */
public record SequenceCombinedFragmentSpec(
        SequenceFragmentKind kind,
        String title,
        String guard,
        String reference,
        String parentId,
        int startMessageIndex,
        int endMessageIndex,
        List<SequenceFragmentOperand> operands,
        int nestingLevel
) {
    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+)\\s*(?:\\.\\.|-|a|to)\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern SINGLE_NUMBER_PATTERN = Pattern.compile("\\b(\\d+)\\b");
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\[([^]]+)]");

    public SequenceCombinedFragmentSpec {
        kind = kind == null ? SequenceFragmentKind.UNKNOWN : kind;
        title = clean(title);
        guard = stripGuardBrackets(guard);
        reference = clean(reference);
        parentId = clean(parentId);
        startMessageIndex = Math.max(0, startMessageIndex);
        endMessageIndex = Math.max(0, endMessageIndex);
        if (startMessageIndex > 0 && endMessageIndex > 0 && endMessageIndex < startMessageIndex) {
            int tmp = startMessageIndex;
            startMessageIndex = endMessageIndex;
            endMessageIndex = tmp;
        }
        operands = List.copyOf(operands == null ? List.of() : operands);
        nestingLevel = Math.max(0, Math.min(6, nestingLevel));
    }

    public static SequenceCombinedFragmentSpec fromNode(BehaviorNode node) {
        if (node == null) {
            return empty();
        }
        return fromText(node.displayName(), node.description(), node.notes());
    }

    public static SequenceCombinedFragmentSpec fromText(String title, String description, String notes) {
        String titleText = clean(title);
        String descriptionText = clean(description);
        String notesText = clean(notes);
        String source = join(titleText, descriptionText, notesText);
        Map<String, String> values = keyValues(source);
        SequenceFragmentKind kind = explicitKind(values);
        if (kind == SequenceFragmentKind.UNKNOWN) {
            kind = SequenceFragmentKind.fromKeyword(source);
        }
        String guard = firstNonBlank(values.get("guarda"), values.get("guard"), values.get("condicion"), values.get("condición"));
        if (guard.isBlank()) {
            guard = firstGuardBracket(titleText, descriptionText, notesText);
        }
        String reference = firstNonBlank(values.get("referencia"), values.get("reference"), values.get("ref"));
        String parentId = firstNonBlank(values.get("padre"), values.get("parent"), values.get("parent_id"));
        int[] range = rangeFrom(firstNonBlank(values.get("rango"), values.get("mensajes"), values.get("messages"), values.get("range")));
        if (range[0] == 0 && range[1] == 0) {
            range = rangeFrom(source);
        }
        int nesting = parseInt(firstNonBlank(values.get("nivel"), values.get("nesting"), values.get("anidacion"), values.get("anidación")), 0);
        List<SequenceFragmentOperand> operands = parseOperands(source, values);
        String displayTitle = cleanTitle(titleText, kind);
        if (displayTitle.isBlank()) {
            displayTitle = firstNonBlank(values.get("titulo"), values.get("title"));
        }
        if (displayTitle.isBlank() && kind == SequenceFragmentKind.REF && !reference.isBlank()) {
            displayTitle = reference;
        }
        return new SequenceCombinedFragmentSpec(kind, displayTitle, guard, reference, parentId, range[0], range[1], operands, nesting);
    }

    public static SequenceCombinedFragmentSpec empty() {
        return new SequenceCombinedFragmentSpec(SequenceFragmentKind.UNKNOWN, "", "", "", "", 0, 0, List.of(), 0);
    }

    public boolean hasRange() {
        return startMessageIndex > 0 && endMessageIndex > 0;
    }

    public String guardWithBrackets() {
        if (guard.isBlank()) {
            return "";
        }
        return guard.startsWith("[") && guard.endsWith("]") ? guard : "[" + guard + "]";
    }

    public String displayTitle() {
        return title.isBlank() ? kind.displayName() : title;
    }

    public String rangeLabel() {
        if (!hasRange()) {
            return "";
        }
        return startMessageIndex == endMessageIndex
                ? Integer.toString(startMessageIndex)
                : startMessageIndex + ".." + endMessageIndex;
    }

    public int effectiveStartIndex(int fallbackIndex) {
        return startMessageIndex > 0 ? startMessageIndex : Math.max(1, fallbackIndex);
    }

    public int effectiveEndIndex(int fallbackIndex) {
        int fallback = Math.max(effectiveStartIndex(fallbackIndex), fallbackIndex);
        return endMessageIndex > 0 ? endMessageIndex : fallback;
    }

    public List<String> detailLines() {
        ArrayList<String> details = new ArrayList<>();
        if (!guard.isBlank()) {
            details.add("guarda: " + guardWithBrackets());
        }
        if (hasRange()) {
            details.add("rango: " + rangeLabel());
        }
        if (!reference.isBlank()) {
            details.add("referencia: " + reference);
        }
        if (!parentId.isBlank()) {
            details.add("padre: " + parentId);
        }
        if (!operands.isEmpty()) {
            details.add("operandos: " + operandsLabel());
        }
        return details;
    }

    public String detailText() {
        return String.join(" | ", detailLines());
    }

    public String canonicalMarkdownLine() {
        return canonicalMarkdownLine("");
    }

    public String canonicalMarkdownLine(String id) {
        StringBuilder builder = new StringBuilder("- fragmento: ").append(kind.keyword());
        String cleanId = cleanValue(id);
        if (!cleanId.isBlank()) {
            builder.append(" | id: ").append(cleanId);
        }
        if (!title.isBlank()) {
            builder.append(" | titulo: ").append(title);
        }
        if (!guard.isBlank()) {
            builder.append(" | guarda: ").append(guardWithBrackets());
        }
        if (hasRange()) {
            builder.append(" | rango: ").append(rangeLabel());
        }
        if (!parentId.isBlank()) {
            builder.append(" | padre: ").append(parentId);
        }
        if (!reference.isBlank()) {
            builder.append(" | referencia: ").append(reference);
        }
        if (!operands.isEmpty()) {
            builder.append(" | operandos: ").append(operandsLabel());
        }
        return builder.toString();
    }

    public String operandsLabel() {
        return String.join("; ", operands.stream()
                .map(SequenceFragmentOperand::canonicalLabel)
                .filter(label -> !label.isBlank())
                .toList());
    }

    private static SequenceFragmentKind explicitKind(Map<String, String> values) {
        return SequenceFragmentKind.fromKeyword(firstNonBlank(
                values.get("tipo"), values.get("type"), values.get("kind"), values.get("fragmento"), values.get("fragment")));
    }

    private static List<SequenceFragmentOperand> parseOperands(String source, Map<String, String> values) {
        ArrayList<SequenceFragmentOperand> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = normalizeKey(entry.getKey());
            if (key.equals("operando") || key.equals("operandos") || key.equals("operand") || key.equals("operands")) {
                addOperandTokens(result, entry.getValue());
            }
        }
        for (String line : source.split("\\R")) {
            String normalized = normalizeKey(line);
            if (normalized.startsWith("operando:") || normalized.startsWith("operando=")
                    || normalized.startsWith("operand:") || normalized.startsWith("operand=")) {
                int separator = Math.max(line.indexOf(':'), line.indexOf('='));
                if (separator >= 0) {
                    addOperandTokens(result, line.substring(separator + 1));
                }
            }
        }
        return result;
    }

    private static void addOperandTokens(List<SequenceFragmentOperand> result, String raw) {
        if (raw == null || raw.isBlank()) {
            return;
        }
        for (String token : raw.split(";")) {
            String normalized = clean(token);
            if (normalized.isBlank()) {
                continue;
            }
            String guard = firstBracket(normalized);
            int[] range = rangeFrom(normalized);
            result.add(new SequenceFragmentOperand(guard, range[0], range[1]));
        }
    }

    private static Map<String, String> keyValues(String source) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        for (String rawSegment : source.split("[|\\n]")) {
            String segment = clean(rawSegment);
            int separator = separatorIndex(segment);
            if (separator < 0) {
                continue;
            }
            String key = normalizeKey(segment.substring(0, separator));
            String value = cleanValue(segment.substring(separator + 1));
            if (!key.isBlank() && !value.isBlank()) {
                values.put(key, value);
            }
        }
        return values;
    }

    private static int separatorIndex(String segment) {
        int colon = segment.indexOf(':');
        int equals = segment.indexOf('=');
        if (colon < 0) return equals;
        if (equals < 0) return colon;
        return Math.min(colon, equals);
    }

    private static int[] rangeFrom(String text) {
        String source = clean(text);
        if (source.isBlank()) {
            return new int[]{0, 0};
        }
        Matcher range = RANGE_PATTERN.matcher(source);
        if (range.find()) {
            return new int[]{parseInt(range.group(1), 0), parseInt(range.group(2), 0)};
        }
        Matcher single = SINGLE_NUMBER_PATTERN.matcher(source);
        if (single.find()) {
            int value = parseInt(single.group(1), 0);
            return new int[]{value, value};
        }
        return new int[]{0, 0};
    }

    private static String firstGuardBracket(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            for (String rawSegment : clean(value).split("[|\n]")) {
                String segment = clean(rawSegment);
                if (segment.isBlank() || isOperandMetadata(segment)) {
                    continue;
                }
                String bracket = firstBracket(segment);
                if (!bracket.isBlank()) {
                    return bracket;
                }
            }
        }
        return "";
    }

    private static boolean isOperandMetadata(String segment) {
        int separator = separatorIndex(segment);
        if (separator < 0) {
            return false;
        }
        String key = normalizeKey(segment.substring(0, separator));
        return key.equals("operando") || key.equals("operand")
                || key.equals("operandos") || key.equals("operands");
    }

    private static String firstBracket(String value) {
        Matcher matcher = BRACKET_PATTERN.matcher(value == null ? "" : value);
        return matcher.find() ? matcher.group(1).strip() : "";
    }

    private static String cleanTitle(String rawTitle, SequenceFragmentKind kind) {
        String title = clean(rawTitle);
        int pipe = title.indexOf('|');
        if (pipe >= 0) {
            title = title.substring(0, pipe).strip();
        }
        int separator = separatorIndex(title);
        if (separator >= 0 && isMetadataKey(title.substring(0, separator))) {
            title = title.substring(separator + 1).strip();
        }
        if (kind != null && kind.known()) {
            String keyword = kind.keyword();
            String normalized = normalize(title);
            if (normalized.equals(keyword)) {
                return "";
            }
            if (normalized.startsWith(keyword + " ") || normalized.startsWith(keyword + ":")) {
                title = title.substring(keyword.length()).replaceFirst("^[:\\s]+", "").strip();
            }
        }
        title = title.replaceAll("\\[[^]]+]", "").strip();
        return cleanValue(title);
    }

    private static boolean isMetadataKey(String key) {
        String normalized = normalizeKey(key);
        return normalized.equals("fragmento") || normalized.equals("tipo") || normalized.equals("type") || normalized.equals("kind")
                || normalized.equals("titulo") || normalized.equals("title");
    }

    private static String stripGuardBrackets(String value) {
        String cleaned = cleanValue(value);
        if (cleaned.startsWith("[") && cleaned.endsWith("]") && cleaned.length() >= 2) {
            return cleaned.substring(1, cleaned.length() - 1).strip();
        }
        return cleaned;
    }

    private static String cleanValue(String value) {
        String cleaned = clean(value);
        while ((cleaned.startsWith("\"") && cleaned.endsWith("\""))
                || (cleaned.startsWith("'") && cleaned.endsWith("'"))
                || (cleaned.startsWith("`") && cleaned.endsWith("`"))) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).strip();
        }
        return cleaned;
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            String cleaned = cleanValue(value);
            if (!cleaned.isBlank()) {
                return cleaned;
            }
        }
        return "";
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(clean(value));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static String join(String... values) {
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            String cleaned = clean(value);
            if (!cleaned.isBlank()) {
                if (!builder.isEmpty()) {
                    builder.append('\n');
                }
                builder.append(cleaned);
            }
        }
        return builder.toString();
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip();
    }

    private static String normalizeKey(String value) {
        return normalize(value).replace(' ', '_');
    }

    private static String normalize(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .strip();
    }
}
