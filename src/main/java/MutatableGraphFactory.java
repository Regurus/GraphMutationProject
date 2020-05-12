import org.jgrapht.generate.BarabasiAlbertGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import static org.jgrapht.util.SupplierUtil.createStringSupplier;

public class MutatableGraphFactory {

    public static MutatableGraph getBarabasiGraph(int m,int n){
        BarabasiAlbertGraphGenerator generator = new BarabasiAlbertGraphGenerator(m,m,n);
        DefaultUndirectedGraph<String, DefaultEdge> target = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        target.setVertexSupplier(createStringSupplier());
        generator.generateGraph(target,null);
        return new MutatableGraph(target);
    }


}
