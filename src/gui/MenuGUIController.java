package gui;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.swing.*;

public class MenuGUIController {
    @FXML private BorderPane container;

    @FXML private Pane menuPane;

    @FXML private Text loadingMessage;

    @FXML protected void initialize() {
        final SwingNode swingNode = new SwingNode();
        createSwingNodeContent(swingNode);
        menuPane.getChildren().add(swingNode);
        container.setCenter(menuPane);
    }

    private void createSwingNodeContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                MenuGUI gui = new MenuGUI();
                swingNode.setContent(gui.getMainPanel());
                container.getTop().setVisible(false);
                System.out.println("MENU GUI INITIALIZED");

            }
        });
    }
}
