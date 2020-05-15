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

}