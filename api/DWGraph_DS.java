package ex1.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
/**
 * Graph_DS implements the directed_weighted_graph interface and represents the Graph structure
 * all nodes in the graph are stores in HashMap and the edges are stores in double HashMap
 * the edgeSize is counting within add\remove edges methods
 * The modCount is increase on each method that changes something in the graph
 */
public class DWGraph_DS implements directed_weighted_graph{
    private HashMap<Integer,node_data> nodes;
    private HashMap<Integer,HashMap<Integer,edge_data>> edgs;
    private int edgeCount,mc;

    public DWGraph_DS(){
        nodes = new HashMap<Integer, node_data>();
        edgs = new HashMap<Integer,HashMap<Integer, edge_data>>();
        edgeCount = 0;
        mc = 0;
    }
    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return edgs.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        if (!nodes.containsKey(n.getKey())) {
            nodes.put(n.getKey(), n);
            edgs.put(n.getKey(),new HashMap<Integer, edge_data>());
            mc++;
        }

    }
    /**
     * checks if the both nodes are in graph (otherwise one of them will be null),
     * checks that the nodes are different and checks that the node are not connected yet if node connected do update
     * if all checks are passed then add src to dest (directed graph)
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (!nodes.containsKey(src) || !nodes.containsKey(dest) || w < 0 || src == dest) return;
        EdgeData e = new EdgeData(src,dest,w);
        if (getEdge(src,dest) == null){
            edgs.get(src).put(dest,e);
            edgeCount++;
            mc++;
        }
        else if(edgs.get(src).get(dest).getWeight() != w){
            edgs.get(src).put(dest,e);
            mc++;
        }
    }

    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return edgs.get(node_id).values();
    }
    /**
     * get the given node by key
     * loop over all neighbors of given node and remove
     * finally, remove given node from the graph
     */
    @Override
    public node_data removeNode(int key) {
        if(nodes.containsKey(key)) {
            nodes.remove(key);
            Set<Integer> niKeys = edgs.get(key).keySet();
            edgs.remove(key);
            for (Integer i : niKeys) {
                edgs.get(i).remove(key);
                edgeCount--;
            }
            mc++;
        }
        return nodes.remove(key);
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if(getEdge(src,dest) == null) return null;
        edgeCount--;
        mc++;
        return edgs.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeCount;
    }

    @Override
    public int getMC() {
        return mc;
    }
}
