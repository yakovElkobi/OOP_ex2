package api;

import java.util.Objects;

/**
 * NodeData implements the node_data interface and represents node in graph
 * The key is unique for each node and always increase
 * all methods in this class is O(1) complexity
 */

public class NodeData implements node_data {
    private geo_location location;
    private int key,tag;
    private double weight;
    private String info;
    private static int u_key = 0;

    /**
     * default constructor the represents a unique key
     */
    public NodeData(){
        this.key = ++u_key;
        info = "";
        tag = -1;
        weight = -1;
        location = new GeoLocation(0,0,0);
    }

    /**
     * constructor for key
     * @param key
     */
    public NodeData(int key){
        this.key = key;
        info = "";
        tag = -1;
        weight = -1;
        location = new GeoLocation(0,0,0);
    }

    /**
     * constructor that get int key ,tag, double weight, geo_location location, String info
     * @param key
     * @param tag
     * @param weight
     * @param location
     * @param info
     */
    public NodeData(int key,int tag,double weight,geo_location location,String info){
        this.key = key;
        this.tag = tag;
        this.weight = weight;
        this.info = info;
        this.location = location;
    }

    /**
     *  constructor that get NodeData and do deep copy.
     * @param n
     */
    public NodeData(NodeData n){
        key = n.getKey();
        info = n.getInfo();
        tag = n.getTag();
        weight = n.getWeight();
        location = new GeoLocation(n.location);
    }

    /**
     * Returns the key (id) associated with this node.
     * @return int
     */
    @Override
    public int getKey() {
        return key;
    }

    /**
     * Returns the location of this node, if
     * none return null
     * @return geo_location
     */
    @Override
    public geo_location getLocation() {
        return location;
    }

    /**
     * Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
       location = p;
    }

    /**
     * Returns the weight associated with this node.
     * @return int
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     * @return String
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * 	 * which can be used be algorithms
     * @return int
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * This method allows setting the tag value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }
}
