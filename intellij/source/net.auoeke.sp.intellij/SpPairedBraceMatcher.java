package net.auoeke.sp.intellij;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpPairedBraceMatcher implements PairedBraceMatcher {
	private static final BracePair[] pairs = new BracePair[]{
		new BracePair(SpLexer.lbraceType, SpLexer.rbraceType, true),
		new BracePair(SpLexer.lbracketType, SpLexer.rbracketType, true)
	};

	@Override public BracePair @NotNull [] getPairs() {
		return pairs;
	}

	@Override public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
		return true;
	}

	@Override public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
		return openingBraceOffset;
	}
}
