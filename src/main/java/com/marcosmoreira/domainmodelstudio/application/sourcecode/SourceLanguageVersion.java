package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/**
 * Versión sugerida de un lenguaje de origen.
 *
 * <p>Es una pista para elegir adaptadores, no una dependencia rígida del dominio. Así se evita acoplar
 * la importación a Java 21, TypeScript moderno u otra versión concreta.
 */
public record SourceLanguageVersion(
        SourceLanguage language,
        String versionHint,
        boolean strict
) {
    public SourceLanguageVersion {
        language = language == null ? SourceLanguage.UNKNOWN : language;
        versionHint = normalize(versionHint);
    }

    public static SourceLanguageVersion flexible(SourceLanguage language) {
        return new SourceLanguageVersion(language, "", false);
    }

    public static SourceLanguageVersion hinted(SourceLanguage language, String versionHint) {
        return new SourceLanguageVersion(language, versionHint, false);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
