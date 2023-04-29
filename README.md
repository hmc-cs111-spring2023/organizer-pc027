## Organizer DSL

### Description
This is a DSL for coarsely organizing Microsoft Word Documents (.docx extension only). To see sample syntax and outputs, please visit `/docs/ideal-examples` and `configuration-details.md` for a more detailed discussion of programming in this DSL.

### Usage
This is an sbt-generated project. To use Organizer, you must have (1) a .txt file containing your Organizer program, and (2) a word document to organize located in the folder `/organizer/src/main/resources`. 

To run your program and obtain an output:
1. Download sbt to your computer. Instructions to download it can be found [here](https://www.scala-sbt.org/download.html).
2. Go into the organizer folder (i.e. `cd organizer`)
3. Run the command in the terminal: `sbt` to open the sbt console
4. Run the command in the sbt console: `run <path_to_file>`. For example, `sbt run ../docs/ideal-examples/ideal-simple.txt` to run the example ideal-simple program. To exit the sbt console, press `control+c` on Mac.

### Demo
https://user-images.githubusercontent.com/63136975/235287027-05c130f0-c764-4ddc-b672-c0a01840b035.mp4

## DSL background

### Motivation and design decisions
This project came about as a result of interviewing my friend about her experiences with computers. It began with a discussion of creating an itinerary for a self-planned trip, and we were both quickly reminded of the pain of brainstorming for essays in our college years. Organizer is built with the intention of allowing anyone who works with .docx, including non-programmers, to declare how they want to reorganize files in a familiar way. Therefore, I chose to implement an external DSL to have more flexibility in creating a syntax that feel like writing a wishlist for the look of the organized file.

### Future Improvements
- Wrap the DSL in a user-friendly interface for non-programmers.
- Resolve file input issues so that the system can take in a file from anywhere in the end user's computer system. Currently, the input file must be located under `/organizer/src/main/resources`.
- Implement max segment length in the system. Currently, end-users can provide this value and the parser will capture it, but there is no algorithm to use it to create segments. 

