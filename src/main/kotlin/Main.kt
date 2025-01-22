package algorithms

import datastructures.DoublyLinkedList
import datastructures.DynamicArray

fun main() {
    val dlist = DoublyLinkedList<Int>()
    dlist.pushFront(5)
    assert(dlist.getSize() == 1)
    dlist.pushBack(2)
    assert(dlist.getSize() == 2)
    dlist.pushFront(3)

    // Test dynamic list
    for (i in dlist.getSize() downTo 1) {
        println(dlist.popFront()!!.value)
    }

    // Test dynamic array
    // test push back and pop back
    val da = DynamicArray<Int>()
    for (i in 0..<128) {
        da.pushBack(i)
    }
    println("Size of dynamic array ${da.size()} and its buffer size is ${da.bufferSize()}")
    for (i in 0..<da.size())
    {
        println("item ${i} is: ${da[i]}")
    }

    for (i in da.size() downTo 1) {
        val item = da.popBack()
        println("item ${i} is: ${item} and buffer size is ${da.bufferSize()} and element count is ${da.size()}")
    }
    println("Is da empty? ${da.isEmpty()}")

    // test push front and pop front
    println("Pushing to front and expanding array")
    for (i in 0..<128) {
        da.pushFront(i)
    }
    println("Size of dynamic array ${da.size()} and its buffer size is ${da.bufferSize()}")
    for (i in 0..<da.size())
    {
        println("item ${i} is: ${da[i]}")
    }
    for (i in da.size() downTo 1) {
        val item = da.popFront()
        println("item ${i} is: ${item} and buffer size is ${da.bufferSize()} and element count is ${da.size()}")
    }
    println("Is da empty? ${da.isEmpty()}")

}