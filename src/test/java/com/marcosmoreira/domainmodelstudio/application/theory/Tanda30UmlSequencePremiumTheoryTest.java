package com.marcosmoreira.domainmodelstudio.application.theory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 30: UML Secuencia debe explicar teoría premium de fragmentos combinados. */
class Tanda30UmlSequencePremiumTheoryTest {

    @Test
    void umlSequenceGuideShouldExplainAllCombinedFragmentOperators() {
        TheoryTopic topic = sequenceTopic();
        String content = normalizedContent(topic);

        for (String token : List.of(
                "fragmentos combinados",
                "operando",
                "guardas",
                "alt",
                "opt",
                "loop",
                "par",
                "break",
                "critical",
                "ref",
                "anidacion",
                "precondiciones",
                "invariantes",
                "postcondiciones")) {
            assertTrue(content.contains(token), "La guía UML Secuencia no explica: " + token);
        }
    }

    @Test
    void umlSequenceGuideShouldExposePremiumFiguresForFragments() {
        TheoryTopic topic = sequenceTopic();
        DefaultTheoryFigureCatalog figureCatalog = new DefaultTheoryFigureCatalog();
        Set<String> figures = topic.sections().stream()
                .flatMap(section -> section.figures().stream())
                .map(TheoryFigureReference::figureId)
                .collect(Collectors.toSet());

        for (String figureId : List.of(
                "uml-sequence-combined-fragment-parts",
                "uml-sequence-opt-guard",
                "uml-sequence-loop-guard",
                "uml-sequence-par-operands",
                "uml-sequence-critical-region",
                "uml-sequence-ref-interaction",
                "uml-sequence-nested-fragments")) {
            assertTrue(figures.contains(figureId), "La guía no referencia la figura premium: " + figureId);
            assertTrue(figureCatalog.contains(figureId), "El catálogo de figuras no registra: " + figureId);
        }
    }

    @Test
    void umlSequenceGuideShouldStayAsAcademicTheoryNotButtonManual() {
        String content = normalizedContent(sequenceTopic());

        assertTrue(content.contains("no reemplaza uml clases"));
        assertTrue(content.contains("no reemplaza uml actividad") || content.contains("diferencia con uml actividad"));
        assertTrue(content.contains("levantamiento logico"));
        assertTrue(content.contains("casos de prueba"));
    }

    private static TheoryTopic sequenceTopic() {
        return new DefaultTheoryCatalog().findByDiagramType(DiagramTypeId.UML_SEQUENCE).orElseThrow();
    }

    private static String normalizedContent(TheoryTopic topic) {
        return (topic.title() + "\n" + topic.sections().stream()
                .flatMap(section -> java.util.stream.Stream.concat(
                        java.util.stream.Stream.of(section.title()),
                        section.lines().stream()))
                .collect(Collectors.joining("\n")))
                .toLowerCase(java.util.Locale.ROOT)
                .replace('á', 'a')
                .replace('é', 'e')
                .replace('í', 'i')
                .replace('ó', 'o')
                .replace('ú', 'u')
                .replace('ñ', 'n');
    }
}
