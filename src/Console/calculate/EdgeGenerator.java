package Console.calculate;

import javafx.scene.paint.Color;
import timeutil.*;
import Shared.Edge;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Scanner;

class EdgeGenerator{

    //Edge generator console app

    private static String EDGEFILE  = "";


    TimeStamp ts = null;

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        KochManager km = new KochManager();
        List<Edge> edges = null;
        boolean exit = false;
        EdgeGenerator eg = new EdgeGenerator();
        while(!exit){
            while (true) {
                System.out.print("Welcome! (Type [exit] to exit application)\n");
                System.out.print("Enter which level you want to generate [1-11] or [genall]: ");
                String input = scanner.nextLine();
                int inputLevel;

                if ("exit".equals(input)) {
                    System.out.println("Exit!");
                    exit = true;
                    break;
                }
                else if("genall".equals(input)){

                    eg.ts = new TimeStamp();
                    for (int i = 1; i < 12; i++){
                        System.out.println("Generating edges for level "+ i +"...\n");

                        edges = km.generateLevel(i);

                        System.out.println("Writing edges to file for level " + i + "...\n");


                        EDGEFILE = "edge" + i + ".byte";

                        eg.WriteEdgesByte(edges);

                    }
                    System.out.println("Generating and writing all files took: " + eg.ts.getLength());

                    break;

                }



                try{
                    inputLevel =Integer.parseInt(input);
                }
                catch(NumberFormatException ex){
                    System.out.println("ERROR, please enter a valid level");
                    break;
                }


                if (inputLevel > 0 && inputLevel < 11) {
                    System.out.println("Generating edge...\n");

                    edges = km.generateLevel(inputLevel);
                }
                else{
                    System.out.println("ERROR, please enter a valid level");
                    break;
                }

                    EDGEFILE = "edge" + inputLevel + ".byte";
                    eg.ts = new TimeStamp();

                    System.out.println("Writing edge to file...\n");
                    eg.WriteEdgesByte(edges);



                System.out.println("Edge generation succesfull!\n");
                System.out.println("Writing took " + eg.ts.getLength() + " MS");
                System.out.println("--------------------------\n");
            }
        }
        scanner.close();
        System.exit(1);
    }

    public void WriteEdgesByte(List<Edge> edges){

        RandomAccessFile memoryMappedFile = null;
        MappedByteBuffer out = null;

        try{
            memoryMappedFile = new RandomAccessFile("EDGE.dat", "rw");

        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }



        int byteSizePrediction = 0;
        for(Edge edge : edges){
            //double is 8 bytes
            //color = 10 bytes / color.tostring.length
            byteSizePrediction += (32 + edge.color.toString().length());
        }


        //Mapping a file into memory
        FileChannel fc = memoryMappedFile.getChannel();

        try{
            out = fc.map(FileChannel.MapMode.READ_WRITE, 0, byteSizePrediction);

        }
        catch (IOException ex){
            ex.printStackTrace();
        }


        //Writing into Memory Mapped File
        ts.setBegin("Writing Edges to Memory Mapped File...");
        System.out.println("Writing Edges to Memory Mapped File...");

        int currentedge = 0;
        for (Edge e : edges){
            // schrijf velden van edge
            out.put(convertDoubleToByteArray(e.X1));
            out.put(convertDoubleToByteArray(e.Y1));
            out.put(convertDoubleToByteArray(e.X2));
            out.put(convertDoubleToByteArray(e.Y2));
            out.put(e.color.toString().getBytes());

            System.out.println("edge: " + currentedge + " - " + e.X1 + "  ,  " + e.Y1 + "  ,  " + e.X2 + "  ,  " + e.Y2);

            currentedge++;
        }

        System.out.println("Writing Edges to Memory Mapped File is completed");
    }

    private byte[] convertDoubleToByteArray(double toConvert){
        double d = toConvert;
        byte[] output = new byte[8];
        long lng = Double.doubleToLongBits(d);
        for(int i = 0; i < 8; i++) output[i] = (byte)((lng >> ((7 - i) * 8)) & 0xff);

        return output;
    }

}

