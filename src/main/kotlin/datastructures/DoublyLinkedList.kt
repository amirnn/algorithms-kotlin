package datastructures

class DoublyLinkedList<T> : Iterable<DoublyLinkedList.Node<T>> {

    data class Node<T>(var value: T, var next: Node<T>? = null, var previous: Node<T>? = null)

    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    private var size = 0

    fun getSize() = size

    /**
     * Pushes a new node to the head
     */
    fun pushFront(value: T) {
        if (size == 0) {
            head = Node(value)
            tail = head

        } else {
            val newNode = Node(value, head)
            head?.previous = newNode
            head = newNode
        }
        // update size
        size++
    }

    /**
     * Pushes a new node to the tail
     */
    fun pushBack(value: T) {
        if (size == 0) {
            tail = Node(value)
            head = tail
        } else {
            val newNode = Node(value, null, tail)
            tail?.next = newNode
            tail = newNode
        }
        // update size
        size++
    }

    /**
     * Returns the element at the head
     */
    fun popFront(): Node<T>? {
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
        return item
    }

    fun popBack(): Node<T>? {
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
        return item
    }

    override fun iterator(): Iterator<Node<T>> {
        return object : Iterator<Node<T>> {
            var current: Node<T>? = head
            override fun hasNext(): Boolean {
                return current != null
            }

            override fun next(): Node<T> {
                if (current == null || current?.next == null) {
                    throw NoSuchElementException()
                }
                current = current?.next
                return current!!
            }
        }
    }
}