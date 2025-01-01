package com.cyrillrx.sample.core.domain

class Stack<T> {
    private val elements: MutableList<T> = mutableListOf()

    fun isEmpty(): Boolean = elements.isEmpty()

    fun size(): Int = elements.size

    fun push(item: T) {
        elements.add(item)
    }

    fun pop(): T? {
        if (isEmpty()) {
            return null
        }
        return elements.removeAt(elements.size - 1)
    }

    fun peek(): T? {
        return elements.lastOrNull()
    }
}
