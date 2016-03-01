import unittest

from dag.dag import Dag


class TestDAG(unittest.TestCase):
    def test_empty(self):
        dag = Dag()
        self.assertEqual(0, len(dag.nodes()))
        self.assertEqual(0, len(dag.edges()))

    def test_one_node(self):
        dag = Dag()
        node = dag.add_node(3)
        self.assertEqual({3}, dag.nodes())
        self.assertEqual(set(), dag.edges())
        self.assertEqual(3, node.context)
        self.assertEqual(dag, node.graph)
        self.assertIs(node, dag[3])

    def test_two_nodes(self):
        dag = Dag()
        n0 = dag.add_node(3)
        n1 = dag.add_node(5)
        self.assertEqual({3, 5}, dag.nodes())
        self.assertIs(n0, dag[3])
        self.assertIs(n1, dag[5])

    def test_add_same_node(self):
        dag = Dag()
        n = dag.add_node(3)
        self.assertIs(n, dag.add_node(3))

    def test_fetch_invalid_node(self):
        dag = Dag()
        with self.assertRaises(KeyError):
            # noinspection PyStatementEffect
            dag[5]

    def test_constructor(self):
        dag = Dag(3, 7, 5)
        self.assertEqual({3, 5, 7}, dag.nodes())

    def test_add_edge(self):
        dag = Dag(9, 2)
        edge = dag.add_edge(9, 2)
        self.assertIs(dag[9], edge.src)
        self.assertIs(dag[2], edge.dst)
        self.assertEqual({edge}, dag.edges())

    def test_two_edges(self):
        dag = Dag(1, 2, 3)
        edge0 = dag.add_edge(1, 2)
        edge1 = dag.add_edge(2, 3)
        self.assertEqual({edge0, edge1}, dag.edges())

    def test_add_same_edges(self):
        dag = Dag(1, 2)
        edge = dag.add_edge(1, 2)
        self.assertIs(edge, dag.add_edge(1, 2))

    def test_invalid_edge(self):
        dag = Dag(1, 2)
        with self.assertRaises(KeyError):
            dag.add_edge(1, 3)
        with self.assertRaises(KeyError):
            dag.add_edge(5, 2)
        dag.add_edge(1, 2)

    def test_edge(self):
        dag = Dag(1, 2)
        edge = dag.add_edge(1, 2)
        self.assertIs(edge, dag.edge(1, 2))

    def test_bfs_trivial(self):
        dag = Dag(1, 2)
        self.assertEqual([1], dag[1].bsf())


if __name__ == '__main__':
    unittest.main()
