package net.auoeke.lusr.tree;

import java.math.BigInteger;

public class IntegerNode extends Node {
	public String source;
	public BigInteger value;

	public IntegerNode(String source, BigInteger value) {
		this.source = source;
		this.value = value;
	}
}
