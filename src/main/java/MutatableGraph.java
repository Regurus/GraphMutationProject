import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.jgrapht.Graph;


import java.util.Random;

public class MutatableGraph {
    private Graph jgraphContained;
    //maintaining two type of graphs for additional score capability.
    private UndirectedSparseGraph jungContained;
    private static Random generator = new Random();

    public UndirectedSparseGraph getJungContained() {
        return jungContained;
    }

    public MutatableGraph(Graph jgraphContained) {
        this.jgraphContained = jgraphContained;
        this.jungContained = JUNG_Adapter.getJungGraph(jgraphContained);
    }

    public void applyOperator(Mutator mutator){
        switch (mutator){
            case ADDITION:
                this.addRandom();
                break;
            case SUBSTRACTION:
                this.substractRandom();
                break;
        }
    }

    private void substractRandom(){
        Object[] edges = this.jgraphContained.edgeSet().toArray();
        int removedIndex = generator.nextInt(edges.length);
        this.jgraphContained.removeEdge(edges[removedIndex]);
        System.out.println(this.jgraphContained.edgeSet());
    }

    public void addRandom(){
        Object[] vertices = this.jgraphContained.vertexSet().toArray();
        int sourceVert = generator.nextInt(vertices.length);
        int destVert = generator.nextInt(vertices.length);
        //filtering self edges
        while (sourceVert==destVert){
            destVert = generator.nextInt(vertices.length);
            //TODO existing edge
        }
        this.jgraphContained.addEdge(vertices[sourceVert],vertices[destVert]);
    }

    public Graph getJgraphContained(){
        return this.jgraphContained;
    }
}
