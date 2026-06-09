package com.marcosmoreira.domainmodelstudio.domain.style;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Hoja de estilos del proyecto.
 *
 * <p>Mantiene un estilo base y estilos específicos por elemento. Así se evita repetir
 * colores/fuentes en cada figura y se permite personalización puntual desde el inspector.</p>
 */
public final class DiagramStyleSheet {

    private final ElementStyle defaultStyle;
    private final Map<DiagramElementId, ElementStyle> stylesByElementId;
    private final DiagramAppearance appearance;

    public DiagramStyleSheet(ElementStyle defaultStyle, Map<DiagramElementId, ElementStyle> stylesByElementId) {
        this(defaultStyle, stylesByElementId, DiagramAppearance.defaults());
    }

    public DiagramStyleSheet(
            ElementStyle defaultStyle,
            Map<DiagramElementId, ElementStyle> stylesByElementId,
            DiagramAppearance appearance
    ) {
        this.defaultStyle = Objects.requireNonNull(defaultStyle, "El estilo por defecto no puede ser null");
        this.stylesByElementId = Map.copyOf(stylesByElementId == null ? Map.of() : stylesByElementId);
        this.appearance = Objects.requireNonNull(appearance, "La apariencia del diagrama no puede ser null");
    }

    public static DiagramStyleSheet defaults() {
        return new DiagramStyleSheet(ElementStyle.defaultElement(), Map.of(), DiagramAppearance.defaults());
    }

    public ElementStyle defaultStyle() {
        return defaultStyle;
    }

    public Map<DiagramElementId, ElementStyle> stylesByElementId() {
        return stylesByElementId;
    }

    public DiagramAppearance appearance() {
        return appearance;
    }

    public Optional<ElementStyle> explicitStyleFor(DiagramElementId elementId) {
        return Optional.ofNullable(stylesByElementId.get(elementId));
    }

    public ElementStyle resolvedStyleFor(DiagramElementId elementId) {
        return explicitStyleFor(elementId).orElse(defaultStyle);
    }

    public DiagramStyleSheet withElementStyle(DiagramElementId elementId, ElementStyle style) {
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        Objects.requireNonNull(style, "El estilo no puede ser null");
        Map<DiagramElementId, ElementStyle> updated = new LinkedHashMap<>(stylesByElementId);
        updated.put(elementId, style);
        return new DiagramStyleSheet(defaultStyle, updated, appearance);
    }

    public DiagramStyleSheet withoutElementStyle(DiagramElementId elementId) {
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");
        Map<DiagramElementId, ElementStyle> updated = new LinkedHashMap<>(stylesByElementId);
        updated.remove(elementId);
        return new DiagramStyleSheet(defaultStyle, updated, appearance);
    }

    public DiagramStyleSheet withAppearance(DiagramAppearance updatedAppearance) {
        return new DiagramStyleSheet(defaultStyle, stylesByElementId, updatedAppearance);
    }
}
