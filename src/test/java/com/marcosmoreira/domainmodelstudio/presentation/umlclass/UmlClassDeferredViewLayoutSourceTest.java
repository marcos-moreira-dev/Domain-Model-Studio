package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassDeferredViewLayoutSourceTest {

    private static final Path UML_CLASS_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass"
    );
    private static final Path VISUAL_SERVICE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualLayoutService.java"
    );

    @Test
    void viewModelShouldPrepareLayoutFromFilteredVisibleResult() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("prepareVisibleLayout(filtered)"),
                "El layout UML debe prepararse desde el resultado filtrado de la vista activa.");
        assertTrue(source.contains("new UmlClassDiagramDocument(") && source.contains("filtered.classes()"),
                "Debe crear un documento visual acotado a clases visibles sin cambiar el modelo completo.");
        assertTrue(source.contains("ensureAdditionalVisualLayout(scopedProject)"),
                "Debe usar reconciliación incremental para no materializar ni podar todo el documento.");
        assertTrue(source.contains("preparedLayoutScopeKey"),
                "Debe cachear el alcance visible para evitar recalcular la misma vista repetidamente.");
    }

    @Test
    void visualLayoutServiceShouldExposeNonPruningIncrementalPreparation() throws IOException {
        String source = Files.readString(VISUAL_SERVICE, StandardCharsets.UTF_8);

        assertTrue(source.contains("ensureAdditionalVisualLayout"),
                "Debe existir una preparación incremental de layout para vistas parciales.");
        assertTrue(source.contains("reconcilePreservingExisting"),
                "La preparación incremental debe conservar layouts ajenos al subconjunto actual.");
        assertTrue(source.contains("reconciled.withNode(layout)"),
                "Los nodos visibles deben agregarse/actualizarse sin recrear el layout completo.");
        assertTrue(source.contains("reconciled.withConnector(layout)"),
                "Los conectores visibles deben agregarse/actualizarse sin recrear el layout completo.");
    }
    @Test
    void movingVisibleUmlNodesShouldUsePreparedLayoutWithoutFullReconciliation() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("movePreparedNodeTo"),
                "Mover clases o módulos visibles debe operar sobre el layout ya preparado.");
        assertTrue(source.contains("currentProject.layouts().activeLayout().moveNode"),
                "El movimiento de nodos visibles debe actualizar el layout activo directamente.");
        assertTrue(!source.contains("visualLayoutService.moveNodeTo(currentProject"),
                "Mover una clase visible no debe forzar ensureVisualLayout completo por cada arrastre.");
        assertTrue(!source.contains("visualLayoutService.moveNodeBy("),
                "Mover módulos visibles no debe mover clases con una ruta que recalcula el layout completo.");
    }

    @Test
    void completeAndAllViewsShouldBeGuardedBeforeRendering() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("requested == null || requested.kind() == UmlClassDiagramViewKind.FULL"),
                "Tanto 'Todas' como 'Mega vista' deben pasar por la protección de costo crítico.");
        assertTrue(source.contains("selectedView.set(safeRenderableView(null))"),
                "Limpiar filtros no debe abrir todo el documento si la vista completa es crítica.");
        assertTrue(source.contains("Vista completa bloqueada por costo crítico"),
                "El usuario debe recibir un mensaje claro cuando se redirige a una vista segura.");
    }

}
