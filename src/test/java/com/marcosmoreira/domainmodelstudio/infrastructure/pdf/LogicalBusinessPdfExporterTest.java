package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturityLevel;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LogicalBusinessPdfExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void exportsPdfFileWithLogicalBusinessContent() throws Exception {
        LogicalBusinessDocument document = sampleDocument();
        Path destination = tempDir.resolve("levantamiento-logico");

        Path exported = new LogicalBusinessPdfExporter().export(document, destination);

        assertEquals(destination.resolveSibling("levantamiento-logico.pdf").toAbsolutePath().normalize(), exported);
        assertTrue(Files.exists(exported));
        assertTrue(Files.size(exported) > 1_200);
        String rawPdf = Files.readString(exported, StandardCharsets.ISO_8859_1);
        assertTrue(rawPdf.startsWith("%PDF-1.4"));
        assertTrue(rawPdf.contains("Levantamiento logico"));
        assertTrue(rawPdf.contains("Indice y guia de lectura"));
        assertTrue(rawPdf.contains("Guia de codigos y categorias"));
        assertTrue(rawPdf.contains("Regla de negocio"));
        assertTrue(rawPdf.contains("Accion transformadora"));
        assertTrue(rawPdf.contains("Entidad candidata"));
        assertTrue(rawPdf.contains("/Annots [ "));
        assertTrue(rawPdf.contains("/Subtype /Link"));
        assertTrue(rawPdf.contains("/S /GoTo"));
        assertTrue(countOccurrences(rawPdf, "/Subtype /Link") >= 6);
        assertTrue(rawPdf.contains("Sistema de cobros"));
        assertTrue(rawPdf.contains("RN-001"));
        assertTrue(rawPdf.contains("ENT-001"));
        assertTrue(rawPdf.contains("ATR-001"));
        assertTrue(rawPdf.contains("REL-001"));
        assertTrue(rawPdf.contains("PEND-001"));
        assertTrue(rawPdf.contains("1 de"));
        assertTrue(rawPdf.contains("%%EOF"));
    }

    @Test
    void exportsFromDiagramProject() throws Exception {
        DiagramProject project = DiagramProject.blank(
                        "cobros",
                        "Sistema de cobros",
                        DiagramTypeId.LOGICAL_BUSINESS_INTAKE)
                .withLogicalBusinessDocument(sampleDocument());
        Path destination = tempDir.resolve("cobros.pdf");

        Path exported = new LogicalBusinessPdfExporter().export(project, destination);

        assertEquals(destination.toAbsolutePath().normalize(), exported);
        assertTrue(Files.exists(exported));
        assertTrue(Files.readString(exported, StandardCharsets.ISO_8859_1).contains("Sistema de cobros"));
    }

    private static LogicalBusinessDocument sampleDocument() {
        LogicalBusinessItem rule = LogicalBusinessItem.of(
                        "RN-001",
                        LogicalBusinessItemKind.RULE,
                        "Toda cuenta por cobrar conserva origen")
                .withEditableDetails(
                        "Toda cuenta por cobrar conserva origen",
                        LogicalBusinessItemStatus.COMPLETE,
                        "Entrevista de cartera",
                        "La cuenta no puede existir sin una factura o convenio.",
                        "Si existe cuenta por cobrar, debe existir documento origen.",
                        "cuenta.origen != vacio",
                        List.of("ACC-001"));
        LogicalBusinessItem action = LogicalBusinessItem.of(
                        "ACC-001",
                        LogicalBusinessItemKind.ACTION,
                        "Registrar pago recibido")
                .withEditableDetails(
                        "Registrar pago recibido",
                        LogicalBusinessItemStatus.PARTIAL,
                        "Observacion operativa",
                        "Caja registra pagos y reduce el saldo pendiente.",
                        "Cuando se registra un pago, se actualiza la cuenta por cobrar.",
                        "registrar pago; recalcular saldo",
                        List.of("RN-001"));
        LogicalBusinessAttributeCandidate attribute = new LogicalBusinessAttributeCandidate(
                "ATR-001",
                "ENT-001",
                "saldoPendiente",
                "Permite saber cuanto queda por cobrar.",
                "decimal",
                true,
                "montoTotal - pagosAplicados",
                "Un saldo incorrecto afecta cobranza.",
                List.of("RN-001"),
                List.of("RN-001"),
                List.of());
        LogicalBusinessRelationshipCandidate relationship = new LogicalBusinessRelationshipCandidate(
                "REL-001",
                "ENT-001",
                "ENT-002",
                "Cuenta por cobrar - Pago recibido",
                "1 a muchos",
                "Los pagos se aplican a una cuenta por cobrar.",
                List.of("ACC-001"));
        LogicalBusinessEntityCandidate account = new LogicalBusinessEntityCandidate(
                "ENT-001",
                "CuentaPorCobrar",
                LogicalBusinessItemStatus.COMPLETE,
                "La accion ACC-001 necesita actualizar el saldo esperado.",
                List.of(attribute),
                List.of(relationship),
                List.of("ACC-001"),
                List.of("RN-001"),
                List.of(),
                List.of(),
                List.of("ACC-001"),
                List.of(),
                "Riesgo financiero si se modela como pago aislado.");
        LogicalBusinessEntityCandidate payment = new LogicalBusinessEntityCandidate(
                "ENT-002",
                "PagoRecibido",
                LogicalBusinessItemStatus.PARTIAL,
                "La accion ACC-001 registra dinero recibido.",
                List.of(),
                List.of(),
                List.of("ACC-001"),
                List.of(),
                List.of(),
                List.of("ACC-001"),
                List.of(),
                List.of(),
                "");
        LogicalBusinessPendingQuestion question = new LogicalBusinessPendingQuestion(
                "PEND-001",
                "Que pasa si el pago excede el saldo?",
                "RN-001, ACC-001",
                LogicalBusinessQuestionPriority.HIGH,
                LogicalBusinessItemStatus.WITH_DOUBTS);
        LogicalBusinessMaturity maturity = new LogicalBusinessMaturity(
                LogicalBusinessMaturityLevel.CONSISTENT,
                List.of("Reglas y acciones tienen trazas internas."),
                List.of("Resolver PEND-001."),
                List.of("Validar excepciones con caja."));
        return new LogicalBusinessDocument(
                "Sistema de cobros",
                "v0.2",
                LocalDate.of(2026, 6, 9),
                LogicalBusinessDocumentStatus.PARTIALLY_VALIDATED,
                "Entrevistas de cartera",
                List.of(LogicalBusinessSection.of("sec-6", "6. Reglas logicas del negocio")
                        .withItemIds(List.of("RN-001", "ACC-001"))),
                List.of(rule, action),
                List.of(account, payment),
                List.of(question),
                maturity,
                "Revisar saldos historicos antes de cerrar.");
    }

    private static int countOccurrences(String text, String needle) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(needle, index)) >= 0) {
            count++;
            index += needle.length();
        }
        return count;
    }
}
