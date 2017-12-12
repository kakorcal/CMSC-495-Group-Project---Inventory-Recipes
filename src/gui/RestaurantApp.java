package gui;

import database.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class RestaurantApp extends Application {
    private SessionManager manager = null;
    private User user = null;
    private UserTransaction userTransaction = null;
    private InventoryTransaction inventoryTransaction = null;
    private RecipeTransaction recipeTransaction = null;
    private MenuTransaction menuTransaction = null;
    private MenuItemTransaction menuItemTransaction = null;
    private static final RestaurantApp instance = new RestaurantApp();

    @Override
    public void start(Stage primaryStage) {
        try {
            manager = new SessionManager();
            manager.setup();
            instance.setSessionManager(manager);

            // signup / login form https://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm
            Parent root = FXMLLoader.load(getClass().getResource("authForm.fxml"));
            primaryStage.setTitle("Welcome");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The Restaurant Management Application cannot be accessed at this moment");
            alert.showAndWait();
            System.exit(0);
        }
    }

    @Override
    public void stop() {
        if(manager != null) {
            manager.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static RestaurantApp getInstance(){
        return instance;
    }

    public SessionManager getSessionManager() {
        return manager;
    }

    public void setSessionManager(SessionManager manager) {
        this.manager = manager;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserTransaction getUserTransaction() {
        return userTransaction;
    }

    public void setUserTransaction(UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

    public InventoryTransaction getInventoryTransaction() {
        return inventoryTransaction;
    }

    public void setInventoryTransaction(InventoryTransaction inventoryTransaction) {
        this.inventoryTransaction = inventoryTransaction;
    }

    public RecipeTransaction getRecipeTransaction() {
        return recipeTransaction;
    }

    public void setRecipeTransaction(RecipeTransaction recipeTransaction) {
        this.recipeTransaction = recipeTransaction;
    }

    public MenuTransaction getMenuTransaction() {
        return menuTransaction;
    }

    public void setMenuTransaction(MenuTransaction menuTransaction) {
        this.menuTransaction = menuTransaction;
    }

    public MenuItemTransaction getMenuItemTransaction() {
        return menuItemTransaction;
    }

    public void setMenuItemTransaction(MenuItemTransaction menuItemTransaction) {
        this.menuItemTransaction = menuItemTransaction;
    }
}
