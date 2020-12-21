package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * DW_Graph_Algo implements dw_graph_algorithms represents a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 */
public class DW_Graph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph g;

    /**
     * default constructor that build graph
     */
    public DW_Graph_Algo(){
        g = new DWGraph_DS();
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return directed_weighted_graph
     */
    @Override
    public directed_weighted_graph getGraph() {
        return g;
    }

    /**
     * create new Graph instance
     * loop over the given graph nodes, create new copy of the node with the same info,tag (without the neighbors)
     * then loop over the graph nodes again and loop over each neighbors of each node in the graph and connect between
     */
    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS copy = new DWGraph_DS();
        for (node_data n: g.getV()) {
            NodeData n1 = new NodeData((NodeData)n);
            copy.addNode(n1);
        }
        for (node_data n: g.getV()){
            for(edge_data e: g.getE(n.getKey())){
                copy.connect(n.getKey(),e.getDest(),e.getWeight());
            }
        }
        return copy;
    }
    /**
     * using the BFS algorithm with the first node in graph, then checks if there is a node the BFS search does not visited
     * using reverseGraph that return the
     * using again the BFS algorithm with the same node in graph,then checks if there is a node the BFS search does not visited
     * (the tag is -1) and if its true return false (not connected), else return true (BFS visit all nodes)
     * @return boolean
     */
    @Override
    public boolean isConnected() {
        if(g == null || g.nodeSize() == 0) return true;
        int srcKey = g.getV().iterator().next().getKey();
        bfs(g, srcKey);
        for(node_data n : g.getV()){
            if(n.getTag() == -1) return false;
        }
        DWGraph_DS r = reverseGraph(g);
        bfs(r, srcKey);
        for(node_data n : g.getV()){
            if(n.getTag() == -1) return false;
        }
        return true;
    }
    /**
     * using shortestPath that return the list with the shortestPath
     * @param src - start node
     * @param dest - end (target) node
     * @return double - weight
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        List<node_data> p = shortestPath(src, dest);
        if(p == null) return -1;
        double sum = 0;
        for (int i = 1; i < p.size(); i++) {
            sum += g.getEdge(p.get(i-1).getKey(), p.get(i).getKey()).getWeight();
        }
        return sum;
    }
    /**
     * using the dijkstra algorithm from src that put the parent of each node in his weight and after the algorithm,
     * start from dest and insert the parent to the list until we get back to src
     * @param src - start node
     * @param dest - end (target) node
     * @return List<node_data>
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(g == null || g.getNode(src) == null || g.getNode(dest) == null) return null;
        HashMap<Integer, node_data> ans = dijkstra(src);
        if(src != dest && ans.get(dest) == null) return null;
        List<node_data> path = new LinkedList<node_data>();
        path.add(0, g.getNode(dest));
        int x = dest;
        while(x != src) {
            node_data p = ans.get(x);
            path.add(0, p);
            x = p.getKey();
        }
        return path;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * @param file - the file name (may include a relative path).
     * @return boolean
     */
    @Override
    public boolean save(String file) {
        try {
            Gson gson = new Gson();
            JsonObject obj = new JsonObject();
            JsonArray edges= new JsonArray();
            JsonArray nodes = new JsonArray();
            for (node_data n : g.getV()) {
                for (edge_data e : g.getE(n.getKey())) {
                    JsonObject edge = new JsonObject();
                    edge.add("src", gson.toJsonTree(e.getSrc()));
                    edge.add("w", gson.toJsonTree(e.getWeight()));
                    edge.add("dest", gson.toJsonTree(e.getDest()));
                    edges.add(edge);
                }
                JsonObject node = new JsonObject();
                geo_location location = n.getLocation();
                String l = location.x() + "," + location.y() + "," + location.z();
                node.add("pos", gson.toJsonTree(l));
                node.add("id", gson.toJsonTree(n.getKey()));
                nodes.add(node);
            }

            obj.add("Edges", edges);
            obj.add("Nodes", nodes);

            PrintWriter pw = new PrintWriter(new File(file));
            pw.println(obj.toString());
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return boolean
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class,new GraphJsonDeserializer());
            Gson gson = builder.create();
            FileReader reader = new FileReader(file);
            directed_weighted_graph ans = gson.fromJson(reader,directed_weighted_graph.class);
            init(ans);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
}

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * using GraphJsonDeserializer
     * @param js
     * @return boolean
     */
    public boolean loadFromString(String js) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class,new GraphJsonDeserializer());
            Gson gson = builder.create();
            StringReader reader = new StringReader(js);
            directed_weighted_graph ans = gson.fromJson(reader,directed_weighted_graph.class);
            init(ans);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private DWGraph_DS reverseGraph(directed_weighted_graph g) {
        DWGraph_DS r = new DWGraph_DS();
        for (node_data n : g.getV()){
            r.addNode(n);
        }
        for (node_data n : g.getV()){
            for (edge_data e : g.getE(n.getKey())){
                r.connect(e.getDest(),e.getSrc(),e.getWeight());
            }
        }
        return r;
    }

    private void bfs(directed_weighted_graph g, int src){
        ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<Integer>(g.nodeSize());
        for(node_data n : g.getV()) {
            n.setTag(-1);
        }
        q.add(src);
        g.getNode(src).setTag(0);
        while(!q.isEmpty()) {
            int v = q.poll();
            for(edge_data e : g.getE(v)) {
                int u = e.getDest();
                if(g.getNode(u).getTag() == -1) {
                    g.getNode(u).setTag(0);
                    q.add(u);
                }
            }
            g.getNode(v).setTag(1);
        }
    }
    private HashMap<Integer, node_data> dijkstra(int src){
        HashMap<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
        HashMap<Integer, node_data> vertex = new HashMap<Integer, node_data>();
        PriorityQueue<node_data> q = new PriorityQueue<node_data>(new Comparator<node_data>() {
            @Override
            public int compare(node_data n1, node_data n2) {
                if(n1.getWeight() < n2.getWeight()) return -1;
                else if(n1.getWeight() > n2.getWeight()) return 1;
                else return 0;
            }
        });
        for (node_data n: g.getV()) {
            n.setWeight(Double.POSITIVE_INFINITY);
            visited.put(n.getKey(), false);
            vertex.put(n.getKey(), null);
        }
        node_data n = g.getNode(src);
        q.add(n);
        n.setWeight(0);
        while(!q.isEmpty()) {
            node_data m = q.poll();
            for (edge_data e: g.getE(m.getKey())) {
                double w = m.getWeight() + e.getWeight();
                int u = e.getDest();
                if (!visited.get(e.getDest()) && g.getNode(u).getWeight() > w){
                    node_data d = g.getNode(u);
                    vertex.put(u, m);
                    d.setWeight(w);
                    q.remove(d);
                    q.add(d);
                }
            }
            visited.put(m.getKey(), true);
        }
        return vertex;
    }
}