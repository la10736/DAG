class Node(object):
    def __init__(self, graph, context):
        self._graph = graph
        self._context = context

    @property
    def graph(self):
        return self._graph

    @property
    def context(self):
        return self._context

    def bsf(self):
        return [self.context]


class Edge(object):
    def __init__(self, graph, src, dst):
        """
        :param Dag graph:
        :param Node src:
        :param Node dst:
        """
        self._graph = graph
        self._src = src
        self._dst = dst

    @property
    def graph(self):
        return self._graph

    @property
    def src(self):
        return self._src

    @property
    def dst(self):
        return self._dst


class Dag(object):
    def __init__(self, *nodes):
        super().__init__()
        self._nodes = {}
        for n in nodes:
            self.add_node(n)
        self._edges = {}

    def nodes(self):
        return set(self._nodes.keys())

    def edges(self):
        return set(self._edges.values())

    def add_node(self, context):
        """
        :param context: Node context
        :rtype: Node
        """
        if context not in self._nodes:
            self._nodes[context] = Node(self, context)
        return self._nodes[context]

    def add_edge(self, src, dst):
        """
        :param src: from
        :param dst: to
        :rtype: Edge
        """
        k = (src, dst)
        if k not in self._edges:
            edge = Edge(self, self._nodes[src], self._nodes[dst])
            self._edges[k] = edge
        return self._edges[k]

    def __getitem__(self, context):
        """
        :param context: Node context
        :rtype: Node
        """
        return self._nodes[context]

    def edge(self, src, dst):
        return self._edges[(src, dst)]
