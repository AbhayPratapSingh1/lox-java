package com.lox;

import java.util.HashMap;

public class Environment {

    Environment enclosing;

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public Environment() {
        this.enclosing = null;
    }

    private final HashMap<String, Object> values = new HashMap<>();

    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name){
        if (values.containsKey(name.lexeme)){
            if (values.get(name.lexeme) == null){
                throw new RuntimeError(name,"Unassigned Variable '" + name.lexeme + "'.");
            }
            return values.get(name.lexeme);
        }
        if (this.enclosing != null){
            return this.enclosing.get(name);
        }
        throw new RuntimeError(name,"Undefined variable '" + name.lexeme + "'.");
    }

    public void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)){
            values.put(name.lexeme, value);
            return;
        }
        if (this.enclosing != null){
            this.enclosing.assign(name, value);
            return;
        }
        System.out.println();
        throw new RuntimeError(name,"Undefined variable '" + name.lexeme + "'.");
    }
}