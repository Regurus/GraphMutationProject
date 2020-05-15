import org.jgrapht.generate.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import static org.jgrapht.util.SupplierUtil.createStringSupplier;

public class MutatableGraphFactory {

    public static MutatableGraph getBarabasiGraph(int m,int n){
        BarabasiAlbertGraphGenerator generator = new BarabasiAlbertGraphGenerator(m,m,n);
        return getGraph(generator);
    }
    public static MutatableGraph getGeneralizedPetersenGraph(int n,int k){
        GraphGenerator generator = new GeneralizedPetersenGraphGenerator(n,k);
        return getGraph(generator);
    }
    public static MutatableGraph getHyperCubeGraph(int dim){
        GraphGenerator generator = new HyperCubeGraphGenerator(dim);
        return getGraph(generator);
    }
    public static MutatableGraph getKleinbergSmallWorldGraph(int n,int p,int q,int r){
        GraphGenerator generator = new KleinbergSmallWorldGraphGenerator(n,p,q,r);
        return getGraph(generator);
    }
    public static MutatableGraph getWindmillGraph(int m,int n){
        GraphGenerator generator = new WindmillGraphsGenerator(WindmillGraphsGenerator.Mode.WINDMILL,m,n);
        return getGraph(generator);
    }
    public static MutatableGraph getWheelGraph(int size){
        GraphGenerator generator = new WheelGraphGenerator(size);
        return getGraph(generator);
    }
    private static MutatableGraph getGraph(GraphGenerator generator){
        DefaultUndirectedGraph<String, DefaultEdge> result = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        result.setVertexSupplier(createStringSupplier());
        generator.generateGraph(result,null);
        return new MutatableGraph(result);
    }

}
