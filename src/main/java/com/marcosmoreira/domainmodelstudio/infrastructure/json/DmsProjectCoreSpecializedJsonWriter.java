package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;

/** Escribe diccionario de datos, mapa de módulos y UML Clases dentro del bloque model. */
final class DmsProjectCoreSpecializedJsonWriter {

    void writeCoreDocuments(DiagramProject project, StringBuilder json, int level) {
        if (project.dataDictionary().isPresent()) {
            writeDataDictionary(project.dataDictionary().get(), json, level, true);
        }
        if (project.moduleMap().isPresent()) {
            writeModuleMap(project.moduleMap().get(), json, level, true);
        }
        if (project.umlClassDiagram().isPresent()) {
            writeUmlClassDiagram(project.umlClassDiagram().get(), json, level, true);
        }
    }

    private void writeDataDictionary(DataDictionaryDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"dataDictionary\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "clientName", document.clientName(), true);
        field(json, level + 1, "organizationName", document.organizationName(), true);
        field(json, level + 1, "author", document.author(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        field(json, level + 1, "status", document.status().name(), true);
        field(json, level + 1, "introduction", document.introduction(), true);
        field(json, level + 1, "logoReference", document.logoReference(), true);
        field(json, level + 1, "notes", document.notes(), true);
        indent(json, level + 1).append("\"entities\": [\n");
        for (int i = 0; i < document.entities().size(); i++) {
            writeDataDictionaryEntity(document.entities().get(i), json, level + 2, i + 1 < document.entities().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeDataDictionaryEntity(DataDictionaryEntity entity, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", entity.id(), true);
        field(json, level + 1, "displayName", entity.displayName(), true);
        field(json, level + 1, "technicalName", entity.technicalName(), true);
        field(json, level + 1, "description", entity.description(), true);
        field(json, level + 1, "moduleName", entity.moduleName(), true);
        field(json, level + 1, "kind", entity.kind().name(), true);
        field(json, level + 1, "origin", entity.origin(), true);
        field(json, level + 1, "status", entity.status().name(), true);
        field(json, level + 1, "notes", entity.notes(), true);
        indent(json, level + 1).append("\"fields\": [\n");
        for (int i = 0; i < entity.fields().size(); i++) {
            writeDataDictionaryField(entity.fields().get(i), json, level + 2, i + 1 < entity.fields().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeDataDictionaryField(DataDictionaryField field, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "name", field.name(), true);
        field(json, level + 1, "displayName", field.displayName(), true);
        field(json, level + 1, "technicalName", field.technicalName(), true);
        field(json, level + 1, "logicalType", field.logicalType().name(), true);
        field(json, level + 1, "physicalTypeSuggestion", field.physicalTypeSuggestion(), true);
        writeEnumArray("constraints", field.constraints(), json, level + 1, true);
        field(json, level + 1, "foreignKeyReference", field.foreignKeyReference(), true);
        field(json, level + 1, "defaultValue", field.defaultValue(), true);
        field(json, level + 1, "expectedFormat", field.expectedFormat(), true);
        field(json, level + 1, "description", field.description(), true);
        field(json, level + 1, "businessRule", field.businessRule(), true);
        field(json, level + 1, "validationRule", field.validationRule(), true);
        field(json, level + 1, "example", field.example(), true);
        writeEnumArray("visibility", field.visibility(), json, level + 1, true);
        booleanField(json, level + 1, "userEditable", field.userEditable(), true);
        field(json, level + 1, "notes", field.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeEnumArray(String name, java.util.Set<? extends Enum<?>> values, StringBuilder json, int level, boolean comma) {
        indent(json, level).append(quote(name)).append(": [");
        int index = 0;
        for (Enum<?> value : values) {
            if (index++ > 0) json.append(", ");
            json.append(quote(value.name()));
        }
        json.append("]").append(comma ? "," : "").append("\n");
    }

    private void writeModuleMap(ModuleMapDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"moduleMap\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        field(json, level + 1, "notes", document.notes(), true);
        indent(json, level + 1).append("\"modules\": [\n");
        for (int i = 0; i < document.modules().size(); i++) {
            writeModuleNode(document.modules().get(i), json, level + 2, i + 1 < document.modules().size());
        }
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"dependencies\": [\n");
        for (int i = 0; i < document.dependencies().size(); i++) {
            writeModuleDependency(document.dependencies().get(i), json, level + 2, i + 1 < document.dependencies().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeModuleNode(ModuleNode module, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", module.id(), true);
        field(json, level + 1, "displayName", module.displayName(), true);
        field(json, level + 1, "parentId", module.parentId(), true);
        field(json, level + 1, "kind", module.kind().name(), true);
        field(json, level + 1, "status", module.status().name(), true);
        field(json, level + 1, "responsibility", module.responsibility(), true);
        field(json, level + 1, "description", module.description(), true);
        writeStringArray("tags", module.tags(), json, level + 1, true);
        field(json, level + 1, "notes", module.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeModuleDependency(ModuleDependency dependency, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", dependency.id(), true);
        field(json, level + 1, "sourceModuleId", dependency.sourceModuleId(), true);
        field(json, level + 1, "targetModuleId", dependency.targetModuleId(), true);
        field(json, level + 1, "kind", dependency.kind().name(), true);
        field(json, level + 1, "description", dependency.description(), true);
        field(json, level + 1, "notes", dependency.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeUmlClassDiagram(UmlClassDiagramDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"umlClassDiagram\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        field(json, level + 1, "notes", document.notes(), true);
        indent(json, level + 1).append("\"modules\": [\n");
        for (int i = 0; i < document.modules().size(); i++) {
            writeUmlModule(document.modules().get(i), json, level + 2, i + 1 < document.modules().size());
        }
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"classes\": [\n");
        for (int i = 0; i < document.classes().size(); i++) {
            writeUmlClass(document.classes().get(i), json, level + 2, i + 1 < document.classes().size());
        }
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"relations\": [\n");
        for (int i = 0; i < document.relations().size(); i++) {
            writeUmlRelation(document.relations().get(i), json, level + 2, i + 1 < document.relations().size());
        }
        indent(json, level + 1).append("],\n");
        indent(json, level + 1).append("\"views\": [\n");
        for (int i = 0; i < document.views().size(); i++) {
            writeUmlClassDiagramView(document.views().get(i), json, level + 2, i + 1 < document.views().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeUmlClassDiagramView(UmlClassDiagramView view, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", view.id(), true);
        field(json, level + 1, "kind", view.kind().name(), true);
        field(json, level + 1, "displayName", view.displayName(), true);
        field(json, level + 1, "description", view.description(), true);
        writeStringArray("sourceRootIds", view.sourceRootIds(), json, level + 1, true);
        writeStringArray("moduleIds", view.moduleIds(), json, level + 1, true);
        writeStringArray("classIds", view.classIds(), json, level + 1, true);
        writeStringArray("relationIds", view.relationIds(), json, level + 1, true);
        field(json, level + 1, "notes", view.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeUmlModule(UmlModuleGroup module, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", module.id(), true);
        field(json, level + 1, "displayName", module.displayName(), true);
        field(json, level + 1, "path", module.path(), true);
        field(json, level + 1, "description", module.description(), true);
        field(json, level + 1, "notes", module.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeUmlClass(UmlClassNode node, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", node.id(), true);
        field(json, level + 1, "moduleId", node.moduleId(), true);
        field(json, level + 1, "displayName", node.displayName(), true);
        field(json, level + 1, "packageName", node.packageName(), true);
        field(json, level + 1, "kind", node.kind().name(), true);
        field(json, level + 1, "visibility", node.visibility().name(), true);
        field(json, level + 1, "responsibility", node.responsibility(), true);
        field(json, level + 1, "description", node.description(), true);
        field(json, level + 1, "notes", node.notes(), true);
        indent(json, level + 1).append("\"members\": [\n");
        for (int i = 0; i < node.members().size(); i++) {
            writeUmlMember(node.members().get(i), json, level + 2, i + 1 < node.members().size());
        }
        indent(json, level + 1).append("]\n");
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeUmlMember(UmlClassMember member, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", member.id(), true);
        field(json, level + 1, "kind", member.kind().name(), true);
        field(json, level + 1, "name", member.name(), true);
        field(json, level + 1, "type", member.type(), true);
        field(json, level + 1, "signature", member.signature(), true);
        field(json, level + 1, "visibility", member.visibility().name(), true);
        booleanField(json, level + 1, "staticMember", member.staticMember(), true);
        field(json, level + 1, "description", member.description(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeUmlRelation(UmlClassRelation relation, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", relation.id(), true);
        field(json, level + 1, "sourceClassId", relation.sourceClassId(), true);
        field(json, level + 1, "targetClassId", relation.targetClassId(), true);
        field(json, level + 1, "kind", relation.kind().name(), true);
        field(json, level + 1, "label", relation.label(), true);
        field(json, level + 1, "description", relation.description(), true);
        field(json, level + 1, "notes", relation.notes(), false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }




    private void writeStringArray(String name, java.util.List<String> values, StringBuilder json, int level, boolean comma) {
        indent(json, level).append(quote(name)).append(": [");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) json.append(", ");
            json.append(quote(values.get(i)));
        }
        json.append("]").append(comma ? "," : "").append("\n");
    }

    private void field(StringBuilder json, int level, String name, String value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(quote(value)).append(comma ? "," : "").append("\n");
    }

    private void numberField(StringBuilder json, int level, String name, double value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(formatNumber(value)).append(comma ? "," : "").append("\n");
    }

    private String formatNumber(double value) {
        if (Math.rint(value) == value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    private void booleanField(StringBuilder json, int level, String name, boolean value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(value).append(comma ? "," : "").append("\n");
    }

    private StringBuilder indent(StringBuilder json, int level) {
        return json.append("  ".repeat(Math.max(0, level)));
    }

    private String quote(String value) {
        return JsonStringEscaper.quote(value);
    }
}
