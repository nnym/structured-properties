package net.auoeke.eson;

import java.net.URL;
import java.nio.file.Path;
import net.auoeke.eson.element.Element;
import net.auoeke.eson.parser.Parser;
import net.auoeke.eson.parser.lexer.error.SyntaxException;

public class Eson {
    public static Element parse(String eson, Option... options) {
        return new Parser(eson, options).parse();
    }

    public static Element parse(byte[] eson) {
        return parse(new String(eson));
    }

    public static Element parseResource(URL eson) {
        return parseResource(eson, eson.toString());
    }

    public static Element parseResource(Path eson) {
        return parseResource(eson.toUri().toURL(), eson.toString());
    }

    private static Element parseResource(URL eson, String source) {
        try {
            try (var stream = eson.openStream()) {
                return parse(stream.readAllBytes());
            }
        } catch (SyntaxException exception) {
            exception.source = source;

            throw exception;
        }
    }

    public enum Option {
        RETAIN_COMMENTS
    }
}
