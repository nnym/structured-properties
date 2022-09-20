package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrArray;
import net.auoeke.lusr.element.LusrBoolean;
import net.auoeke.lusr.element.LusrElement;
import net.auoeke.lusr.element.LusrFloat;
import net.auoeke.lusr.element.LusrInteger;
import net.auoeke.lusr.element.LusrMap;
import net.auoeke.lusr.element.LusrNull;
import net.auoeke.lusr.element.LusrPair;
import net.auoeke.lusr.element.LusrString;

public class LusrSerializer {
    protected final String indentation;
    // protected final int elementLength = 16;
    // protected final Deque<Integer> lengths = new ArrayDeque<>();
    protected int depth;
    // protected int line;
    // protected int offset;
    // protected int length;
    protected boolean prependLine;

    public LusrSerializer(String indentation) {
        this.indentation = indentation;
    }

    public final synchronized Appendable serialize(Appendable output, LusrElement element) {
        this.depth = -1;
        // this.line = 0;
        // this.offset = 0;
        return this.append(output, element);
    }

    protected synchronized Appendable append(Appendable output, LusrElement element) {
        if (element instanceof LusrBoolean boolea) return this.append(output, boolea);
        if (element instanceof LusrInteger number) return this.append(output, number);
        if (element instanceof LusrFloat number) return this.append(output, number);
        if (element instanceof LusrString string) return this.append(output, string);
        if (element instanceof LusrPair pair) return this.append(output, pair);
        if (element instanceof LusrArray array) return this.append(output, array);
        if (element instanceof LusrMap map) return this.append(output, map);
        if (element instanceof LusrNull nul) return this.append(output, nul);

        throw new IllegalArgumentException(String.valueOf(element));
    }

    protected Appendable append(Appendable output, LusrBoolean element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, LusrInteger element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, LusrFloat element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, LusrString element) {
        return this.append(output, element.toString());
    }

    protected Appendable append(Appendable output, LusrPair element) {
        this.append(this.append(output, element.a), ' ');

        if (element.b.type().primitive()) {
            this.append(output, "= ");
        }

        return this.append(output, element.b);
    }

    protected Appendable append(Appendable output, LusrArray array) {
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

    protected Appendable append(Appendable output, LusrMap map) {
        var surround = this.descend() > 0;

        try {
            if (surround) {
                this.append(output, '{');
            }

            if (map.size() > 1) {
                var iterator = map.entrySet().iterator();

                if (!surround) {
                    var entry = iterator.next();
                    this.append(output, new LusrPair(new LusrString(entry.getKey()), entry.getValue()));
                }

                while (iterator.hasNext()) {
                    var entry = iterator.next();
                    this.newline(output, false);
                    this.append(output, new LusrPair(new LusrString(entry.getKey()), entry.getValue()));
                }

                if (surround) {
                    this.newline(output, true);
                }
            } else {
                map.forEach((key, value) -> this.append(output, new LusrPair(new LusrString(key), value)));
            }
        } finally {
            this.ascend();
        }

        if (surround) {
            this.append(output, '}');
        }

        return output;
    }

    protected Appendable append(Appendable output, LusrNull element) {
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

    protected int appendStructureElement(Appendable output, LusrElement element) {
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
