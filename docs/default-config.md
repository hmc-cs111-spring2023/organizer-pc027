## Default configurations

The system will make the following assumptions if no "configuration" section is defined in the program.
Information will be grouped as a keyword segment if:
- The sentence contains the keyword.
- The sentence does not contain any keywords and is within 7 lines before/after the keyword is mentioned.

Keyword segments will end "early" (before the 7 line limit) if:
- The current line ends with a definitive break (an empty line, 2 newline characters, etc.)
- The current line contains another keyword. If the current line contains multiple keywords, place the line
in the corresponding keyword segment as well as "multiple keywords detected" 
- The line is a repeat of the same punctuation character (7 or more).

Lines are defined as:
- A relatively gramaticized sentence, prioritizing periods 
    ex. (.<spacebar) as end of a line.
- A bullet point (denoted by - or *)
- A string of words that begins with a newline character (\n)
- Quotes - from starting " to end " is considered 1 line.

The output file type will be the same as the input file type.

## Advance configurations/settings
maximum segment length

synonymize

file type

## Tie-breaking

keywords
literal vs keyword variations (spelling)

"synonyms"

lines

## Output format
The output file would have at least n parts for n keywords.
Two more parts might be included if the following categories are found:
- 1 part for multiple keywords
- 1 part for unsorted information.
