package docorg.semantics

import org.apache.poi.xwpf.usermodel._
import java.io.InputStream
import scala.collection.JavaConverters._
import scala.collection.mutable.{Map, ArrayBuffer, ListBuffer}

import docorg.ir._

def parseDocument(
  stream: InputStream,
  keywords: List[KeySegment],
  segLength: Int): XWPFDocument =
    // Paragraphs are sections of information delimited by new lines
    val doc: XWPFDocument = new XWPFDocument(stream)
    val paragraphs: List[XWPFParagraph] = doc.getParagraphs().asScala.toList

    // Create dictionary for output text sections
    // https://stackoverflow.com/questions/51957242/how-to-initialize-empty-map-that-has-been-type-aliased
    var sectionMap: Map[KeySegment, ArrayBuffer[String]] = Map()

    sortParagraphsIntoSections(keywords, sectionMap, paragraphs)
    
    // Close streams for original document
    doc.close()

    formatOutputWordDoc(keywords, sectionMap)

// Intermediate solution: only lines containing the keyword goes into the section
def sortParagraphsIntoSections(
  keywords: List[KeySegment],
  sectionMap: Map[KeySegment, ArrayBuffer[String]], 
  paragraphs: List[XWPFParagraph]): Unit =
    // hardcoding line limit for testing for now
    val lineLimit = 7

    // edit to count lines picked per segment
    var linesLeft = 7

    for p <- paragraphs
    do 
      val text: String = p.getText()
      // TODO: figure out how to detect bullet point style

      val found = keywords.filter(seg => {
        // find at least 1 word in synonyms list in the text
        val findAnyWord = seg.words.filter(word => {
            if word.isExact then 
              text.split(' ') contains word.str
            else text contains word.str
          })
        findAnyWord.length > 0
      })


      // Sort text found into corresponding segment
      if found.length == 0 then
        // uncategorizable text do not require additional splitting
        val uncategorized = KeySegment(List(Word("uncategorized")))
        sectionMap.get(uncategorized) match {
          case Some(curr) => 
            sectionMap.put(uncategorized, curr += text)
          case None => sectionMap.put(uncategorized, ArrayBuffer(text))
        }
      else for seg <- found 
      do 
        // for each keyword found, do additional stuff to split into segments
        // so that sectionMap.put is not just text
        // convertParagraphIntoSegments(w, text)

        sectionMap.get(seg) match {
          case Some(curr) => 
            sectionMap.put(seg, curr += text)
          case None => sectionMap.put(seg, ArrayBuffer(text))
        }


////////////////////////////////////////////////////////
/////////////// Create .docx file output ///////////////
////////////////////////////////////////////////////////
// Creating file output
def formatOutputWordDoc(
  keywords: List[KeySegment],
  sectionMap: Map[KeySegment, ArrayBuffer[String]]): XWPFDocument =
    // Use dictionary to create a word document
    var output = new XWPFDocument()

    // Print keyword sections to document in the order provided by end-users
    for seg <- keywords :+ KeySegment(List(Word("uncategorized")))
    do {
      var sectionTitle = output.createParagraph
      sectionTitle.setAlignment(ParagraphAlignment.CENTER)
      var titleData = sectionTitle.createRun
      titleData.setText(seg.toString)
      titleData.setBold(true)
      titleData.setFontSize(24)


      val content = sectionMap.get(seg) // ArrayBuffer[String] or None
      var sectionContent = output.createParagraph
      var contentData = sectionContent.createRun
      contentData.setFontSize(12)

      content match {
        case Some(curr) => {
          // print("found content")
          for segText <- curr
          do 
            contentData.setText(segText)
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

////////////////////////////////////////////////////////
///////////////// debug helper methods /////////////////
////////////////////////////////////////////////////////
def wordDocParagraphsAsString(paragraphs: List[XWPFParagraph]): String = 
  paragraphs.foldLeft[String]("")(
    (a, b) => {
    val text = b.getText()
    if text.length == 0 then a + "\n\n"
    else a + "\n" + b.getText()
    })


// IN-PROGRESS: more sorting attempts, but ran out of time.
////////////////////////////////////////////////////////
// processing paragraphs to maximum user-specified length ///
////////////////////////////////////////////////////////
// def preprocessParagraphs(paragraphs: List[XWPFParagraph], segLength: Int = 7): List[XWPFParagraph] = 
//   var preprocessedList = ListBuffer[XWPFParagraph]().empty
//   for p <- paragraphs
//   do 
//     val text: List[String] = p.getText().split('.').toList
//     println(p.getStyle)
//     if text.length > segLength then 
//       val newLength = text.length - segLength

//       preprocessedList.append(new XWPFParagraph(text.takeLeft(newLength).mkString(".")))
//       preprocessedList.append(new XWPFParagraph(text.takeRight(segLength).mkString(".")))
//     else 
//       preprocessedList.append(p)
//   return preprocessedList.toList

// IN-PROGRESS: sorting w/ default-config.md 
// def convertParagraphIntoSegments(keyword: Word, paragraph: String): Unit =
//   // split the line by found word
//   val chunks = paragraph.split(keyword.str)

  // if chunks.length != 0 then
  //   val linesPerChunk = chunks.foldLeft[ArrayBuffer[Array[String]]]
  //     (ArrayBuffer.empty)(
  //       (lines, c) => {
  //           lines += c.split('.')
  //       })

  //   // TODO: Pairwise take lines
  //   println(linesPerChunk.size)
  //   linesPerChunk.foldLeft[ArrayBuffer[String]](ArrayBuffer.empty)(
  //       (before, after) => {
  //           var front = ArrayBuffer.empty
  //           var back = ArrayBuffer.empty
  //           while before.size != 0 && linesLeft != 0 do
  //               front += before.apply(-1) + "."
  //               linesLeft -= 1
  //               if after.size != 0 && linesLeft != 0 then
  //                   back += after.apply(0) + "."
  //                   linesLeft -= 1
            
  //           // construct segment within configuration limits for word
  //           val segment = front.foldLeft[String]("")(_ + _) 
  //               + w + back.foldLeft[String]("")(_ + _) 

  //           // TODO: consider the rest of the lines as uncategorized
  //       }
  //   )
    // linesPerChunk.toList



////////////////////////////////////////////////////////
// OLD: reate final list of keywords for search and sort ///
////////////////////////////////////////////////////////
// def appendKeywordsWithPrefixSuffix(
//   keywords: List[KeySegment],
//   synonyms: List[String]): List[KeySegment] =
//     var newKeywords = ListBuffer[KeySegment]().empty
//     for seg <- keywords
//     do
//       val words: List[Word] = seg.words
//       // val wordsToProcess: List[Word] = words.filter(w => !w.isExact)
//       val newWords = ListBuffer[Word]().empty
      
//       for w <- words
//       do
//         if w.isExact then newWords.append(w)
//         // create new words to search for with prefix and suffix
//         else newWords.appendAll(makeWordList(w, synonyms))
      
//       // newKeywords.append(KeySegment(allWordVariations))
//       newKeywords.append(KeySegment(newWords.toList))


//       val exactWords: List[Word] = words.filter(w => w.isExact)

//     newKeywords.toList

// def makeWordList(word: Word, synonyms: List[String]): List[Word] =
//   var newWords = ListBuffer[Word](word)
//   for fix <- synonyms 
//   do
//     val corrected = fix.trim
//     if corrected.head == '-' then newWords.append(Word(word.str + corrected.tail)) // suffix case
//     else newWords.append(Word(corrected.dropRight(1) + word.str)) // prefix case
//   newWords.toList
