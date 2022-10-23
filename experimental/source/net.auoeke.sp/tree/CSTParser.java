package net.auoeke.sp.tree;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import net.auoeke.sp.element.SpArray;
import net.auoeke.sp.element.SpBoolean;
import net.auoeke.sp.element.SpFloat;
import net.auoeke.sp.element.SpInteger;
import net.auoeke.sp.element.SpNull;
import net.auoeke.sp.element.SpString;
import net.auoeke.sp.parser.Context;
import net.auoeke.sp.parser.lexer.LexemeIterator;
import net.auoeke.sp.parser.lexer.Lexer;
import net.auoeke.sp.parser.lexer.error.ErrorKey;
import net.auoeke.sp.parser.lexer.lexeme.Lexeme;
import net.auoeke.sp.parser.lexer.lexeme.StringLexeme;
import net.auoeke.sp.parser.lexer.lexeme.Token;

@SuppressWarnings("DuplicatedCode")
public class CSTParser {
	private final List<Error> errors = new ArrayList<>();
	private final LexemeIterator iterator;
	private Context context = Context.FILE;
	private Lexeme lexeme;

    public CSTParser(String sp) {
        this.iterator = new Lexer(sp).iterator();
    }

    public SourceUnit parse() {
        var source = new SourceUnit();

        if (this.advanceCode()) {
            var element = this.nextElement(false);

			if (element != null) {
				if (element instanceof PairTree pair) {
				    if (!pair.a.isPrimitive()) {
				        this.error(ErrorKey.COMPOUND_KEY);
				    }

				    var map = new MapTree();
				    this.endMap(map, false);
				    source.add(map);
				} else if (this.consumeSeparator(false)) {
				    if (this.lexeme.token().newline()) {
				        if (this.advanceCode()) {
				            this.iterator.previous();
				        } else {
							source.add(element);
				            return source;
				        }
				    }

				    var array = new ArrayTree();
				    array.add(element);
				    this.endArray(array, false);
				    source.add(array);
				} else {
					source.add(element);
				}
			}
        }

        return source;
    }

    private Node nextElement(boolean advance) {
        if (advance && !this.advanceCode()) {
            this.error(ErrorKey.EOF, Error.Offset.AFTER);
			return null;
        }

        var element = switch (this.lexeme.token()) {
            case STRING -> {
                var string = (StringLexeme) this.lexeme;

                switch (string.value) {
                    case "false" -> {
						yield new BooleanNode(false);
					}
                    case "true" -> {
						yield new BooleanNode(true);
					}
                    case "null" -> {
						yield new NullNode();
					}
                    default -> {
                        try {
                            yield new IntegerNode(string.value, new BigInteger(string.value));
                        } catch (NumberFormatException exception) {
                            try {
                                yield new FloatNode(string.value, new BigDecimal(string.value));
                            } catch (NumberFormatException e) {}
                        }
                    }
                }

	            yield new StringNode(string.delimiter, string.value);
            }
            default -> {
                var context = this.context;

                var structure = switch (this.lexeme.token()) {
                    case ARRAY_BEGIN -> {
                        this.context = Context.ARRAY;
                        var array = new ArrayTree();
                        this.endArray(array, true);

                        yield array;
                    }
                    case MAP_BEGIN -> {
                        this.context = Context.MAP;
                        var map = new MapTree();
                        this.endMap(map, true);

                        yield map;
                    }
                    default -> {
						this.error(ErrorKey.ILLEGAL_TOKEN, this.lexeme);
						this.advance();
						yield null;
					}
                };

                this.context = context;

                yield structure;
            }
        };

        var index = this.iterator.nextIndex();

        if (this.advance()) {
            var primitive = element.isPrimitive();

            if (this.lexeme.token().begin()) {
                if (primitive) {
	                return new PairTree(element, this.nextElement(false));
                }

                this.error(ErrorKey.COMPOUND_STRUCTURE_KEY);
            } else if (this.lexeme.token().primitive()) {
                this.error(ErrorKey.PRIMITIVE_RIGHT_NO_MAPPING, Error.Offset.BEFORE);
            }

            this.rewind(index);

            if (this.advanceCode()) {
                if (this.lexeme.token() == Token.MAPPING) {
                    if (this.context == Context.MAP && !primitive) {
                        this.error(ErrorKey.COMPOUND_KEY);
                    }

	                return new PairTree(element, this.nextElement(true));
                }
            }
        }

        this.rewind(index);

        return element;
    }

    private void endArray(ArrayTree array, boolean close) {
        while (this.advanceCode()) {
            if (this.lexeme.token() == this.context.end()) {
                return;
            }

            array.element(this.nextElement(false));
            this.consumeSeparator(true);
        }

        if (close) {
            this.error(ErrorKey.UNCLOSED_ARRAY);
        }
    }

    private void endMap(MapTree map, boolean close) {
        while (this.advanceCode()) {
            if (this.lexeme.token() == Token.MAP_END) {
                return;
            }

            var element = this.nextElement(false);

            if (element instanceof PairTree pair) {
	            if (map.entry(pair) != null) {
		            this.error(ErrorKey.DUPLICATE_KEY, this.lexeme);
	            }
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

    private boolean consumeSeparator(boolean require) {
        if (this.advance()) {
            if (this.lexeme.token().newline()) {
                return true;
            }

            if (this.lexeme.token() == Token.COMMA) {
                while (this.advanceCode() && this.lexeme.token() == Token.COMMA) {
                    this.error(ErrorKey.CONSECUTIVE_COMMA);
                }

	            this.rewind(this.iterator.previousIndex());

                return true;
            }

            this.iterator.previous();

            if (this.lexeme.token() == this.context.end()) {
                return true;
            }

            if (require) {
                this.error(ErrorKey.NO_SEPARATOR, Error.Offset.BEFORE);
            }
        }

        return false;
    }

    private boolean advanceCode() {
        while (this.iterator.hasNext()) {
            if (!this.next().token().newline()) {
                return true;
            }
        }

        return false;
    }

	private boolean advanceAny() {
		if (this.iterator.hasNext()) {
			this.next();
			return true;
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
            this.error(ErrorKey.END_OUT_OF_CONTEXT, this.lexeme.token().character());
			return this.advanceAny() ? this.lexeme : null;
        }

        return this.lexeme;
    }

	private void error(ErrorKey error, Object... arguments) {
		this.error(error, Error.Offset.NONE, arguments);
	}

    private void error(ErrorKey error, Error.Offset offset, Object... arguments) {
        this.errors.add(new Error(this.lexeme, offset, error, arguments));
    }
}
