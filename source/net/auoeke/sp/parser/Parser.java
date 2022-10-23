package net.auoeke.sp.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpArray;
import net.auoeke.sp.element.SpBoolean;
import net.auoeke.sp.element.SpElement;
import net.auoeke.sp.element.SpFloat;
import net.auoeke.sp.element.SpInteger;
import net.auoeke.sp.element.SpMap;
import net.auoeke.sp.element.SpNull;
import net.auoeke.sp.element.SpPair;
import net.auoeke.sp.element.SpPrimitive;
import net.auoeke.sp.element.SpString;
import net.auoeke.sp.parser.lexer.LexemeIterator;
import net.auoeke.sp.parser.lexer.Lexer;
import net.auoeke.sp.parser.lexer.error.ErrorKey;
import net.auoeke.sp.parser.lexer.error.SyntaxException;
import net.auoeke.sp.parser.lexer.lexeme.CommentLexeme;
import net.auoeke.sp.parser.lexer.lexeme.Lexeme;
import net.auoeke.sp.parser.lexer.lexeme.StringLexeme;
import net.auoeke.sp.parser.lexer.lexeme.Token;

public class Parser {
    private final List<CommentLexeme> comments = new ArrayList<>();
    private final LexemeIterator iterator;
    private Context context = Context.FILE;
    private Lexeme lexeme;

    public Parser(String sp, StructuredProperties.Option... options) {
        this.iterator = new Lexer(sp).iterator();
    }

    public SpElement parse() {
        if (!this.advanceCode()) {
            return new SpMap();
        }

        var element = this.nextElement(false);

        if (element instanceof SpPair pair) {
            if (!pair.a.type().primitive()) {
                throw this.error(ErrorKey.COMPOUND_KEY);
            }

            var map = new SpMap();

            if (map.put(((SpPrimitive) pair.a).stringValue(), pair.b) != null) {
                this.error(ErrorKey.DUPLICATE_KEY, this.lexeme);
            }

            this.consumeSeparator(false);
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

            var array = new SpArray();
            array.add(element);
            this.endArray(array, false);

            return array;
        }

        return element;
    }

    private SpElement nextElement(boolean advance) {
        if (advance && !this.advanceCode()) {
            throw this.error(ErrorKey.EOF);
        }

        var element = switch (this.lexeme.token()) {
            case STRING -> {
                var string = (StringLexeme) this.lexeme;

                if (string.delimiter != null) {
                    yield new SpString(string.value, string.delimiter);
                }

                yield switch (string.value) {
                    case "false" -> SpBoolean.of(false);
                    case "true" -> SpBoolean.of(true);
                    case "null" -> SpNull.instance;
                    default -> {
                        try {
                            yield new SpInteger(string.value, new BigInteger(string.value));
                        } catch (NumberFormatException exception) {
                            try {
                                yield new SpFloat(string.value, new BigDecimal(string.value));
                            } catch (NumberFormatException e) {
                                yield new SpString(string.value, string.delimiter);
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
                        var array = new SpArray();
                        this.endArray(array, true);

                        yield array;
                    }
                    case MAP_BEGIN -> {
                        this.context = Context.MAP;
                        var map = new SpMap();
                        this.endMap(map, true);

                        yield map;
                    }
                    default -> throw this.error(ErrorKey.ILLEGAL_TOKEN, this.lexeme);
                };

                this.context = context;

                yield structure;
            }
        };

        var index = this.iterator.nextIndex();

        if (this.advance()) {
            var primitive = element.type().primitive();

            if (this.lexeme.token().begin()) {
                if (primitive) {
                    return new SpPair(element, this.nextElement(false));
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

                        return new SpPair(element, this.nextElement(true));
                    }
                }
            }
        }

        this.rewind(index);

        return element;
    }

    private void endArray(SpArray array, boolean close) {
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

    private void endMap(SpMap map, boolean close) {
        while (this.advanceCode()) {
            if (this.lexeme.token() == Token.MAP_END) {
                return;
            }

            var element = this.nextElement(false);

            if (element instanceof SpPair pair) {
                map.put(((SpPrimitive) pair.a).stringValue(), pair.b);
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
