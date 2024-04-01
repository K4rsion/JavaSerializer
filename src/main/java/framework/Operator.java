package framework;

public enum Operator {
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN_OR_EQUALS("<="),
    IN("in");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }
}