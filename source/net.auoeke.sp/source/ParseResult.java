package net.auoeke.sp.source;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.auoeke.sp.source.error.Error;
import net.auoeke.sp.source.error.SyntaxException;
import net.auoeke.sp.source.tree.SourceUnit;

public record ParseResult(SourceUnit tree, List<Error> errors) {
	public SourceUnit success() {
		if (this.isSuccess()) {
			return this.tree;
		}

		throw new SyntaxException(this.message());
	}

	public boolean isSuccess() {
		return this.errors.isEmpty();
	}

	public String message() {
		var source = this.tree.source.toString();
		return this.errors.stream()
			.sorted(Comparator.comparing(Error::offsetPosition))
			.map(error -> error.help(source, this.tree.location))
			.collect(Collectors.joining("\n\n"));
	}
}
