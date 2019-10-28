/*
 * @(#) NewPubController.java 1.0 03/05/19
 *
 * Copyright (c) 2019 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.group2.desktop;


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uk.ac.aber.group2.common.*;


import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;

/**
 * NewPubController - contains a number of methods to allow the user to perform data entry for a new pub.
 *
 * @author Michael Male (mim39@aber.ac.uk)
 * @version 1.0 FINAL
 */
public class NewPubController extends CrawlifyController
        implements Initializable {

    @FXML
    public TextField pubName;
    @FXML
    public TextArea pubDescription;
    @FXML
    public TextField pubLongitude;
    @FXML
    public TextField pubLatitude;


    @FXML
    public Button addImage;
    public Text imagesGiven;
    public ListView filters;
    public TextField customFilter;
    public Button customFilterButton;

    private String key;
    private List<Pub> pubs;
    protected List<String> checkedFilters = new LinkedList<>();
    private static ArrayList<URL> images;


    /**
     * Initalizes the pub data entry form such that it includes a number of
     * default filters as well as any custom filters that a user may have
     * added in the past.
     *
     * @param url            derived from super-class
     * @param resourceBundle derived from super-class
     * @see Initializable
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pubs = new LinkedList<>();

        pubDescription.setWrapText(true); // Ensures that the description
        // area wraps

        // Hides the filter and URL entry until it's necessary to show it
        images = new ArrayList<>();
        ObservableList filterList = filters.getItems();

        customFilter.setDisable(true);
        customFilter.setVisible(false);

        // Main code runs during runtime rather than compiled before entering
        // class
        Platform.runLater(() -> {

            filters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            filterList.addAll(CrawlifyController.masterList.getFilters());
            filters.setItems(filterList);




            if (!checkedFilters.isEmpty()) {
                for (String f : checkedFilters) {
                    filters.getSelectionModel().select(f);
                }
            }
        });

    }



    /**
     * Adds a pub to the list of pubs
     */
    @FXML
    public void pubAdd(ActionEvent event) {
        addAPub();
    }

    /**
     * Parses data required to add a pub
     */
    protected void addAPub() {
        String name = pubName.getText();
        String description = pubDescription.getText();

        double latitude = Double.parseDouble(pubLatitude.getText());
        double longitude = Double.parseDouble(pubLongitude.getText());

        ObservableList observableFilters = filters.getSelectionModel().getSelectedItems();
        LinkedList<String> checkedFilters = new LinkedList<>(observableFilters);

        ArrayList<String> imageStrings = new ArrayList<>();

        for (URL url : images) {
            imageStrings.add(url.toString());
        }

        pubName.clear();
        pubDescription.clear();
        pubLongitude.clear();
        pubLatitude.clear();

        this.parsePub(name,description,latitude, longitude, checkedFilters, imageStrings);
    }

    /**
     * Parses any data required for a pub to be added.
     *
     * @param name
     * @param description
     * @param latitude
     * @param longitude
     * @param checkedFilters
     * @param imageStrings
     */
    protected void parsePub(String name, String description, double latitude, double longitude, LinkedList<String> checkedFilters, ArrayList<String> imageStrings) {
        boolean successLat=false;
        boolean successLon=false;

        Pub temp = new Pub(name);
    while (!successLat && !successLon) {
    temp.setDescription(description);

    try {
        temp.setLat(latitude);
        successLat = true;
    } catch (IllegalArgumentException e) {
        generateAlert(latitude + " is an invalid value for latitude.", Alert.AlertType.ERROR,
                ButtonType.CLOSE);
        successLat = false;
    }

    try {
        temp.setLon(longitude);
        successLon = true;
    } catch (IllegalArgumentException e) {
        generateAlert(longitude + " is an invalid value for longitude.", Alert.AlertType.ERROR,
                ButtonType.CLOSE);
        successLon = false;
    }

    temp.setImages(imageStrings);
    }

            CrawlifyController.masterList.getPubs().add(temp);

            for (String filter : checkedFilters) {
                CrawlifyController.masterList.addFilterToPub(temp, filter);
            }
        }

    /**
     * Sets the key to be used when traversing through the HashMap
     *
     * @param key String containing key
     */
    void setKey(String key) {
        this.key = key;
    }

    /**
     * Fires from the add image button and creates a new window with parameters to add
     * an image.
     *
     * @param event the even that is to be fired
     */
    @FXML
    private void addImgBtn(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/" +
                    "fxml_files/addImage.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add an image");
            stage.setScene(new Scene(root, getNormalHeight() / 2, getNormalWidth() / 2));
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            generateAlert(e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
        }

    }

    /**
     * Passes all images into the array list
     * @param img
     */
    static void setImages(ArrayList<URL> img) {
        images.addAll(img);
    }

    /**
     * Adds a filter to the pub.
     * @param actionEvent
     */
    @FXML
    public void addAFilter(ActionEvent actionEvent) {
        customFilterButton.setDisable(true);
        customFilterButton.setVisible(false);

        customFilter.setVisible(true);
        customFilter.setDisable(false);
    }

    /**
     * Adds a custom filter to the pub.
     * @param keyEvent
     */
    @FXML
    public void enterCustomFilter(KeyEvent keyEvent) {

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            String theFilter = customFilter.getText();
            filters.getItems().add(theFilter);
            customFilter.setVisible(false);
            customFilter.setDisable(true);
            customFilter.clear();

            customFilterButton.setVisible(true);
            customFilterButton.setDisable(false);
        }
    }

    /**
     * Takes a filter off a pub.
     * @param actionEvent
     */
    @FXML
    public void removeFilter(ActionEvent actionEvent) {
        String filterToRemove = (String) filters.getSelectionModel().getSelectedItem();

        CrawlifyController.masterList.removeFilterForAllPubs(filterToRemove);
        filters.getItems().remove(filterToRemove);
    }

    /**
     * Goes to the previous screen.
     * @param actionEvent
     */
    @FXML
    public void backButtonClicked(ActionEvent actionEvent) {

        Stage current = (Stage) pubName.getScene().getWindow();

        current.setScene(getPreviousScreen());

    }

}