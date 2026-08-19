package com.hrk.test_app.data.parser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LyricsXmlParserTest {

    private lateinit var parser: LyricsXmlParser

    @Before
    fun setup() {
        parser = LyricsXmlParser()
    }

    @Test
    fun testParseXmlLyrics_Success() {
        val xmlSample = """
            <data>
               <param s="b">
                  <i va="35.144">Hồn </i>
                  <i va="35.587997">lỡ </i>
                  <i va="36.006">sa </i>
                  <i va="36.475998">vào </i>
                  <i va="36.972">đôi </i>
                  <i va="37.495">mắt </i>
                  <i va="37.939">em</i>
               </param>
               <param s="b">
                  <i va="42.641">Chiều </i>
                  <i va="43.085">nao </i>
                  <i va="45.485">xõa </i>
                  <i va="45.486">tóc </i>
                  <i va="45.487">ngồi </i>
                  <i va="45.488">bên </i>
                  <i va="45.488">rèm</i>
               </param>
            </data>
        """.trimIndent()

        val song = parser.parse(xmlSample)

        assertNotNull(song)
        assertEquals(2, song.lines.size)

        // Line 1 verification
        val line1 = song.lines[0]
        assertEquals(0, line1.lineIndex)
        assertEquals(7, line1.words.size)
        assertEquals("Hồn lỡ sa vào đôi mắt em", line1.fullText.trim())
        assertEquals(35144L, line1.startTimeMs)
        assertEquals(35144L, line1.words[0].startTimeMs)
        assertEquals("Hồn ", line1.words[0].text)
        assertEquals("em", line1.words[6].text)

        // Test smooth progress calculation on word
        val wordHon = line1.words[0] // start: 35144, end: 35587
        assertEquals(0.0f, wordHon.getProgress(35000L), 0.001f)
        assertEquals(1.0f, wordHon.getProgress(36000L), 0.001f)
        val midTime = (wordHon.startTimeMs + wordHon.endTimeMs) / 2
        assertTrue(wordHon.getProgress(midTime) in 0.45f..0.55f)

        // Line 2 verification
        val line2 = song.lines[1]
        assertEquals(1, line2.lineIndex)
        assertEquals(7, line2.words.size)
        assertEquals("Chiều nao xõa tóc ngồi bên rèm", line2.fullText.trim())
        assertEquals(42641L, line2.startTimeMs)
    }
}
