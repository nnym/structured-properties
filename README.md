### Primitive elements
- A **null** element indicates an absence of a value and is represented as `null`.
- A **Boolean** element is represented as `true` or `false`.
- An **integer** is a string of digits with minimum value [Long.MIN_VALUE], maximum value [Long.MAX_VALUE]
and optional preceding `[+-]`.
  - `21474836480`
  - `-12`
  - `+3`
- A **float** is a floating point number as specified at [`Double::valueOf`](https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#valueOf-java.lang.String-).
  - `1.0`
  - `-123.0D`
  - `1e6`
- A **string** is either
  - a sequence of characters that are not reserved for other tokens that is terminated by a newline or comma or
    - `hello word`
    - `//# This is still a string. :)`
  - a sequence of characters delimited by `'`, `"` or a string of any number thereof >= 3.
    - `"Can't touch this".`
    - `'Can touch "this"'.`
    - ```
      """
      public static void main(String... args) {
          System.out.println("");
      }
      """
      ```

### Compound elements
- A **pair** comprises 2 elements joined by `=` or a primitive element followed by a structure (array or map).
  - `1.0 = 1`
  - `pocket [wallet, phone]`
  - `times {start = 8:00, end = 13:00}`.
- An **array** is a sequence of elements separated by `\n` or `,` and enclosed in `[]`.
  - `[string, true]`
- A **map** is a sequence of pairs whose left elements are primitive separated by `\n` or `,` and enclosed in `{}`.
  - `{key = value}`
  - `{pocket [wallet, phone]}`

Sole top-level structures' delimiters can be omitted:
```
## This file represents an array.
one
2
three
```
```
## This file represents a map; not an array of pairs.
key = value
question = Remember properties?
```
```
## This file represents a pair.
key = value
## Adding another element here would produce a map.
```

### Comments
- A **line comment** starts with `##` and lasts until the next line.
  - `time = 6:00 ## My alarm is set to ring at this time.`
- A **block comment** starts with `/*`, is terminated by `*/` and can be nested.
  - ```
    /* /* nest */
    The comment continues.
    */
    ```
