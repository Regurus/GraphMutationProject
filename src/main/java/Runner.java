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
        Scores.getEigenvecorCentrality(g1);
    }

    public static MutatableGraph applyMutators(MutatableGraph graph, int howMuch){
        Random generator = new Random();
        for(int i=0;i<howMuch;i++){
            graph.applyOperator(Mutator.values()[generator.nextInt(1)]);
        }
        return graph;
    }


}
