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

        //Mapping a file into memory
        FileChannel fc = memoryMappedFile.getChannel();
        MappedByteBuffer out = null;
        try {
            out = fc.map(FileChannel.MapMode.READ_ONLY, 0, 10000);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        int byteSizePrediction = 0;
        for (int i = 0; i < koch.getNrOfEdges(); i++) {
            //double is 8 bytes
            //color = 10 bytes?

            Color color = Color.RED;
            byteSizePrediction += (32 + color.toString().length());
            //System.out.println(edge.color.toString());
        }

        List<Edge> edges = new ArrayList<>();

//        //reading bytes from memory file
//        for (int currentedge = 0; currentedge < koch.getNrOfEdges(); currentedge++) {
//            byte[] X1 = new byte[8];
//            for (int n = 0; n < 8; n++) {
//                X1[n] = out.get(((currentedge) * 8) + n);
//            }
//            byte[] Y1 = new byte[8];
//            for (int n = 0; n < 8; n++) {
//                Y1[n] = out.get((currentedge + 1) * 8 + n);
//            }
//            byte[] X2 = new byte[8];
//            for (int n = 0; n < 8; n++) {
//                X2[n] = out.get((currentedge + 2) * 8 + n);
//            }
//            byte[] Y2 = new byte[8];
//            for (int n = 0; n < 8; n++) {
//                Y2[n] = out.get((currentedge + 3) * 8 + n);
//            }
//
//            out.getDouble();
//            System.out.println(toDouble());
//            byte[] Y1 = new byte[8];
//            for (int n = 9; n < 17; n++) {
//                Y1[n - 9] = out.get((currentedge * 8) + n);
//            }
//            byte[] X2 = new byte[8];
//            for (int n = 17; n < 25; n++) {
//                X2[n - 17] = out.get((currentedge * 8) + n);
//            }
//            byte[] Y2 = new byte[8];
//            for (int n = 25; n < 33; n++) {
//                Y2[n - 25] = out.get((currentedge * 8) + n);
//            }
//            byte[] color = new byte[10];
//            for (int n = 0; n < 10; n++) {
//                color[n] = out.get((currentedge * 8) + n);
//            }
//
//            double double1 = toDouble(X1);
//            double double2 = toDouble(X2);
//            double double3 = toDouble(Y1);
//            double double4 = toDouble(Y2);
//
//            System.out.println(double1 + "  ,  " + double2 + "  ,  " + double3 + "  ,  " + double4);
//
////          String colorString = new String(color);
////          System.out.println(colorString);
//            edges.add(new Edge(double1, double2, double3, double4, Color.RED));
//
//        }

        for (int currentedge = 0; currentedge < koch.getNrOfEdges(); currentedge++) {
            double double1 = 0;
            double double2 = 0;
            double double3 = 0;
            double double4 = 0;
//
//            if(currentedge == 0){
//                double1 = out.getDouble();
//            }

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
