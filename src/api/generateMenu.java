package api;

import javax.swing.*;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class generateMenu extends RecipeFinderController {

    JFrame menuFrame = new JFrame();
    JPanel tempPanel;
    JTextArea ItemTitle;
    DecimalFormat df = new DecimalFormat("#.##");
    ArrayList<String> descriptions = new ArrayList<String>();
    private Random randomGenerator;



    public generateMenu(){
        super("Restraunt Menu");
        randomGenerator = new Random();
        df.setRoundingMode(RoundingMode.FLOOR);
        descriptions.add("You'll eat this till you drop!");
        descriptions.add("Our favorite dish!");
        descriptions.add("To die for!");
        descriptions.add("You cant find this at home!");
        descriptions.add("This will beat your grandmas!");
        descriptions.add("Have fun recreating this masterpiece!");
        descriptions.add("We know you would be back for this delectable item!");
        descriptions.add("You'll never get enough!");
        descriptions.add("Don't miss this!");
        descriptions.add("Mhhhhhhhhhhhmmmmmmmmmmmmmm!");
        descriptions.add("You'll weigh 5 more pounds after this dish!");
        descriptions.add("Hope your pants are loose!");
        descriptions.add("One for the memories!");
        descriptions.add("You'll want to Instagram this one!");

    }

    public void addItem(JFrame frame, String itemName){
        double randomPrice = new Double(df.format(ThreadLocalRandom.current().nextDouble(10, 20)));
        tempPanel = new JPanel(new BorderLayout());
        tempPanel.setSize(600,50);
        int index = randomGenerator.nextInt(descriptions.size());
        System.out.println("Random Index " + index);
        String description = descriptions.get(index);
        descriptions.remove(index);
        ItemTitle = new JTextArea("Name: " + itemName + "\n Price: " + randomPrice + "\n Description: " + description,3, 35);
        //ItemTitle = new JTextArea("Name: " + itemName + "\n Price: " + randomPrice,3, 25);
        tempPanel.add(ItemTitle);
        ItemTitle.setEditable(false);
        tempPanel.setVisible(true);
        frame.add(tempPanel);
    }




}
