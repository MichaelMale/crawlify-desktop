/*
 * @(#) CrawlifyController.java 1.0 03/05/19
 *
 * Copyright (c) 2019 Aberystwyth University.
 * All rights reserved.
 *
 */


package uk.ac.aber.group2.desktop;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import uk.ac.aber.group2.common.PubList;

import java.io.Console;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * CrawlifyController - This is a superclass that extends to all subclasses within the desktop package.
 *
 * This controller contains common variables and methods for the subclasses, as well as a static PubList, masterList,
 * that is used to contain a single masterList during the lifetime of the program.
 *
 * @author Michael Male (mim39@aber.ac.uk)
 * @version 1.0 FINAL
 */
public class CrawlifyController {

    private static boolean isDebug = false;
    protected static PubList masterList;
    private Scene previousScreen;

    /**
     * Gets the previous screen.
     * @return  The previous screen of type Scene
     */
    public Scene getPreviousScreen() {
        return previousScreen;
    }

    /**
     * Sets the previous screen.
     * @param previousScreen An object of type Scene that should contain the previous screen
     */
    public void setPreviousScreen(Scene previousScreen) {
        this.previousScreen = previousScreen;
    }

    /**
     * Returns the master pub list
     *
     * @return object of List which is the one that is currently in use
     */
    PubList getMasterList() {
        return masterList;
    }

    /**
     * Sets a list of type ArrayList to the PubList that is in use
     *
     * @param list object of type list containing null to many PubList
     */
    public void setMasterList(PubList list) {
        masterList = list;
    }

    /**
     * Returns a boolean that defines whether a "debugging" mode is active or not. The
     * default should be false, and the boolean can only be changed prior to build.
     *
     * @return true if developer is debugging, false if not
     */
    public static boolean isIsDebug() {
        return isDebug;
    }

    /**
     * Sets the debugging boolean
     * @param isDebug   true if to be debugged, false if not
     */
    public static void setIsDebug(boolean isDebug) {
        CrawlifyController.isDebug = isDebug;
    }

    /**
     * Defines and returns the normal height that a window should be
     *
     * @return 1265px
     */
    int getNormalHeight() {
        return 1265;
    }

    /**
     * Defines and returns the normal width that a window should be
     *
     * @return 811px
     */
    int getNormalWidth() {
        return 811;
    }

    /**
     * Generates an alert that shows up on the Graphical User Interface
     *
     * @param alertMessage The message to be shown on the alert
     * @param alertType    The type of alert, dictated by an ENUM Alert.AlertType
     * @param buttonType   The type of button, dictated by an ENUM Button.ButtonType
     */
    void generateAlert(String alertMessage, Alert.AlertType alertType,
                       ButtonType buttonType) {
        Alert alert = new Alert(alertType, alertMessage, buttonType);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    /**
     * Creates a graphical interface to show the details of an exception that may have not been explicitly handled.
     * @param exception The Exception that was thrown
     * @see <a href="https://code.makery.ch/blog/javafx-dialogs-official/">This</a> website was used as the primary
     * reference for this code.
     */
    void exceptionAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error occurred");
        alert.setHeaderText("An error occurred");
        alert.setContentText("There was an unexpected issue whilst running the program.");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Please provide the following below to the software developers:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }


    /**
     * Provides code for an alert window to ask the user whether they wish to save, close or cancel the dialog box.
     */
    @FXML
    void saveAndClose() {

        /* Generates a new alert and gives it a title and content text */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close Crawlify");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to close?");

        /* Generates three buttons, one to save, one to not save, and one to cancel */
        ButtonType yepSave = new ButtonType("Yes, and save", ButtonBar.ButtonData.YES);
        ButtonType yepDontSave = new ButtonType("Yes, and don't save", ButtonBar.ButtonData.FINISH);
        ButtonType bye = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yepSave, yepDontSave, bye); // Gives these buttons to the alert window

        Optional<ButtonType> result = alert.showAndWait(); // Waits for the user to answer the window

        if (result.isPresent()) {
            if (result.get() == yepSave) {
                try {
                    /* Saves all data to a JSON file */
                   CrawlifyController.masterList.saveJson("http://users.aber.ac.uk/mop14/Group2/save.php");

                } catch (Exception e) {
                    exceptionAlert(e);
                }
                Platform.exit();
            } else if (result.get() == yepDontSave) {
                /* Simply closes the process */
                Platform.exit();
            } else {
                /* Does nothing */
                System.out.println("Cancel");
            }
        }
    }

    /**
     * Used throughout classes to confirm if the close button is clicked.
     * @param actionEvent   This is fired when the close button is clicked.
     */
    @FXML
    public void closeButtonClicked(ActionEvent actionEvent) {
        saveAndClose();
    }




}
