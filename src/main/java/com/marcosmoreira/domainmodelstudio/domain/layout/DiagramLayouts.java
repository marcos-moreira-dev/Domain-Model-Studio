package com.marcosmoreira.domainmodelstudio.domain.layout;

import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Colección de layouts por notación.
 *
 * <p>Permite que Chen y Crow's Foot conserven acomodos distintos sin duplicar el modelo
 * semántico. Esta decisión evita tratar ambas notaciones como simples temas visuales.</p>
 */
public final class DiagramLayouts {

    private final NotationType activeNotation;
    private final Map<NotationType, DiagramLayout> layoutsByNotation;

    public DiagramLayouts(NotationType activeNotation, Map<NotationType, DiagramLayout> layoutsByNotation) {
        this.activeNotation = activeNotation == null ? NotationType.CHEN : activeNotation;
        this.layoutsByNotation = copyLayouts(layoutsByNotation);
    }

    public static DiagramLayouts empty() {
        return new DiagramLayouts(NotationType.CHEN, Map.of(NotationType.CHEN, DiagramLayout.empty(NotationType.CHEN)));
    }

    public static DiagramLayouts forNotation(NotationType notation) {
        NotationType normalized = notation == null ? NotationType.CHEN : notation;
        return new DiagramLayouts(normalized, Map.of(normalized, DiagramLayout.empty(normalized)));
    }

    public NotationType activeNotation() {
        return activeNotation;
    }

    public DiagramLayout activeLayout() {
        return layoutFor(activeNotation).orElseGet(() -> DiagramLayout.empty(activeNotation));
    }

    public Optional<DiagramLayout> layoutFor(NotationType notation) {
        return Optional.ofNullable(layoutsByNotation.get(notation));
    }

    public Map<NotationType, DiagramLayout> layoutsByNotation() {
        return Map.copyOf(layoutsByNotation);
    }

    public DiagramLayouts withActiveNotation(NotationType notation) {
        NotationType normalized = notation == null ? NotationType.CHEN : notation;
        Map<NotationType, DiagramLayout> updated = new EnumMap<>(layoutsByNotation);
        updated.putIfAbsent(normalized, DiagramLayout.empty(normalized));
        return new DiagramLayouts(normalized, updated);
    }

    public DiagramLayouts withLayout(DiagramLayout layout) {
        Objects.requireNonNull(layout, "El layout no puede ser null");
        Map<NotationType, DiagramLayout> updated = new EnumMap<>(layoutsByNotation);
        updated.put(layout.notation(), layout);
        return new DiagramLayouts(activeNotation, updated);
    }

    private static Map<NotationType, DiagramLayout> copyLayouts(Map<NotationType, DiagramLayout> layouts) {
        Map<NotationType, DiagramLayout> copy = new EnumMap<>(NotationType.class);
        if (layouts != null) {
            for (Map.Entry<NotationType, DiagramLayout> entry : layouts.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    copy.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return Map.copyOf(copy);
    }
}
