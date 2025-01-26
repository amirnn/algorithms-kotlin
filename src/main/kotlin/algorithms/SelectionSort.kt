package algorithms.algorithms

import algorithms.ISort
import datastructures.ASortableList

/**
 * Complexity O(n^2)
 */
class SelectionSort<T: Comparable<T>>: ISort<T>() {
    override fun sort(data: ASortableList<T>) {
        this.data = data
        var min = data[0]
        for (p in 0 until data.size()) {
            for (i in p until data.size()) {
                if (data[i] < min) {
                    min = data[i]
                }
            }
            data[p] = min
        }
    }
}