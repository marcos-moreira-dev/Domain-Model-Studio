package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.List;

/** Tipos oficiales UML estructurales, de comportamiento e interacción. */
public final class UmlDiagramTypeDefinitions {

    private UmlDiagramTypeDefinitions() {
    }

    public static List<DiagramTypeOfficialDefinition> all() {
        return List.of(
                DiagramTypeDefinitionFactory.available(DiagramTypeId.UML_CLASS, "UML Clases", DiagramCategoryId.UML_STRUCTURAL,
                        DiagramWorkspaceKind.UML_CLASS_DIAGRAM, DiagramCapabilityProfiles.umlClass(),
                        "Representa clases, atributos, métodos y relaciones estructurales agrupadas por módulo, carpeta o paquete.",
                        "uml-clases", "uml-clases-gramatica", "uml-class",
                        "uml_class_restaurante_minimo.md", "uens-uml-clases",
                        "UENS — UML clases dominio escolar", "uml_class_uens_gordito.md",
                        "Clases de secretaría, académico, calificaciones y seguridad con relaciones estructurales.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.UML_USE_CASE, "UML Casos de uso", DiagramCategoryId.UML_BEHAVIOR,
                        DiagramWorkspaceKind.BEHAVIOR_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa actores y funcionalidades observables del sistema.",
                        "uml-casos-uso", "uml-casos-uso-gramatica", "uml-use-case",
                        "uml_use_case_restaurante_minimo.md", "uens-uml-casos-uso",
                        "UENS — UML casos de uso sistema escolar", "uml_use_case_uens_gordito.md",
                        "Actores y funcionalidades observables del sistema administrativo escolar.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.UML_ACTIVITY, "UML Actividad", DiagramCategoryId.UML_BEHAVIOR,
                        DiagramWorkspaceKind.BEHAVIOR_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa flujos de acciones del sistema o del negocio.",
                        "uml-actividad", "uml-actividad-gramatica", "uml-activity",
                        "uml_activity_cierre_caja_minimo.md", "uens-uml-actividad-matricula",
                        "UENS — UML actividad asignar estudiante a sección", "uml_activity_registrar_matricula_uens_gordito.md",
                        "Actividad de Secretaría para guardar estudiante, representante y sección vigente sin inventar tabla matrícula.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.UML_SEQUENCE, "UML Secuencia", DiagramCategoryId.UML_INTERACTION,
                        DiagramWorkspaceKind.BEHAVIOR_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa mensajes entre actores, objetos, servicios o componentes en el tiempo.",
                        "uml-secuencia", "uml-secuencia-gramatica", "uml-sequence",
                        "uml_sequence_login_minimo.md", "uens-uml-secuencia-calificacion",
                        "UENS — UML secuencia registrar calificación", "uml_sequence_registrar_calificacion_uens_gordito.md",
                        "Interacción entre docente, pantalla, servicios, base de datos y auditoría al registrar notas.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.UML_STATE, "UML Estados", DiagramCategoryId.UML_BEHAVIOR,
                        DiagramWorkspaceKind.BEHAVIOR_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa estados y transiciones de una entidad, proceso o componente.",
                        "uml-estados", "uml-estados-gramatica", "uml-state",
                        "uml_state_orden_minimo.md", "uens-uml-estados-reporte",
                        "UENS — UML estados solicitud de reporte", "uml_state_matricula_uens_gordito.md",
                        "Estados reales de reporte_solicitud_queue: PENDIENTE, EN_PROCESO, COMPLETADA y ERROR.", true)
        );
    }
}
