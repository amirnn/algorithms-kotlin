package algorithms

import datastructures.ASortableList

class HeapSort<T: Comparable<T>>: ISort<T>() {

    private data class Node<T> (var data: T, var left: Node<T>? = null, var right: Node<T>? = null)

    override fun sort(data: ASortableList<T>) {
        this.data = data
    }

    fun buildHeap() {

    }
}