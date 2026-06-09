package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import org.junit.jupiter.api.Test;

class DataDictionaryMarkdownParserTest {

    @Test
    void importsOfficialDictionaryTableAsEditableDocument() throws Exception {
        DiagramProject project = new DataDictionaryMarkdownParser().parse(markdown(), "diccionario.md");

        var document = project.dataDictionary().orElseThrow();
        assertEquals(DiagramTypeId.DATA_DICTIONARY, project.metadata().diagramTypeId());
        assertEquals(2, document.entityCount());
        assertEquals(4, document.fieldCount());
        var idField = document.entities().get(0).fieldByName("id").orElseThrow();
        assertTrue(idField.constraints().contains(FieldConstraint.PRIMARY_KEY));
        assertEquals(LogicalDataType.INTEGER_NUMBER, idField.logicalType());
        var fkField = document.entities().get(1).fieldByName("estudiante_id").orElseThrow();
        assertTrue(fkField.constraints().contains(FieldConstraint.FOREIGN_KEY));
        assertEquals("estudiante", fkField.foreignKeyReference());
    }

    private static String markdown() {
        return """
                ---
                diagram_type: "data-dictionary"
                name: "Diccionario escolar"
                importable: true
                ---
                # Diccionario de datos

                ## Estudiante
                Propósito: registrar estudiantes.
                Responsable del dato: Secretaría académica.

                | Campo | Tipo esperado | Obligatorio | Regla | Observación |
                |---|---|---:|---|---|
                | id | entero | sí | único | Identificador interno. |
                | nombres | texto | sí | no vacío | Nombres legales. |

                ## Matrícula
                Propósito: registrar matrícula vigente.
                Responsable del dato: Secretaría académica.

                | Campo | Tipo esperado | Obligatorio | Regla | Observación |
                |---|---|---:|---|---|
                | id | entero | sí | único | Identificador interno. |
                | estudiante_id | entero | sí | FK válida | Estudiante matriculado. |
                """;
    }
}
