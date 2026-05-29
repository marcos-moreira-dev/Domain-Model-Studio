package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.java;

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
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/** Parser Java mínimo basado en el AST del JDK para alimentar el modelo neutral de importación UML. */
public final class JavaSourceCodeParserAdapter implements SourceCodeParserPort {

    @Override
    public boolean supports(SourceLanguage language, SourceLanguageVersion version) {
        SourceLanguage normalized = language == null ? SourceLanguage.UNKNOWN : language;
        return normalized == SourceLanguage.JAVA;
    }

    @Override
    public ParsedCodeProject parse(SourceCodeParseRequest request) {
        Map<String, ParsedCodeModule> modules = new LinkedHashMap<>();
        List<ParsedCodeType> types = new ArrayList<>();
        List<ParsedCodeRelation> relations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            warnings.add("No se encontró el compilador Java del JDK; no se puede parsear código Java.");
            return emptyProject(request, warnings);
        }

        List<SourceFileCandidate> javaFiles = request.files().stream()
                .filter(file -> file.language() == SourceLanguage.JAVA)
                .toList();
        if (javaFiles.isEmpty()) {
            return emptyProject(request, warnings);
        }
        parseFilesInBatch(request, compiler, javaFiles, modules, types, relations, warnings);
        return new ParsedCodeProject(request.sourceRoot().displayName(),
                List.of(ParsedCodeSourceRoot.from(request.sourceRoot())),
                List.copyOf(modules.values()), types, relations, warnings);
    }

    private static ParsedCodeProject emptyProject(SourceCodeParseRequest request, List<String> warnings) {
        return new ParsedCodeProject(request.sourceRoot().displayName(),
                List.of(ParsedCodeSourceRoot.from(request.sourceRoot())), List.of(), List.of(), List.of(), warnings);
    }

    private static void parseFilesInBatch(SourceCodeParseRequest request, JavaCompiler compiler,
                                          List<SourceFileCandidate> javaFiles,
                                          Map<String, ParsedCodeModule> modules,
                                          List<ParsedCodeType> types,
                                          List<ParsedCodeRelation> relations,
                                          List<String> warnings) {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        Map<Path, SourceFileCandidate> candidatesByPath = candidatesByPath(javaFiles);
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.ROOT,
                StandardCharsets.UTF_8)) {
            Iterable<? extends JavaFileObject> sourceObjects = fileManager.getJavaFileObjectsFromPaths(
                    javaFiles.stream().map(SourceFileCandidate::absolutePath).toList());
            JavacTask task = (JavacTask) compiler.getTask(null, fileManager, diagnostics,
                    List.of("-proc:none", "-Xlint:none"), null, sourceObjects);
            int index = 0;
            for (CompilationUnitTree unit : task.parse()) {
                SourceFileCandidate file = candidateFor(unit, candidatesByPath, javaFiles);
                index++;
                request.progressListener().onProgress("Procesando Java " + index + "/" + javaFiles.size()
                        + ": " + displayPath(file, unit));
                JavaAstModelExtractor.Extraction extraction = new JavaAstModelExtractor(
                        request.sourceRoot(), file, unit).extract();
                extraction.modules().forEach(module -> modules.putIfAbsent(module.id(), module));
                types.addAll(extraction.types());
                relations.addAll(extraction.relations());
            }
        } catch (IOException | RuntimeException exception) {
            warnings.add("No se pudo parsear lote Java de " + request.sourceRoot().displayName()
                    + ": " + exception.getMessage());
        }
        appendDiagnostics(diagnostics, warnings);
    }

    private static Map<Path, SourceFileCandidate> candidatesByPath(List<SourceFileCandidate> files) {
        Map<Path, SourceFileCandidate> candidates = new LinkedHashMap<>();
        for (SourceFileCandidate file : files) {
            candidates.put(file.absolutePath().toAbsolutePath().normalize(), file);
        }
        return candidates;
    }

    private static SourceFileCandidate candidateFor(CompilationUnitTree unit,
                                                    Map<Path, SourceFileCandidate> candidatesByPath,
                                                    List<SourceFileCandidate> fallbackFiles) {
        if (unit != null && unit.getSourceFile() != null) {
            try {
                URI uri = unit.getSourceFile().toUri();
                if (uri != null && "file".equalsIgnoreCase(uri.getScheme())) {
                    SourceFileCandidate candidate = candidatesByPath.get(Path.of(uri).toAbsolutePath().normalize());
                    if (candidate != null) {
                        return candidate;
                    }
                }
            } catch (RuntimeException ignored) {
                // Usa fallback por nombre cuando el JavaFileObject no expone una ruta normalizable.
            }
        }
        String sourceName = unit == null || unit.getSourceFile() == null ? "" : unit.getSourceFile().getName();
        return fallbackFiles.stream()
                .filter(file -> sourceName.endsWith(file.absolutePath().getFileName().toString()))
                .findFirst()
                .orElseGet(() -> fallbackFiles.isEmpty()
                        ? new SourceFileCandidate(Path.of("Unknown.java"), Path.of("Unknown.java"), SourceLanguage.JAVA,
                        "unknown-root")
                        : fallbackFiles.get(0));
    }

    private static String displayPath(SourceFileCandidate file, CompilationUnitTree unit) {
        if (file != null) {
            return file.relativePath().toString();
        }
        return unit == null || unit.getSourceFile() == null ? "archivo Java" : unit.getSourceFile().getName();
    }

    private static void appendDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics, List<String> warnings) {
        int emitted = 0;
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR && emitted < 25) {
                warnings.add("Java parser: " + sourceName(diagnostic) + ":" + diagnostic.getLineNumber()
                        + " - " + diagnostic.getMessage(Locale.ROOT));
                emitted++;
            }
        }
        long totalErrors = diagnostics.getDiagnostics().stream()
                .filter(diagnostic -> diagnostic.getKind() == Diagnostic.Kind.ERROR)
                .count();
        if (totalErrors > emitted) {
            warnings.add("Java parser: se omitieron " + (totalErrors - emitted)
                    + " diagnósticos adicionales para mantener la vista previa legible.");
        }
    }

    private static String sourceName(Diagnostic<? extends JavaFileObject> diagnostic) {
        JavaFileObject source = diagnostic.getSource();
        return source == null ? "archivo Java" : source.getName();
    }
}
