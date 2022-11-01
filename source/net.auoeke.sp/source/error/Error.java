package net.auoeke.sp.source.error;

import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.lexeme.Position;

public record Error(Node node, Offset offset, Key key, Object... arguments) {
	public Position offsetPosition() {
		var position = this.node().start();
		return new Position(position.index(), position.line(), position.column() + this.offset().value);
	}

	public String description() {
		return this.key.template.replace("%node", this.node).formatted(this.arguments);
	}

	public String help(String sp, String source) {
		var position = this.offsetPosition();

		return """
			%s: %s
			 %s
			%s
			""".formatted(
			source == null ? position : source + ':' + position, this.description(),
			sp.substring(sp.lastIndexOf('\n', position.index()) + 1, (sp.indexOf('\n', position.index()) + sp.length() + 1) % (sp.length() + 1)),
			" ".repeat(position.column() + 1) + "^".repeat(this.offset == Offset.NONE ? this.node.length() : 1)
		);
	}

	@Override public String toString() {
		return "%s: %s: %s".formatted(this.node.start(), this.key, this.description());
	}

	public enum Offset {
		BEFORE(-1),
		NONE(0),
		AFTER(1);

		public final int value;

		Offset(int value) {
			this.value = value;
		}
	}

	public enum Key {
		COMPOUND_KEY("Compound map keys are not allowed."),
		COMPOUND_STRUCTURE_KEY("Structure keys must be primitive."),
		CONSECUTIVE_COMMA("Consecutive commas are not allowed."),
		DUPLICATE_KEY("Duplicate map key %node."),
		END_OUT_OF_CONTEXT("Delimiter '%node' does not match enclosing context."),
		EOF("Premature end of file."),
		ILLEGAL_TOKEN("Expected an element instead of %node."),
		NO_MAP_VALUE("Map key is not followed by assignment, array or map."),
		NO_SEPARATOR("Expected a separator."),
		NO_VALUE("Key is not associated with a value."),
		OPEN_ARRAY("Array is not closed."),
		OPEN_MAP("Map is not closed."),
		OPEN_STRING("String is not closed."),
		PRIMITIVE_RIGHT_NO_MAPPING("Primitive right side of pair must be preceded by '='."),
		RBRACE_OUTSIDE_MAP("Found '}' outside a map."),
		RBRACKET_OUTSIDE_ARRAY("Found ']' outside an array.");

		public final String template;

		Key(String template) {
			this.template = template;
		}
	}
}
