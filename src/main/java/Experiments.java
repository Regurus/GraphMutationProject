import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Experiments {
    private static String savePath;
    static int done;
    public static void randomExperiment(Map<String,Integer> vars,Object[] paths,String path){
        savePath = path;
        ArrayList<MutatableGraph> graphs = getBarabasis(vars.get("bar_a"),vars.get("bar_n"),vars.get("bar_m"));
        graphs = getPetersens(vars.get("pet_a"),vars.get("pet_n"),vars.get("pet_k"),graphs);
        graphs = getHypercubes(vars.get("hyp_a"),vars.get("hyp_dim"),graphs);
        graphs = getKleinbergs(vars.get("kle_a"),vars.get("kle_n"),vars.get("kle_p"),
                vars.get("kle_q"),vars.get("kle_r"),graphs);
        graphs = getWindmills(vars.get("wind_a"),vars.get("wind_m"),vars.get("wind_n"),graphs);
        graphs = getWheels(vars.get("whe_a"),vars.get("whe_sz"),graphs);
        graphs = getCustoms(paths,graphs);
        performExp(vars.get("ex_rnd_vert_inp"),vars.get("ex_rnd_edg_inp"),vars.get("ex_rnd_const_inp"),
                vars.get("threads"),graphs);
    }

    private static ArrayList<MutatableGraph> getBarabasis(int a, int n, int m){
        ArrayList result = new ArrayList();
        for(int i=0;i<a;i++){
            result.add(MutatableGraphFactory.getBarabasiGraph(m,n));
        }
        return result;
    }

    private static ArrayList<MutatableGraph> getPetersens(int a,int n,int k, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getGeneralizedPetersenGraph(n,k));
        }
        return list;
    }

    private static ArrayList<MutatableGraph> getHypercubes(int a,int dim, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getHyperCubeGraph(dim));
        }
        return list;
    }

    private static ArrayList<MutatableGraph> getKleinbergs(int a,int n,int p,int q,int r, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getKleinbergSmallWorldGraph(n,p,q,r));
        }
        return list;
    }

    private static ArrayList<MutatableGraph> getWindmills(int a,int m,int n, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getWindmillGraph(m,n));
        }
        return list;
    }

    private static ArrayList<MutatableGraph> getWheels(int a,int sz, ArrayList<MutatableGraph> list){
        for(int i=0;i<a;i++){
            list.add(MutatableGraphFactory.getWheelGraph(sz));
        }
        return list;
    }

    private static ArrayList<MutatableGraph> getCustoms(Object[] paths, ArrayList<MutatableGraph> list){
        for(int i=0;i<paths.length;i++){
            list.add(IOUtils.fileToGraph(paths[i].toString()));
        }
        return list;
    }

    private static void performExp(double vertP,double edgeP,int amount,int threads,ArrayList<MutatableGraph> graphs){
        ExecutorService pool = Executors.newFixedThreadPool(threads);
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

    private static void runSingle( MutatableGraph graph,double vertP,double edgeP,int amount,int instance){
        ORCA_Adapter orca = new ORCA_Adapter("node",5,""+instance);
        List<String> names = Scores.getScoreNames(orca);
        List<Map> old_scores = Scores.scoreGraph(graph,orca);
        if(vertP!=0)
            graph.applyRandomByPercentVert(vertP);
        else if(edgeP!=0)
            graph.applyRandomByPercentEdge(edgeP);
        else if(amount!=0)
            graph.applyRandomByAmount(amount);
        List<Map> new_scores = Scores.scoreGraph(graph,orca);
        IOUtils.saveRunResults(new_scores,old_scores,names,""+graph.hashCode(),savePath);
    }


}
