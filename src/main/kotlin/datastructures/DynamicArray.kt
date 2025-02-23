// Copyright Amir Nourinia 2025

package datastructures

import java.util.*

/**
 * Dynamic Array that is implemented using a circular buffer.
 * Complexity:
 * da[random] = theta(1)
 * da.pushFront() = theta(1), asymptotic
 * da.pushBack() = theta(1), asymptotic
 * da.popFront() = theta(1), asymptotic
 * da.popBack() = theta(1), asymptotic
 */
class DynamicArray<T : Comparable<T>> : ASortableList<T>() {
    private val initialBufferSize = 2
    private var data = Array<Any?>(initialBufferSize) { null }
    private var bufferSize: Int = initialBufferSize // size of buffer
    private var numberOfItems = 0
    private var head: Int = 0 // decreases towards negative numbers and wraps around the array
    private var tail: Int = 0 // increases towards positive numbers
    private val bufferExtensionsScale = 2 // the buffer will increase in size by this scale
    private val bufferShrinkScale = 2 // the buffer will reduce in size by this scale

    // the number of elements ratio to buffer size should fall down this ratio, so that it triggers in resize
    private val itemShrinkRatio = 4


    // ------------------------------------------- Getters ------------------------------------------- //

    /**
     * Returns number of elements currently inside the buffer
     */
    override fun size(): Int = numberOfItems

    /**
     * Get buffer size
     */
    fun bufferSize() = bufferSize

    override fun isEmpty(): Boolean = numberOfItems == 0

    /**
     * Get item at @param index. In case index is greater or equal to #elements inside the container, it
     * @throws IndexOutOfBoundsException .
     */
    override operator fun get(index: Int): T {
        checkSizeAndBounds(index)
        return data[getActualIndex(index)] as T
    }

    override fun getHead(): T = get(0)

    override fun getTail(): T = get(size() - 1)

    /**
     * Pushing an item at index, will put it at index i, so that we can access it using the index i.
     * @param index is in [0, #items]. if index == #items, it will be added as the new tail
     */
    @Throws(IndexOutOfBoundsException::class)
    override fun pushAt(index: Int, item: T) {
        checkSizeAndBounds(index)
        if (isBufferFull()) extendBufferAndCopyData()
        if (index < size() / 2) {
            pushFront(item)
            for (i in 0 until index) exchange(i, i + 1)
        } else {
            pushBack(item);
            for (i in size() - 1 downTo index) exchange(i, i - 1)
        }
    }

    private fun exchange(i: Int, j: Int) {
        val ai = getActualIndex(i)
        val aj = getActualIndex(j)
        val temp = data[ai]
        data[ai] = data[aj]
        data[aj] = temp
    }

    override operator fun set(index: Int, value: T) {
        checkSizeAndBounds(index)
        data[getActualIndex(index)] = value
    }

    private fun getTailIndexInBuffer(): Int = getActualIndex(size() - 1)

    private fun getHeadIndexInBuffer(): Int = getActualIndex(0)

    /**
     * maps an index from [head, tail] to [0, #items]
     * note that head goes to negatives and tail goes to positives
     * @return actual value index, can be used in data[index]
     */
    private fun getActualIndex(index: Int): Int {
        val indexMod = index % bufferSize
        val headMod = head % bufferSize
        val secondMod = (headMod + bufferSize) % bufferSize
        val actualIndex = (indexMod + secondMod) % bufferSize
        return actualIndex
    }

    private fun checkSizeAndBounds(index: Int) {
        if (index < 0 || index >= size()) {
            throw IndexOutOfBoundsException("Index $index is out of bounds")
        }
        if (isEmpty()) {
            throw NoSuchElementException("Array is empty")
        }
    }

    private fun isBufferFull(): Boolean = size() >= bufferSize

    private fun isBufferAlmostEmpty(): Boolean = size() <= (bufferSize / itemShrinkRatio)

    // ------------------------------------------- Setters ------------------------------------------- //

    override fun pushFront(item: T) {
        if (isBufferFull()) extendBufferAndCopyData()
        if (isEmpty()) {
            data[head] = item
        } else {
            // decrease head
            --head
            val headIndex = getHeadIndexInBuffer()
            data[headIndex] = item
        }
        ++numberOfItems
    }

