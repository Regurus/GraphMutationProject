import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;


import java.util.*;

public class MutatableGraph {
    private Graph jgraphContained;
    //maintaining two type of graphs for additional scoring capability.
    private UndirectedSparseGraph jungContained;
    private HashSet<String> edgeMap;//used to prevent repetitive addition and conserve search time
    private HashSet<String> missingEdgeMap;//used to prevent speed up addition in dense graphs
    private static Random generator = new Random();
    private boolean filled = false;
    private final int MAX_EDGES;

    public MutatableGraph(Graph jgraphContained) {
        this.jgraphContained = jgraphContained;
        this.jungContained = JUNG_Adapter.getJungGraph(jgraphContained);
        this.buildEdgeMap();
        MAX_EDGES = (jgraphContained.vertexSet().size()*(jgraphContained.vertexSet().size()-1))/2;
        if(edgeMap.size()>0.8*MAX_EDGES)
            this.buildMissingEdgeMap();
    }
    public void buildMissingEdgeMap(){
        this.missingEdgeMap = new HashSet<>();
        for(int i=0;i<this.jgraphContained.vertexSet().size();i++){
            for(int j=0;j<this.jgraphContained.vertexSet().size();j++){
                if(i==j)
                    continue;
                if(!this.jgraphContained.containsEdge(""+i,""+j)&&!this.jgraphContained.containsEdge(""+j,""+i)){
                    if(!this.missingEdgeMap.contains("("+i+" : "+j+")")&&!this.missingEdgeMap.contains("("+j+" : "+i+")"))
                        this.missingEdgeMap.add("("+i+" : "+j+")");
                }
            }
        }
    }
    private void buildEdgeMap(){
        Set<Object> edges = this.jgraphContained.edgeSet();
        this.edgeMap = new HashSet<>();
        for(Object edge:edges){
            this.edgeMap.add(edge.toString());
        }
    }

    public void applyOperator(Mutator mutator){
        switch (mutator){
            case ADDITION:
                this.addRandom();
                break;
            case SUBTRACTION:
                this.substractRandom();
                break;
        }
    }

    private void substractRandom(){
        Object[] edges = this.jgraphContained.edgeSet().toArray();
        int removedIndex = generator.nextInt(edges.length);
        String[] split = edges[removedIndex].toString().split(" : ");
        this.removeEdge(split[0].substring(1),split[1].substring(0,split[1].length()-1));
    }
    public void addRandomPrecalculated(){
        int next = generator.nextInt(this.missingEdgeMap.size());
        String nextEdge = this.missingEdgeMap.toArray()[next].toString();
        String[] split = nextEdge.split(" : ");
        this.addEdge(split[0].substring(1),split[1].substring(0,split[1].length()-1));

    }
    public void addRandomNotPrecalculated(){
        Object[] vertices = this.jgraphContained.vertexSet().toArray();
        int sourceVert = 0;
        int destVert = 0;
        //filtering self edges
        while (sourceVert==destVert){
            sourceVert = generator.nextInt(vertices.length);
            destVert = generator.nextInt(vertices.length);
            if(this.edgeContained(vertices[sourceVert].toString(),vertices[destVert].toString()))//filtering reoccurring edges
                sourceVert=destVert;
        }
        this.addEdge(vertices[sourceVert].toString(),vertices[destVert].toString());
    }
    public void addRandom(){
        if(this.missingEdgeMap!=null){
            addRandomPrecalculated();
        }
        else{
            addRandomNotPrecalculated();
        }
    }

    private void addEdge(String source,String target){
        if(this.edgeMap.size()==this.MAX_EDGES){
            this.filled = true;
            return;
        }
        this.jungContained.addEdge("("+source+" : "+target+")",source,target);
        this.jgraphContained.addEdge(source,target);
        this.edgeMap.add("("+source+" : "+target+")");
        if(this.missingEdgeMap!=null)
            this.missingEdgeMap.remove("("+source+" : "+target+")");
    }

    private void removeEdge(String source,String target){
        if(this.missingEdgeMap!=null&&this.missingEdgeMap.size()==0){
            this.filled = true;
            return;
        }
        this.jungContained.removeEdge("("+source+" : "+target+")");
        this.jgraphContained.removeEdge(source,target);
        this.edgeMap.remove("("+source+" : "+target+")");
        if(this.missingEdgeMap!=null)
            this.missingEdgeMap.add("("+source+" : "+target+")");
    }

    private boolean edgeContained(String source, String target){
        return this.edgeMap.contains("("+source+" : "+target+")") ||
                this.edgeMap.contains("("+target+" : "+source+")");
    }
    public Graph getJgraphContained(){
        return this.jgraphContained;
    }

    public UndirectedSparseGraph getJungContained() {
        return jungContained;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutatableGraph graph = (MutatableGraph) o;
        if(this.edgeMap.size()!=graph.edgeMap.size())
            return false;
        if(this.getJgraphContained().vertexSet().size()!=graph.getJgraphContained().vertexSet().size())
            return false;
        Set edges = this.getJgraphContained().edgeSet();
        for(Object edge : edges){
            String ed = edge.toString();
            String[] split = ed.split(" : ");
            if(!this.edgeContained(split[0].substring(1),split[1].substring(0,split[1].length()-1)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jgraphContained, jungContained, edgeMap);
    }

    //debug function to validate both graphs to be the same and correspond to edgemap
    boolean assertIntegrity(){
        Object[] JGraphEdges = this.jgraphContained.edgeSet().toArray();
        Collection JUNGEdges = this.jungContained.getEdges();
        if(this.edgeMap.size()!=JGraphEdges.length)
            return false;
        if(this.edgeMap.size()!=JUNGEdges.size())
            return false;
        for(Object edge : JGraphEdges){
            if(!this.edgeMap.contains(edge.toString()))
                return false;
        }
        for(Object edge : JUNGEdges){
            if(!this.edgeMap.contains(edge.toString()))
                return false;
        }
        return true;
    }

    private Mutator[] getRandomMutators(double amount, int bound){
        Mutator[] result = new Mutator[(int)amount];
        Random generator = new Random();
        for(int i=0;i<amount;i++){
            result[i] = Mutator.values()[generator.nextInt(bound)];
        }
        return result;
    }

    public void applyRandomByPercentVert(double pers){
        this.applyMutators(this.getRandomMutators((int)(this.jgraphContained.vertexSet().size()*pers),2));
    }

    public void applyRandomByPercentEdge(double pers){
        this.applyMutators(this.getRandomMutators((int)(this.jgraphContained.edgeSet().size()*pers),2));
    }

    public void applyRandomByAmount(double amount){
        this.applyMutators(this.getRandomMutators(amount,2));
    }

    public void applyMutators(Mutator[] mutators){
        for(Mutator mutator:mutators){
            if(this.filled)
                return;
            this.applyOperator(mutator);
        }
    }
}
