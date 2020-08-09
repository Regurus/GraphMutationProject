package GraphProject.mains;

import GraphProject.IOUtils;
import GraphProject.MutatableGraph;
import GraphProject.ORCA_Adapter;
import GraphProject.Scores;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static GraphProject.Experiments.getCustoms;

public class GraphAnalysis {

    public static void main(String[] args) {
        String path = "resources/Graphs/Analysis";
        File dir = new File(path);
        String[] files = dir.list();
        for(int i=0;i<files.length;i++){
            files[i] = path+'/'+files[i];
        }
        ArrayList<MutatableGraph> graphs = getCustoms(files,new ArrayList<>());
        for (MutatableGraph graph:graphs){
            System.out.println(graph.getName()+"----------------------------");
            int nodes = graph.getJgraphContained().vertexSet().size();
            int edges = graph.getJgraphContained().edgeSet().size();
            System.out.println("Nodes Count: "+nodes);
            System.out.println("Edges Count: "+edges);
            double occupancy = (double)edges/(nodes*(nodes-1)/2);
            System.out.println("Occupancy: "+String.format("%.2f", occupancy));
            ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
            List<String> names = Scores.getScoreNames(orca);
            List<Map> scores = Scores.scoreGraph(graph,orca);
            List<Map> meta = graph.getMetadata();
            for(int i=0;i<scores.size();i++){
                System.out.println("\t"+names.get(i)+":");
                System.out.println("\t\tMean :"+String.format("%.2f", calculateMean(scores.get(i).values())));
                System.out.println("\t\tStandard Deviation :"+String.format("%.2f", calculateSD(scores.get(i).values())));
            }
            IOUtils.saveRunResults(scores,meta,names,""+graph.getName(),"analysis.csv");

        }

    }

    public static double calculateSD(Collection numArray)
    {
        double mean = calculateMean(numArray);
        double standardDeviation = 0.0;
        for(Object num: numArray) {
            if(num instanceof Integer){
                int temp = (Integer)num;
                num = (double)temp;
            }
            else if(num instanceof String){
                num = Double.parseDouble((String)num);
            }
            standardDeviation += Math.pow((double)num - mean, 2);
        }
        return Math.sqrt(standardDeviation/numArray.size());
    }
    public static double calculateMean(Collection numArray)
    {
        double sum = 0.0;
        int length = numArray.size();
        for(Object num : numArray) {
            int temp = 0;
            if(num instanceof Integer){
                temp = (Integer)num;
                num = (double)temp;
            }
            else if(num instanceof String){
                num = Double.parseDouble((String)num);
            }
            sum += (double)num;
        }
        double mean = sum/length;
        return mean;
    }
}
