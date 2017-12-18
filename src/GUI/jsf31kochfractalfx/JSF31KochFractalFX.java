/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.jsf31kochfractalfx;

import GUI.calculate.Edge;
import GUI.calculate.FileType;
import GUI.calculate.KochManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Nico Kuijpers
 */
public class JSF31KochFractalFX extends Application {
    
    // Zoom and drag
    private double zoomTranslateX = 0.0;
    private double zoomTranslateY = 0.0;
    private double zoom = 1.0;
    private double startPressedX = 0.0;
    private double startPressedY = 0.0;
    private double lastDragX = 0.0;
    private double lastDragY = 0.0;

    // Koch manager
    // TO DO: Create class KochManager in package calculate
    private KochManager kochManager;

    // Current level of Koch fractal
    private int currentLevel = 0;
    
    // Labels for level, nr edges, calculation time, and drawing time
    private Label labelLevel;
    private Label labelNrEdges;
    private Label labelNrEdgesText;
    private Label labelCalc;
    private Label labelCalcText;
    private Label labelDraw;
    private Label labelDrawText;
    
    // Koch panel and its size
    private Canvas kochPanel;
    private final int kpWidth = 500;
    private final int kpHeight = 500;


    //Choose level elements
    TextField tfLevel;
    Button btnDrawLevel;

    public ProgressBar pbLeftEdge;
    public ProgressBar pbBottomEdge;
    public ProgressBar pbRightEdge;
    private Label pbLeft;
    private Label pbBottom;
    private Label pbRight;
    public Label nrEdgesLeft;
    public Label nrEdgesBottom;
    public Label nrEdgesRight;
    
    @Override
    public void start(Stage primaryStage) {


        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // For debug purposes
        // Make de grid lines visible
        // grid.setGridLinesVisible(true);
        
        // Drawing panel for Koch fractal
        kochPanel = new Canvas(kpWidth,kpHeight);
        grid.add(kochPanel, 0, 3, 25, 1);
        
        // Labels to present number of edges for Koch fractal
        labelNrEdges = new Label("Nr edges:");
        labelNrEdgesText = new Label();
        grid.add(labelNrEdges, 0, 0, 4, 1);
        grid.add(labelNrEdgesText, 3, 0, 22, 1);
        
        // Labels to present time of calculation for Koch fractal
        labelCalc = new Label("Calculating:");
        labelCalcText = new Label();
        grid.add(labelCalc, 0, 1, 4, 1);
        grid.add(labelCalcText, 3, 1, 22, 1);
        
        // Labels to present time of drawing for Koch fractal
        labelDraw = new Label("Drawing:");
        labelDrawText = new Label();
        grid.add(labelDraw, 0, 2, 4, 1);
        grid.add(labelDrawText, 3, 2, 22, 1);
        
        // Label to present current level of Koch fractal
        labelLevel = new Label(" Choose a level to generate: ");
        grid.add(labelLevel, 0, 6);
        
//         //Button to increase level of Koch fractal
//        Button buttonIncreaseLevel = new Button();
//        buttonIncreaseLevel.setText("Increase Level");
//        buttonIncreaseLevel.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                increaseLevelButtonActionPerformed(event);
//            }
//        });
//        grid.add(buttonIncreaseLevel, 3, 6);

//        // Button to decrease level of Koch fractal
//        Button buttonDecreaseLevel = new Button();
//        buttonDecreaseLevel.setText("Decrease Level");
//        buttonDecreaseLevel.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                decreaseLevelButtonActionPerformed(event);
//            }
//        });
//        grid.add(buttonDecreaseLevel, 5, 6);
//
        // Button to fit Koch fractal in Koch panel
        Button buttonFitFractal = new Button();
        buttonFitFractal.setText("Fit Fractal");
        buttonFitFractal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fitFractalButtonActionPerformed(event);
            }
        });
        grid.add(buttonFitFractal, 9, 6);

        //Choose level controls
        tfLevel = new TextField();
        tfLevel.setMaxWidth(40);
        btnDrawLevel = new Button("Draw selected level");
        btnDrawLevel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawSelectedlevel(event);
            }
        });


        grid.add(tfLevel,  3, 6);
        grid.add(btnDrawLevel, 5, 6);


        //Progressbars and labels

//        pbLeftEdge = new ProgressBar();
//        pbBottomEdge = new ProgressBar();
//        pbRightEdge = new ProgressBar();
//
//        pbLeft = new Label("Progress left:");
//        pbBottom = new Label("Progress bottom:");
//        pbRight = new Label("Progress right:");
//
//        nrEdgesLeft = new Label("Progress left:");
//        nrEdgesBottom = new Label("Progress bottom:");
//        nrEdgesRight = new Label("Progress right:");
//
//        grid.add(pbLeftEdge, 5,7);
//        grid.add(pbBottomEdge, 5,8);
//        grid.add(pbRightEdge, 5,9);
//
//        grid.add(pbLeft, 0,7);
//        grid.add(pbBottom, 0,8);
//        grid.add(pbRight, 0,9);

