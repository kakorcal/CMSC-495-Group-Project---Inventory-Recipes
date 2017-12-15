package api;

import javafx.scene.control.Alert;

public class Message {
    /**
     * Empty Constructor
     */
    public Message(){ }

    /**
     * Simple way to display messages for use within the API
     * @param title
     * @param subject
     * @param body
     * @param alertType
     */
    public void showMessage(String title, String subject, String body, Alert.AlertType alertType){
             Alert alert = new Alert(alertType);
             alert.setTitle(title);
             alert.setHeaderText(subject);
             alert.setContentText(body);
             alert.showAndWait();
    }
}
