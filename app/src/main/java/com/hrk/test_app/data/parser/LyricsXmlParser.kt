package com.hrk.test_app.data.parser

import com.hrk.test_app.data.model.LyricLine
import com.hrk.test_app.data.model.LyricSong
import com.hrk.test_app.data.model.LyricWord
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.InputStream
import java.io.StringReader
import javax.inject.Inject
import javax.inject.Singleton
import javax.xml.parsers.DocumentBuilderFactory

@Singleton
class LyricsXmlParser @Inject constructor() {

    /**
     * Parses the lyrics XML string into a structured [LyricSong].
     */
    fun parse(xmlString: String, title: String = "Về đâu mái tóc người thương", artist: String = "Quang Lê"): LyricSong {
        val inputSource = InputSource(StringReader(xmlString))
        return parseInternal(inputSource, title, artist)
    }

    /**
     * Parses the lyrics XML from an [InputStream].
     */
    fun parse(inputStream: InputStream, title: String = "Về đâu mái tóc người thương", artist: String = "Quang Lê"): LyricSong {
        val inputSource = InputSource(inputStream).apply {
            encoding = "UTF-8"
        }
        return parseInternal(inputSource, title, artist)
    }

    private fun parseInternal(inputSource: InputSource, title: String, artist: String): LyricSong {
        val factory = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = false
        }
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(inputSource)
        document.documentElement.normalize()

        val rawLines = mutableListOf<List<RawWord>>()
        val paramNodes = document.getElementsByTagName("param")

        for (i in 0 until paramNodes.length) {
            val paramElement = paramNodes.item(i) as? Element ?: continue
            val iNodes = paramElement.getElementsByTagName("i")
            val currentLineWords = mutableListOf<RawWord>()

            for (j in 0 until iNodes.length) {
                val iElement = iNodes.item(j) as? Element ?: continue
                val vaAttr = iElement.getAttribute("va")
                val startTimeSeconds = vaAttr.toDoubleOrNull() ?: 0.0
                val startTimeMs = (startTimeSeconds * 1000).toLong()
                val text = iElement.textContent ?: ""
                currentLineWords.add(RawWord(text = text, startTimeMs = startTimeMs))
            }

            if (currentLineWords.isNotEmpty()) {
                rawLines.add(currentLineWords)
            }
        }

        val lines = buildLines(rawLines)
        return LyricSong(
            title = title,
            artist = artist,
            lines = lines
        )
    }

    private fun buildLines(rawLines: List<List<RawWord>>): List<LyricLine> {
        val result = mutableListOf<LyricLine>()

        for (lineIdx in rawLines.indices) {
            val rawWords = rawLines[lineIdx]
            if (rawWords.isEmpty()) continue

            val words = mutableListOf<LyricWord>()
            for (wordIdx in rawWords.indices) {
                val currentRaw = rawWords[wordIdx]
                val startTime = currentRaw.startTimeMs

                val endTime = if (wordIdx < rawWords.size - 1) {
                    val nextStart = rawWords[wordIdx + 1].startTimeMs
                    if (nextStart > startTime) nextStart else startTime + DEFAULT_WORD_DURATION_MS
                } else {
                    // Last word of this line
                    val nextLineStart = if (lineIdx < rawLines.size - 1) {
                        rawLines[lineIdx + 1].firstOrNull()?.startTimeMs
                    } else null

                    if (nextLineStart != null && nextLineStart > startTime) {
                        val gap = nextLineStart - startTime
                        // Give a natural sustain to the last word (up to 1500ms or until gap - 200ms)
                        (startTime + minOf(DEFAULT_LAST_WORD_DURATION_MS, (gap - 200L).coerceAtLeast(DEFAULT_WORD_DURATION_MS)))
                    } else {
                        startTime + DEFAULT_LAST_WORD_DURATION_MS
                    }
                }

                words.add(
                    LyricWord(
                        text = currentRaw.text,
                        startTimeMs = startTime,
                        endTimeMs = endTime
                    )
                )
            }

            val lineStartTime = words.first().startTimeMs
            val lineEndTime = words.last().endTimeMs
            val fullText = words.joinToString(separator = "") { it.text }

            result.add(
                LyricLine(
                    lineIndex = lineIdx,
                    words = words,
                    startTimeMs = lineStartTime,
                    endTimeMs = lineEndTime,
                    fullText = fullText
                )
            )
        }

        return result
    }

    private data class RawWord(
        val text: String,
        val startTimeMs: Long
    )

    companion object {
        private const val DEFAULT_WORD_DURATION_MS = 350L
        private const val DEFAULT_LAST_WORD_DURATION_MS = 1200L
    }
}
