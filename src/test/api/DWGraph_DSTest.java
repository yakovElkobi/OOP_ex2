package test.api;

import static org.junit.jupiter.api.Assertions.*;

import api.DWGraph_DS;
import api.NodeData;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    public static DWGraph_DS build(int size) {
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 1; i <= size; i++) {
            g.addNode(new NodeData(i));
        }
        return g;
    }

    @org.junit.jupiter.api.Test
    void getNode() {
        DWGraph_DS g = new DWGraph_DS();
        g = build(5);
        assertTrue(g.getNode(1).getKey() == 1);
        assertTrue(g.getNode(3).getKey() == 3);
        assertTrue(g.getNode(6) == null);// not exit
    }

    @org.junit.jupiter.api.Test
    void getEdge() {
        DWGraph_DS g = new DWGraph_DS();
        g = build(10);
        g.connect(2, 3, 3.2);
        g.connect(1, 2, 5);
        g.connect(5, 3, -1);
        assertTrue(g.getEdge(2, 3).getWeight() == 3.2);
        assertNull(g.getEdge(5, 3));
        g.connect(2, 3, 4);
        assertTrue(g.getEdge(2, 3).getWeight() == 4);
        assertNull(g.getEdge(3, 2));

    }

    @org.junit.jupiter.api.Test
    void removeNode() {
        DWGraph_DS g = new DWGraph_DS();
        g = build(5);
        g.connect(1, 2, 4);
        g.connect(1, 3, 5);
        g.connect(1, 4, 5);
        g.connect(4, 1, 7);
        g.removeNode(1);
        assertNull(g.getEdge(4, 1));
        assertNull(g.getNode(1));
        g.connect(4, 3, 4);
        assertTrue(g.getEdge(4, 3).getWeight() == 4);
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
        DWGraph_DS g = new DWGraph_DS();
        g = build(8);
        g.connect(2, 3, 4);
        g.connect(3, 2, 3);
        g.removeEdge(2, 3);
        assertNull(g.getEdge(2, 3));
        g.removeEdge(1, 5);
    }

    @org.junit.jupiter.api.Test
    void nodeSize() {
        DWGraph_DS g = new DWGraph_DS();
        g = build(10);
        assertTrue(g.nodeSize() == 10);
        g.connect(2, 3, 8);
        g.connect(3, 3, 4);
        g.connect(2, 3, 8);
        g.connect(1, 2, 2);
        g.removeNode(3);
        assertTrue(g.nodeSize() == 9);

    }

    @org.junit.jupiter.api.Test
    void edgeSize() {
        DWGraph_DS g = new DWGraph_DS();
        g = build(10);
        g.connect(1, 2, 4);
        g.connect(1, 3, 5);
        g.connect(1, 4, 5);
        g.connect(4, 1, 7);
        g.removeNode(1);
        assertTrue(g.edgeSize() == 0);
        g.connect(4, 2, 4);
        g.connect(5, 5, 3);
        g.connect(6, 7, -1);
        g.connect(6, 7, 3);
        assertTrue(g.edgeSize() == 2);

    }

    @org.junit.jupiter.api.Test
    void getMC() {
        DWGraph_DS g = new DWGraph_DS();
        g = build(10);
        g.connect(1, 2, 4);
        g.connect(1, 3, 5);
        g.connect(1, 4, 5);
        g.connect(4, 1, 7);
        g.removeNode(1);
        assertTrue(g.getMC() == 15);
        g.connect(5, 6, 9);
        g.connect(5, 6, 7);
        g.connect(7, 8, -1);
        assertTrue(g.getMC() == 17);
        g.removeEdge(3, 2);
        g.removeEdge(5, 6);
        assertTrue(g.getMC() == 18);
    }
}