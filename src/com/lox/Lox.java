package com.lox;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;

public class Lox {
    private static boolean hasError = false;
    private static boolean hadRuntimeError = false;
    private static final Interpreter interpreter = new Interpreter();

    static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            System.out.println(" File Mode");

            if (hasError) {
                System.exit(65);
            }
            if (hadRuntimeError) {
                System.exit(70);
            }
        } else {
            System.out.println(" REPL");
            runInteractive();
        }
    }

    static void runInteractive() throws IOException {
        InputStreamReader inputStream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStream);

        while (true) {
            String line = reader.readLine();
            System.out.println("INTERPRETER MODE : " + line);
            if (line.equals("exit")) {
                break;
            }
            run(line);
            hasError = false;
            hadRuntimeError = false;
        }
    }

    public static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();

        if (hasError) return;
        System.out.println(tokens);

        System.out.println(new AstPrinter().print(expr));
        System.out.println(interpreter.interpret(expr));
    }

    public static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.out.println("[line " + line + "] Error " + where + ": " + message);
        hasError = true;
    }

    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, "at '" + token.lexeme + "'", message);
        }
    }

    public static void runTimeError(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }
}
