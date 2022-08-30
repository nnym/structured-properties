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
import net.auoeke.eson.parser.lexer.error.ErrorKey;
import net.auoeke.eson.parser.lexer.error.SyntaxException;
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

        if (element instanceof EsonPair pair) {
            if (!pair.a.type().primitive()) {
                throw this.error(ErrorKey.COMPOUND_KEY);
            }

            var map = new EsonMap();

            if (map.put(((EsonPrimitive) pair.a).stringValue(), pair.b) != null) {
                this.error(ErrorKey.DUPLICATE_KEY, this.lexeme);
            }

            this.endMap(map, false);

            return map;
        }

        if (element != null && this.consumeSeparator(false)) {
            if (this.lexeme.token() == Token.NEWLINE) {
                if (this.advanceCode()) {
                    this.iterator.previous();
                } else {
                    return element;
                }
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

                throw this.error(ErrorKey.COMPOUND_STRUCTURE_KEY);
            } else if (this.lexeme.token().primitive()) {
                throw this.error(ErrorKey.PRIMITIVE_RIGHT_NO_MAPPING);
            } else {
                this.rewind(index);

                if (this.advanceCode()) {
                    if (this.lexeme.token() == Token.MAPPING) {
                        if (this.context == Context.MAP && !primitive) {
                            throw this.error(ErrorKey.COMPOUND_KEY);
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
            throw this.error(ErrorKey.EOF);
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
                var context = this.context;

                var structure = switch (this.lexeme.token()) {
                    case ARRAY_BEGIN -> {
                        this.context = Context.ARRAY;
                        var array = new EsonArray();
                        this.endArray(array, true);

                        yield array;
                    }
                    case MAP_BEGIN -> {
                        this.context = Context.MAP;
                        var map = new EsonMap();
                        this.endMap(map, true);

                        yield map;
                    }
                    default -> throw this.error(ErrorKey.WRONG_TOKEN, this.lexeme);
                };

                this.context = context;

                yield structure;
            }
        };
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
            this.error(ErrorKey.UNCLOSED_ARRAY);
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
                this.error(ErrorKey.NO_VALUE);
            }

            this.consumeSeparator(true);
        }

        if (close) {
            this.error(ErrorKey.UNCLOSED_MAP);
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
                    throw this.error(ErrorKey.CONSECUTIVE_COMMA);
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
                throw this.error(ErrorKey.NO_SEPARATOR);
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
            throw this.error(ErrorKey.END_OUT_OF_CONTEXT, this.lexeme.token().character());
        }

        return this.lexeme;
    }

    private SyntaxException error(ErrorKey error, Object... arguments) {
        throw new SyntaxException(this.lexeme.position(), error, arguments);
    }
}
