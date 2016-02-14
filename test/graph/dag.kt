/**
 * Created by michele on 14/02/16.
 * Test DAG
 */

package graph

import kotlin.test.*
import org.junit.*

class TestDAG {

    @Test
    fun empty() {
        val dag = DAG<Int>()
        assertEquals(0, dag.nodes().size)
        assertEquals(0, dag.edges().size)
    }

    @Test
    fun oneNode() {
        val dag = DAG<Int>()
        val node = dag.addNode(3)
        assertEquals(setOf(node), dag.nodes())
        assertEquals(3, node.context)
        assertEquals(dag, node.graph)
        assertEquals(node, dag[3])
    }

    @Test
    fun twoNodes() {
        val dag = DAG<Int>()
        val n0 = dag.addNode(3)
        val n1 = dag.addNode(5)
        assertEquals(setOf(n0, n1), dag.nodes())
        assertEquals(n0, dag[3])
        assertEquals(n1, dag[5])
    }

    @Test
    fun addSameNode() {
        val dag = DAG<Int>()
        val n0 = dag.addNode(3)
        assertEquals(n0, dag.addNode(3))
    }

    @Test
    fun notPresentNode() {
        val dag = DAG<Int>()
        assertFailsWith(IllegalArgumentException::class) {
            dag[2]
        }
    }

    @Test
    fun constructors() {
        var dag = DAG(listOf(4, 1, 7))
        assertEquals(1, dag[1].context)
        assertEquals(7, dag[7].context)
        assertEquals(4, dag[4].context)

        dag = DAG(4, 1, 7)
        assertEquals(1, dag[1].context)
        assertEquals(7, dag[7].context)
        assertEquals(4, dag[4].context)
    }

    @Test
    fun addEdge() {
        val dag = DAG(1, 2)
        val edge = dag.addEdge(1, 2)
        assertEquals(dag, edge.graph)
        assertEquals(dag[1], edge.from)
        assertEquals(dag[2], edge.to)
        assertEquals(setOf(edge), dag.edges())
    }

    @Test
    fun twoEdges() {
        val dag = DAG(1, 2, 3)
        val edge0 = dag.addEdge(1, 2)
        val edge1 = dag.addEdge(2, 3)
        assertEquals(setOf(edge0, edge1), dag.edges())
    }

    @Test
    fun addSameEdge() {
        val dag = DAG(1, 2)
        val edge = dag.addEdge(1, 2)
        assertEquals(edge, dag.addEdge(1, 2))
    }

    @Test
    fun invalidEdge0() {
        val dag = DAG(1, 2)
        assertFailsWith(IllegalArgumentException::class) {
            dag.addEdge(0, 3)
        }
        assertFailsWith(IllegalArgumentException::class) {
            dag.addEdge(1, 3)
        }
        dag.addEdge(1, 2)
    }

    @Test
    fun getEdge() {
        val dag = DAG(1, 2)
        assertFailsWith(IllegalArgumentException::class) {
            dag[1, 2]
        }
        val edge = dag.addEdge(1, 2)
        assertEquals(edge, dag[1, 2])
    }

    @Test
    fun edgeAsPair() {
        val dag = DAG(1, 2)
        assertEquals(Pair(1, 2), dag.addEdge(1, 2).asPair())
    }

    @Test
    fun bfsTrivial0() {
        val dag = DAG(1, 2)
        assertEquals(listOf(1), dag[1].bfs())
        assertEquals(listOf(2), dag[2].bfs())
        dag.addEdge(1, 2)
        assertEquals(listOf(1, 2), dag[1].bfs())
        assertEquals(listOf(2), dag[2].bfs())
    }

