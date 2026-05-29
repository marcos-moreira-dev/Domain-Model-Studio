package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Orquesta escaneo, selección de parser por lenguaje y normalización del modelo neutral. */
public final class SourceCodeProjectParserUseCase {
    private final SourceDirectoryScannerPort scanner;
    private final SourceCodeParserRegistry parserRegistry;
    private final ParsedCodeProjectNormalizer normalizer;

    public SourceCodeProjectParserUseCase(SourceDirectoryScannerPort scanner, SourceCodeParserRegistry parserRegistry,
                                          ParsedCodeProjectNormalizer normalizer) {
        if (scanner == null) {
            throw new IllegalArgumentException("El escáner de código fuente no puede ser nulo.");
        }
        if (parserRegistry == null) {
            throw new IllegalArgumentException("El registro de parsers no puede ser nulo.");
        }
        this.scanner = scanner;
        this.parserRegistry = parserRegistry;
        this.normalizer = normalizer == null ? new ParsedCodeProjectNormalizer() : normalizer;
    }

    public ParsedCodeProject parse(SourceCodeImportRequest request) {
        return parse(request, SourceCodeImportProgressListener.NONE);
    }

    public ParsedCodeProject parse(SourceCodeImportRequest request, SourceCodeImportProgressListener progressListener) {
        SourceCodeImportProgressListener progress = progressListener == null ? SourceCodeImportProgressListener.NONE : progressListener;
        progress.onProgress("Escaneando directorio de código fuente...");
        SourceScanResult scan = scanner.scan(request);
        progress.onProgress("Archivos detectados: " + scan.files().size() + ". Preparando parsers...");
        List<ParsedCodeSourceRoot> roots = scan.sourceRoots().stream()
                .map(ParsedCodeSourceRoot::from)
                .toList();
        List<ParsedCodeModule> modules = new ArrayList<>();
        List<ParsedCodeType> types = new ArrayList<>();
        List<ParsedCodeRelation> relations = new ArrayList<>();
        List<String> warnings = new ArrayList<>(scan.warnings());

        for (SourceRoot root : scan.sourceRoots()) {
            parseRoot(scan, root, modules, types, relations, warnings, progress);
        }
        ParsedCodeProject raw = new ParsedCodeProject(projectNameFrom(request), roots, modules, types, relations, warnings);
        progress.onProgress("Normalizando modelo neutral de código fuente...");
        return normalizer.normalize(raw);
    }

    private String projectNameFrom(SourceCodeImportRequest request) {
        return request.projectRoot().getFileName() == null
                ? "Proyecto de código fuente"
                : request.projectRoot().getFileName().toString();
    }

    private void parseRoot(SourceScanResult scan, SourceRoot root, List<ParsedCodeModule> modules,
                           List<ParsedCodeType> types, List<ParsedCodeRelation> relations, List<String> warnings,
                           SourceCodeImportProgressListener progress) {
        Map<SourceLanguage, List<SourceFileCandidate>> filesByLanguage = filesByLanguage(scan.filesForRoot(root.id()));
        for (Map.Entry<SourceLanguage, List<SourceFileCandidate>> entry : filesByLanguage.entrySet()) {
            SourceLanguage language = entry.getKey();
            SourceLanguageVersion version = versionFor(root, language);
            parserRegistry.parserFor(language, version).ifPresentOrElse(parser -> {
                progress.onProgress("Parseando " + entry.getValue().size() + " archivos " + language.displayName()
                        + " en " + root.displayName() + "...");
                ParsedCodeProject partial = parser.parse(new SourceCodeParseRequest(root, version, entry.getValue(), progress));
                modules.addAll(partial.modules());
                types.addAll(partial.types());
                relations.addAll(partial.relations());
                warnings.addAll(partial.warnings());
            }, () -> warnings.add("No hay parser registrado para " + language.displayName()
                    + " en la raíz " + root.displayName() + "."));
        }
    }

    private Map<SourceLanguage, List<SourceFileCandidate>> filesByLanguage(List<SourceFileCandidate> files) {
        Map<SourceLanguage, List<SourceFileCandidate>> grouped = new LinkedHashMap<>();
        for (SourceFileCandidate file : files) {
            if (file.language() == SourceLanguage.UNKNOWN) {
                continue;
            }
            grouped.computeIfAbsent(file.language(), ignored -> new ArrayList<>()).add(file);
        }
        return grouped;
    }

    private SourceLanguageVersion versionFor(SourceRoot root, SourceLanguage language) {
        return root.languageVersions().stream()
                .filter(version -> version.language() == language)
                .findFirst()
                .orElseGet(() -> SourceLanguageVersion.flexible(language));
    }
}
