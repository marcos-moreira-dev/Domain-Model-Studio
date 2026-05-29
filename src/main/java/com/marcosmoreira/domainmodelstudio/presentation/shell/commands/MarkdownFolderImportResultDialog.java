package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.presentation.dialogs.OperationResultDialog;

/** Adaptador específico de importación Markdown sobre el diálogo transversal de resultado operativo. */
final class MarkdownFolderImportResultDialog {

    private static final String SUCCESS_DETAIL = "Todo está en orden. No se detectaron Markdown no catalogados, rechazados o fuera de gramática.";

    private final OperationResultDialog.Request request;

    MarkdownFolderImportResultDialog(String summary, String detail, boolean hasProblemFiles) {
        this.request = new OperationResultDialog.Request(
                "Resultado de importación Markdown",
                header(hasProblemFiles),
                summary,
                status(hasProblemFiles),
                statusText(hasProblemFiles),
                detailLabel(hasProblemFiles),
                detailText(detail, hasProblemFiles),
                copyButtonText(hasProblemFiles),
                "Aceptar");
    }

    void showDeferred() {
        OperationResultDialog.showLater(null, request);
    }

    private static String header(boolean hasProblemFiles) {
        if (hasProblemFiles) {
            return "Importación completada con archivos para revisar";
        }
        return "Importación completada sin problemas";
    }

    private static OperationResultDialog.Status status(boolean hasProblemFiles) {
        return hasProblemFiles ? OperationResultDialog.Status.PROBLEM : OperationResultDialog.Status.SUCCESS;
    }

    private static String statusText(boolean hasProblemFiles) {
        if (hasProblemFiles) {
            return "Se detectaron Markdown no catalogados, rechazados o fuera de gramática.";
        }
        return "Todo está en orden. La importación Markdown se completó sin archivos problemáticos.";
    }

    private static String detailLabel(boolean hasProblemFiles) {
        if (hasProblemFiles) {
            return "Markdown problemáticos para corregir con IA:";
        }
        return "Estado de validación:";
    }

    private static String detailText(String detail, boolean hasProblemFiles) {
        if (hasProblemFiles) {
            return detail;
        }
        return SUCCESS_DETAIL;
    }

    private static String copyButtonText(boolean hasProblemFiles) {
        if (hasProblemFiles) {
            return "Copiar problemas";
        }
        return "Copiar estado";
    }
}
