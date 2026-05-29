package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CanvasElementIdCodecTest {

    @Test
    void extractsRawIdsFromKnownPrefixes() {
        CanvasElementIdCodec codec = CanvasElementIdCodec.withPrefixes("node:", "dependency:", "containment:");

        assertEquals("ventas", codec.rawNodeId(" node:ventas "));
        assertEquals("dep-1", codec.rawConnectorId("dependency:dep-1").orElseThrow());
        assertEquals("parent-child", codec.rawConnectorId("containment:parent-child").orElseThrow());
    }

    @Test
    void detectsCanvasIdentifierFamily() {
        CanvasElementIdCodec codec = CanvasElementIdCodec.withPrefixes("node:", "dependency:");

        assertTrue(codec.isNodeId("node:cliente"));
        assertTrue(codec.isConnectorId("dependency:consulta"));
    }
}
