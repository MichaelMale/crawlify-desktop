/*
@(#) StartController.java   1.0 2019/05/03

Copyright (c) 2019 Aberystwyth University.
All rights reserved.

 */
package uk.ac.aber.group2.desktop;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

/**
 * StartController - a class that includes methods to control the introductory screen to the application.
 *
 * This screen has two buttons. One button allows the user to enter in a brand new location, and the other loads a list
 * of locations that are currently in a PHP-based store of JSON files. It also confirms that the computer that the
 * program is running on can connect to the server, and if not, checks if it is connected to Internet by pinging
 * a popular web address. If this can not be pinged then it is highly likely that the Internet is not connected. Either
 * way, the program is unable to run without an Internet connection, and closes.
 *
 * @author Michael Male (mim39@aber.ac.uk)
 * @author Joseph Mayo (jom79@aber.ac.uk)
 * @version 1.0 FINAL
 */
public class StartController extends CrawlifyController implements Initializable {

    @FXML
    public Button newLocation;

    @FXML
    public Button existingLocation;

    private Scene initialScreen;

    /**
     * Gets the initial screen that has been assigned to this controller. This should always be the first
     * screen that the program operates off of.
     *
     * @return  Object of type Scene containing details about this screen.
     */
    public Scene getInitialScreen() {
        return initialScreen;
    }

    /**
     * Sets the initial screen assigned to this controller. This should always be the first screen that the
     * program operates off of.
     *
     * @param initialScreen Object of type Scene containing details about this screen.
     */
    public void setInitialScreen(Scene initialScreen) {
        this.initialScreen = initialScreen;
    }


    /**
     * Upon the user pressing the 'Enter new location' button, the program gets a reference to the stage that is
     * used, and then tries to load data concerning "createLocation.fxml".
     *
     * @see CreateLocationController
     *
     * @param event This is fired when the 'Enter new location' button is clicked
     */
    @FXML
    private void enterNewLocation(ActionEvent event) {
        System.out.println("You clicked \"Enter new Location\""); // debug text

        Stage belongingToSource =
                (Stage) ((Node)event.getSource()).getScene().getWindow();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources" +
                    "/fxml_files/createLocation.fxml"));
            Parent parent = loader.load();
            CreateLocationController nextController = loader.getController();
            Scene prev = ((Node)event.getSource()).getScene();

            nextController.setPreviousScreen(prev);
            nextController.setMasterList(this.getMasterList());

            belongingToSource.setScene(new Scene(parent, this.getNormalHeight(),
                    this.getNormalWidth()));
        } catch (IOException e) {
            e.printStackTrace(); // needs to be changed to generate a meaningful error, perhaps GUI-related
        }

    }


    /**
     * Upon the user pressing the 'Select existing location' button, the program gets a reference to the stage
     * that is currently in use. It then tries to load data concerning 'existingLocation.fxml'.
     *
     * @see ExistingLocationController
     *
     * @param event This is fired when the 'Select existing location' button is pressed.
     */
    @FXML
    private void selectExistingLocation(ActionEvent event) {

        System.out.println("You clicked \"Select an existing location\"");

        Stage belongingToSource =
                (Stage) ((Node)event.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/fxml_files/" +
                    "existingLocation.fxml"));
            Parent parent = loader.load();
            ExistingLocationController nextController = loader.getController();
            Scene prev = ((Node)event.getSource()).getScene();
            nextController.setPreviousScreen(prev);
            setMasterList(this.getMasterList());
            belongingToSource.setScene(new Scene(parent, this.getNormalHeight(), this.getNormalWidth()));
        } catch (IOException e) {
            e.printStackTrace(); // needs to be changed to generate a meaningful error
        }

    }


    /**
     * This method checks whether a connection can be achieved. It does this by trying to connect, and opening an
     * input stream. If this does not work it will throw an exception; MalformedURLException if the URL was not
     * entered in correctly (which should never be the case), and IOException if the content of the website was
     * not able to be correctly found. This would be the case if data is unreachable on the server.
     *
     * @param url   String containing a Uniform Resource Locator, that is checked for its validity within the
     *              method.
     *
     * @return  Boolean containing true if the server was reachable, and false if the server was not reachable.
     */
    private static boolean reachedServer(String url) {
        try {
            final URL serverAddress = new URL(url);
            final URLConnection connection = serverAddress.openConnection();
            connection.connect(); // Tries to make a connection to the URL specified
            connection.getInputStream().close(); // Closes the connection and returns true, if it has got this far it
            // can successfully connect
            return true;
        } catch (MalformedURLException e) { // Should only be thrown if the protocol or URL is incorrect, as this is
            // hard-coded this should not ever be the case
            throw new RuntimeException(e);
        } catch (IOException e) { // This will be thrown and the method will return false, IOException will only be
            // thrown if there was an issue with connecting to the server i.e. it is down or there's no connection
            // to the Internet
            return false;
        }
    }

    /**
     * Initializes the first aspect of the GUI by calling reachedServer(). If this is false then an error message is
     * displayed and the process is killed. If this is true then the program continues as per usual.
     *
     * @param url Links to the FXMl file that is currently in use
     *
     * @param resourceBundle Links to an object of type ResourceBundle if necessary
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!reachedServer("http://users.aber.ac.uk")) {
            Alert noConnection = new Alert(Alert.AlertType.ERROR);
            noConnection.setTitle("Server unreachable");
            noConnection.setHeaderText(null);

            if (reachedServer("http://www.google.com")) {
                noConnection.setContentText("Cannot connect to the server.");
            } else {
                noConnection.setContentText("Cannot connect to the Internet.");
            }

            noConnection.showAndWait();
            Platform.exit();

        }
    }
}
