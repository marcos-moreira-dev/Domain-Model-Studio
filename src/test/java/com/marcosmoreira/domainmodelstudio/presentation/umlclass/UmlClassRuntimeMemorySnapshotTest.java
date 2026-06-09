package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UmlClassRuntimeMemorySnapshotTest {

    @Test
    void classifiesComfortableHeapAsLowPressure() {
        UmlClassRuntimeMemorySnapshot snapshot = UmlClassRuntimeMemorySnapshot.fromBytes(
                mib(2048), mib(512), mib(384), mib(4096));

        assertEquals(UmlClassRuntimeMemoryLevel.LOW, snapshot.level());
        assertEquals(mib(128), snapshot.usedHeapBytes());
        assertEquals(mib(1920), snapshot.availableHeapBytes());
        assertTrue(snapshot.shortSummary().contains("JVM Baja"));
    }

    @Test
    void classifiesAlmostExhaustedHeapAsCritical() {
        UmlClassRuntimeMemorySnapshot snapshot = UmlClassRuntimeMemorySnapshot.fromBytes(
                mib(1024), mib(960), mib(16), mib(512));

        assertEquals(UmlClassRuntimeMemoryLevel.CRITICAL, snapshot.level());
        assertTrue(snapshot.warns());
        assertTrue(snapshot.recommendation().contains("vistas pequeñas"));
    }

    @Test
    void formatsMemoryInHumanReadableUnits() {
        assertEquals("64 MiB", UmlClassRuntimeMemorySnapshot.formatBytes(mib(64)));
        assertEquals("1.5 GiB", UmlClassRuntimeMemorySnapshot.formatBytes(mib(1536)));
    }

    private static long mib(long value) {
        return value * 1024L * 1024L;
    }
}
