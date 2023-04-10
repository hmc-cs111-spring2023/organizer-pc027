package docorg.ir

trait Document 
// final object after parsing to create output document
case class Configurations(path: FilePath, format: Format, keywords: Keywords) extends Document

// metadata
case class FilePath(path: String) extends Document
case class Format(ext: String) extends Document
case class Keywords(w: List[String]) extends Document

// Document information (for evaluation steps)
// case class Keyword(word: String) extends Document
// case class Sections(s: List[Segment]) extends Document
// case class Segment(keyword: Keyword, lines: List[String]) extends Document

// Placeholder functions to create AST
// given a list of keywords, create all sections of the output (reorganized, document)
// def createSections(wordList: List[String]): Unit = {

// }


// def createSegment(keyword: String): Segment = {
//     return Segment("temp", List("line1"))
// }