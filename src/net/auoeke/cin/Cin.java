package net.auoeke.cin;

import java.net.URL;
import java.nio.file.Path;
import net.auoeke.cin.element.Element;
import net.auoeke.cin.lexer.error.SyntaxException;

public class Cin {
    public static Element parse(String cin) {
        return null;
    }

    public static Element parse(byte[] cin) {
        return parse(new String(cin));
    }

    public static Element parseFile(URL cin) {
        return parseFile(cin, cin.toString());
    }

    public static Element parseFile(Path cin) {
        return parseFile(cin.toUri().toURL(), cin.toString());
    }

    private static Element parseFile(URL cin, String source) {
        try {
            try (var stream = cin.openStream()) {
                return parse(stream.readAllBytes());
            }
        } catch (SyntaxException exception) {
            exception.source = source;

            throw exception;
        }
    }
}
