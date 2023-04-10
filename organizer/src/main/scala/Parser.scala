package docorg.parser

import scala.util.parsing.combinator._
import scala.collection.mutable.ListBuffer

import docorg.ir._

// current progress: working on parsers for syntax features in ideal-simple.txt
object DocumentParser extends JavaTokenParsers with PackratParsers {
  val nonEmptyString = ".+".r
  // parsing interface
  def apply(s: String): ParseResult[Configurations] = parseAll(combineSections, s)

  // combine to parse entire input program
  def combineSections: Parser[Configurations] = {
    source ~ (("keywords:" ~> wordList)) ^^ {
        case (path, fileType) ~ listOfWords => 
            Configurations(path, fileType, listOfWords)
    }
  }

  // parses the bulleted list into a list of keywords
  // and then use helper function to create segments
  def wordList: Parser[Keywords] = {
    rep("- " ~> nonEmptyString) ^^ {
      case words => {
        var keys = ListBuffer.empty[String]
        words.foreach(keyword => keys.append(keyword))

        Keywords(keys.to(List))
      }
    }
  }
  
  def keyword: Parser[String] =
    nonEmptyString ^^ {case word => word}

  // parses source to extract file information + format to output
  // "source: essay_brainstorm.txt"
  def source: Parser[(FilePath, Format)] = 
    "source: " ~> ("[^.]*".r) ~ ("." ~> "txt"|"docx") ^^ {
        case filePath ~ format => (FilePath(filePath), Format(format))
    }
}