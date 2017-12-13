package gui;

import database.SessionManager;
import database.User;
import database.UserTransaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AuthFormController {
    @FXML private Text errorMessage;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // how to swap scenes
    // https://stackoverflow.com/questions/12804664/how-to-swap-screens-in-a-javafx-application-in-the-controller-class
    @FXML protected void handleSignup(ActionEvent event) throws Exception {
        System.out.println("User attempting to signup");

        //---------- How to interact with the database ------------

        // 1. get the RestaurantApp singleton
        RestaurantApp app = RestaurantApp.getInstance();
        // 2. get the SessionManager
        SessionManager manager = app.getSessionManager();
        // 3. get the transaction type (User, Inventory, Recipe, Menu, MenuItem).
        // now you have access to all the database methods with that transaction type
        UserTransaction userTransaction = app.getUserTransaction();
        // 4. If a particular transaction is not set, set it here
        if(userTransaction == null) {
            app.setUserTransaction(new UserTransaction(manager));
            userTransaction = app.getUserTransaction();
        }

        //----------- Signup the user ---------------
        // attempt to signup the user
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userTransaction.signup(username, password);

        // every transaction type has an errors object when initialized.
        // to check if there is any errors ALWAYS call hasErrors method after calling
        // create, read, update, delete, list, signup, and login.
        // get the errorMessage with getErrorMessage
        if(userTransaction.getError().hasError()) {
            // if an error exist, get the message and output to the gui
            String error = userTransaction.getError().getMessage();
            errorMessage.setText(error);
        }else {
            // if there is no error, set the user and change the scene
            // you will need the user instance to use other transactions
            app.setUser(user);

            Stage primaryStage = (Stage) usernameField.getScene().getWindow();
            // NOTE TO ROSS/DUSTIN/ALEX: you change the scene to your GUI by replacing "/api/recipeChooser.fxml"
            Parent recipeChooser = FXMLLoader.load(getClass().getResource("/api/recipeChooser.fxml"));
            primaryStage.setScene(new Scene(recipeChooser));
        }
    }

    @FXML protected void handleLogin(ActionEvent event) throws Exception {
        System.out.println("User attempting to login");


        //---------- How to interact with the database ------------

        // 1. get the RestaurantApp singleton
        RestaurantApp app = RestaurantApp.getInstance();
        // 2. get the SessionManager
        SessionManager manager = app.getSessionManager();
        // 3. get the transaction type (User, Inventory, Recipe, Menu, MenuItem).
        // now you have access to all the database methods with that transaction type
        UserTransaction userTransaction = app.getUserTransaction();
        // 4. If a particular transaction is not set, set it here
        if(userTransaction == null) {
            app.setUserTransaction(new UserTransaction(manager));
            userTransaction = app.getUserTransaction();
        }

        //----------- Signup the user ---------------
        // attempt to signup the user
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userTransaction.login(username, password);

        // every transaction type has an errors object when initialized.
        // to check if there is any errors ALWAYS call hasErrors method after calling
        // create, read, update, delete, list, signup, and login.
        // get the errorMessage with getErrorMessage
        if(userTransaction.getError().hasError()) {
            // if an error exist, get the message and output to the gui
            String error = userTransaction.getError().getMessage();
            errorMessage.setText(error);
        }else {
            // if there is no error, set the user and change the scene
            // you will need the user instance to use other transactions
            app.setUser(user);

            Stage primaryStage = (Stage) usernameField.getScene().getWindow();
            // NOTE TO ROSS: you change the scene to your GUI by replacing "/api/recipeChooser.fxml"
            Parent recipeChooser = FXMLLoader.load(getClass().getResource("/api/recipeChooser.fxml"));
            primaryStage.setScene(new Scene(recipeChooser));
        }
    }
}
