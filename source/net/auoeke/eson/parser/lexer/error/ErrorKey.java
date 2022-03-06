package net.auoeke.eson.parser.lexer.error;

public enum ErrorKey {
    NO_SEPARATOR("%s: Expected a separator."),
    NO_MAPPING("%s: Every key in a map must be followed by an assignment, an array or a map."),
    RBRACE_OUTSIDE_MAP("%s: Found '}' outside a map."),
    RBRACKET_OUTSIDE_ARRAY("%s: Found ']' outside an array."),
    UNCLOSED_STRING("%s: The string is not closed."),
    UNCLOSED_MAP("%s: Map is not closed."),
    UNCLOSED_ARRAY("%s: Array is not closed.");

    public final String template;

    ErrorKey(String template) {
        this.template = template;
    }
}
