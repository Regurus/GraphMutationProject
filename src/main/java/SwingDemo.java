import javax.swing.JFrame;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import com.mxgraph.layout.*;

public class SwingDemo extends JFrame
{

    /**
     *
     */
    private static final long serialVersionUID = -2707712944901661771L;

    public SwingDemo()
    {
        super("Hello, World!");

        Graph g = MutatableGraphFactory.getBarabasiGraph(2,100).getJgraphContained();

        // create a visualization using JGraph, via an adapter
        JGraphXAdapter<String, DefaultEdge> jgxAdapter = new JGraphXAdapter<>(g);

        mxGraph graph = jgxAdapter;
        Object parent = graph.getDefaultParent();

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);
        graphComponent.getGraph().setAllowDanglingEdges(false);
        getContentPane().add(graphComponent);
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        int radius = 50;
        layout.setX0((800 / 4.0) - radius);
        layout.setY0((800 / 4.0) - radius);
        layout.setRadius(radius);
        layout.setMoveCircle(true);
        layout.execute(jgxAdapter.getDefaultParent());
    }

    public static void main(String[] args)
    {
        SwingDemo frame = new SwingDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }

}