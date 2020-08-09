package GraphProject;

import GraphProject.MutatableGraph;
import GraphProject.MutatableGraphFactory;
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
    void getStrogatzGraphTest() {
        MutatableGraph strogatz = MutatableGraphFactory.getStrogatzGraph(100,6,0.5);
        assertEquals(100,strogatz.getJgraphContained().vertexSet().size(),"Incorrect strogatz graph vert size");
        assertEquals(300,strogatz.getJgraphContained().edgeSet().size(),"Incorrect strogatz graph edge size");
    }

    @Test
    void getErdoshGraphTest() {
        MutatableGraph erdposh = MutatableGraphFactory.getErdoshGraph(10,40);
        assertEquals(10,erdposh.getJgraphContained().vertexSet().size(),"Incorrect erdosh graph vert size");
        assertEquals(40,erdposh.getJgraphContained().edgeSet().size(),"Incorrect erdosh graph edge size");
    }

    @Test
    void getWheelGraphTest() {
        MutatableGraph wheel = MutatableGraphFactory.getWheelGraph(11);
        assertEquals(11,wheel.getJgraphContained().vertexSet().size(),"Incorrect wheel graph vert size");
        assertEquals(20,wheel.getJgraphContained().edgeSet().size(),"Incorrect wheel graph edge size");
    }
}