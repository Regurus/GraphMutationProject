package GraphProject;

import org.jgrapht.generate.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.text.DecimalFormat;

import static org.jgrapht.util.SupplierUtil.createStringSupplier;

public class MutatableGraphFactory {
    public static int barCount = 0;
    public static int petCount = 0;
    public static int hypCount = 0;
    public static int wheCount = 0;
    public static int erdCount = 0;
    public static int strCount = 0;
    private static DecimalFormat df = new DecimalFormat("#.##");


    public static MutatableGraph getBarabasiGraph(double m,double n){
        BarabasiAlbertGraphGenerator generator = new BarabasiAlbertGraphGenerator((int)m,(int)m,(int)n);
        return getGraph(generator,"Barabasi(m="+(int)m+" n="+(int)n+")",barCount++);
    }
    public static MutatableGraph getGeneralizedPetersenGraph(double n,double k){
        GraphGenerator generator = new GeneralizedPetersenGraphGenerator((int)n,(int)k);
        return getGraph(generator,"Petersen(n="+(int)n+" k="+(int)k+")",petCount++);
    }
    public static MutatableGraph getHyperCubeGraph(double dim){
        GraphGenerator generator = new HyperCubeGraphGenerator((int)dim);
        return getGraph(generator,"Hypercube(dim="+(int)dim+")",hypCount++);
    }
    public static MutatableGraph getWheelGraph(double size){
        GraphGenerator generator = new WheelGraphGenerator((int)size);
        return getGraph(generator,"Wheel(size="+(int)size+")",wheCount++);
    }
    public static MutatableGraph getErdoshGraph(double n,double m){
        GraphGenerator generator = new GnmRandomGraphGenerator((int)n,(int)m);
        return getGraph(generator,"Erdosh-Renyi(n="+(int)n+" m="+(int)m+")",erdCount++);
    }
    public static MutatableGraph getStrogatzGraph(double n,double k,double p){
        GraphGenerator generator = new WattsStrogatzGraphGenerator((int)n,(int)k,p);
        return getGraph(generator,"Strogatz(n="+(int)n+" k="+(int)k+ "p="+df.format(p)+")", strCount++);
    }
    private static MutatableGraph getGraph(GraphGenerator generator,String name,int id){
        DefaultUndirectedGraph<String, DefaultEdge> result = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        result.setVertexSupplier(createStringSupplier());
        generator.generateGraph(result,null);
        return new MutatableGraph(result,name,id);
    }

}
