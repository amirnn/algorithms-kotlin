package algorithms

import datastructures.ASortableList

abstract class ISort<T: Comparable<T>> {

    protected lateinit var data: ASortableList<T>

    fun isSorted(): Boolean {
        for (i in 0 until data.size() - 1) {
            if (data[i] > data[i+1]) {
                return false
            }
        }
        return true
    }

    fun exchange(i: Int, j: Int) {
        val temp = data[i]
        data[i] = data[j]
        data[j] = temp
    }

    /**
     * the children will need to only implement this method
     */
    abstract fun sort(data: ASortableList<T>)
}