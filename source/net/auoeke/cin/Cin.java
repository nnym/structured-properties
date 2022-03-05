package net.auoeke.cin;

import java.net.URL;
import java.nio.file.Path;
import net.auoeke.cin.element.Element;
import net.auoeke.cin.parser.Parser;
import net.auoeke.cin.parser.lexer.error.SyntaxException;

public class Cin {
    public static Element parse(String cin, Option... options) {
        return new Parser(cin, options).parse();
    }

    public static Element parse(byte[] cin) {
        return parse(new String(cin));
    }

    public static Element parseResource(URL cin) {
        return parseResource(cin, cin.toString());
    }

    public static Element parseResource(Path cin) {
        return parseResource(cin.toUri().toURL(), cin.toString());
    }

    private static Element parseResource(URL cin, String source) {
        try {
            try (var stream = cin.openStream()) {
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
