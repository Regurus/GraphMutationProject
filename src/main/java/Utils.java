import org.jgrapht.Graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Utils {
    /**
     * WARNING: appends to file if file exists
     * @param newScores map vertex->score for graph after manipulation
     * @param oldScores map vertex->score for graph before manipulation
     * @param scoreNames names of those scores
     *                   !!! - all those should be placed in the same order
     * @param graphName identifier of current graph
     * @param fileName filename to save into
     */
    public static void saveRunResults(List<Map> newScores, List<Map> oldScores, List<String> scoreNames, String graphName,String fileName){
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
        dumpToFile(lines,fileName,head,true);
    }

    static void dumpToFile (List<String> lines, String filename,String headerLine,boolean append){
        System.out.println("File write in progress...");
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
}
