package net.auoeke.eson.parser.lexer.error;

public enum ErrorKey {
    COMPOUND_KEY("Compound map keys are not allowed."),
    COMPOUND_STRUCTURE_KEY("Structure keys must be primitive."),
    CONSECUTIVE_COMMA("Consecutive commas are not allowed."),
    DUPLICATE_KEY("Duplicate map key \"%s\"."),
    EOF("Premature end of file."),
    END_OUT_OF_CONTEXT("Delimiter '%s' does not match enclosing context."),
    ILLEGAL_TOKEN("Expected the beginning of an element but found \"%s\"."),
    NO_SEPARATOR("Expected a separator."),
    NO_VALUE("Map key is not followed by assignment, array or map."),
    PRIMITIVE_RIGHT_NO_MAPPING("Primitive right side of pair must be preceded by '='."),
    RBRACE_OUTSIDE_MAP("Found '}' outside a map."),
    RBRACKET_OUTSIDE_ARRAY("Found ']' outside an array."),
    UNCLOSED_ARRAY("Array is not closed."),
    UNCLOSED_MAP("Map is not closed."),
    UNCLOSED_STRING("String is not closed.");

    public final String template;

    ErrorKey(String template) {
        this.template = template;
    }
}
