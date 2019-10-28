
/*
 * @(#) Main.java 1.0 03/05/19
 *
 * Copyright (c) 2019 Aberystwyth University.
 * All rights reserved.
 *
 */

package uk.ac.aber.group2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import uk.ac.aber.group2.desktop.StartController;

/**
 * Main - This class is the first class that runs when running the desktop application.
 *
 * The class runs a method that loads the first controller. This controller runs the first screen (start.fxml), and is
 * loaded via reference from the FXML file. If this is not possible, the method throws an exception, with the output of
 * this available in a console.
 *
 * @author Michael Male (mim39@aber.ac.uk)
 * @version 1.0 FINAL
 * @see StartController
 */

public class Main extends Application {

    /**
     * The main method that passes arguments to the JVM.
     * @param args  Any arguments that are passed before run-time.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method begins the JavaFX application. It uses StartController, which is the first JavaFX controller in the
     * application, and loads it.
     *
     * @param primaryStage  The stage that is to be loaded first, and to be used for the remainder of the program's
     *                      run-time.
     * @throws Exception    If there is an issue with loading the graphical interface. Obviously, the stack trace will
     * only print to a console, therefore it may not be possible for an end user to see this.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        StartController firstController;
        Parent root;
        /* Gets the FXML file and loads it. */
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("desktop/resources/fxml_files/start.fxml"));
            root = loader.load();
            firstController = loader.getController();
        } catch (IOException e) {
            System.err.println("An error occurred when loading the graphical interface." +
                    " Please see below.\n");
            e.printStackTrace();
            return;
        }

        /* Sets the title and the dimensions for the window. */
        primaryStage.setTitle("Crawlify");
        Scene currentScene = new Scene(root, 1265, 811);
        firstController.setInitialScreen(currentScene);

        primaryStage.setScene(currentScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
