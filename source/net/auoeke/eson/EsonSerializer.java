package net.auoeke.eson;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import net.auoeke.eson.element.EsonArray;
import net.auoeke.eson.element.EsonBoolean;
import net.auoeke.eson.element.EsonElement;
import net.auoeke.eson.element.EsonFloat;
import net.auoeke.eson.element.EsonInteger;
import net.auoeke.eson.element.EsonMap;
import net.auoeke.eson.element.EsonNull;
import net.auoeke.eson.element.EsonPair;
import net.auoeke.eson.element.EsonString;

public class EsonSerializer {
    protected final String indentation;
    // protected final int elementLength = 16;
    // protected final Deque<Integer> lengths = new ArrayDeque<>();
    protected int depth;
    // protected int line;
    // protected int offset;
    // protected int length;
    protected boolean prependLine;

    public EsonSerializer(String indentation) {
        this.indentation = indentation;
    }

    public final synchronized Appendable serialize(Appendable output, EsonElement element) {
        this.depth = -1;
        // this.line = 0;
        // this.offset = 0;
        return this.append(output, element);
    }

    protected synchronized Appendable append(Appendable output, EsonElement element) {
        if (element instanceof EsonBoolean boolea) return this.append(output, boolea);
        if (element instanceof EsonInteger number) return this.append(output, number);
        if (element instanceof EsonFloat number) return this.append(output, number);
        if (element instanceof EsonString string) return this.append(output, string);
        if (element instanceof EsonPair pair) return this.append(output, pair);
        if (element instanceof EsonArray array) return this.append(output, array);
        if (element instanceof EsonMap map) return this.append(output, map);
        if (element instanceof EsonNull nul) return this.append(output, nul);

        throw new IllegalArgumentException(String.valueOf(element));
    }

    protected Appendable append(Appendable output, EsonBoolean element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, EsonInteger element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, EsonFloat element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, EsonString element) {
        return this.append(output, element.toString());
    }

    protected Appendable append(Appendable output, EsonPair element) {
        this.append(this.append(output, element.a), ' ');

        if (element.b.type().primitive()) {
            this.append(output, "= ");
        }

        return this.append(output, element.b);
    }

    protected Appendable append(Appendable output, EsonArray array) {
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

    protected Appendable append(Appendable output, EsonMap map) {
        var surround = this.descend() > 0;

        try {
            if (surround) {
                this.append(output, '{');
            }

            if (map.size() > 1) {
                var iterator = map.entrySet().iterator();

                if (!surround) {
                    var entry = iterator.next();
                    this.append(output, new EsonPair(new EsonString(entry.getKey()), entry.getValue()));
                }

                while (iterator.hasNext()) {
                    var entry = iterator.next();
                    this.newline(output, false);
                    this.append(output, new EsonPair(new EsonString(entry.getKey()), entry.getValue()));
                }

                if (surround) {
                    this.newline(output, true);
                }
            } else {
                map.forEach((key, value) -> this.append(output, new EsonPair(new EsonString(key), value)));
            }
        } finally {
            this.ascend();
        }

        if (surround) {
            this.append(output, '}');
        }

        return output;
    }

    protected Appendable append(Appendable output, EsonNull element) {
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

    protected int appendStructureElement(Appendable output, EsonElement element) {
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
