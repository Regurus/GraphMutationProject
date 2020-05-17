import edu.uci.ics.jung.algorithms.scoring.VertexScorer;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.VertexScoringAlgorithm;
import org.jgrapht.alg.scoring.*;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;

import java.util.*;

public class Scores {

    public static List<Map> scoreGraph(MutatableGraph graph, ORCA_Adapter orca){
        ArrayList<Map> result = new ArrayList<Map>();
        result.add(getScores(new ClusteringCoefficient(graph.getJgraphContained())));
        result.add(getScores(new Coreness(graph.getJgraphContained())));
        result.add(getScores(new AlphaCentrality(graph.getJgraphContained())));
        result.add(getScores(new BetweennessCentrality(graph.getJgraphContained())));
        result.add(getScores(new ClosenessCentrality(graph.getJgraphContained())));
        result.add(getScores(new HarmonicCentrality(graph.getJgraphContained())));
        result.add(getScores(new PageRank(graph.getJgraphContained())));
        result.add(getEigenvecorCentrality(graph));
        result = (ArrayList<Map>) orca.addGraphletScores(result,graph);
        return result;
    }

    public static List<String> getScoreNames(ORCA_Adapter orca){
        ArrayList<String> result = new ArrayList<String>();
        result.add("AlphaCentrality");
        result.add("BetweennessCentrality");
        result.add("ClosenessCentrality");
        result.add("ClusteringCoefficient");
        result.add("Coreness");
        result.add("HarmonicCentrality");
        result.add("PageRank");
        result.add("Eigenvector");//Eigenvector
        result = (ArrayList<String>) orca.addGraphletsNames(result);
        return result;
    }

    private static Map<String,String> getEigenvecorCentrality(MutatableGraph graph){
        EigenvectorCentrality eig = new EigenvectorCentrality(graph.getJungContained());
        eig.evaluate();
        Map<String,String> result = new HashMap<>();
        Set<String> vertexSet = graph.getJgraphContained().vertexSet();
        for(String vert: vertexSet){
            Object res = eig.getVertexScore(vert);
            result.put(vert.toString(),res.toString());
        }
        return result;
    }

    private static Map getScores(VertexScoringAlgorithm scorer){
        return scorer.getScores();
    }
}
