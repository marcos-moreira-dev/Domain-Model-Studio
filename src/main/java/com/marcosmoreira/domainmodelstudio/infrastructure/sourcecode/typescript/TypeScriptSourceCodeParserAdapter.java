package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeModule;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeRelation;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeSourceRoot;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeParseRequest;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceCodeParserPort;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceFileCandidate;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguage;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.SourceLanguageVersion;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parser TypeScript mínimo y desacoplado para alimentar UML Clases sin reemplazar el parser Markdown. */
public final class TypeScriptSourceCodeParserAdapter implements SourceCodeParserPort {

    @Override
    public boolean supports(SourceLanguage language, SourceLanguageVersion version) {
        SourceLanguage normalized = language == null ? SourceLanguage.UNKNOWN : language;
        return normalized == SourceLanguage.TYPESCRIPT;
    }

    @Override
    public ParsedCodeProject parse(SourceCodeParseRequest request) {
        Map<String, ParsedCodeModule> modules = new LinkedHashMap<>();
        List<ParsedCodeType> types = new ArrayList<>();
        List<ParsedCodeRelation> relations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        for (SourceFileCandidate file : request.files()) {
            if (file.language() == SourceLanguage.TYPESCRIPT) {
                request.progressListener().onProgress("Procesando TypeScript: " + file.relativePath());
                parseFile(request, file, modules, types, relations, warnings);
            }
        }
        return new ParsedCodeProject(request.sourceRoot().displayName(),
                List.of(ParsedCodeSourceRoot.from(request.sourceRoot())), List.copyOf(modules.values()), types, relations, warnings);
    }

    private static void parseFile(SourceCodeParseRequest request, SourceFileCandidate file,
                                  Map<String, ParsedCodeModule> modules, List<ParsedCodeType> types,
                                  List<ParsedCodeRelation> relations, List<String> warnings) {
        try {
            String source = Files.readString(file.absolutePath(), StandardCharsets.UTF_8);
            List<TypeScriptToken> tokens = TypeScriptLexicalSanitizer.tokenize(source);
            TypeScriptModelExtractor.Extraction extraction = new TypeScriptModelExtractor(
                    request.sourceRoot(), file, tokens, source).extract();
            extraction.modules().forEach(module -> modules.putIfAbsent(module.id(), module));
            types.addAll(extraction.types());
            relations.addAll(extraction.relations());
        } catch (IOException | RuntimeException exception) {
            warnings.add("No se pudo parsear " + file.relativePath() + ": " + exception.getMessage());
        }
    }
}
