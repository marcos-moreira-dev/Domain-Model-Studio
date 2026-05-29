package com.marcosmoreira.domainmodelstudio.application.theory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Carga temas teóricos desde recursos Markdown versionados junto al producto. */
final class TheoryTopicResourceLoader {

    private final ClassLoader classLoader;
    private final TheoryTopicMarkdownParser parser;

    TheoryTopicResourceLoader() {
        this(Thread.currentThread().getContextClassLoader(), new TheoryTopicMarkdownParser());
    }

    TheoryTopicResourceLoader(ClassLoader classLoader, TheoryTopicMarkdownParser parser) {
        this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
        this.parser = Objects.requireNonNull(parser, "parser");
    }

    List<TheoryTopic> loadAll(List<TheoryTopicResource> resources) {
        Objects.requireNonNull(resources, "resources");
        List<TheoryTopic> topics = new ArrayList<>();
        for (TheoryTopicResource resource : resources) {
            topics.add(load(resource));
        }
        return List.copyOf(topics);
    }

    private TheoryTopic load(TheoryTopicResource resource) {
        try (InputStream input = classLoader.getResourceAsStream(resource.resourcePath())) {
            if (input == null) {
                throw new IllegalStateException("No se encontró el tema académico: " + resource.resourcePath());
            }
            String markdown = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            return parser.parse(resource, markdown);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo leer el tema académico: " + resource.resourcePath(), exception);
        }
    }
}
