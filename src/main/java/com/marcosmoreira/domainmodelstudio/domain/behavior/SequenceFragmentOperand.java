package com.marcosmoreira.domainmodelstudio.domain.behavior;

/** Operando de un fragmento combinado UML Secuencia: guarda y rango temporal de mensajes. */
public record SequenceFragmentOperand(
        String guard,
        int startMessageIndex,
        int endMessageIndex
) {
    public SequenceFragmentOperand {
        guard = normalize(guard);
        startMessageIndex = Math.max(0, startMessageIndex);
        endMessageIndex = Math.max(0, endMessageIndex);
        if (startMessageIndex > 0 && endMessageIndex > 0 && endMessageIndex < startMessageIndex) {
            int tmp = startMessageIndex;
            startMessageIndex = endMessageIndex;
            endMessageIndex = tmp;
        }
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

    public String rangeLabel() {
        if (!hasRange()) {
            return "";
        }
        return startMessageIndex == endMessageIndex
                ? Integer.toString(startMessageIndex)
                : startMessageIndex + ".." + endMessageIndex;
    }

    public String canonicalLabel() {
        String guardLabel = guardWithBrackets();
        String range = rangeLabel();
        if (guardLabel.isBlank()) {
            return range;
        }
        return range.isBlank() ? guardLabel : guardLabel + " " + range;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
