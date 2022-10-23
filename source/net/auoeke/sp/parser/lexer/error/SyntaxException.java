package net.auoeke.sp.parser.lexer.error;

public class SyntaxException extends RuntimeException {
    public String source = null;

    private final String position;
    private final ErrorKey error;
    private final Object[] arguments;

    public SyntaxException(String position, ErrorKey error, Object... arguments) {
        super((String) null);

        this.position = position;
        this.error = error;
        this.arguments = arguments;
    }

    @Override public String getMessage() {
        return (this.source == null ? "" : this.source + ':') + this.position + ' ' + this.error.template.formatted(this.arguments);
    }
}
