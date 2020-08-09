package GraphProject.mains;

import GraphProject.*;
import com.google.common.base.Stopwatch;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimeAssessment {
    public static void main(String[] args) {
        int runCount = 100;
        DecimalFormat df = new DecimalFormat("#.##");
        int[] nodes = new int[]{100};
        double[] occupancies = new double[]{0.01};
        for(int nodeA : nodes){
            System.out.println("Node amount: "+nodeA);
            for (double occupancy:occupancies){
                long timeDump = 0;
                for(int i=0;i<runCount;i++){
                    Stopwatch stopwatch = Stopwatch.createStarted();
                    runSingle(nodeA,occupancy,"testSave.svvv");
                    stopwatch.stop();
                    timeDump += stopwatch.elapsed(TimeUnit.MILLISECONDS);
                }
                System.out.println("\tOccupancy ratio: "+occupancy+" Average Time: "+df.format(timeDump/runCount)+" ms.");
            }
        }
    }
    private static void runSingle(int nodeAmount, double occupancy,String file){
        int edgeCount = (int)(((nodeAmount*(nodeAmount-1))/2)*occupancy);
        MutatableGraph graph = MutatableGraphFactory.getErdoshGraph(nodeAmount,edgeCount);
        ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
        List<String> names = Scores.getScoreNames(orca);
        //try{
            List<Map> old_scores = Scores.scoreGraph(graph,orca);
            Random generator = new Random();
            for(int i=0;i<nodeAmount;i++){
                Mutator mut = Mutator.values()[generator.nextInt(2)];
                graph.applyOperator(mut);
            }
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
