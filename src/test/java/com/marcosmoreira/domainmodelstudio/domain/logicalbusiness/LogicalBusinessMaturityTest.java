package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogicalBusinessMaturityTest {

    @Test
    void maturityKnowsWhenDocumentIsUsableAsSource() {
        LogicalBusinessMaturity initial = LogicalBusinessMaturity.initial();
        LogicalBusinessMaturity sourceReady = new LogicalBusinessMaturity(
                LogicalBusinessMaturityLevel.SOURCE_READY,
                List.of("Reglas e invariantes conectadas"),
                List.of(),
                List.of("Reutilizar como fuente lógica revisable")
        );

        assertFalse(initial.usableAsSource());
        assertTrue(sourceReady.usableAsSource());
    }

    @Test
    void legacyDerivableAliasIsKeptForCompatibility() {
        LogicalBusinessMaturity legacy = new LogicalBusinessMaturity(
                LogicalBusinessMaturityLevel.DERIVABLE,
                List.of("Contrato histórico importado"),
                List.of(),
                List.of("Revisar lenguaje visible")
        );

        assertTrue(legacy.usableAsSource());
        assertTrue(legacy.derivable());
    }
}
