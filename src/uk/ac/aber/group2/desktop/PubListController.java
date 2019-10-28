/*
 * @(#) PubListController.java 1.0 03/05/19
 *
 * Copyright (c) 2019 Aberystwyth University.
 * All rights reserved.
 *
 */

package uk.ac.aber.group2.desktop;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uk.ac.aber.group2.common.Pub;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * PubListController - Gets a list of pubs from the JSON file and has a photo of the pub and details about the pub.
 *
 * @author Michael Male (mim39@aber.ac.uk)
 * @version 1.0 FINAL
 */
public class PubListController extends CrawlifyController implements Initializable {

    public ListView<Label> listOfPubs;
    final String noImage = "https://www.freeiconspng.com/uploads/no-image-icon-11.PNG";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Platform.runLater(() ->{
            setListView(CrawlifyController.masterList.getPubs());

        });

    }

    /**
     * Sets up the list such that images are downloaded and placed to the left of the screen. The text to the right
     * of the screen is the pub's name, its coordinates, and its filters.
     * @param pubList   A list containing metadata of the current pubs assigned to the program
     */
     private void setListView(List<Pub> pubList) {
        ObservableList<Label> pubItems = listOfPubs.getItems();
        if (!pubItems.isEmpty()) {
            pubItems.clear();
        }
        for (Pub pub : pubList) {
            Image firstImage;
            if (pub.getImages().isEmpty()) {
                firstImage = new Image(noImage);
            } else firstImage = new Image(pub.getImages().get(0));
            ImageView firstImageView = new ImageView(firstImage);

            // sets the height and width of the image to 150x150px to ensure that they are all the same size
            // as each other
            firstImageView.setFitHeight(150);
            firstImageView.setFitWidth(150);

            StringBuilder pubDetails = new StringBuilder();
            pubDetails.append("Name: ");
            pubDetails.append(pub.getName());
            pubDetails.append("\n");
            pubDetails.append("Location: ");
            pubDetails.append(pub.getLat());
            pubDetails.append(", ");
            pubDetails.append(pub.getLon());
            pubDetails.append("\n");
            pubDetails.append("Filters: ");

            for (String filter : pub.getFilters()) {
                pubDetails.append(filter);
                pubDetails.append("\t");
            }


            pubDetails.append("\n");

            Label pubLabel = new Label(pubDetails.toString(), firstImageView);

        pubItems.add(pubLabel);
        }
    }

    /**
     * Loads up the screen required to add a pub, along with passing the relevant PubList to the controller
     * @param actionEvent   When the 'Add a Pub' button is pressed
     */
    public void addPub(ActionEvent actionEvent) {
        Stage belongingToSource =
                (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/fxml_files/" +
                            "newPub.fxml"));

            Parent parent = loader.load();
            NewPubController nextController =
                    loader.getController();
            nextController.setKey(getMasterList().getLocation());
            Scene prev = listOfPubs.getScene();
            nextController.setPreviousScreen(prev);
            nextController.setMasterList(getMasterList());
            belongingToSource.setScene(new Scene(parent, getNormalHeight(), getNormalWidth()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Finds the index of the pub that the user has clicked on. After confirming with the user whether they want
     * this pub to be deleted, the method will either not execute anything if the user indicated they did not want to
     * continue, or, should they indicate the opposite, the index that had been found will be used to delete the pub
     * from the ObservableList, and delete the pub from the master list.
     *
     * @param actionEvent   When the 'Delete a pub' button is pressed
     */
    public void deletePub(ActionEvent actionEvent) {
        int labelIndex = listOfPubs.getSelectionModel().getSelectedIndex();

        Pub selectedPub = (Pub) getMasterList().getPubs().get(labelIndex);

        System.out.println(selectedPub.getName());

        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete " + selectedPub.getName());
        deleteAlert.setHeaderText(null);
        deleteAlert.setContentText("Are you sure you want to delete " + selectedPub.getName() + "?");

        Optional<ButtonType> answer = deleteAlert.showAndWait();

        if (answer.isPresent()) {
            if (answer.get() == ButtonType.OK) {
                System.out.println("Deleting the pub");
                listOfPubs.getItems().remove(labelIndex);
                getMasterList().getPubs().remove(labelIndex);
            }
        }

    }

    public void editPub(ActionEvent actionEvent) {
        Stage belongingToSource =
                (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        int labelIndex = listOfPubs.getSelectionModel().getSelectedIndex();

        Pub selectedPub = (Pub) getMasterList().getPubs().get(labelIndex);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/fxml_files/" +
                    "editPub.fxml"));

            Parent parent = loader.load();
            EditPubController nextController =
                    loader.getController();
            nextController.setKey(getMasterList().getLocation());
            nextController.setMasterList(getMasterList());
            nextController.setPubToEdit(selectedPub);
            nextController.setPreData();
            Scene prev = listOfPubs.getScene();
            nextController.setPreviousScreen(prev);
            belongingToSource.setScene(new Scene(parent, getNormalHeight(), getNormalWidth()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void backButtonClicked(ActionEvent actionEvent) {
        Stage current = (Stage) listOfPubs.getScene().getWindow();

        current.setScene(getPreviousScreen());
    }

    public void refreshButtonClicked(ActionEvent actionEvent) {
        this.setListView(CrawlifyController.masterList.getPubs());
    }


}
