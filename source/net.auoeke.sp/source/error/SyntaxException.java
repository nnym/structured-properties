package net.auoeke.sp.source.error;

import net.auoeke.sp.source.lexeme.Position;

public class SyntaxException extends RuntimeException {
	public String source = null;

	private final Position position;
	private final Error.Key error;
	private final Object[] arguments;

	public SyntaxException(Position position, Error.Key error, Object... arguments) {
		super((String) null);

		this.position = position;
		this.error = error;
		this.arguments = arguments;
	}

	@Override public String getMessage() {
		return (this.source == null ? "" : this.source + ':') + this.position + ' ' + this.error.template.formatted(this.arguments);
	}
}
