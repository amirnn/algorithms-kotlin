package datastructures.tests

import datastructures.DynamicArray
import datastructures.DoublyLinkedList
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class DatastructuresTests {
    @Test
    fun dynamicArrayPushBackTest(){
        val da = DynamicArray<Int>()
        val itemCount = 128
        for (i in 0..<itemCount) {
            da.pushBack(i)
        }
        assertEquals(da.size(), itemCount)
    }

    @Test
    fun dynamicArrayPushFrontTest(){
        val da = DynamicArray<Int>()
        val itemCount = 128
        for (i in 0..<itemCount) {
            da.pushFront(i)
        }
        assertEquals(da.size(), itemCount)
    }
}