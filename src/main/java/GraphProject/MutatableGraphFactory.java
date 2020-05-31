package GraphProject;

import org.jgrapht.generate.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import static org.jgrapht.util.SupplierUtil.createStringSupplier;

public class MutatableGraphFactory {

    public static MutatableGraph getBarabasiGraph(double m,double n){
        BarabasiAlbertGraphGenerator generator = new BarabasiAlbertGraphGenerator((int)m,(int)m,(int)n);
        return getGraph(generator);
    }
    public static MutatableGraph getGeneralizedPetersenGraph(double n,double k){
        GraphGenerator generator = new GeneralizedPetersenGraphGenerator((int)n,(int)k);
        return getGraph(generator);
    }
    public static MutatableGraph getHyperCubeGraph(double dim){
        GraphGenerator generator = new HyperCubeGraphGenerator((int)dim);
        return getGraph(generator);
    }

    public static MutatableGraph getWheelGraph(double size){
        GraphGenerator generator = new WheelGraphGenerator((int)size);
        return getGraph(generator);
    }
    public static MutatableGraph getErdoshGraph(double n,double m){
        GraphGenerator generator = new GnmRandomGraphGenerator((int)n,(int)m);
        return getGraph(generator);
    }
    public static MutatableGraph getStrogatzGraph(double n,double k,double p){
        GraphGenerator generator = new WattsStrogatzGraphGenerator((int)n,(int)k,p);
        return getGraph(generator);
    }
    private static MutatableGraph getGraph(GraphGenerator generator){
        DefaultUndirectedGraph<String, DefaultEdge> result = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        result.setVertexSupplier(createStringSupplier());
        generator.generateGraph(result,null);
        return new MutatableGraph(result);
    }

}
