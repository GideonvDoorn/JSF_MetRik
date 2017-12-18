package GUI.calculate;//package calculate;
//
//import javafx.application.Application;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//
//public class GenerateEdges implements Runnable {
//
//
//    private int nxt;
//    private KochManager km;
//    private ArrayList<Edge> edges = new ArrayList<>();
//    private Application UIElements;
//
//    GenerateEdge GenLeftEdge;
//    GenerateEdge GenBottomEdge;
//    GenerateEdge GenRightEdge;
//
//    List<Edge> leftEdges;
//
//    public GenerateEdges(int nxt, KochManager km, Application UIElements){
//
//        this.nxt = nxt;
//        this.km = km;
//        this.UIElements = UIElements;
//    }
//
//    @Override
//    public void run() {
//
//        ExecutorService pool = km.getPool();
//
//        CreateTasks();
//
//        pool.submit(new GenerateEdge(EdgeSide.Right,nxt,km));
//        pool.submit(new GenerateEdge(EdgeSide.Bottom,nxt,km));
//        pool.submit(new GenerateEdge(EdgeSide.Left,nxt,km));
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    leftEdges = GenLeftEdge.get();
//                    List<Edge> bottomEdges = GenBottomEdge.get();
//                    List<Edge> rightEdges = GenRightEdge.get();
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        try{
//            edges.addAll(GenLeftEdge.get());
//            edges.addAll(GenBottomEdge.get());
//            edges.addAll(GenRightEdge.get());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        km.addEdges(leftEdges);
//    }
//
//    private void CreateTasks(){
//
//        GenLeftEdge = new GenerateEdge(EdgeSide.Left,nxt,km);
//        GenBottomEdge = new GenerateEdge(EdgeSide.Bottom,nxt,km);
//        GenRightEdge = new GenerateEdge(EdgeSide.Right,nxt,km);
//    }
//
//}
