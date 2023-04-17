package docorg.ir

trait Document 
// final object after parsing to create output document
case class Configurations(path: String, keywords: List[String]) extends Document
