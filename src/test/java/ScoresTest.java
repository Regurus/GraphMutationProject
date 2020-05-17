import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ScoresTest {

    @Test
    public void fullSingleRunTest(){
        MutatableGraph graph = MutatableGraphFactory.getBarabasiGraph(2,10);
        ORCA_Adapter orca = new ORCA_Adapter("node",5,"1");
        List<String> names = Scores.getScoreNames(orca);
        List<Map> old_scores = Scores.scoreGraph(graph,orca);
        Random generator = new Random();
        for(int i=0;i<15;i++){
            Mutator mut = Mutator.values()[generator.nextInt(2)];
            graph.applyOperator(mut);
        }
        List<Map> new_scores = Scores.scoreGraph(graph,orca);
        IOUtils.saveRunResults(new_scores,old_scores,names,""+graph.hashCode(),"testSave.svvv");
    }
}