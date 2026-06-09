package com.marcosmoreira.domainmodelstudio.domain.behavior;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * Operadores UML de fragmentos combinados para diagramas de secuencia.
 *
 * <p>El dominio mantiene estos valores separados del texto visible para que
 * validación, layout, exportación y ayuda no dependan de buscar palabras en una
 * etiqueta dibujada.</p>
 */
public enum SequenceFragmentKind {
    ALT("alt", "Alternativa"),
    OPT("opt", "Opcional"),
    LOOP("loop", "Ciclo"),
    PAR("par", "Paralelo"),
    BREAK("break", "Interrupción"),
    CRITICAL("critical", "Región crítica"),
    REF("ref", "Referencia"),
    UNKNOWN("fragment", "Fragmento");

    private final String keyword;
    private final String displayName;

    SequenceFragmentKind(String keyword, String displayName) {
        this.keyword = keyword;
        this.displayName = displayName;
    }

    public String keyword() { return keyword; }
    public String displayName() { return displayName; }
    public boolean known() { return this != UNKNOWN; }

    public static SequenceFragmentKind fromKeyword(String value) {
        return fromKeywordOptional(value).orElse(UNKNOWN);
    }

    public static Optional<SequenceFragmentKind> fromKeywordOptional(String value) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(kind -> kind != UNKNOWN)
                .filter(kind -> normalized.equals(kind.keyword)
                        || normalized.startsWith(kind.keyword + " ")
                        || normalized.startsWith(kind.keyword + ":")
                        || normalized.startsWith(kind.keyword + "|")
                        || normalized.contains(" " + kind.keyword + " ")
                        || normalized.contains(" " + kind.keyword + ":")
                        || normalized.contains(" " + kind.keyword + "|")
                        || aliasMatches(kind, normalized))
                .findFirst();
    }

    public static String supportedKeywords() {
        return "alt, opt, loop, par, break, critical o ref";
    }

    private static boolean aliasMatches(SequenceFragmentKind kind, String normalized) {
        return switch (kind) {
            case CRITICAL -> normalized.contains("critico") || normalized.contains("region critica") || normalized.contains("seccion critica");
            case BREAK -> normalized.contains("interrupcion") || normalized.contains("romper") || normalized.contains("corte");
            case REF -> normalized.contains("referencia") || normalized.contains("interaccion referenciada");
            case PAR -> normalized.contains("paralelo") || normalized.contains("concurrente");
            default -> false;
        };
    }

    private static String normalize(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .strip();
    }
}
