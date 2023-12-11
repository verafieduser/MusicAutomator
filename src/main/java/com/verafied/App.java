package com.verafied;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

import com.verafied.library.NativeLibrary;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public static final boolean DEMO = true;

    public static void main(String[] args) throws Exception { 
        launch(args); 
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        

        try {
            Initializer init = new Initializer(DEMO);
            UserInterface ui = new UserInterface(init);
            ui.inputLoop(new InputHandler(DEMO));
        } catch (Exception e) {
            e.printStackTrace();
        }


    
        //StackPane root = new StackPane();
        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 800, 600));
        //primaryStage.show();
        HibernateUtil.getSessionJavaConfigFactory().close();
        Platform.exit();
    }

}
