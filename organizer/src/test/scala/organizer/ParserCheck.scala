package docorg.parser

import docorg.ir._
import scala.io.Source

class DocorgParseSpec extends munit.FunSuite {
  val defaultOptional = OptionalSettings(-1, "organizer-output.docx")
  
  test("minimal required syntax") {
    val testString = "source: essay-sample-input-synonymize.docx keywords: - hello"
    val result = DocumentParser(testString)
    val output = (Configurations("essay-sample-input-synonymize.docx", List(KeySegment(List(Word("hello", false)))))
      , defaultOptional)
    assert(result.successful)
    assertEquals(result.get, output)
  }

  test("invalid file extension".fail) {
    val testString = "source: essay-sample-input-synonymize.bad keywords: - hello"
    val result = DocumentParser(testString)
    assert(result.successful)
  }

  test("multiple keywords") {
    val testString = "source: essay-sample-input-synonymize.docx keywords: - hello \n- hi"
    val result = DocumentParser(testString)
    // two resulting segments
    val first: List[Word] = List(Word("hello", false))
    val second: List[Word] = List(Word("hi", false))
    val output = (Configurations("essay-sample-input-synonymize.docx", List(KeySegment(first), KeySegment(second)))
      , defaultOptional)
    assert(result.successful)
    assertEquals(result.get, output)
  }

  test("multiple keys per keyword") {
    val testString = "source: essay-sample-input-synonymize.docx keywords: - hello, hi"
    val result = DocumentParser(testString)
    // one resulting segment
    val first: List[Word] = List(Word("hello", false), Word("hi", false))
    val output = (Configurations("essay-sample-input-synonymize.docx", List(KeySegment(first)))
      , defaultOptional)
    assert(result.successful)
    assertEquals(result.get, output)
  }

  test("exact keyword") {
    val testString = "source: essay-sample-input-synonymize.docx keywords: - $hello$"
    val result = DocumentParser(testString)
    val exact: List[Word] = List(Word("hello", true))
    val output = (Configurations("essay-sample-input-synonymize.docx", List(KeySegment(exact)))
      , defaultOptional)
    assert(result.successful)
    assertEquals(result.get, output)
  }

  test("complex required syntax") {
    val testString = "source: essay-sample-input-synonymize.docx keywords: - $hello$, hi \n- hola"
    val result = DocumentParser(testString)
    // two resulting segments
    val first: List[Word] = List(Word("hello", true), Word("hi", false))
    val second: List[Word] = List(Word("hola", false))
    val output = (Configurations("essay-sample-input-synonymize.docx", List(KeySegment(first), KeySegment(second)))
      , defaultOptional)
    assert(result.successful)
    assertEquals(result.get, output)
  }


  test("invalid keyword delimiter".fail) {
    val testString = "source: essay-sample-input-synonymize.docx keywords: * $hello$, hi \n* hola"
    val result = DocumentParser(testString)
    assert(result.successful)
  }

  test("valid, but unintended keywords due to spacing") {
    val testString = "source: essay-sample-input-synonymize.docx keywords: - $hello$, hi - hola"
    val result = DocumentParser(testString)
    val first: List[Word] = List(Word("hello", true), Word("hi - hola", false))
    val output = (Configurations("essay-sample-input-synonymize.docx", List(KeySegment(first)))
      , defaultOptional)
    assert(result.successful)
    assertEquals(result.get, output)
  }

  test("optional syntax") {
    val testString = Source.fromFile("../docs/ideal-examples/ideal-fancy.txt").mkString
    val result = DocumentParser(testString)
    val first: List[Word] = List(Word("thesis", false), Word("conclusion", false))
    val second: List[Word] = List(Word("process", true))
    val third: List[Word] = List(Word("neoliberal", false))
    val output = (Configurations("sample-input-synonymize.docx"
        , List(KeySegment(first), KeySegment(second), KeySegment(third)))
      , OptionalSettings(2, "essay_brainstorm_organized.docx"))
    assert(result.successful)
    assertEquals(result.get, output)
  }
}