package net.auoeke.cin.lexer;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import net.auoeke.cin.lexer.error.ErrorKey;
import net.auoeke.cin.lexer.error.SyntaxError;
import net.auoeke.cin.lexer.error.SyntaxException;
import net.auoeke.cin.lexer.lexeme.BooleanLexeme;
import net.auoeke.cin.lexer.lexeme.CommaLexeme;
import net.auoeke.cin.lexer.lexeme.CommentLexeme;
import net.auoeke.cin.lexer.lexeme.DelimiterLexeme;
import net.auoeke.cin.lexer.lexeme.FloatLexeme;
import net.auoeke.cin.lexer.lexeme.IntegerLexeme;
import net.auoeke.cin.lexer.lexeme.Lexeme;
import net.auoeke.cin.lexer.lexeme.Lexeme.Type;
import net.auoeke.cin.lexer.lexeme.MappingLexeme;
import net.auoeke.cin.lexer.lexeme.NewlineLexeme;
import net.auoeke.cin.lexer.lexeme.NullLexeme;
import net.auoeke.cin.lexer.lexeme.StringLexeme;
import net.auoeke.cin.lexer.lexeme.WhitespaceLexeme;

public class Lexer implements Iterable<Lexeme> {
    private static final String EXPRESSION_TERMINATORS = "\n={}[]/,";

    private final String cin;
    private final boolean retainSource;
    private final boolean throwExceptions;
    private final StringCharacterIterator iterator;
    private final List<Lexeme> lexemes = new ArrayList<>();

    private int line = 1;
    private int column = 0;
    private int savedLine = -1;
    private int savedColumn = -1;
    private char current;
    private Context context;
    private Lexeme lastCode;

    public Lexer(String cin, boolean retainSource, boolean throwExceptions) {
        this.cin = cin;
        this.retainSource = retainSource;
        this.throwExceptions = throwExceptions;
        this.iterator = new StringCharacterIterator(cin);

        while (this.advance()) {
            this.process(this.current);
        }
    }

    public Lexer(String cin) {
        this(cin, false, true);
    }

    @Override public ListIterator<Lexeme> iterator() {
        return this.lexemes.listIterator();
    }

    private String position() {
        return this.savedLine + ":" + this.savedColumn;
    }

    private int index() {
        return this.iterator.getIndex();
    }

    private int nextIndex() {
        return this.iterator.getIndex() + 1;
    }

    private char next() {
        this.current = this.iterator.current();

        if (this.current == '\n') {
            this.line++;
            this.column = 0;
        }

        this.column++;
        this.iterator.setIndex(this.nextIndex());

        return this.current;
    }

    private boolean advance() {
        return this.next() != CharacterIterator.DONE;
    }

    private char previous() {
        var previous = this.iterator.previous();

        if (previous == '\n') {
            this.line--;
        }

        this.column--;

        return previous;
    }

    private char peek(int distance) {
        var index = this.nextIndex() + distance - 1;
        return index >= this.cin.length() ? CharacterIterator.DONE : this.cin.charAt(index);
    }

    private char peek() {
        return this.peek(1);
    }

    private void savePosition() {
        this.savedLine = this.line;
        this.savedColumn = this.column;
    }

    private boolean whitespaceOrComment(char character) {
        if (character == '\n') {
            this.add(new NewlineLexeme(this.savedLine, this.savedColumn));
        } else if (Character.isWhitespace(character)) {
            this.whitespace(character);
        } else {
            return this.comment(character);
        }

        return true;
    }

    private void requireNext(ErrorKey error, Predicate<Character> predicate) {
        while (this.advance()) {
            this.savePosition();

            if (!this.whitespaceOrComment(this.current)) {
                if (predicate.test(this.current)) {
                    this.process(this.current);
                    return;
                }

                break;
            }
        }

        this.error(error);
    }

    private void requireClose(char character, ErrorKey error) {
        var position = this.position();

        while (this.advance()) {
            if (this.current == character) {
                this.structure(this.current);
                return;
            }

            this.process(this.current);
        }

        this.error(position, error);
    }

