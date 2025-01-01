package com.cyrillrx.sample.core.presentation

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.cyrillrx.sample.core.domain.MarkdownParser
import com.cyrillrx.sample.core.domain.StyledTextWrapper

private const val ANNOTATION_TAG_URL = "url"

/**
 * Simple Text composable to show the text with html styling from a String.
 * Supported are:
 *
 * <b>Bold</b>
 * <i>Italic</i>
 * <u>Underlined</u>
 * <strike>Strikethrough</strike>
 * <a href="https://google.de">Link</a>
 */
@Composable
fun MarkdownText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: ((TextLayoutResult) -> Unit)? = {},
    style: TextStyle = LocalTextStyle.current,
    onUriClick: ((String) -> Unit)? = null,
) {
    AnnotatedText(
        modifier = modifier,
        annotatedString = text.toAnnotatedString(),
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = style,
        onUriClick = onUriClick,
    )
}

@Composable
private fun String.toAnnotatedString(): AnnotatedString = MarkdownParser().parse(this).toAnnotatedString()

@Composable
private fun List<StyledTextWrapper>.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        forEach { styledText ->

            when (styledText) {
                is StyledTextWrapper.Plain -> withStyle(SpanStyle(fontSize = 16.sp)) { append(styledText.text) }
                is StyledTextWrapper.Header1 -> withStyle(SpanStyle(fontSize = 32.sp)) { append(styledText.text) }
                is StyledTextWrapper.Header2 -> withStyle(SpanStyle(fontSize = 24.sp)) { append(styledText.text) }
                is StyledTextWrapper.Header3 -> withStyle(SpanStyle(fontSize = 22.sp)) { append(styledText.text) }
                is StyledTextWrapper.Header4 -> withStyle(SpanStyle(fontSize = 20.sp)) { append(styledText.text) }
                is StyledTextWrapper.Header5 -> withStyle(SpanStyle(fontSize = 18.sp)) { append(styledText.text) }
                is StyledTextWrapper.Header6 -> withStyle(SpanStyle(fontSize = 16.sp)) { append(styledText.text) }

                is StyledTextWrapper.Italic -> withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(styledText.text)
                }

                is StyledTextWrapper.Bold -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(styledText.text)
                }

                is StyledTextWrapper.BoldAndItalic -> withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                ) { append(styledText.text) }

                is StyledTextWrapper.Link -> withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(styledText.text)
                    addStringAnnotation(ANNOTATION_TAG_URL, styledText.url, length - styledText.text.length, length)
                }
            }
        }
    }
}

fun StyledTextWrapper.toSpanStyle(): SpanStyle {
    return when (this) {
        is StyledTextWrapper.Plain -> SpanStyle(fontSize = 16.sp)
        is StyledTextWrapper.Header1 -> SpanStyle(fontSize = 32.sp)
        is StyledTextWrapper.Header2 -> SpanStyle(fontSize = 24.sp)
        is StyledTextWrapper.Header3 -> SpanStyle(fontSize = 22.sp)
        is StyledTextWrapper.Header4 -> SpanStyle(fontSize = 20.sp)
        is StyledTextWrapper.Header5 -> SpanStyle(fontSize = 18.sp)
        is StyledTextWrapper.Header6 -> SpanStyle(fontSize = 16.sp)
        is StyledTextWrapper.Bold -> SpanStyle(fontWeight = FontWeight.Bold)
        is StyledTextWrapper.Italic -> SpanStyle(fontStyle = FontStyle.Italic)
        is StyledTextWrapper.BoldAndItalic -> SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
        is StyledTextWrapper.Link -> SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
    }
}
