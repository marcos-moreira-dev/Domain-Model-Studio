package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import java.util.Optional;

/** Portapapeles interno de selección visual para copiar/pegar entre tabs abiertas. */
public final class VisualSelectionClipboard {

    private static VisualSelectionTransferPayload currentPayload;

    private VisualSelectionClipboard() {
    }

    public static void copy(VisualSelectionTransferPayload payload) {
        currentPayload = payload;
    }

    public static Optional<VisualSelectionTransferPayload> current() {
        return Optional.ofNullable(currentPayload);
    }

    public static void clear() {
        currentPayload = null;
    }
}
