package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/** Decide qué módulo debe quedar activo cuando cambia el contexto del tab/panel activo. */
public final class SideDockStatePolicy {

    public Optional<SideDockModuleId> nextActiveModule(
            SideDockModuleId previous,
            Collection<SideDockModuleId> available
    ) {
        Objects.requireNonNull(available, "available");
        if (available.isEmpty()) {
            return Optional.empty();
        }
        if (previous != null && available.contains(previous)) {
            return Optional.of(previous);
        }
        if (available.contains(SideDockModuleId.STRUCTURE)) {
            return Optional.of(SideDockModuleId.STRUCTURE);
        }
        if (available.contains(SideDockModuleId.SECTIONS)) {
            return Optional.of(SideDockModuleId.SECTIONS);
        }
        if (available.contains(SideDockModuleId.CONTENTS)) {
            return Optional.of(SideDockModuleId.CONTENTS);
        }
        return available.stream().findFirst();
    }
}
