package database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class MenuItemTransaction {
    /*
     *  how to insert list of menu items
     *  1. create the menu
     *  2. loop through each selected recipe
     *  3. add menu item to database
     * */

    private SessionFactory sessionFactory;
    private ErrorHandler error;
    private User user;
    
    public MenuItemTransaction(SessionManager manager, User user) {
        this.user = user;
        this.sessionFactory = manager.getSessionFactory();
        this.error = new ErrorHandler();
    }

    public MenuItem create(MenuItem menuItem, Menu menu, Recipe recipe) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to create menu.");
            return null;
        }

        if(menu == null) {
            error.setMessage("Please select a menu.");
            return null;
        }

        if(user.getId() != menu.getUser().getId()) {
            error.setMessage("Unauthorized to read menu item.");
            return null;
        }

        if(recipe == null) {
            error.setMessage("Please select a recipe.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            // check for dups
            Query<MenuItem> query = session.createQuery("FROM MenuItem M WHERE M.menu.id = :menu_id AND M.recipe.id = :recipe_id", MenuItem.class);
            query.setParameter("menu_id", menu.getId());
            query.setParameter("recipe_id", recipe.getId());
            List<MenuItem> list = query.list();

            if(list.isEmpty()) {
                error.setMessage("Cannot have duplicate menu items.");
                return null;
            }

            menuItem.setMenu(menu);
            menuItem.setRecipe(recipe);
            session.save(menuItem);
            transaction.commit();
            System.out.println("Menu item created: ");
            System.out.println(menuItem.toString());
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to create menu item.");
            return null;
        }finally {
            session.close();
        }

        return menuItem;
    }

    public MenuItem read(long id, Menu menu) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        MenuItem menuItem;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to read menu item.");
            return null;
        }

        if(menu == null) {
            error.setMessage("Please select a menu.");
            return null;
        }

        if(user.getId() != menu.getUser().getId()) {
            error.setMessage("Unauthorized to read menu item.");
            return null;
        }

        if(id < 0) {
            error.setMessage("Invalid menu item identifier.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            menuItem = session.get(MenuItem.class, id);

            if(menuItem != null && menuItem.getMenu().getId() == menu.getId()) {
                System.out.println("Menu item found: ");
                System.out.println(menuItem.toString());
            }else {
                System.out.println("Menu item id: " + id + " does not exist");
                error.setMessage("Specified menu item does not exist.");
                return null;
            }

            transaction.commit();
        }catch(HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to retrieve menu item data.");
            return null;
        }finally {
            session.close();
        }

        return menuItem;
    }

//    public MenuItem update(MenuItem menuItem, Menu menu) throws HibernateException {
//        Session session = sessionFactory.openSession();
//        Transaction transaction = null;
//        MenuItem newMenuItem;
//
//        try {
//            transaction = session.beginTransaction();
//            long id = menuItem.getId();
//            newMenuItem = session.get(MenuItem.class, id);
//
//            if(newMenuItem != null && newMenuItem.getMenu().getId() == menu.getId()) {
//                newMenuItem.setPrice(menuItem.getPrice());
//                session.update(newMenuItem);
//                System.out.println("Menu Item updated: ");
//                System.out.println(newMenuItem.toString());
//            }else {
//                System.out.println("Menu item id: " + id + " does not exist");
//                newMenuItem = null;
//            }
//
//            transaction.commit();
//        }catch (HibernateException e){
//            if(transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//            throw new HibernateException(e);
//        }finally {
//            session.close();
//        }
//
//        return newMenuItem;
//    }

    public MenuItem delete(long id, Menu menu) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        MenuItem menuItem;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to read menu item.");
            return null;
        }

        if(menu == null) {
            error.setMessage("Please select a menu.");
            return null;
        }

        if(user.getId() != menu.getUser().getId()) {
            error.setMessage("Unauthorized to read menu item.");
            return null;
        }

        if(id < 0) {
            error.setMessage("Invalid menu item identifier.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            menuItem = session.get(MenuItem.class, id);

            if(menuItem != null && menuItem.getMenu().getId() == menu.getId()) {
                session.delete(menuItem);
                System.out.println("Menu item deleted: ");
                System.out.println(menuItem.toString());
            }else {
                System.out.println("Menu item id: " + id + " does not exist");
                error.setMessage("Specified menu item does not exist.");
                return null;
            }

            transaction.commit();
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to delete menu item.");
            return null;
        }finally {
            session.close();
        }

        return menuItem;
    }

    public List<MenuItem> list(Menu menu) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<MenuItem> list;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to read menu item.");
            return null;
        }

        if(menu == null) {
            error.setMessage("Please select a menu.");
            return null;
        }

        if(user.getId() != menu.getUser().getId()) {
            error.setMessage("Unauthorized to read menu item.");
            return null;
        }

        try {
            transaction = session.beginTransaction();
            Query<MenuItem> query = session.createQuery("FROM MenuItem M WHERE M.menu.id = :menu_id", MenuItem.class);
            query.setParameter("menu_id", menu.getId());
            list = query.list();

            for(MenuItem item: list) {
                System.out.println(item.toString());
            }

            transaction.commit();
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to retrieve menu data.");
            return null;
        }finally {
            session.close();
        }

        return list;
    }

    public ErrorHandler getError() {
        return error;
    }

    public void setError(ErrorHandler error) {
        this.error = error;
    }

    //    public Menu getMenu() {
//        return menu;
//    }
//
//    public void setMenu(Menu menu) {
//        this.menu = menu;
//    }
//
//    public Recipe getRecipe() {
//        return recipe;
//    }
//
//    public void setRecipe(Recipe recipe) {
//        this.recipe = recipe;
//    }
}