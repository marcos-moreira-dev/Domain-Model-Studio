package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifica que los contenedores del mapa de módulos nazcan con aire para sus tarjetas internas. */
class ModuleMapTextFitContainerFootprintTest {

    @Test
    void moduleContainerExpandsFromFittedChildCards() {
        ModuleNode root = ModuleNode.root("calificaciones", "Calificaciones");
        ModuleNode registro = child("registro", "Registro notas",
                "captura de nota, parcial y observación.");
        ModuleNode consulta = child("consulta", "Consulta notas",
                "búsqueda por estudiante, clase o sección.");
        ModuleMapDocument document = new ModuleMapDocument(
                "UENS", "borrador", LocalDate.of(2026, 5, 26),
                List.of(root, registro, consulta), List.<ModuleDependency>of(), "");

        List<VisualNodeReference> refs = new AdminApplicationsLayoutPolicy().moduleMapReferences(document, 0);
        VisualNodeReference container = reference(refs, root.id());
        VisualNodeReference child = reference(refs, registro.id());

        assertTrue(child.preferredWidth() > 190.0, "la tarjeta hija debe crecer por su texto");
        assertTrue(container.preferredWidth() >= child.preferredWidth() * 2.0 + 100.0,
                "el contenedor debe reservar aire horizontal para tarjetas ajustadas");
        assertTrue(container.preferredHeight() >= child.preferredHeight() + 120.0,
                "el contenedor debe conservar cabecera y margen interno preliminar");
    }

    private static ModuleNode child(String id, String name, String description) {
        return new ModuleNode(id, name, "calificaciones", ModuleKind.SUPPORT, ModuleStatus.PLANNED,
                "Soporte · Planificado", description, List.of(), "");
    }

    private static VisualNodeReference reference(List<VisualNodeReference> refs, String moduleId) {
        return refs.stream()
                .filter(ref -> ref.layoutId().equals(VisualElementLayoutIds.module(moduleId)))
                .findFirst()
                .orElseThrow();
    }
}
