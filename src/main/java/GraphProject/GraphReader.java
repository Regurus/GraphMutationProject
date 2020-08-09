package GraphProject;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import static org.jgrapht.util.SupplierUtil.createStringSupplier;

public class GraphReader {
    public GraphReader() {

    }
    public DefaultUndirectedGraph getGraph(String path, String separator){
        HashSet<String> edges = preprocessGraphFile(path,separator);
        HashSet<String> filtered = filterDuplicates(edges);
        HashSet<String> vertexes = getVertexes(filtered);
        DefaultUndirectedGraph<String, DefaultEdge> graph = new DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        graph.setVertexSupplier(createStringSupplier());
        for(String vert : vertexes){
            graph.addVertex(vert);
        }
        for(String edge : filtered){
            String[] split = edge.split(":");
            if(split.length<2)
                continue;
            graph.addEdge(split[0],split[1]);
        }
        return graph;
    }

    private HashSet<String> filterDuplicates(HashSet<String> edges){
        HashSet<String> duplicateList = new HashSet<>();
        HashSet<String> lines = new HashSet<>();
        for(String next:edges){
            String[] split = next.split(":");
            if(split[0].equals(split[1]))
                continue;
            if(duplicateList.contains(split[0]+":"+split[1])||duplicateList.contains(split[1]+":"+split[0])){
                continue;
            }
            lines.add(split[0]+":"+split[1]);
            duplicateList.add(split[1]+":"+split[0]);
        }
        return lines;
    }

    private HashSet<String> preprocessGraphFile(String path,String delimiter){
        HashSet<String> edges = new HashSet<>();
        try {
            String nextLine;
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((nextLine = br.readLine()) != null) {
                String[] split = nextLine.split(delimiter);
                if(split.length<=1)
                    continue;
                if(split[0]==split[1])
                    System.out.println("repeat");
                edges.add(split[0]+":"+split[1]);
                edges.add(split[1]+":"+split[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return edges;
    }

    private HashSet<String> getVertexes(HashSet<String> edges){
        HashSet<String> verts = new HashSet<>();
        for(String edge:edges){
            String[] split = edge.split(":");
            if(split.length<2)
                continue;
            verts.add(split[0]);
            verts.add(split[1]);
        }
        return verts;
    }
}
