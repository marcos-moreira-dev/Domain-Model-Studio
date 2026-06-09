package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

/** Textos de ayuda breves pero útiles para las acciones visibles de toolbar. */
final class DiagramToolbarTooltipCatalog {

    private DiagramToolbarTooltipCatalog() {
    }

    static String describe(
            DiagramToolbarActionId id,
            String buttonText,
            String currentTooltip,
            DiagramToolbarSection section
    ) {
        String base = normalize(currentTooltip, fallbackTitle(buttonText));
        String detail = specificDetail(id);
        if (detail.isBlank()) {
            detail = sectionDetail(section);
        }
        if (base.endsWith(".") || base.endsWith("?") || base.endsWith("!")) {
            return base + " " + detail;
        }
        return base + ". " + detail;
    }

    private static String specificDetail(DiagramToolbarActionId id) {
        return switch (id) {
            case ADD_ENTITY -> "Úsalo para registrar un concepto principal del dominio antes de detallar sus atributos.";
            case ADD_ATTRIBUTE -> "Selecciona una entidad y agrega un dato que ayude a describirla en el modelo.";
            case ADD_RELATIONSHIP -> "Después de crearla, revisa cardinalidades y etiquetas para que la lectura sea clara.";
            case DUPLICATE_ELEMENT -> "Sirve para crear una variante rápida de un elemento ya configurado.";
            case DELETE_ELEMENT, REMOVE_DICTIONARY_ITEM, REMOVE_MODULE_MAP_ITEM, REMOVE_ARCHITECTURE_ITEM,
                    REMOVE_UML_ITEM, REMOVE_BEHAVIOR_ITEM, REMOVE_SCREEN_FLOW_ITEM, REMOVE_WIREFRAME_ITEM,
                    REMOVE_ROLES_PERMISSIONS_ITEM -> "Elimina solo lo seleccionado; revisa el panel lateral si necesitas confirmar qué elemento está activo.";
            case DELETE_SELECTED_BEND_POINT -> "Permite enderezar una relación cuando agregaste un vértice de más.";
            case VALIDATE_MODEL, VALIDATE_DICTIONARY, VALIDATE_MODULE_MAP, VALIDATE_UML_CLASS,
                    VALIDATE_ARCHITECTURE_DIAGRAM, VALIDATE_BEHAVIOR_DIAGRAM, VALIDATE_SCREEN_FLOW,
                    VALIDATE_WIREFRAME, VALIDATE_ROLES_PERMISSIONS -> "Úsalo antes de exportar o guardar una entrega para detectar inconsistencias visibles.";
            case REORGANIZE_DIAGRAM -> "Recalcula posiciones iniciales sin cambiar el significado de los elementos del modelo.";
            case SWITCH_TO_CHEN, SWITCH_TO_CROWS_FOOT -> "Cambia la forma de lectura del modelo conceptual sin alterar sus entidades ni relaciones.";
            case ZOOM_IN, ZOOM_OUT, RESET_ZOOM, FIT_TO_CONTENT, CENTER_DIAGRAM, CENTER_SELECTION -> "Afecta solo la vista del lienzo; no modifica el contenido ni el archivo guardado.";
            case EXPORT_SVG -> com.marcosmoreira.domainmodelstudio.presentation.exportable.SvgExportContract.TOOLTIP_DETAIL;
            case EXPORT_PNG -> "Genera una imagen rápida para revisar, compartir o insertar en documentos.";
            case EXPORT_MARKDOWN -> "Produce texto estructurado que puedes versionar, revisar o pasar a una IA como contexto.";
            case EXPORT_PDF -> "Genera un documento formal con el contenido completo del artefacto activo.";
            case EXPORT_DICTIONARY_PDF -> "Genera un documento formal del diccionario con secciones y tablas legibles.";
            case ADD_DICTIONARY_ENTITY -> "Crea una sección de datos para documentar campos, reglas y significado de negocio.";
            case ADD_DICTIONARY_FIELD -> "Agrega un campo a la entidad seleccionada con tipo, restricciones y observaciones.";
            case ADD_MODULE -> "Crea un módulo principal para ordenar responsabilidades funcionales del sistema.";
            case ADD_SUBMODULE -> "Agrega una pieza interna al módulo seleccionado para refinar el alcance.";
            case ADD_MODULE_DEPENDENCY -> "Relaciona módulos cuando uno necesita información, servicio o coordinación del otro.";
            case ADD_UML_MODULE -> "Agrupa clases por paquete, carpeta o responsabilidad y ayuda a leer diagramas grandes.";
            case ADD_UML_CLASS -> "Crea una clase con nombre, atributos, métodos y relaciones dentro del módulo activo.";
            case ADD_UML_INTERFACE -> "Úsala para representar contratos que estabilizan dependencias entre clases o módulos.";
            case ADD_UML_ENUM -> "Úsalo para modelar un conjunto cerrado de valores del diseño.";
            case ADD_UML_ATTRIBUTE -> "Añade un dato propio de la clase seleccionada.";
            case ADD_UML_METHOD -> "Añade una operación o responsabilidad visible de la clase seleccionada.";
            case ADD_UML_RELATION -> "Crea una relación estructural; luego ajusta multiplicidad, tipo y etiqueta.";
            case ADD_C4_PERSON -> "Representa un rol humano o actor externo que interactúa con el sistema.";
            case ADD_C4_SYSTEM, ADD_C4_EXTERNAL_SYSTEM -> "Representa un sistema dentro o fuera del límite de análisis.";
            case ADD_C4_CONTAINER, ADD_C4_APPLICATION, ADD_C4_API -> "Representa una pieza ejecutable o técnica dentro del sistema.";
            case ADD_C4_BOUNDARY -> "Agrupa elementos dentro de un límite de sistema o contexto.";
            case ADD_ARCHITECTURE_USES, ADD_ARCHITECTURE_DEPENDENCY, ADD_ARCHITECTURE_INTEGRATION, ADD_ARCHITECTURE_CALL,
                    ADD_ARCHITECTURE_READS_WRITES -> "Dibuja una relación arquitectónica y luego ajusta su etiqueta para que no se superponga.";
            case ADD_BPMN_START, ADD_UML_INITIAL_STATE -> "Marca el punto donde comienza el flujo o ciclo de vida.";
            case ADD_BPMN_ACTIVITY, ADD_UML_ACTION -> "Agrega una acción concreta que debe realizar alguien o el sistema.";
            case ADD_BPMN_DECISION, ADD_UML_DECISION -> "Agrega una decisión explícita para separar caminos del proceso.";
            case ADD_BPMN_END, ADD_UML_FINAL_STATE -> "Marca dónde termina el flujo o estado analizado.";
            case ADD_BPMN_LANE -> "Agrupa actividades por responsable o área del negocio.";
            case ADD_USE_CASE_ACTOR -> "Representa un rol externo que participa en el caso de uso.";
            case ADD_USE_CASE -> "Agrega una funcionalidad vista desde el objetivo del usuario o actor.";
            case ADD_USE_CASE_SYSTEM -> "Dibuja el límite del sistema para separar lo interno de lo externo.";
            case ADD_SEQUENCE_PARTICIPANT -> "Agrega una línea de vida que participa en la interacción temporal.";
            case ADD_SEQUENCE_MESSAGE, ADD_SEQUENCE_RETURN_MESSAGE -> "Agrega comunicación entre participantes; el orden vertical expresa el tiempo.";
            case ADD_SEQUENCE_ACTIVATION -> "Muestra el intervalo en que un participante está ejecutando una acción.";
            case ADD_SCREEN, ADD_SCREEN_TRANSITION -> "Úsalo para modelar navegación y acciones entre pantallas administrativas.";
            case ADD_WIREFRAME_SCREEN, ADD_WIREFRAME_SECTION, ADD_WIREFRAME_FORM, ADD_WIREFRAME_TABLE,
                    ADD_WIREFRAME_FIELD, ADD_WIREFRAME_BUTTON, APPLY_WIREFRAME_TEMPLATE ->
                    "Sirve para levantar estructura de interfaz sin comprometer todavía el diseño gráfico final.";
            case ADD_ROLE, ADD_PERMISSION, ADD_PERMISSION_ASSIGNMENT -> "Mantiene la matriz administrativa de acceso, responsabilidades y restricciones.";
            default -> "Úsalo dentro del contexto activo del proyecto para mantener la entrega ordenada y revisable.";
        };
    }

    private static String sectionDetail(DiagramToolbarSection section) {
        return switch (section) {
            case ELEMENTS -> "Agrega o modifica piezas visibles del artefacto activo.";
            case MODEL -> "Trabaja sobre reglas, relaciones o validaciones del modelo activo.";
            case NOTATION -> "Cambia la forma de representación sin cambiar el contenido conceptual.";
            case VIEW -> "Afecta únicamente cómo miras el lienzo en pantalla.";
            case EXPORT -> "Genera una salida para revisión, documentación o entrega.";
        };
    }

    private static String normalize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.strip();
    }

    private static String fallbackTitle(String buttonText) {
        if (buttonText == null || buttonText.isBlank()) {
            return "Acción de la barra de herramientas";
        }
        return buttonText.strip();
    }
}
