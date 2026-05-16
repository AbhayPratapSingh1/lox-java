package com.lox;

public class Interpreter implements Expr.Visitor<Object> {
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {

        Object right = evaluate(expr.right);
        Object left = evaluate(expr.left);

        switch (expr.operator.type) {
            case LESS:
                checkNumberOperant(expr.operator, left, right);
                return (Double) left < (Double) right;

            case LESS_EQUAL:
                checkNumberOperant(expr.operator, left, right);
                return (Double) left <= (Double) right;

            case GREATER:
                checkNumberOperant(expr.operator, left, right);
                return (Double) left > (Double) right;

            case GREATER_EQUAL:
                checkNumberOperant(expr.operator, left, right);
                return (Double) left >= (Double) right;

            case MINUS:
                checkNumberOperant(expr.operator, left, right);
                return (Double) left - (Double) right;

            case SLASH:
                checkNumberOperant(expr.operator, left, right);
                return (Double) left / (Double) right;

            case STAR:
                checkNumberOperant(expr.operator, left, right);
                return (Double) left * (Double) right;

            case EQUAL_EQUAL:
                checkNumberOperant(expr.operator, left, right);
                return isEqual(left, right);

            case BANG_EQUAL:
                checkNumberOperant(expr.operator, left, right);
                return !isEqual(left, right);

            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    checkNumberOperant(expr.operator, left, right);
                    return (Double) left + (Double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }

                throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
        }
        return null;
    }


    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {

        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperant(expr.operator, right);
                return -(double) right;
            case BANG:
                return !isTruthy(right);
        }
        return null;
    }


    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        return true;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }


    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    private Object evaluate(Expr expression) {
        return expression.accept(this);
    }

    private boolean isEqual(Object term1, Object term2) {
        if (term1 == null && term2 == null) return true;
        if (term1 == null) return false;
        return term1.equals(term2);
    }


    private void checkNumberOperant(Token operator, Object candidate) {
        if (candidate instanceof Double) return;
        throw new RuntimeError(operator, "Operator must be number");
    }

    private void checkNumberOperant(Token operator, Object candidate1, Object candidate2) {
        if (candidate1 instanceof Double && candidate2 instanceof Double) return;
        throw new RuntimeError(operator, "Operator must be number");
    }


    Object interpret(Expr expr) {
        try {
            Object evaluated = evaluate(expr);
            return stringify(evaluated);
        } catch (RuntimeError error) {
            System.out.println(error);
            Lox.runTimeError(error);
        }
        return null;
    }

    private String stringify(Object object) {
        if (object == null) return "nil";
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                return text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }
}
