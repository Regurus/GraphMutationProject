package GraphProject;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.jgrapht.Graph;


import java.util.*;

public class MutatableGraph {
    private Graph jgraphContained;
    //maintaining two type of graphs for additional scoring capability.
    private UndirectedSparseGraph jungContained;
    private HashSet<String> edgeMap;//used to prevent repetitive addition and conserve search time
    private HashSet<String> missingEdgeMap;//used to prevent speed up addition in dense graphs
    private int[] mutatorsApplied = new int[2];
    private long startTime;
    private String name;
    private boolean filled = false;
    private HashMap<String,Integer> vertexChange = new HashMap<>();
    private static Random generator = new Random();
    private final int MAX_EDGES;
    private final int PRIOR_EDG;
    private List<Map> metadata;

    public MutatableGraph(Graph jgraphContained,String name,int id) {
        if(id>0)
            this.name = name+": "+id;
        else
            this.name = name;

        this.jgraphContained = jgraphContained;
        this.jungContained = JUNG_Adapter.getJungGraph(jgraphContained);
        this.startTime = System.currentTimeMillis();
        this.buildEdgeMap();

        PRIOR_EDG = this.jgraphContained.edgeSet().size();
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
        if(edges.length==0)
            return;
        int removedIndex = generator.nextInt(edges.length);
        String[] split = edges[removedIndex].toString().split(" : ");
        this.removeEdge(split[0].substring(1),split[1].substring(0,split[1].length()-1));
    }
    public void addRandomPrecalculated(){
        if(this.missingEdgeMap.size()==0)
            return;
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
        this.vertexChange.put(source,1+this.vertexChange.getOrDefault(source,0));
        this.vertexChange.put(target,1+this.vertexChange.getOrDefault(target,0));
        this.jungContained.addEdge("("+source+" : "+target+")",source,target);
        this.jgraphContained.addEdge(source,target);
        this.edgeMap.add("("+source+" : "+target+")");
        this.mutatorsApplied[0]++;
        if(this.missingEdgeMap!=null)
            this.missingEdgeMap.remove("("+source+" : "+target+")");
    }
    private void removeEdge(String source,String target){
        if(this.missingEdgeMap!=null&&this.missingEdgeMap.size()==0){
            this.filled = true;
            return;
        }
        this.vertexChange.put(source,1+this.vertexChange.getOrDefault(source,0));
        this.vertexChange.put(target,1+this.vertexChange.getOrDefault(target,0));
        this.jungContained.removeEdge("("+source+" : "+target+")");
        this.jgraphContained.removeEdge(source,target);
        this.edgeMap.remove("("+source+" : "+target+")");
        this.mutatorsApplied[1]++;
        if(this.missingEdgeMap!=null)
            this.missingEdgeMap.add("("+source+" : "+target+")");
    }
    private boolean edgeContained(String source, String target){
        return this.edgeMap.contains("("+source+" : "+target+")") ||
                this.edgeMap.contains("("+target+" : "+source+")");
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
    private Mutator[] getAddMutators(double amount){
        Mutator[] result = new Mutator[(int)amount];
        Random generator = new Random();
        for(int i=0;i<amount;i++){
            result[i] = Mutator.values()[0];
        }
        return result;
    }
    private Mutator[] getRemoveMutators(double amount){
        Mutator[] result = new Mutator[(int)amount];
        Random generator = new Random();
        for(int i=0;i<amount;i++){
            result[i] = Mutator.values()[1];
        }
        return result;
    }
    public void applyRandomByPercentVert(double pers){
        this.applyMutators(this.getRandomMutators((int)(this.jgraphContained.vertexSet().size()*pers),2));
    }
    public void applyRandomByPercentEdge(double pers){
        this.applyMutators(this.getRandomMutators((int)(this.PRIOR_EDG*pers),2));
    }
    public void applyAddOnlyByPercentEdge(double pers){
        this.applyMutators(this.getAddMutators((int)(this.PRIOR_EDG*pers)));
    }
    public void applyAddOnlyByAmount(int amount){
        this.applyMutators(this.getAddMutators(amount));
    }
    public void applyRemoveOnlyByPercentEdge(double pers){
        this.applyMutators(this.getRemoveMutators((int)(this.PRIOR_EDG*pers)));
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
    public Map getVerticeChanges(){
        HashMap<String,Double> result = new HashMap<>();
        for(Object vert:this.jgraphContained.vertexSet()){
            result.put(vert.toString(),(double)vertexChange.getOrDefault(vert.toString(),0));
        }
        return result;
    }
    public Map getStartTime(){
        HashMap<String,Long> result = new HashMap<>();
        for(Object vert :this.jgraphContained.vertexSet()){
            result.put(vert.toString(),this.startTime);
        }
        return result;
    }
    public Map getVerticesAmount(){
        HashMap<String,Integer> result = new HashMap<>();
        for(Object vert :this.jgraphContained.vertexSet()){
            result.put(vert.toString(),this.jgraphContained.vertexSet().size());
        }
        return result;
    }
    public Map getPriorEdges(){
        HashMap<String,Integer> result = new HashMap<>();
        for(Object vert :this.jgraphContained.vertexSet()){
            result.put(vert.toString(),this.PRIOR_EDG);
        }
        return result;
    }
    public Map getPostEdges(){
        HashMap<String,Integer> result = new HashMap<>();
        for(Object vert :this.jgraphContained.vertexSet()){
            result.put(vert.toString(),this.jgraphContained.edgeSet().size());
        }
        return result;
    }
    public Map getRemovedAmount(){
        HashMap<String,Integer> result = new HashMap<>();
        for(Object vert :this.jgraphContained.vertexSet()){
            result.put(vert.toString(),this.mutatorsApplied[1]);
        }
        return result;
    }
    public Map getAddedAmount(){
        HashMap<String,Integer> result = new HashMap<>();
        for(Object vert :this.jgraphContained.vertexSet()){
            result.put(vert.toString(),this.mutatorsApplied[0]);
        }
        return result;
    }
    public Map getAddedPercent(){
        HashMap<String,Double> result = new HashMap<>();
        for(Object vert :this.jgraphContained.vertexSet()){
            result.put(vert.toString(),(double)this.mutatorsApplied[0]/this.PRIOR_EDG);
        }
        return result;
    }
    public Map getRemovedPercent(){
        HashMap<String,Double> result = new HashMap<>();
        for(Object vert :this.jgraphContained.vertexSet()){
            result.put(vert.toString(),(double)this.mutatorsApplied[1]/this.PRIOR_EDG);
        }
        return result;
    }
    public List<Map> getMetadata(){
        ArrayList<Map> result = new ArrayList<>();
        result.add(this.getStartTime());
        result.add(this.getVerticeChanges());
        result.add(this.getVerticesAmount());
        result.add(this.getPriorEdges());
        result.add(this.getPostEdges());
        result.add(this.getAddedAmount());
        result.add(this.getRemovedAmount());
        result.add(this.getAddedPercent());
        result.add(this.getRemovedPercent());
        return result;
    }
    public Graph getJgraphContained(){
        return this.jgraphContained;
    }
    public UndirectedSparseGraph getJungContained() {
        return jungContained;
    }
    public String getName() {
        return name;
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
}
