# Sample Markdown Text Application

This project is a sample application demonstrating the use of Jetpack Compose for rendering markdown and HTML text with various styles and annotations.
The application includes custom composables for displaying annotated text, markdown text and HTML text.

## Features

- Display text with HTML-like styling (bold, italic, underline, strikethrough, links)
- Handle user interactions with links
- Customizable text styles and attributes

## Project Structure

- `AnnotatedText.kt`: Contains the `AnnotatedText` composable for displaying annotated text with clickable links.
- `MarkdownText.kt`: Contains the `MarkdownText` composable for rendering markdown text using the `AnnotatedText` composable.

## Code Overview

### `AnnotatedText`

A composable function that displays text with annotations and handles user interactions with links.

### `MarkdownText`

A composable function that parses a markdown string and displays it using the `AnnotatedText` composable.
