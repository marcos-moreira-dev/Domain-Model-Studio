package com.marcosmoreira.domainmodelstudio.domain.er;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cardinalidad textual de una relación ER.
 *
 * <p>Conserva la expresión conceptual usada por el Markdown y por el diagrama.
 * Acepta cardinalidades clásicas como {@code 1}, {@code 0..1}, {@code 1..M} y
 * también rangos acotados como {@code 0..35}. La representación visual puede
 * simplificar ciertos rangos para una notación concreta, pero el dominio conserva
 * el texto original para no perder intención del modelo.</p>
 */
public record Cardinality(String expression) {

    private static final Pattern SINGLE_PATTERN = Pattern.compile("(?:[1-9][0-9]*|M|N)");
    private static final Pattern RANGE_PATTERN = Pattern.compile("([0-9]+)\\.\\.([1-9][0-9]*|M|N)");

    public Cardinality {
        expression = requireSupportedExpression(expression);
    }

    public static Cardinality of(String expression) {
        return new Cardinality(expression);
    }

    public boolean isOptional() {
        Matcher matcher = RANGE_PATTERN.matcher(expression);
        return matcher.matches() && "0".equals(matcher.group(1));
    }

    public boolean isMany() {
        if ("M".equals(expression) || "N".equals(expression)) {
            return true;
        }
        Matcher matcher = RANGE_PATTERN.matcher(expression);
        if (!matcher.matches()) {
            return false;
        }
        String upperBound = matcher.group(2);
        if ("M".equals(upperBound) || "N".equals(upperBound)) {
            return true;
        }
        return Integer.parseInt(upperBound) > 1;
    }

    public String displayText() {
        return expression;
    }

    public static boolean isSupported(String expression) {
        if (expression == null) {
            return false;
        }
        return isSupportedNormalized(normalize(expression));
    }

    private static String requireSupportedExpression(String rawExpression) {
        String normalized = normalize(Objects.requireNonNull(rawExpression, "La cardinalidad no puede ser null"));
        if (!isSupportedNormalized(normalized)) {
            throw new IllegalArgumentException("Cardinalidad no soportada: " + rawExpression);
        }
        return normalized;
    }

    private static boolean isSupportedNormalized(String normalizedExpression) {
        if (SINGLE_PATTERN.matcher(normalizedExpression).matches()) {
            return true;
        }
        Matcher matcher = RANGE_PATTERN.matcher(normalizedExpression);
        if (!matcher.matches()) {
            return false;
        }
        String upperBound = matcher.group(2);
        if ("M".equals(upperBound) || "N".equals(upperBound)) {
            return true;
        }
        int lower = Integer.parseInt(matcher.group(1));
        int upper = Integer.parseInt(upperBound);
        return lower <= upper;
    }

    private static String normalize(String expression) {
        return expression.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return expression;
    }
}
