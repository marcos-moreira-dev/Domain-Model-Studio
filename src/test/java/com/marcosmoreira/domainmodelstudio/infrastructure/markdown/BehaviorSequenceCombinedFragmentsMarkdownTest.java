package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceCombinedFragmentSpec;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceFragmentKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import org.junit.jupiter.api.Test;

class BehaviorSequenceCombinedFragmentsMarkdownTest {

    @Test
    void importsAndExportsCombinedFragmentsWithGuardsAndOperands() throws Exception {
        String markdown = """
                ---
                diagram_type: "uml-sequence"
                name: "Secuencia"
                ---
                # Participantes

                - Usuario
                - Servicio

                # Fragmentos combinados

                - fragmento: alt | id: alt-validacion | titulo: Validación | rango: 1..2 | operandos: [válido] 1..1; [inválido] 2..2
                - fragmento: loop | id: loop-reintentos | titulo: Reintentos | guarda: [quedan intentos] | rango: 1..2

                # Mensajes

                1. Usuario -> Servicio: validar
                2. Servicio -> Usuario: respuesta
                """;

        DiagramProject project = new BehaviorMarkdownParser().parse(markdown, "inline.md");
        var document = project.behaviorDiagram().orElseThrow();

        var participants = document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.PARTICIPANT)
                .toList();
        assertEquals(2, participants.size());

        var fragments = document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.FRAGMENT)
                .toList();
        assertEquals(2, fragments.size());
        SequenceCombinedFragmentSpec alt = SequenceCombinedFragmentSpec.fromNode(fragments.get(0));
        assertEquals(SequenceFragmentKind.ALT, alt.kind());
        assertEquals(2, alt.operands().size());

        String exported = new BehaviorMarkdownExporter().export(project);
        assertTrue(exported.contains("# Fragmentos combinados"));
        assertTrue(exported.contains("fragmento: alt"));
        assertTrue(exported.contains("operandos: [válido] 1; [inválido] 2"));
        assertTrue(exported.contains("fragmento: loop"));
    }


    @Test
    void participantSectionDoesNotBecomeParFragment() throws Exception {
        String markdown = """
                ---
                diagram_type: "uml-sequence"
                name: "Secuencia"
                ---
                # Participantes

                - Profesor
                - Pantalla

                # Mensajes

                1. Profesor -> Pantalla: consulta
                """;

        DiagramProject project = new BehaviorMarkdownParser().parse(markdown, "inline.md");
        var document = project.behaviorDiagram().orElseThrow();

        assertEquals(2, document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.PARTICIPANT)
                .count());
        assertEquals(0, document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.FRAGMENT)
                .count());
    }

}
