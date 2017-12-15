package gui;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
                gui.setMainPanelLayout();
                JScrollPane scrollPane = new JScrollPane(gui.getMainPanel(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setMinimumSize(new Dimension(800, 500));
                scrollPane.setBorder(new EmptyBorder(0,0,0,0));
                swingNode.setContent(scrollPane);
                container.getTop().setVisible(false);
                System.out.println("MENU GUI INITIALIZED");

            }
        });
    }
}
