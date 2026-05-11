package com.lox;

import java.util.List;

abstract class Expr {

    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);

        R visitUnaryExpr(Unary expr);

        R visitGroupExpr(Group expr);

        R visitLiteralExpr(Literal expr);
    }

    static class Binary extends Expr {
        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }

    static class Unary extends Expr {
        public Unary(Token operator, Expr literal) {
            this.operator = operator;
            this.literal = literal;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        final Token operator;
        final Expr literal;
    }

    static class Group extends Expr {
        public Group(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupExpr(this);
        }

        final Expr expression;
    }

    static class Literal extends Expr {
        public Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        final Object value;
    }

    abstract <R> R accept(Visitor<R> visitor);

}
