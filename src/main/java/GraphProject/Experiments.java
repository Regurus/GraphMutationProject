package GraphProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Experiments {
    private static String savePath;
    static int done;
    public static void randomExperiment(Map<String,Double> vars,Object[] paths,String path){
        savePath = path;
        ArrayList<MutatableGraph> graphs = getBarabasis(vars.get("bar_a"),vars.get("bar_n"),vars.get("bar_m"));
        graphs = getPetersens(vars.get("pet_a"),vars.get("pet_n"),vars.get("pet_k"),graphs);
        graphs = getHypercubes(vars.get("hyp_a"),vars.get("hyp_dim"),graphs);
        graphs = getStrogatz(vars.get("strog_a"),vars.get("strog_n"),vars.get("strog_p"),
                vars.get("strog_k"),graphs);
        graphs = getErdosh(vars.get("erd_a"),vars.get("erd_m"),vars.get("erd_n"),graphs);
        graphs = getWheels(vars.get("whe_a"),vars.get("whe_sz"),graphs);
        graphs = getCustoms(paths,graphs);
        performExp(vars.get("ex_rnd_vert_inp"),vars.get("ex_rnd_edg_inp"),vars.get("ex_rnd_const_inp"),
                vars.get("threads"),graphs);
    }

    public static ArrayList<MutatableGraph> getBarabasis(double a, double n, double m){
        ArrayList result = new ArrayList();
        for(int i=0;i<a;i++){
            result.add(MutatableGraphFactory.getBarabasiGraph(m,n));
        }
        return result;
    }

    public static ArrayList<MutatableGraph> getErdosh(double a, double m, double n, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getErdoshGraph(n,m));
        }
        return list;
    }

    public static ArrayList<MutatableGraph> getStrogatz(double a, double n, double p, double k, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getStrogatzGraph(n,k,p));
        }
        return list;
    }

    public static ArrayList<MutatableGraph> getPetersens(double a,double n,double k, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getGeneralizedPetersenGraph(n,k));
        }
        return list;
    }

    public static ArrayList<MutatableGraph> getHypercubes(double a,double dim, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getHyperCubeGraph(dim));
        }
        return list;
    }

    public static ArrayList<MutatableGraph> getWheels(double a,double sz, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getWheelGraph(sz));
        }
        return list;
    }

    public static ArrayList<MutatableGraph> getCustoms(Object[] paths, ArrayList<MutatableGraph> list){
        for(int i=0;i<paths.length;i++){
            if(paths[i].toString().substring(paths[i].toString().length()-3).equals("csv")){
                MutatableGraph graph = IOUtils.fileToGraph(paths[i].toString());
                list.add(graph);
            }
            else{
                MutatableGraph graph = IOUtils.fileToGraphEdges(paths[i].toString());
                list.add(graph);
            }
        }
        return list;
    }

    public static void performExp(double vertP,double edgeP,double amount,double threads,ArrayList<MutatableGraph> graphs){
        ExecutorService pool = Executors.newFixedThreadPool((int)threads);
        for(int i=0;i<graphs.size();i++){
            int ins = i;
            pool.submit(() -> {
                runSingle(graphs.get(ins),vertP,edgeP,amount,ins);
                Experiments.done++;
                UIController.progress.setValue((double)done/UIController.jobs);
            });
        }
        try {
            pool.awaitTermination(60, TimeUnit.SECONDS);
            UIController.isDone.setValue(false);
        } catch (InterruptedException e) {
            System.out.println("Pool interrupted exception!");
        }
    }

    public static void runSingle( MutatableGraph graph,double vertP,double edgeP,double amount,double instance){
        ORCA_Adapter orca = new ORCA_Adapter("node",5,""+instance);
        String graphName = ""+graph.getName();
        List<String> names = Scores.getScoreNames(orca);
        List<Map> old_scores = Scores.scoreGraph(graph,orca);

        if(UIController.graphSavePre.get())
            saveGraph(graph,graphName+"_pre");

        if(vertP!=0)
            graph.applyRandomByPercentVert(vertP);
        else if(edgeP!=0)
            graph.applyRandomByPercentEdge(edgeP);
        else if(amount!=0)
            graph.applyRandomByAmount(amount);

        if(UIController.graphSavePost.get())
            saveGraph(graph,graphName+"_post");

        List<Map> new_scores = Scores.scoreGraph(graph,orca);
        List<Map> meta = graph.getMetadata();
        IOUtils.saveRunResults(new_scores,old_scores,meta,names,graphName,savePath);
    }

    public static void saveGraph(MutatableGraph graph, String name){
        IOUtils.graphToFile(graph,UIController.graphSaveFolder.get()+'\\'+name+".csv");
    }


}
