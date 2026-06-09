package com.marcosmoreira.domainmodelstudio.domain.datadictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DataDictionaryDocumentTest {

    @Test
    void blankDocumentKeepsProjectNameAndStartsEmpty() {
        DataDictionaryDocument document = DataDictionaryDocument.blank("Sistema de óptica", LocalDate.of(2026, 5, 11));

        assertEquals("Sistema de óptica", document.projectName());
        assertEquals("borrador", document.version());
        assertEquals(DataDictionaryStatus.DRAFT, document.status());
        assertEquals("", document.introduction());
        assertEquals("", document.logoReference());
        assertTrue(document.isEmpty());
        assertEquals(0, document.fieldCount());
    }


    @Test
    void documentDetailsCanBeUpdatedWithoutChangingEntities() {
        DataDictionaryDocument document = DataDictionaryDocument.blank("Sistema", LocalDate.of(2026, 5, 11))
                .withDocumentDetails("Sistema", "Cliente", "Empresa", "Autor", "1.0",
                        DataDictionaryStatus.REVIEWED, "Introducción del documento.",
                        "logo.png", "Notas finales.");

        assertEquals("Empresa", document.organizationName());
        assertEquals("Introducción del documento.", document.introduction());
        assertEquals("logo.png", document.logoReference());
        assertEquals(DataDictionaryStatus.REVIEWED, document.status());
    }

    @Test
    void countsFieldsAcrossEntities() {
        DataDictionaryField id = new DataDictionaryField(
                "cliente_id",
                "ID del cliente",
                "cliente_id",
                LogicalDataType.IDENTIFIER,
                "UUID",
                Set.of(FieldConstraint.PRIMARY_KEY, FieldConstraint.REQUIRED),
                "",
                "",
                "",
                "Identificador interno del cliente.",
                "",
                "",
                "6b7...",
                Set.of(FieldVisibility.TABLE, FieldVisibility.REPORT),
                false,
                "");
        DataDictionaryField cedula = new DataDictionaryField(
                "cedula",
                "Cédula",
                "cedula",
                LogicalDataType.IDENTIFICATION,
                "VARCHAR(10)",
                Set.of(FieldConstraint.REQUIRED, FieldConstraint.UNIQUE, FieldConstraint.VISIBLE_IN_FORM),
                "",
                "",
                "10 dígitos",
                "Documento de identidad del cliente.",
                "Identifica al cliente en operación.",
                "Debe cumplir formato ecuatoriano.",
                "0912345678",
                Set.of(FieldVisibility.FORM, FieldVisibility.TABLE),
                true,
                "");
        DataDictionaryEntity cliente = new DataDictionaryEntity(
                "cliente",
                "Cliente",
                "cliente",
                "Persona atendida por el negocio.",
                "Clientes",
                DataEntityKind.MAIN,
                "modelo conceptual",
                DataDictionaryStatus.DRAFT,
                List.of(id, cedula),
                "");

        DataDictionaryDocument document = DataDictionaryDocument
                .blank("Sistema de óptica", LocalDate.of(2026, 5, 11))
                .withEntity(cliente);

        assertEquals(1, document.entityCount());
        assertEquals(2, document.fieldCount());
        assertTrue(cliente.hasPrimaryKey());
        assertTrue(cedula.isRequired());
        assertTrue(cedula.isVisibleIn(FieldVisibility.FORM));
    }

    @Test
    void rejectsDuplicateEntityTechnicalNames() {
        DataDictionaryEntity first = new DataDictionaryEntity("cliente", "Cliente", List.of());
        DataDictionaryEntity second = new DataDictionaryEntity(
                "customer",
                "Customer",
                "cliente",
                "",
                "",
                DataEntityKind.MAIN,
                "levantamiento manual",
                DataDictionaryStatus.DRAFT,
                List.of(),
                "");

        assertThrows(IllegalArgumentException.class, () -> new DataDictionaryDocument(
                "Sistema",
                "",
                "",
                "borrador",
                LocalDate.of(2026, 5, 11),
                DataDictionaryStatus.DRAFT,
                List.of(first, second),
                ""));
    }

    @Test
    void foreignKeyConstraintRequiresReference() {
        assertThrows(IllegalArgumentException.class, () -> new DataDictionaryField(
                "cliente_id",
                "Cliente",
                "cliente_id",
                LogicalDataType.REFERENCE,
                "UUID",
                Set.of(FieldConstraint.FOREIGN_KEY),
                "",
                "",
                "",
                "Referencia al cliente.",
                "",
                "",
                "",
                Set.of(FieldVisibility.TABLE),
                false,
                ""));
    }
}
