package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;

/** Perfiles de capacidades reutilizables por los tipos oficiales de proyecto. */
public final class DiagramCapabilityProfiles {

    private DiagramCapabilityProfiles() {
    }

    public static DiagramCapabilitySet planning() {
        return DiagramCapabilitySet.of(DiagramCapability.PLANNING_VIEW);
    }

    public static DiagramCapabilitySet logicalBusinessDocument() {
        return DiagramCapabilitySet.of(
                DiagramCapability.CREATE_PROJECT,
                DiagramCapability.IMPORT_MARKDOWN,
                DiagramCapability.SHOW_DOCUMENT_OUTPUT,
                DiagramCapability.MANUAL_EDITING,
                DiagramCapability.EXPORT_MARKDOWN,
                DiagramCapability.SAVE_DMS,
                DiagramCapability.LOAD_DMS,
                DiagramCapability.AI_RESOURCES,
                DiagramCapability.THEORY_HELP);
    }

    public static DiagramCapabilitySet logicalBusinessGraphVisual() {
        return visual();
    }

    public static DiagramCapabilitySet freeGraph() {
        return visual();
    }

    public static DiagramCapabilitySet conceptual() {
        return DiagramCapabilitySet.of(
                DiagramCapability.CREATE_PROJECT,
                DiagramCapability.IMPORT_MARKDOWN,
                DiagramCapability.SHOW_VISUAL_OUTPUT,
                DiagramCapability.MANUAL_EDITING,
                DiagramCapability.EXPORT_PNG,
                DiagramCapability.EXPORT_SVG,
                DiagramCapability.EXPORT_MARKDOWN,
                DiagramCapability.SAVE_DMS,
                DiagramCapability.LOAD_DMS,
                DiagramCapability.AI_RESOURCES,
                DiagramCapability.THEORY_HELP,
                DiagramCapability.BATCH_EXPORT);
    }

    public static DiagramCapabilitySet document() {
        return DiagramCapabilitySet.of(
                DiagramCapability.CREATE_PROJECT,
                DiagramCapability.IMPORT_MARKDOWN,
                DiagramCapability.SHOW_DOCUMENT_OUTPUT,
                DiagramCapability.MANUAL_EDITING,
                DiagramCapability.EXPORT_PDF,
                DiagramCapability.EXPORT_MARKDOWN,
                DiagramCapability.SAVE_DMS,
                DiagramCapability.LOAD_DMS,
                DiagramCapability.AI_RESOURCES,
                DiagramCapability.THEORY_HELP);
    }

    public static DiagramCapabilitySet matrix() {
        return DiagramCapabilitySet.of(
                DiagramCapability.CREATE_PROJECT,
                DiagramCapability.IMPORT_MARKDOWN,
                DiagramCapability.SHOW_DOCUMENT_OUTPUT,
                DiagramCapability.MANUAL_EDITING,
                DiagramCapability.EXPORT_PNG,
                DiagramCapability.EXPORT_SVG,
                DiagramCapability.EXPORT_MARKDOWN,
                DiagramCapability.SAVE_DMS,
                DiagramCapability.LOAD_DMS,
                DiagramCapability.AI_RESOURCES,
                DiagramCapability.THEORY_HELP);
    }

    public static DiagramCapabilitySet umlClass() {
        return DiagramCapabilitySet.of(
                DiagramCapability.CREATE_PROJECT,
                DiagramCapability.IMPORT_MARKDOWN,
                DiagramCapability.IMPORT_SOURCE_CODE,
                DiagramCapability.OPEN_SOURCE_CODE,
                DiagramCapability.SHOW_VISUAL_OUTPUT,
                DiagramCapability.MANUAL_EDITING,
                DiagramCapability.EXPORT_PNG,
                DiagramCapability.EXPORT_SVG,
                DiagramCapability.EXPORT_MARKDOWN,
                DiagramCapability.SAVE_DMS,
                DiagramCapability.LOAD_DMS,
                DiagramCapability.AI_RESOURCES,
                DiagramCapability.THEORY_HELP);
    }

    public static DiagramCapabilitySet visual() {
        return DiagramCapabilitySet.of(
                DiagramCapability.CREATE_PROJECT,
                DiagramCapability.IMPORT_MARKDOWN,
                DiagramCapability.SHOW_VISUAL_OUTPUT,
                DiagramCapability.MANUAL_EDITING,
                DiagramCapability.EXPORT_PNG,
                DiagramCapability.EXPORT_SVG,
                DiagramCapability.EXPORT_MARKDOWN,
                DiagramCapability.SAVE_DMS,
                DiagramCapability.LOAD_DMS,
                DiagramCapability.AI_RESOURCES,
                DiagramCapability.THEORY_HELP);
    }
}
