package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Pokemon {
	private edge_data edge;
	private double value;
	private int type;
	private Point3D pos;
	private double weight;
	private int min_ro;

	public CL_Pokemon(){
		edge = null;
		type = 0;
		value = 0;
		pos = null;
		weight = -1;
		min_ro = -1;
	}
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		type = t;
		value = v;
		setEdge(e);
		pos = p;
		weight = -1;
		min_ro = -1;
	}

	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}

	public String toString() {
		return "F:{v="+ value +", t="+ type +"}";
	}

	public edge_data getEdge() {
		return edge;
	}

	public void setEdge(edge_data edge) {
		this.edge = edge;
	}

	public Point3D getLocation() {
		return pos;
	}

	public int getType() {
		return type;
	}

	public double getValue() {
		return value;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double mid_dist) {
		this.weight = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}
}
