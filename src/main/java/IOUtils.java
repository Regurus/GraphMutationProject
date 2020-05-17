import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.csv.CSVExporter;
import org.jgrapht.nio.csv.CSVFormat;
import org.jgrapht.nio.csv.CSVImporter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jgrapht.util.SupplierUtil.createStringSupplier;


//collections of IO utility functions
public class IOUtils {
    /**
     * WARNING: appends to file if file exists
     * @param newScores map vertex->score for graph after manipulation
     * @param oldScores map vertex->score for graph before manipulation
     * @param scoreNames names of those scores
     *                   !!! - all those should be placed in the same order
     * @param graphName identifier of current graph
     * @param fileName filename to save into
     */
    public static synchronized void saveRunResults(List<Map> newScores, List<Map> oldScores, List<String> scoreNames, String graphName,String fileName){
        Object[] keys = newScores.get(0).keySet().toArray();
        ArrayList<String> lines = new ArrayList();
        //build header line
        StringBuilder header = new StringBuilder();
        header.append("graphName,vertexID,");
        for (String scoreName: scoreNames){
            header.append("new_"+scoreName+",");
            header.append("old_"+scoreName+",");
        }
        String head = header.toString();
        //building lines vertex by vertex
        for(Object key:keys){
            StringBuilder lineBuilder = new StringBuilder();
            lineBuilder.append(graphName+","+key.toString()+",");
            for(int i=0;i<newScores.size();i++){
                lineBuilder.append(newScores.get(i).get(key)+",");
                lineBuilder.append(oldScores.get(i).get(key)+",");
            }
            lines.add(lineBuilder.toString());
        }
        dumpResultsToFile(lines,fileName,head,true);
    }

    static void dumpResultsToFile(List<String> lines, String filename, String headerLine, boolean append){
        BufferedWriter writer = null;
        try {
            File fileExistsCheck = new File(filename);
            boolean exists = fileExistsCheck.exists();
            FileWriter file = new FileWriter(filename, append); //true tells to append data.
            writer = new BufferedWriter(file);
            if(!exists||!append)
                writer.write(headerLine+"\r\n");
            for(String line:lines){
                writer.write(line+"\r\n");
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //saves the graph instance to file
    public static void graphToFile(MutatableGraph graph,String path){
        CSVExporter exporter = new CSVExporter(CSVFormat.MATRIX,';');
        exporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID,true);
        File target = new File(path);
        exporter.exportGraph(graph.getJgraphContained(),target);
    }

    public static MutatableGraph fileToGraph(String path){
        CSVImporter importer = new CSVImporter(CSVFormat.MATRIX,';');
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID,true);
        File target = new File(path);
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
