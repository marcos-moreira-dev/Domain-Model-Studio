package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import java.util.Optional;

/** Puerto opcional para aplicar estilos visuales persistentes desde el canvas común. */
public interface CanvasStylePort {

    Optional<ElementStyle> explicitStyleForElement(String elementId);

    ElementStyle resolvedStyleForElement(String elementId);

    void applyElementStyle(String elementId, ElementStyle style);

    void resetElementStyle(String elementId);
}
