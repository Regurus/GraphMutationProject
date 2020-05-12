import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IOUtilsTest {
    MutatableGraph tested = MutatableGraphFactory.getBarabasiGraph(2,10);

    @Test
    public void testImportExport(){
        String path = "ExportTest.csv";
        IOUtils.graphToFile(tested,path);
        MutatableGraph graph = IOUtils.fileToGraph(path);
        assertTrue(graph.equals(tested));
    }

}