package docorg.parser

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties
import docorg.ir._

object DocorgParsePropertySpec extends Properties("Parser") {
  
  // some syntactic sugar for expressing parser tests
  extension (input: String)
    def ~>(output: Expr) = {
      val result = CalcParser(input)
      result.successful && result.get == output
    }
  
  property("minimal required configurations") = {
    (name: String, keyword: String) => 
        s"source: $name \n keywords: - $keyword" ~> 
        Configurations(name, List(KeySegment(List(Word(keyword, false)))))
  }
}