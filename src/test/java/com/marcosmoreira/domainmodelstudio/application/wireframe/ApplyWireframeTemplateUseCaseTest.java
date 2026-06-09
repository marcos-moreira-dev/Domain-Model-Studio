package com.marcosmoreira.domainmodelstudio.application.wireframe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreenTemplateKind;
import org.junit.jupiter.api.Test;

class ApplyWireframeTemplateUseCaseTest {

    @Test
    void appliesEveryTemplateAsOneScreenWithComponents() {
        ApplyWireframeTemplateUseCase useCase = new ApplyWireframeTemplateUseCase();
        WireframeDocument document = WireframeDocument.blank("Maquetas");

        for (WireframeScreenTemplateKind template : WireframeScreenTemplateKind.values()) {
            document = useCase.apply(document, template);
        }

        assertEquals(WireframeScreenTemplateKind.values().length, document.screens().size());
        assertFalse(document.components().isEmpty());
        assertTrue(document.components().stream().anyMatch(component -> component.kind() == WireframeComponentKind.TABLE));
        assertTrue(document.components().stream().anyMatch(component -> component.kind() == WireframeComponentKind.BUTTON));
        assertTrue(document.components().stream().anyMatch(component -> component.kind() == WireframeComponentKind.TOP_BAR));
    }

    @Test
    void defaultsToCrudListWhenTemplateIsNull() {
        ApplyWireframeTemplateUseCase useCase = new ApplyWireframeTemplateUseCase();
        WireframeDocument document = useCase.apply(WireframeDocument.blank("Maquetas"), null);

        assertEquals(1, document.screens().size());
        assertEquals("Listado administrativo", document.screens().get(0).displayName());
        assertTrue(document.components().stream().anyMatch(component -> component.kind() == WireframeComponentKind.PAGINATION));
    }
}
