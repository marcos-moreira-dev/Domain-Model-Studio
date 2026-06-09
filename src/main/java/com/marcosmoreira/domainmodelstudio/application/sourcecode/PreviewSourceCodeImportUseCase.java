package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Genera un resumen previo para que el usuario confirme una importación desde código fuente. */
public final class PreviewSourceCodeImportUseCase {

    private final SourceDirectoryScannerPort scanner;

    public PreviewSourceCodeImportUseCase(SourceDirectoryScannerPort scanner) {
        this.scanner = Objects.requireNonNull(scanner, "scanner");
    }

    public SourceCodeImportPreview preview(SourceCodeImportRequest request) {
        SourceScanResult scan = scanner.scan(request);
        List<SourceRootImportPreview> roots = scan.sourceRoots().stream()
                .map(root -> toPreview(root, scan))
                .toList();
        List<String> suggestedViews = suggestViews(roots, scan);
        return new SourceCodeImportPreview(
                request.projectRoot(),
                roots,
                scan.ignoredPaths().size(),
                scan.warnings(),
                suggestedViews);
    }

    private SourceRootImportPreview toPreview(SourceRoot root, SourceScanResult scan) {
        Map<SourceLanguage, Long> counts = new EnumMap<>(SourceLanguage.class);
        for (SourceFileCandidate file : scan.filesForRoot(root.id())) {
            counts.merge(file.language(), 1L, Long::sum);
        }
        return new SourceRootImportPreview(
                root.id(),
                root.displayName(),
                root.path(),
                root.kind(),
                root.languageVersions(),
                counts);
    }

    private List<String> suggestViews(List<SourceRootImportPreview> roots, SourceScanResult scan) {
        List<String> views = new ArrayList<>();
        views.add("Resumen");
        if (roots.stream().anyMatch(root -> root.kind() == SourceRootKind.BACKEND)) {
            views.add("Backend");
        }
        if (roots.stream().anyMatch(root -> root.kind() == SourceRootKind.FRONTEND)) {
            views.add("Frontend");
        }
        if (scan.hasFiles() && roots.size() > 1) {
            views.add("Integración API");
        }
        views.add("Mega vista");
        return List.copyOf(views);
    }
}
