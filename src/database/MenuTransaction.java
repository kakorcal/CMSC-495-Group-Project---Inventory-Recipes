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

    public MenuTransaction(SessionManager manager, User user) {
        this.user = user;
        this.sessionFactory = manager.getSessionFactory();
    }

    public Menu create(Menu menu) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
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
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return menu;
    }

    public Menu read(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Menu menu;

        try {
            transaction = session.beginTransaction();

            menu = session.get(Menu.class, id);

            if(menu != null && menu.getUser().getId() == user.getId()) {
                System.out.println("Menu found: ");
                System.out.println(menu.toString());
            }else {
                System.out.println("Menu id: " + id + " does not exist");
                menu = null;
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

        return menu;
    }

    public Menu update(Menu menu) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Menu newMenu;

        try {
            transaction = session.beginTransaction();
            long id = menu.getId();
            newMenu = session.get(Menu.class, id);

            if(newMenu != null && newMenu.getUser().getId() == user.getId()) {
                newMenu.setName(newMenu.getName());
                session.update(newMenu);
                System.out.println("Menu updated: ");
                System.out.println(newMenu.toString());
            }else {
                System.out.println("Menu id: " + id + " does not exist");
                newMenu = null;
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

        return newMenu;
    }

    public Menu delete(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Menu menu;

        try {
            transaction = session.beginTransaction();

            menu = session.get(Menu.class, id);

            if(menu != null && menu.getUser().getId() == user.getId()) {
                session.delete(menu);
                System.out.println("Menu deleted: ");
                System.out.println(menu.toString());
            }else {
                System.out.println("Menu id: " + id + " does not exist");
                menu = null;
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

        return menu;
    }

    public List<Menu> list() throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Menu> list;

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
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return list;
    }
}