    private boolean comma(char character) {
        if (character == ',') {
            this.add(new CommaLexeme(this.savedLine, this.savedColumn));
            return true;
        }

        return false;
    }

    private boolean mapping(char character) {
        if (character == '=') {
            this.add(new MappingLexeme(this.savedLine, this.savedColumn));
            return true;
        }

        return false;
    }

    private void scanExpression(char character, boolean key) {
        if (!key && contains("{}[]", character)) {
            this.structure(character);
        } else if (contains("\"'`", character)) {
            this.string(character);
        } else {
            this.rawString(character);
        }
    }

    private void process(char character, boolean key) {
        this.savePosition();
        if (!this.whitespaceOrComment(character) && !this.comma(character) && !this.mapping(character)) {
            this.scanExpression(character, key);
            if (key) {
                this.requireNext(ErrorKey.NO_MAPPING, s -> contains("={[", s));
            } else {
                while (this.advance()) {
                    if (contains(EXPRESSION_TERMINATORS, this.current) && (this.current != '/' || this.peek() == '/')) {
                        this.previous();
                        return;
                    }

                    if (Character.isWhitespace(this.current)) {
                        this.whitespace(this.current);
                    } else {
                        this.error(ErrorKey.NO_SEPARATOR);
                    }
                }
            }
        }
    }

    private void process(char character) {
        this.process(character, false);
    }

    private void add(Lexeme lexeme) {
        if (lexeme != null && (this.retainSource || !lexeme.type().isSourceOnly())) {
            this.lexemes.add(lexeme);

            if (!lexeme.type().isSourceOnly()) {
                this.lastCode = lexeme;
            }
        }
    }

    private boolean ifNext(char target, Runnable action) {
        var result = this.next() == target;

        if (result) {
            action.run();
        } else {
            this.previous();
        }

        return result;
    }

    private boolean ifNext(char target) {
        return this.ifNext(target, () -> {});
    }

    private void error(String position, ErrorKey error) {
        if (this.throwExceptions) {
            throw new SyntaxException(position, error);
        }

        if (this.lastCode != null) {
            this.lastCode.error = new SyntaxError(error.template.formatted(position));
        }
    }

    private void error(ErrorKey error) {
        this.error(this.position(), error);
    }

    private void whitespace(char whitespace) {
        this.add(new WhitespaceLexeme(this.savedLine, this.savedColumn, buildString(builder -> {
            builder.append(whitespace);

            while (this.advance()) {
                if (!Character.isWhitespace(this.current) || this.current == '\n') {
                    this.previous();
                    return;
                }

                builder.append(this.current);
            }
        })));
    }

    private boolean comment(char character) {
        if (character == '/' && this.ifNext('*')) {
            this.add(new CommentLexeme(this.savedLine, this.savedColumn, Type.BLOCK_COMMENT, buildString(builder -> {
                var depth = 1;

                while (this.advance()) {
                    switch (this.current) {
                        case '/' -> {
                            builder.append('/');

                            var next = this.next();
                            builder.append(next)
                            ;
                            if (next == '*') {
                                depth++;
                            }
                        }
                        case '*' -> {
                            var next = this.next();
                            if (next == '/' && --depth == 0) {
                                return;
                            }
                            builder.append('*');
                            builder.append(next);
                        }
                        default -> builder.append(this.current);
                    }
                }
            })));
        } else if (character == '#' && this.ifNext('#')) {
            this.add(new CommentLexeme(this.savedLine, this.savedColumn, Type.LINE_COMMENT, buildString(builder -> {
                while (this.advance()) {
                    if (this.current == '\n') {
                        this.previous();
                        return;
                    }

                    builder.append(this.current);
                }
            })));
        } else {
            return false;
        }
        return true;
    }

