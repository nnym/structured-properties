package net.auoeke.sp.intellij;

import java.util.Map;
import javax.swing.Icon;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.codeStyle.DisplayPriority;
import com.intellij.psi.codeStyle.DisplayPrioritySortable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpColorSettingsPage implements ColorSettingsPage, DisplayPrioritySortable {
	@Override public @Nullable Icon getIcon() {
		return SpFileType.icon;
	}

	@Override public @NotNull SyntaxHighlighter getHighlighter() {
		return new SpHighlighter();
	}

	@Override public @NonNls @NotNull String getDemoText() {
		return """
			color scheme {
				/* color scheme settings for Structured Properties */
				Structured Properties {
					string = #00FF00 ## green
					brace = white
					literal = orange
				}
			}
			""";
	}

	@Override public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		return null;
	}

	@Override public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
		return new AttributesDescriptor[]{
			descriptor("string", SpHighlighter.stringKey),
			descriptor("string delimiter", SpHighlighter.stringDelimiterKey),
			descriptor("number", SpHighlighter.numberKey),
			descriptor("keyword", SpHighlighter.keywordKey),
			descriptor("escape sequence", SpHighlighter.escapeKey),
			descriptor("comma", SpHighlighter.commaKey),
			descriptor("mapping", SpHighlighter.equalsKey),
			descriptor("brace", SpHighlighter.braceKey),
			descriptor("bracket", SpHighlighter.bracketKey),
			descriptor("line comment", SpHighlighter.lineCommentKey),
			descriptor("block comment", SpHighlighter.blockCommentKey),
			descriptor("block comment delimiter", SpHighlighter.blockCommentDelimiterKey)
		};
	}

	@Override public ColorDescriptor @NotNull [] getColorDescriptors() {
		return new ColorDescriptor[0];
	}

	@NotNull
	@NlsContexts.ConfigurableName
	@Override public String getDisplayName() {
		return SpFileType.instance.getName();
	}

	@Override public DisplayPriority getPriority() {
		return DisplayPriority.LANGUAGE_SETTINGS;
	}

	private static AttributesDescriptor descriptor(String name, TextAttributesKey key) {
		return new AttributesDescriptor(name, key);
	}
}
