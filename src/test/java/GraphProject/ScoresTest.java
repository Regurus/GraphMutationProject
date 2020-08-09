package GraphProject;

import GraphProject.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static GraphProject.Experiments.getCustoms;

class ScoresTest {

    @Test
    public void fullSingleRunTest(){
        MutatableGraph graph = MutatableGraphFactory.getBarabasiGraph(15,300);
        ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
        List<String> names = Scores.getScoreNames(orca);
        List<Map> old_scores = Scores.scoreGraph(graph,orca);
        Random generator = new Random();
        /*for(int i=0;i<15;i++){
            Mutator mut = Mutator.values()[generator.nextInt(2)];
            graph.applyOperator(mut);
        }*/
        List<Map> new_scores = Scores.scoreGraph(graph,orca);
        IOUtils.saveRunResults(new_scores,old_scores,names,""+graph.hashCode(),"testSave.svvv");
    }
    @Test
    public void fullSingleTest(){
        String[] gs = new String[]{"resources/Graphs/Analysis/syeast0.el","resources/Graphs/Analysis/yeast.el"};
        ArrayList<MutatableGraph> graphs = getCustoms(gs,new ArrayList<>());
        ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
        List<String> names = Scores.getScoreNames(orca);
        List<Map> old_scores = Scores.scoreGraph(graphs.get(0),orca);
        List<Map> new_scores = Scores.scoreGraph(graphs.get(1),orca);
        List<Map> meta = graphs.get(0).getMetadata();
        IOUtils.saveRunResults(new_scores,old_scores,meta,names,"yeast run","exp4.csv");
    }
}