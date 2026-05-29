package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.List;
import java.util.Optional;

/** Registro simple de adaptadores de parsing disponibles para importación desde código fuente. */
public final class SourceCodeParserRegistry {
    private final List<SourceCodeParserPort> parsers;

    public SourceCodeParserRegistry(List<SourceCodeParserPort> parsers) {
        this.parsers = List.copyOf(parsers == null ? List.of() : parsers);
    }

    public Optional<SourceCodeParserPort> parserFor(SourceLanguage language, SourceLanguageVersion version) {
        SourceLanguage normalizedLanguage = language == null ? SourceLanguage.UNKNOWN : language;
        SourceLanguageVersion normalizedVersion = version == null
                ? SourceLanguageVersion.flexible(normalizedLanguage)
                : version;
        return parsers.stream()
                .filter(parser -> parser.supports(normalizedLanguage, normalizedVersion))
                .findFirst();
    }

    public List<SourceCodeParserPort> parsers() {
        return parsers;
    }
}
