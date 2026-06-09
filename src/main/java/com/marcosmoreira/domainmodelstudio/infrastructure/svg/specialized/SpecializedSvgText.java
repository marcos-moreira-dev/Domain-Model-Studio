package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import java.util.Locale;

/** Utilidades pequeñas de texto, escape y formato numérico para SVG. */
final class SpecializedSvgText {

    String format(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    String firstNonBlank(String first, String fallback) {
        String normalized = first == null ? "" : first.strip();
        return normalized.isBlank() ? fallback : normalized;
    }

    String shorten(String value, int maxLength) {
        String normalized = value == null ? "" : value.strip();
        return normalized.length() <= maxLength
                ? normalized
                : normalized.substring(0, Math.max(0, maxLength - 1)) + "…";
    }

    String escape(String value) {
        return value == null ? "" : value.replace("&", "&amp;")
                .replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    String escapeAttr(String value) {
        return escape(value).replace("'", "&apos;");
    }
}
