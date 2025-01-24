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
class DynamicArray<T> : IList<T> {
    private val initialBufferSize = 2
    private var data = Array<Any?>(initialBufferSize) { null }
    private var bufferSize: Int = initialBufferSize // size of buffer
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
    override fun size() = getNumberOfItems()

    /**
     * Get buffer size
     */
    fun bufferSize() = bufferSize

    override fun isEmpty(): Boolean {
        val headIndex = getHeadIndexInBuffer()
        val tailIndex = getTailIndexInBuffer()
        val headIsEmpty = data[headIndex] == null
        val tailIsEmpty = data[tailIndex] == null
        val headAndTailAreSame = headIndex == tailIndex
        return headAndTailAreSame && tailIsEmpty && headIsEmpty
    }

    /**
     * Get item at @param index. In case index is greater or equal to #elements inside the container, it
     * @throws IndexOutOfBoundsException .
     */
    override operator fun get(index: Int): T {
        checkBounds(index)
        return data[getActualIndex(index)] as T
    }

    override fun getHead(): T = get(0)

    override fun getTail(): T = get(size() - 1)

    override fun pushAt(index: Int, item: T) {
        checkBounds(index)
        if (isBufferFull()) {
            extendBufferAndCopyData()
        }
        // where to put data in the array
        val injectionActualIndex = getActualIndex(index)
        // index in [head, tail] index space
        val transformedIndex = index + head
        if (index < size() / 2) {
            // closer to head
            shiftItemsToLeft(transformedIndex)
        } else {
            // closer to tail
            shiftItemsToRight(transformedIndex)
        }
        assert(data[injectionActualIndex] == null)
        data[injectionActualIndex] = item
    }

    override operator fun set(index: Int, value: T) {
        checkBounds(index)
        data[getActualIndex(index)] = value
    }

    /**
     * Get number of items inside the array
     */
    private fun getNumberOfItems(): Int {
        return if (isEmpty()) {
            0
        } else {
            (tail - head + 1)
        }
    }

    private fun getTailIndexInBuffer(): Int = getActualIndex(tail)

    private fun getHeadIndexInBuffer(): Int = getActualIndex(0)

    /**
     * maps an index from [head, tail] to [0, #items]
     * note that head goes to negatives and tail goes to positives
     * @return actual value index, can be used in data[index]
     */
    private fun getActualIndex(index: Int): Int {
        checkBounds(index)
        return (index + head + bufferSize) % bufferSize
    }

    private fun checkBounds(index: Int) {
        if (index >= getNumberOfItems()) {
            throw IndexOutOfBoundsException("Index $index is out of bounds")
        }
    }

    private fun isBufferFull(): Boolean = getNumberOfItems() >= bufferSize // this is ((tail - head) + 1) % size
    private fun isBufferCloseToGetFull(): Boolean =
        getNumberOfItems() == bufferSize - 1 // this is ((tail - head) + 1) % size

    private fun bufferNeedsShrinking(): Boolean = getNumberOfItems() <= (bufferSize / itemShrinkRatio)
    private fun isBufferCloseToNeedShrinking(): Boolean = getNumberOfItems() <= (bufferSize / itemShrinkRatio) + 1

    // ------------------------------------------- Setters ------------------------------------------- //

    override fun pushFront(item: T) {
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

    override fun pushBack(item: T) {
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

    override fun popFront(): T {
        if (isEmpty()) throw NoSuchElementException()
        if (bufferNeedsShrinking()) shrinkBufferAndCopyData()
        val headIndex = getHeadIndexInBuffer()
        val item = data[headIndex]
        data[headIndex] = null
        if (!isEmpty()) ++head
        return item as T
    }

    override fun popBack(): T {
        if (isEmpty()) throw NoSuchElementException()
        if (bufferNeedsShrinking()) shrinkBufferAndCopyData()
        val tailIndex = getTailIndexInBuffer()
        val item = data[tailIndex]
        data[tailIndex] = null
        if (!isEmpty()) --tail
        return item as T
    }

    override fun popAt(index: Int): T {
        checkBounds(index)
        if (bufferNeedsShrinking()) shrinkBufferAndCopyData()
        val item = data[getActualIndex(index)] as T
        val transformedIndex = index + head
        if (index < size() / 2) {
            // closer to head
            shiftItemsToRight(startFrom = head, stopAt = transformedIndex)
        } else {
            // closer to tail
            shiftItemsToLeft(startFrom = tail, stopAt = transformedIndex)
        }
        return item
    }

    /**
     * will extend the buffer to its @param bufferExtensionsScale size and will reset head and tail
     */
    private fun extendBufferAndCopyData() {
        val numberOfItems = getNumberOfItems()
        val newData: Array<Any?> = Array(bufferExtensionsScale * bufferSize) { null }
        for (i in 0..<numberOfItems) {
            newData[i] = data[getActualIndex(i)]
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
        if (reserveSize < 2) throw UnsupportedOperationException("Size of $reserveSize is negative")
        if (reserveSize <= this.size()) throw UnsupportedOperationException("Buffer size must be greater than the current size")
        val powerOfTwo = findClosestPowerOfTwoGreaterOrEqualTo(reserveSize)
        val newData: Array<Any?> = Array(powerOfTwo) { null }
        val numberOfItems = getNumberOfItems()
        for (i in 0..<numberOfItems) {
            newData[i] = data[getActualIndex(i)]
        }
        data = newData
        head = 0
        tail = numberOfItems - 1
        bufferSize = powerOfTwo
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

    /**
     * Shifts items one position towards stopAt and starting form:
     * @param startFrom is in [head, tail] index space
     * @param stopAt is in [head, tail] index space
     * Note that startFrom must be <= stopAt
     */
    private fun shiftItemsToRight(startFrom: Int, stopAt: Int = tail) {
        checkBounds(startFrom)
        val actualStartingIndex = getActualIndex(startFrom)
        var shiftingItem = data[actualStartingIndex] as T
        for (i in startFrom + 1..stopAt) {
            val actualIndex = getActualIndex(i)
            val temp = data[actualIndex] as T
            data[actualIndex] = shiftingItem
            shiftingItem = temp
        }
        data[actualStartingIndex] = null
    }

    /**
     * Shift items one position towards stopAt, starting at startFrom and stopping at stopAt
     * @param startFrom Note that startFrom will also be shifted one position to the left,
     * and it is in [head, tail]
     * Note that startFrom must be >= stopAt
     */
    private fun shiftItemsToLeft(startFrom: Int, stopAt: Int = head) {
        checkBounds(startFrom)
        val actualStartingIndex = getActualIndex(startFrom)
        var shiftingItem = data[actualStartingIndex] as T
        for (i in startFrom - 1 downTo stopAt) {
            val actualIndex = getActualIndex(i)
            val temp = data[actualIndex] as T
            data[actualIndex] = shiftingItem
            shiftingItem = temp
        }
        data[actualStartingIndex] = null
    }

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