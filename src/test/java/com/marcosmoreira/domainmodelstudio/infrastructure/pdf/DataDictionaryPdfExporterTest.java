package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DataDictionaryPdfExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void exportsPdfFileWithDictionaryContent() throws Exception {
        DataDictionaryDocument document = sampleDocument();
        Path destination = tempDir.resolve("diccionario-datos.pdf");

        Path exported = new DataDictionaryPdfExporter().export(document, destination);

        assertEquals(destination.toAbsolutePath().normalize(), exported);
        assertTrue(Files.exists(exported));
        assertTrue(Files.size(exported) > 800);
        String rawPdf = Files.readString(exported, StandardCharsets.ISO_8859_1);
        assertTrue(rawPdf.startsWith("%PDF-1.4"));
        assertTrue(rawPdf.contains("Diccionario de datos"));
        assertTrue(rawPdf.contains("Indice del documento"));
        assertTrue(rawPdf.contains("/Annots [ "));
        assertTrue(rawPdf.contains("/S /GoTo"));
        assertTrue(rawPdf.contains("Sistema de óptica"));
        assertTrue(rawPdf.contains("Cliente"));
        assertTrue(rawPdf.contains("Organizaci"));
        assertTrue(rawPdf.contains("cedula"));
        assertTrue(rawPdf.contains("%%EOF"));
    }

    @Test
    void embedsLogoImageWithoutPrintingAbsolutePath() throws Exception {
        Path logo = tempDir.resolve("logo-prueba.png");
        BufferedImage image = new BufferedImage(4, 3, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xF5E6B8);
        ImageIO.write(image, "png", logo.toFile());
        DataDictionaryDocument document = sampleDocumentWithLogo(logo.toString());
        Path destination = tempDir.resolve("diccionario-con-logo.pdf");

        Path exported = new DataDictionaryPdfExporter().export(document, destination);

        String rawPdf = Files.readString(exported, StandardCharsets.ISO_8859_1);
        assertTrue(rawPdf.contains("/Subtype /Image"));
        assertTrue(rawPdf.contains("/XObject"));
        assertTrue(!rawPdf.contains(logo.toString()));
    }


    @Test
    void omitsInvalidLogoDiagnosticsFromFinalPdf() throws Exception {
        DataDictionaryDocument document = sampleDocumentWithLogo(tempDir.resolve("no-existe.png").toString());
        Path destination = tempDir.resolve("diccionario-logo-invalido.pdf");

        Path exported = new DataDictionaryPdfExporter().export(document, destination);

        String rawPdf = Files.readString(exported, StandardCharsets.ISO_8859_1);
        assertTrue(!rawPdf.contains("No se pudo insertar"));
        assertTrue(!rawPdf.contains("Logo opcional"));
        assertTrue(rawPdf.contains("Diccionario de datos"));
    }

    @Test
    void wrapsLongTechnicalTokensInsteadOfKeepingSingleOversizedCellWord() throws Exception {
        SimplePdfDocument pdf = new SimplePdfDocument("Prueba de tablas largas");
        String longToken = "identificador_tecnico_extremadamente_largo_para_probar_particion_de_palabras_en_tablas_pdf";
        pdf.table(List.of("Campo", "Descripción"), List.of(List.of(longToken, "valor de prueba")), 0.22, 0.78);
        Path destination = tempDir.resolve("tabla-larga.pdf");

        pdf.writeTo(destination);

        String rawPdf = Files.readString(destination, StandardCharsets.ISO_8859_1);
        assertTrue(!rawPdf.contains(longToken));
        assertTrue(rawPdf.contains(longToken.substring(0, 12)));
        assertTrue(rawPdf.contains(longToken.substring(longToken.length() - 10)));
    }

    @Test
    void addsPdfExtensionWhenDestinationDoesNotHaveOne() throws Exception {
        DataDictionaryDocument document = sampleDocument();
        Path destination = tempDir.resolve("diccionario-datos");

        Path exported = new DataDictionaryPdfExporter().export(document, destination);

        assertEquals(destination.resolveSibling("diccionario-datos.pdf").toAbsolutePath().normalize(), exported);
        assertTrue(Files.exists(exported));
    }

    @Test
    void exportsFromDiagramProject() throws Exception {
        DiagramProject project = DiagramProject.blank("diccionario", "Diccionario", DiagramTypeId.DATA_DICTIONARY)
                .withDataDictionary(sampleDocument());
        Path destination = tempDir.resolve("diccionario-proyecto.pdf");

        Path exported = new DataDictionaryPdfExporter().export(project, destination);

        assertEquals(destination.toAbsolutePath().normalize(), exported);
        assertTrue(Files.exists(exported));
        assertTrue(Files.readString(exported, StandardCharsets.ISO_8859_1).contains("Diccionario de datos"));
    }

    private static DataDictionaryDocument sampleDocumentWithLogo(String logoReference) {
        DataDictionaryDocument base = sampleDocument();
        return base.withDocumentDetails(base.projectName(), base.clientName(), base.organizationName(), base.author(), base.version(),
                base.status(), base.introduction(), logoReference, base.notes());
    }

    private static DataDictionaryDocument sampleDocument() {
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
                "Permite trazabilidad interna.",
                "Debe generarse automáticamente.",
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
                "levantamiento manual",
                DataDictionaryStatus.DRAFT,
                List.of(id, cedula),
                "");
        return new DataDictionaryDocument(
                "Sistema de óptica",
                "Cliente interno",
                "Óptica piloto",
                "Marcos Moreira",
                "0.1",
                LocalDate.of(2026, 5, 11),
                DataDictionaryStatus.DRAFT,
                "Introducción para entregar el documento al cliente final.",
                "logo-optica.png",
                List.of(cliente),
                "Exportación PDF del diccionario como documento registrado, no PDF universal para diagramas visuales.");
    }
}
