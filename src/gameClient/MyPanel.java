package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;


public class MyPanel extends JPanel {
	private Arena arena;
	private gameClient.util.Range2Range w2f;

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
			g.setColor(Color.BLACK);
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

	private void drawInfo(Graphics g,int x, int y, int agentNumber) {
		Graphics2D G2D = (Graphics2D) g;
		G2D.setColor(Color.BLACK);
		G2D.setFont(new Font(null, Font.BOLD, 17));
		G2D.drawString("ID: "+agentNumber,x,y);
	}

	private void drawInfoGame(Graphics g) {
		g.setColor(new Color(0x000000));
		g.setFont(new Font(null, Font.BOLD, 20));
		String moves = ""+arena.getMoves();
		String level = ""+arena.getGameLevel();
		String score = ""+arena.getScore();
		g.drawString("Game level: "+level,200,50);
		g.drawString("Score: "+score,200,70);
		g.drawString("Player moves: "+moves,200,90);

		Graphics2D G2D = (Graphics2D) g;
		List<String> str = arena.getInfo();
		G2D.setColor(Color.BLACK);
		G2D.setFont(new Font(null, Font.BOLD, 12));
		for(int i = 0; i < str.size(); i++) {
			G2D.drawString(str.get(i), 580, (i*15) +50);
		}
	}

	private void drawGraph(Graphics g) {
		directed_weighted_graph graph = arena.getGraph();
		for(node_data n: graph.getV()){
			g.setColor(Color.blue);
			drawNode(n,5,g);
			for(edge_data e: graph.getE(n.getKey())) {
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
				if(point != null) {
					geo_location w2fPoint = this.w2f.world2frame(point);
					if(pokemon.getValue() <=5)
						paintPokemon(g,(int)w2fPoint.x()-18,(int)w2fPoint.y()-20,3*r,3*r);
					if(pokemon.getValue() > 5 && pokemon.getValue() <= 10)
						paintBulbasaur(g,(int)w2fPoint.x()-18,(int)w2fPoint.y()-20,3*r,3*r);
					else if(pokemon.getValue() > 10)
						paintCharmander(g,(int)w2fPoint.x()-18,(int)w2fPoint.y()-20,3*r,3*r);
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
			int agentNumber = listAgents.get(i).getID();
			int r = 8;
			i++;
			if(c != null) {
				geo_location fp = this.w2f.world2frame(c);
				//g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);//boaz
				paintAgent(g,(int)fp.x()-r,(int)fp.y()-r,3*r,3*r);
				g.setFont(new Font(null,Font.BOLD,25));
				drawInfo(g,(int)fp.x()-r,(int)fp.y()-r,agentNumber);
			}
		}
	}

	private void drawNode(node_data n, int r, Graphics g) {
		Graphics2D G2D = (Graphics2D) g;
		geo_location pos = n.getLocation();
		geo_location fp = this.w2f.world2frame(pos);
		G2D.setFont(new Font("Comic Sans",Font.ITALIC,25));
		G2D.fillOval((int)fp.x()-r, (int)fp.y()-r, 3*r, 3*r);
		G2D.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
	}

	private void drawEdge(edge_data e, Graphics g) {
		Graphics2D G2D = (Graphics2D) g;
		directed_weighted_graph gameGraph = arena.getGraph();
		geo_location s = gameGraph.getNode(e.getSrc()).getLocation();
		geo_location d = gameGraph.getNode(e.getDest()).getLocation();
		geo_location s0 = this.w2f.world2frame(s);
		geo_location d0 = this.w2f.world2frame(d);
		G2D.setStroke(new BasicStroke(2));
		G2D.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
		//g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
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
		BufferedImage pikachu = null;
		try {
		pikachu = ImageIO.read(new File("./resources/pikachu.png"));
		} catch (IOException e) {
		e.printStackTrace();
		}
		g.drawImage(pikachu, x, y, width, height, this);
		}
        public void paintBulbasaur(Graphics g,int x,int y,int width,int height) {
		BufferedImage bulbasaur = null;
		try {
		bulbasaur = ImageIO.read(new File("./resources/bullbasaur.png"));
		} catch (IOException e) {
		e.printStackTrace();
		}
		g.drawImage(bulbasaur, x, y, width, height, this);
		}
            public void paintCharmander(Graphics g,int x,int y,int width,int height) {
		BufferedImage charmander = null;
		try {
		charmander = ImageIO.read(new File("./resources/charmander.png"));
		} catch (IOException e) {
		e.printStackTrace();
		}
		g.drawImage(charmander, x, y, width, height, this);
	}
}
