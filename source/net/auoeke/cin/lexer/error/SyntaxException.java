package net.auoeke.cin.lexer.error;

public class SyntaxException extends RuntimeException {
    public String source = null;

    private final String position;
    private final ErrorKey error;

    public SyntaxException(String position, ErrorKey error) {
        super((String) null);

        this.position = position;
        this.error = error;
    }

    @Override public String getMessage() {
        return this.error.template.formatted(this.source == null ? this.position : this.source + ':' + this.position);
    }
}
