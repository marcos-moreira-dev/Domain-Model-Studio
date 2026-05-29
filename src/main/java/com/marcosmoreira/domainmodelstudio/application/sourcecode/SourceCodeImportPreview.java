package com.marcosmoreira.domainmodelstudio.application.sourcecode;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Vista previa de una importación de código fuente antes de generar el diagrama UML. */
public record SourceCodeImportPreview(
        Path projectRoot,
        List<SourceRootImportPreview> roots,
        long ignoredPathCount,
        List<String> warnings,
        List<String> suggestedViews
) {
    public SourceCodeImportPreview {
        if (projectRoot == null) {
            throw new IllegalArgumentException("La raíz del proyecto de vista previa no puede ser nula.");
        }
        roots = List.copyOf(roots == null ? List.of() : roots);
        warnings = List.copyOf(warnings == null ? List.of() : warnings);
        suggestedViews = List.copyOf(suggestedViews == null ? List.of() : suggestedViews);
    }

    public long totalFiles() {
        return roots.stream().mapToLong(SourceRootImportPreview::totalFiles).sum();
    }

    public boolean importable() {
        return totalFiles() > 0;
    }

    public Map<SourceLanguage, Long> fileCountByLanguage() {
        Map<SourceLanguage, Long> counts = new EnumMap<>(SourceLanguage.class);
        for (SourceRootImportPreview root : roots) {
            root.fileCountByLanguage().forEach((language, count) -> counts.merge(language, count, Long::sum));
        }
        return Map.copyOf(counts);
    }

    public Set<SourceRootKind> detectedKinds() {
        return roots.stream().map(SourceRootImportPreview::kind).collect(Collectors.toSet());
    }
}
