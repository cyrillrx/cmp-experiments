package com.cyrillrx.textinterpreter.markdown

import com.cyrillrx.sample.core.domain.MarkdownParser
import com.cyrillrx.sample.core.domain.StyledTextWrapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MarkdownParserTest {
    @Test
    fun testAll() {
        val input = "**bold** *italic* ***boldItalic***"
        val textWrappers = MarkdownParser.parseLine(input)

        assertEquals(expected = 5, actual = textWrappers.count())
        assertTrue(actual = textWrappers[0] is StyledTextWrapper.Bold)
        assertTrue(actual = textWrappers[1] is StyledTextWrapper.Plain)
        assertTrue(actual = textWrappers[2] is StyledTextWrapper.Italic)
        assertTrue(actual = textWrappers[3] is StyledTextWrapper.Plain)
        assertTrue(actual = textWrappers[4] is StyledTextWrapper.BoldAndItalic)
    }

    @Test
    fun testBold() {
        val input = "**bold**"
        val textWrappers = MarkdownParser.parseLine(input)

        assertEquals(expected = 1, actual = textWrappers.count())
        assertTrue(actual = textWrappers.first() is StyledTextWrapper.Bold)
    }

    @Test
    fun testItalic() {
        val input = "*italic*"
        val textWrappers = MarkdownParser.parseLine(input)

        assertEquals(expected = 1, actual = textWrappers.count())
        assertTrue(actual = textWrappers.first() is StyledTextWrapper.Italic)
    }

    @Test
    fun testBoldItalic() {
        val input = "***boldItalic***"
        val textWrappers = MarkdownParser.parseLine(input)

        assertEquals(expected = 1, actual = textWrappers.count())
        assertTrue(actual = textWrappers.first() is StyledTextWrapper.BoldAndItalic)
    }
}
