package algorithms

import datastructures.ASortableList

class MergeSort<T : Comparable<T>> : ISort<T>() {
    override fun sort(data: ASortableList<T>) {
        this.data = data
    }

    /**
     * merge two sub-arrays
     * the left sub-array and the right sub-array are adjacent to each other
     */
    fun merge(data: ASortableList<T>, leftBegin: Int, rightBegin: Int, rightEnd: Int) {
        val lend = rightBegin - 1
        var leftIndex = 0 // left index
        var rightIndex = 0 // right index
        var k = 0
        for (k in leftBegin until rightEnd + 1) {
            if (leftIndex == lend) {
                break
            }
            if (rightIndex == rightEnd) {
                exchange(leftIndex++, k)
                continue
            }
            data[k] = if (data[leftIndex] < data[rightIndex]) data[leftIndex++] else data[rightIndex++]
        }
    }
}