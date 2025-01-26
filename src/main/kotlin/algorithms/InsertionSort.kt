package algorithms

import datastructures.ASortableList
import datastructures.IList

/**
 * Complexity:
 * n*log(n), since we are using a binary search to find the correct position to enter the item
 */
class InsertionSort<T : Comparable<T>> : ISort<T>() {

    override fun sort(data: ASortableList<T>) {
        this.data = data // this should be set, since exchange method will use the this.data
        for (i in 1 until data.size()) {
            val firstBiggerItem = binarySearchFirstBiggerItem(data, 0, i-1, item = data[i])
            if (firstBiggerItem != i)
            {
                exchange(i, firstBiggerItem)
            }
        }
    }

    /**
     * if @return == data.size() it means the item is bigger than all the items in data
     * @param data should be a sorted array
     * @param begin included
     * @param end included
     */
    private fun binarySearchFirstBiggerItem(data: ASortableList<T>, begin: Int = 0, end: Int = data.size(), item: T): Int {
        if (data.size() == 0) throw NoSuchElementException("provided list is empty.")
        val size = end - begin + 1 // end is also included
        // base case
        if (size == 1) return if (data[begin] > item) begin else begin + 1 // begin == end, end + 1
        // recursion
        val middleIndex = begin + ((size-1) / 2)
        return if (data[middleIndex] > item) binarySearchFirstBiggerItem(data, begin, middleIndex, item)
        else binarySearchFirstBiggerItem(data, middleIndex + 1, end, item)
    }
}