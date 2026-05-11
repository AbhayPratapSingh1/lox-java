package com.lox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void shouldGiveTheTokenDetailsOnDoingToString() {
        TokenType type = TokenType.DOT;
        String lexeme = "LEX";
        String literal = "LITERAL";
        int line = 1;
        Token token = new Token(type, lexeme, literal, line);
        assertEquals(type + " " + lexeme + " " + literal ,  token.toString() );
    }
}