    override fun pushBack(item: T) {
        if (isBufferFull()) extendBufferAndCopyData()
        if (isEmpty()) {
            data[tail] = item
        } else {
            // increase tail
            tail++
            val tailIndex = getTailIndexInBuffer()
            data[tailIndex] = item
        }
        ++numberOfItems
    }

    override fun popFront(): T {
        if (isEmpty()) throw NoSuchElementException()
        if (isBufferAlmostEmpty()) shrinkBufferAndCopyData()
        val headIndex = getHeadIndexInBuffer()
        val item = data[headIndex]
        data[headIndex] = null
        if (size() > 1) ++head
        --numberOfItems
        return item as T
    }

    override fun popBack(): T {
        if (isEmpty()) throw NoSuchElementException()
        if (isBufferAlmostEmpty()) shrinkBufferAndCopyData()
        val tailIndex = getTailIndexInBuffer()
        val item = data[tailIndex]
        data[tailIndex] = null
        if (size() > 1) --tail
        --numberOfItems
        return item as T
    }

    override fun popAt(index: Int): T {
        checkSizeAndBounds(index)
        if (isBufferAlmostEmpty()) shrinkBufferAndCopyData()
        val item: T
        if (index < size() / 2) {
            for (i in index downTo 0) {
                exchange(i, i - 1)
            }
            item = popFront()
        } else {
            for (i in index until size() - 1) {
                exchange(i, i + 1)
            }
            item = popBack()
        }
        return item
    }

    /**
     * will extend the buffer to its @param bufferExtensionsScale size and will reset head and tail
     */
    private fun extendBufferAndCopyData() {
        val numberOfItems = size()
        val extendedSize = bufferExtensionsScale * bufferSize
        val newData: Array<Any?> = Array(extendedSize) { null }
        for (i in 0..<numberOfItems) {
            newData[i] = data[getActualIndex(i)]
        }
        // update the data
        data = newData
        // update head and tail
        head = 0
        tail = numberOfItems - 1
        // update the size
        bufferSize *= bufferExtensionsScale
    }

    /**
     * will shrink the buffer to its @param bufferShrinkScale size and will reset head and tail
     */
    private fun shrinkBufferAndCopyData() {
        val numberOfItems = size()
        val shrinkSize = bufferSize / bufferShrinkScale
        assert(numberOfItems <= shrinkSize / itemShrinkRatio)
        val newData: Array<Any?> = Array(shrinkSize) { null }
        for (i in 0..<numberOfItems) {
            newData[i] = data[getActualIndex(i)]
        }
        // update data
        data = newData
        // update head and tail
        head = 0
        tail = numberOfItems - 1
        // update the size
        bufferSize /= bufferShrinkScale
    }

    /**
     * reserves a buffer of size for the array
     * this method only can extend, not shrink
     * @param reserveSize
     */
    fun reserve(reserveSize: Int) {
        if (reserveSize == bufferSize) {
            return
        }
        if (reserveSize < 2) throw UnsupportedOperationException("Size of $reserveSize is negative")
        if (reserveSize <= this.size()) throw UnsupportedOperationException("Buffer size must be greater than the current size")
        val powerOfTwo = findClosestPowerOfTwoGreaterOrEqualTo(reserveSize)
        val newData: Array<Any?> = Array(powerOfTwo) { null }
        val numberOfItems = size()
        for (i in 0..<numberOfItems) {
            newData[i] = data[getActualIndex(i)]
        }
        data = newData
        head = 0
        tail = numberOfItems - 1
        bufferSize = powerOfTwo
    }

    /**
     * Clears the buffer
     */
    fun clear() {
        data = Array(initialBufferSize) { null }
        numberOfItems = 0
        head = 0
        tail = 0
        bufferSize = initialBufferSize
    }

    /**
     * Complexity Lg(n)
     */
    private fun findClosestPowerOfTwoGreaterOrEqualTo(number: Int): Int {
        var factor = 1
        do {
            factor *= 2
        } while (factor <= number)
        return factor
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            var current = 0
            override fun hasNext(): Boolean {
                return current < size() - 1
            }

            override fun next(): T {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                return data[getActualIndex(++current)] as T
            }
        }
    }
}