package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Valida que el catálogo no prometa capacidades que el producto todavía no entrega.
 *
 * <p>Esta regla vive en application porque controla consistencia de catálogo oficial,
 * no teoría del dominio. Evita que un tipo vuelva a quedar como disponible solo por
 * tener clases, datos o una pantalla auxiliar.</p>
 */
final class DiagramCatalogConsistencyValidator {

    private static final Set<DiagramCapability> PRODUCT_OUTPUT_CAPABILITIES = EnumSet.of(
            DiagramCapability.SHOW_VISUAL_OUTPUT,
            DiagramCapability.SHOW_DOCUMENT_OUTPUT);

    private static final Set<DiagramCapability> EXPORT_CAPABILITIES = EnumSet.of(
            DiagramCapability.EXPORT_PNG,
            DiagramCapability.EXPORT_SVG,
            DiagramCapability.EXPORT_PDF,
            DiagramCapability.EXPORT_MARKDOWN);

    private static final Set<DiagramCapability> FORBIDDEN_FOR_PREPARATION = EnumSet.of(
            DiagramCapability.CREATE_PROJECT,
            DiagramCapability.IMPORT_MARKDOWN,
            DiagramCapability.IMPORT_SOURCE_CODE,
            DiagramCapability.OPEN_SOURCE_CODE,
            DiagramCapability.SHOW_VISUAL_OUTPUT,
            DiagramCapability.SHOW_DOCUMENT_OUTPUT,
            DiagramCapability.MANUAL_EDITING,
            DiagramCapability.EXPORT_PNG,
            DiagramCapability.EXPORT_SVG,
            DiagramCapability.EXPORT_PDF,
            DiagramCapability.EXPORT_MARKDOWN,
            DiagramCapability.SAVE_DMS,
            DiagramCapability.LOAD_DMS,
            DiagramCapability.BATCH_EXPORT);

    void validate(List<DiagramTypeDescriptor> descriptors) {
        Objects.requireNonNull(descriptors, "descriptors");
        descriptors.forEach(this::validate);
    }

    void validate(DiagramTypeDescriptor descriptor) {
        Objects.requireNonNull(descriptor, "descriptor");
        if (descriptor.supportStatus() == DiagramSupportStatus.AVAILABLE) {
            validateAvailable(descriptor);
            return;
        }
        if (descriptor.supportStatus() == DiagramSupportStatus.IN_PREPARATION) {
            validateInPreparation(descriptor);
        }
    }

    private void validateAvailable(DiagramTypeDescriptor descriptor) {
        if (!hasAny(descriptor, PRODUCT_OUTPUT_CAPABILITIES)) {
            throw inconsistent(descriptor, "un tipo disponible debe mostrar una salida visual o documental real");
        }
        if (!hasAny(descriptor, EXPORT_CAPABILITIES)) {
            throw inconsistent(descriptor, "un tipo disponible debe tener al menos una exportación real");
        }
        if (descriptor.supports(DiagramCapability.PLANNING_VIEW)) {
            throw inconsistent(descriptor, "un tipo disponible no debe depender de una guía de preparación");
        }
        if (!descriptor.supports(DiagramCapability.CREATE_PROJECT)) {
            throw inconsistent(descriptor, "un tipo disponible debe permitir crear proyecto editable");
        }
        if (descriptor.workspaceKind() == DiagramWorkspaceKind.PLACEHOLDER_GUIDE) {
            throw inconsistent(descriptor, "un tipo disponible debe declarar familia de workspace real");
        }
    }

    private void validateInPreparation(DiagramTypeDescriptor descriptor) {
        if (!descriptor.supports(DiagramCapability.PLANNING_VIEW)) {
            throw inconsistent(descriptor, "un tipo en preparación debe abrir solo guía de preparación");
        }
        for (DiagramCapability forbidden : FORBIDDEN_FOR_PREPARATION) {
            if (descriptor.supports(forbidden)) {
                throw inconsistent(descriptor, "un tipo en preparación no debe declarar " + forbidden.name());
            }
        }
        if (descriptor.workspaceKind() != DiagramWorkspaceKind.PLACEHOLDER_GUIDE) {
            throw inconsistent(descriptor, "un tipo en preparación debe mantenerse en guía de preparación");
        }
    }

    private static boolean hasAny(DiagramTypeDescriptor descriptor, Set<DiagramCapability> capabilities) {
        return capabilities.stream().anyMatch(descriptor::supports);
    }

    private static IllegalStateException inconsistent(DiagramTypeDescriptor descriptor, String reason) {
        return new IllegalStateException("Catálogo inconsistente para "
                + descriptor.id().value() + ": " + reason + ".");
    }
}
