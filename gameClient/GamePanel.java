package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;


public class GamePanel extends JPanel {
	private Arena arena;
	private gameClient.util.Range2Range w2f;
	LinkedList<Point3D> points = new LinkedList<Point3D>();

	public void update(Arena ar) {
		this.arena = ar;
		updateFrame();
	}

	public void paint(Graphics g) {
		int width = this.getWidth();
		int height = this.getHeight();

		g.clearRect(0, 0, width, height);
		updateFrame();
		paintBackGround(g);
		drawPokemons(g);
		drawGraph(g);
		drawAgants(g);
		drawTime(g);
		drawInfoGame(g);
		//drawInfo(g);
	}

	private void drawTime(Graphics g) {
		if (arena != null) {
			String str = arena.getGameTime();
			String time = "Time:";
			g.setColor(Color.red);
			g.setFont(new Font("Ariel", Font.BOLD, (this.getHeight() + this.getWidth()) / 60));
			// graphics.drawString(str , 100, 60 + 2 * 20);
			g.drawString(time +str, 20, 50);
			//  graphics.setFont(new Font("Ariel", Font.BOLD, (this.getHeight() + this.getWidth()) / 90));
		}
	}

	private void updateFrame() {
		Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-10,150);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = arena.getGraph();
		w2f = Arena.w2f(g,frame);
	}

	public void paintBackGround(Graphics g) {
		BufferedImage background = null;
		try {
			background = ImageIO.read(new File("./resources/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	private void drawInfo(Graphics g,int x, int y, int width, int height) {
		List<String> str = arena.getInfo();
		//String dt = "none";
		for(int i = 0; i < str.size(); i++) {
			g.drawString(str.get(i),x,y);//60+i*10
		}
	}

	private void drawInfoGame(Graphics g) {
		g.setColor(new Color(0x000000));
		g.setFont(new Font(null, Font.BOLD, 30));
		String moves = ""+arena.getMoves();
		String level = ""+arena.getGameLevel();
		String score = ""+arena.getScore();
		g.drawString("Game level:"+level,580,60);
		g.drawString("Score:"+score,580,90);
		g.drawString("Player moves:"+moves,580,120);

		List<String> str = arena.getInfo();
		g.setColor(new Color(0x0019FF));
		g.setFont(new Font(null, Font.BOLD, 20));
		for(int i = 0; i < str.size(); i++) {
				g.drawString(str.get(i), 180, 50);
		}
	}

	private void drawGraph(Graphics g) {
		directed_weighted_graph graph = arena.getGraph();
		for(node_data n: graph.getV()){
			g.setColor(Color.blue);
			drawNode(n,5,g);
			for(edge_data e: graph.getE(n.getKey())) {
				//g.setFont(new Font(null,Font.BOLD,500));
				g.setColor(Color.BLACK);
				drawEdge(e, g);
			}
		}
	}

	private void drawPokemons(Graphics g) {
		List<CL_Pokemon> listPokemos = arena.getPokemons();
		if(listPokemos != null) {
			for (CL_Pokemon pokemon: listPokemos){
				Point3D point = pokemon.getLocation();
				int r =10;
				g.setColor(Color.green);
				if(pokemon.getType() < 0) {
					g.setColor(Color.orange);
				}
				if(point != null) {
					geo_location w2fPoint = this.w2f.world2frame(point);
					paintPokemon(g,(int)w2fPoint.x()-40,(int)w2fPoint.y()-40,7*r,7*r);
					//g.fillOval((int)w2fPoint.x()-r, (int)w2fPoint.y()-r, 2*r, 2*r);//boaz
					//g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
				}
			}
		}
	}

	private void drawAgants(Graphics g) {
		List<CL_Agent> listAgents = arena.getAgents();
		//	Iterator<OOP_Point3D> itr = rs.iterator();
		g.setColor(Color.black);
		int i=0;
		while(listAgents != null && i < listAgents.size()) {
			geo_location c = listAgents.get(i).getLocation();
			int r = 8;
			i++;
			if(c != null) {
				geo_location fp = this.w2f.world2frame(c);
				//g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);//boaz
				paintAgent(g,(int)fp.x()-r,(int)fp.y()-r,4*r,4*r);
				g.setFont(new Font(null,Font.BOLD,25));
				drawInfo(g,(int)fp.x()-r,(int)fp.y()-r,3*r,4*r);
			}
		}
	}

	private void drawNode(node_data n, int r, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location fp = this.w2f.world2frame(pos);
		g.setFont(new Font("Comic Sans",Font.ITALIC,25));
		g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
		g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
	}

	private void drawEdge(edge_data e, Graphics g) {
		directed_weighted_graph gameGraph = arena.getGraph();
		geo_location s = gameGraph.getNode(e.getSrc()).getLocation();
		geo_location d = gameGraph.getNode(e.getDest()).getLocation();
		geo_location s0 = this.w2f.world2frame(s);
		geo_location d0 = this.w2f.world2frame(d);
		//g.setFont(new Font(null,Font.BOLD,25));
		g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
		//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
	}

	public void paintAgent(Graphics g,int x,int y,int width,int height) {
		BufferedImage agent = null;
		try {
			agent = ImageIO.read(new File("./resources/pokeboll_v2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(agent, x, y, width, height, this);
	}

	public void paintPokemon(Graphics g,int x,int y,int width,int height) {
		BufferedImage agent = null;
		try {
			agent = ImageIO.read(new File("./resources/pikachu.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(agent, x, y, width, height, this);
	}
}