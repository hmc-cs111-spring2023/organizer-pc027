import java.io.{InputStream, FileOutputStream}

import docorg.parser._
import docorg.ir._
import docorg.semantics._

// "integration" test rather than unit test because of time
// but also because the only intermediate function is sortParagraphsIntoSections 
// and sortParagraphSection requires XWPFParagraph, whose constructor params
// are difficult to mock

// TODO: test with fixtures: https://scalameta.org/munit/docs/fixtures.html
class MainInterpreterSpec extends munit.FunSuite {

// tests are commented out because running them will actually generate
// a file in the root folder

//   test("run ideal-simple") {
//     runFile("../docs/ideal-examples/ideal-simple.txt")
//   }

//   test("run ideal-synonym") {
//     runFile("../docs/ideal-examples/ideal-synonyms.txt")
//   }

//   test("run ideal-fancy") {
//     runFile("../docs/ideal-examples/ideal-fancy.txt")
//   }

  intercept[java.io.FileNotFoundException]{
    runFile("../docs/ideal-examples/ideal-unfound.txt")
  }
}
