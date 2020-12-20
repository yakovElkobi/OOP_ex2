   ![](https://github.com/yakovElkobi/OOP_ex2/blob/master/resources/906edc8b6f1b7089442ce99ca0b5a7a2.jpg)

In this pokemon game you have a grop of agents and Pokemons that appear in randomly way.

The goal to is to catch as much as you can before that time is running out.
The movements is on weighted directed graph.

Player Stats
![](https://github.com/yakovElkobi/OOP_ex2/blob/master/resources/pokeboll_v2.png)



| **Methods**      |    **Details**        | **Complexity** |
|-----------------|-----------------------|----------------|
| `WGraph_DS()` | Default constructor     |
| `getNode()` | Returns a node by the nodeKey |
| `hasEdge()` | Checks is two nodes are connected | O(1) |
| `getEdge()` | Returns the weight of an edge between two nodes | O(1) |
| `addNode()` | Adds a new node to the graph | O(1) |
| `connect()` | Connects two nodes in the graph | O(1) |
| `getV()` | Returns a collection view of the graph | O(1) |
| `getV(int node_id)` | Returns a collection view of the graph | O(1), Originally O(k). k=node degree |
| `removeNode()` | Removed a node from the graph | O(n) |
| `removeEdge()` | Remove an edge between two nodes in the graph | O(1) |
| `nodeSize()` | Returns the number of the nodes in the graph | O(1) |
| `edgeSize()` | Returns the number of the edges in the graph | O(1) |
| `getMC()` | Returns the number of mode counts in the graph, Every change in the internal state of the graph counts as a mode count | O(1) |
| `equals()` | Compares two graphs and cheks if they are equal |
| `toString()` | Creates a String representing the graph, adds each and every connection |
