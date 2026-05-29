package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassFullDetailFormatterTest {

    @Test
    void formatsCompleteMemberListWithoutTruncatingCanvasHiddenMembers() {
        List<UmlClassMember> members = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            members.add(new UmlClassMember(
                    "metodo_" + i,
                    UmlMemberKind.METHOD,
                    "metodoLargoDeNegocioNumero" + i,
                    "ResultadoComplejo" + i,
                    "metodoLargoDeNegocioNumero" + i + "(ParametroMuyLargo parametro" + i + ")",
                    UmlVisibility.PUBLIC,
                    i % 2 == 0,
                    "Regla importada número " + i
            ));
        }
        members.add(new UmlClassMember("attr_1", UmlMemberKind.ATTRIBUTE, "codigoInterno", "String",
                "", UmlVisibility.PRIVATE, false, "Identificador interno"));

        UmlClassNode node = new UmlClassNode(
                "pedido_service",
                "backend",
                "PedidoServiceConNombreExtremadamenteLargoParaProbarDetalleCompleto",
                "com.example.pedidos",
                UmlClassKind.SERVICE,
                UmlVisibility.PUBLIC,
                "Coordina reglas de pedido",
                "ruta absoluta: C:/repo/src/main/java/com/example/pedidos/PedidoService.java",
                members,
                "Source root path: C:/repo/src/main/java"
        );

        String detail = new UmlClassFullDetailFormatter().format(node);

        assertTrue(detail.contains("33 total"));
        assertTrue(detail.contains("32 métodos"));
        assertTrue(detail.contains("metodoLargoDeNegocioNumero32(ParametroMuyLargo parametro32)"));
        assertTrue(detail.contains("Regla importada número 32"));
        assertTrue(detail.contains("ruta absoluta: C:/repo/src/main/java/com/example/pedidos/PedidoService.java"));
    }
}
