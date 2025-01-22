package datastructures

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
        size++
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
        size++
    }

    override fun size(): Int  = size

    override fun isEmpty(): Boolean = (size == 0)

    override fun get(index: Int): T  = getNthItem(index)

    // this method can be improved to have thetha(lg(n)) rather than linear
    private fun getNthItem(index: Int): T {
        if (isEmpty()) throw NoSuchElementException()
        var node = head
        for ( i in 1 until index) {
            node = node!!.next
        }
        return node!!.value
    }

    private fun getNthNode(index: Int): Node<T> {
        if (isEmpty()) throw NoSuchElementException()
        var node = head
        for ( i in 1 until index) {
            node = node!!.next
        }
        return node!!
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
            }
        }
        // update size
        size--
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
            }
        }
        // update size
        size--
        return item!!.value
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