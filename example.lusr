/* regex pseudogrammar
FILE -> ELEMENT (SEPARATOR ELEMENT)* | PAIR (SEPARATOR PAIR)*
STRUCTURE -> ARRAY | MAP
PRIMITIVE -> NULL | BOOLEAN | INTEGER | FLOAT | STRING
ELEMENT -> PRIMITIVE | PAIR | STRUCTURE

INTEGER -> (+|-)?\d+
FLOAT -> \d+"."\d+
BOOLEAN -> "false" | "true"
NULL -> "null"
STRING -> (?s) DELIMITER .* DELIMITER | CHARACTER ([\s[^\n]]* CHARACTER)*
PAIR -> ELEMENT "=" ELEMENT | STRING STRUCTURE
MAP_PAIR -> PRIMITIVE ("=" ELEMENT | STRUCTURE)
ARRAY -> "[" (ELEMENT (SEPARATOR ELEMENT)*)? "]"
MAP -> "{" (PAIR (SEPARATOR MAP_PAIR)*)? "}"

CHARACTER -> [^]\[{}=\s,]
DELIMITER -> ["'`](|{3,})
SEPARATOR -> [\n,]
*/

/* an example /* nested */ comment */

buildscript {
    dependencies {
        classpath = [net.example:old-gradle-plugin:1.2.3]
    }
}

name = lusr
version = 1.2.3
description = '
    lusr supports
    multi-line comments
'

repositories [
    mavenCentral
    ~/.m2/repository ## the local repository
    https://repo.maven.apache.org/maven2/
]

dependencies {
    implementation = [gradleApi, org.ow2.asm:asm:9.0-SNAPSHOT]
    testImplementation = org.junit.jupiter:junit-jupiter:5.8.0
}
