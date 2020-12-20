package api;

/**
 * EdgeData implements the edge_data interface represents the set of operations applicable on a
 *  * directional edge(src,dest) in a (directional) weighted graph.
 */
public class EdgeData implements edge_data {
    private int src,dest,tag;
    private double weight;
    private String info;

    /**
     * constructor that get int src, dest, Double weight
     * @param src
     * @param dest
     * @param weight
     */
    public EdgeData(int src,int dest,Double weight){
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        tag = 0;
        info = "";
    }

    /**
     * The id of the source node of this edge.
     * @return int
     */
    public int getSrc() {
        return src;
    }

    /**
     * The id of the destination node of this edge
     * @return int
     */
    @Override
    public int getDest() {
        return dest;
    }

    /**
     * the weight of this edge (positive value).
     * @return double
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     * @return String
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return int
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }
}
