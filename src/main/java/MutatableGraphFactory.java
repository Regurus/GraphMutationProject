import org.jgrapht.generate.BarabasiAlbertGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.csv.CSVFormat;
import org.jgrapht.nio.csv.CSVImporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import static org.jgrapht.util.SupplierUtil.createStringSupplier;

public class MutatableGraphFactory {

    public static MutatableGraph getBarabasiGraph(int m,int n){
        BarabasiAlbertGraphGenerator generator = new BarabasiAlbertGraphGenerator(m,m,n);
        DefaultUndirectedGraph<String, DefaultEdge> target = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        target.setVertexSupplier(createStringSupplier());
        generator.generateGraph(target,null);
        return new MutatableGraph(target);
    }

    public static MutatableGraph openCSV(String pathToFile){
        CSVImporter importer = new CSVImporter(CSVFormat.MATRIX,';');
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID,true);
        File target = new File(pathToFile);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(target));
            DefaultUndirectedGraph<String, DefaultEdge> graph = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
            graph.setVertexSupplier(createStringSupplier());
            importer.importGraph(graph,inputStreamReader);
            return new MutatableGraph(graph);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }
        catch (ImportException ie){
            ie.printStackTrace();
            System.out.println("Import error!");
        }
        return null;
    }


}
