/*
@(#) CreationLocationController.java    1.0 2019/05/03

Copyright (c) 2019 Aberystwth University.
All rights reserved.

 */
package uk.ac.aber.group2.desktop;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uk.ac.aber.group2.common.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * CreateLocationController - This controller functions by creating a location, searching for this location,
 * and either opening an existing pub list or creating a new list.
 *
 * @author Michael Male (mim39@aber.ac.uk)
 * @version 1.0
 */
public class CreateLocationController extends CrawlifyController {

    public TextField locationName;


    /**
     * Searches for a location. if this location is already in the database, then it will not create a duplicate
     * location and then loads an existing location.
     * @param event This is if a key is pressed, the method then checks if this is an enter key.
     */
    @FXML
    void searchForLocation(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) // if the Enter key is pressed
        {

            String key = locationName.getText();
            List<String> locationsInDataBase = new LinkedList<>();
            CrawlifyController.masterList = new PubList(key);

            try {
                locationsInDataBase =
                        PubList.getLocations("http://users.aber.ac.uk/mop14/Group2/locations.php");
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (!locationsInDataBase.isEmpty()) {
                for (String location : locationsInDataBase) {
                    if (location.equalsIgnoreCase(key)) {
                        generateAlert("\"" + key + "\"" + " is already a location name within the database." +
                                        "Opening the list.",
                                Alert.AlertType.INFORMATION, ButtonType.CLOSE);
                        key = location;
                        CrawlifyController.masterList = new PubList(key);
                        CrawlifyController.masterList.loadJson("http://users.aber.ac.uk/mop14/Group2/download.php");
                        break;
                    }
                }
            }

            Stage stageThatTextFieldBelongs =
                    (Stage) locationName.getScene().getWindow();

            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("resources/fxml_files/newPub.fxml"));

                Parent parent = loader.load();
                NewPubController nextController =
                        loader.getController();
                nextController.setKey(key);
                nextController.setMasterList(getMasterList());
                Scene prev = locationName.getScene();
                nextController.setPreviousScreen(prev);
                stageThatTextFieldBelongs.setScene(new Scene(parent, getNormalHeight(), getNormalWidth()));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * If the back button is clicked the method will go to the previous screen.
     * @param actionEvent
     */
    public void backButtonClicked(ActionEvent actionEvent) {
        Stage current = (Stage) locationName.getScene().getWindow();

        current.setScene(getPreviousScreen());
    }
}
