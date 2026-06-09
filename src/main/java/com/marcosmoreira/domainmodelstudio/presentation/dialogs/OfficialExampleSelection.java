package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.application.examples.OfficialExampleDescriptor;
import java.util.List;

/** Resultado del selector de ejemplos: uno o varios ejemplos importables. */
public record OfficialExampleSelection(List<OfficialExampleDescriptor> examples) {

    public OfficialExampleSelection {
        examples = List.copyOf(examples == null ? List.of() : examples);
    }

    public static OfficialExampleSelection single(OfficialExampleDescriptor example) {
        return new OfficialExampleSelection(example == null ? List.of() : List.of(example));
    }

    public boolean empty() {
        return examples.isEmpty();
    }
}
