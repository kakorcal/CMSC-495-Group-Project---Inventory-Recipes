package gui;

import api.Message;
import api.RecipeObject;
import database.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class MenuGUI {
    //--Fields-----------------------------------//
    private JPanel f;
    private JLabel l;
    private JTabbedPane tp;
    private JOptionPane op;
    private ButtonPanel bp1;
    private JPanel main1, main2, main3, sub1, sub2, sub3;
    private String s1 = "Inventory ";
    private String s2 = "Recipe ";
    private String s3 = "Menu ";
    private String s4 = " ";
    private String s5 = "\n";
    private String[] ar = {"Add", "Add item to inventory: "};
    private ArrayList<RecipeObject> input, menu;
    private ArrayList<CheckPanel> cpa;
    private ArrayList<SpinnerPanel> spa;
    private ArrayList<JComponent> jpa;
    private ButtonSet recipeSet, menuSet;
    private Write writeFile;

    private SessionManager manager = null;
    private User user = null;
    private UserTransaction userTransaction = null;
    private InventoryTransaction inventoryTransaction = null;
    private RecipeTransaction recipeTransaction = null;
    private MenuTransaction menuTransaction = null;
    private MenuItemTransaction menuItemTransaction = null;
    
    private Message m;
    private BufferedWriter output;
    private OutputStream out;
    private Path path;
    private byte[] data;

    public JPanel getMainPanel() {
        return f;
    }

    public void setMainPanelLayout() {
        f.setLayout(new BoxLayout(f, BoxLayout.Y_AXIS));
    }

    public MenuGUI() {
        if(initializeTransactions()) {
            f = new JPanel();
            l = new JLabel("Menu Selections");
            tp = new JTabbedPane();
            op = new JOptionPane();
            bp1 = new ButtonPanel(ar, 8);
            cpa = new ArrayList<>();
            spa = new ArrayList<>();
            jpa = new ArrayList<>();
            sub1 = new JPanel();
            sub2 = new JPanel();
            sub3 = new JPanel();
            recipeSet = new ButtonSet("Generate Recipes", "Generate Menu",
                    "Clear Selections");
            menuSet = new ButtonSet("Rename Menu", "Save Menu", "Print Menu");
            setTabs();
            try {
                setAction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            main1.add(new JLabel("Failed to retrieve user data."));
        }
    }

    private Boolean initializeTransactions() {
        RestaurantApp app = RestaurantApp.getInstance();
        manager = app.getSessionManager();
        userTransaction = app.getUserTransaction();
        user = app.getUser();
        inventoryTransaction = app.getInventoryTransaction();
        recipeTransaction = app.getRecipeTransaction();
        menuTransaction = app.getMenuTransaction();
        menuItemTransaction = app.getMenuItemTransaction();

        if(userTransaction == null || user == null) {
            return false;
        }

        if(inventoryTransaction == null) {
            app.setInventoryTransaction(new InventoryTransaction(manager, user));
            inventoryTransaction = app.getInventoryTransaction();
        }

        if(recipeTransaction == null) {
            app.setRecipeTransaction(new RecipeTransaction(manager, user));
            recipeTransaction = app.getRecipeTransaction();
        }

        if(menuTransaction == null) {
            app.setMenuTransaction(new MenuTransaction(manager, user));
            menuTransaction = app.getMenuTransaction();
        }

        if(menuItemTransaction == null) {
            app.setMenuItemTransaction(new MenuItemTransaction(manager, user));
            menuItemTransaction = app.getMenuItemTransaction();
        }
        
        return true;
    }

    synchronized public void addArray(ArrayList al, JComponent jc) {
        Iterator more;
        if (al != null) {
            more = al.iterator();
            while (more.hasNext()) {
                jc.add((JComponent) more.next());
            }
        }
    }

    synchronized public void addTabs() {
        addArray(spa, sub1);
        addArray(cpa, sub2);
        addArray(jpa, sub3);
    }

    synchronized public void setAction() throws Exception {
        recipeSet.addFunction(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("RECIPE SET ACTION PERFORMED");
                        //cpa.get(0).setWords("This occured");
                        setAllFalse();
                    }
                }, 3);

        menuSet.addFunction(new ActionListener() {
            Message m = new Message();
            public void actionPerformed(ActionEvent ae) {
                System.out.println("MENU SET ACTION PERFORMED");
                    String fileName="menuLog.txt";
                    writeFile=new Write(fileName,"");
                  
            }
        }, 2);
    }

        synchronized public void setAllFalse () {
            if (cpa != null) {
                Iterator checks = cpa.iterator();
                while (checks.hasNext()) {
                    CheckPanel temp = (CheckPanel) checks.next();
                    temp.setBox();
                }
            }
        }

        synchronized public void setArrays (ArrayList a, ArrayList b, ArrayList c){
            spa = a;
            cpa = b;
            jpa = c;
        }

        private void setTabs () {
            //FirstTab
            tp.addTab(s1 + s3, main1 = new JPanel());
            main1.setBorder(new TitledBorder(s1 + s3));
            main1.add(bp1);
            main1.setLayout(new BoxLayout(main1, BoxLayout.Y_AXIS));
            sub1.setLayout(new GridLayout(10, 1, 5, 5));
            getInventory(sub1);
            main1.add(sub1);
            //SecondTab
            tp.addTab(s2 + s3, main2 = new JPanel());
            main2.setBorder(new TitledBorder(s2 + s3));
            sub2.setLayout(new GridLayout(2, 10));
            main2.add(recipeSet);
            main2.add(sub2);
            //ThirdTab
            tp.addTab(s3 + s3, main3 = new JPanel());
            main3.setLayout(new GridLayout(2, 3));
            main3.setBorder(new TitledBorder(s3 + s3));
            main3.add(menuSet);
            main3.add(sub3);
            f.add(tp);
        }

        private void getInventory(JComponent container) {
            List<Inventory> inventories = inventoryTransaction.list();

            if(inventoryTransaction.getError().hasError()) {
                Platform.runLater(() -> {
                    new Message().showMessage("Error", null, inventoryTransaction.getError().getMessage(), Alert.AlertType.WARNING);
                });
            }else {
                for(Inventory inventory: inventories) {
                    ButtonPanel inventoryPanel = new ButtonPanel(inventory.getId(), inventory.getName(), inventory.getQuantity());
                    container.add(inventoryPanel);
                }
            }

        }

        public void setMenuTitle (String s1){
            l.setText(s1);
        }

        public void postText (String s){
            op.showMessageDialog(op, s);
        }

    class ScrollPanel extends JPanel {
        private JScrollPane sp;
        public JTextArea ta;

        public ScrollPanel(int x, int y) {
            ta = new JTextArea(x, y);
            sp = new JScrollPane(ta);
            sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            add(sp);
        }

        public void flipWhite() {
            setBackground(Color.WHITE);
        }

        public void flipGray() {
            setBackground(Color.GRAY);
        }

        public void flipDark() {
            setBackground(Color.DARK_GRAY);
        }

        public String getText() {
            return ta.getText();
        }

        public void makeText(String s) {
            ta.setText(s);
        }
    }

    class ButtonPanel extends JPanel {

        private JButton b;
        private JTextField tf;
        private JLabel l;
        private JSpinner s;

        public ButtonPanel(String[] sa, int i) {

            b = new JButton(sa[0]);
            addFunction();
            tf = new JTextField(i);
            l = new JLabel(sa[1]);
            s = new JSpinner();
            s.setValue(000);
            setLayout(new FlowLayout());
            s.setPreferredSize(new Dimension(80, 20));
            this.add(l);
            this.add(s);
            this.add(tf);
            this.add(b);
        }

        public ButtonPanel(long id, String inventory, int quantity) {
            JButton updateButton = new JButton("Update");
            JButton deleteButton = new JButton("Delete");
            JTextField inventoryId = new JTextField(Long.toString(id));
            JTextField inventoryName = new JTextField(inventory);
            JTextField inventoryQuantity = new JTextField(Integer.toString(quantity));
            JTextField updateInventoryField = new JTextField(inventory, 6);
            JSpinner updateSpinner = new JSpinner();
            updateSpinner.setValue(quantity);
            setLayout(new FlowLayout());
            updateSpinner.setPreferredSize(new Dimension(80, 20));
            ButtonPanel self = this;

            updateButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("UPDATE INVENTORY BUTTON CLICKED");
                            int quantity = -1;

                            try {
                                quantity = Integer.parseInt("" + updateSpinner.getValue());
                            }catch (Exception err) {
                                Platform.runLater(() -> {
                                    new Message().showMessage("Error", "Invalid quantity", "Please enter a valid integer.", Alert.AlertType.WARNING);
                                });
                            }

                            if(quantity >= 0) {
                                String name = updateInventoryField.getText();
                                Inventory inventory = new Inventory(id, name, quantity);
                                Inventory newInventory = inventoryTransaction.update(inventory);

                                if(inventoryTransaction.getError().hasError()) {
                                    updateInventoryField.setText(inventoryName.getText());
                                    updateSpinner.setValue(updateSpinner.getValue());
                                    Platform.runLater(() -> {
                                        new Message().showMessage("Error", null, inventoryTransaction.getError().getMessage(), Alert.AlertType.WARNING);
                                    });
                                }else {
                                    updateInventoryField.setText(newInventory.getName());
                                    updateSpinner.setValue(newInventory.getQuantity());
                                    inventoryName.setText(newInventory.getName());
                                    inventoryQuantity.setText(Integer.toString(newInventory.getQuantity()));
                                    sub1.revalidate();
                                    Platform.runLater(() -> {
                                        new Message().showMessage("Success", null, "Successfully updated inventory.", Alert.AlertType.INFORMATION);
                                    });
                                }
                            }
                        }
                    });

            deleteButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("DELETE INVENTORY BUTTON CLICKED");

                            inventoryTransaction.delete(id);

                            if(inventoryTransaction.getError().hasError()) {
                                Platform.runLater(() -> {
                                    new Message().showMessage("Error", null, inventoryTransaction.getError().getMessage(), Alert.AlertType.WARNING);
                                });
                            }else {
                                sub1.remove(self);
                                sub1.revalidate();
                                Platform.runLater(() -> {
                                    new Message().showMessage("Success", null, "Successfully deleted inventory.", Alert.AlertType.INFORMATION);
                                });
                            }


                            sub1.revalidate();
                        }
                    });

            this.add(inventoryId);
            this.add(inventoryName);
            this.add(inventoryQuantity);
            inventoryId.setVisible(false);
            inventoryName.setVisible(false);
            inventoryQuantity.setVisible(false);
            this.add(updateSpinner);
            this.add(updateInventoryField);
            this.add(updateButton);
            this.add(deleteButton);
        }

        private void addFunction() {
            b.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("ADD INVENTORY BUTTON CLICKED");
                            int quantity = -1;

                            try {
                                quantity = Integer.parseInt("" + s.getValue());
                            }catch (Exception err) {
                                Platform.runLater(() -> {
                                    new Message().showMessage("Error", "Invalid quantity", "Please enter a valid integer.", Alert.AlertType.WARNING);
                                });
                            }

                            if(quantity >= 0) {
                                String name = tf.getText();
                                Inventory inventory = new Inventory(name, quantity);
                                Inventory newInventory = inventoryTransaction.create(inventory);

                                if(inventoryTransaction.getError().hasError()) {
                                    Platform.runLater(() -> {
                                        new Message().showMessage("Error", null, inventoryTransaction.getError().getMessage(), Alert.AlertType.WARNING);
                                    });
                                }else {
                                    sub1.add(new ButtonPanel(newInventory.getId(), newInventory.getName(), newInventory.getQuantity()));
                                    sub1.revalidate();
                                    Platform.runLater(() -> {
                                        new Message().showMessage("Success", null, "Successfully created inventory.", Alert.AlertType.INFORMATION);
                                    });
                                }
                            }
                        }
                    });
        }

        public int getSpin() {
            return (Integer) s.getValue();
        }

        public void setSpin(int i) {
            s.setValue(i);
        }
    }

    class CheckPanel extends ScrollPanel {
        private JCheckBox cb;
        private Boolean changed;

        public CheckPanel(int x, int y) {
            super(x, y);
            cb = new JCheckBox();
            changed = false;
            add(cb);
            checkCheck();
        }

        private void checkCheck() {
            cb.addChangeListener(
                    new ChangeListener() {
                        public void stateChanged(ChangeEvent ce) {
                            System.out.println("CHECK BOX CLICKED");
                            if (cb.isSelected()) {
                                flipGray();
                                changed = true;
                            } else if (!cb.isSelected()) {
                                flipWhite();
                                changed = false;
                            }
                        }
                    });
        }

        public Boolean getChanged() {
            return changed;
        }

        public String getWords() {
            return ta.getText();
        }

        public void setWords(String s) {
            ta.setText(s);
        }

        public Boolean getBox() {
            return cb.isSelected();
        }

        public void setBox() {
            cb.setSelected(false);
        }
    }

    class SpinnerPanel extends JPanel {
        private JLabel l;
        private JSpinner s;
        Boolean changed;
        private int initial;

        public SpinnerPanel(String s1, int i) {
            l = new JLabel(s1);
            s = new JSpinner();
            changed = false;
            initial = i;
            s.setPreferredSize(new Dimension(80, 20));
            add(l);
            s.setValue(i);
            add(s);
        }

        private void checkSpinner() {
            s.addChangeListener(
                    new ChangeListener() {
                        public void stateChanged(ChangeEvent ce) {
                            System.out.println("SPINNING STATE CHANGED.");
                            if ((int) s.getValue() != initial) {
                                changed = true;
                            }
                        }
                    });
        }

        public Boolean getChange() {
            return changed;
        }

        public int getSpin() {
            return (Integer) s.getValue();
        }

        public void setSpin(int i) {
            s.setValue(i);
        }
    }

    class ButtonSet extends JPanel {
        private JButton b1, b2, b3;

        public ButtonSet(String s1, String s2, String s3) {
            b1 = new JButton(s1);
            b2 = new JButton(s2);
            b3 = new JButton(s3);
            System.out.println("ADDING EVENT LISTENERS TO THE FOLLOWING");
            System.out.println(s1);
            System.out.println(s2);
            System.out.println(s3);
            this.add(b1);
            this.add(b2);
            this.add(b3);
        }

        public void addFunction(ActionListener al, int i) {
            switch (i) {
                case 1:
                    b1.addActionListener(al);
                    break;
                case 2:
                    b2.addActionListener(al);
                    break;
                case 3:
                    b3.addActionListener(al);
                    break;
            }
        }
    }
}
class Write {
    private Message m;
    private BufferedWriter output;
    private OutputStream out;
    private Path path;
    private byte[] data;
    synchronized public void saveString(String s1,String s2){
        try {
            path = Paths.get("./"+s1);
            data=s2.getBytes();
            out = new BufferedOutputStream(
                    Files.newOutputStream(path,CREATE,APPEND));
            out.write(data,0,data.length);
            out.flush();
            //m.showMessage("Files saved successfully.");
            System.out.println("File saved.");
        } 
        catch (IOException ioe) {
            //m.showMessage("File could not save.");
            System.out.println("File could not save.");
            System.err.println(ioe);} 
        finally {}}  
}
