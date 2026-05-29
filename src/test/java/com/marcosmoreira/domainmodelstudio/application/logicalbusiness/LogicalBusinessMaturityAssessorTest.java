package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturityLevel;
import java.util.List;
import org.junit.jupiter.api.Test;

class LogicalBusinessMaturityAssessorTest {

    private final LogicalBusinessMaturityAssessor assessor = new LogicalBusinessMaturityAssessor();

    @Test
    void emptyDocumentRemainsInitial() {
        LogicalBusinessMaturity maturity = assessor.assess(LogicalBusinessDocument.blank("Vacío"));

        assertEquals(LogicalBusinessMaturityLevel.INITIAL, maturity.level());
        assertFalse(maturity.usableAsSource());
    }

    @Test
    void blockingIssuesKeepDocumentPartial() {
        LogicalBusinessDocument document = LogicalBusinessDocument.blank("Parcial")
                .withItem(LogicalBusinessItem.of("ACC-001", LogicalBusinessItemKind.ACTION, "Acción sin cierre"));

        LogicalBusinessMaturity maturity = assessor.assess(document);

        assertEquals(LogicalBusinessMaturityLevel.PARTIAL, maturity.level());
        assertFalse(maturity.blockers().isEmpty());
    }

    @Test
    void coherentDocumentBecomesSourceReadyWithoutDerivationLanguage() {
        LogicalBusinessAttributeCandidate attribute = new LogicalBusinessAttributeCandidate("ATR-001", "ENT-001",
                "Nombre", "Se registra durante la acción", "texto", false, "", "Puede afectar reportes.",
                List.of("ACC-001"), List.of(), List.of());
        LogicalBusinessEntityCandidate entity = new LogicalBusinessEntityCandidate("ENT-001", "Estudiante",
                LogicalBusinessItemStatus.DRAFT, "Se gestiona durante la acción del expediente.",
                List.of(attribute), List.of(), List.of("ACC-001"), List.of(), List.of(), List.of("ACC-001"),
                List.of(), List.of(), "Riesgo de duplicidad.");
        LogicalBusinessDocument document = LogicalBusinessDocument.blank("Fuente")
                .withItem(item("RN-001", LogicalBusinessItemKind.RULE, "Regla", "Regla documentada.",
                        "Lectura humana de la regla.", "Contenido verificable."))
                .withItem(item("PRE-001", LogicalBusinessItemKind.PRECONDITION, "Precondición", "Entrada documentada.",
                        "Lectura humana de la precondición.", "Contenido verificable."))
                .withItem(item("POST-001", LogicalBusinessItemKind.POSTCONDITION, "Postcondición", "Cierre documentado.",
                        "Lectura humana de la postcondición.", "Contenido verificable."))
                .withItem(item("INV-001", LogicalBusinessItemKind.INVARIANT, "Invariante", "Verdad protegida.",
                        "Lectura humana de la invariante.", "Riesgo explícito con prueba de control."))
                .withItem(item("ACC-001", LogicalBusinessItemKind.ACTION, "Acción", "Acción documentada.",
                        "Lectura humana de la acción.", "Acción con evidencia, registro y auditoría.")
                        .withEditableDetails("Acción", LogicalBusinessItemStatus.DRAFT, "", "Acción documentada.",
                                "Lectura humana de la acción.", "Acción con evidencia, registro y auditoría.",
                                List.of("PRE-001", "POST-001")))
                .withEntityCandidate(entity);

        LogicalBusinessMaturity maturity = assessor.assess(document);

        assertEquals(LogicalBusinessMaturityLevel.SOURCE_READY, maturity.level());
        assertTrue(maturity.usableAsSource());
        assertTrue(maturity.strengths().contains("Entidades candidatas justificadas por la lógica."));
        assertFalse(String.join(" ", maturity.strengths()).contains("derivadas"));
        assertFalse(String.join(" ", maturity.nextSteps()).contains("Derivar"));
    }

    private LogicalBusinessItem item(
            String id,
            LogicalBusinessItemKind kind,
            String title,
            String description,
            String humanReading,
            String content
    ) {
        return LogicalBusinessItem.of(id, kind, title)
                .withEditableDetails(title, LogicalBusinessItemStatus.DRAFT, "", description, humanReading, content, List.of());
    }
}
