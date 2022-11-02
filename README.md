Structured Properties is approximately a fusion of JSON and Java's `Properties`
intended to be a more concise, writable, readable and lucid alternative to serialization formats like JSON.

The preferred extension for Structured Properties files is `str`.

### Primitive elements
- A **null** element indicates an absence of a value and is represented as `null`.
- A **Boolean** element is represented as `true` or `false`.
- An **integer** is a decimal integer.
  - `92233720368547758070`
  - `-12`
  - `+3`
- A **float** is a decimal rational number with optional order of magnitude
and possibly a leading or trailing radix point.
  - `-3.141592653589793238462643383279502884197169399`
  - `123.4e6`
- A **string** is either
  - a sequence of characters that are not reserved for other tokens that is terminated by a newline or comma or
    - <code>//# Quotation marks ("), apostrophes (') and backticks (`) are included. :^)</code>
  - a sequence of characters delimited by `"`, `'`, <code>`</code> or a string of any number >= 3 thereof.
    - `"quoted string"`
    - `'apostrophed string'`
    - <code>\`backticked string\`</code>
    - ```
      """
      public static void main(String... args) {
          System.out.println("There's no escaping the quotation marks.");
      }
      """
      ```

### Compound elements
- A **pair** comprises 2 elements joined by `=` or a primitive element followed by a structure (array or map).
  - `1.0 = 1`
  - `pocket [wallet, phone]`
  - `times {start = 8:00, end = 13:00}`
- An **array** is a sequence of elements separated by `\n` or `,` and enclosed in `[]`.
  - `[string, true]`
  - ```
    [
        element 0
        element 1
    ]
    ```
- A **map** is a sequence enclosed in `{}` of pairs with primitive keys and that are separated by `\n` or `,`.
  - `{key 0 = value 0, key 1 = value 1}`
  - ```
    {
        color = dark gray, length = 40
        pocket [wallet, phone]
    }
    ```

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
## This file represents a map.
autosave interval = 5
```

### Comments
- A **line comment** starts with `##` and lasts until the next line.
  - `time = 6:00 ## My alarm is set to ring at this time.`
- A **block comment** starts with `/*`, is terminated by `*/` and can be nested.
  - ```
    /* /* nest */
    The comment continues. */
    ```
