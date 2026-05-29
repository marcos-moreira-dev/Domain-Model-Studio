package com.marcosmoreira.domainmodelstudio.infrastructure.sourcecode.typescript;

/** Token liviano usado por el parser TypeScript mínimo. */
record TypeScriptToken(String text, int index) {
    boolean is(String expected) {
        return text.equals(expected);
    }

    boolean isIdentifier() {
        if (text.isBlank()) {
            return false;
        }
        char first = text.charAt(0);
        return Character.isJavaIdentifierStart(first) || first == '_' || first == '$';
    }
}
