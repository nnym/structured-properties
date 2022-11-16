package net.auoeke.sp.intellij;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class SpFile extends PsiFileBase {
	public SpFile(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, SpFileType.language);
	}

	@NotNull
	@Override public FileType getFileType() {
		return SpFileType.instance;
	}
}
