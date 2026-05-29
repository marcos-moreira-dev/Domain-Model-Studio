package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.util.List;

/**
 * Calcula tamaños mínimos para wireframes administrativos según texto y tipo visual.
 *
 * <p>No mide fuentes JavaFX reales: entrega una aproximación estable para layout inicial,
 * importación Markdown y reconciliación de tamaños. El render final sigue siendo de baja
 * fidelidad, pero evita cajas demasiado pequeñas para títulos y descripciones comunes.</p>
 */
public final class WireframeVisualTextMetricsPolicy {

    private static final double CHARACTER_WIDTH = 6.4;
    private static final double LINE_HEIGHT = 14.0;
    private static final double HORIZONTAL_PADDING = 28.0;
    private static final double VERTICAL_PADDING = 32.0;

    public ScreenSize screenSize(WireframeScreen screen, int componentCount) {
        double titleWidth = textWidth(screen == null ? "" : screen.displayName(), 300.0, 560.0);
        double purposeWidth = textWidth(screen == null ? "" : screen.purpose(), 300.0, 560.0);
        int rows = Math.max(1, (int) Math.ceil(Math.max(1, componentCount) / 2.0));
        double width = Math.max(320.0, Math.max(titleWidth, purposeWidth));
        double height = Math.max(210.0, 72.0 + rows * 70.0);
        return new ScreenSize(Math.min(width, 560.0), Math.min(height, 420.0));
    }

    public ScreenSize componentSize(WireframeComponent component) {
        WireframeComponentKind kind = component == null ? WireframeComponentKind.OTHER : component.kind();
        ScreenSize base = baseComponentSize(kind);
        double textWidth = widestText(component);
        int textLines = estimatedLines(component, base.width());
        double width = Math.max(base.width(), textWidth);
        double height = Math.max(base.height(), base.height() + Math.max(0, textLines - 2) * LINE_HEIGHT);
        return new ScreenSize(Math.min(width, maxWidth(kind)), Math.min(height, maxHeight(kind)));
    }

    private static ScreenSize baseComponentSize(WireframeComponentKind kind) {
        return switch (kind == null ? WireframeComponentKind.OTHER : kind) {
            case TOP_BAR -> new ScreenSize(230.0, 34.0);
            case SIDEBAR -> new ScreenSize(90.0, 120.0);
            case TABLE, REPORT -> new ScreenSize(180.0, 72.0);
            case FORM, DETAIL -> new ScreenSize(170.0, 76.0);
            case MODAL -> new ScreenSize(166.0, 76.0);
            case CHART -> new ScreenSize(150.0, 70.0);
            case ALERT, EMPTY_STATE -> new ScreenSize(150.0, 46.0);
            case STEPPER -> new ScreenSize(164.0, 46.0);
            case BADGE -> new ScreenSize(96.0, 34.0);
            case DOCUMENT_LIST -> new ScreenSize(180.0, 72.0);
            case CALENDAR -> new ScreenSize(180.0, 82.0);
            case APPROVAL_PANEL -> new ScreenSize(170.0, 72.0);
            case SUMMARY -> new ScreenSize(154.0, 62.0);
            case BUTTON -> new ScreenSize(110.0, 34.0);
            case FIELD, FILTER, SEARCH -> new ScreenSize(130.0, 42.0);
            case PAGINATION -> new ScreenSize(126.0, 36.0);
            case TABS, MENU -> new ScreenSize(146.0, 40.0);
            case SECTION, PANEL, CARD -> new ScreenSize(150.0, 60.0);
            default -> new ScreenSize(136.0, 50.0);
        };
    }

    private static double widestText(WireframeComponent component) {
        if (component == null) {
            return 0.0;
        }
        return List.of(component.displayName(), component.dataBinding(), component.behavior()).stream()
                .mapToDouble(text -> textWidth(firstLine(text), 0.0, 360.0))
                .max()
                .orElse(0.0);
    }

    private static int estimatedLines(WireframeComponent component, double baseWidth) {
        if (component == null) {
            return 1;
        }
        double usable = Math.max(80.0, baseWidth - HORIZONTAL_PADDING);
        return List.of(component.displayName(), component.dataBinding(), component.behavior()).stream()
                .mapToInt(text -> lineCount(text, usable))
                .sum();
    }

    private static int lineCount(String value, double usableWidth) {
        String clean = value == null ? "" : value.strip();
        if (clean.isBlank()) {
            return 0;
        }
        int explicitLines = clean.split("\\R", -1).length;
        int wrappedLines = (int) Math.ceil(clean.length() * CHARACTER_WIDTH / usableWidth);
        return Math.max(explicitLines, Math.max(1, wrappedLines));
    }

    private static double textWidth(String text, double minimum, double maximum) {
        String clean = text == null ? "" : text.strip();
        double approx = clean.length() * CHARACTER_WIDTH + HORIZONTAL_PADDING;
        return Math.min(Math.max(minimum, approx), maximum);
    }

    private static String firstLine(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.strip().split("\\R", 2)[0];
    }

    private static double maxWidth(WireframeComponentKind kind) {
        return switch (kind == null ? WireframeComponentKind.OTHER : kind) {
            case TABLE, REPORT, FORM, DETAIL, MODAL, DOCUMENT_LIST, CALENDAR, APPROVAL_PANEL -> 280.0;
            case TOP_BAR -> 420.0;
            case SIDEBAR -> 170.0;
            default -> 230.0;
        };
    }

    private static double maxHeight(WireframeComponentKind kind) {
        return switch (kind == null ? WireframeComponentKind.OTHER : kind) {
            case TABLE, REPORT, FORM, DETAIL, MODAL, SIDEBAR, DOCUMENT_LIST, CALENDAR, APPROVAL_PANEL -> 150.0;
            case TOP_BAR, BUTTON, FIELD, FILTER, SEARCH, PAGINATION -> 90.0;
            default -> 110.0;
        };
    }

    public record ScreenSize(double width, double height) { }
}
