// import org.apache.poi.xwpf.usermodel._
import scala.io.Source
// import scala.io.BufferedSource
import Console.{RED, RESET}
import util.control.Breaks._

import java.io.{InputStream, FileOutputStream}

import docorg.parser._
import docorg.ir._
import docorg.semantics._


/** Format an error message */
def error(message: String): String =
  s"${RESET}${RED}[Error] ${message}${RESET}"

/** Evaluate an AST, but catch arithmetic exceptions */
def doEval(config: (docorg.ir.Configurations, docorg.ir.OptionalSettings)): Unit =
  try {
    val parsedConfigs = config match {
      case (Configurations(path, keys), settings) => {
        handleDocumentIO(path, keys, settings)
      }
    }
  } catch {
    case e: Exception => throw e
  }

/** Parse a line and potentially evaluate it */
def parseAndEvalLine(input: String) =
  DocumentParser(input) match
    case DocumentParser.Success(ast, _) => {
      print(ast)
      doEval(ast)
    }
    case e: DocumentParser.NoSuccess    => println(error(e.toString))

/** Parse a file and potentially evaluate it */
def runFile(filename: String): Unit =
  try {
    val input = Source.fromFile(filename).mkString
    parseAndEvalLine(input)
  } catch {
    case e: java.io.FileNotFoundException => println(error(e.getMessage))
  }

// TODO: get file inputs and outputs to go to non-default locations
def handleDocumentIO(
  path: String,
  keywords: List[KeySegment],
  settings: OptionalSettings): Unit = {
  // Java's way of creating an InputStream
  try {
    val javaStream: InputStream = main.getClass.getResourceAsStream(path)

    val output = parseDocument(javaStream, keywords, settings.segLength)

    // Close original stream
    javaStream.close()

    // Write output file
    val outputStream = new FileOutputStream(settings.outputName)
    output.write(outputStream)
  } catch {
    case e1: java.lang.NullPointerException => throw new NullPointerException(
      "java.lang.NullPointerException: check input word doc for spelling and location")
    case e2: Exception => throw new Exception("internal error: "+ e2.getMessage)
  }
}

@main
def main(args: String*): Unit =
  runFile(args(0))
