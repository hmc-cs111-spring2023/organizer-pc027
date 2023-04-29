## Default configurations

The system will makes the following assumptions in segmenting text into corresponding categories. Information will be grouped as a keyword segment if the sentence contains the keyword. Any sentences in the same paragraph as the keyword will also be grouped in the same segment. Sentences are defined as a relatively gramaticized sentence, prioritizing periods ex. (spacebar) as end of a line.

A paragraph is made of multiple sentences and ends when a newline character is found (ex. when you hit enter/return while typing). A paragraph can also be a single bullet point as stylized by the word document.

The output file will be located in /organizer and named `organizer-output.docx` unless specified in the input program. Note that maximum segment length is not currently implemented.

Example programs and their outputs can be found in `/docs/ideal-examples` and `/docs/current-outputs`.

## Keyword Identification
A sentence is classified into a keyword category if any word (or more technically, a string of characters delimited by whitespace characters) in the sentence contains the keyword as a substring. For example, if the keyword is "process", words that add prefixes and suffixes to it like "processed" or "preprocess" will also count under the same keyword category. The only exception is if the keyword is wrapped in $ charaters, ex. "$process$", in which case only words spelled exactly that way will be categorized.

Any paragraphs that do not contain any of the keywords will be listed under "uncategorized" at the end of the word document. If a paragraph contains multiple keywords, it will appear in multiple categories corresponding to the found keywords.

## Program syntax
### Required fields
A program must start by specifying a .docx source file. This source file must be located under `/organizer/src/main/resources` folder.

A program must also specify at least 1 keyword with a bullet point. The most simple program one can write is the following:

```
source: essay-sample-input-synonymize.docx

keywords:
- hello
```

See `/docs/ideal-examples/ideal-simple.txt` for another example of an Organizer basic syntax.

### Keyword configurations
Multiple words can be considered the same "keyword". The category corresponding to that "keyword" will list segments of text (paragraphs) that has at least one of the words under it.

Additionally, wrapping a word in '$' signs will ask the system to search for the exact word only.

See `/docs/ideal-examples/ideal-synonyms.txt` for an example both configurations in action.

### Additional configurations
Users can specify the file output name. The parser also anticipates users to optionally specify the maximum segment length (aka how many lines can a paragraph in the resulting .docx contain), though this functionality is not yet implemented.

See `/docs/ideal-examples/ideal-fancy.txt` for an example all required and optional syntax in action.
