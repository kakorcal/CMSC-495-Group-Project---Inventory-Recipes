package gui;

import database.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
 * GUI for testing Create, Read, Update, Delete, and List operations on database tables
 * This is NOT included in the actual Restaurant Management Application. This is for testing purposes only.
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
        super("Restaurant Management Application");
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
//        private JButton readButton = new JButton("Read");
//        private JButton createButton = new JButton("Create");
//        private JButton updateButton = new JButton("Update");
//        private JButton deleteButton = new JButton("Delete");
//        private JButton listButton = new JButton("List All");
        private JButton testButton = new JButton("Run Test");

//        private JLabel idLabel = new JLabel("Id: ");
//        private JTextField idField = new JTextField(10);
//        private JLabel nameLabel = new JLabel("Name: ");
//        private JTextField nameField = new JTextField(10);
//        private JLabel quantityLabel = new JLabel("Quantity: ");
//        private JTextField quantityField = new JTextField(10);

        private JTextArea textarea = new JTextArea(22, 45);
        private JScrollPane textareaScrollPane = new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        public MainPanel() {
            /*
             * Inputs and Labels
             */
//            JPanel inputsPanel = new JPanel();
//            inputsPanel.add(idLabel);
//            inputsPanel.add(idField);
//            inputsPanel.add(nameLabel);
//            inputsPanel.add(nameField);
//            inputsPanel.add(quantityLabel);
//            inputsPanel.add(quantityField);

            /*
             * Buttons
             */
            JPanel buttonsPanel = new JPanel();
//            buttonsPanel.add(readButton);
//            buttonsPanel.add(createButton);
//            buttonsPanel.add(updateButton);
//            buttonsPanel.add(deleteButton);
//            buttonsPanel.add(listButton);
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
//            readButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    String idStr = idField.getText();
//
//                    try {
//                        long id = Long.parseLong(idStr);
//                        Inventory inventory = inventoryTransaction.read(id);
//
//                        if(inventory != null) {
//                            textarea.setText("Item found:\n\n" + inventory.toString());
//                        }else {
//                            textarea.setText("Item not found");
//                        }
//                    }catch (Exception err) {
//                        textarea.setText("Item not found.");
//                    }
//
//                }
//            });
//
//            createButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    String nameStr = nameField.getText();
//                    String quantityStr = quantityField.getText();
//
//                    try {
//                        int quantity = Integer.parseInt(quantityStr);
//                        Inventory inventory = inventoryTransaction.create(new Inventory(nameStr, quantity));
//                        textarea.setText("Item created:\n\n" + inventory.toString());
//                    }catch (Exception err) {
//                        textarea.setText("Failed to create item.");
//                    }
//
//                }
//            });
//
//            updateButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    String idStr = idField.getText();
//                    String nameStr = nameField.getText();
//                    String quantityStr = quantityField.getText();
//
//                    try {
//                        long id = Long.parseLong(idStr);
//                        int quantity = Integer.parseInt(quantityStr);
//                        Inventory inventory = inventoryTransaction.update(new Inventory(id, nameStr, quantity));
//
//                        if(inventory != null) {
//                            textarea.setText("Item updated:\n\n" + inventory.toString());
//                        }else {
//                            textarea.setText("Failed to update item.");
//                        }
//                    }catch (Exception err) {
//                        textarea.setText("Item not found.");
//                    }
//                }
//            });
//
//            deleteButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    String idStr = idField.getText();
//
//                    try {
//                        long id = Long.parseLong(idStr);
//                        Inventory inventory = inventoryTransaction.delete(id);
//
//                        if(inventory != null) {
//                            textarea.setText("Item deleted:\n\n" + inventory.toString());
//                        }else {
//                            textarea.setText("Failed to delete item.");
//                        }
//                    }catch (Exception err) {
//                        textarea.setText("Item not found.");
//                    }
//
//                }
//            });
//
//            listButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    List<Inventory> inventories = inventoryTransaction.list();
//
//                    if(!inventories.isEmpty()) {
//                        String result = "Current Items:\n\n";
//
//                        for(Inventory inventory : inventories) {
//                            result += inventory.toString() + "\n\n";
//                        }
//
//                        textarea.setText(result);
//                    }else {
//                        textarea.setText("No items found.");
//                    }
//                }
//            });

            testButton.addActionListener(new ActionListener() {
                private Boolean testPerformed = false;
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Running Database Integration Test.");

                    if(testPerformed) {
                        return;
                    }else {
                        testPerformed = true;
                        SessionFactory sessionFactory = manager.getSessionFactory();
                        Session session = sessionFactory.openSession();
                        Transaction transaction = null;

                        try {
                            transaction = session.beginTransaction();

                            // refresh db
                            Query<User> query = session.createQuery("from User", User.class);

                            List<User> list = query.list();

                            for(User item: list) {
                                session.delete(item);
                            }

                            transaction.commit();
                        }catch (HibernateException err) {
                            err.printStackTrace();
                            textarea.setText("Test failed.");
                            return;
                        }finally {
                            session.close();
                        }
                    }

                    testUser();
                    testInventory();
                }
            });

            /*
             * Container
             * */
            setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();

