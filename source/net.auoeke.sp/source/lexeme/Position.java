package net.auoeke.sp.source.lexeme;

public record Position(int index, int line, int column) implements Comparable<Position> {
	public int readableLine() {
		return this.line + 1;
	}

	public int readableColumn() {
		return this.column + 1;
	}

	@Override public int compareTo(Position position) {
		return this.line == position.line ? this.column - position.column : this.line - position.line;
	}

	@Override public String toString() {
		return this.readableLine() + ":" + this.readableColumn();
	}
}
