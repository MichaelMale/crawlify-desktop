/*
 * @(#) AddImageController.java 1.0 03/05/19
 *
 * Copyright (c) 2019 Aberystwyth University.
 * All rights reserved.
 *
 */

package uk.ac.aber.group2.desktop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * AddImageController - This class adds an image to a specific Pub.
 *
 * This class is called from either NewPubController or EditPubController. When called, it asks the user for an image.
 * Once the user presses the Enter key, the image is checked for whether it is valid and then you can either add it or
 * cancel the operation.
 *
 * @author Michael Male (mim39@aber.ac.uk)
 * @version 1.0 FINAL
 */
public class AddImageController
        extends CrawlifyController
        implements Initializable {

    @FXML
    public ImageView imgProvided;
    @FXML
    public Button btnAddImg;
    @FXML
    public Button btnReset;
    @FXML
    public TextField imageLink;

    private URL enteredURL;
    private ArrayList<URL> urlArrayList;

    /**
     * Initializes the image controller by checking if the Enter key is pressed at a constant interval.
     * @param url   The FXML file
     * @param resourceBundle    Object of type ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        urlArrayList = new ArrayList<>();
        imageLink.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    boolean validImage = false;

                    do {
                        try {
                            imgProvided.setImage(checkAndShowImage(imageLink.getText()));
                            validImage = true;
                        } catch (IllegalArgumentException | MalformedURLException e) {
                            generateAlert(e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
                        }
                    } while (!validImage);

                    try {
                        enteredURL = new URL(imageLink.getText());
                    } catch (MalformedURLException e) {
                        generateAlert(e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
                    }
                }
            }


        });
    }

    /**
     * Resets the entire screen to null
     *
     * @param actionEvent reset button
     */
    public void reset(ActionEvent actionEvent) {
        enteredURL = null;
        imgProvided.setImage(null);
        imageLink.clear();
    }

    /**
     * Passes a link to an image into a new pub
     * @param actionEvent   The 'Add' button
     */
    public void addImage(ActionEvent actionEvent) {
        urlArrayList.add(enteredURL);
        NewPubController.setImages(urlArrayList);
        ((Stage) btnAddImg.getScene().getWindow()).close(); // closes the window after
        // the image is added
    }

    /**
     * Checks that an image is a valid image and returns it in a valid object
     * @param link  String containing a user-defined link to an image
     * @return  Object of type image, containing the image from the link provided
     * @throws MalformedURLException    If the URL is incorrect
     * @throws IllegalArgumentException If a link was given that does not have a file
     * extension, or the file extension is not one of the supported types (BMP,
     * JPEG, GIF, or PNG)
     */
    private Image checkAndShowImage(String link) throws MalformedURLException,
            IllegalArgumentException{
        Image enteredImage;

        URL enteredURL = new URL(link);

        int lastIndexOfDot = link.lastIndexOf(".");

        String fileExtension = null;

        if (lastIndexOfDot > 0) {
            fileExtension = link.substring(lastIndexOfDot+1);
        }


        if (fileExtension == null) {
            throw new IllegalArgumentException("No link was provided.");
        } else if (!fileExtension.matches("BMP|JPG|JPEG|GIF|PNG|bmp" +
                "|jpg|jpeg|gif|png")) {
            throw new IllegalArgumentException("The file extension is not recognized.");
        } else {
            enteredImage = new Image(enteredURL.toString());
            return enteredImage;
        }


    }

}
