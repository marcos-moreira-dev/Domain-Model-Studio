package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javafx.scene.Parent;

/** Módulo lateral que reutiliza una vista ya existente del workspace. */
public final class StaticSideDockModule implements SideDockModule {

    private final SideDockModuleId id;
    private final String title;
    private final String tooltip;
    private final String iconText;
    private final Supplier<Parent> contentSupplier;
    private final Predicate<SideDockContext> supportPredicate;

    public StaticSideDockModule(
            SideDockModuleId id,
            String title,
            String tooltip,
            String iconText,
            Supplier<Parent> contentSupplier,
            Predicate<SideDockContext> supportPredicate
    ) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = clean(title, id.displayName());
        this.tooltip = clean(tooltip, this.title);
        this.iconText = clean(iconText, id.iconText());
        this.contentSupplier = Objects.requireNonNull(contentSupplier, "contentSupplier");
        this.supportPredicate = supportPredicate == null ? context -> true : supportPredicate;
    }

    public static StaticSideDockModule of(SideDockModuleId id, String title, Parent content) {
        Objects.requireNonNull(content, "content");
        return new StaticSideDockModule(id, title, id.tooltipText(), id.iconText(), () -> content, context -> true);
    }

    public static StaticSideDockModule contextual(
            SideDockModuleId id,
            String title,
            Parent content,
            Predicate<SideDockContext> supportPredicate
    ) {
        Objects.requireNonNull(content, "content");
        return new StaticSideDockModule(id, title, id.tooltipText(), id.iconText(), () -> content, supportPredicate);
    }

    @Override
    public SideDockModuleId id() {
        return id;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String tooltip() {
        return tooltip;
    }

    @Override
    public String iconText() {
        return iconText;
    }

    @Override
    public boolean supports(SideDockContext context) {
        return supportPredicate.test(context);
    }

    @Override
    public Parent createView(SideDockContext context) {
        return contentSupplier.get();
    }

    private static String clean(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.strip();
    }
}
