/**
 * Directed Acyclic Graph
 *
 * Created by michele on 14/02/16.
 *
 */

package graph

import java.util.*

class DAG<T>(nodes: Collection<T>) {
    private val nodes = HashMap<T, Node<T>>()
    private val edges = HashSet<Edge<T>>()

    init {
        nodes.map { addNode(it) }
    }

    constructor(vararg nodes: T) : this(nodes.toList())


    fun nodes(): Collection<Node<T>> {
        return nodes.values.toSet()
    }

    fun edges(): Collection<Edge<T>> {
        return edges.toSet()
    }

    fun addNode(context: T): Node<T> {
        var node = nodes[context] ?: Node(this, context)
        nodes[context] = node
        return node
    }

    operator fun get(context: T): Node<T> {
        return nodes[context] ?: throw IllegalArgumentException("Invalid Node " + context.toString())
    }

    operator fun get(from: T, to: T): Edge<T> {
        var edge = connection(from, to)
        if (to !in edge.from.outputs()) {
            throw IllegalArgumentException("Invalid Edge")
        }
        return edge
    }

    fun addEdge(from: T, to: T): Edge<T> {
        var edge = connection(from, to)
        fixConnection(edge)
        return edge
    }

    private fun connection(from: T, to: T): Edge<T> {
        return Edge(this[from], this[to])
    }

    private fun fixConnection(edge: Edge<T>) {
        edges.add(edge)
        with (edge) {
            from.outputConnection(to)
            to.inputConnection(from)
        }
    }
}

data class Edge<T>(var from: Node<T>, var to: Node<T>) {
    val graph = from.graph

    fun asPair(): Pair<T, T> {
        return Pair(from.context, to.context)
    }
}

class Node<T>(var graph: DAG<T>, var context: T) {
    private val inputs = HashMap<T, Node<T>>()
    private val outputs = HashMap<T, Node<T>>()

    @Suppress("unused")
    fun input(): Collection<T> {
        return inputs.keys.toSet()
    }

    fun outputs(): Collection<T> {
        return outputs.keys.toSet()
    }

    internal fun inputConnection(from: Node<T>) {
        inputs[from.context] = from
    }

    internal fun outputConnection(to: Node<T>) {
        outputs[to.context] = to
    }

    fun bfs(): List<T> {
        return bfsNodes().map { it.context }
    }

    fun bfsEdges(): List<Pair<T, T>> {
        val edges = mutableListOf<Pair<T, T>>()
        bfsNodes().forEach {
            from ->
            edges.addAll(
                    from.outputs().map {
                        to ->
                        Pair(from.context, to)
                    }
            )
        }
        return edges
    }

    fun nodes(): Set<T> {
        return bfs().toSet()
    }

    fun edges(): Set<Pair<T, T>> {
        return bfsEdges().toSet()
    }

    fun ends(): Set<T> {
        return bfsNodes().
                filter { it.endPoint() }.
                map { it.context }.toSet()
    }

    private fun endPoint(): Boolean {
        return outputs.isEmpty()
    }

    internal fun bfsNodes(): List<Node<T>> {
        val bfs = mutableListOf<Node<T>>()
        val visited = mutableSetOf<Node<T>>()
        val stack = mutableListOf(this)
        while (stack.isNotEmpty()) {
            val node = stack.removeAt(0)
            if (node in visited) {
                continue
            }
            visited.add(node)
            bfs.add(node)
            stack.addAll(node.outputs.values)
        }
        return bfs
    }

    internal fun dfsNodes(): List<Node<T>> {
        val dfs = mutableListOf<Node<T>>()
        val visited = mutableSetOf<Node<T>>()
        val nodes = mutableListOf(this)
        while (nodes.isNotEmpty()) {
            val node = nodes.removeAt(0)
            if (node in visited) {
                continue
            }
            visited.add(node)
            dfs.add(node)
            nodes.addAll(0, node.outputs.values)
        }
        return dfs
    }

    internal fun dfsEdgesPrivate(): List<Edge<T>> {
        val dfs = mutableListOf<Edge<T>>()
        val visited = mutableSetOf(this)
        val edges = outputEdges().toMutableList()
        while (edges.isNotEmpty()) {
            val edge = edges.removeAt(0)
            val node = edge.to
            dfs.add(edge)
            if (node !in visited) {
                visited.add(node)
                edges.addAll(0, node.outputEdges())
            }
        }
        return dfs
    }

    internal fun outputEdges(): List<Edge<T>> {
        return outputs.values.map { Edge(this, it) }
    }

    fun dfs(): List<T> {
        return dfsNodes().map { it.context }
    }

    fun dfsEdges(): List<Pair<T, T>> {
        return dfsEdgesPrivate().map { it.asPair() }
    }


}