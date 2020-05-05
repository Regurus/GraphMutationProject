import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * class utilizing existing code for computing graphlet count in graph and adapting it for java runtime
 * original code written in C++ found https://github.com/thocevar/orca
 * for use on other machines it should be recompiled as orca.exe and put in resources folder
 */
public class ORCA_Adapter {

    private final String BASE_COMMAND = "resources/orca.exe ";
    private final String CACHE_FILE = "resources/orca_cache.ch";
    private final String TEMP_FILE = "resources/orca_temp.ch";
    private int n = 0;
    private String mode = "node ";
    private int orbit_size=4;
    private int wide = 15;

    public ORCA_Adapter(String mode, int orbit_size){
        if(orbit_size==5){
            this.orbit_size = 5;
            this.wide=73;
        }
        if(mode.equals("edge")){
            this.mode = mode+" ";
        }
    }

    public long[][] getOrbits(MutatableGraph graph){
        this.graphToPairFile(graph);
        this.runORCA();
        long[][] res = this.readResults();
        //cleanup
        File cache = new File(CACHE_FILE);
        cache.delete();
        File temp = new File(TEMP_FILE);
        temp.delete();
        return res;
    }

    private void graphToPairFile(MutatableGraph graph){
        Graph g = graph.getJgraphContained();
        this.n = g.vertexSet().size();
        Set<DefaultEdge> edges = g.edgeSet();
        ArrayList<String> lines = new ArrayList<String>();
        for(DefaultEdge edge:edges){
            String edgeRepr = edge.toString();
            edgeRepr = edgeRepr.substring(1,edgeRepr.length()-1);
            edgeRepr = edgeRepr.replace(" : "," ");
            lines.add(edgeRepr);
        }
        String headerLine = this.n+" "+edges.size();
        Utils.dumpToFile(lines,CACHE_FILE,headerLine,false);
    }

    private void runORCA(){
        try {
            String command = BASE_COMMAND+mode+orbit_size+" "+CACHE_FILE+" "+TEMP_FILE;
            Runtime run  = Runtime.getRuntime();
            Process proc = run.exec(command);
            int exitVal = proc.waitFor();
            if(exitVal!=0)
                throw new RuntimeException("ORCA runtime Error occured!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long[][] readResults(){
        long[][] result = new long[n][this.wide];
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader(TEMP_FILE));
            String line="";
            int lineCounter = 0;
            while ((line = bufferreader.readLine()) != null) {
                String[] splitLine = line.split(" ");
                for(int i=0;i<splitLine.length;i++){
                    result[lineCounter][i] = Long.parseLong(splitLine[i]);
                }
                lineCounter++;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
