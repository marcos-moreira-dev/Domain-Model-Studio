package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.List;

/** Tipos oficiales de modelado de datos. */
public final class DataModelDiagramTypeDefinitions {

    private DataModelDiagramTypeDefinitions() {
    }

    public static List<DiagramTypeOfficialDefinition> all() {
        return List.of(
                DiagramTypeDefinitionFactory.available(DiagramTypeId.CONCEPTUAL_MODEL, "Modelo conceptual", DiagramCategoryId.DATA_MODELING,
                        DiagramWorkspaceKind.CONCEPTUAL_CANVAS, DiagramCapabilityProfiles.conceptual(),
                        "Representa entidades, atributos, relaciones y cardinalidades del dominio.",
                        "modelo-conceptual", "modelo-conceptual-gramatica", "conceptual-model",
                        "conceptual_model_colegio_minimo_importable.md", "uens-conceptual-escolar",
                        "UENS — modelo conceptual escolar", "conceptual_model_uens_gordito_importable.md",
                        "Entidades, atributos y relaciones centrales de estudiantes, representantes, secciones, clases, calificaciones, reportes y auditoría.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.DATA_DICTIONARY, "Diccionario de datos", DiagramCategoryId.DATA_MODELING,
                        DiagramWorkspaceKind.DATA_DICTIONARY_DOCUMENT, DiagramCapabilityProfiles.document(),
                        "Documenta entidades, campos, tipos, reglas, validaciones y observaciones.",
                        "diccionario-datos", "diccionario-datos-gramatica", "data-dictionary",
                        "data_dictionary_colegio_minimo.md", "uens-diccionario-datos",
                        "UENS — diccionario de datos escolar", "data_dictionary_uens_gordito.md",
                        "Diccionario editable de campos, reglas, responsables y observaciones del dominio escolar.", true)
        );
    }
}
