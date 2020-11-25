package ex1.api;

public class GeoLocation implements geo_location {
    private double x,y,z;

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(geo_location g) {
        double dx = x - g.x();
        double dy = y - g.y();
        double dz = z - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
    }
}
