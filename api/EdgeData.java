package ex1.api;

public class EdgeData implements  edge_data {
    private int src,dest,tag;
    private double weight;
    private String info;

    public EdgeData(int src,int dest,Double weight){
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        tag = 0;
        info = "";
    }
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
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
