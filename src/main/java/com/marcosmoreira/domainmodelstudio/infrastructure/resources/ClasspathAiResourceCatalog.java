package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceCatalog;
import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Objects;

/**
 * Catálogo de recursos IA basado en descriptores conocidos por la composición.
 *
 * <p>Expone recursos exportables y filtros por tipo de diagrama sin leer todavía el
 * contenido del classpath. La verdad de importabilidad vive en el descriptor, no en el
 * texto del recurso.</p>
 */
public final class ClasspathAiResourceCatalog implements AiResourceCatalog {

    private final List<AiResourceDescriptor> resources;

    public ClasspathAiResourceCatalog(List<AiResourceDescriptor> resources) {
        this.resources = List.copyOf(Objects.requireNonNull(resources, "resources"));
    }

    @Override
    public List<AiResourceDescriptor> findAllExportable() {
        return resources.stream().filter(AiResourceDescriptor::exportable).toList();
    }

    @Override
    public List<AiResourceDescriptor> findByDiagramType(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        return resources.stream()
                .filter(resource -> resource.diagramTypeId().equals(diagramTypeId))
                .toList();
    }
}
