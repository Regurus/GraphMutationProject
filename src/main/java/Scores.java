import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.VertexScoringAlgorithm;
import org.jgrapht.alg.scoring.*;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Scores {

    public static void printAlphaCentrality(Graph graph){
        System.out.println("============================Alpha Centrality============================");
        AlphaCentrality scorer = new AlphaCentrality(graph);
        Map scores = scorer.getScores();
        for(Object key:scores.keySet()){
            System.out.println("Score: "+key.toString()+" "+scores.get(key).toString());
        }
    }

    public static void  printBetweennessCentrality(Graph graph){
        System.out.println("============================Betweenness Centrality============================");
        BetweennessCentrality scorer = new BetweennessCentrality(graph);
        Map scores = scorer.getScores();
        for(Object key:scores.keySet()){
            System.out.println("Score: "+key.toString()+" "+scores.get(key).toString());
        }
    }

    public static void printClosenessCentrality(Graph graph){
        System.out.println("============================Closeness Centrality============================");
        ClosenessCentrality scorer = new ClosenessCentrality(graph);
        Map scores = scorer.getScores();
        for(Object key:scores.keySet()){
            System.out.println("Score: "+key.toString()+" "+scores.get(key).toString());
        }
    }

    public static void printClusteringCoefficient(Graph graph){
        System.out.println("============================Clustering Coefficient============================");
        ClusteringCoefficient scorer = new ClusteringCoefficient(graph);
        Map scores = scorer.getScores();
        for(Object key:scores.keySet()){
            System.out.println("Score: "+key.toString()+" "+scores.get(key).toString());
        }
    }

    public static void printCoreness(Graph graph){
        System.out.println("============================Coreness============================");
        Coreness scorer = new Coreness(graph);
        Map scores = scorer.getScores();
        for(Object key:scores.keySet()){
            System.out.println("Score: "+key.toString()+" "+scores.get(key).toString());
        }
    }

    public static void printHarmonicCentrality(Graph graph){
        System.out.println("============================Harmonic Centrality============================");
        HarmonicCentrality scorer = new HarmonicCentrality(graph);
        Map scores = scorer.getScores();
        for(Object key:scores.keySet()){
            System.out.println("Score: "+key.toString()+" "+scores.get(key).toString());
        }
    }

    public static void printPageRank(Graph graph){
        System.out.println("============================PageRank============================");
        PageRank scorer = new PageRank(graph);
        Map scores = scorer.getScores();
        for(Object key:scores.keySet()){
            System.out.println("Score: "+key.toString()+" "+scores.get(key).toString());
        }
    }
    public static List<Map> scoreGraph(MutatableGraph graph){
        ArrayList<Map> result = new ArrayList<Map>();
        result.add(getScores(new ClusteringCoefficient(graph.getJgraphContained())));
        result.add(getScores(new Coreness(graph.getJgraphContained())));
        result.add(getScores(new AlphaCentrality(graph.getJgraphContained())));
        result.add(getScores(new BetweennessCentrality(graph.getJgraphContained())));
        result.add(getScores(new ClosenessCentrality(graph.getJgraphContained())));
        result.add(getScores(new HarmonicCentrality(graph.getJgraphContained())));
        result.add(getScores(new PageRank(graph.getJgraphContained())));
        //eigenvector

        //orbits
        return result;
    }

    public static List<String> getScoreNames(){
        ArrayList<String> result = new ArrayList<String>();
        result.add("AlphaCentrality");
        result.add("BetweennessCentrality");
        result.add("ClosenessCentrality");
        result.add("ClusteringCoefficient");
        result.add("Coreness");
        result.add("HarmonicCentrality");
        result.add("PageRank");
        return result;
    }

    public static List<String> getEigenvecorCentrality(MutatableGraph graph){
        EigenvectorCentrality eig = new EigenvectorCentrality(graph.getJungContained());
        ArrayList<String> result = new ArrayList<String>();
        Set<String> edges = graph.getJgraphContained().vertexSet();
        for(String vert: edges){
            Object res = eig.getVertexScore(vert);
            System.out.println(vert);
            result.add(""+res);
        }
        return result;
    }

    public static Map getScores(VertexScoringAlgorithm scorer){
        return scorer.getScores();
    }
}
