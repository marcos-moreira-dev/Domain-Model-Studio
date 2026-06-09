package com.marcosmoreira.domainmodelstudio.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SequenceCombinedFragmentSpecTest {

    @Test
    void parsesCombinedFragmentKindGuardRangeAndOperands() {
        BehaviorNode fragment = new BehaviorNode(
                "loop-estudiantes",
                BehaviorNodeKind.FRAGMENT,
                "loop | titulo: Por cada estudiante | guarda: [estudiante != null] | rango: 2..6 | operandos: [siguiente] 2..6",
                "",
                "",
                "",
                1);

        SequenceCombinedFragmentSpec spec = SequenceCombinedFragmentSpec.fromNode(fragment);

        assertEquals(SequenceFragmentKind.LOOP, spec.kind());
        assertEquals("Por cada estudiante", spec.title());
        assertEquals("estudiante != null", spec.guard());
        assertEquals(2, spec.startMessageIndex());
        assertEquals(6, spec.endMessageIndex());
        assertEquals(1, spec.operands().size());
        assertTrue(spec.canonicalMarkdownLine().contains("fragmento: loop"));
    }

    @Test
    void supportsPremiumSequenceOperators() {
        assertEquals(SequenceFragmentKind.PAR, SequenceFragmentKind.fromKeyword("par"));
        assertEquals(SequenceFragmentKind.BREAK, SequenceFragmentKind.fromKeyword("break"));
        assertEquals(SequenceFragmentKind.CRITICAL, SequenceFragmentKind.fromKeyword("crítico"));
        assertEquals(SequenceFragmentKind.REF, SequenceFragmentKind.fromKeyword("referencia"));
    }
}
