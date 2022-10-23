package net.auoeke.sp.tree;

public class StringNode extends Node {
	public String delimiter;
	public String value;

	public StringNode(String delimiter, String value) {
		this.delimiter = delimiter;
		this.value = value;
	}
}
