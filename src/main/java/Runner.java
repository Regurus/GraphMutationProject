import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Random;



public class Runner {
    private static final double MUTATION_FACTOR = 0.2;


    public static void main(String[] args) {

        MutatableGraph g1 = MutatableGraphFactory.getBarabasiGraph(6,100);
        //MutatableGraph g1 = MutatableGraphFactory.openCSV("test.csv");
        List<Map> apriori = Scores.scoreGraph(g1.getContained());
        g1 = applyMutators(g1,(int)(g1.getContained().edgeSet().size()*MUTATION_FACTOR));
        List<Map> postpriori = Scores.scoreGraph(g1.getContained());
        Utils.saveRunResults(postpriori,apriori,Scores.getScoreNames(),""+g1.hashCode(),"testFile.csv");
    }

    public static MutatableGraph applyMutators(MutatableGraph graph, int howMuch){
        Random generator = new Random();
        for(int i=0;i<howMuch;i++){
            graph.applyOperator(Mutator.values()[generator.nextInt(1)]);
        }
        return graph;
    }


}
