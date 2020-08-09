package GraphProject.mains;

import GraphProject.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static GraphProject.Experiments.getCustoms;

public class SecondExperiment {
    public static void main(String[] args) {
        String path = "resources/Graphs/second";
        File secondDir = new File(path);
        String[] secondFiles = secondDir.list();
        for(int i=0;i<secondFiles.length;i++){
            secondFiles[i] = path+'/'+secondFiles[i];
        }

        path = "resources/Graphs/yeast";
        File yeastDir = new File(path);
        String[] yeastFiles = yeastDir.list();
        for(int i=0;i<yeastFiles.length;i++){
            yeastFiles[i] = path+'/'+yeastFiles[i];
        }
        ArrayList<MutatableGraph> batch1_graphs = getCustoms(secondFiles,new ArrayList<>());
        //node names removed
        ArrayList<MutatableGraph> batch2_graphs = getCustoms(yeastFiles,new ArrayList<>());
        for(MutatableGraph graph:batch2_graphs){
            scoreOnly(graph,graph.getName()+".csv");
            System.out.println(graph.getName()+" - DONE");
        }
        for(MutatableGraph graph:batch1_graphs){
            runSingle(graph,"second experiment result.csv",0,0.1);
            System.out.println(graph.getName()+" 01 - DONE");
        }
        batch1_graphs = getCustoms(secondFiles,new ArrayList<>());
        for(MutatableGraph graph:batch1_graphs){
            runSingle(graph,"second experiment result.csv",0.1,0);
            System.out.println(graph.getName()+" 02 - DONE");
        }
        batch1_graphs = getCustoms(secondFiles,new ArrayList<>());
        for(MutatableGraph graph:batch1_graphs){
            runSingle(graph,"second experiment result.csv",0.1,0.1);
            System.out.println(graph.getName()+" 03 - DONE");
        }

    }
    private static void runSingle(MutatableGraph graph, String file,double add,double substract){
        ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
        List<String> names = Scores.getScoreNames(orca);
        //try{
        List<Map> old_scores = Scores.scoreGraph(graph,orca);
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
    private static void scoreOnly(MutatableGraph graph, String file){
        ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
        List<String> names = Scores.getScoreNames(orca);
        List<Map> old_scores = Scores.scoreGraph(graph,orca);
        List<Map> meta = graph.getMetadata();
        IOUtils.saveRunResults(old_scores,meta,names,""+graph.getName(),file);
    }
}
