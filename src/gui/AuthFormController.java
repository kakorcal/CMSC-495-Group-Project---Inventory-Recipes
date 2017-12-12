package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AuthFormController {
    @FXML private Text errorMessage;

    @FXML protected void handleSignup(ActionEvent event) {
        System.out.println("User attempting to signup");
        errorMessage.setText("Sign in button pressed");
    }

    @FXML
    protected void handleLogin(ActionEvent event) {
        System.out.println("User attempting to login");
        errorMessage.setText("Login button pressed");
    }
}
