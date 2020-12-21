package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Ex2 implements Runnable {
    private static dw_graph_algorithms algoGraph;
    private static MyFrame window;
    private static Arena arena;
    protected static Integer id;
    protected static Integer level_Number;


    public static void main(String[] args) {
        Thread client = new Thread(new Ex2());
        //client.start(); // delete
        if (args.length != 0) {//cmd run
            id = Integer.parseInt(args[0]);
            level_Number = Integer.parseInt(args[1]);
            client.start();
        } else {//panel run
            new StartPanel(client);
    }

    }

    @Override
    public void run() {
        //int level_Number = 9;
        game_service game = Game_Server_Ex2.getServer(level_Number);
         //int id = 99999;
        game.login(id);
        algoGraph = new DW_Graph_Algo();
        String getGraphFromServer = game.getGraph();// get json string of graph
        ((DW_Graph_Algo) algoGraph).loadFromString(getGraphFromServer);
        directed_weighted_graph graph = algoGraph.getGraph();// make graph algo from server
        init(game);// init the game -> graph,pokemons,agent

        game.startGame();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(game.timeToEnd());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(game.timeToEnd());
        arena.setGameTime("" + minutes + ":" + seconds);

        int ind = 1;
        long dt = 100;

        while (game.isRunning()) {
            moveAgants(game, graph);
            try {
                if (ind % 1 == 0) {
                    window.repaint();
                    minutes = TimeUnit.MILLISECONDS.toMinutes(game.timeToEnd());
                    seconds = TimeUnit.MILLISECONDS.toSeconds(game.timeToEnd());
                    arena.setGameTime("" + minutes + ":" + seconds);
                    String info = game.toString();
                    arena.json2Info(info);
                    window.repaint();
                }
                Thread.sleep(dt);
                ind++;
                window.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        window.setVisible(false);
        JOptionPane.showMessageDialog(null, "You made: " + arena.getMoves() + " moves" + " ,Your grade: " + arena.getScore());

        System.out.println(res);
        System.exit(0);
    }


    private static void moveAgants(game_service game, directed_weighted_graph graph) {
        String log = game.move();
        List<CL_Agent> agents = Arena.getAgents(log, graph);
        arena.setAgents(agents);
        String getPokemonsFromServer = game.getPokemons();// get json string of pokemons
        List<CL_Pokemon> listOfPokemons = Arena.json2Pokemons(getPokemonsFromServer);
        arena.setPokemons(listOfPokemons);
        DWGraph_DS g = new DWGraph_DS();
        List<CL_Pokemon> list = new ArrayList<>();
        for (int i = 0; i < listOfPokemons.size(); i++) {
            Arena.updateEdge(listOfPokemons.get(i), graph);
            list.add(listOfPokemons.get(i));
        }

        for (int i = 0; i < agents.size(); i++) {
            CL_Agent agent = agents.get(i);
            //int id = agent.getID();
           // int src = agent.getSrcNode();
           // double v = agent.getValue();
            int dest = nextNode(algoGraph, list, agent);
            game.chooseNextEdge(agent.getID(), dest);
            list.remove(agent.getCurr_fruit());
            //System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
        }
    }

    private static int nextNode(dw_graph_algorithms algoGraph, List<CL_Pokemon> listOfPokemons, CL_Agent agent) {
        double min = Double.POSITIVE_INFINITY;
        edge_data min_node = null;
        CL_Pokemon nextPok = new CL_Pokemon();
        for (CL_Pokemon p : listOfPokemons) {
            double d = algoGraph.shortestPathDist(agent.getSrcNode(), p.getEdge().getSrc());
            if (d < min) {
                min = d;
                min_node = p.getEdge();
                nextPok = p;
            }
        }
        agent.setCurr_fruit(nextPok);
        int dest = -1;
        if (min == 0) dest = algoGraph.shortestPath(agent.getSrcNode(), min_node.getDest()).get(1).getKey();
        else dest = algoGraph.shortestPath(agent.getSrcNode(), min_node.getSrc()).get(1).getKey();

        return dest;
    }

    private void init(game_service game) {
        directed_weighted_graph graph = algoGraph.getGraph();// make graph algo from server
        arena = new Arena();
        arena.setGraph(graph);//set graph algo to arena
        String getPokemonsFromServer = game.getPokemons();// get json string of pokemons
        arena.setPokemons(Arena.json2Pokemons(getPokemonsFromServer));
        window = new MyFrame(); // gui
        window.update(arena); // gui
        window.setVisible(true); // gui
        String info = game.toString();// info of the game -> pokemons,moves,agens...

        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gameInfo = line.getJSONObject("GameServer");
           // System.out.println(info);
           // System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> listOfPokemons = Arena.json2Pokemons(game.getPokemons());// get list of pokemons
            for (int i = 0; i < listOfPokemons.size(); i++) {
                Arena.updateEdge(listOfPokemons.get(i), graph);
            }
            int agentsNumber = gameInfo.getInt("agents");// get agens number from server
            List<Integer> nodes = findBestMatch(agentsNumber, listOfPokemons, algoGraph);
            for (int i = 0; i < agentsNumber; i++) {
                game.addAgent(nodes.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> findBestMatch(int k, ArrayList<CL_Pokemon> listOfPokemons, dw_graph_algorithms g_algo) {
        Collections.sort(listOfPokemons, new Comparator<CL_Pokemon>() {
            @Override
            public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                if (o1.getValue() < o2.getValue()) return 1;
                if (o1.getValue() > o2.getValue()) return -1;
                return 0;
            }
        });
        HashMap<Integer, Double> dist = new HashMap<Integer, Double>();
        List<Integer> nodes = new LinkedList<Integer>();
        for (CL_Pokemon p : listOfPokemons) {
            if (nodes.size() < k) {
                edge_data e = p.getEdge();
                if (!nodes.contains(e.getSrc())) nodes.add(e.getSrc());
                for (node_data node : g_algo.getGraph().getV()) {
                    double d = g_algo.shortestPathDist(e.getSrc(), node.getKey());
                    if (!dist.containsKey(node.getKey()) || dist.get(node.getKey()) > d) {
                        dist.put(node.getKey(), d);
                    }
                }
            }
        }
        return nodes;
    }
}
