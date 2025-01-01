package com.cyrillrx.sample.core.domain

interface TextParser {
    fun parse(input: String): List<StyledTextWrapper>
}