    private void structure(char character) {
        this.add(new DelimiterLexeme(this.savedLine, this.savedColumn, character));

        switch (character) {
            case '}' -> {
                if (this.context != Context.MAP) {
                    this.error(ErrorKey.RBRACE_OUTSIDE_MAP);
                }
            }
            case ']' -> {
                if (this.context != Context.ARRAY) {
                    this.error(ErrorKey.RBRACKET_OUTSIDE_MAP);
                }
            }
            default -> {
                var previousContext = this.context;

                switch (character) {
                    case '{' -> {
                        this.context = Context.MAP;
                        this.requireClose('}', ErrorKey.UNCLOSED_MAP);
                    }
                    case '[' -> {
                        this.context = Context.ARRAY;
                        this.requireClose(']', ErrorKey.UNCLOSED_ARRAY);
                    }
                }

                this.context = previousContext;
            }
        }
    }

    private int delimiterLength(char delimiter) {
        var length = 1;

        while (this.advance()) {
            if (this.current != delimiter) {
                this.previous();
                return length;
            }

            length++;
        }

        return length;
    }

    private void string(char delimiter) {
        var length = this.delimiterLength(delimiter);

        if (length == 2) {
            this.add(new StringLexeme(this.savedLine, this.savedColumn, String.valueOf(delimiter), ""));
        } else {
            this.add(new StringLexeme(this.savedLine, this.savedColumn, String.valueOf(delimiter).repeat(length), buildString(builder -> {
                while (this.advance()) {
                    if (this.current == delimiter) {
                        var count = 1;

                        while (count < length && this.peek(count) == delimiter) {
                            count++;
                        }

                        if (count == length) {
                            repeat(count - 1, this::next);
                            return;
                        }

                        builder.append(this.current);
                        repeat(count - 1, () -> builder.append(this.next()));
                    } else {
                        builder.append(this.current);
                    }
                }

                this.error(ErrorKey.UNCLOSED_STRING);
            })));
        }
    }

    private void rawString(char character) {
        var additionalLexemes = new ArrayList<Lexeme>();

        var string = buildString(builder -> {
            builder.append(character);
            var whitespace = -1;

            while (this.advance()) {
                if (EXPRESSION_TERMINATORS.indexOf(this.current) >= 0 && (this.current != '/' || this.peek() == '/')) {
                    if (whitespace != -1) {
                        additionalLexemes.add(new WhitespaceLexeme(this.savedLine, this.savedColumn, this.cin.substring(whitespace, this.index())));
                    }

                    switch (character) {
                        case '=' -> additionalLexemes.add(new MappingLexeme(this.savedLine, this.savedColumn));
                        default -> this.previous();
                    }

                    return;
                } else if (Character.isWhitespace(this.current)) {
                    if (whitespace == -1) {
                        whitespace = this.index();
                    }
                } else {
                    if (whitespace == -1) {
                        builder.append(this.current);
                    } else {
                        IntStream.rangeClosed(whitespace, this.index()).forEach(index -> builder.append(this.cin.charAt(index)));
                        whitespace = -1;
                    }
                }
            }
        });

        this.add(switch (string) {
            case "false" -> new BooleanLexeme(this.savedLine, this.savedColumn, false);
            case "true" -> new BooleanLexeme(this.savedLine, this.savedColumn, true);
            case "null" -> new NullLexeme(this.savedLine, this.savedColumn);
            default -> {
                try {
                    yield new IntegerLexeme(this.savedLine, this.savedColumn, Long.parseLong(string));
                } catch (NumberFormatException exception) {
                    try {
                        yield new FloatLexeme(this.savedLine, this.savedColumn, Double.parseDouble(string), string);
                    } catch (NumberFormatException e) {
                        yield new StringLexeme(this.savedLine, this.savedColumn, null, string);
                    }
                }
            }
        });

        additionalLexemes.forEach(this::add);
    }

    private static String buildString(Consumer<StringBuilder> builder) {
        var b = new StringBuilder();
        builder.accept(b);
        return b.toString();
    }

    private static void repeat(int n, Runnable action) {
        IntStream.range(0, n - 1).forEach(__ -> action.run());
    }

    private static boolean contains(String string, char character) {
        return string.indexOf(character) >= 0;
    }
}
