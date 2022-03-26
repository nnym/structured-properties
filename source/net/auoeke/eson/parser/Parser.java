package net.auoeke.eson.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import net.auoeke.eson.Eson;
import net.auoeke.eson.element.EsonArray;
import net.auoeke.eson.element.EsonBoolean;
import net.auoeke.eson.element.EsonElement;
import net.auoeke.eson.element.EsonFloat;
import net.auoeke.eson.element.EsonInteger;
import net.auoeke.eson.element.EsonMap;
import net.auoeke.eson.element.EsonNull;
import net.auoeke.eson.element.EsonPair;
import net.auoeke.eson.element.EsonPrimitive;
import net.auoeke.eson.element.EsonString;
import net.auoeke.eson.parser.lexer.LexemeIterator;
import net.auoeke.eson.parser.lexer.Lexer;
import net.auoeke.eson.parser.lexer.lexeme.CommentLexeme;
import net.auoeke.eson.parser.lexer.lexeme.Lexeme;
import net.auoeke.eson.parser.lexer.lexeme.StringLexeme;
import net.auoeke.eson.parser.lexer.lexeme.Token;

public class Parser {
    private final List<CommentLexeme> comments = new ArrayList<>();
    private final LexemeIterator iterator;
    private Context context = Context.FILE;
    private Lexeme lexeme;

    public Parser(String eson, Eson.Option... options) {
        this.iterator = new Lexer(eson).iterator();
    }

    public EsonElement parse() {
        if (!this.advanceCode()) {
            return new EsonMap();
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

            if (element instanceof EsonPair pair) {
                if (!pair.a.type().primitive()) {
                    // todo: error: compound type map keys are not allowed
                    return element;
                }

                var map = new EsonMap();

                if (map.put(((EsonPrimitive) pair.a).stringValue(), pair.b) != null) {
                    // todo: error: duplicate map key
                }

                this.endMap(map, false);

                return map;
            }

            var array = new EsonArray();
            array.add(element);
            this.endArray(array, false);

            return array;
        }

        return element;
    }

    private EsonElement nextElement(boolean advance) {
        var element = this.nextElement0(advance);
        var primitive = element.type().primitive();
        var index = this.iterator.nextIndex();

        if (this.advance()) {
            if (this.lexeme.token().begin()) {
                if (primitive) {
                    return new EsonPair(element, this.nextElement0(false));
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

                        return new EsonPair(element, this.nextElement0(true));
                    }
                }
            }
        }

        this.rewind(index);

        return element;
    }

    private EsonElement nextElement0(boolean advance) {
        if (advance && !this.advanceCode()) {
            // todo: error: expected element but reached end
            return null;
        }

        return switch (this.lexeme.token()) {
            case STRING -> {
                var string = (StringLexeme) this.lexeme;

                if (string.delimiter != null) {
                    yield new EsonString(string.value, string.delimiter);
                }

                yield switch (string.value) {
                    case "false" -> EsonBoolean.of(false);
                    case "true" -> EsonBoolean.of(true);
                    case "null" -> EsonNull.instance;
                    default -> {
                        try {
                            yield new EsonInteger(string.value, new BigInteger(string.value));
                        } catch (NumberFormatException exception) {
                            try {
                                yield new EsonFloat(string.value, new BigDecimal(string.value));
                            } catch (NumberFormatException e) {
                                yield new EsonString(string.value, string.delimiter);
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

    private EsonElement nextStructure() {
        var context = this.context;

        switch (this.lexeme.token()) {
            case ARRAY_BEGIN -> {
                this.context = Context.ARRAY;
                var array = new EsonArray();
                this.endArray(array, true);
                this.context = context;

                return array;
            }
            case MAP_BEGIN -> {
                this.context = Context.MAP;
                var map = new EsonMap();
                this.endMap(map, true);
                this.context = context;

                return map;
            }
            default -> throw new IllegalArgumentException(this.lexeme.token().toString());
        }
    }

    private void endArray(EsonArray array, boolean close) {
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

    private void endMap(EsonMap map, boolean close) {
        while (this.advanceCode()) {
            if (this.lexeme.token() == Token.MAP_END) {
                return;
            }

            var element = this.nextElement(false);

            if (element instanceof EsonPair pair) {
                map.put(((EsonPrimitive) pair.a).stringValue(), pair.b);
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
