package GUI.calculate;

import GUI.jsf31kochfractalfx.JSF31KochFractalFX;
import Shared.KochFractal;
import timeutil.TimeStamp;
import Shared.Edge;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KochManager{

    private static String EDGETXTFILE = "";

    private List<Edge> edgeArrayList = new ArrayList<>();
    private JSF31KochFractalFX application;
    private long previousDuration;
    private int currentLevel;
    private int count = 0;
    KochFractal koch = new KochFractal();

    ExecutorService pool = Executors.newFixedThreadPool(3);

    private Task<List<Edge>> TaskLeft;
    private Task<List<Edge>> TaskBottom;
    private Task<List<Edge>> TaskRight;

    private TimeStamp tsg;
    private Thread leftThread = new Thread();
    private Thread bottomThread = new Thread();
    private Thread rightThread = new Thread();

    public KochManager(JSF31KochFractalFX application){
        this.application = application;
    }

    public void drawEdges(){

        application.clearKochPanel();
        TimeStamp ts = new TimeStamp();
        ts.setBegin();
        for(Edge edge : edgeArrayList){
            application.drawEdge(edge, false);
        }
        ts.setEnd();
        previousDuration = ts.getLength();
        application.setTextDraw(ts.toString());
    }

    public void changeLevel(int nxt){


        koch.cancel();
        count = 0;
        edgeArrayList.clear();


        koch.setLevel(nxt);


        tsg = new TimeStamp();
        tsg.setBegin();


        edgeArrayList = addEdges(nxt);

        application.requestDrawEdges();
    }



    public List<Edge> addEdges(int level) {

        List<Edge> result = new ArrayList<Edge>();
        //result = readEdgesByte(level);
        result = ReadEdgesByte(level);

        return result;



    }

//
//    private List<Edge> readEdgesByte(int level) {
//        EDGETXTFILE = "edge" + level + ".byte";
//        List<Edge> returnvalue = new ArrayList<>();
//
//        try (DataInputStream dis = new DataInputStream(new FileInputStream(EDGETXTFILE))){
//
//            TimeStamp ts = new TimeStamp();
//            ts.setBegin();
//            for(int i = 0; i < koch.getNrOfEdges(); i++){
//                double X1 = dis.readDouble();
//                double X2 = dis.readDouble();
//                double Y1 = dis.readDouble();
//                double Y2 = dis.readDouble();
//
//
//                String color = dis.readUTF();
//                Color edgeColor = Color.valueOf(color);
//                returnvalue.add(new Edge(X1, Y1, X2, Y2, edgeColor));
//            }
//            ts.setEnd();
//            System.out.println("File reading took: " + ts.getLength());
//
//        }         catch (FileNotFoundException ex){
//            System.out.println("No file found for this combination, please generate using console application!");
//        }  catch (IOException e) {
//            e.printStackTrace();
//        }
//        return returnvalue;
//    }

    public List<Edge> ReadEdgesByte( int level ) {

        RandomAccessFile memoryMappedFile = null;
        try {
            memoryMappedFile = new RandomAccessFile("EDGE.dat", "r");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int byteSizePrediction = 0;
        for(int i = 0; i < koch.getNrOfEdges(); i++){
            //double is 8 bytes
            //color = 10 bytes / color.tostring.length
            Color c = Color.RED;
            byteSizePrediction += (32 + c.toString().length());
        }


        //Mapping a file into memory
        FileChannel fc = memoryMappedFile.getChannel();
        MappedByteBuffer out = null;
        try {
            out = fc.map(FileChannel.MapMode.READ_ONLY, 0, byteSizePrediction);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        List<Edge> edges = new ArrayList<>();

        for (int currentedge = 0; currentedge < koch.getNrOfEdges(); currentedge++) {
            double double1 = 0;
            double double2 = 0;
            double double3 = 0;
            double double4 = 0;

            double1 = out.getDouble();
            double2 = out.getDouble();
            double3 = out.getDouble();
            double4 = out.getDouble();

            String s;
            byte[] bytes = new byte[10];
            out.get(bytes);
            s = new String(bytes);

            Color c = Color.valueOf(s);

            System.out.println("edge: " + currentedge + " - " + double1 + "  ,  " + double2 + "  ,  " + double3 + "  ,  " + double4);

            edges.add(new Edge(double1, double2, double3, double4, c));
        }

        System.out.println("\nReading from Memory Mapped File is completed");
        return edges;
    }


    public static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}
