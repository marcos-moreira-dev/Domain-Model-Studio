package com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;

/** Constructor común para recursos IA empaquetados. */
final class AiResourceDescriptorFactory {

    private static final String BASE = "ai-resources/";

    private AiResourceDescriptorFactory() {
    }

    static AiResourceDescriptor importable(String id, String fileName, DiagramTypeId diagramTypeId, String description) {
        return new AiResourceDescriptor(id, fileName, diagramTypeId, BASE + fileName, true, true, description);
    }

    static AiResourceDescriptor planning(String id, String fileName, DiagramTypeId diagramTypeId, String description) {
        return new AiResourceDescriptor(id, fileName, diagramTypeId, BASE + fileName, true, false, description);
    }

    static AiResourceDescriptor alias(String id, String fileName, DiagramTypeId diagramTypeId, String description) {
        return new AiResourceDescriptor(id, fileName, diagramTypeId, BASE + fileName, false, false, description);
    }

    static AiResourceDescriptor logicalBusiness(String id, String fileName, String description) {
        String path = "official-markdown/levantamiento-logico/" + fileName;
        return new AiResourceDescriptor(id, path, DiagramTypeId.LOGICAL_BUSINESS_INTAKE, BASE + path, true, true, description);
    }

    static AiResourceDescriptor uensExample(String id, String fileName, DiagramTypeId diagramTypeId, String description) {
        String path = "official-markdown/diagramas/" + fileName;
        return new AiResourceDescriptor(id, path, diagramTypeId, BASE + path, true, true, description);
    }
}
