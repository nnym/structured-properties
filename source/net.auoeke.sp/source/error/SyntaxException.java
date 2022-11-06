package net.auoeke.sp.source.error;

public class SyntaxException extends RuntimeException {
	public SyntaxException(String message) {
		super('\n' + message);
	}
}
