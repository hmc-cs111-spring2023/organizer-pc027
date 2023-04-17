package docorg.semantics

import org.apache.poi.xwpf.usermodel._
import java.io.InputStream
import scala.collection.JavaConverters._
import scala.collection.mutable.{Map, ArrayBuffer}

import docorg.ir._

def parseDocument(
  stream: InputStream,
  keywords: List[String]): XWPFDocument =
    val doc: XWPFDocument = new XWPFDocument(stream)

    // Paragraphs are sections of information delimited by new lines
    val paragraphs: List[XWPFParagraph] = doc.getParagraphs().asScala.toList
    // println(paragraphs.apply(2).getText())

    // TODO: validations - check that paragraphs.length != 0

    val docAsString: String = paragraphs.foldLeft[String]("")(
        (a, b) => {
        val text = b.getText()
        if text.length == 0 then a + "\n\n"
        else a + "\n" + b.getText()
        })

    // Create dictionary for output text sections
    // https://stackoverflow.com/questions/51957242/how-to-initialize-empty-map-that-has-been-type-aliased
    // type SectionType = Map[String, ArrayBuffer[String]]
    var sectionMap: Map[String, ArrayBuffer[String]] = Map()
    sortParagraphsIntoSections(keywords, sectionMap, paragraphs)
    
    // Close streams for original document
    doc.close()

    formatOutputWordDoc(keywords :+ "uncategorized", sectionMap)

// IN-PROGRESS: sorting w/ default-config.md 
// def convertParagraphIntoSegments(keyword: String, paragraph: String): Unit =
//     println(paragraph.split('.'))
    



// Intermediate solution: only lines containing the keyword goes into the section
def sortParagraphsIntoSections(
  keywords: List[String],
  sectionMap: Map[String, ArrayBuffer[String]], 
  paragraphs: List[XWPFParagraph]): Unit =
    // hardcoding line limit for testing for now
    val lineLimit = 7

    // edit to count lines picked per segment
    var linesLeft = 7

    for p <- paragraphs
    do 
      val text: String = p.getText()
      // TODO: figure out how to detect bullet point style
      val found = keywords.filter(word => text contains word)

      if found.length == 0 then
        sectionMap.get("uncategorized") match {
          case Some(curr) => 
            sectionMap.put("uncategorized", curr += text)
          case None => sectionMap.put("uncategorized", ArrayBuffer(text))
        }
      else for w <- found 
      do 
        // for each keyword found, do additional stuff to split into segments
        // so that sectionMap.put is not just text
        // convertParagraphIntoSegments(w, text)

        // split the line by found word
        val chunks = text.split(w)
        // println(chunks)
        val linesPerChunk = chunks.foldLeft[ArrayBuffer[Array[String]]]
          (ArrayBuffer.empty)(
            (lines, c) => {
                // println(c)
                lines += c.split('.')
            })

        // TODO: Pairwise take lines
        // println(linesPerChunk.size)
        // linesPerChunk.foldLeft[ArrayBuffer[String]](ArrayBuffer.empty)(
        //     (before, after) => {
        //         var front = ArrayBuffer.empty
        //         var back = ArrayBuffer.empty
        //         while before.size != 0 && linesLeft != 0 do
        //             front += before.apply(-1) + "."
        //             linesLeft -= 1
        //             if after.size != 0 && linesLeft != 0 then
        //                 back += after.apply(0) + "."
        //                 linesLeft -= 1
                
        //         // construct segment within configuration limits for word
        //         val segment = front.foldLeft[String]("")(_ + _) 
        //             + w + back.foldLeft[String]("")(_ + _) 

        //         // TODO: consider the rest of the lines as uncategorized
        //     }
        // )

        // linesPerChunk.toList

        sectionMap.get(w) match {
          case Some(curr) => 
            sectionMap.put(w, curr += text)
          case None => sectionMap.put(w, ArrayBuffer(text))
        }

// Creating file output
def formatOutputWordDoc(
  keywords: List[String],
  sectionMap: Map[String, ArrayBuffer[String]]): XWPFDocument =
    // Use dictionary to create a word document
    var output = new XWPFDocument()

    // Print keyword sections to document in the order provided by end-users
    for key <- keywords
    do {
      var sectionTitle = output.createParagraph
      sectionTitle.setAlignment(ParagraphAlignment.CENTER)
      var titleData = sectionTitle.createRun
      titleData.setText(key)
      titleData.setBold(true)
      titleData.setFontSize(24)


      val content = sectionMap.get(key) // ArrayBuffer[String] or None
      var sectionContent = output.createParagraph
      var contentData = sectionContent.createRun
      contentData.setFontSize(12)

      content match {
        case Some(curr) => {
          // print("found content")
          for seg <- curr
          do 
            contentData.setText(seg)
            contentData.addBreak
        }
        case None => {
          // print("cannot find content for this")
          contentData.setText("[No information found]")
          contentData.setItalic(true)
        }
      }
    }
    
    output