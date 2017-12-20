package Console.calculate;


import Shared.Edge;
import Shared.EdgeSide;
import Shared.KochFractal;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GenerateEdge implements Observer{

    private EdgeSide edge;
    private KochManager km;
    private List<Edge> edges = new ArrayList<>();
    private KochFractal koch = new KochFractal();


    public GenerateEdge(EdgeSide edge, int level, KochManager km){
        this.edge = edge;
        this.km = km;
        koch.addObserver(this);
        koch.setLevel(level);
    }


    public List<Edge> generate() {

        if(edge == EdgeSide.Right){
            koch.generateRightEdge();
        }
        else if(edge == EdgeSide.Bottom){
            koch.generateBottomEdge();
        }
        else if(edge == EdgeSide.Left){
            koch.generateLeftEdge();
        }
        return edges;
    }


    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge) arg);

    }
}