    @Test
    fun bfs() {
        val dag = DAG((1..10).toList())
        with(dag) {
            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(1, 4)
            addEdge(2, 4)
            addEdge(3, 5)
            addEdge(3, 6)
            addEdge(3, 8)
            addEdge(6, 7)
            addEdge(6, 9)
        }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 8, 7, 9), dag[1].bfs())
    }

    @Test
    fun bfsEdgesTrivial0() {
        val dag = DAG(1, 2)
        assertEquals(listOf<Pair<Int, Int>>(), dag[1].bfsEdges())
        assertEquals(listOf<Pair<Int, Int>>(), dag[2].bfsEdges())
        dag.addEdge(1, 2)
        assertEquals(listOf(Pair(1, 2)), dag[1].bfsEdges())
        assertEquals(listOf<Pair<Int, Int>>(), dag[2].bfsEdges())
    }

    @Test
    fun bfsEdges() {
        val dag = DAG((1..10).toList())
        with(dag) {
            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(1, 4)
            addEdge(2, 4)
            addEdge(3, 5)
            addEdge(3, 6)
            addEdge(3, 8)
            addEdge(6, 7)
            addEdge(6, 9)
        }
        assertEquals(listOf(
                Pair(1, 2),
                Pair(1, 3),
                Pair(1, 4),
                Pair(2, 4),
                Pair(3, 5),
                Pair(3, 6),
                Pair(3, 8),
                Pair(6, 7),
                Pair(6, 9)), dag[1].bfsEdges())
    }

    @Test
    fun dfsSimple() {
        val dag = DAG(1, 2, 3, 4)
        with(dag) {
            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(2, 4)
            addEdge(3, 4)
        }
        assertEquals(listOf(1, 2, 4, 3), dag[1].dfs())
    }

    @Test
    fun dfs() {
        val dag = DAG((1..10).toList())
        with(dag) {
            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(1, 4)
            addEdge(2, 4)
            addEdge(3, 5)
            addEdge(3, 6)
            addEdge(3, 8)
            addEdge(6, 7)
            addEdge(6, 9)
        }
        assertEquals(listOf(1, 2, 4, 3, 5, 6, 7, 9, 8), dag[1].dfs())
    }


    @Test
    fun dfsEdges() {
        val dag = DAG((1..10).toList())
        with(dag) {
            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(1, 4)
            addEdge(2, 4)
            addEdge(3, 5)
            addEdge(3, 6)
            addEdge(3, 8)
            addEdge(6, 7)
            addEdge(6, 9)
        }
        assertEquals(listOf(
                Pair(1, 2),
                Pair(2, 4),
                Pair(1, 3),
                Pair(3, 5),
                Pair(3, 6),
                Pair(6, 7),
                Pair(6, 9),
                Pair(3, 8),
                Pair(1, 4)
                ), dag[1].dfsEdges())
    }

    @Test
    fun nodes() {
        val dag = DAG((1..10).toList())
        assertEquals(setOf(1), dag[1].nodes())
        with(dag) {
            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(1, 4)
            addEdge(2, 3)
            addEdge(2, 4)
            addEdge(5, 6)
            addEdge(5, 7)
            addEdge(6, 9)
        }
        assertEquals(setOf(1, 2, 3, 4), dag[1].nodes())
        assertEquals(setOf(2, 3, 4), dag[2].nodes())
        assertEquals(setOf(3), dag[3].nodes())
        assertEquals(setOf(4), dag[4].nodes())
        assertEquals(setOf(5, 6, 7, 9), dag[5].nodes())
        assertEquals(setOf(6, 9), dag[6].nodes())
        assertEquals(setOf(7), dag[7].nodes())
        assertEquals(setOf(8), dag[8].nodes())
        assertEquals(setOf(9), dag[9].nodes())
        assertEquals(setOf(10), dag[10].nodes())
    }

    @Test
    fun edges() {
        val dag = DAG((1..10).toList())
        assertEquals(setOf<Pair<Int, Int>>(), dag[1].edges())
        with(dag) {
            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(1, 4)
            addEdge(2, 3)
            addEdge(2, 4)
            addEdge(5, 6)
            addEdge(5, 7)
            addEdge(6, 9)
        }
        assertEquals(setOf(
                Pair(1, 2),
                Pair(1, 3),
                Pair(1, 4),
                Pair(2, 3),
                Pair(2, 4)), dag[1].edges())
        assertEquals(setOf(
                Pair(2, 3),
                Pair(2, 4)), dag[2].edges())
        assertEquals(setOf(
                Pair(5, 6),
                Pair(5, 7),
                Pair(6, 9)), dag[5].edges())
        assertEquals(setOf(Pair(6, 9)), dag[6].edges())
        for (n in listOf(3, 4, 7, 8, 9, 10)) {
            assertEquals(setOf(n), dag[n].nodes())
        }
    }

    @Test
    fun ends() {
        val dag = DAG((1..10).toList())
        assertEquals(setOf(1), dag[1].ends())
        with(dag) {
            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(1, 4)
            addEdge(2, 3)
            addEdge(2, 4)
            addEdge(5, 6)
            addEdge(5, 7)
            addEdge(6, 9)
        }
        assertEquals(setOf(3, 4), dag[1].ends())
        assertEquals(setOf(7, 9), dag[5].ends())

    }
}

