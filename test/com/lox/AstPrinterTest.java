package com.lox;

import jdk.jfr.Description;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AstPrinterTest {
    @Test
    @Description("Simple Equation")
    void simpleEquation() {
        Expr expression = new Expr.Binary(new Expr.Unary(new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(123)), new Token(TokenType.STAR, "*", null, 1), new Expr.Grouping(new Expr.Literal(45.67)));
        assertEquals("(* (- 123) (Group 45.67))", new AstPrinter().print(expression));
    }

    @Test
    @Description("Addition: 2 + 3")
    void addition() {
        Expr expression = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(3));

        assertEquals("(+ 2 3)", new AstPrinter().print(expression));
    }

    @Test
    @Description("Subtraction: 2 - 3")
    void subtraction() {
        Expr expression = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(3));

        assertEquals("(- 2 3)", new AstPrinter().print(expression));
    }

    @Test
    @Description("Division: 2 / 4")
    void division() {
        Expr expression = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.SLASH, "/", null, 1), new Expr.Literal(4));

        assertEquals("(/ 2 4)", new AstPrinter().print(expression));
    }

    @Test
    @Description("Multiplication: 2 * 3")
    void multiplication() {
        Expr expression = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.STAR, "*", null, 1), new Expr.Literal(3));

        assertEquals("(* 2 3)", new AstPrinter().print(expression));
    }

    @Test
    @Description("Comparisons: Equality and Inequality")
    void comparisons() {
        // 2 == 3
        Expr eq = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.EQUAL_EQUAL, "==", null, 1), new Expr.Literal(3));
        assertEquals("(== 2 3)", new AstPrinter().print(eq));

        // 2 != 3
        Expr neq = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.BANG_EQUAL, "!=", null, 1), new Expr.Literal(3));
        assertEquals("(!= 2 3)", new AstPrinter().print(neq));

        // 2 > 3
        Expr gt = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.GREATER, ">", null, 1), new Expr.Literal(3));
        assertEquals("(> 2 3)", new AstPrinter().print(gt));

        // 2 < 3
        Expr lt = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.LESS, "<", null, 1), new Expr.Literal(3));
        assertEquals("(< 2 3)", new AstPrinter().print(lt));

        // 2 >= 3
        Expr gte = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.GREATER_EQUAL, ">=", null, 1), new Expr.Literal(3));
        assertEquals("(>= 2 3)", new AstPrinter().print(gte));

        // 2 <= 3
        Expr lte = new Expr.Binary(new Expr.Literal(2), new Token(TokenType.LESS_EQUAL, "<=", null, 1), new Expr.Literal(3));
        assertEquals("(<= 2 3)", new AstPrinter().print(lte));
    }

    @Nested
    @Description("Grouping and Precedence")
    class GroupingAndPrecedence {
        @Test
        @Description("(2 + 3) > 2")
        void groupedComparison() {
            Expr expression = new Expr.Binary(
                    new Expr.Grouping(
                            new Expr.Binary(new Expr.Literal(2),
                            new Token(TokenType.PLUS, "+", null, 1),
                            new Expr.Literal(3))),
                    new Token(TokenType.GREATER, ">", null, 1),
                    new Expr.Literal(2));
            assertEquals("(> (Group (+ 2 3)) 2)", new AstPrinter().print(expression));
        }

        @Test
        @Description("(2 + 3 * 4) == 3")
        void complexPrecedence() {
            Expr expression = new Expr.Binary(new Expr.Grouping(new Expr.Binary(new Expr.Literal(2), new Token(TokenType.PLUS, "+", null, 1), new Expr.Binary(new Expr.Literal(3), new Token(TokenType.STAR, "*", null, 1), new Expr.Literal(4)))), new Token(TokenType.EQUAL_EQUAL, "==", null, 1), new Expr.Literal(3));

            assertEquals("(== (Group (+ 2 (* 3 4))) 3)", new AstPrinter().print(expression));
        }

        @Test
        @Description("(1 + 2) == (3 + 4)")
        void dualGroups() {
            Expr expression = new Expr.Binary(new Expr.Grouping(new Expr.Binary(new Expr.Literal(1), new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(2))), new Token(TokenType.EQUAL_EQUAL, "==", null, 1), new Expr.Grouping(new Expr.Binary(new Expr.Literal(3), new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(4))));

            assertEquals("(== (Group (+ 1 2)) (Group (+ 3 4)))", new AstPrinter().print(expression));
        }
    }
}
