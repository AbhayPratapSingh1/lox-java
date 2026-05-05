package com.lox;

import com.lox.utils.Scanner;
import com.lox.utils.Token;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;

public class Lox {
    private static boolean hasError = false;

    static void main(String[] args) throws IOException {
        if (args.length > 1){
          System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1){
            System.out.println(" File Mode");

            if (hasError){
                System.exit(65);
            }
        } else {
            System.out.println(" REPL");
            runInteractive();
        }
    }

    static void runInteractive() throws IOException {
        InputStreamReader inputStream =new InputStreamReader(System.in);
        BufferedReader reader  = new BufferedReader(inputStream);

        while(true){
            String line = reader.readLine();
            System.out.println("INTERPRETER MODE : "+line);
            if(line.equals("exit")){
                break;
            }
            run(line);
            hasError = false;
        }
    }

    public static List<Token> run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens  = scanner.scanTokens();
        System.out.println(tokens);
        return tokens;
    }

    public static void error(int line, String message) {
        report(line, "",message);
    }

    private static void report(int line, String where, String message) {
        System.out.println("[line "+line+"] Error " + where+ ": "+message );
        hasError = true;
    }

}
