package net.auoeke.sp;

import net.auoeke.sp.element.SpArray;
import net.auoeke.sp.element.SpBoolean;
import net.auoeke.sp.element.SpElement;
import net.auoeke.sp.element.SpFloat;
import net.auoeke.sp.element.SpInteger;
import net.auoeke.sp.element.SpMap;
import net.auoeke.sp.element.SpNull;
import net.auoeke.sp.element.SpPair;
import net.auoeke.sp.element.SpString;

public class SpSerializer {
    protected final String indentation;
    // protected final int elementLength = 16;
    // protected final Deque<Integer> lengths = new ArrayDeque<>();
    protected int depth;
    // protected int line;
    // protected int offset;
    // protected int length;
    protected boolean prependLine;

    public SpSerializer(String indentation) {
        this.indentation = indentation;
    }

    public final synchronized Appendable serialize(Appendable output, SpElement element) {
        this.depth = -1;
        // this.line = 0;
        // this.offset = 0;
        return this.append(output, element);
    }

    protected synchronized Appendable append(Appendable output, SpElement element) {
        if (element instanceof SpBoolean boolea) return this.append(output, boolea);
        if (element instanceof SpInteger number) return this.append(output, number);
        if (element instanceof SpFloat number) return this.append(output, number);
        if (element instanceof SpString string) return this.append(output, string);
        if (element instanceof SpPair pair) return this.append(output, pair);
        if (element instanceof SpArray array) return this.append(output, array);
        if (element instanceof SpMap map) return this.append(output, map);
        if (element instanceof SpNull nul) return this.append(output, nul);

        throw new IllegalArgumentException(String.valueOf(element));
    }

    protected Appendable append(Appendable output, SpBoolean element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, SpInteger element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, SpFloat element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, SpString element) {
        return this.append(output, element.toString());
    }

    protected Appendable append(Appendable output, SpPair element) {
        this.append(this.append(output, element.a), ' ');

        if (element.b.type().primitive()) {
            this.append(output, "= ");
        }

        return this.append(output, element.b);
    }

    protected Appendable append(Appendable output, SpArray array) {
        var surround = this.descend() > 0;

        try {
            if (surround) {
                this.append(output, '[');
            }

            if (array.size() > 1) {
                var i = 0;

                if (!surround) {
                    this.appendStructureElement(output, array.get(0));
                    i = 1;
                }

                while (i < array.size()) {
                    this.newline(output, false);
                    this.append(output, array.get(i++));
                }

                if (surround) {
                    this.newline(output, true);
                }
            } else {
                for (var element : array) {
                    this.append(output, element);
                }
            }
        } finally {
            this.ascend();
        }

        if (surround) {
            this.append(output, ']');
        }

        return output;
    }

    protected Appendable append(Appendable output, SpMap map) {
        var surround = this.descend() > 0;

        try {
            if (surround) {
                this.append(output, '{');
            }

            if (map.size() > 1) {
                var iterator = map.entrySet().iterator();

                if (!surround) {
                    var entry = iterator.next();
                    this.append(output, new SpPair(new SpString(entry.getKey()), entry.getValue()));
                }

                while (iterator.hasNext()) {
                    var entry = iterator.next();
                    this.newline(output, false);
                    this.append(output, new SpPair(new SpString(entry.getKey()), entry.getValue()));
                }

                if (surround) {
                    this.newline(output, true);
                }
            } else {
                map.forEach((key, value) -> this.append(output, new SpPair(new SpString(key), value)));
            }
        } finally {
            this.ascend();
        }

        if (surround) {
            this.append(output, '}');
        }

        return output;
    }

    protected Appendable append(Appendable output, SpNull element) {
        return output.append(element.stringValue());
    }

    protected Appendable newline(Appendable output, boolean outdent) {
        this.append(output, System.lineSeparator());
        var d = this.depth;

        if (outdent) {
            --d;
        }

        if (d > 0) {
            this.append(output, this.indentation.repeat(d));
        }

        return output;
    }

    protected Appendable append(Appendable output, CharSequence sequence) {
        // this.length += sequence.length();
        return output.append(sequence);
    }

    protected Appendable append(Appendable output, char character) {
        // ++this.offset;
        return output.append(character);
    }

    protected int appendStructureElement(Appendable output, SpElement element) {
        // this.lengths.push(this.length);
        // this.length = 0;
        this.append(output, element);
        // var previous = this.lengths.pollLast();
        //
        // if (previous != null) {
        //     this.length += previous;
        // }

        return 0;//this.length;
    }

    protected int ascend() {
        return --this.depth;
    }

    protected int descend() {
        return ++this.depth;
    }
}
