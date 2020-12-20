package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Agent {
	public static final double EPS = 0.0001;
	private static int count = 0;
	private static int seed = 3331;
	private int id;//id agent
	//	private long _key;
	private geo_location pos;//location agent
	private double speed;//speed agent
	private edge_data curr_edge;
	private node_data curr_node;
	private directed_weighted_graph gameGraph;
	private CL_Pokemon curr_fruit;
	private long _sg_dt;
	private double value;


	public CL_Agent(directed_weighted_graph graph, int startNode) {
		this.gameGraph = graph;
		setMoney(0);
		this.curr_node = gameGraph.getNode(startNode);
		pos = curr_node.getLocation();
		id = -1;
		setSpeed(0);
	}
	public void update(String json) {
		JSONObject line;
		try {
			// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
			line = new JSONObject(json);

			JSONObject agent = line.getJSONObject("Agent");
			int id = agent.getInt("id");

			if(id==this.getID() || this.getID() == -1) {
				if(this.getID() == -1) {
					this.id = id;}

				double speed = agent.getDouble("speed");
				String pos = agent.getString("pos");
				Point3D point = new Point3D(pos);
				int src = agent.getInt("src");
				int dest = agent.getInt("dest");
				double value = agent.getDouble("value");
				this.pos = point;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int getSrcNode() {
		return this.curr_node.getKey();
	}

	public String toJSON() {
		int d = this.getNextNode();
		String ans = "{\"Agent\":{"
				+ "\"id\":"+this.id +","
				+ "\"value\":"+this.value +","
				+ "\"src\":"+this.curr_node.getKey()+","
				+ "\"dest\":"+d+","
				+ "\"speed\":"+this.getSpeed()+","
				+ "\"pos\":\""+ pos.toString()+"\""
				+ "}"
				+ "}";
		return ans;
		/*
		Gson gson = new Gson();
		JsonObject date = new JsonObject();
		date.add("id", gson.toJsonTree(this.getID()));
		date.add("value", gson.toJsonTree(this.getValue()));
		date.add("src", gson.toJsonTree(this.getSrcNode()));
		date.add("dest", gson.toJsonTree(this.getNext()));
		date.add("speed", gson.toJsonTree(this.getSpeed()));
		date.add("pos", gson.toJsonTree(this.getLocation().toString()));
		JsonObject agent = new JsonObject();
		agent.add("Agent", date);
		return agent.toString();

		 */
	}

	private void setMoney(double v) {
		value = v;
	}

	public boolean setNextNode(int dest) {
		boolean ans = false;
		int src = this.curr_node.getKey();
		this.curr_edge = gameGraph.getEdge(src, dest);
		if(curr_edge !=null) {
			ans=true;
		}
		else {
			curr_edge = null;}
		return ans;
	}

	public void setCurrNode(int src) {
		this.curr_node = gameGraph.getNode(src);
	}

	public boolean isMoving() {
		return this.curr_edge !=null;
	}

	public String toString() {
		return toJSON();
	}

	public String toString1() {
		String ans=""+this.getID()+","+ pos +", "+isMoving()+","+this.getValue();
		return ans;
	}
	public int getID() {
		return this.id;
	}

	public geo_location getLocation() {
		return this.pos;
	}

	public double getValue() {
		return this.value;
	}

	public int getNextNode() {
		int ans = -2;
		if(this.curr_edge ==null) {
			ans = -1;}
		else {
			ans = this.curr_edge.getDest();
		}
		return ans;
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double v) {
		this.speed = v;
	}
	public CL_Pokemon getCurr_fruit() {
		return curr_fruit;
	}
	public void setCurr_fruit(CL_Pokemon curr_fruit) {
		this.curr_fruit = curr_fruit;
	}
	public void set_SDT(long ddtt) {
		long ddt = ddtt;
		if(this.curr_edge !=null) {
			double w = getCurr_edge().getWeight();
			geo_location dest = gameGraph.getNode(getCurr_edge().getDest()).getLocation();
			geo_location src = gameGraph.getNode(getCurr_edge().getSrc()).getLocation();
			double de = src.distance(dest);
			double dist = pos.distance(dest);
			if(this.getCurr_fruit().getEdge()==this.getCurr_edge()) {
				dist = curr_fruit.getLocation().distance(this.pos);
			}
			double norm = dist/de;
			double dt = w*norm / this.getSpeed();
			ddt = (long)(1000.0*dt);
		}
		this.set_sg_dt(ddt);
	}

	public edge_data getCurr_edge() {
		return this.curr_edge;
	}
	public long get_sg_dt() {
		return _sg_dt;
	}
	public void set_sg_dt(long _sg_dt) {
		this._sg_dt = _sg_dt;
	}
}
