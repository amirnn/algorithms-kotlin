package datastructures


/**
 * A Doubly Linked List.
 * Complexity:
 * pushFront() = theta(1)
 * pushBack() = theta(1)
 * popFront() = theta(1)
 * popBack() = theta(1)
 * doublyLinkedList[n] = theta(n) (get operator)
 *
 * Note that I can improve the get operator to tetha lg(n)
 */
class DoublyLinkedList<T> : IList<T> {

    data class Node<T>(var value: T, var next: Node<T>? = null, var previous: Node<T>? = null)

    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    private var size = 0

    /**
     * Pushes a new node to the head
     */
    override fun pushFront(item: T) {
        if (size == 0) {
            head = Node(item)
            tail = head

        } else {
            val newNode = Node(item, head)
            head?.previous = newNode
            head = newNode
        }
        // update size
        ++size
    }

    /**
     * Pushes a new node to the tail
     */
    override fun pushBack(item: T) {
        if (size == 0) {
            tail = Node(item)
            head = tail
        } else {
            val newNode = Node(item, null, tail)
            tail?.next = newNode
            tail = newNode
        }
        // update size
        ++size
    }

    override fun size(): Int = size

    override fun isEmpty(): Boolean = (size == 0)

    override fun get(index: Int): T = getNthItem(index)

    // this method can be improved to have thetha(lg(n)) rather than linear
    private fun getNthItem(index: Int): T {
        checkSizeAndBounds(index)
        var node = head
        for (i in 1 until index) {
            node = node!!.next
        }
        return node!!.value
    }

    private fun getNthNode(index: Int): Node<T> {
        checkSizeAndBounds(index)
        var node = head
        for (i in 1 until index) {
            node = node!!.next
        }
        return node!!
    }

    private fun checkSizeAndBounds(index: Int) {
        if (isEmpty()) throw NoSuchElementException()
        if (index > size - 1) throw IndexOutOfBoundsException()
    }


    override fun getHead(): T {
        if (isEmpty()) {
            throw NoSuchElementException()
        }
        return head!!.value
    }

    override fun getTail(): T {
        if (isEmpty()) {
            throw NoSuchElementException()
        }
        return tail!!.value
    }

    override fun pushAt(index: Int, item: T) {
        checkSizeAndBounds(index)
        when (index) {
            0 -> {
                pushFront(item)
            }

            size - 1 -> {
                pushBack(item)
            }

            else -> {
                val newNode = Node(item)
                val oldNthNode = getNthNode(index)
                oldNthNode.previous?.next = newNode // re-point the n-1th node to the new node
                newNode.previous = oldNthNode.previous // point to n-1th node from the new node
                oldNthNode.previous = newNode // re-point the old nth node to the new node
                newNode.next = oldNthNode
            }
        }
        // update size
        ++size
    }

    /**
     * Returns the element at the head
     */
    override fun popFront(): T {
        val item: Node<T>?
        when (size) {
            0 -> item = null
            1 -> {
                item = head
                head = null
                tail = null
            }

            else -> {
                item = head
                head = head?.next
                head?.previous = null // the new head should point to null
                item?.next = null // point to null, not necessary but to be correct
            }
        }
        // update size
        --size
        return item!!.value
    }

    override fun popBack(): T {
        val item: Node<T>?
        when (size) {
            0 -> item = null
            1 -> {
                item = tail
                head = null
                tail = null
            }

            else -> {
                item = tail
                tail = tail?.previous
                tail?.next = null // the new tail should point to null
                item?.previous = null // point to null, not necessary but to be correct
            }
        }
        // update size
        --size
        return item!!.value
    }

    override fun popAt(index: Int): T {
        checkSizeAndBounds(index)
        val item: T
        when (index) {
            0 -> item = popFront()
            size - 1 -> item = popBack()
            else -> {
                val nthNode = getNthNode(index)
                nthNode.previous?.next = nthNode.next
                nthNode.next?.previous = nthNode.previous
                nthNode.previous = null
                nthNode.next = null
                item = nthNode.value
            }
        }
        // update size
        --size
        return item
    }

    override fun set(index: Int, value: T) {
        val node = getNthNode(index)
        node.value = value
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            var current: Node<T>? = head
            override fun hasNext(): Boolean {
                return current != null
            }

            override fun next(): T {
                if (current == null || current?.next == null) {
                    throw NoSuchElementException()
                }
                current = current?.next
                return current!!.value
            }
        }
    }
}