package com.cyrillrx.sample.core.presentation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

private const val ANNOTATION_TAG_URL = "url"

@Composable
fun AnnotatedText(
    modifier: Modifier = Modifier,
    annotatedString: AnnotatedString,
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
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
    onUriClick: ((String) -> Unit)? = null,
) {
    val uriHandler = LocalUriHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val urls = remember(layoutResult, annotatedString) {
        annotatedString.getStringAnnotations(ANNOTATION_TAG_URL, 0, annotatedString.lastIndex)
    }

    val clickable = urls.isNotEmpty()

    Text(
        modifier = modifier.then(
            if (clickable) Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { pos ->
                            layoutResult.value?.let { layoutResult ->
                                val position = layoutResult.getOffsetForPosition(pos)
                                annotatedString
                                    .getStringAnnotations(position, position)
                                    .firstOrNull()
                                    ?.let { sa ->
                                        if (sa.tag == ANNOTATION_TAG_URL) {
                                            val url = sa.item
                                            onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                                        }
                                    }
                            }
                        },
                    )
                }
                .semantics {
                    if (urls.size == 1) {
                        role = Role.Button
                        onClick("Link (${annotatedString.substring(urls[0].start, urls[0].end)}") {
                            val url = urls[0].item
                            onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                            true
                        }
                    } else {
                        customActions = urls.map {
                            CustomAccessibilityAction("Link (${annotatedString.substring(it.start, it.end)})") {
                                val url = it.item
                                onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                                true
                            }
                        }
                    }
                } else Modifier,
        ),
        text = annotatedString,
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
        onTextLayout = {
            layoutResult.value = it
            onTextLayout?.invoke(it)
        },
        style = style,
    )
}
