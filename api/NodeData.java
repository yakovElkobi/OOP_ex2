package ex1.api;
/**
 * NodeData implements the node_data interface and represents node in graph
 * The key is unique for each node and always increase
 * all methods in this class is O(1) complexity
 */

public class NodeData implements node_data{
    private geo_location location;
    private int key,tag;
    private double weight;
    private String info;
    private static int u_key = 0;

    public NodeData(){
        u_key = ++u_key;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        return location;
    }

    @Override
    public void setLocation(geo_location p) {
       location = p;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        weight = w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        tag = t;
    }
}
