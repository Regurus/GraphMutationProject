package GraphProject;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.csv.CSVExporter;
import org.jgrapht.nio.csv.CSVFormat;
import org.jgrapht.nio.csv.CSVImporter;

import java.io.*;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;
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
    public static synchronized void saveRunResults(List<Map> newScores, List<Map> oldScores,List<Map> metadata, List<String> scoreNames, String graphName,String fileName){
        Object[] keys = newScores.get(0).keySet().toArray();
        ArrayList<String> lines = new ArrayList<>();
        //build header line
        StringBuilder header = new StringBuilder();
        header.append("graphName,vertexID,startTime,verticeChanged,verticeAmount,priorEdged,postEdges,addedAmount," +
                "removedAmount,addedPercent,removedPercent,");
        for (String scoreName: scoreNames){
            header.append("new_"+scoreName+",");
            header.append("old_"+scoreName+",");
        }
        String head = header.toString();
        //building lines vertex by vertex
        for(Object key:keys){
            StringBuilder lineBuilder = new StringBuilder();
            lineBuilder.append(graphName+","+key.toString()+",");
            for(int i=0;i<metadata.size();i++){
                lineBuilder.append(metadata.get(i).get(key)+",");
            }
            for(int i=0;i<newScores.size();i++){
                lineBuilder.append(newScores.get(i).get(key)+",");
                lineBuilder.append(oldScores.get(i).get(key)+",");
            }
            lines.add(lineBuilder.toString());
        }
        dumpResultsToFile(lines,fileName,head,true);
    }

    /**
     * Same as previous function, used for analysis without mutating the graph. No new/old classification
     * WARNING: do not use on the same file as previous function.
     */
    public static synchronized void saveRunResults(List<Map> scores,List<Map> metadata, List<String> scoreNames, String graphName,String fileName){
        Object[] keys = scores.get(0).keySet().toArray();
        ArrayList<String> lines = new ArrayList<>();
        //build header line
        StringBuilder header = new StringBuilder();
        header.append("graphName,vertexID,startTime,verticeChanged,verticeAmount,priorEdged,postEdges,addedAmount," +
                "removedAmount,addedPercent,removedPercent,");
        for (String scoreName: scoreNames){
            header.append(scoreName+",");
        }
        String head = header.toString();
        //building lines vertex by vertex
        for(Object key:keys){
            StringBuilder lineBuilder = new StringBuilder();
            lineBuilder.append(graphName+","+key.toString()+",");
            for(int i=0;i<metadata.size();i++){
                lineBuilder.append(metadata.get(i).get(key.toString())+",");
            }
            for(int i=0;i<scores.size();i++){
                lineBuilder.append(scores.get(i).get(key.toString())+",");
            }
            lines.add(lineBuilder.toString());
        }
        dumpResultsToFile(lines,fileName,head,true);
    }
    /**
     * write to file procedure
     * @param lines content
     * @param filename file
     * @param headerLine first line
     * @param append or rewrite
     */
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
        CSVImporter<String, DefaultEdge> importer = new CSVImporter<String, DefaultEdge>(CSVFormat.MATRIX,';');
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID,true);
        return getGraph(path,importer);
    }

    public static MutatableGraph fileToGraphEdges(String path){
        CSVImporter<String, DefaultEdge> importer = new CSVImporter<String, DefaultEdge>(CSVFormat.ADJACENCY_LIST,' ');
        importer.setParameter(CSVFormat.Parameter.EDGE_WEIGHTS,false);
        return getGraph(path,importer);
    }



    private static MutatableGraph getGraph(String path, CSVImporter<String, DefaultEdge> importer){
        GraphReader graphReader = new GraphReader();
        File target = new File(path);
        DefaultUndirectedGraph graph = graphReader.getGraph(path," ");
        return new MutatableGraph(graph,target.getName(),-1);
    }

}
