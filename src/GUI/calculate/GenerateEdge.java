package GUI.calculate;

import GUI.jsf31kochfractalfx.JSF31KochFractalFX;
import Shared.Edge;
import Shared.EdgeSide;
import Shared.KochFractal;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

public class GenerateEdge extends Task implements Observer{

    private EdgeSide edge;
    private KochManager km;
    private List<Edge> edges = new ArrayList<>();
    private KochFractal koch = new KochFractal();
    private JSF31KochFractalFX application;
    private int total;
    AtomicInteger done = new AtomicInteger(0);

    public GenerateEdge(EdgeSide edge, int level, KochManager km, JSF31KochFractalFX application){
        this.edge = edge;
        this.km = km;
        koch.addObserver(this);
        koch.setLevel(level);
        this.application = application;
        total = koch.getNrOfEdges()/3;

    }


    @Override
    protected Object call() {
        if(!isCancelled()){

            if(edge == EdgeSide.Right){
                koch.generateRightEdge();
            }
            else if(edge == EdgeSide.Bottom){
                koch.generateBottomEdge();
            }
            else if(edge == EdgeSide.Left){
                koch.generateLeftEdge();
            }
        }
        return edges;
    }

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge) arg);
        try {
            Thread.currentThread().sleep(1);
        } catch (InterruptedException e) {
            this.cancel();
        }
        updateProgress(done.incrementAndGet(), total);
        updateMessage(String.valueOf(done.get()));
        Platform.runLater(()->{
            this.application.drawEdge((Edge) arg, true);
        });
    }

    @Override
    public void cancelled(){
        application.clearKochPanel();
        super.cancelled();
        koch.cancel();
    }

}