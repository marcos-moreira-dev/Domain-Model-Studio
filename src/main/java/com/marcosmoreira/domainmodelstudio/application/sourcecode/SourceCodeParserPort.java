package com.marcosmoreira.domainmodelstudio.application.sourcecode;

/** Puerto que desacopla el parser concreto de Java, TypeScript u otro lenguaje futuro. */
public interface SourceCodeParserPort {
    boolean supports(SourceLanguage language, SourceLanguageVersion version);

    ParsedCodeProject parse(SourceCodeParseRequest request);
}
