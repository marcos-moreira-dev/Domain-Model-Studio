package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import org.junit.jupiter.api.Test;

class WireframeMarkdownParserScaffoldingKindTest {

    @Test
    void infersExtendedScaffoldingKinds() throws Exception {
        String markdown = """
                ---
                diagram_type: "admin-wireframes"
                name: "Maqueta"
                ---
                # Pantallas

                ## Gestión
                id: gestion
                módulo: administración
                propósito: revisar bloques administrativos.

                ### Secciones
                - barra superior: navegación global.
                - menú lateral: módulos del sistema.
                - pestañas de detalle: resumen, historial y acciones.
                - alerta de validación: datos incompletos.

                ### Controles
                - búsqueda rápida: por nombre o código.
                - tabla de resultados: columnas visibles.
                - paginación: página actual.
                - botón guardar: confirma cambios.
                """;

        var project = new WireframeMarkdownParser().parse(markdown, "inline");
        var components = project.wireframe().orElseThrow().components();
        assertTrue(components.stream().anyMatch(component -> component.kind() == WireframeComponentKind.TOP_BAR));
        assertTrue(components.stream().anyMatch(component -> component.kind() == WireframeComponentKind.SIDEBAR));
        assertTrue(components.stream().anyMatch(component -> component.kind() == WireframeComponentKind.TABS));
        assertTrue(components.stream().anyMatch(component -> component.kind() == WireframeComponentKind.ALERT));
        assertTrue(components.stream().anyMatch(component -> component.kind() == WireframeComponentKind.SEARCH));
        assertTrue(components.stream().anyMatch(component -> component.kind() == WireframeComponentKind.PAGINATION));
    }
}
