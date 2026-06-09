package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

/** Ayuda rápida de categorías del árbol del expediente lógico. */
final class LogicalBusinessTreeHelpGuide {

    private LogicalBusinessTreeHelpGuide() {
    }

    static String titleFor(LogicalBusinessTreeNode node) {
        return node == null ? "Ayuda del expediente" : node.label();
    }

    static String messageFor(LogicalBusinessTreeNode node) {
        if (node == null || node.selection() == null) {
            return "Selecciona una categoría para ver una explicación rápida de su función dentro del levantamiento lógico.";
        }
        if (!node.selection().kindIs(LogicalBusinessSelectionKind.GROUP)
                && !node.selection().kindIs(LogicalBusinessSelectionKind.SECTION)
                && !node.selection().kindIs(LogicalBusinessSelectionKind.DOCUMENT)
                && !node.selection().kindIs(LogicalBusinessSelectionKind.MATURITY)) {
            return "Este nodo representa un elemento concreto del expediente. La ficha central muestra su contenido y los módulos laterales muestran propiedades, validación, trazas internas y ayuda contextual.";
        }
        String id = node.selection().id();
        return switch (id) {
            case "portada-contexto" -> "Reúne los datos generales del expediente: nombre del proyecto, fuente principal, contexto observado y notas iniciales. Sirve para saber de dónde sale el levantamiento antes de revisar reglas o entidades.";
            case "secciones-canonicas" -> "Muestra el índice de la plantilla escrita en piedra. Es útil para comprobar que el Markdown importado conserva las secciones esperadas y que el documento no perdió estructura.";
            case "lenguaje-negocio" -> "Agrupa actores, conceptos y evidencias. Ayuda a fijar el vocabulario del cliente antes de identificar entidades candidatas o preparar otros artefactos revisables.";
            case "estados-datos" -> "Resume estados observables y datos del negocio. Aquí se revisa qué situaciones existen antes o después de una acción, por ejemplo orden creada, pagada o entregada.";
            case "reglas-condiciones" -> "Agrupa reglas, precondiciones, invariantes y postcondiciones. Sirve para revisar qué debe cumplirse antes, durante y después de una operación.";
            case "acciones-flujos" -> "Concentra acciones transformadoras, casos de uso, macroflujos y flujos. Describe cómo el negocio cambia de estado por operaciones concretas.";
            case "entidades-candidatas" -> "Muestra entidades, atributos y relaciones candidatas. Todavía no son tablas finales: son candidatos justificados por reglas, acciones y evidencia.";
            case "reportes-decisiones" -> "Agrupa reportes, consultas y decisiones esperadas. Sirve para detectar qué información necesita el negocio para operar o controlar resultados.";
            case "riesgos-preguntas" -> "Concentra dudas, supuestos y riesgos pendientes. Si algo aquí es crítico, conviene resolverlo antes de cerrar o reutilizar el expediente como fuente.";
            case "trazas-internas" -> "Permite revisar de dónde viene un elemento y qué otros elementos dependen de él dentro del mismo expediente. No sincroniza proyectos ni genera artefactos: es defensa lógica para revisar cambios o dudas.";
            default -> {
                if (node.selection().kindIs(LogicalBusinessSelectionKind.SECTION)) {
                    yield "Sección canónica del Markdown importado. Su función es preservar la estructura del levantamiento y ubicar los elementos vinculados a esa parte del documento.";
                }
                if (node.selection().kindIs(LogicalBusinessSelectionKind.DOCUMENT)) {
                    yield "Vista general del expediente lógico. Desde aquí se entiende el tamaño del levantamiento y se navega hacia categorías más específicas.";
                }
                if (node.selection().kindIs(LogicalBusinessSelectionKind.MATURITY)) {
                    yield "Resume si el expediente está suficientemente coherente para cerrar, revisar o reutilizar como fuente. Revisa fortalezas, bloqueos y próximos pasos antes de avanzar.";
                }
                yield "Categoría del expediente lógico. Agrupa elementos relacionados para evitar leer todo el Markdown como una pared de texto.";
            }
        };
    }
}
