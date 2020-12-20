package test.api;

import static org.junit.jupiter.api.Assertions.*;

import api.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DW_Graph_AlgoTest {

    public static directed_weighted_graph little_graph(){
        directed_weighted_graph g = new DWGraph_DS();
        int[] nodes = new int[]{0,1,2,3,4};
        for(int i = 0; i < nodes.length; i++){
            g.addNode(new NodeData(nodes[i]));
        }
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(2,4,10);

        return  g;
    }

    public static directed_weighted_graph small_graph() {
        directed_weighted_graph g0 = new DWGraph_DS();
        for(int i = 0 ; i < 11 ; i++) {
            node_data n = new NodeData(i);
            //     geo_location f = new GeoLocation(i,i+1,i+2);
            //    n.setLocation(f);
            g0.addNode(n);
        }
        g0.connect(0,1,1);
        g0.connect(0,2,6);
        g0.connect(0,3,3);
        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(8,10,10);
        g0.connect(5,3,5);
        g0.connect(3,2,2);


        return g0;
    }

    @Test
    void isConnected() {
        directed_weighted_graph g0 = new DWGraph_DS();
        g0.addNode(new NodeData(0));
        //    System.out.println(g0);
        dw_graph_algorithms ga0 = new DW_Graph_Algo();
        ga0.init(g0);
        assertTrue(ga0.isConnected());

        directed_weighted_graph g1 = new DWGraph_DS();
        g1.addNode(new NodeData(0));
        g1.addNode(new NodeData(1));
        //    System.out.println(g1);
        dw_graph_algorithms ga1 = new DW_Graph_Algo();
        ga1.init(g1);
        assertFalse(ga1.isConnected());

        directed_weighted_graph g2 = new DWGraph_DS();
        g2.addNode(new NodeData(0));
        g2.addNode(new NodeData(1));
        g2.connect(0,1,5);
        //System.out.println(g2);
        dw_graph_algorithms ga2 = new DW_Graph_Algo();
        ga2.init(g2);
        assertFalse(ga2.isConnected());

        directed_weighted_graph g3 = new DWGraph_DS();
        g3.addNode(new NodeData(0));
        g3.addNode(new NodeData(1));
        g3.addNode(new NodeData(2));
        g3.addNode(new NodeData(3));
        g3.connect(0,1,5);
        g3.connect(2,3,7);
        //System.out.println(g3);
        dw_graph_algorithms ga3 = new DW_Graph_Algo();
        ga3.init(g3);
        assertFalse(ga3.isConnected());

        directed_weighted_graph g4 = new DWGraph_DS();
        g4.addNode(new NodeData(0));
        g4.addNode(new NodeData(1));
        g4.addNode(new NodeData(2));
        g4.connect(0,1,5);
        g4.connect(1,2,7);
        g4.connect(2,0,9);
        //System.out.println(g4);
        dw_graph_algorithms ga4 = new DW_Graph_Algo();
        ga4.init(g4);
        assertTrue(ga4.isConnected());

        directed_weighted_graph g5 = small_graph();
        dw_graph_algorithms ga5 = new DW_Graph_Algo();
        ga5.init(g5);
        //System.out.println(ga5);

        assertFalse(ga5.isConnected());

        directed_weighted_graph g6 = small_graph();
        g6.removeEdge(3,9);
        dw_graph_algorithms ga6 = new DW_Graph_Algo();
        ga6.init(g6);
        //System.out.println(ga6);

        assertFalse(ga6.isConnected());
    }

    @Test
    void shortestPathDist() {
        directed_weighted_graph g = little_graph();
        dw_graph_algorithms ga0 = new DW_Graph_Algo();
        ga0.init(g);
        //System.out.println(ga0);

        double w = ga0.shortestPathDist(0,12);
        assertEquals(w, -1,"12 not in graph");
        double w1 = ga0.shortestPathDist(14,12);
        assertEquals(w1, -1,"14 and 12 not in graph");

        double w2 = ga0.shortestPathDist(0,1);
        assertEquals(w2, 1);
        double w3 = ga0.shortestPathDist(0,2);
        assertEquals(w3, 2);

        directed_weighted_graph g0 = small_graph();
        dw_graph_algorithms ag0 = new DW_Graph_Algo();
        ag0.init(g0);
        //System.out.println(ag0);

        double w4 = ag0.shortestPathDist(0,2);
        assertEquals(w4, 5);

        directed_weighted_graph g1 = small_graph();
        dw_graph_algorithms ag1 = new DW_Graph_Algo();
        ag1.init(g1);

        double w5 = ag0.shortestPathDist(0,10);
        assertEquals(w5, 5.1);
    }

    @Test
    void shortestPath() {
        directed_weighted_graph g = little_graph();
        dw_graph_algorithms ga0 = new DW_Graph_Algo();
        ga0.init(g);
        //System.out.println(ga0);

        List<node_data> spt1 = ga0.shortestPath(0,10);
        assertNull(spt1);

        List<node_data> spt2 = ga0.shortestPath(0,1);
        int n = 0;
        for(node_data node: spt2) {
            assertEquals(node.getKey(), n);
            n++;
        }

        directed_weighted_graph g0 = small_graph();
        dw_graph_algorithms ag0 = new DW_Graph_Algo();
        ag0.init(g0);

        List<node_data> sp = ag0.shortestPath(0,10);
        double[] checkWeight = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_data node: sp) {
            assertEquals(node.getWeight(), checkWeight[i]);
            assertEquals(node.getKey(), checkKey[i]);
/*            System.out.print(n.getKey() +"-->");
            System.out.print(n.getWeight() +", ");
            System.out.println();*/
            i++;
        }

        directed_weighted_graph g1 = small_graph();
        dw_graph_algorithms ag1 = new DW_Graph_Algo();
        ag1.init(g1);

        List<node_data> sp1 = ag1.shortestPath(0,2);
        double[] checkWeight1 = {0.0, 3.0, 5.0};
        int[] checkKey1 = {0, 3, 2};
        int j = 0;
        for(node_data node: sp1) {
            assertEquals(node.getWeight(), checkWeight1[j]);
            assertEquals(node.getKey(), checkKey1[j]);
/*            System.out.print(n.getKey() +"-->");
            System.out.print(n.getWeight() +", ");
            System.out.println();*/
            j++;
        }
    }


    @Test
    void save_load(){
        directed_weighted_graph g = small_graph();
        dw_graph_algorithms ga0 = new DW_Graph_Algo();
        ga0.init(g);
        //System.out.println(ga0);

        assertTrue(ga0.save("ga0.json"));
        DW_Graph_Algo a = new DW_Graph_Algo();
        a.load("ga0.json");

    }
}