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
    private Menu menu;
    private Recipe recipe;

    public MenuItemTransaction(SessionManager manager, Menu menu) {
        this.menu = menu;
        this.sessionFactory = manager.getSessionFactory();
    }

    public MenuItem create(MenuItem menuItem) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
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
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return menuItem;
    }

    public MenuItem read(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        MenuItem menuItem;

        try {
            transaction = session.beginTransaction();

            menuItem = session.get(MenuItem.class, id);

            if(menuItem != null && menuItem.getMenu().getId() == menu.getId()) {
                System.out.println("Menu item found: ");
                System.out.println(menuItem.toString());
            }else {
                System.out.println("Menu item id: " + id + " does not exist");
                menuItem = null;
            }

            transaction.commit();
        }catch(HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return menuItem;
    }

    public MenuItem update(MenuItem menuItem) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        MenuItem newMenuItem;

        try {
            transaction = session.beginTransaction();
            long id = menuItem.getId();
            newMenuItem = session.get(MenuItem.class, id);

            if(newMenuItem != null && newMenuItem.getMenu().getId() == menu.getId()) {
                newMenuItem.setPrice(menuItem.getPrice());
                session.update(newMenuItem);
                System.out.println("Menu Item updated: ");
                System.out.println(newMenuItem.toString());
            }else {
                System.out.println("Menu item id: " + id + " does not exist");
                newMenuItem = null;
            }

            transaction.commit();
        }catch (HibernateException e){
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return newMenuItem;
    }

    public MenuItem delete(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        MenuItem menuItem;

        try {
            transaction = session.beginTransaction();

            menuItem = session.get(MenuItem.class, id);

            if(menuItem != null && menuItem.getMenu().getId() == menu.getId()) {
                session.delete(menuItem);
                System.out.println("Menu item deleted: ");
                System.out.println(menuItem.toString());
            }else {
                System.out.println("Menu item id: " + id + " does not exist");
                menuItem = null;
            }

            transaction.commit();
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return menuItem;
    }

    public List<MenuItem> list() throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<MenuItem> list;

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
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return list;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}