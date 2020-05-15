import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutatableGraphFactoryTest {

    @Test
    void getBarabasiGraphTest() {
        MutatableGraph barabasi = MutatableGraphFactory.getBarabasiGraph(2,10);
        assertEquals(10,barabasi.getJgraphContained().vertexSet().size(),"Incorrect barabasi graph vert size");
        assertEquals(17,barabasi.getJgraphContained().edgeSet().size(),"Incorrect barabasi graph edge size");
    }

    @Test
    void getGeneralizedPetersenGraphTest() {
        MutatableGraph petersen = MutatableGraphFactory.getGeneralizedPetersenGraph(5,2 );
        assertEquals(10,petersen.getJgraphContained().vertexSet().size(),"Incorrect petersen graph vert size");
        assertEquals(15,petersen.getJgraphContained().edgeSet().size(),"Incorrect petersen graph edge size");

    }

    @Test
    void getHyperCubeGraphTest() {
        MutatableGraph hyperCube = MutatableGraphFactory.getHyperCubeGraph(6);
        assertEquals(64,hyperCube.getJgraphContained().vertexSet().size(),"Incorrect hypercube graph vert size");
        assertEquals(192,hyperCube.getJgraphContained().edgeSet().size(),"Incorrect hypercube graph edge size");
    }

    @Test
    void getKleinbergSmallWorldGraphTest() {
        MutatableGraph kleinberg = MutatableGraphFactory.getKleinbergSmallWorldGraph(10,10,10,10);
        assertEquals(100,kleinberg.getJgraphContained().vertexSet().size(),"Incorrect kleinberg graph vert size");
        assertEquals(4660,kleinberg.getJgraphContained().edgeSet().size(),"Incorrect kleinberg graph edge size");
    }

    @Test
    void getWindmillGraphTest() {
        MutatableGraph windmill = MutatableGraphFactory.getWindmillGraph(2,3);
        assertEquals(5,windmill.getJgraphContained().vertexSet().size(),"Incorrect kleinberg graph vert size");
        assertEquals(6,windmill.getJgraphContained().edgeSet().size(),"Incorrect kleinberg graph edge size");
    }

    @Test
    void getWheelGraphTest() {
        MutatableGraph wheel = MutatableGraphFactory.getWheelGraph(11);
        assertEquals(11,wheel.getJgraphContained().vertexSet().size(),"Incorrect kleinberg graph vert size");
        assertEquals(20,wheel.getJgraphContained().edgeSet().size(),"Incorrect kleinberg graph edge size");
    }
}