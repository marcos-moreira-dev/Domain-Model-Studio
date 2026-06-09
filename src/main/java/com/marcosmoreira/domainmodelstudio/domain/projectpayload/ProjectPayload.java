package com.marcosmoreira.domainmodelstudio.domain.projectpayload;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;

/**
 * Contrato mínimo de un payload de proyecto especializado.
 *
 * <p>Esta interfaz introduce el lenguaje común de payload sin cambiar todavía
 * la estructura pública de {@code DiagramProject} ni el formato externo .dms.
 * Las tandas posteriores podrán reemplazar gradualmente los campos opcionales
 * especializados por implementaciones registrables de este contrato.</p>
 */
public interface ProjectPayload {

    /** Tipo de proyecto al que pertenece este payload. */
    DiagramTypeId diagramTypeId();

    /** Identificador técnico estable del tipo de payload. */
    String payloadKind();
}
