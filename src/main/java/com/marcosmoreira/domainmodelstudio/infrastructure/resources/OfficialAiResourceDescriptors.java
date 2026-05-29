package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions.CoreAiResourceDescriptors;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions.OfficialMinimalExampleAiResourceDescriptors;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions.OfficialTemplateAiResourceDescriptors;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions.OfficialUensExampleAiResourceDescriptors;
import java.util.ArrayList;
import java.util.List;

/** Descriptores oficiales de recursos IA empaquetados en el classpath. */
public final class OfficialAiResourceDescriptors {

    private OfficialAiResourceDescriptors() {
    }

    public static List<AiResourceDescriptor> all() {
        List<AiResourceDescriptor> resources = new ArrayList<>();
        resources.addAll(CoreAiResourceDescriptors.all());
        resources.addAll(OfficialTemplateAiResourceDescriptors.all());
        resources.addAll(OfficialMinimalExampleAiResourceDescriptors.all());
        resources.addAll(OfficialUensExampleAiResourceDescriptors.all());
        resources.addAll(LogicalBusinessGraphAiResourceDescriptors.all());
        return List.copyOf(resources);
    }
}
