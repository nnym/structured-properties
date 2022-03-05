package net.auoeke.cin.parser;

import java.util.ArrayList;
import java.util.List;
import net.auoeke.cin.Cin;
import net.auoeke.cin.element.ArrayElement;
import net.auoeke.cin.element.BooleanElement;
import net.auoeke.cin.element.Element;
import net.auoeke.cin.element.EmptyElement;
import net.auoeke.cin.element.FloatElement;
import net.auoeke.cin.element.IntegerElement;
import net.auoeke.cin.element.MapElement;
import net.auoeke.cin.element.NullElement;
import net.auoeke.cin.element.PairElement;
import net.auoeke.cin.element.PrimitiveElement;
import net.auoeke.cin.element.StringElement;
import net.auoeke.cin.parser.lexer.lexeme.CommentLexeme;
import net.auoeke.cin.parser.lexer.lexeme.Lexeme;
import net.auoeke.cin.parser.lexer.LexemeIterator;
import net.auoeke.cin.parser.lexer.Lexer;
import net.auoeke.cin.parser.lexer.lexeme.StringLexeme;
import net.auoeke.cin.parser.lexer.lexeme.Token;

public class Parser {
    private final List<CommentLexeme> comments = new ArrayList<>();
    private final LexemeIterator iterator;
    private Context context = Context.FILE;
    private Lexeme lexeme;

    public Parser(String cin, Cin.Option... options) {
        this.iterator = new Lexer(cin).iterator();
    }

    public Element parse() {
        if (!this.advanceCode()) {
            return EmptyElement.instance;
        }

        var element = this.nextElement(false);

        if (element != null && this.consumeSeparator(false)) {
            if (this.lexeme.token() == Token.NEWLINE) {
                if (this.advanceCode()) {
                    this.iterator.previous();
                } else {
                    return element;
                }
            }

            if (element instanceof PairElement pair) {
                if (!pair.a.type().primitive()) {
                    // todo: error: compound type map keys are not allowed
                    return element;
                }

                var map = new MapElement();
                map.put(((PrimitiveElement) pair.a).stringValue(), pair.b);
                this.endMap(map, false);

                return map;
            }

            var array = new ArrayElement();
            array.add(element);
            this.endArray(array, false);

            return array;
        }

        return element;
    }

    private Element nextElement(boolean advance) {
        var element = this.nextElement0(advance);
        var primitive = element.type().primitive();
        var index = this.iterator.nextIndex();

        if (this.advance()) {
            if (this.lexeme.token().begin()) {
                if (primitive) {
                    return new PairElement(element, this.nextElement0(false));
                }

                // todo: error: structure key must be primitive but compound type was found
                throw null;
            } else if (this.lexeme.token().primitive()) {
                // todo: error: primitive right side of pair must be preceded by "="
                throw null;
            } else {
                this.rewind(index);

                if (this.advanceCode()) {
                    if (this.lexeme.token() == Token.MAPPING) {
                        if (this.context == Context.MAP && !primitive) {
                            // todo: error: map key must be primitive but compound type was found
                            throw null;
                        }

                        return new PairElement(element, this.nextElement0(true));
                    }
                }
            }
        }

        this.rewind(index);

        return element;
    }

    private Element nextElement0(boolean advance) {
        if (advance && !this.advanceCode()) {
            // todo: error: expected element but reached end
            return null;
        }

        return switch (this.lexeme.token()) {
            case STRING -> {
                var string = (StringLexeme) this.lexeme;

                if (string.delimiter != null) {
                    yield new StringElement(string.value, string.delimiter);
                }

                yield switch (string.value) {
                    case "false" -> BooleanElement.of(false);
                    case "true" -> BooleanElement.of(true);
                    case "null" -> NullElement.instance;
                    default -> {
                        try {
                            yield new IntegerElement(string.value, Long.parseLong(string.value));
                        } catch (NumberFormatException exception) {
                            try {
                                yield new FloatElement(string.value, Double.parseDouble(string.value));
                            } catch (NumberFormatException e) {
                                yield new StringElement(string.value, string.delimiter);
                            }
                        }
                    }
                };
            }
            default -> {
                if (this.lexeme.token().begin()) {
                    yield this.nextStructure();
                }

                // todo: error: element expected but found $lexeme
                throw null;
            }
        };
    }

    private Element nextStructure() {
        var context = this.context;

        switch (this.lexeme.token()) {
            case ARRAY_BEGIN -> {
                this.context = Context.ARRAY;
                var array = new ArrayElement();
                this.endArray(array, true);
                this.context = context;

                return array;
            }
            case MAP_BEGIN -> {
                this.context = Context.MAP;
                var map = new MapElement();
                this.endMap(map, true);
                this.context = context;

                return map;
            }
            default -> throw new IllegalArgumentException(this.lexeme.token().toString());
        }
    }

    private void endArray(ArrayElement array, boolean close) {
        while (this.advanceCode()) {
            if (this.lexeme.token() == this.context.end()) {
                return;
            }

            array.add(this.nextElement(false));
            this.consumeSeparator(true);
        }

        if (close) {
            // todo: error: not closed
            throw null;
        }
    }

    private void endMap(MapElement map, boolean close) {
        while (this.advanceCode()) {
            if (this.lexeme.token() == Token.MAP_END) {
                return;
            }

            var element = this.nextElement(false);

            if (element instanceof PairElement pair) {
                map.put(((PrimitiveElement) pair.a).stringValue(), pair.b);
            } else {
                // todo: error: pair expected but single element found
                throw null;
            }

            this.consumeSeparator(true);
        }

        if (close) {
            // todo: error: not closed
            throw null;
        }
    }

    private void rewind(int cursor) {
        this.iterator.cursor(cursor - 1);
        this.lexeme = this.iterator.next();
    }

    private boolean consumeSeparator(boolean error) {
        if (this.advance()) {
            if (this.lexeme.token() == Token.NEWLINE) {
                return true;
            }

            if (this.lexeme.token() == Token.COMMA) {
                var index = this.iterator.nextIndex();

                if (this.advanceCode() && this.lexeme.token() == Token.COMMA) {
                    // todo: error: consecutive commas not allowed
                } else {
                    this.rewind(index);
                }

                return true;
            }

            this.iterator.previous();

            if (this.lexeme.token() == this.context.end()) {
                return true;
            }

            if (error) {
                // todo: error: expected [\n,] or structure end but found $lexeme
            }
        }

        return false;
    }

    private boolean advanceCode() {
        while (this.iterator.hasNext()) {
            if (this.next().token() != Token.NEWLINE) {
                return true;
            }
        }

        return false;
    }

    private boolean advance() {
        while (this.iterator.hasNext()) {
            if (!this.next().token().sourceOnly()) {
                return true;
            }
        }

        return false;
    }

    private Lexeme next() {
        this.lexeme = this.iterator.next();

        if (this.lexeme.token().end() && this.context.end() != this.lexeme.token()) {
            // todo: error: delimiter does not match enclosing context
            throw null;
        }

        return this.lexeme;
    }
}
