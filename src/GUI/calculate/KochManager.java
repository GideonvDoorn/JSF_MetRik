package GUI.calculate;

import GUI.jsf31kochfractalfx.JSF31KochFractalFX;
import GUI.timeutil.TimeStamp;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import shared.Edge;
import shared.FileType;
import shared.KochFractal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
        //pool = Executors.newFixedThreadPool(4);
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

    public void changeLevel(int nxt, FileType type, boolean buffer){


        koch.cancel();
        count = 0;
        edgeArrayList.clear();


        koch.setLevel(nxt);


        tsg = new TimeStamp();
        tsg.setBegin();


            edgeArrayList = addEdges(nxt, type, buffer);

        application.requestDrawEdges();
    }


    public synchronized void finishedAdding() {
        count++;

        if(count == 3){
            tsg.setEnd();
            application.setTextCalc(tsg.toString());
            application.clearKochPanel();
            try{
                edgeArrayList.addAll(TaskLeft.get());
                edgeArrayList.addAll(TaskBottom.get());
                edgeArrayList.addAll(TaskRight.get());
            }
            catch(InterruptedException ex){
                ex.printStackTrace();
            }
            catch(ExecutionException ex){
                ex.printStackTrace();
            }
            application.requestDrawEdges();
        }
    }

    public List<Edge> addEdges(int level, FileType type, boolean buffer) {

        List<Edge> result = new ArrayList<Edge>();


        if(type == FileType.TEXT){
            if(buffer){
                result = readEdgesTextBuffer(level);
            }
            else{
                result = readEdgesText(level);
            }
        }
        else if(type == FileType.BYTE){
            if(buffer){
                result = readEdgesByteBuffer(level);
            }
            else{
                result = readEdgesByte(level);
            }

        }
        return result;



    }



    public List<Edge> readEdgesText(int level) {
        EDGETXTFILE = "edge" + level + ".txt";
        List<Edge> returnvalue = new ArrayList<>();
        try (FileReader fr = new FileReader(EDGETXTFILE); Scanner inputScanner = new Scanner(fr)) {

            TimeStamp ts = new TimeStamp();
            ts.setBegin();
            while(inputScanner.hasNextLine()){
                String regel = inputScanner.nextLine();
                // split regel in velden, gescheiden door ,
                String[] velden = regel.split(",");
                // parse Strings naar de juiste waarden

                String X1 = velden[0];
                String X2 = velden[1];
                String Y1 = velden[2];
                String Y2 = velden[3];
                String color = velden[4];
                Color edgeColor = Color.valueOf(color);
                returnvalue.add(new Edge(Double.parseDouble(X1), Double.parseDouble(Y1), Double.parseDouble(X2), Double.parseDouble(Y2), edgeColor));
            }
            ts.setEnd();
            System.out.println("File reading took: " + ts.getLength());


        }
        catch (FileNotFoundException ex){
            System.out.println("No file found for this combination, please generate using console application!");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return returnvalue;
    }

    public List<Edge> readEdgesTextBuffer(int level) {
        EDGETXTFILE = "edge" + level + ".txt";
        List<Edge> returnvalue = new ArrayList<>();

        try  {
            FileReader fr = new FileReader(EDGETXTFILE);
            BufferedReader br = new BufferedReader(fr);


            TimeStamp ts = new TimeStamp();
            ts.setBegin();

            String line;
            while((line = br.readLine()) != null){
                // split regel in velden, gescheiden door ,
                String[] velden = line.split(",");
                // parse Strings naar de juiste waarden

                String X1 = velden[0];
                String X2 = velden[1];
                String Y1 = velden[2];
                String Y2 = velden[3];
                String color = velden[4];
                Color edgeColor = Color.valueOf(color);

                returnvalue.add(new Edge(Double.parseDouble(X1), Double.parseDouble(Y1), Double.parseDouble(X2), Double.parseDouble(Y2), edgeColor));
            }
            ts.setEnd();
            System.out.println("File reading took: " + ts.getLength());


        }
        catch (FileNotFoundException ex){
            System.out.println("No file found for this combination, please generate using console application!");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }


        return returnvalue;
    }

    public List<Edge> readEdgesByte(int level) {
        EDGETXTFILE = "edge" + level + ".byte";
        List<Edge> returnvalue = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(EDGETXTFILE))){

            TimeStamp ts = new TimeStamp();
            ts.setBegin();
            for(int i = 0; i < koch.getNrOfEdges(); i++){
                double X1 = dis.readDouble();
                double X2 = dis.readDouble();
                double Y1 = dis.readDouble();
                double Y2 = dis.readDouble();


                String color = dis.readUTF();
                Color edgeColor = Color.valueOf(color);
                returnvalue.add(new Edge(X1, Y1, X2, Y2, edgeColor));
            }
            ts.setEnd();
            System.out.println("File reading took: " + ts.getLength());

        }         catch (FileNotFoundException ex){
            System.out.println("No file found for this combination, please generate using console application!");
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }

    public List<Edge> readEdgesByteBuffer(int level) {
        EDGETXTFILE = "edge" + level + ".byte";
        List<Edge> returnvalue = new ArrayList<>();

        try {

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(EDGETXTFILE));
            DataInputStream dis = new DataInputStream(bis);

            TimeStamp ts = new TimeStamp();
            ts.setBegin();
            for(int i = 0; i < koch.getNrOfEdges(); i++){

                double X1 = dis.readDouble();
                double X2 = dis.readDouble();
                double Y1 = dis.readDouble();
                double Y2 = dis.readDouble();

                String color = dis.readUTF();
                Color edgeColor = Color.valueOf(color);

                returnvalue.add(new Edge(X1, Y1, X2, Y2, edgeColor));
            }
            ts.setEnd();
            System.out.println("File reading took: " + ts.getLength());

        }  catch (FileNotFoundException ex){
            System.out.println("No file found for this combination, please generate using console application!");
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }
}
