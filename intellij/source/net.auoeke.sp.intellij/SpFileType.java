package net.auoeke.sp.intellij;

import javax.swing.Icon;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.NlsContexts.Label;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NotNull;

public class SpFileType extends LanguageFileType {
	public static final Language language = new Language("Structured Properties") {};
	public static final SpFileType instance = new SpFileType();
	public static final Icon icon = IconLoader.getIcon("META-INF/fileTypeIcon.png", SpFileType.class);

	private SpFileType() {
		super(language);
	}

	@Override public @NotNull String getName() {
		return "Structured Properties";
	}

	@SuppressWarnings("DialogTitleCapitalization")
	@Override public @Label @NotNull String getDescription() {
		return this.getName();
	}

	@Override public @NlsSafe @NotNull String getDefaultExtension() {
		return "str";
	}

	@Override public Icon getIcon() {
		return icon;
	}
}
