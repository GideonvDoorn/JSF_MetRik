package Console.calculate;


import Shared.EdgeSide;
import Shared.KochFractal;
import timeutil.*;
import Shared.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KochManager{

    private List<Edge> edgeArrayList = new ArrayList<>();
    private long previousDuration;
    private int currentLevel;
    private int count = 0;
    KochFractal koch = new KochFractal();

    ExecutorService pool = Executors.newFixedThreadPool(3);

    private GenerateEdge genLeft;
    private GenerateEdge genBottom;
    private GenerateEdge genRight;
    private TimeStamp tsg;

    public boolean generated = false;

    public List<Edge> getEdges(){
        return edgeArrayList;
    }

    public KochManager(){
    }

    public List<Edge> generateLevel(int level){
        koch.cancel();
        count = 0;
        koch.setLevel(level);
        edgeArrayList.clear();

        genLeft = new GenerateEdge(EdgeSide.Left,level,this);
        genBottom = new GenerateEdge(EdgeSide.Bottom,level,this);
        genRight = new GenerateEdge(EdgeSide.Right,level,this);

        edgeArrayList.addAll(genLeft.generate());
        edgeArrayList.addAll(genRight.generate());
        edgeArrayList.addAll(genBottom.generate());


        return edgeArrayList;
    }
}
