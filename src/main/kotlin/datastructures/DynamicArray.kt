// Copyright Amir Nourinia 2025

package datastructures


/**
 * Dynamic Array that is implemented using a circular buffer.
 * Complexity:
 * da[random] = theta(1)
 * da.pushFront() = theta(1), asymptotic
 * da.pushBack() = theta(1), asymptotic
 * da.popFront() = theta(1), asymptotic
 * da.popBack() = theta(1), asymptotic
 */
class DynamicArray<T> : Iterable<T> {
    private val initialBufferSize = 2
    private var data = Array<Any?>(initialBufferSize) { null }
    private var bufferSize: Int = initialBufferSize // size of buffer
    private var head: Int = 0 // decreases towards negative numbers and wraps around the array
    private var tail: Int = 0 // increases towards positive numbers
    private val bufferExtensionsScale = 2 // the buffer will increase in size by this scale
    private val bufferShrinkScale = 2 // the buffer will reduce in size by this scale

    // the number of elements ratio to buffer size should fall down this ratio, so that it triggers in resize
    private val itemShrinkRatio = 4


    // ------------------------------------------- Getters

    /**
     * Returns number of elements currently inside the buffer
     */
    fun size() = getNumberOfItems()

    /**
     * Get buffer size
     */
    fun bufferSize() = bufferSize

    fun isEmpty(): Boolean {
        val headIsEmpty = data[head] == null
        val tailIsEmpty = data[tail] == null
        val headAndTailAreSame = head == tail
        return headAndTailAreSame && tailIsEmpty && headIsEmpty
    }

    /**
     * Get item at @param index. In case index is greater or equal to #elements inside the container, it
     * @throws IndexOutOfBoundsException .
     */
    operator fun get(index: Int): T {
        if (index >= getNumberOfItems()) {
            throw IndexOutOfBoundsException("Index $index is out of bounds")
        }
        return data[getCorrectIndex(index)] as T
    }

    operator fun set(index: Int, value: T) {
        if (index >= getNumberOfItems()) {
            throw IndexOutOfBoundsException("Index $index out of bounds")
        }
        data[getCorrectIndex(index)] = value
    }

    // ------------------------------------------- Setters
    fun pushFront(item: T) {
        if (isBufferFull()) extendBufferAndCopyData()
        if (isEmpty()) {
            data[head] = item
            return
        }
        // decrease head
        --head
        val headIndex = getHeadIndexInBuffer()
        data[headIndex] = item
    }

    fun pushBack(item: T) {
        if (isBufferFull()) extendBufferAndCopyData()
        if (isEmpty()) {
            data[tail] = item
            return
        }
        // increase tail
        tail++
        val tailIndex = getTailIndexInBuffer()
        data[tailIndex] = item
    }

    fun popFront(): T {
        if (bufferNeedsShrinking()) shrinkBufferAndCopyData()
        val item = data[head]
        data[head] = null
        if (!isEmpty()) ++head
        return item as T
    }

    fun popBack(): T {
        if (bufferNeedsShrinking()) shrinkBufferAndCopyData()
        val item = data[tail]
        data[tail] = null
        if (!isEmpty()) --tail
        return item as T
    }

    /**
     * Get number of items inside the array
     */
    private fun getNumberOfItems(): Int = (tail - head + 1)

    /**
     * will extend the buffer to its @param bufferExtensionsScale size and will reset head and tail
     */
    private fun extendBufferAndCopyData() {
        val numberOfItems = getNumberOfItems()
        val newData: Array<Any?> = Array(bufferExtensionsScale * bufferSize) { null }
        for (i in 0..<numberOfItems) {
            newData[i] = data[getCorrectIndex(i)]
        }
        // update the data
        data = newData
        // update head and tail
        head = 0
        tail = numberOfItems - 1
        // update the size
        bufferSize *= 2
    }


    /**
     * will shrink the buffer to its @param bufferShrinkScale size and will reset head and tail
     */
    private fun shrinkBufferAndCopyData() {
        val numberOfItems = getNumberOfItems()
        val shrinkSize = bufferSize / bufferShrinkScale
        assert(numberOfItems <= shrinkSize / itemShrinkRatio)
        val newData: Array<Any?> = Array(shrinkSize) { null }
        for (i in 0..<numberOfItems) {
            newData[i] = data[getCorrectIndex(i)]
        }
        // update data
        data = newData
        // update head and tail
        head = 0
        tail = numberOfItems - 1
        // update the size
        bufferSize /= bufferShrinkScale
    }

    private fun getTailIndexInBuffer(): Int = getCorrectIndex(tail)

    private fun getHeadIndexInBuffer(): Int = getCorrectIndex(head)

    private fun getCorrectIndex(index: Int): Int = (index + head + bufferSize) % bufferSize

    private fun isBufferFull(): Boolean = getNumberOfItems() >= bufferSize // this is ((tail - head) + 1) % size

    private fun bufferNeedsShrinking(): Boolean = getNumberOfItems() <= (bufferSize / itemShrinkRatio)


    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            var current = head
            override fun hasNext(): Boolean {
                return current < tail
            }

            override fun next(): T {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                return data[++current] as T
            }
        }
    }
}