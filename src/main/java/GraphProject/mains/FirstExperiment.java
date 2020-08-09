package GraphProject.mains;

import GraphProject.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FirstExperiment {
    public static void main(String[] args) {
        int runCount = 10;
        int[] nodes = new int[]{300};
        double[] occupancies = new double[]{0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9};
        for(int nodeA : nodes){
            System.out.println("Node amount: "+nodeA);
            for (double occupancy:occupancies){
                for(int i=0;i<runCount;i++){
                    runSingle(nodeA,occupancy,"first experiment result.csv",0,0.1);
                    runSingle(nodeA,occupancy,"first experiment result.csv",0.1,0);
                    runSingle(nodeA,occupancy,"first experiment result.csv",0.1,0.1);
                }
            }
        }
    }
    private static void runSingle(int nodeAmount, double occupancy,String file,double add,double substract){
        int edgeCount = (int)(((nodeAmount*(nodeAmount-1))/2)*occupancy);
        MutatableGraph graph = MutatableGraphFactory.getErdoshGraph(nodeAmount,edgeCount);
        IOUtils.graphToFile(graph,"graf.csv");
        ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
        List<String> names = Scores.getScoreNames(orca);
        //try{
        List<Map> old_scores = Scores.scoreGraph(graph,orca);
        Random generator = new Random();
        if(add>0&&substract==0)
            graph.applyAddOnlyByPercentEdge(add);
        else if(substract>0&&add==0)
            graph.applyRemoveOnlyByPercentEdge(substract);
        else
            graph.applyRandomByPercentEdge(substract);

        List<Map> new_scores = Scores.scoreGraph(graph,orca);
        List<Map> meta = graph.getMetadata();
        IOUtils.saveRunResults(new_scores,old_scores,meta,names,""+graph.getName(),file);
        //}
        /*catch (RuntimeException re){
            System.out.println("ORCA Fail!");
            return;
        }*/
    }
}
