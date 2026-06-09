package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class SideDockStatePolicyTest {

    private final SideDockStatePolicy policy = new SideDockStatePolicy();

    @Test
    void keepsPreviousModuleWhenStillAvailable() {
        assertEquals(
                SideDockModuleId.PROPERTIES,
                policy.nextActiveModule(
                        SideDockModuleId.PROPERTIES,
                        List.of(SideDockModuleId.STRUCTURE, SideDockModuleId.PROPERTIES)
                ).orElseThrow()
        );
    }

    @Test
    void fallsBackToStructureForGraphWorkspaces() {
        assertEquals(
                SideDockModuleId.STRUCTURE,
                policy.nextActiveModule(
                        SideDockModuleId.MATRIX,
                        List.of(SideDockModuleId.STRUCTURE, SideDockModuleId.PROPERTIES, SideDockModuleId.HELP)
                ).orElseThrow()
        );
    }

    @Test
    void fallsBackToSectionsForDocumentWorkspaces() {
        assertEquals(
                SideDockModuleId.SECTIONS,
                policy.nextActiveModule(
                        SideDockModuleId.STRUCTURE,
                        List.of(SideDockModuleId.SECTIONS, SideDockModuleId.PROPERTIES, SideDockModuleId.HELP)
                ).orElseThrow()
        );
    }

    @Test
    void returnsEmptyWhenThereAreNoModules() {
        assertTrue(policy.nextActiveModule(SideDockModuleId.PROPERTIES, List.of()).isEmpty());
    }
}
