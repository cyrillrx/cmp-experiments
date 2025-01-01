package com.cyrillrx.sample.core.domain

class MarkdownParser : TextParser {
    override fun parse(input: String): List<StyledTextWrapper> {
        val textWrappers = mutableListOf<StyledTextWrapper>()
        input.lines().forEach { line ->
            textWrappers.addAll(parseLine(line))
        }
        return textWrappers
    }

    companion object {
        fun parseLine(line: String): List<StyledTextWrapper> {
            if (line.startsWith("###### ")) {
                return listOf(StyledTextWrapper.Header6("${line.substring(7)}\n"))
            } else if (line.startsWith("##### ")) {
                return listOf(StyledTextWrapper.Header5("${line.substring(6)}\n"))
            } else if (line.startsWith("#### ")) {
                return listOf(StyledTextWrapper.Header4("${line.substring(5)}\n"))
            } else if (line.startsWith("### ")) {
                return listOf(StyledTextWrapper.Header3("${line.substring(4)}\n"))
            } else if (line.startsWith("## ")) {
                return listOf(StyledTextWrapper.Header2("${line.substring(3)}\n"))
            } else if (line.startsWith("# ")) {
                return listOf(StyledTextWrapper.Header1("${line.substring(2)}\n"))
            }

            val tags = mutableListOf<Tag>()
            val stack = Stack<EnclosingTag>()

            var cursorPosition = 0
            while (cursorPosition < line.length) {
                val lastTag = stack.peek()
                if (isClosingItalic(line, cursorPosition, lastTag)) {
                    val startingItalic = stack.pop() as OpenTag.Italic
                    tags.add(
                        Tag.Italic(
                            startPos = startingItalic.start,
                            endPos = cursorPosition,
                            text = line.substring(startingItalic.start, cursorPosition),
                            content = line.substring(startingItalic.end + 1, cursorPosition)
                        )
                    )
                    cursorPosition += 1

                } else if (isClosingBold(line, cursorPosition, lastTag)) {
                    val startingBold = stack.pop() as OpenTag.Bold
                    tags.add(
                        Tag.Bold(
                            startPos = startingBold.start,
                            endPos = cursorPosition + 1,
                            text = line.substring(startingBold.start, cursorPosition + 1),
                            content = line.substring(startingBold.end + 1, cursorPosition)
                        )
                    )
                    cursorPosition += 2

                } else if (isClosingBoldAndItalic(line, cursorPosition, lastTag)) {
                    val startingBoldAndItalic = stack.pop() as OpenTag.BoldAndItalic
                    tags.add(
                        Tag.BoldAndItalic(
                            startPos = startingBoldAndItalic.start,
                            endPos = cursorPosition + 2,
                            text = line.substring(startingBoldAndItalic.start, cursorPosition + 2),
                            content = line.substring(startingBoldAndItalic.end + 1, cursorPosition)
                        )
                    )
                    cursorPosition += 3

                } else if (isOpeningBoldAndItalic(line, cursorPosition, lastTag)) {
                    stack.push(OpenTag.BoldAndItalic(cursorPosition, cursorPosition + 2))
                    cursorPosition += 3

                } else if (isOpeningBold(line, cursorPosition, lastTag)) {
                    stack.push(OpenTag.Bold(cursorPosition, cursorPosition + 1))
                    cursorPosition += 2

                } else if (isOpeningItalic(line, cursorPosition, lastTag)) {
                    stack.push(OpenTag.Italic(cursorPosition, cursorPosition))
                    cursorPosition += 1
                } else {
                    cursorPosition += 1
                }
            }

            if (tags.isEmpty()) {
                return listOf(StyledTextWrapper.Plain("$line\n"))
            }

            tags.sortBy { it.startPos }

            val textWrappers = mutableListOf<StyledTextWrapper>()
            cursorPosition = 0
            tags.forEach { tag ->
                if (tag.startPos > cursorPosition) {
                    textWrappers.add(StyledTextWrapper.Plain(line.substring(cursorPosition, tag.startPos)))
                } else if (tag.startPos < cursorPosition) {
                    println("Error: Tag starts before cursor position")
                }

                when (tag) {
                    is Tag.Bold -> textWrappers.add(StyledTextWrapper.Bold(tag.content))
                    is Tag.Italic -> textWrappers.add(StyledTextWrapper.Italic(tag.content))
                    is Tag.BoldAndItalic -> textWrappers.add(StyledTextWrapper.BoldAndItalic(tag.content))
                    is Tag.Link -> textWrappers.add(StyledTextWrapper.Link(tag.content, tag.url))
                }
                cursorPosition = tag.endPos + 1
            }

            textWrappers.add(StyledTextWrapper.Plain("${line.substring(cursorPosition)}\n"))

            return textWrappers
        }

        private fun isClosingItalic(line: String, currentPosition: Int, lastTag: EnclosingTag?): Boolean {
            return lastTag is OpenTag.Italic &&
                    lastTag.end < currentPosition - 1 &&
                    line.getOrNull(currentPosition) == '*'
        }

        private fun isClosingBold(line: String, currentPosition: Int, lastTag: EnclosingTag?): Boolean {
            return lastTag is OpenTag.Bold &&
                    lastTag.end < currentPosition - 1 &&
                    line.getOrNull(currentPosition) == '*' &&
                    line.getOrNull(currentPosition + 1) == '*'
        }

        private fun isClosingBoldAndItalic(line: String, currentPosition: Int, lastTag: EnclosingTag?): Boolean {
            return lastTag is OpenTag.BoldAndItalic &&
                    lastTag.end < currentPosition - 1 &&
                    line.getOrNull(currentPosition) == '*' &&
                    line.getOrNull(currentPosition + 1) == '*' &&
                    line.getOrNull(currentPosition + 2) == '*'
        }

        private fun isOpeningItalic(line: String, currentPosition: Int, lastTag: EnclosingTag?): Boolean {
            if (lastTag is OpenTag) return false

            val currentChar = line.getOrNull(currentPosition)
            val nextChar = line.getOrNull(currentPosition + 1)
            return currentChar == '*' && nextChar != '*' && nextChar != ' '
        }

        private fun isOpeningBold(line: String, currentPosition: Int, lastTag: EnclosingTag?): Boolean {
            if (lastTag is OpenTag) return false

            return line.getOrNull(currentPosition) == '*' &&
                    line.getOrNull(currentPosition + 1) == '*' &&
                    line.getOrNull(currentPosition + 2) != '*' &&
                    line.getOrNull(currentPosition + 2) != ' '
        }

        private fun isOpeningBoldAndItalic(line: String, currentPosition: Int, lastTag: EnclosingTag?): Boolean {
            if (lastTag is OpenTag) return false

            return line.getOrNull(currentPosition) == '*' &&
                    line.getOrNull(currentPosition + 1) == '*' &&
                    line.getOrNull(currentPosition + 2) == '*' &&
                    line.getOrNull(currentPosition + 3) != '*' &&
                    line.getOrNull(currentPosition + 3) != ' '
        }
    }

    sealed class EnclosingTag(val start: Int, val end: Int)
    sealed class OpenTag(start: Int, end: Int) : EnclosingTag(start, end) {
        class Bold(start: Int, end: Int) : OpenTag(start, end)
        class Italic(start: Int, end: Int) : OpenTag(start, end)
        class BoldAndItalic(start: Int, end: Int) : OpenTag(start, end)
    }

    sealed class CloseTag(start: Int, end: Int) : EnclosingTag(start, end) {
        class Bold(start: Int, end: Int) : CloseTag(start, end)
        class Italic(start: Int, end: Int) : CloseTag(start, end)
        class BoldAndItalic(start: Int, end: Int) : CloseTag(start, end)
    }
}
