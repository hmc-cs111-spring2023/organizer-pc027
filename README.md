## Organizer DSL

### Description
This is a DSL for coarsely organizing Microsoft Word Documents (.docx extension only). To see sample syntax and outputs, please visit `/docs/ideal-examples` and `configuration-details.md` for a more detailed discussion of programming in this DSL.

### Usage
This is an sbt-generated project. To use the language, you must have (1) a .txt file with your program syntax, and (2) a word document to organize located in the folder `/organizer/src/main/resources`. 

To run your program and obtain an output:
1. Download sbt to your computer. Instructions to download it can be found [here](https://www.scala-sbt.org/download.html).
2. Go into the organizer folder (i.e. `cd organizer`)
3. Run the command in the terminal: `sbt run <path_to_file>`. For example, `sbt run ../docs/ideal-examples/ideal-simple.txt` to run the example ideal-simple program.
- To exit sb console, press control+c on Mac.

### Demo
[placeholder here]