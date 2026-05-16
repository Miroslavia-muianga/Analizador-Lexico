package org.analizador;

public enum TipoToken {

    //palavra reservada
    PALAVRA_RESERVADA,

    //CONSTANTES
    INTEGER_CONSTANT,
    CHARACTER_CONSTANT,

    //identificadores

    IDENTIFICADOR,

    //SIMBOLOS
    SIMBOLO_ESPECIAL,

    ERRO,
    EOF
}