//        grid.add(nrEdgesLeft, 6,7);
//        grid.add(nrEdgesBottom, 6,8);
//        grid.add(nrEdgesRight, 6,9);

        // Add mouse clicked event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    kochPanelMouseClicked(event);
                }
            });
        
        // Add mouse pressed event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    kochPanelMousePressed(event);
                }
            });
        
        // Add mouse dragged event to Koch panel
        kochPanel.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                kochPanelMouseDragged(event);
            }
        });
        
        // Create Koch manager and set initial level
        resetZoom();
        kochManager = new KochManager(this);
        //kochManager.changeLevel(0, FileType.TEXT, true);
        
        // Create the scene and add the grid pane
        Group root = new Group();
        Scene scene = new Scene(root);
        root.getChildren().add(grid);
        
        // Define title and assign the scene for main window
        primaryStage.setTitle("Koch Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void clearKochPanel() {
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
        gc.clearRect(0.0,0.0,kpWidth,kpHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0.0,0.0,kpWidth,kpHeight);
    }
    
    public void drawEdge(Edge e, boolean oneEdge) {
        // Graphics
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
        
        // Adjust edge for zoom and drag
        Edge e1 = edgeAfterZoomAndDrag(e);
        
        // Set line color

        if(oneEdge){
            gc.setStroke(Color.WHITE);
        }
        else{
            gc.setStroke(e1.color);
        }

        
        // Set line width depending on level
        if (currentLevel <= 3) {
            gc.setLineWidth(2.0);
        }
        else if (currentLevel <=5 ) {
            gc.setLineWidth(1.5);
        }
        else {
            gc.setLineWidth(1.0);
        }
        
        // Draw line
        gc.strokeLine(e1.X1,e1.Y1,e1.X2,e1.Y2);
    }
    
    public void setTextNrEdges(String text) {
        labelNrEdgesText.setText(text);
    }
    
    public void setTextCalc(String text) {
        labelCalcText.setText(text);
    }
    
    public void setTextDraw(String text) {
        labelDrawText.setText(text);
    }
    
    public void requestDrawEdges() {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                kochManager.drawEdges();
            }
        });
    }
    
//    private void increaseLevelButtonActionPerformed(ActionEvent event) {
//        if (currentLevel < 12) {
//            // resetZoom();
//            currentLevel++;
//            //labelLevel.setText("Level: " + currentLevel);
//            kochManager.changeLevel(currentLevel);
//        }
//    }
//
//    private void decreaseLevelButtonActionPerformed(ActionEvent event) {
//        if (currentLevel > 1) {
//            // resetZoom();
//            currentLevel--;
//            //labelLevel.setText("Level: " + currentLevel);
//
//
//
//
//            kochManager.changeLevel(currentLevel);
//        }
//    }

    private void drawSelectedlevel(ActionEvent event) {

        int input;
        try{
            input = Integer.parseInt(tfLevel.getText());
        }
        catch(NumberFormatException ex){
            System.out.println("ERROR, Please enter valid number [1-11]");
            return;
        }

        if(input == currentLevel){
//            return;
        }
        else if(input > 11){
            System.out.println("ERROR, Please enter valid number [1-11]");
            return;
        }
        else if(input < 1){
            System.out.println("ERROR, Please enter valid number [1-11]");
            return;
        }
        currentLevel = input;




        kochManager.changeLevel(input);

    }

    private void fitFractalButtonActionPerformed(ActionEvent event) {
        resetZoom();
        kochManager.drawEdges();
    }
    
    private void kochPanelMouseClicked(MouseEvent event) {
        if (Math.abs(event.getX() - startPressedX) < 1.0 && 
            Math.abs(event.getY() - startPressedY) < 1.0) {
            double originalPointClickedX = (event.getX() - zoomTranslateX) / zoom;
            double originalPointClickedY = (event.getY() - zoomTranslateY) / zoom;
            if (event.getButton() == MouseButton.PRIMARY) {
                zoom *= 2.0;
            } else if (event.getButton() == MouseButton.SECONDARY) {
                zoom /= 2.0;
            }
            zoomTranslateX = (int) (event.getX() - originalPointClickedX * zoom);
            zoomTranslateY = (int) (event.getY() - originalPointClickedY * zoom);
            kochManager.drawEdges();
        }
    }                                      

    private void kochPanelMouseDragged(MouseEvent event) {
        zoomTranslateX = zoomTranslateX + event.getX() - lastDragX;
        zoomTranslateY = zoomTranslateY + event.getY() - lastDragY;
        lastDragX = event.getX();
        lastDragY = event.getY();
        kochManager.drawEdges();
    }

    private void kochPanelMousePressed(MouseEvent event) {
        startPressedX = event.getX();
        startPressedY = event.getY();
        lastDragX = event.getX();
        lastDragY = event.getY();
    }                                                                        

    private void resetZoom() {
        int kpSize = Math.min(kpWidth, kpHeight);
        zoom = kpSize;
        zoomTranslateX = (kpWidth - kpSize) / 2.0;
        zoomTranslateY = (kpHeight - kpSize) / 2.0;
    }

    private Edge edgeAfterZoomAndDrag(Edge e) {
        return new Edge(
                e.X1 * zoom + zoomTranslateX,
                e.Y1 * zoom + zoomTranslateY,
                e.X2 * zoom + zoomTranslateX,
                e.Y2 * zoom + zoomTranslateY,
                e.color);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
