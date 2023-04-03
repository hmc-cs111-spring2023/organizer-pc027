import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.XWPFTestDataSamples

@main def hello: Unit =
  println("Hello, New York!")
  println(msg)
  parseSimpleDocument

def msg = "I was compiled by Scala 3. :)"

def parseSimpleDocument: Unit = {
  // todo: open a word document and extract words
  val doc: XWPFDocument = XWPFTestDataSamples.openSampleDocument("sample.docx")
  // val docExtractor: XWPFWordExtractor = new XWPFWordExtractor(doc) {
  //   String text = extractor.getText();
  //   println(text)
  // }
}