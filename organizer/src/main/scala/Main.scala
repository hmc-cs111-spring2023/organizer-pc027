import org.apache.poi.xwpf.usermodel.XWPFDocument
import scala.io.Source
import Console.{RED, RESET}
import util.control.Breaks._

import docorg.parser._
import docorg.ir._
// import org.apache.poi.xwpf.XWPFTestDataSamples


/** Format an error message */
def error(message: String): String =
  s"${RESET}${RED}[Error] ${message}${RESET}"

/** Parse a line and potentially evaluate it */
def parseAndEvalLine(input: String) =
  DocumentParser(input) match
    case DocumentParser.Success(ast, _) => println("success")
    case e: DocumentParser.NoSuccess    => println(error(e.toString))

/** Parse a file and potentially evaluate it */
def runFile(filename: String): Unit =
  try {
    val input = Source.fromFile(filename).mkString
    parseAndEvalLine(input)
  } catch {
    case e: java.io.FileNotFoundException => println(error(e.getMessage))
  }

@main
def main(args: String*): Unit =
  runFile(args(0))

// @main def hello: Unit =
//   println("Hello, New York!")
//   println(msg)
//   parseSimpleDocument

// def msg = "I was compiled by Scala 3. :)"

// def parseSimpleDocument: Unit = {
//   // todo: open a word document and extract words
//   // val doc: XWPFDocument = XWPFTestDataSamples.openSampleDocument("sample.docx")
//   // val docExtractor: XWPFWordExtractor = new XWPFWordExtractor(doc) {
//   //   String text = extractor.getText();
//   //   println(text)
//   // }
// }