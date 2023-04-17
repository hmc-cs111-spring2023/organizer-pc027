// import org.apache.poi.xwpf.usermodel._
import scala.io.Source
// import scala.io.BufferedSource
import Console.{RED, RESET}
import util.control.Breaks._

import java.io.{InputStream, FileOutputStream}
// import scala.collection.JavaConverters._
// import scala.collection.mutable.{Map, ArrayBuffer}

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

  handleDocumentIO()
  // runFile(args(0))

// TODO: add Try to file input and output
// TODO: get file inputs and outputs to go to non-default locations
def handleDocumentIO(keywords: List[String] = 
  List("thesis", "point-and-click", "neoliberal", "paratext", "conclusion")): Unit = {
  
  // Java's way of creating an InputStream
  val javaStream: InputStream = main.getClass.getResourceAsStream("sample-input-bulleted.docx")

  val output = parseDocument(javaStream)

  // Close original stream
  javaStream.close()

  // Write output file
  val outputStream = new FileOutputStream("generated-sample-output-bulleted.docx")
  output.write(outputStream)
}