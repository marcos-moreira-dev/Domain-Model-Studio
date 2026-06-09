package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.util.List;

/** Sección navegable del expediente lógico, por ejemplo reglas, invariantes o entidades candidatas. */
public record LogicalBusinessSection(
        String id,
        String title,
        String purpose,
        LogicalBusinessItemStatus status,
        List<String> itemIds,
        String notes
) {
    public LogicalBusinessSection {
        id = LogicalBusinessText.require(id, "id");
        title = LogicalBusinessText.require(title, "title");
        purpose = LogicalBusinessText.normalize(purpose);
        status = status == null ? LogicalBusinessItemStatus.DRAFT : status;
        itemIds = LogicalBusinessText.normalizedList(itemIds);
        notes = LogicalBusinessText.normalize(notes);
    }

    public static LogicalBusinessSection of(String id, String title) {
        return new LogicalBusinessSection(id, title, "", LogicalBusinessItemStatus.DRAFT, List.of(), "");
    }

    public LogicalBusinessSection withDetails(
            String updatedTitle,
            String updatedPurpose,
            LogicalBusinessItemStatus updatedStatus,
            String updatedNotes
    ) {
        return new LogicalBusinessSection(id, updatedTitle, updatedPurpose, updatedStatus, itemIds, updatedNotes);
    }

    public LogicalBusinessSection withItemIds(List<String> updatedItemIds) {
        return new LogicalBusinessSection(id, title, purpose, status, updatedItemIds, notes);
    }
}
