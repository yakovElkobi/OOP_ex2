package api;

/**
 * GeoLocation implements the geo_location interface and represents represents a geo location <x,y,z>, aka Point3D
 */
public class GeoLocation implements geo_location {
    private double x,y,z;

    /**
     * constructor that get geo_location.
     * @param g
     */
      public GeoLocation(geo_location g){
       this.x = g.x();
       this.y = g.y();
       this.z = g.z();
    }

    /**
     * constructor that get double parseDouble, parseDouble1, parseDouble2.
     * This method for GraphJsonDeserializer.
     * @param parseDouble
     * @param parseDouble1
     * @param parseDouble2
     */
    public GeoLocation(double parseDouble, double parseDouble1, double parseDouble2) {
          this.x = parseDouble;
          this.y = parseDouble1;
          this.z = parseDouble2;
    }

    /**
     * This method returns the x value of the point
     * @return double
     */
    @Override
    public double x() {
        return x;
    }

    /**
     * This method returns the y value of the point
     * @return
     */
    @Override
    public double y() {
        return y;
    }

    /**
     * This method returns the z value of the point
     * @return
     */
    @Override
    public double z() {
        return z;
    }

    /**
     * This method do distance between location that get geo_location
     * @param g
     * @return double
     */
    @Override
    public double distance(geo_location g) {
        double dx = x - g.x();
        double dy = y - g.y();
        double dz = z - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
    }
}
