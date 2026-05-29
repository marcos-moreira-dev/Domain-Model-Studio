package com.marcosmoreira.domainmodelstudio.application.examples;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeRegistry;
import java.util.List;

/** Catálogo oficial de ejemplos Markdown empaquetados en recursos de la aplicación. */
public final class DefaultOfficialExampleCatalog implements OfficialExampleCatalog {

    private static final String UENS_DOMAIN_NOTE = "UENS es una unidad educativa usada como dominio escolar oficial de ejemplo. ";

    private final List<OfficialExampleDescriptor> examples;

    public DefaultOfficialExampleCatalog() {
        this.examples = buildExamples();
    }

    public DefaultOfficialExampleCatalog(DiagramTypeRegistry ignoredRegistry) {
        this();
    }

    @Override
    public List<OfficialExampleDescriptor> findAll() {
        return examples;
    }

    private static List<OfficialExampleDescriptor> buildExamples() {
        return DefaultDiagramTypeDefinitions.all().stream()
                .filter(DiagramTypeOfficialDefinition::hasOfficialExample)
                .map(DefaultOfficialExampleCatalog::example)
                .toList();
    }

    private static OfficialExampleDescriptor example(DiagramTypeOfficialDefinition definition) {
        return new OfficialExampleDescriptor(
                definition.officialExampleId(),
                definition.officialExampleTitle(),
                definition.id(),
                definition.displayName(),
                definition.officialExampleResource(),
                UENS_DOMAIN_NOTE + definition.officialExampleSummary(),
                definition.officialExampleImportable());
    }
}
