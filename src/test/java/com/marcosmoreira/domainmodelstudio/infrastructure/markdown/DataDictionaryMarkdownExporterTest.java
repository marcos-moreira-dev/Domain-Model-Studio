package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DataDictionaryMarkdownExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void exportsProfessionalMarkdownWithOfficialDiagramType() throws Exception {
        DataDictionaryDocument document = sampleDocument();
        Path destination = tempDir.resolve("diccionario-datos.md");

        Path exported = new DataDictionaryMarkdownExporter().export(document, destination);

        assertEquals(destination.toAbsolutePath().normalize(), exported);
        String markdown = Files.readString(exported);
        assertTrue(markdown.contains("diagram_type: \"data-dictionary\""));
        assertTrue(markdown.contains("# Diccionario de datos — Sistema de óptica"));
        assertTrue(markdown.contains("organization:"));
        assertTrue(markdown.contains("## Introducción"));
        assertTrue(markdown.contains("- Entidades documentadas:"));
        assertTrue(markdown.contains("### Cliente"));
        assertTrue(markdown.contains("| Cédula | `cedula` | Identificación"));
        assertTrue(markdown.contains("Regla de negocio"));
    }

    @Test
    void exportedMarkdownRoundTripsWithoutCreatingDocumentSectionsAsEntities() throws Exception {
        DataDictionaryDocument document = sampleDocument();

        String markdown = new DataDictionaryMarkdownExporter().exportToString(document);
        DiagramProject reparsed = new DataDictionaryMarkdownParser().parse(markdown, "exported-diccionario.md");

        DataDictionaryDocument roundTripped = reparsed.dataDictionary().orElseThrow();
        assertEquals(1, roundTripped.entityCount());
        assertEquals("Cliente", roundTripped.entities().getFirst().displayName());
        assertFalse(roundTripped.entities().stream()
                .anyMatch(entity -> entity.displayName().equalsIgnoreCase("Tabla general de entidades")));
        assertEquals("cedula", roundTripped.entities().getFirst().fieldByName("cedula").orElseThrow().technicalName());
        assertTrue(roundTripped.entities().getFirst().fieldByName("cedula").orElseThrow()
                .constraints().contains(FieldConstraint.REQUIRED));
    }

    private static DataDictionaryDocument sampleDocument() {
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
                "levantamiento manual",
                DataDictionaryStatus.DRAFT,
                List.of(cedula),
                "");
        return new DataDictionaryDocument(
                "Sistema de óptica",
                "Cliente interno",
                "Óptica piloto",
                "Marcos Moreira",
                "0.1",
                LocalDate.of(2026, 5, 11),
                DataDictionaryStatus.DRAFT,
                "Documento introductorio para cliente final.",
                "logo-optica.png",
                List.of(cliente),
                "Documento de revisión.");
    }
}
