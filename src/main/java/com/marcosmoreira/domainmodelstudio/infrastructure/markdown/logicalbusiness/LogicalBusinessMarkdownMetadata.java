package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import java.time.LocalDate;

record LogicalBusinessMarkdownMetadata(
        String businessName,
        String projectName,
        String version,
        LocalDate documentDate,
        LogicalBusinessDocumentStatus status,
        String mainSource
) {
    LogicalBusinessMarkdownMetadata {
        businessName = clean(businessName);
        projectName = clean(projectName);
        version = clean(version).isBlank() ? "v0.1" : clean(version);
        documentDate = documentDate == null ? LocalDate.now() : documentDate;
        status = status == null ? LogicalBusinessDocumentStatus.DRAFT : status;
        mainSource = clean(mainSource);
    }

    static LogicalBusinessMarkdownMetadata empty() {
        return new LogicalBusinessMarkdownMetadata("", "Levantamiento lógico", "v0.1", LocalDate.now(),
                LogicalBusinessDocumentStatus.DRAFT, "");
    }

    String effectiveProjectName() {
        if (!projectName.isBlank()) {
            return projectName;
        }
        if (!businessName.isBlank()) {
            return "Levantamiento lógico — " + businessName;
        }
        return "Levantamiento lógico";
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
