package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import java.util.Optional;

/** Proveedor de la salida final activa que puede entregarse o exportarse. */
@FunctionalInterface
public interface ActiveOutputProvider {
    Optional<ExportableOutput> activeOutput();
}
