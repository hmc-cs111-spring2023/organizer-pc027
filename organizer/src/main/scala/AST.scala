package docorg.ir

trait Document 

// final object after parsing to create output document
case class Configurations(path: String, keywords: List[KeySegment]) extends Document {
    override def toString() = "Configurations(" + path + ", " + keywords.map(
        seg => "(" + seg.toString + ")"
    ) + ")"
}

case class KeySegment(words: List[Word]) extends Document {
    override def toString() = {
        "<" + words.tail.foldLeft[String](words.head.toString){
            (a, b) => a + ", " + b
        } + ">"
    }
}

case class Word(str: String, isExact: Boolean = false) extends Document {
    override def toString() = if isExact then str+"(exact phrasing)" else str
}

case class OptionalSettings(
    segLength: Int = -1,
    outputName: String = "organizer-output.docx") extends Document