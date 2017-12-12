package database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class MenuTransaction {
    private SessionFactory sessionFactory;
    private User user;
    private ErrorHandler error;

    public MenuTransaction(SessionManager manager, User user) {
        this.user = user;
        this.sessionFactory = manager.getSessionFactory();
        this.error = new ErrorHandler();
    }

    public Menu create(Menu menu) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to create menu.");
            return null;
        }

        if(menu.getName().isEmpty()) {
            error.setMessage("Please enter a menu name.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            // check for dups
            Query<Menu> query = session.createQuery("FROM Menu M WHERE M.user.id = :user_id AND M.name = :menu_name", Menu.class);
            query.setParameter("user_id", user.getId());
            query.setParameter("menu_name", menu.getName());
            List<Menu> list = query.list();

            if(!list.isEmpty()) {
                error.setMessage("Cannot have duplicate menu names.");
                return null;
            }

            menu.setUser(user);
            session.save(menu);
            transaction.commit();
            System.out.println("Menu created: ");
            System.out.println(menu.toString());
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to create menu.");
            return null;
        }finally {
            session.close();
        }

        return menu;
    }

    public Menu read(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Menu menu;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to read menu.");
            return null;
        }

        if(id < 0) {
            error.setMessage("Invalid recipe identifier.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            menu = session.get(Menu.class, id);

            if(menu != null && menu.getUser().getId() == user.getId()) {
                System.out.println("Menu found: ");
                System.out.println(menu.toString());
            }else {
                System.out.println("Menu id: " + id + " does not exist");
                error.setMessage("Specified menu does not exist.");
                return null;
            }

            transaction.commit();
        }catch(HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to retrieve menu data.");
            return null;
        }finally {
            session.close();
        }

        return menu;
    }

    public Menu update(Menu menu) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Menu newMenu;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to update menu.");
            return null;
        }

        if(menu.getName().isEmpty()) {
            error.setMessage("Please enter a menu name.");
            return null;
        }

        try {
            transaction = session.beginTransaction();
            long id = menu.getId();
            newMenu = session.get(Menu.class, id);

            if(newMenu != null && newMenu.getUser().getId() == user.getId()) {

                // check for dups
                Query<Menu> query = session.createQuery("FROM Menu M WHERE M.user.id = :user_id AND M.name = :menu_name", Menu.class);
                query.setParameter("user_id", user.getId());
                query.setParameter("menu_name", menu.getName());
                List<Menu> list = query.list();

                if(!list.isEmpty()) {
                    error.setMessage("Cannot have duplicate menu names.");
                    return null;
                }

                newMenu.setName(newMenu.getName());
                session.update(newMenu);
                System.out.println("Menu updated: ");
                System.out.println(newMenu.toString());
            }else {
                System.out.println("Menu id: " + id + " does not exist");
                error.setMessage("Cannot identify menu.");
                return null;
            }

            transaction.commit();
        }catch (HibernateException e){
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed update menu.");
            return null;
        }finally {
            session.close();
        }

        return newMenu;
    }

    public Menu delete(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Menu menu;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to delete menu.");
            return null;
        }

        if(id < 0) {
            error.setMessage("Invalid menu identifier.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            menu = session.get(Menu.class, id);

            if(menu != null && menu.getUser().getId() == user.getId()) {
                session.delete(menu);
                System.out.println("Menu deleted: ");
                System.out.println(menu.toString());
            }else {
                System.out.println("Menu id: " + id + " does not exist");
                error.setMessage("Specified menu does not exist.");
                return null;
            }

            transaction.commit();
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to delete menu.");
            return null;
        }finally {
            session.close();
        }

        return menu;
    }

    public List<Menu> list() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Menu> list;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to read recipe.");
            return null;
        }

        try {
            transaction = session.beginTransaction();
            Query<Menu> query = session.createQuery("FROM Menu M WHERE M.user.id = :user_id", Menu.class);
            query.setParameter("user_id", user.getId());
            list = query.list();

            for(Menu item: list) {
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
}
