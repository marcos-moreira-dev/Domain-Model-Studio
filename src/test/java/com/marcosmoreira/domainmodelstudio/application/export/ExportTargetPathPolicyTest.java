package com.marcosmoreira.domainmodelstudio.application.export;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ExportTargetPathPolicyTest {

    private final ExportTargetPathPolicy policy = new ExportTargetPathPolicy();

    @Test
    void keepsExistingKnownExtensions() {
        assertEquals(Path.of("diagrama.svg"), policy.ensureSvgExtension(Path.of("diagrama.svg")));
        assertEquals(Path.of("reporte.PDF"), policy.ensurePdfExtension(Path.of("reporte.PDF")));
        assertEquals(Path.of("modelo.markdown"), policy.ensureMarkdownExtension(Path.of("modelo.markdown")));
    }

    @Test
    void addsExpectedExtensionWhenUserOmitsIt() {
        assertEquals(Path.of("diagrama.svg"), policy.ensureSvgExtension(Path.of("diagrama")));
        assertEquals(Path.of("captura.png"), policy.ensurePngExtension(Path.of("captura")));
        assertEquals(Path.of("documento.pdf"), policy.ensurePdfExtension(Path.of("documento")));
        assertEquals(Path.of("modelo.md"), policy.ensureMarkdownExtension(Path.of("modelo")));
    }
}
