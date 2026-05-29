package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

/** Contrato pequeño para preparar un tipo específico de Markdown compatible. */
interface LogicalBusinessDerivationWriter {

    LogicalBusinessDerivationTarget target();

    LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context);
}
