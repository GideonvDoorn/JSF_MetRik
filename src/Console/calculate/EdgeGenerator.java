package Console.calculate;

import Console.timeutil.TimeStamp;

import java.io.*;
import java.util.List;
import java.util.Scanner;

class EdgeGenerator{

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

                        EDGEFILE = "edge" + i + ".txt";

                        eg.WriteEdgesTextBuffer(edges);

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





                System.out.print("Enter file writing method [text, byte, textbuffer, bytebuffer]: ");
                String writeInput = scanner.nextLine();


                if ("text".equals(writeInput)) {
                    EDGEFILE = "edge" + inputLevel + ".txt";
                    eg.ts = new TimeStamp();
                    System.out.println("Writing edge to file via " + writeInput + "...\n");
                    eg.WriteEdgesText(edges);
                }
                else if ("byte".equals(writeInput)) {
                    EDGEFILE = "edge" + inputLevel + ".byte";
                    eg.ts = new TimeStamp();

                    System.out.println("Writing edge to file via " + writeInput + "...\n");
                    eg.WriteEdgesByte(edges);
                }
                else if ("textbuffer".equals(writeInput)) {
                    EDGEFILE = "edge" + inputLevel + ".txt";
                    eg.ts = new TimeStamp();
                    System.out.println("Writing edge to file via " + writeInput + "...\n");
                    eg.WriteEdgesTextBuffer(edges);
                }
                else if ("bytebuffer".equals(writeInput)) {
                    EDGEFILE = "edge" + inputLevel + ".byte";
                    eg.ts = new TimeStamp();
                    System.out.println("Writing edge to file via " + writeInput + "...\n");
                    eg.WriteEdgesByteBuffer(edges);
                }
                else{
                    System.out.println("ERROR, please enter a valid method");
                    break;
                }

                System.out.println("Edge generation succesfull!\n");
                System.out.println("Writing took " + eg.ts.getLength() + " MS");
                System.out.println("--------------------------\n");
            }
        }
        scanner.close();
        System.exit(1);
    }

    public void WriteEdgesByte(List<Edge> edges){

        FileOutputStream fos = null;
        DataOutputStream dos = null;


        try  {
            fos = new FileOutputStream(EDGEFILE);
            dos = new DataOutputStream(fos);

            ts.setBegin("WriteByte start");
            for (Edge e : edges){
                // schrijf velden van edge
                dos.writeDouble(e.X1);
                dos.writeDouble(e.X2);
                dos.writeDouble(e.Y1);
                dos.writeDouble(e.Y2);
                dos.writeUTF(e.color.toString());
            }
            ts.setEnd("WriteByte end" );

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {

                if (dos != null)
                    dos.close();

                if (fos != null)
                    fos.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

    }

    public void WriteEdgesByteBuffer(List<Edge> edges){

        //Werkt niet toppie

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;


        try {
            fos = new FileOutputStream(EDGEFILE);
            bos = new BufferedOutputStream(fos);

            ts.setBegin("WriteByteBuffer start");
            for (Edge e : edges){
                // schrijf velden van edge

                bos.write(convertDoubleToByteArray(e.X1));
                bos.write(convertDoubleToByteArray(e.X2));
                bos.write(convertDoubleToByteArray(e.Y1));
                bos.write(convertDoubleToByteArray(e.Y2));

                String s = e.color.toString();
                bos.write(s.getBytes());

            }
            ts.setEnd("WriteByteBuffer end");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {

                if (bos != null){
                    bos.flush();
                    bos.close();

                }


            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }

    public void WriteEdgesText(List<Edge> edges) {


        FileWriter fw = null;

        try{
            fw = new FileWriter(EDGEFILE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            ts.setBegin("WriteText start");
            for (Edge edge : edges) {
                fw.write(String.format(edge.toString() + "%n", System.lineSeparator()));
            }
            ts.setEnd("WriteText end");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            try {


                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }



    public void WriteEdgesTextBuffer(List<Edge> edges) {

        BufferedWriter bw = null;
        FileWriter fw = null;
        try{
            fw = new FileWriter(EDGEFILE);
            bw = new BufferedWriter(fw);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

        try {
            ts.setBegin("WriteTextBuffer start");
            for (Edge edge : edges) {
                bw.write(String.format(edge.toString() + "%n", System.lineSeparator()));
            }
            ts.setEnd("WriteTextBuffer end");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

    }

    private byte[] convertDoubleToByteArray(double toConvert){
        double d = toConvert;
        byte[] output = new byte[8];
        long lng = Double.doubleToLongBits(d);
        for(int i = 0; i < 8; i++) output[i] = (byte)((lng >> ((7 - i) * 8)) & 0xff);

        return output;
    }

}

