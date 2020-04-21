import org.jgrapht.Graph;

import java.util.Random;

public class MutatableGraph {
    private Graph contained;
    private static Random generator = new Random();
    public MutatableGraph(Graph contained) {
        this.contained = contained;
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
        Object[] edges = this.contained.edgeSet().toArray();
        int removedIndex = generator.nextInt(edges.length);
        this.contained.removeEdge(edges[removedIndex]);
        System.out.println(this.contained.edgeSet());
    }

    public void addRandom(){
        Object[] vertices = this.contained.vertexSet().toArray();
        int sourceVert = generator.nextInt(vertices.length);
        int destVert = generator.nextInt(vertices.length);
        //filtering self edges
        while (sourceVert==destVert){
            destVert = generator.nextInt(vertices.length);
            //TODO existing edge
        }
        this.contained.addEdge(vertices[sourceVert],vertices[destVert]);
    }

    public Graph getContained(){
        return this.contained;
    }
}
