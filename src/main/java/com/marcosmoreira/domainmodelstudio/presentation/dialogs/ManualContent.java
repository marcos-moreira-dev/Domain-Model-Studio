package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramCategoryCatalog;
import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.theory.DefaultTheoryCatalog;
import com.marcosmoreira.domainmodelstudio.application.theory.TheoryTopicId;
import com.marcosmoreira.domainmodelstudio.application.theory.TheoryTopic;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategory;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Contenido canónico del centro de ayuda teórica integrado. */
final class ManualContent {

    private static final DefaultDiagramCategoryCatalog CATEGORY_CATALOG = new DefaultDiagramCategoryCatalog();
    private static final DefaultDiagramTypeRegistry TYPE_REGISTRY = new DefaultDiagramTypeRegistry();
    private static final DefaultTheoryCatalog THEORY_CATALOG = new DefaultTheoryCatalog();

    private ManualContent() {
    }

    static List<ManualCategory> categories() {
        List<ManualCategory> categories = new ArrayList<>();
        categories.add(introductionCategory());
        for (DiagramCategory category : CATEGORY_CATALOG.findAll()) {
            List<ManualSection> sections = TYPE_REGISTRY.findByCategory(category.id()).stream()
                    .map(ManualContent::sectionFromDescriptor)
                    .toList();
            if (!sections.isEmpty()) {
                categories.add(new ManualCategory(category.displayName(), category.purpose(), sections));
            }
        }
        return List.copyOf(categories);
    }

    static List<ManualSection> sections() {
        return categories().stream()
                .flatMap(category -> category.sections().stream())
                .toList();
    }

