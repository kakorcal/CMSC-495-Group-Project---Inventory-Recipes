package api;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Name: Dustin Summers
 * Date: 11/06/2017
 * Description:
 *
 * Controller for GUI.
 *
 */
public class RecipeFinderController {

    private generateMenu myMenu;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Pane recipePane;

    Message message = new Message();
    ArrayList<RecipeObject> recipes;

    /**
     * findRecipes()
     * Button Controller.  Couple of steps here:
     * 1) Check To make sure we have internet
     * 2) If we have internet, pull in the recipe list from our desired URL
     * <p>
     */
    @FXML
    private void findRecipes() {
        System.out.println("Finding recipes!");

        //TODO:At this time, the URL is a hardcoded String.  Build in functionality to adjust this dynamically based on user criteria/ingredients.
        String recipeURL = "https://food2fork.com/api/search?key=304840c6b26d5a8c28da4ff2661b8e85&q=egg,milk,floursort=r";

        System.out.println("Passing in RecipeURL: " + recipeURL);

        //Extract Recipes from Given URL and create ArrayList of all Recipes
        recipes = RecipesQuery.extractRecipes(recipeURL);

        //Ensure Recipes were returned
        if (recipes.size() <= 0) {
            System.out.println("No recipes returned!");
            message.showMessage("No Recipes", "No Recipes Returned", "No Recipes based off of stock!  Acquire more items!", Alert.AlertType.ERROR);
        } else {
            displayRecipes(recipes);
        }
    }

    /**
     * Launches the other stage to display the recipes generated from our current inventory
     *
     * From this, I will generate a separate window, not relying on JavaFX so that I can retain
     * a reference to the RecipesArrayList/Object that I setup in this phase...
     *
     * @param recipes
     */
    private void displayRecipes (ArrayList < RecipeObject > recipes) {
        final SwingNode swingNode = new SwingNode();
        createSwingNodeContent(swingNode, recipes);
        recipePane.getChildren().add(swingNode);
        borderPane.setCenter(recipePane);
    }

    /**
     * Launches web-page based off of selected recipes
     */
    @FXML
    private void getDirections(){
        System.out.println("Get recipes pushed");
        //Pull in the Recipes from our ArrayList RecipeList
        for(int i = 0; i<recipes.size(); i++){
            if(recipes.get(i).isCheckBoxChecked()) {
                if(Desktop.isDesktopSupported()){
                    try {
                        Desktop.getDesktop().browse(new URI(recipes.get(i).getRecipeURL()));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else{
                    message.showMessage("Error", "Unable to Open Browser",
                            "Your Operating System is not supported.  Please browse to the following website to view the directions" + recipes.get(i).getRecipeURL(),
                            Alert.AlertType.INFORMATION);
                }
            }
        }
    }

    /**
     * Generates a menu based off of selected items
     */
    @FXML
    private void generateMenu(){
        System.out.println("Generate menu pressed!");
        myMenu = new generateMenu();
        for(int i = 0; i<recipes.size(); i++){
            if(recipes.get(i).isCheckBoxChecked()) {
                myMenu.addItem(recipes.get(i).getRecipeTitle());
            }
        }
    }

    /**
     * This is a weird "java" hack that I needed to do in order to properly display my Custom object's elements
     *
     * @param swingNode
     * @param recipes
     */
    private void createSwingNodeContent(final SwingNode swingNode, ArrayList<RecipeObject> recipes){
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                for(int i = 0; i < recipes.size(); i++){
                    panel.add(recipes.get(i).getRecipeJPanelObject());
                }
                JScrollPane scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setMaximumSize(new Dimension(350, 330));
                swingNode.setContent(scrollPane);
            }
        });
    }

}
