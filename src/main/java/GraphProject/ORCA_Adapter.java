package GraphProject;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.io.*;
import java.util.*;

/**
 * class utilizing existing code for computing graphlet count in graph and adapting it for java runtime
 * original code written in C++ found https://github.com/thocevar/orca
 * for use on other machines it should be recompiled as orca.exe and put in resources folder
 */
public class ORCA_Adapter {

    private final String BASE_COMMAND = "resources/orca.exe ";
    private String CACHE_FILE = "resources/orca_cache1.ch";
    private String TEMP_FILE = "resources/orca_temp1.ch";
    private int n = 0;
    private String mode = "node ";
    private int orbit_size=4;
    private int wide = 15;

    public ORCA_Adapter(String mode, int orbit_size,String instance){
        CACHE_FILE = "resources/orca_cache"+instance+".ch";
        TEMP_FILE = "resources/orca_temp"+instance+".ch";
        if(orbit_size==5){
            this.orbit_size = 5;
            this.wide=73;
        }
        if(mode.equals("edge")){
            this.mode = mode+" ";
        }
    }

    private Map[] encode(MutatableGraph graph){
        Map[] result = new Map[2];
        result[0] = new HashMap<String,Integer>();
        result[1] = new HashMap<Integer,String>();
        int count=0;
        for(Object vert: graph.getJgraphContained().vertexSet()){
            result[0].put(vert.toString(),count);
            result[1].put(count++,vert.toString());
        }
        return result;
    }

    private List<Map> decode(long[][] coded,Map[] encoding){
        ArrayList<Map> result = new ArrayList<>();
        for(int i=0;i<coded[0].length;i++){
            HashMap<String,Double> singleScore = new HashMap();
            for(int j=0;j<coded.length;j++){
                singleScore.put((String)encoding[1].get(j),(double)coded[j][i]);
            }
            result.add(singleScore);
        }
        return result;
    }

    public List<Map> getOrbits(MutatableGraph graph){
        Map[] encoding = this.encode(graph);
        this.graphToPairFile(graph,encoding);
        this.runORCA();
        long[][] res = this.readResults(encoding);
        //cleanup
        File cache = new File(CACHE_FILE);
        cache.delete();
        File temp = new File(TEMP_FILE);
        temp.delete();
        return this.decode(res,encoding);
    }

    private void graphToPairFile(MutatableGraph graph,Map[] encoding){
        Graph g = graph.getJgraphContained();
        this.n = g.vertexSet().size();
        Set<DefaultEdge> edges = g.edgeSet();
        ArrayList<String> lines = new ArrayList<String>();
        for(DefaultEdge edge:edges){
            String edgeRepr = edge.toString();
            edgeRepr = edgeRepr.substring(1,edgeRepr.length()-1);
            String[] split = edgeRepr.split(" : ");
            edgeRepr = ""+encoding[0].get(split[0])+" "+encoding[0].get(split[1]);
            lines.add(edgeRepr);
        }
        String headerLine = this.n+" "+edges.size();
        IOUtils.dumpResultsToFile(lines,CACHE_FILE,headerLine,false);
    }

    private synchronized void runORCA(){
        try {
            String command = BASE_COMMAND+mode+orbit_size+" "+CACHE_FILE+" "+TEMP_FILE;
            Runtime run  = Runtime.getRuntime();
            Process proc = run.exec(command);
            int exitVal = proc.waitFor();
            if(exitVal!=0)
                throw new RuntimeException("GraphProject.ORCA runtime Error occured!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long[][] readResults(Map[] encoding){
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public List<String> addGraphletsNames(List<String> scores){
        for(int i=0;i<this.wide;i++){
            scores.add("Orb"+(i));
        }
        return scores;
    }

    public List<Map> addGraphletScores(List<Map> scores, MutatableGraph graph){
        for(Map score: this.getOrbits(graph)){
            scores.add(score);
        }
        return scores;
    }

}
