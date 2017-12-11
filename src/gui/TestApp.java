package gui;

import database.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;


/**
 * Name: Kenneth Korcal
 * Date: 12/03/2017
 * Description:
 *
 * GUI for applying Create, Read, Update, Delete, and List operations on Inventory table
 *
 */
public class TestApp extends JFrame {
    private SessionManager manager = null;
    private User user = null;
    private UserTransaction userTransaction = null;
    private InventoryTransaction inventoryTransaction = null;
    private RecipeTransaction recipeTransaction = null;
    private MenuTransaction menuTransaction = null;
    private MenuItemTransaction menuItemTransaction = null;

    public static void main(String[] args) {
        TestApp app = new TestApp();
        app.display();
    }

    public TestApp() {
        super("Inventory Manager");
        setFrame();

        try {
            manager = new SessionManager();
            manager.setup();
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    manager.exit();
                }
            });
            add(new MainPanel());
        }catch(Exception e) {
            JOptionPane pane = new JOptionPane("Database cannot be accessed at this moment.");
            JDialog dialog = pane.createDialog(new MainPanel(), "Error");
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(null);
        }
    }

    private void display() {
        setVisible(true);
    }

    private void setFrame() {
        setPreferredSize(new Dimension(600, 500));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class MainPanel extends JPanel {
        private JButton readButton = new JButton("Read");
        private JButton createButton = new JButton("Create");
        private JButton updateButton = new JButton("Update");
        private JButton deleteButton = new JButton("Delete");
        private JButton listButton = new JButton("List All");
        private JButton testButton = new JButton("Test");

        private JLabel idLabel = new JLabel("Id: ");
        private JTextField idField = new JTextField(10);
        private JLabel nameLabel = new JLabel("Name: ");
        private JTextField nameField = new JTextField(10);
        private JLabel quantityLabel = new JLabel("Quantity: ");
        private JTextField quantityField = new JTextField(10);

        private JTextArea textarea = new JTextArea(22, 45);
        private JScrollPane textareaScrollPane = new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        public MainPanel() {
            /*
             * Inputs and Labels
             */
            JPanel inputsPanel = new JPanel();
            inputsPanel.add(idLabel);
            inputsPanel.add(idField);
            inputsPanel.add(nameLabel);
            inputsPanel.add(nameField);
            inputsPanel.add(quantityLabel);
            inputsPanel.add(quantityField);

            /*
             * Buttons
             */
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(readButton);
            buttonsPanel.add(createButton);
            buttonsPanel.add(updateButton);
            buttonsPanel.add(deleteButton);
            buttonsPanel.add(listButton);
            buttonsPanel.add(testButton);

            /*
             * Textarea
             */
            JPanel textareaPanel = new JPanel();
            textarea.setEditable(false);
            textarea.setBackground(getBackground());
            textareaPanel.add(textareaScrollPane);
            textareaPanel.setMinimumSize(new Dimension(325, getHeight()));

            /*
             * Event Listeners
             * */
            readButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String idStr = idField.getText();

                    try {
                        long id = Long.parseLong(idStr);
                        Inventory inventory = inventoryTransaction.read(id);

                        if(inventory != null) {
                            textarea.setText("Item found:\n\n" + inventory.toString());
                        }else {
                            textarea.setText("Item not found");
                        }
                    }catch (Exception err) {
                        textarea.setText("Item not found.");
                    }

                }
            });

            createButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String nameStr = nameField.getText();
                    String quantityStr = quantityField.getText();

                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        Inventory inventory = inventoryTransaction.create(new Inventory(nameStr, quantity));
                        textarea.setText("Item created:\n\n" + inventory.toString());
                    }catch (Exception err) {
                        textarea.setText("Failed to create item.");
                    }

                }
            });

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String idStr = idField.getText();
                    String nameStr = nameField.getText();
                    String quantityStr = quantityField.getText();

                    try {
                        long id = Long.parseLong(idStr);
                        int quantity = Integer.parseInt(quantityStr);
                        Inventory inventory = inventoryTransaction.update(new Inventory(id, nameStr, quantity));

                        if(inventory != null) {
                            textarea.setText("Item updated:\n\n" + inventory.toString());
                        }else {
                            textarea.setText("Failed to update item.");
                        }
                    }catch (Exception err) {
                        textarea.setText("Item not found.");
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String idStr = idField.getText();

                    try {
                        long id = Long.parseLong(idStr);
                        Inventory inventory = inventoryTransaction.delete(id);

                        if(inventory != null) {
                            textarea.setText("Item deleted:\n\n" + inventory.toString());
                        }else {
                            textarea.setText("Failed to delete item.");
                        }
                    }catch (Exception err) {
                        textarea.setText("Item not found.");
                    }

                }
            });

            listButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    List<Inventory> inventories = inventoryTransaction.list();

                    if(!inventories.isEmpty()) {
                        String result = "Current Items:\n\n";

                        for(Inventory inventory : inventories) {
                            result += inventory.toString() + "\n\n";
                        }

                        textarea.setText(result);
                    }else {
                        textarea.setText("No items found.");
                    }
                }
            });

            testButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Test button clicked.");

                    // testing signup
                    userTransaction = new UserTransaction(manager);

                    try {
                        // this can be replaced with userTransaction.login as well. you will just have to change the error message.
                        user = userTransaction.signup("test", "user");
                        if(user == null) {
                            textarea.setText("Invalid credentials.");
                            return;
                        }
                    }catch (Exception err) {
                        textarea.setText("Failed to login.");
                        return;
                    }

                    // creating all transaction instances
                    inventoryTransaction = new InventoryTransaction(manager, user);
                    recipeTransaction = new RecipeTransaction(manager, user);
                    menuTransaction = new MenuTransaction(manager, user);
                    // menu item is dependent on menu and recipe when called the CRUDL methods
                    menuItemTransaction = new MenuItemTransaction(manager);

                    // doing some transactions

                    // create inventory
                    inventoryTransaction.create(new Inventory("Egg", 12));
                    inventoryTransaction.create(new Inventory("Milk", 1));
                    inventoryTransaction.list();

                    // call api and retrieve recipe. pass that result into Recipe object
                    recipeTransaction.create(new Recipe("Title1", "Source1", "Image1", 10.00));
                    recipeTransaction.create(new Recipe("Title2", "Source2", "Image2", 20.00));
                    recipeTransaction.create(new Recipe("Title3", "Source3", "Image3", 22.00));
                    List<Recipe> recipes = recipeTransaction.list();

                    // create a menu based on the recipe
                    Menu menu1 = menuTransaction.create(new Menu("Menu1"));
                    menuTransaction.list();

                    // create the menu items
                    for(Recipe recipe: recipes) {
                        menuItemTransaction.create(new MenuItem(), menu1, recipe);
                    }
                    menuItemTransaction.list(menu1);
                }
            });


            /*
             * Container
             * */
            setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridy = 0;
            add(inputsPanel, constraints);

            constraints.gridy = 1;
            add(buttonsPanel, constraints);

            constraints.gridy = 2;
            add(textareaPanel, constraints);
        }

    }
}