    static Optional<String> sectionTitleForDiagramType(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) {
            return Optional.empty();
        }
        return TYPE_REGISTRY.findById(diagramTypeId).map(DiagramTypeDescriptor::displayName);
    }

    private static ManualCategory introductionCategory() {
        return new ManualCategory(
                "Referencia académica de diagramas",
                "Centro de estudio teórico para comprender diagramas de levantamiento, modelado, arquitectura y aplicaciones administrativas.",
                introductionSections());
    }

    private static List<ManualSection> introductionSections() {
        List<ManualSection> sections = new ArrayList<>(List.of(
                        section("Cómo usar la guía académica", "Método de lectura recomendado para estudiar los temas sin confundir teoría, notación y uso práctico.",
                                block("Rol del centro de ayuda",
                                        "Este centro funciona como una referencia académica integrada: su propósito no es repetir botones de la interfaz, sino explicar la teoría que permite leer, construir y revisar cada artefacto de modelado.",
                                        "Cada tema responde qué problema resuelve el diagrama, qué elementos usa, cómo se interpreta y qué errores conviene evitar cuando se trabaja con un sistema real.",
                                        "La interfaz puede evolucionar con el tiempo, pero la teoría debe mantenerse como una guía estable para analizar procesos, datos, pantallas, arquitectura y responsabilidades."),
                                block("Orden sugerido de estudio",
                                        "Lee primero el propósito del diagrama para saber si realmente corresponde al problema que estás intentando aclarar.",
                                        "Luego revisa sus elementos principales y relaciones para no confundir notaciones; cada forma debe representar una idea concreta dentro de su diagrama.",
                                        "Finalmente contrasta casos especiales, cuándo usarlo, cuándo no usarlo y errores comunes antes de convertirlo en documento de entrega.")),
                        section("Mapa de familias de diagramas", "Panorama de los grupos de diagramas incluidos en Domain Model Studio y del problema que ayuda a resolver cada familia.",
                                block("Modelado y datos",
                                        "Modelo conceptual: identifica conceptos, relaciones y cardinalidades del dominio.",
                                        "Diccionario de datos: precisa campos, reglas, tipos lógicos, ejemplos y validaciones."),
                                block("Procesos y comportamiento",
                                        "BPMN y flujo operativo explican trabajo del negocio, pasos, decisiones y responsables.",
                                        "UML actividad, secuencia y estados explican comportamiento, interacción temporal y ciclos de vida."),
                                block("Arquitectura y aplicación administrativa",
                                        "C4 y despliegue técnico explican límites, contenedores, sistemas externos y ejecución física.",
                                        "Mapa de módulos, roles, flujo de pantallas y wireframes organizan alcance funcional e interfaz.")),
                        section("Lectura de figuras didácticas", "Criterios para interpretar los dibujos de la guía como apoyo académico, sin confundirlos con una notación completa ni con pantallas reales del programa.",
                                block("Principio de lectura",
                                        "Las figuras son esquemas didácticos simplificados. Sirven para estudiar una idea, comparar conceptos o reconocer una estructura típica.",
                                        "El significado exacto de una forma depende del capítulo: un rectángulo puede ser entidad, clase, pantalla, módulo, contenedor o nodo de despliegue según la teoría que se esté explicando.",
                                        "La leyenda, el título del capítulo y el texto cercano mandan sobre la forma aislada."),
                                block("Convenciones comunes",
                                        "Rectángulo: elemento principal del tema, como entidad, clase, pantalla, módulo, sistema, contenedor o nodo.",
                                        "Óvalo: atributo en Chen o caso de uso en UML, según el capítulo.",
                                        "Rombo: relación Chen, decisión UML o gateway BPMN, según la notación explicada.",
                                        "Línea o flecha: relación, navegación, flujo, mensaje, transición o dependencia, según el diagrama."),
                                block("Riesgos de interpretación",
                                        "No asumir que una figura de ayuda representa una implementación final del editor visual.",
                                        "No mezclar símbolos entre familias: BPMN, UML, C4, ER y wireframes usan formas parecidas con significados distintos.",
                                        "Usar las figuras como apoyo de estudio y confirmar siempre la regla en el texto del capítulo.")),
                        section("Glosario mínimo", "Vocabulario base para leer la guía sin confundir niveles de análisis.",
                                block("Conceptos generales",
                                        "Dominio: parte de la realidad del negocio que el sistema necesita comprender.",
                                        "Artefacto: documento, diagrama o modelo producido durante el análisis o diseño.",
                                        "Notación: conjunto de símbolos y reglas visuales para representar una idea.",
                                        "Abstracción: decisión de mostrar lo importante y ocultar detalles que todavía no corresponden."),
                                block("Niveles frecuentes",
                                        "Conceptual: significado del negocio, sin tecnología concreta.",
                                        "Lógico: organización más precisa de datos, clases, pantallas o responsabilidades.",
                                        "Físico o técnico: implementación real en base de datos, código, servidores o despliegue.",
                                        "Operativo: forma en que las personas trabajan, toman decisiones y completan procedimientos.")),
                        section("Checklist final de estudio", "Preguntas para cerrar cada capítulo antes de usarlo en un proyecto real.",
                                block("Antes de dar un diagrama por entendido",
                                        "Puedo decir qué pregunta responde y qué pregunta no responde.",
                                        "Puedo nombrar sus elementos principales sin mezclarlo con diagramas vecinos.",
                                        "Puedo leer el diagrama en frases del negocio o del sistema.",
                                        "Puedo detectar al menos tres errores comunes del capítulo.",
                                        "Puedo explicar qué otro diagrama conviene usar después."),
                                block("Antes de pedir ayuda a una IA",
                                        "Tengo claro el nivel de abstracción que quiero: conceptual, operativo, visual, arquitectónico o técnico.",
                                        "Puedo entregar contexto suficiente: negocio, módulos, datos, roles, procesos o estados.",
                                        "Puedo pedir revisión de límites: qué pertenece al diagrama y qué debe ir a otro artefacto.",
                                        "Puedo pedir una versión MVP y una versión futura sin mezclar ambas.")),
                        section("Referencias de estudio", "Base académica general que sostiene la guía sin convertirla en una bibliografía pesada.",
                                block("Fuentes conceptuales",
                                        "Modelo ER: tradición de modelado entidad-relación para entender datos y relaciones del dominio.",
                                        "UML: lenguaje unificado de modelado para casos de uso, clases, actividades, secuencias y estados.",
                                        "BPMN: notación de procesos de negocio para tareas, eventos, responsables y decisiones.",
                                        "C4: enfoque de arquitectura para contexto, contenedores y comunicación de alto nivel."),
                                block("Criterio de uso",
                                        "La guía adapta estas tradiciones a sistemas administrativos y aprendizaje práctico.",
                                        "Las figuras del visor son didácticas; cuando haya duda, manda la definición textual del capítulo.",
                                        "La teoría debe ayudar a tomar decisiones, no a decorar documentos con símbolos."))));
        THEORY_CATALOG.findById(TheoryTopicId.LOGICAL_BUSINESS_INTAKE)
                .map(topic -> ManualTheorySectionFactory.fromTheoryTopic(
                        "Levantamiento lógico",
                        "Fundamento académico para tratar entrevistas y procesos de negocio como estados, acciones, reglas, invariantes y cierres verificables.",
                        topic))
                .ifPresent(sections::add);
        return List.copyOf(sections);
    }

    private static ManualSection sectionFromDescriptor(DiagramTypeDescriptor descriptor) {
        List<ManualBlock> blocks = new ArrayList<>();
        Optional<TheoryTopic> theory = THEORY_CATALOG.findByDiagramType(descriptor.id());
        if (theory.isPresent()) {
            return ManualTheorySectionFactory.fromTheoryTopic(
                    descriptor.displayName(),
                    descriptor.shortDescription(),
                    theory.get());
        } else {
            blocks.add(block("Teoría pendiente",
                    "Este tipo está registrado, pero todavía no tiene tema académico cargado en la referencia académica."));
        }
        return new ManualSection(descriptor.displayName(), descriptor.shortDescription(), blocks);
    }

    private static ManualSection section(String title, String summary, ManualBlock... blocks) {
        return new ManualSection(title, summary, List.of(blocks));
    }

    private static ManualBlock block(String title, String... lines) {
        return new ManualBlock(title, List.of(lines));
    }

    private static ManualBlock diagramBlock(String title, List<String> lines, String... diagramLines) {
        return new ManualBlock(title, lines, List.of(diagramLines));
    }
}
