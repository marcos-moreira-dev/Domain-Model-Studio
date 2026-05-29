package com.marcosmoreira.domainmodelstudio.domain.assets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ProjectAssetReferenceTest {

    @Test
    void acceptsOnlyRelativeProjectPaths() {
        ProjectAssetReference reference = ProjectAssetReference.logo("logo", "Logo", "assets/logos/cliente.png");

        assertEquals("assets/logos/cliente.png", reference.relativePath());
    }

    @Test
    void rejectsAbsolutePaths() {
        assertThrows(IllegalArgumentException.class,
                () -> ProjectAssetReference.logo("logo", "Logo", "C:/Users/cliente/logo.png"));
        assertThrows(IllegalArgumentException.class,
                () -> ProjectAssetReference.logo("logo", "Logo", "/home/user/logo.png"));
        assertThrows(IllegalArgumentException.class,
                () -> ProjectAssetReference.logo("logo", "Logo", "../logo.png"));
    }
}
