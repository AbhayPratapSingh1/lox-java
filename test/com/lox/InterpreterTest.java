package com.lox;

import jdk.jfr.Description;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterTest {
    @Test
    @Description("Simple test")
    void simple() {
        Expr expression = new Expr.Binary(
                new Expr.Literal(2.0),
                new Token(TokenType.PLUS, "+", null, 1),
                new Expr.Literal(3.0)
        );

        System.out.println(expression);
        assertEquals("5", new Interpreter().interpret(expression));
    }

    @Nested
    @Description("Arithmetic Test")
    class Arithmetic{
        @Test
        @Description("2 + 3")
        void addition() {
            Expr expression = new Expr.Binary(new Expr.Literal(2.0), op(TokenType.PLUS, "+"), new Expr.Literal(3.0));
            assertEquals("5", new Interpreter().interpret(expression));
        }

        @Test
        @Description("2 - 3")
        void subtraction() {
            Expr expression = new Expr.Binary(new Expr.Literal(2.0), op(TokenType.MINUS, "-"), new Expr.Literal(3.0));
            assertEquals("-1", new Interpreter().interpret(expression));
        }

        @Test
        @Description("2 / 4")
        void division() {
            Expr expression = new Expr.Binary(new Expr.Literal(2.0), op(TokenType.SLASH, "/"), new Expr.Literal(4.0));
            assertEquals("0.5", new Interpreter().interpret(expression));
        }

        @Test
        @Description("2 * 3")
        void multiplication() {
            Expr expression = new Expr.Binary(new Expr.Literal(2.0), op(TokenType.STAR, "*"), new Expr.Literal(3.0));
            assertEquals("6", new Interpreter().interpret(expression));
        }
    }



    @Nested
    @Description("Comparison Test")
    class Comparison {
        @Test
        @Description("2 > 3")
        void greaterThan() {
            Expr expression = new Expr.Binary(new Expr.Literal(2.0), op(TokenType.GREATER, ">"), new Expr.Literal(3.0));
            assertEquals("false", new Interpreter().interpret(expression));
        }

        @Test
        @Description("2 == 3")
        void equality() {
            Expr expression = new Expr.Binary(new Expr.Literal(2.0), op(TokenType.EQUAL_EQUAL, "=="), new Expr.Literal(3.0));
            assertEquals("false", new Interpreter().interpret(expression));
        }

        @Test
        @Description("2 != 3")
        void inequality() {
            Expr expression = new Expr.Binary(new Expr.Literal(2.0), op(TokenType.BANG_EQUAL, "!="), new Expr.Literal(3.0));
            assertEquals("true", new Interpreter().interpret(expression));
        }

        @Test
        @Description("2 <= 3")
        void lessThanEqual() {
            Expr expression = new Expr.Binary(new Expr.Literal(2.0), op(TokenType.LESS_EQUAL, "<="), new Expr.Literal(3.0));
            assertEquals("true", new Interpreter().interpret(expression));
        }
    }

    @Nested
    @Description("Complex Expression")
    class ComplexExpression{
        @Test
        @Description("(2 + 3) > 2")
        void groupedComparison() {
            Expr expression = new Expr.Binary(
                    new Expr.Grouping(new Expr.Binary(new Expr.Literal(2.0), op(TokenType.PLUS, "+"), new Expr.Literal(3.0))),
                    op(TokenType.GREATER, ">"),
                    new Expr.Literal(2.0));
            assertEquals("true", new Interpreter().interpret(expression));
        }

        @Test
        @Description("(2 + 3 * 4) == 3")
        void complexPrecedence() {
            // 2 + (3 * 4) = 14
            Expr expression = new Expr.Binary(
                    new Expr.Grouping(
                            new Expr.Binary(
                                    new Expr.Literal(2.0),
                                    op(TokenType.PLUS, "+"),
                                    new Expr.Binary(new Expr.Literal(3.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0))
                            )
                    ),
                    op(TokenType.EQUAL_EQUAL, "=="),
                    new Expr.Literal(3.0));
            assertEquals("false", new Interpreter().interpret(expression));
        }

        @Test
        @Description("(2 + 3 * 4) == 3 + 3 (Wait, 14 == 6)")
        void complexEquality() {
            Expr expression = new Expr.Binary(
                    new Expr.Grouping(
                            new Expr.Binary(
                                    new Expr.Literal(2.0),
                                    op(TokenType.PLUS, "+"),
                                    new Expr.Binary(new Expr.Literal(3.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0))
                            )
                    ),
                    op(TokenType.EQUAL_EQUAL, "=="),
                    new Expr.Binary(new Expr.Literal(3.0), op(TokenType.PLUS, "+"), new Expr.Literal(3.0))
            );
            assertEquals("false", new Interpreter().interpret(expression));
        }

        @Test
        @Description("(1 + 2) == (3 + 4) (3 == 7)")
        void dualGroups() {
            Expr expression = new Expr.Binary(
                    new Expr.Grouping(new Expr.Binary(new Expr.Literal(1.0), op(TokenType.PLUS, "+"), new Expr.Literal(2.0))),
                    op(TokenType.EQUAL_EQUAL, "=="),
                    new Expr.Grouping(new Expr.Binary(new Expr.Literal(3.0), op(TokenType.PLUS, "+"), new Expr.Literal(4.0)))
            );
            assertEquals("false", new Interpreter().interpret(expression));
        }
    }


    private List<Token> tokensOf(Token... tokens) {
        List<Token> list = new ArrayList<>(Arrays.asList(tokens));
        list.add(new Token(TokenType.EOF, "", null, 1));
        return list;
    }

    private Token num(double value) {
        return new Token(TokenType.NUMBER, String.valueOf(value), value, 1);
    }

    private Token op(TokenType type, String lexeme) {
        return new Token(type, lexeme, null, 1);
    }
}

