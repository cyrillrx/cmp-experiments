package com.cyrillrx.sample.core.domain

sealed class Tag(
    open val startPos: Int,
    open val endPos: Int,
    open val text: String,
    open val content: String,
) {
    data class Italic(
        override val startPos: Int,
        override val endPos: Int,
        override val text: String,
        override val content: String,
    ) : Tag(startPos, endPos, text, content)

    data class Bold(
        override val startPos: Int,
        override val endPos: Int,
        override val text: String,
        override val content: String,
    ) : Tag(startPos, endPos, text, content)

    data class BoldAndItalic(
        override val startPos: Int,
        override val endPos: Int,
        override val text: String,
        override val content: String,
    ) : Tag(startPos, endPos, text, content)

    data class Link(
        override val startPos: Int,
        override val endPos: Int,
        override val text: String,
        override val content: String,
        val url: String,
    ) : Tag(startPos, endPos, text, content)
}
