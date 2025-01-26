package datastructures

import algorithms.ISort

abstract class ASortableList<T: Comparable<T>>: IList<T> {
    fun sort(using: ()->ISort<T>) {
        val sortingAlgorithm = using()
        sortingAlgorithm.sort(this)
    }
}