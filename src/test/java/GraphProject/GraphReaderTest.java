package GraphProject;

import org.jgrapht.graph.DefaultUndirectedGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphReaderTest {
    GraphReader reader = new GraphReader();

    @Test
    void readGraph(){
        DefaultUndirectedGraph graph = reader.getGraph("resources/Graphs/yeast/syeast0.el"," ");
        assertEquals(8323,graph.edgeSet().size(),"edge amount incorrect");
        assertEquals(1004,graph.vertexSet().size(),"vertex amount incorrect");
    }
}