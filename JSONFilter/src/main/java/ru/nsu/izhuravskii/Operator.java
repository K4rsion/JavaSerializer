package ru.nsu.izhuravskii;

/**
 * This is enum for operator of JSON filter
 */
public enum Operator {
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN_OR_EQUALS("<=");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

}
