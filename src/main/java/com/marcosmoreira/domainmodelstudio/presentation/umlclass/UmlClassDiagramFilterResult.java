package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.List;

/** Resultado filtrado que consumen el árbol lateral, el lienzo y los combos del editor UML. */
record UmlClassDiagramFilterResult(
        List<UmlModuleGroup> modules,
        List<UmlClassNode> classes,
        List<UmlClassRelation> relations
) {
    UmlClassDiagramFilterResult {
        modules = List.copyOf(modules == null ? List.of() : modules);
        classes = List.copyOf(classes == null ? List.of() : classes);
        relations = List.copyOf(relations == null ? List.of() : relations);
    }

    static UmlClassDiagramFilterResult empty() {
        return new UmlClassDiagramFilterResult(List.of(), List.of(), List.of());
    }
}
