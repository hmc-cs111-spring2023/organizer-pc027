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
def doEval(config: docorg.ir.Document): Unit =
  try {
    val parsedConfigs = config match {
      case Configurations(path, keys) => {
        handleDocumentIO(path, keys)
      }
      case _ => throw new IllegalArgumentException
    }
  } catch {
    case e: Exception => println(error("invalid configuration found"))
  }

/** Parse a line and potentially evaluate it */
def parseAndEvalLine(input: String) =
  DocumentParser(input) match
    case DocumentParser.Success(ast, _) => {
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

// TODO: add Try to file input and output
// TODO: get file inputs and outputs to go to non-default locations
def handleDocumentIO(
  path: String,
  keywords: List[String]): Unit = {
  
  // Java's way of creating an InputStream
  val javaStream: InputStream = main.getClass.getResourceAsStream(path)

  val output = parseDocument(javaStream, keywords)

  // Close original stream
  javaStream.close()

  // Write output file
  val outputStream = new FileOutputStream("generated-sample-output-connected.docx")
  output.write(outputStream)
}

@main
def main(args: String*): Unit =
  // BasicConfigurator.configure();

  // val logger = Logger.getLogger(this.getClass.getName)
  // logger.info("###### logging #####")

  // handleDocumentIO()
  runFile(args(0))
