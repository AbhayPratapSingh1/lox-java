package com.lox.utils;

public class Token {
    final int line;
    final String lexeme;
    final TokenType type;
    final Object literal;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.line = line;
        this.lexeme = lexeme;
        this.literal = literal;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
