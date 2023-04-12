import org.apache.poi.xwpf.usermodel._
import scala.io.Source
import scala.io.BufferedSource
import Console.{RED, RESET}
import util.control.Breaks._

import java.io.InputStream
import scala.collection.JavaConverters._

import docorg.parser._
import docorg.ir._


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
  // BasicConfigurator.configure();

  // val logger = Logger.getLogger(this.getClass.getName)
  // logger.info("###### logging #####")

  parseSimpleDocument
  // runFile(args(0))

def parseSimpleDocument: Unit = {
  // Java's way of creating an InputStream
  val javaStream: InputStream = main.getClass.getResourceAsStream("sample-input.docx")
  val doc: XWPFDocument = new XWPFDocument(javaStream)

  // Paragraphs are sections of information delimited by new lines
  val paragraphs: List[XWPFParagraph] = doc.getParagraphs().asScala.toList
  println(paragraphs.apply(5).getText())

  // close streams for original document
  doc.close()
  javaStream.close()
}