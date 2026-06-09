package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Revisa consistencia básica del diagrama UML Clases. */
public final class ValidateUmlClassDiagramUseCase {
    public UmlClassDiagramValidationResult validate(UmlClassDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        ArrayList<String> warnings = new ArrayList<>();
        if (document.modules().isEmpty()) { warnings.add("El diagrama todavía no tiene módulos o carpetas agrupadoras."); }
        if (document.classes().isEmpty()) { warnings.add("El diagrama todavía no tiene clases."); }
        Set<String> moduleNames = new HashSet<>();
        for (UmlModuleGroup module : document.modules()) {
            if (!moduleNames.add(module.displayName().toLowerCase(java.util.Locale.ROOT))) {
                warnings.add("Nombre de módulo repetido: " + module.displayName());
            }
            if (module.path().isBlank() && module.description().isBlank()) {
                warnings.add("El módulo '" + module.displayName() + "' no tiene ruta ni descripción.");
            }
        }
        Set<String> classNames = new HashSet<>();
        for (UmlClassNode node : document.classes()) {
            String classKey = (node.moduleId() + ":" + node.displayName()).toLowerCase(java.util.Locale.ROOT);
            if (!classNames.add(classKey)) {
                warnings.add("Clase repetida dentro del mismo módulo: " + node.displayName());
            }
            if (node.moduleId().isBlank()) {
                warnings.add("La clase '" + node.displayName() + "' no está agrupada en un módulo/carpeta.");
            }
            if (node.members().isEmpty()) {
                warnings.add("La clase '" + node.displayName() + "' no tiene atributos ni métodos registrados.");
            }
        }
        for (UmlClassRelation relation : document.relations()) {
            if (relation.sourceClassId().equals(relation.targetClassId())) {
                warnings.add("Relación UML circular hacia la misma clase: " + relation.sourceClassId());
            }
        }
        return new UmlClassDiagramValidationResult(warnings);
    }
}
