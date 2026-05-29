package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.application.runtime.DefaultPayloadRuntimeRegistry;
import com.marcosmoreira.domainmodelstudio.application.runtime.PayloadRuntimeDescriptor;
import com.marcosmoreira.domainmodelstudio.application.runtime.PayloadRuntimeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.List;
import java.util.Objects;

/**
 * Verifica que el archivo .dms guarde el documento principal correcto para su tipo.
 *
 * <p>Esta compuerta evita que un proyecto especializado se degrade silenciosamente a
 * modelo conceptual, o que un .dms mezcle datos de varios tipos y confunda la apertura.
 * Desde la Tanda 12 delega la detección de payloads en {@link PayloadRuntimeRegistry},
 * manteniendo estable el formato .dms v3 mientras se prepara la migración a payloads
 * registrables.</p>
 */
final class DmsProjectPayloadConsistencyValidator {

    private final PayloadRuntimeRegistry payloadRuntimeRegistry;

    DmsProjectPayloadConsistencyValidator() {
        this(new DefaultPayloadRuntimeRegistry());
    }

    DmsProjectPayloadConsistencyValidator(PayloadRuntimeRegistry payloadRuntimeRegistry) {
        this.payloadRuntimeRegistry = Objects.requireNonNull(payloadRuntimeRegistry, "payloadRuntimeRegistry");
    }

    void validate(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        DiagramTypeId typeId = project.metadata().diagramTypeId();
        if (typeId == null) {
            throw new IllegalArgumentException("El proyecto no declara tipo de diagrama.");
        }
        validatePrimaryPayload(project, typeId);
        validateNoConflictingSpecializedPayload(project, typeId);
    }

    private void validatePrimaryPayload(DiagramProject project, DiagramTypeId typeId) {
        PayloadRuntimeDescriptor descriptor = payloadRuntimeRegistry.require(typeId);
        if (!descriptor.presentIn(project)) {
            throw new IllegalArgumentException("El proyecto declara " + typeId.value()
                    + " pero no contiene el documento principal de " + descriptor.displayName() + ".");
        }
        descriptor.consistencyProblem(project).ifPresent(problem -> {
            throw new IllegalArgumentException(problem);
        });
    }

    private void validateNoConflictingSpecializedPayload(DiagramProject project, DiagramTypeId typeId) {
        List<String> present = payloadRuntimeRegistry.detectSpecializedPayloadTypeIds(project).stream()
                .map(DiagramTypeId::value)
                .toList();

        if (present.isEmpty() || DiagramTypeId.CONCEPTUAL_MODEL.equals(typeId)) {
            if (DiagramTypeId.CONCEPTUAL_MODEL.equals(typeId) && !present.isEmpty()) {
                throw new IllegalArgumentException("El proyecto conceptual contiene documentos especializados: "
                        + String.join(", ", present) + ". Corrige el tipo de diagrama antes de guardar.");
            }
            return;
        }
        if (present.size() > 1 || !present.get(0).equals(typeId.value())) {
            throw new IllegalArgumentException("El proyecto declara " + typeId.value()
                    + " pero contiene documentos especializados no alineados: " + String.join(", ", present) + ".");
        }
    }
}