//            constraints.gridy = 0;
//            add(inputsPanel, constraints);

            constraints.gridy = 0;
            add(buttonsPanel, constraints);

            constraints.gridy = 1;
            add(textareaPanel, constraints);
        }

        /*
        * For each table, test each method, and common errors that might occur.
        * Major things to test: User authentication, Duplicate name / title, Distinguish user data from others.
        * These tests ARE NOT an exhaustive list. This only tests for major issues that may occur.
        * */

        private void resetTransactions() {
//            user = null;
//            userTransaction = null;
            inventoryTransaction = null;
            recipeTransaction = null;
            menuTransaction = null;
            menuItemTransaction = null;
        }

        private void addTestResult(String testTitle, String results, Boolean pass) {
            String str = "";
            if(pass) {
                str = "PASS";
            }else {
                str = "FAIL";
            }

            textarea.append(
                    "\n-------------------\n" +
                    testTitle + ":" +
                    "\nMessage: " + results +
                    "\nPass: " + str +
                    "\n-------------------\n"
            );
        }

        private void testUser() {
            int testId = 1;

            userTransaction = new UserTransaction(manager);

            // test user creation
            user = userTransaction.signup("test" + testId, "password");

            if(userTransaction.getError().hasError()) {
                addTestResult("Signup user", userTransaction.getError().getMessage(), false);
            }else {
                addTestResult("Signup user", "Id - " + user.getId() + " " + user.getUsername(), true);
            }

            // test user deletion (bad value)
            userTransaction.delete(-1);

            if(userTransaction.getError().hasError()) {
                addTestResult("Delete user (incorrect value)", userTransaction.getError().getMessage(), true);
            }else {
                addTestResult("Delete user (incorrect value)", "User deleted", false);
            }

            // test user creation (with previous id)
            userTransaction.signup("test" + testId, "password");
            testId++;

            if(userTransaction.getError().hasError()) {
                addTestResult("Signup user again", userTransaction.getError().getMessage(), true);
            }else {
                addTestResult("Signup user again", "User created again", false);
            }

            // test user deletion (good value)
            user = userTransaction.delete(user.getId());

            if(userTransaction.getError().hasError()) {
                addTestResult("Delete user again", userTransaction.getError().getMessage(), false);
            }else {
                addTestResult("Delete user again", "Id - " + user.getId() + " " + user.getUsername(), true);
            }

            // login (bad password)
            userTransaction.signup("test" + testId, "password");
            userTransaction.login("test" + testId, "foobar");

            if(userTransaction.getError().hasError()) {
                addTestResult("Login bad password", userTransaction.getError().getMessage(), true);
            }else {
                addTestResult("Login bad password", "Able to login with bad password", false);
            }

            // login (bad username)
            userTransaction.login("testy", "foobar");

            if(userTransaction.getError().hasError()) {
                addTestResult("Login bad username", userTransaction.getError().getMessage(), true);
            }else {
                addTestResult("Login bad username", "Able to login with bad username", false);
            }

            // login (success)
            user = userTransaction.login("test" + testId, "password");

            if(userTransaction.getError().hasError()) {
                addTestResult("Login success", userTransaction.getError().getMessage(), false);
            }else {
                addTestResult("Login success", "Id - " + user.getId() + " " + user.getUsername(), true);
            }

            // creating second user and adding dummy data
            testId--;
            user = userTransaction.signup("test" + testId, "password");
            testId++;

            inventoryTransaction = new InventoryTransaction(manager, user);

            inventoryTransaction.create(new Inventory("Spinach", 2));
            inventoryTransaction.create(new Inventory("Kale", 3));
            inventoryTransaction.create(new Inventory("Turkey", 4));
            inventoryTransaction.create(new Inventory("Egg", 5));
            inventoryTransaction.create(new Inventory("Pasta", 1));

            recipeTransaction = new RecipeTransaction(manager, user);

            Recipe r1 = recipeTransaction.create(new Recipe("Turkey Kale Pasta", "source1", "url1"));
            Recipe r2 = recipeTransaction.create(new Recipe("Spinach and Eggs", "source2", "url2", 10.00));
            Recipe r3 = recipeTransaction.create(new Recipe("Turkey and Eggs", "source3", "url3", 12.00));
            Recipe r4 = recipeTransaction.create(new Recipe("Egg Pasta", "source4", "url4", 8.00));

            menuTransaction = new MenuTransaction(manager, user);

            Menu m1 = menuTransaction.create(new Menu("Morning"));
            Menu m2 = menuTransaction.create(new Menu("Lunch"));

            menuItemTransaction = new MenuItemTransaction(manager, user);

            menuItemTransaction.create(new MenuItem(), m1, r1);
            menuItemTransaction.create(new MenuItem(), m1, r2);

            menuItemTransaction.create(new MenuItem(), m2, r2);
            menuItemTransaction.create(new MenuItem(), m2, r3);
            menuItemTransaction.create(new MenuItem(), m2, r4);

            // return back to test2 user
            user = userTransaction.login("test" + testId, "password");
            resetTransactions();
        }

        // main thing is to ensure the methods work, no access to test1 user, and no duplicate names (exception if its other user)
        // user right now = test2. this is true for the rest of the tests as well.
        private void testInventory() {
            inventoryTransaction = new InventoryTransaction(manager, user);

            // create inventory (good input)
            inventoryTransaction.create(new Inventory("Carrot", 3));

            if(inventoryTransaction.getError().hasError()) {
                addTestResult("User 2 create inventory", inventoryTransaction.getError().getMessage(), false);
            }else {
                addTestResult("User 2 create inventory", "Successfully created inventory", true);
            }

            // adding bacon for update tests
            inventoryTransaction.create(new Inventory("Bacon", 7));

            // create inventory (duplicate)
            inventoryTransaction.create(new Inventory("Carrot", 6));

            if(inventoryTransaction.getError().hasError()) {
                addTestResult("User 2 create inventory duplicate", inventoryTransaction.getError().getMessage(), true);
            }else {
                addTestResult("User 2 create inventory duplicate", "Successfully created duplicate inventory", false);
            }

            // create inventory (duplicate with other users)
            Inventory inventory = inventoryTransaction.create(new Inventory("Spinach", 6));

            if(inventoryTransaction.getError().hasError()) {
                addTestResult("User 2 create inventory duplicate with user 1", inventoryTransaction.getError().getMessage(), false);
            }else {
                addTestResult("User 2 create inventory duplicate with user 1", "Successfully created inventory", true);
            }

            // update inventory
            inventory = inventoryTransaction.update(new Inventory(inventory.getId(), "Kale", 9));

            if(inventoryTransaction.getError().hasError()) {
                addTestResult("Update Spinach to Kale", inventoryTransaction.getError().getMessage(), false);
            }else {
                addTestResult("Update Spinach to Kale", "Update successful", true);
            }

            // updating kale to bacon (duplicate inventory)
            inventoryTransaction.update(new Inventory(inventory.getId(), "Bacon", 9));

            if(inventoryTransaction.getError().hasError()) {
                addTestResult("Update Kale to Bacon (duplicate inventory)", inventoryTransaction.getError().getMessage(), true);
            }else {
                addTestResult("Update Kale to Bacon (duplicate inventory)", "Update successful", false);
            }

            // delete inventory
            inventoryTransaction.delete(inventory.getId());

            if(inventoryTransaction.getError().hasError()) {
                addTestResult("Delete Bacon", inventoryTransaction.getError().getMessage(), false);
            }else {
                addTestResult("Delete Bacon", "Delete successful", true);
            }

            // adding more inventory
            inventoryTransaction.create(new Inventory("Chicken", 3));
            inventoryTransaction.create(new Inventory("Naan", 5));
            inventoryTransaction.create(new Inventory("Tofu", 1));
        }

        // main thing is to ensure the methods work, no access to test1 user, and no duplicate names (exception if its other user)
        private void testRecipe() {

        }

        // main thing is to ensure the methods work, no access to test1 user, and no duplicate names (exception if its other user)
        private void testMenu() {

        }

        // main thing is to ensure the methods work, no access to test1 user, and no duplicate names (exception if its other user)
        private void testMenuItem() {

        }

    }
}

