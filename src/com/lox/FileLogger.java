package com.lox;

import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger implements  Logger {
    private PrintWriter writer = null;

    public FileLogger(String name) throws IOException {

        String path = "./" + name + ".txt";
        this.writer = new PrintWriter(path, "UTF-8");
    }

    public  void println(Object obj){
        System.out.println(obj);
        this.writer.write(obj.toString()+"\n");
        this.writer.flush();
    }

    public  void print(Object obj){
        System.out.println(obj);
        this.writer.write(obj.toString());
        this.writer.flush();
    }
}
