package com.cyrillrx.sample.core.domain

sealed class StyledTextWrapper(open val text: String) {
    data class Plain(override val text: String) : StyledTextWrapper(text)
    data class Header1(override val text: String) : StyledTextWrapper(text)
    data class Header2(override val text: String) : StyledTextWrapper(text)
    data class Header3(override val text: String) : StyledTextWrapper(text)
    data class Header4(override val text: String) : StyledTextWrapper(text)
    data class Header5(override val text: String) : StyledTextWrapper(text)
    data class Header6(override val text: String) : StyledTextWrapper(text)
    data class Italic(override val text: String) : StyledTextWrapper(text)
    data class Bold(override val text: String) : StyledTextWrapper(text)
    data class BoldAndItalic(override val text: String) : StyledTextWrapper(text)
    data class Link(override val text: String, val url: String) : StyledTextWrapper(text)
}
