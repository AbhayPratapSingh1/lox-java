package com.lox;

import jdk.jfr.Description;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScannerTest {

    @Test
    @Description("should tokenize a single identifier and append EOF")
    void simpleIdentifier() {
        Scanner scanner = new Scanner("hello");
        List<Token> tokens = scanner.scanTokens();
        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Token(TokenType.IDENTIFIER, "hello", null, 1));
        expectedTokens.add(new Token(TokenType.EOF, "", null, 1));
        assertEquals(expectedTokens, tokens);
    }

    @Test
    @Description("should tokenize a simple assignment statement with identifier, number, and semicolon")
    void simpleEquation() {
        Scanner scanner = new Scanner("a = 10;");
        List<Token> tokens = scanner.scanTokens();
        List<Token> expectedTokens = new ArrayList<>();

        expectedTokens.add(new Token(TokenType.IDENTIFIER, "a", null, 1));
        expectedTokens.add(new Token(TokenType.EQUAL, "=", null, 1));
        expectedTokens.add(new Token(TokenType.NUMBER, "10", 10.0, 1));
        expectedTokens.add(new Token(TokenType.SEMICOLON, ";", null, 1));
        expectedTokens.add(new Token(TokenType.EOF, "", null, 1));
        assertEquals(expectedTokens, tokens);
    }


    @Nested
    @Description("COMMENTS")
    class Comments {
        @Test
        @Description("should ignore entire input when it is a single-line comment")
        void entierCommented() {
            Scanner scanner = new Scanner("// hello");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should tokenize code before a single-line comment and ignore the rest")
        void simpleEquationWithComments() {
            Scanner scanner = new Scanner("var // hello");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should ignore entire input when it is a closed multiline comment")
        void multilineEntierCommented() {
            Scanner scanner = new Scanner("/* hello */");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should tokenize code before a closed multiline comment and ignore the comment")
        void multilineSimpleEquationWithComments() {
            Scanner scanner = new Scanner("var /* hello */");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should handle unterminated multiline comment as EOF when it is the entire input")
        void multilineEntierCommentedNotTerminated() {
            Scanner scanner = new Scanner("/* hello ");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should tokenize code before an unterminated multiline comment and ignore the rest")
        void multilineSimpleEquationWithCommentsNotTerminated() {
            Scanner scanner = new Scanner("var /* hello ");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should correctly track line numbers for multiline comment spanning multiple lines")
        void multilineEntierCommentedMultipleLines() {
            Scanner scanner = new Scanner("/* hello\n hello world */");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();
            expectedTokens.add(new Token(TokenType.EOF, "", null, 2));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should update line number correctly after multiline comment following code")
        void multilineBlockCommentAfterVar() {
            Scanner scanner = new Scanner("var /* hello\nworld */");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 2));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should return EOF with correct line count for unterminated multiline comment")
        void unterminatedMultilineCommentOnly() {
            Scanner scanner = new Scanner("/* hello\nworld ");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();
            expectedTokens.add(new Token(TokenType.EOF, "", null, 2));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should tokenize code before unterminated multiline comment and track line numbers")
        void unterminatedMultilineCommentAfterVar() {
            Scanner scanner = new Scanner("var /* hello\nworld ");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 2));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should resume tokenization after multiline comment and assign correct line number")
        void multilineCommentThenCode() {
            Scanner scanner = new Scanner("/* hello\nworld\n */ var");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 3));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 3));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should correctly tokenize code before and after multiline comment across lines")
        void multilineCommentBetweenCode() {
            Scanner scanner = new Scanner("var /* hello\nworld */ var");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.VAR, "var", null, 2));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 2));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should ignore all code after an unterminated multiline comment")
        void unterminatedMultilineCommentThenCode() {
            Scanner scanner = new Scanner("/* hello\nworld  var");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();
            expectedTokens.add(new Token(TokenType.EOF, "", null, 2));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should tokenize code before unterminated multiline comment and ignore subsequent tokens")
        void unterminatedMultilineCommentBetweenCode() {
            Scanner scanner = new Scanner("var /* hello\nworld  var");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 2));
            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should tokenize variable between two single-line comments on different lines")
        void varBetweenSingleLineComments() {
            Scanner scanner = new Scanner("// first comment\nvar\n// second comment");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 2));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 3));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should tokenize variable between two multiline comments across lines")
        void varBetweenMultilineComments() {
            Scanner scanner = new Scanner("/* first */ var /* second */");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should correctly track line numbers when variable is between multiline comments spanning lines")
        void varBetweenMultilineCommentsWithNewLines() {
            Scanner scanner = new Scanner("/* first\ncomment */\nvar\n/* second\ncomment */");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 3));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 5));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should ignore single-line and multiline comments before code and tokenize correctly")
        void mixedCommentsBeforeCode() {
            Scanner scanner = new Scanner("// first\n/* second */\nvar");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 3));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 3));

            assertEquals(expectedTokens, tokens);
        }


        @Test
        @Description("should tokenize code between single-line and multiline comments")
        void mixedCommentsAroundCode() {
            Scanner scanner = new Scanner("// first\nvar\n/* second */");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 2));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 3));

            assertEquals(expectedTokens, tokens);
        }


        @Test
        @Description("should handle mixture of single-line and multiline comments with declaration between them")
        void mixedCommentsWithDeclarationBetween() {
            Scanner scanner = new Scanner("/* first */ var // second");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }


        @Test
        @Description("should stop processing after unterminated multiline comment even if single-line comment appears later")
        void mixedCommentsWithUnterminatedMultiline() {
            Scanner scanner = new Scanner("// first\nvar\n/* second\n// third");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 2));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 4));

            assertEquals(expectedTokens, tokens);
        }
    }

    @Nested
    @Description("For Symbol Identifiers")
    class ScannerSingleTokenTest {

        @Test
        @Description("should identify LEFT_PAREN token")
        void leftParen() {
            Scanner scanner = new Scanner("(");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.LEFT_PAREN, "(", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify RIGHT_PAREN token")
        void rightParen() {
            Scanner scanner = new Scanner(")");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.RIGHT_PAREN, ")", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify LEFT_BRACE token")
        void leftBrace() {
            Scanner scanner = new Scanner("{");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.LEFT_BRACE, "{", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify RIGHT_BRACE token")
        void rightBrace() {
            Scanner scanner = new Scanner("}");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.RIGHT_BRACE, "}", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify COMMA token")
        void comma() {
            Scanner scanner = new Scanner(",");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.COMMA, ",", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify DOT token")
        void dot() {
            Scanner scanner = new Scanner(".");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.DOT, ".", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify SLASH token")
        void slash() {
            Scanner scanner = new Scanner("/");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.SLASH, "/", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify UNDERSCORE token")
        void underscore() {
            Scanner scanner = new Scanner("_");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.UNDERSCORE, "_", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify SEMICOLON token")
        void semicolon() {
            Scanner scanner = new Scanner(";");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.SEMICOLON, ";", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify MINUS token")
        void minus() {
            Scanner scanner = new Scanner("-");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.MINUS, "-", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify PLUS token")
        void plus() {
            Scanner scanner = new Scanner("+");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.PLUS, "+", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify STAR token")
        void star() {
            Scanner scanner = new Scanner("*");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.STAR, "*", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }
    }

    @Nested
    @Description("Comparison Operator")
    class ScannerComparisonTokenTest {

        @Test
        @Description("should identify BANG token")
        void bang() {
            Scanner scanner = new Scanner("!");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.BANG, "!", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify BANG_EQUAL token")
        void bangEqual() {
            Scanner scanner = new Scanner("!=");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.BANG_EQUAL, "!=", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify EQUAL token")
        void equal() {
            Scanner scanner = new Scanner("=");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.EQUAL, "=", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify EQUAL_EQUAL token")
        void equalEqual() {
            Scanner scanner = new Scanner("==");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.EQUAL_EQUAL, "==", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify GREATER token")
        void greater() {
            Scanner scanner = new Scanner(">");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.GREATER, ">", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify GREATER_EQUAL token")
        void greaterEqual() {
            Scanner scanner = new Scanner(">=");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.GREATER_EQUAL, ">=", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify LESS token")
        void less() {
            Scanner scanner = new Scanner("<");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.LESS, "<", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify LESS_EQUAL token")
        void lessEqual() {
            Scanner scanner = new Scanner("<=");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.LESS_EQUAL, "<=", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }
    }


    @Nested
    @Description("Literals")
    class ScannerLiteralTokenTest {

        @Test
        @Description("should identify IDENTIFIER token")
        void identifier() {
            Scanner scanner = new Scanner("hello");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.IDENTIFIER, "hello", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify STRING token")
        void string() {
            Scanner scanner = new Scanner("\"hello\"");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.STRING, "\"hello\"", "hello", 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify NUMBER token")
        void number() {
            Scanner scanner = new Scanner("123");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.NUMBER, "123", 123.0, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }
    }

    @Nested
    @Description("Keywords Token")
    class ScannerKeywordTokenTest {

        @Test
        @Description("should identify AND keyword token")
        void and() {
            Scanner scanner = new Scanner("and");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.AND, "and", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify CLASS keyword token")
        void classToken() {
            Scanner scanner = new Scanner("class");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.CLASS, "class", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify ELSE keyword token")
        void elseToken() {
            Scanner scanner = new Scanner("else");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.ELSE, "else", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify FALSE keyword token")
        void falseToken() {
            Scanner scanner = new Scanner("false");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.FALSE, "false", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify FUN keyword token")
        void fun() {
            Scanner scanner = new Scanner("fun");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.FUN, "fun", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify FOR keyword token")
        void forToken() {
            Scanner scanner = new Scanner("for");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.FOR, "for", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify IF keyword token")
        void ifToken() {
            Scanner scanner = new Scanner("if");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.IF, "if", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify NIL keyword token")
        void nil() {
            Scanner scanner = new Scanner("nil");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.NIL, "nil", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify OR keyword token")
        void or() {
            Scanner scanner = new Scanner("or");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.OR, "or", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }


        @Test
        @Description("should identify PRINT keyword token")
        void print() {
            Scanner scanner = new Scanner("print");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.PRINT, "print", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify RETURN keyword token")
        void returnToken() {
            Scanner scanner = new Scanner("return");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.RETURN, "return", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify SUPER keyword token")
        void superToken() {
            Scanner scanner = new Scanner("super");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.SUPER, "super", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify THIS keyword token")
        void thisToken() {
            Scanner scanner = new Scanner("this");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.THIS, "this", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify TRUE keyword token")
        void trueToken() {
            Scanner scanner = new Scanner("true");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.TRUE, "true", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify VAR keyword token")
        void var() {
            Scanner scanner = new Scanner("var");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.VAR, "var", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

        @Test
        @Description("should identify WHILE keyword token")
        void whileToken() {
            Scanner scanner = new Scanner("while");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.WHILE, "while", null, 1));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 1));

            assertEquals(expectedTokens, tokens);
        }

    }

    @Nested
    @Description("Multiple Comments followed")
    class MultipleCommentsAreFollowed {

        @Test
        @Description("should identify identifiers between several comments")
        void multiComments() {
            Scanner scanner = new Scanner("// adsf \na//asdf \n //sd \n /* aslkjf\nasdf\nafs\ndga\n */ b// /*asdf*/");
            List<Token> tokens = scanner.scanTokens();
            List<Token> expectedTokens = new ArrayList<>();

            expectedTokens.add(new Token(TokenType.IDENTIFIER, "a", null, 2));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "b", null, 8));
            expectedTokens.add(new Token(TokenType.EOF, "", null, 8));

            assertEquals(expectedTokens, tokens);
        }
    }
}
