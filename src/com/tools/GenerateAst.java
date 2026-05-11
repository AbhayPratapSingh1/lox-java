package com.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary : Expr left, Token operator, Expr right",
                "Unary : Token operator, Expr literal",
                "Group : Expr expression",
                "Literal : Object value"
        ));
    }

    private static void defineAst(String outputDir, String basename, List<String> types) throws IOException {
        String path = outputDir + "/" + basename + ".java";
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
        writer.println("package com.lox;");

        writer.println();
        writer.println("abstract class " + basename + "{");

        defineVisitor(basename, types, writer);

        for (String type : types) {
            writer.println();
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, basename, className, fields);
        }


        writer.println("\t\tabstract <R> R accept(Visitor<R> visitor);");
        writer.println();
        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(String basename, List<String> types, PrintWriter writer) {
        writer.println();
        writer.println("\tinterface Visitor<R> {");

        for (String type : types) {
            String fieldType = type.split(":")[0].trim();
            writer.println("\t\tR visit" + fieldType + basename + "(" +  fieldType  + " "+ basename.toLowerCase() + ");");
        }

        writer.println("\t}");
    }

    private static void defineType(PrintWriter writer, String basename, String className, String fieldsList) {
        writer.println("\tstatic class " + className + " extends " + basename + " {");
        writer.println("\t\tpublic " + className + "(" + fieldsList + ") {");

        String[] fields = fieldsList.split(", ");
        for (String field : fields) {
            writer.println("\t\t\tthis." + field.split(" ")[1] + " = " + field.split(" ")[1] + ";");
        }
        writer.println("\t\t}");
        writer.println();

        writer.println("\t\t@Override");
        writer.println("\t\t<R> R accept(Visitor<R> visitor) {");

        writer.println("\t\t\treturn visitor.visit" + className + basename + "(this);" );

        writer.println("\t\t}");

        writer.println();

        for (String field : fields) {
            writer.println("\t\tfinal " + field + ";");
        }

        writer.println("\t}");
    }
}
