import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.canvas.*; 
import javafx.animation.AnimationTimer; 
import javafx.scene.input.KeyEvent;

/**
 * The demo will make use of JavaFX's Canvas API. This class creates three clocks which are animated using an 
 * AnimationTimer class. 
 * 
 * This clock is using Widget API for app containers to manage widget launching life cycle. 
 * 
 * Inspired by http://burnwell88.deviantart.com/art/Clock-136761577 and http://rainmeter.net/cms/ 
 * 
 * @author Carl Dea <carl.dea@gmail.com> 
 * @since 1.0 
 * 
 * Original code, Copyright 2013 eWidgetFX. Modified by Steve Birtles for easy use in BlueJ.
 */ 

/* This is the first class for our JavaFX application that contains the main method. As well as setting
 * everything in motion, it also contains a method to stop the program running. */
public class Application
{

    public static void main(String args[])
    {       
        /* JavaFX apps run in a different processing thread to the main method so that they keep running
         * after the main method has completed. The 'start' method is invoked when this thread starts. */
        JFXPanel panel = new JFXPanel();        
        Platform.runLater(() -> start());               
    }

    private static void start() 
    {
        try
        {         
            System.out.println("Application Starting...");

            Group root = new Group();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 1024, 768, Color.BLACK);
            
            stage.setTitle("JavaFX Demo");
            stage.setScene(scene);
            
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> System.out.println("Pressed: " + event.getCode()));

            // create a canvas node and bind the dimensions when the user resizes the window.
            Canvas canvas = new Canvas();
            canvas.widthProperty().bind(stage.widthProperty());
            canvas.heightProperty().bind(stage.heightProperty());
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // create three clocks
            ArcClock blueClock = new ArcClock(20, ArcClock.BLUE1, ArcClock.BLUE2, 200);
            ArcClock greenClock = new ArcClock(20, ArcClock.BLUE1, ArcClock.GREEN1, 200);
            ArcClock redClock = new ArcClock(20, ArcClock.BLUE1, ArcClock.RED1, 200);

            // create an animation (update & render loop)
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    // update clocks
                    blueClock.update(now);
                    greenClock.update(now);
                    redClock.update(now);

                    // clear screen
                    gc.clearRect(0, 0, stage.getWidth(), stage.getHeight());

                    // draw blue clock
                    blueClock.draw(gc);
                    // save the origin or the current state
                    // of the Graphics Context.
                    gc.save();

                    // shift x coord position the width of a clock plus 20 pixels
                    gc.translate(blueClock.maxDiameter + 20, 0);
                    greenClock.draw(gc);

                    // shift x coord position past the first clock
                    gc.translate(blueClock.maxDiameter + 20, 0);
                    redClock.draw(gc);

                    // reset Graphics Context to last saved point.
                    // Translate x, y to (0,0)
                    gc.restore();

                }
            }.start();

            root.getChildren().add(canvas);

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        System.out.println("Close button was clicked!");
                        Application.terminate();
                    }
                });

            stage.show();           

        }
        catch (Exception ex)    // If anything goes wrong starting the application then call the terminate method.
        {
            System.out.println(ex.getMessage());
            terminate();
        }
    }

    /* The following method can be called from any controller class and will terminate the application. */
    public static void terminate()
    {
        System.out.println("Terminating Application...");
        System.exit(0);                                 // Finally, terminate the entire application.
    }

}