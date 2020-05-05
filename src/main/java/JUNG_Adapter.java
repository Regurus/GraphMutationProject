import com.google.common.eventbus.DeadEvent;
import org.jgrapht.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Set;


public class JUNG_Adapter {

    public static UndirectedSparseGraph getJungGraph(Graph graph){
        Set<String> verts = graph.vertexSet();
        Set<DefaultEdge> edges = graph.edgeSet();
        UndirectedSparseGraph<String, String> g = new UndirectedSparseGraph<String, String>();
        // Add some vertices. From above we defined these to be type Integer.
        for(String vert:verts){
            g.addVertex(vert);
        }
        // Add some edges. From above we defined these to be of type String
        // Note that the default is for undirected edges.
        for(DefaultEdge edge:edges){
            String edgeRepr = edge.toString();
            edgeRepr = edgeRepr.substring(1,edgeRepr.length()-1);
            String[] vs = edgeRepr.split(" : ");
            g.addEdge(edge.toString(),vs[0],vs[1]);
        }
        return g;
    }
}
