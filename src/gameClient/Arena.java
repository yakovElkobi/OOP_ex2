package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class Arena {
	public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph gameGraph;
	private List<CL_Agent> agents;
	private List<CL_Pokemon> pokemons;
	private List<String> info;
	private String gameTime;
	private int moves;
	private int score;
	private int gameLevel;
	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);

	public Arena() {;
		info = new ArrayList<String>();
	}

	private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
		gameGraph = g;
		this.setAgents(r);
		this.setPokemons(p);
	}
	public String getGameTime() {
		return gameTime;
	}
	public void setGameTime(String gameTime) {
		this.gameTime = gameTime;
	}
	public void setPokemons(List<CL_Pokemon> f) {
		this.pokemons = f;
	}

	public void setAgents(List<CL_Agent> f) {
		this.agents = f;
	}

	public void setGraph(directed_weighted_graph g) {
		this.gameGraph =g;
	}

	private void init( ) {
		MIN=null; MAX=null;
		double x0=0,x1=0,y0=0,y1=0;
		Iterator<node_data> iter = gameGraph.getV().iterator();
		while(iter.hasNext()) {
			geo_location c = iter.next().getLocation();
			if(MIN==null) {
				x0 = c.x(); y0=c.y(); x1=x0;y1=y0;MIN = new Point3D(x0,y0);
			}
			if(c.x() < x0) {
				x0=c.x();
			}
			if(c.y() < y0) {
				y0=c.y();
			}
			if(c.x() > x1) {
				x1=c.x();
			}
			if(c.y() > y1) {
				y1=c.y();
			}
		}
		double dx = x1-x0, dy = y1-y0;
		MIN = new Point3D(x0-dx/10,y0-dy/10);
		MAX = new Point3D(x1+dx/10,y1+dy/10);

	}

	public List<CL_Agent> getAgents() {
		return agents;
	}
	public List<CL_Pokemon> getPokemons() {
		return pokemons;
	}

	public directed_weighted_graph getGraph() {
		return gameGraph;
	}

	public List<String> getInfo() {
		info = new LinkedList<>();
		for (CL_Agent curAgent : agents) {
			info.add("agent: " + curAgent.getID() + ",  " + "value: " + curAgent.getValue() + ",  "
					+ "dest: " + curAgent.getNextNode());
		}
		return info;
	}
	public List<String> getInfoAgent() {
		info = new LinkedList<>();
		for (CL_Agent curAgent : agents) {
			info.add("id: " + curAgent.getID());
		}
		return info;
	}
	public List<String> getInfoPokimon() {
		info = new LinkedList<>();
		for (CL_Pokemon p : pokemons) {
			info.add("value: " + p.getValue());
		}
		return info;
	}
	public void setInfo(List<String> info) {
		this.info = info;
	}
	public int getMoves() {
		return moves;
	}
	public void setMoves(int moves) {
		this.moves = moves;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getGameLevel() {
		return gameLevel;
	}
	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}

	////////////////////////////////////////////////////
	public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
		try {
			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				CL_Agent c = new CL_Agent(gg,0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public void json2Info(String jsonInfo) {
		try {
			JSONObject info = new JSONObject(jsonInfo);
			this.setScore(info.getJSONObject("GameServer").getInt("grade"));
			this.setMoves(info.getJSONObject("GameServer").getInt("moves"));
			this.setGameLevel(info.getJSONObject("GameServer").getInt("game_level"));

		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
	}

	public static ArrayList<CL_Pokemon> json2Pokemons(String jsonPokemons) {
		ArrayList<CL_Pokemon> ans = new  ArrayList<CL_Pokemon>();
		try {
			JSONObject pokemons = new JSONObject(jsonPokemons);
			JSONArray ags = pokemons.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				String p = pk.getString("pos");
				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}

	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		while(itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while(iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
				if(f) {
					fr.setEdge(e);
				}
			}
		}
	}

	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {
		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {
			ans = true;
		}
		return ans;
	}

	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}

	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {
			return false;
		}
		if(type>0 && src>dest) {
			return false;
		}
		return isOnEdge(p,src, dest, g);
	}

	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {
					x0=p.x();
				}
				if(p.x()>x1) {
					x1=p.x();
				}
				if(p.y()<y0) {
					y0=p.y();
				}
				if(p.y()>y1) {
					y1=p.y();
				}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}

	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}

}
