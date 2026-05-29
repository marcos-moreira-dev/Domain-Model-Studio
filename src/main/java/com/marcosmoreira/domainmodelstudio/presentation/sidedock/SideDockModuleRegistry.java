package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Registro local de módulos laterales disponibles para un workspace. */
public final class SideDockModuleRegistry {

    private final List<SideDockModule> modules = new ArrayList<>();

    public SideDockModuleRegistry register(SideDockModule module) {
        modules.add(Objects.requireNonNull(module, "module"));
        return this;
    }

    public List<SideDockModule> modulesFor(SideDockContext context) {
        Objects.requireNonNull(context, "context");
        return modules.stream()
                .filter(module -> module.supports(context))
                .toList();
    }
}
