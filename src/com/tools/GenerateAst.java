package com.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    static void main(String[] args) throws IOException {
        if (args.length != 1){
            System.out.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir,"Expr", Arrays.asList(
                "Binary : Expr literal, Token operator, Expr literal",
                "Unary : Token operator, Expr literal",
                "Group : Expr expression",
                "Literal : Object value"
        ));
    }

    private static void defineAst(String outputDir, String basename, List<String> types) throws IOException {
        String path = outputDir + "/" + basename + ".class";
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
        writer.println("package com.lox");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class "+basename + "{");
        writer.println("}");
        writer.close();

    }
}
