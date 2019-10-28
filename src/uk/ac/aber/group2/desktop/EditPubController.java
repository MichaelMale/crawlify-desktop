/*
 * @(#) EditPubController.java 1.0 03/05/19
 *
 * Copyright (c) 2019 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.group2.desktop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uk.ac.aber.group2.common.Pub;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * EditPubContoller - This class runs through editing a pub.
 *
 * @author Michael Male (mim39@aber.ac.uk)
 * @version 1.0 FINAL
 * @see NewPubController for similar methods and for the superclass
 */
public class EditPubController extends NewPubController {

    public Button goBack;
    private Pub pubToEdit;

    /**
     * Sets the pub to be edited.
     * @param pubToEdit
     */
    void setPubToEdit(Pub pubToEdit) {
        this.pubToEdit = pubToEdit;
    }

    /**
     * Initialize method that inherits most of it from the super-class but disables the pub name.
     * @param url            derived from super-class
     * @param resourceBundle derived from super-class
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        pubName.setDisable(true);
    }

    /**
     * Sets all data for an existing pub.
     */
    void setPreData() {
        this.pubName.setText(pubToEdit.getName());
        this.pubDescription.setText(pubToEdit.getDescription());
        this.pubLatitude.setText(Double.toString(pubToEdit.getLat()));
        this.pubLongitude.setText(Double.toString(pubToEdit.getLon()));
        this.checkedFilters = new LinkedList<>(pubToEdit.getFilters());
    }


    /**
     * Goes to the previous screen.
     * @param actionEvent
     */
    @Override
    public void backButtonClicked(ActionEvent actionEvent) {
                Stage current = (Stage) goBack.getScene().getWindow();

                current.setScene(getPreviousScreen());
    }

    /**
     * Parses all data required for the pub to be inputted into a JSON file.
     * @param name
     * @param description
     * @param latitude
     * @param longitude
     * @param checkedFilters
     * @param imageStrings
     */
    @Override
    protected void parsePub(String name,
                           String description,
                           double latitude,
                           double longitude,
                           LinkedList<String> checkedFilters,
                           ArrayList<String> imageStrings) {

        for (Pub p : (ArrayList<Pub>) CrawlifyController.masterList.getPubs()) {
            boolean successLat = false;
            boolean successLon = false;
            if (p.getName().equals(name)) {
                p.setName(name);
                p.setDescription(description);

                do {

                    try {
                        p.setLat(latitude);
                        successLat = true;
                    } catch (IllegalArgumentException e) {
                        generateAlert(latitude + " is not a valid value for latitude.", Alert.AlertType.ERROR,
                                ButtonType.CLOSE);
                    }

                    try {
                        p.setLon(longitude);
                        successLon = true;
                    } catch (IllegalArgumentException e) {
                        generateAlert(longitude + " is not a valid value for longitude.", Alert.AlertType.ERROR,
                                ButtonType.CLOSE);
                    }
                } while (!successLat && !successLon);

                pubToEdit.getFilters().clear();
                for (String providedFilter : checkedFilters) {
                    CrawlifyController.masterList.addFilterToPub(pubToEdit, providedFilter);
                }

                for (String providedImage : imageStrings) {
                    if (!pubToEdit.getImages().contains(providedImage)) {
                        pubToEdit.getImages().add(providedImage);
                    }
                }

                break;
            }
        }





    }

}