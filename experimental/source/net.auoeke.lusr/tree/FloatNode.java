package net.auoeke.lusr.tree;

import java.math.BigDecimal;

public class FloatNode extends Node {
	public String source;
	public BigDecimal value;

	public FloatNode(String source, BigDecimal value) {
		this.source = source;
		this.value = value;
	}
}
