package GraphProject;

import GraphProject.MutatableGraph;
import GraphProject.MutatableGraphFactory;
import GraphProject.Mutator;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import org.jgrapht.alg.interfaces.VertexScoringAlgorithm;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MutatableGraphTest {
    MutatableGraph tested = MutatableGraphFactory.getBarabasiGraph(900,1000);

    @Test
    void assertIntegrity() {
        for(int i=0; i<10;i++){
            Random generator = new Random();
            for(int j=0;j<500;j++){
                tested.applyOperator(Mutator.values()[generator.nextInt(2)]);
            }
            assertTrue(tested.assertIntegrity(),"Graphs are not equal");
        }
    }
    //Cross validation between the libraries in scores they can both calculate
    @Test
    void compareBetweennessCentrality(){
        //constructing JUNG result
        BetweennessCentrality scorer = new BetweennessCentrality(tested.getJungContained());
        Map<String,String> JUNGRes = new HashMap<>();
        Set<String> vertexSet = tested.getJgraphContained().vertexSet();
        for(String vert: vertexSet){
            Object res = scorer.getVertexScore(vert);
            JUNGRes.put(vert.toString(),res.toString());
        }
        VertexScoringAlgorithm scoringAlgorithm = new org.jgrapht.alg.scoring.BetweennessCentrality(tested.getJgraphContained());
        Map JGraphRes = scoringAlgorithm.getScores();
        assertTrue(JUNGRes.size()==JGraphRes.size(),"Betweenness Centrality size fail!");
        for(int i=0;i<JUNGRes.size();i++){
            assertTrue(areFloatsEquals(JUNGRes.get(""+i),""+JGraphRes.get(""+i)),"Betweenness Centrality comparison fail!");
        }
    }
    @Test
    void compareClosenessCentrality(){
        //constructing JUNG result
        ClosenessCentrality scorer = new ClosenessCentrality<>(tested.getJungContained());
        Map<String,String> JUNGRes = new HashMap<>();
        Set<String> vertexSet = tested.getJgraphContained().vertexSet();
        for(String vert: vertexSet){
            Object res = scorer.getVertexScore(vert);
            JUNGRes.put(vert.toString(),res.toString());
        }
        VertexScoringAlgorithm scoringAlgorithm = new org.jgrapht.alg.scoring.ClosenessCentrality<>(tested.getJgraphContained());
        Map<String,Double> JGraphRes = scoringAlgorithm.getScores();
        assertTrue(JUNGRes.size()==JGraphRes.size(),"Closeness Centrality size fail!");
        for(int i=0;i<JUNGRes.size();i++){
            assertTrue(areFloatsEquals(JUNGRes.get(""+i),""+JGraphRes.get(""+i)),"Closeness Centrality comparison fail!");
        }
    }
    //better way of comparing float point numbers considering their natural inaccuracy
    private boolean areFloatsEquals(String a,String b){
        BigDecimal A = new BigDecimal(a,new MathContext(10, RoundingMode.HALF_UP));
        BigDecimal B = new BigDecimal(b,new MathContext(10, RoundingMode.HALF_UP));
        return A.compareTo(B)==0;
    }

    //check PageRank
}