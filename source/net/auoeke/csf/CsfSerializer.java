package net.auoeke.csf;

import net.auoeke.csf.element.CsfArray;
import net.auoeke.csf.element.CsfBoolean;
import net.auoeke.csf.element.CsfElement;
import net.auoeke.csf.element.CsfFloat;
import net.auoeke.csf.element.CsfInteger;
import net.auoeke.csf.element.CsfMap;
import net.auoeke.csf.element.CsfNull;
import net.auoeke.csf.element.CsfPair;
import net.auoeke.csf.element.CsfString;

public class CsfSerializer {
    protected final String indentation;
    // protected final int elementLength = 16;
    // protected final Deque<Integer> lengths = new ArrayDeque<>();
    protected int depth;
    // protected int line;
    // protected int offset;
    // protected int length;
    protected boolean prependLine;

    public CsfSerializer(String indentation) {
        this.indentation = indentation;
    }

    public final synchronized Appendable serialize(Appendable output, CsfElement element) {
        this.depth = -1;
        // this.line = 0;
        // this.offset = 0;
        return this.append(output, element);
    }

    protected synchronized Appendable append(Appendable output, CsfElement element) {
        if (element instanceof CsfBoolean boolea) return this.append(output, boolea);
        if (element instanceof CsfInteger number) return this.append(output, number);
        if (element instanceof CsfFloat number) return this.append(output, number);
        if (element instanceof CsfString string) return this.append(output, string);
        if (element instanceof CsfPair pair) return this.append(output, pair);
        if (element instanceof CsfArray array) return this.append(output, array);
        if (element instanceof CsfMap map) return this.append(output, map);
        if (element instanceof CsfNull nul) return this.append(output, nul);

        throw new IllegalArgumentException(String.valueOf(element));
    }

    protected Appendable append(Appendable output, CsfBoolean element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, CsfInteger element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, CsfFloat element) {
        return this.append(output, element.stringValue());
    }

    protected Appendable append(Appendable output, CsfString element) {
        return this.append(output, element.toString());
    }

    protected Appendable append(Appendable output, CsfPair element) {
        this.append(this.append(output, element.a), ' ');

        if (element.b.type().primitive()) {
            this.append(output, "= ");
        }

        return this.append(output, element.b);
    }

    protected Appendable append(Appendable output, CsfArray array) {
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

    protected Appendable append(Appendable output, CsfMap map) {
        var surround = this.descend() > 0;

        try {
            if (surround) {
                this.append(output, '{');
            }

            if (map.size() > 1) {
                var iterator = map.entrySet().iterator();

                if (!surround) {
                    var entry = iterator.next();
                    this.append(output, new CsfPair(new CsfString(entry.getKey()), entry.getValue()));
                }

                while (iterator.hasNext()) {
                    var entry = iterator.next();
                    this.newline(output, false);
                    this.append(output, new CsfPair(new CsfString(entry.getKey()), entry.getValue()));
                }

                if (surround) {
                    this.newline(output, true);
                }
            } else {
                map.forEach((key, value) -> this.append(output, new CsfPair(new CsfString(key), value)));
            }
        } finally {
            this.ascend();
        }

        if (surround) {
            this.append(output, '}');
        }

        return output;
    }

    protected Appendable append(Appendable output, CsfNull element) {
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

    protected int appendStructureElement(Appendable output, CsfElement element) {
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
