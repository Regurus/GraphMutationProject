package GraphProject;

import GraphProject.IOUtils;
import GraphProject.MutatableGraph;
import GraphProject.MutatableGraphFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IOUtilsTest {
    private MutatableGraph tested = MutatableGraphFactory.getBarabasiGraph(2,10);

    @Test
    void testImportExport(){
        String path = "ExportTest.csv";
        IOUtils.graphToFile(tested,path);
        MutatableGraph graph = IOUtils.fileToGraph(path);
        assertEquals(graph, tested);
    }

    @Test
    void testEdgesFileImport(){
        String path = "test.edges";
        MutatableGraph graph = IOUtils.fileToGraphEdges(path);
        assertEquals(graph.getJgraphContained().edgeSet().size(), 3239);
        assertEquals(graph.getJgraphContained().vertexSet().size(), 924);
        assertTrue(graph.getJgraphContained().containsEdge("60","65"));
        assertTrue(graph.getJgraphContained().containsEdge("178","181"));
        assertTrue(graph.getJgraphContained().containsEdge("33","140"));
        assertTrue(graph.getJgraphContained().containsEdge("46","607"));
    }
}