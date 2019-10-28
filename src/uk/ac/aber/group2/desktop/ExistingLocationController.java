/*
 * @(#) ExistingLocationController.java 1.0 03/05/19
 *
 * Copyright (c) 2019 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.group2.desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uk.ac.aber.group2.common.Pub;
import uk.ac.aber.group2.common.PubList;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * ExistingLocationController - a method to run through what locations are available via querying the JSON
 * database.
 *
 * @author Michael Male (mim39@aber.ac.uk
 * @version 1.0     FINAL
 */
public class ExistingLocationController extends CrawlifyController implements Initializable {

    public ListView listOfLocations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList items = listOfLocations.getItems();


        try {
            List<String> locations =
                    PubList.getLocations("http://users.aber.ac.uk/" +
                            "mop14/Group2/locations.php"); // Points to a PHP script that fetches
            // all locations with regards to the set of data

            items.addAll(locations);

        } catch (Exception e) {
            generateAlert(e.getMessage(), Alert.AlertType.ERROR, ButtonType.CLOSE);
        }
        
    }


    public void clickedOnLocation(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String currentItemSelected = (String) listOfLocations.getSelectionModel().getSelectedItem();
            System.out.println(currentItemSelected);
            PubList list = new PubList(currentItemSelected);
            list.loadJson("http://users.aber.ac.uk" +
                    "/mop14/Group2/download.php");
            setMasterList(list);

            Stage stageThatTextFieldBelongs =
                    (Stage) listOfLocations.getScene().getWindow();

            try {

                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("resources/fxml_files/pubList.fxml"));

                Parent parent = loader.load();
                PubListController nextController =
                        loader.getController();
                nextController.setMasterList(getMasterList());
                Scene prev = listOfLocations.getScene();
                nextController.setPreviousScreen(prev);
                stageThatTextFieldBelongs.setScene(new Scene(parent, getNormalHeight(), getNormalWidth()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        Stage current = (Stage) listOfLocations.getScene().getWindow();

        current.setScene(getPreviousScreen());
    }
}
