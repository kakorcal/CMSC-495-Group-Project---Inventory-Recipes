package api;

import javax.swing.*;
import java.awt.*;

public class generateMenu {

    private JFrame menuFrame;
    private JPanel tempPanel;
    private JTextArea textArea;

    public generateMenu(){
        JFrame menuFrame = new JFrame ("Menu");
        menuFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(600,600);
        menuFrame.pack();
        menuFrame.setVisible (true);
        System.out.println("Completed Frame initiliaztion");
    }

    public void addItem(String itemName){
        tempPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea(itemName,5, 20);
        tempPanel.add(textArea);
        System.out.println("Attempting to create a panel with a text area of: " + itemName);
        textArea.setEditable(false);
        menuFrame.add(tempPanel);
    }




}
