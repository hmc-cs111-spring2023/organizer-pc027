package docorg.parser

import scala.util.parsing.combinator._
import scala.collection.mutable.ListBuffer

import docorg.ir._
import java.text.ParseException

// current progress: working on parsers for syntax features in ideal-simple.txt
object DocumentParser extends JavaTokenParsers with PackratParsers {
  val nonEmptyString = ".+".r
  val integer = "([0-9]+)$".r

  // parsing interface
  def apply(s: String): ParseResult[(Configurations, OptionalSettings)] = parseAll(combineRequiredOptional, s)


  def combineRequiredOptional: Parser[(Configurations, OptionalSettings)] = {
    combinedSections ~ opt(settings) ^^ {
      case configs ~ maybeOptions => {
        maybeOptions match {
          case Some(optionals) => (configs, optionals)
          case None => (configs, new OptionalSettings)
        }
      }
    }
  }

  ////////////////////////////////////////////////////////
  ////////////// REQUIRED syntax parsing /////////////////
  ////////////////////////////////////////////////////////
  // combine to parse entire input program
  def combinedSections: Parser[Configurations] = {
    source ~ (("keywords:" ~> wordList)) ^^ {
        case (path) ~ listOfWords => 
            Configurations(path, listOfWords)
    }
  }

  // parses the bulleted list into a list of keywords
  // and then use helper function to create segments
  def wordList: Parser[List[KeySegment]] = {
    rep("- " ~> keyword) ^^ {
      case words => {
        var keys = ListBuffer.empty[KeySegment]
        words.foreach(keyword => keys.append(keyword))

        keys.to(List)
      }
    }
  }
  
  def keyword: Parser[KeySegment] =
    nonEmptyString ^^ {case bulletPoint => {
      // look for multi-word keys (synonyms) for this segment
      val words: List[String] = bulletPoint.split(",").toList

      val segments: List[Word] = words.map(w => {
        val cleanWord = w.trim
        // check for user-specified exact spelling
        if (cleanWord.head == '$' && cleanWord.takeRight(1) == "$") {
          Word(cleanWord.tail.dropRight(1), true)
        } else {
          Word(cleanWord)
        }
      })

      KeySegment(segments)
    }}

  // parses source to extract file information + format to output
  // "source: essay_brainstorm.txt"
  def source: Parser[String] = 
    "source: " ~> ("[^.]*".r) ~ ("." ~> ("txt"|"docx")) ^^ {
        case filePath ~ format => filePath + "." + format
    }

  ////////////////////////////////////////////////////////
  ////////////// OPTIONAL syntax parsing /////////////////
  ////////////////////////////////////////////////////////
  def settings: Parser[OptionalSettings] =
    "settings:" ~> rep(option) ^^ {
      case optConfigs => {
        var segLength: Int = -1
        var synonyms: List[String] = List()
        var outputName: String = "organizer-result.docx"

        optConfigs.foreach((param, value) => {
          param match
            case "segLength" => segLength = value.toInt
            case "outputName" => outputName = value
            case _ => throw new ParseException(
              "parsing system error: optional settings parameter must be segLength, synonyms, or outputName", 0)
        })

        OptionalSettings(segLength, outputName)
      }
    }
  
  def option: Parser[(String, String)] =
    // * ~> everything before equals ~> everything after equals (and whitespace, if exists)
    "* " ~> (("[^=]*".r) ~ (("=" ~> opt(" ")) ~> (nonEmptyString))) ^^ {
      case optionsKey ~ optionsValue => {
        optionsKey.trim match
          case "maximum segment length" => ("segLength", optionsValue)
          case "output file name" => ("outputName", optionsValue)
          case _ => throw new ParseException("invalid optional settings found: " +
            "settings must be 'maximum segment length', 'synonymize', or 'output file name'", 0)
      }
    }

}