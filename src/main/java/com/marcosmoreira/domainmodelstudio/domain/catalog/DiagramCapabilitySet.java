package com.marcosmoreira.domainmodelstudio.domain.catalog;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/** Conjunto inmutable de capacidades declaradas para un tipo de diagrama. */
public final class DiagramCapabilitySet {

    private final EnumSet<DiagramCapability> capabilities;

    private DiagramCapabilitySet(EnumSet<DiagramCapability> capabilities) {
        this.capabilities = capabilities.clone();
    }

    public static DiagramCapabilitySet of(DiagramCapability... capabilities) {
        Objects.requireNonNull(capabilities, "capabilities");
        if (capabilities.length == 0) {
            return empty();
        }
        return new DiagramCapabilitySet(EnumSet.copyOf(Arrays.asList(capabilities)));
    }

    public static DiagramCapabilitySet empty() {
        return new DiagramCapabilitySet(EnumSet.noneOf(DiagramCapability.class));
    }

    public boolean has(DiagramCapability capability) {
        return capabilities.contains(Objects.requireNonNull(capability, "capability"));
    }

    public Set<DiagramCapability> asSet() {
        return Collections.unmodifiableSet(capabilities);
    }
}
