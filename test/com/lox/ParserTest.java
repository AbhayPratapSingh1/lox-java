//package com.lox;
//
//import jdk.jfr.Description;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//
//
//@Disabled
//public class ParserTest {
//    @Nested
//    @Description("Expression")
//    class Expression {
//        @Nested
//        @Description("Plus Operator +")
//        class PlusOperator {
//            @Test
//            @Description("Should able to parse simple expression")
//            void simpleTest() {
//                List<Token> tokens = new ArrayList<>();
//                tokens.add(new Token(TokenType.NUMBER, "10", 10.0, 1));
//                tokens.add(new Token(TokenType.PLUS, "+", null, 1));
//                tokens.add(new Token(TokenType.NUMBER, "10", 10.0, 1));
//                tokens.add(new Token(TokenType.EOF, "", null, 1));
//
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr exprectedExpr = new Expr.Binary(new Expr.Literal(10.0), new Token(TokenType.STAR, "+", null, 1), new Expr.Literal(10.0));
//
//                assertEquals(new AstPrinter().print(exprectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should parse unary minus on the left operand: -5 + 3")
//            void testUnaryLeftOperand() {
//                List<Token> tokens = tokensOf(op(TokenType.MINUS, "-"), num(5.0), op(TokenType.PLUS, "+"), num(3.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(5.0)), op(TokenType.PLUS, "+"), new Expr.Literal(3.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should parse unary minus inside grouping: 5 + (-3)")
//            void testUnaryRightOperandWithGrouping() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.PLUS, "+"), op(TokenType.LEFT_PAREN, "("), op(TokenType.MINUS, "-"), num(3.0), op(TokenType.RIGHT_PAREN, ")"));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Literal(5.0), op(TokenType.PLUS, "+"), new Expr.Grouping(new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(3.0))));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should parse nested unary operators on both sides: -5 + (-3)")
//            void testUnaryBothSides() {
//                List<Token> tokens = tokensOf(op(TokenType.MINUS, "-"), num(5.0), op(TokenType.PLUS, "+"), op(TokenType.LEFT_PAREN, "("), op(TokenType.MINUS, "-"), num(3.0), op(TokenType.RIGHT_PAREN, ")"));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(5.0)), op(TokenType.PLUS, "+"), new Expr.Grouping(new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(3.0))));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should handle operator precedence for trailing unary: 5 + -3")
//            void testTrailingUnaryWithoutGrouping() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.PLUS, "+"), op(TokenType.MINUS, "-"), num(3.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Literal(5.0), op(TokenType.PLUS, "+"), new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(3.0)));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should return null for invalid double plus: 5 ++ 3")
//            void testInvalidDoublePlus() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.PLUS, "+"), op(TokenType.PLUS, "+"), num(3.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                assertNull(expr);
//            }
//
//            @Test
//            @Description("Should return null for triple plus (invalid unary/binary mix): 1 +++ 2")
//            void testTriplePlus() {
//                List<Token> tokens = tokensOf(num(1.0), op(TokenType.PLUS, "+"), op(TokenType.PLUS, "+"), op(TokenType.PLUS, "+"), num(2.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                assertNull(expr);
//            }
//        }
//
//        @Nested
//        @Description("Minus Operator - Associativity")
//        class SubtractionPrecedenceTest {
//
//            @Test
//            @Description("Should parse unary leading minus followed by subtraction: -5 - 3")
//            void testUnaryAndSubtraction() {
//                List<Token> tokens = tokensOf(op(TokenType.MINUS, "-"), num(5.0), op(TokenType.MINUS, "-"), num(3.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(5.0)), op(TokenType.MINUS, "-"), new Expr.Literal(3.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should parse multiple subtractions with leading unary: -5 - 4 - 2")
//            void testMultipleSubtractionsWithUnary() {
//                List<Token> tokens = tokensOf(op(TokenType.MINUS, "-"), num(5.0), op(TokenType.MINUS, "-"), num(4.0), op(TokenType.MINUS, "-"), num(2.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                // Left-associativity: ((-5) - 4) - 2
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(5.0)), op(TokenType.MINUS, "-"), new Expr.Literal(4.0)), op(TokenType.MINUS, "-"), new Expr.Literal(2.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should parse complex mix of unary and binary minus: -5 - 4 - -3")
//            void testComplexUnaryBinaryMix() {
//                List<Token> tokens = tokensOf(op(TokenType.MINUS, "-"), num(5.0), op(TokenType.MINUS, "-"), num(4.0), op(TokenType.MINUS, "-"), op(TokenType.MINUS, "-"), num(3.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                // Left-associativity: ((-5) - 4) - (-3)
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(5.0)), op(TokenType.MINUS, "-"), new Expr.Literal(4.0)), op(TokenType.MINUS, "-"), new Expr.Unary(op(TokenType.MINUS, "-"), new Expr.Literal(3.0)));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//        }
//
//        @Nested
//        @Description("Multiplication Operator - Precedence and Nesting")
//        class MultiplicationPrecedenceTest {
//            @Test
//            @Description("Should parse simple multiplication: 5 * 4")
//            void testSimpleMultiplication() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.STAR, "*"), num(4.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Literal(5.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should prioritize multiplication over addition: 5 * 4 + 2")
//            void testMultiplicationBeforeAddition() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.STAR, "*"), num(4.0), op(TokenType.PLUS, "+"), num(2.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Literal(5.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0)), op(TokenType.PLUS, "+"), new Expr.Literal(2.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should prioritize multiplication even when it appears second: 5 + 2 * 4")
//            void testAdditionBeforeMultiplication() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.PLUS, "+"), num(2.0), op(TokenType.STAR, "*"), num(4.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Literal(5.0), op(TokenType.PLUS, "+"), new Expr.Binary(new Expr.Literal(2.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0)));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should handle mixed precedence with subtraction: 5 + 2 * 4 - 2")
//            void testMixedPrecedence() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.PLUS, "+"), num(2.0), op(TokenType.STAR, "*"), num(4.0), op(TokenType.MINUS, "-"), num(2.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Literal(5.0), op(TokenType.PLUS, "+"), new Expr.Binary(new Expr.Literal(2.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0))), op(TokenType.MINUS, "-"), new Expr.Literal(2.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should prioritize grouping over multiplication: 5 + 2 * (4 - 2)")
//            void testGroupingPrecedence() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.PLUS, "+"), num(2.0), op(TokenType.STAR, "*"), op(TokenType.LEFT_PAREN, "("), num(4.0), op(TokenType.MINUS, "-"), num(2.0), op(TokenType.RIGHT_PAREN, ")"));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Literal(5.0), op(TokenType.PLUS, "+"), new Expr.Binary(new Expr.Literal(2.0), op(TokenType.STAR, "*"), new Expr.Grouping(new Expr.Binary(new Expr.Literal(4.0), op(TokenType.MINUS, "-"), new Expr.Literal(2.0)))));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should handle left-associative multiplication before addition: 5 * 4 * 2 + 4")
//            void testMultipleMultiplication() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.STAR, "*"), num(4.0), op(TokenType.STAR, "*"), num(2.0), op(TokenType.PLUS, "+"), num(4.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Binary(new Expr.Literal(5.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0)), op(TokenType.STAR, "*"), new Expr.Literal(2.0)), op(TokenType.PLUS, "+"), new Expr.Literal(4.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should handle nested grouping: 5 * ((4 * 2) + 4)")
//            void testNestedGrouping() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.STAR, "*"), op(TokenType.LEFT_PAREN, "("), op(TokenType.LEFT_PAREN, "("), num(4.0), op(TokenType.STAR, "*"), num(2.0), op(TokenType.RIGHT_PAREN, ")"), op(TokenType.PLUS, "+"), num(4.0), op(TokenType.RIGHT_PAREN, ")"));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Literal(5.0), op(TokenType.STAR, "*"), new Expr.Grouping(new Expr.Binary(new Expr.Grouping(new Expr.Binary(new Expr.Literal(4.0), op(TokenType.STAR, "*"), new Expr.Literal(2.0))), op(TokenType.PLUS, "+"), new Expr.Literal(4.0))));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should return null for invalid binary plus as unary: 1 * + 2")
//            void testInvalidPlusAsUnary() {
//                List<Token> tokens = tokensOf(num(1.0), op(TokenType.STAR, "*"), op(TokenType.PLUS, "+"), num(2.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                assertNull(expr);
//            }
//
//            @Test
//            @Description("Should return null for consecutive binary operators: 1 + * 2")
//            void testConsecutiveBinaryOperators() {
//                List<Token> tokens = tokensOf(num(1.0), op(TokenType.PLUS, "+"), op(TokenType.STAR, "*"), num(2.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                assertNull(expr);
//            }
//
//        }
//
//        @Nested
//        @Description("Division Operator - Precedence and Left-Associativity")
//        class DivisionPrecedenceTest {
//            @Test
//            @Description("Should parse simple division: 5 / 2")
//            void testSimpleDivision() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.SLASH, "/"), num(2.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(new Expr.Literal(5.0), op(TokenType.SLASH, "/"), new Expr.Literal(2.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should respect left-associativity with mixed multiplication: 5 / 4 * 3")
//            void testDivisionMultiplicationAssociativity() {
//                List<Token> tokens = tokensOf(num(5.0), op(TokenType.SLASH, "/"), num(4.0), op(TokenType.STAR, "*"), num(3.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                // (5 / 4) * 3
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Literal(5.0), op(TokenType.SLASH, "/"), new Expr.Literal(4.0)), op(TokenType.STAR, "*"), new Expr.Literal(3.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should respect left-associativity when division is second: 3 * 4 / 5")
//            void testMultiplicationDivisionAssociativity() {
//                List<Token> tokens = tokensOf(num(3.0), op(TokenType.STAR, "*"), num(4.0), op(TokenType.SLASH, "/"), num(5.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                // (3 * 4) / 5
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Literal(3.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0)), op(TokenType.SLASH, "/"), new Expr.Literal(5.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should handle multiple multiplication/division left-to-right: 3 * 4 / 5 * 6")
//            void testLongMultiplicationDivisionChain() {
//                List<Token> tokens = tokensOf(num(3.0), op(TokenType.STAR, "*"), num(4.0), op(TokenType.SLASH, "/"), num(5.0), op(TokenType.STAR, "*"), num(6.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                // ((3 * 4) / 5) * 6
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Binary(new Expr.Literal(3.0), op(TokenType.STAR, "*"), new Expr.Literal(4.0)), op(TokenType.SLASH, "/"), new Expr.Literal(5.0)), op(TokenType.STAR, "*"), new Expr.Literal(6.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should handle multiple divisions and multiplications left-to-right: 6 / 5 * 2 / 3")
//            void testLongDivisionMultiplicationChain() {
//                List<Token> tokens = tokensOf(num(6.0), op(TokenType.SLASH, "/"), num(5.0), op(TokenType.STAR, "*"), num(2.0), op(TokenType.SLASH, "/"), num(3.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                // ((6 / 5) * 2) / 3
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Binary(new Expr.Literal(6.0), op(TokenType.SLASH, "/"), new Expr.Literal(5.0)), op(TokenType.STAR, "*"), new Expr.Literal(2.0)), op(TokenType.SLASH, "/"), new Expr.Literal(3.0));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should parse deep nesting and mixed precedence: 1 * (2 + 3) / ((4 - 3 / 2) - 3)")
//            void testComplexNestedExpression() {
//                List<Token> tokens = tokensOf(num(1.0), op(TokenType.STAR, "*"), op(TokenType.LEFT_PAREN, "("), num(2.0), op(TokenType.PLUS, "+"), num(3.0), op(TokenType.RIGHT_PAREN, ")"), op(TokenType.SLASH, "/"), op(TokenType.LEFT_PAREN, "("), op(TokenType.LEFT_PAREN, "("), num(4.0), op(TokenType.MINUS, "-"), num(3.0), op(TokenType.SLASH, "/"), num(2.0), op(TokenType.RIGHT_PAREN, ")"), op(TokenType.MINUS, "-"), num(3.0), op(TokenType.RIGHT_PAREN, ")"));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//// Structure: (1 * (2 + 3)) / ((4 - (3 / 2)) - 3)
//                Expr expectedExpr = new Expr.Binary(new Expr.Binary(new Expr.Literal(1.0), op(TokenType.STAR, "*"), new Expr.Grouping(new Expr.Binary(new Expr.Literal(2.0), op(TokenType.PLUS, "+"), new Expr.Literal(3.0)))), op(TokenType.SLASH, "/"), new Expr.Grouping(new Expr.Binary(new Expr.Grouping(new Expr.Binary(new Expr.Literal(4.0), op(TokenType.MINUS, "-"), new Expr.Binary(new Expr.Literal(3.0), op(TokenType.SLASH, "/"), new Expr.Literal(2.0)))), op(TokenType.MINUS, "-"), new Expr.Literal(3.0))));
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//        }
//
//
//        @Nested
//        @Description("Comparison Operators - Precedence and Syntax")
//        class ComparisonPrecedenceTest {
//
//            @Test
//            @Description("Should parse simple comparison: 1 > 1")
//            void testSimpleComparison() {
//                List<Token> tokens = tokensOf(num(1.0), op(TokenType.GREATER, ">"), num(1.0));
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(
//                        new Expr.Literal(1.0),
//                        op(TokenType.GREATER, ">"),
//                        new Expr.Literal(1.0)
//                );
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should prioritize addition over comparison: 1 + 2 >= 3")
//            void testComparisonPrecedence() {
//                List<Token> tokens = tokensOf(
//                        num(1.0), op(TokenType.PLUS, "+"), num(2.0),
//                        op(TokenType.GREATER_EQUAL, ">="), num(3.0)
//                );
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                // (1 + 2) >= 3
//                Expr expectedExpr = new Expr.Binary(
//                        new Expr.Binary(new Expr.Literal(1.0), op(TokenType.PLUS, "+"), new Expr.Literal(2.0)),
//                        op(TokenType.GREATER_EQUAL, ">="),
//                        new Expr.Literal(3.0)
//                );
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should parse comparison inside grouping: 1 + (3 <= 3)")
//            void testComparisonInGrouping() {
//                List<Token> tokens = tokensOf(
//                        num(1.0), op(TokenType.PLUS, "+"),
//                        op(TokenType.LEFT_PAREN, "("), num(3.0), op(TokenType.LESS_EQUAL, "<="), num(3.0), op(TokenType.RIGHT_PAREN, ")")
//                );
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                Expr expectedExpr = new Expr.Binary(
//                        new Expr.Literal(1.0),
//                        op(TokenType.PLUS, "+"),
//                        new Expr.Grouping(
//                                new Expr.Binary(new Expr.Literal(3.0), op(TokenType.LESS_EQUAL, "<="), new Expr.Literal(3.0))
//                        )
//                );
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//
//            @Test
//            @Description("Should return null for invalid operator sequence: 5 == / 3")
//            void testInvalidOperatorSequence() {
//                List<Token> tokens = tokensOf(
//                        num(5.0),
//                        op(TokenType.EQUAL_EQUAL, "=="),
//                        op(TokenType.SLASH, "/"),
//                        num(3.0)
//                );
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                assertNull(expr);
//            }
//
//            @Test
//            @Description("Should prioritize addition on the right side of comparison: 1 > 2 + 3")
//            void testComparisonRightSidePrecedence() {
//                List<Token> tokens = tokensOf(
//                        num(1.0), op(TokenType.GREATER, ">"), num(2.0), op(TokenType.PLUS, "+"), num(3.0)
//                );
//
//                Parser parser = new Parser(tokens);
//                Expr expr = parser.parse();
//
//                // 1 > (2 + 3)
//                Expr expectedExpr = new Expr.Binary(
//                        new Expr.Literal(1.0),
//                        op(TokenType.GREATER, ">"),
//                        new Expr.Binary(new Expr.Literal(2.0), op(TokenType.PLUS, "+"), new Expr.Literal(3.0))
//                );
//
//                assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//            }
//        }
//
//        @Test
//        @Description("Should prioritize unary negation over equality: ! 5 == 3")
//        void testUnaryNegationPrecedenceWithEquality() {
//            List<Token> tokens = tokensOf(
//                    op(TokenType.BANG, "!"),
//                    num(5.0),
//                    op(TokenType.EQUAL_EQUAL, "=="),
//                    num(3.0)
//            );
//
//            Parser parser = new Parser(tokens);
//            Expr expr = parser.parse();
//
//            // (!5) == 3
//            Expr expectedExpr = new Expr.Binary(
//                    new Expr.Unary(op(TokenType.BANG, "!"), new Expr.Literal(5.0)),
//                    op(TokenType.EQUAL_EQUAL, "=="),
//                    new Expr.Literal(3.0)
//            );
//
//            assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//        }
//
//        @Test
//        @Description("Should prioritize unary negation and addition over comparison: !3 < 4 + 2")
//        void testUnaryNegationAndAdditionPrecedence() {
//            List<Token> tokens = tokensOf(
//                    op(TokenType.BANG, "!"),
//                    num(3.0),
//                    op(TokenType.LESS, "<"),
//                    num(4.0),
//                    op(TokenType.PLUS, "+"),
//                    num(2.0)
//            );
//
//            Parser parser = new Parser(tokens);
//            Expr expr = parser.parse();
//
//            // (!3) < (4 + 2)
//            Expr expectedExpr = new Expr.Binary(
//                    new Expr.Unary(op(TokenType.BANG, "!"), new Expr.Literal(3.0)),
//                    op(TokenType.LESS, "<"),
//                    new Expr.Binary(new Expr.Literal(4.0), op(TokenType.PLUS, "+"), new Expr.Literal(2.0))
//            );
//
//            assertEquals(new AstPrinter().print(expectedExpr), new AstPrinter().print(expr));
//        }
//    }
//
//
//    private List<Token> tokensOf(Token... tokens) {
//        List<Token> list = new ArrayList<>(Arrays.asList(tokens));
//        list.add(new Token(TokenType.EOF, "", null, 1));
//        return list;
//    }
//
//    private Token num(double value) {
//        return new Token(TokenType.NUMBER, String.valueOf(value), value, 1);
//    }
//
//    private Token op(TokenType type, String lexeme) {
//        return new Token(type, lexeme, null, 1);
//    }
//}
