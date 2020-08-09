package GraphProject.mains;

import GraphProject.IOUtils;
import GraphProject.MutatableGraph;
import GraphProject.ORCA_Adapter;
import GraphProject.Scores;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static GraphProject.Experiments.getCustoms;

public class ThirdExperiment {
    public static void main(String[] args) {
        String path = "resources/Graphs/third";
        File thirdDir = new File(path);
        String[] thirdFiles = thirdDir.list();
        for(int i=0;i<thirdFiles.length;i++){
            thirdFiles[i] = path+'/'+thirdFiles[i];
        }
        ArrayList<MutatableGraph> graphs = getCustoms(thirdFiles,new ArrayList<>());
        MutatableGraph graph = graphs.get(0);
        for(int i=0;i<5;i++){
            ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
            graph.applyAddOnlyByAmount(200);
            scoreOnly(graph,"thirdExp_"+(i*5)+".csv");
        }

    }

    private static void scoreOnly(MutatableGraph graph, String file){
        ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
        List<String> names = Scores.getScoreNames(orca);
        List<Map> old_scores = Scores.scoreGraph(graph,orca);
        List<Map> meta = graph.getMetadata();
        IOUtils.saveRunResults(old_scores,meta,names,""+graph.getName(),file);
    }
}
