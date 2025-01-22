package datastructures

interface IList<T>: Iterable<T> {
    // getters
    fun size(): Int
    fun isEmpty(): Boolean
    operator fun get(index: Int): T
    fun getHead(): T
    fun getTail(): T

    // setters
    fun pushFront(item: T)
    fun pushBack(item: T)

    fun pushAt(index: Int, item: T)

    fun popFront(): T
    fun popBack(): T

    fun popAt(index: Int): T

    operator fun set(index: Int, value: T)

}