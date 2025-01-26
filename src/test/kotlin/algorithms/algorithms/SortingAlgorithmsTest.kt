package algorithms

import algorithms.algorithms.SelectionSort
import datastructures.DynamicArray
import kotlin.test.Test

class SortingAlgorithmsTest {
    @Test
    fun insertionSort() {
        val da = DynamicArray<Int>()
        for (i in 10 downTo 0) {
            da.pushBack(i)
        }
        da.sort { InsertionSort() }
        TODO("Write rest of the test")
    }

    @Test
    fun selectionSort() {
        val da = DynamicArray<Int>()
        for (i in 10 downTo 0) {
            da.pushBack(i)
        }
        da.sort { SelectionSort() }
        TODO("Write rest of the test")
    }
}