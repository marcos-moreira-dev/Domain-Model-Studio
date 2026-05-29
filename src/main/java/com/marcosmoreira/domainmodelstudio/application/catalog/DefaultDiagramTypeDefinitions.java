package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.application.catalog.definitions.AdministrativeDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.definitions.ArchitectureDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.definitions.BusinessAnalysisDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.definitions.BusinessProcessDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.definitions.DataModelDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.definitions.TechnicalDocumentationDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.definitions.UmlDiagramTypeDefinitions;
import java.util.ArrayList;
import java.util.List;

/**
 * Fuente oficial de definiciones visibles de proyecto.
 *
 * <p>Desde la Tanda 30, el catálogo mantiene una única API pública pero delega la declaración
 * de tipos a familias pequeñas. Así se conserva la cadena de verdad del producto: estado,
 * workspace, capacidades, guía académica, gramática y ejemplos oficiales, sin acumular todos
 * los contratos visibles en un archivo monolítico.</p>
 */
public final class DefaultDiagramTypeDefinitions {

    private DefaultDiagramTypeDefinitions() {
    }

    public static List<DiagramTypeOfficialDefinition> all() {
        List<DiagramTypeOfficialDefinition> definitions = new ArrayList<>();
        definitions.addAll(BusinessAnalysisDiagramTypeDefinitions.all());
        definitions.addAll(DataModelDiagramTypeDefinitions.all());
        definitions.addAll(BusinessProcessDiagramTypeDefinitions.all());
        definitions.addAll(ArchitectureDiagramTypeDefinitions.all());
        definitions.addAll(UmlDiagramTypeDefinitions.all());
        definitions.addAll(AdministrativeDiagramTypeDefinitions.all());
        definitions.addAll(TechnicalDocumentationDiagramTypeDefinitions.all());
        return List.copyOf(definitions);
    }
}
