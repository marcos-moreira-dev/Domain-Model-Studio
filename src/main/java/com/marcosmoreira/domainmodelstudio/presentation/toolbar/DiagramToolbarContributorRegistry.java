package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Objects;

/** Registro pequeño que evita que el proveedor central conozca todos los botones de cada diagrama. */
final class DiagramToolbarContributorRegistry {

    private final List<DiagramToolbarContributor> contributors;

    DiagramToolbarContributorRegistry(List<DiagramToolbarContributor> contributors) {
        this.contributors = List.copyOf(Objects.requireNonNull(contributors, "contributors"));
    }

    static DiagramToolbarContributorRegistry createDefault() {
        return new DiagramToolbarContributorRegistry(List.of(
                new ConceptualToolbarContributor(),
                new ConceptualDocumentToolbarContributor(),
                new AdministrativeToolbarContributor(),
                new UmlClassToolbarContributor(),
                new BehaviorToolbarContributor(),
                new ArchitectureToolbarContributor(),
                new FreeGraphToolbarContributor(),
                new LogicalBusinessGraphToolbarContributor(),
                new LogicalBusinessToolbarContributor()
        ));
    }

    List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) {
            return List.of();
        }
        return contributors.stream()
                .filter(contributor -> contributor.supports(diagramTypeId))
                .findFirst()
                .map(contributor -> contributor.actionsFor(diagramTypeId))
                .orElseGet(List::of);
    }
}
