package net.auoeke.sp.intellij;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

public class SpCommenter implements Commenter {
	@Nullable
	@Override public String getLineCommentPrefix() {
		return "##";
	}

	@Nullable
	@Override public String getBlockCommentPrefix() {
		return "/*";
	}

	@Nullable
	@Override public String getBlockCommentSuffix() {
		return "*/";
	}

	@Nullable
	@Override public String getCommentedBlockCommentPrefix() {
		return this.getBlockCommentPrefix();
	}

	@Nullable
	@Override public String getCommentedBlockCommentSuffix() {
		return this.getBlockCommentSuffix();
	}
}
