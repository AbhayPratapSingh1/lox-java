package com.lox;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AstPrinterTest {
    @Test
    @Description("Simple Equation")
    void simpleEquation() {
        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(45.67)));

        assertEquals("(* (- 123) (Group 45.67))",new AstPrinter().print(expression));
    }
}
