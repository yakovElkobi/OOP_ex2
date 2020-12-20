package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
/**
 * Graph_DS implements the directed_weighted_graph interface and represents the Graph structure
 * all nodes in the graph are stores in HashMap and the edges are stores in double HashMap
 * the edgeSize is counting within add\remove edges methods
 * The modCount is increase on each method that changes something in the graph
 */
public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> nodes;
    private HashMap<Integer,HashMap<Integer, edge_data>> outEdgs;
    private HashMap<Integer,HashMap<Integer,edge_data>> inEdgs;
    private int edgeCount,mc;

    /**
     * default constructor
     */
    public DWGraph_DS(){
        nodes = new HashMap<Integer, node_data>();
        outEdgs = new HashMap<Integer,HashMap<Integer, edge_data>>();
        inEdgs = new HashMap<Integer,HashMap<Integer, edge_data>>();
        edgeCount = 0;
        mc = 0;
    }

    /**
     * returns the node_data by the node_id,
     * the node_data by the node_id, null if none.
     * @param key - the node_id
     * @return node_data
     */
    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * @param src
     * @param dest
     * @return edge_data
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(!nodes.containsKey(src)) return  null;
        return outEdgs.get(src).get(dest);
    }

    /**
     * adds a new node to the graph with the given node_data.
     * 	 * Note: this method should run in O(1) time.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!nodes.containsKey(n.getKey())) {
            nodes.put(n.getKey(), n);
            outEdgs.put(n.getKey(),new HashMap<Integer, edge_data>());
            inEdgs.put(n.getKey(), new HashMap<Integer, edge_data>());
            mc++;
        }

    }
   /**
     * checks if the both nodes are in graph (otherwise one of them will be null),
     * checks that the nodes are different and checks that the node are not connected yet if node connected do update
     * if all checks are passed then add src to dest (directed graph)
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (!nodes.containsKey(src) || !nodes.containsKey(dest) || w < 0 || src == dest) return;
        EdgeData e = new EdgeData(src,dest,w);
        if (getEdge(src,dest) == null){
            outEdgs.get(src).put(dest,e);
            inEdgs.get(dest).put(src,e);
            edgeCount++;
            mc++;
        }
        else if(outEdgs.get(src).get(dest).getWeight() != w){
            outEdgs.get(src).put(dest,e);
            inEdgs.get(dest).put(src,e);
            mc++;
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        return outEdgs.get(node_id).values();
    }
    /**
     * get the given node by key
     * loop over all neighbors of given node and remove
     * finally, remove given node from the graph
     * @param key
     * @return node_data
     */
    @Override
    public node_data removeNode(int key) {
        if(nodes.containsKey(key)) {
            int sizeNi = outEdgs.get(key).size() + inEdgs.get(key).size();
            Set<Integer> keys = inEdgs.get(key).keySet();
            for (Integer i : keys) {
                outEdgs.get(i).remove(key);
            }
            keys = outEdgs.get(key).keySet();
            for (Integer i : keys) {
                inEdgs.get(i).remove(key);
            }
            outEdgs.remove(key);
            inEdgs.remove(key);
            mc++;
            edgeCount -= sizeNi;
        }
        return nodes.remove(key);
    }

    /**
     * Deletes the edge from the graph.
     * @param src
     * @param dest
     * @return edge_data
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(getEdge(src,dest) == null) return null;
        edgeCount--;
        mc++;
        inEdgs.get(dest).remove(src);
        return outEdgs.get(src).remove(dest);
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * @return int
     */
    @Override
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * @return int
     */
    @Override
    public int edgeSize() {
        return edgeCount;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return int
     */
    @Override
    public int getMC() {
        return mc;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        return edgeCount == that.edgeCount &&
                mc == that.mc &&
                nodes.equals(that.nodes) &&
                outEdgs.equals(that.outEdgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, outEdgs, edgeCount, mc);
    }
}
