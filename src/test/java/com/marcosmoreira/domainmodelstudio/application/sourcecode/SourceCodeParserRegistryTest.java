package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class SourceCodeParserRegistryTest {

    @Test
    void registryShouldResolveParserByLanguageThroughPort() {
        SourceCodeParserPort javaParser = new FakeParser(SourceLanguage.JAVA, "java-parser");
        SourceCodeParserRegistry registry = new SourceCodeParserRegistry(List.of(javaParser));

        var found = registry.parserFor(SourceLanguage.JAVA, SourceLanguageVersion.hinted(SourceLanguage.JAVA, "17"));

        assertTrue(found.isPresent());
        assertEquals(javaParser, found.orElseThrow());
    }

    @Test
    void registryShouldLeaveUnsupportedLanguageUnresolved() {
        SourceCodeParserRegistry registry = new SourceCodeParserRegistry(List.of(new FakeParser(SourceLanguage.JAVA, "java")));

        assertTrue(registry.parserFor(SourceLanguage.TYPESCRIPT,
                SourceLanguageVersion.flexible(SourceLanguage.TYPESCRIPT)).isEmpty());
    }

    private record FakeParser(SourceLanguage language, String projectName) implements SourceCodeParserPort {
        @Override
        public boolean supports(SourceLanguage candidate, SourceLanguageVersion version) {
            return candidate == language;
        }

        @Override
        public ParsedCodeProject parse(SourceCodeParseRequest request) {
            ParsedCodeSourceRoot root = new ParsedCodeSourceRoot("root", "Root", Path.of("."),
                    SourceRootKind.UNKNOWN, List.of(SourceLanguageVersion.flexible(language)));
            return new ParsedCodeProject(projectName, List.of(root), List.of(), List.of(), List.of(), List.of());
        }
    }
}
