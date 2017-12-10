package database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class InventoryTransaction {
    private SessionFactory sessionFactory;
    private User user;

    public InventoryTransaction(User user, SessionManager manager) {
        this.user = user;
        this.sessionFactory = manager.getSessionFactory();
    }

    public Inventory create(Inventory inventory) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            inventory.setUser(user);
            session.save(inventory);
            transaction.commit();
            System.out.println("Inventory created: ");
            System.out.println(inventory.toString());
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return inventory;
    }

    public Inventory read(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Inventory inventory;

        try {
            transaction = session.beginTransaction();

            inventory = session.get(Inventory.class, id);

            if(inventory != null) {
                System.out.println("Inventory found: ");
                System.out.println(inventory.toString());
            }else {
                System.out.println("Inventory id: " + id + " does not exist");
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

        return inventory;
    }

    public Inventory update(Inventory inventory) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Inventory newInventory;

        try {
            transaction = session.beginTransaction();
            long id = inventory.getId();
            newInventory = session.get(Inventory.class, id);

            if(newInventory != null) {
                newInventory.setName(inventory.getName());
                newInventory.setQuantity(inventory.getQuantity());
                session.update(newInventory);
                System.out.println("Inventory updated: ");
                System.out.println(newInventory.toString());
            }else {
                System.out.println("Inventory id: " + id + " does not exist");
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

        return newInventory;
    }

    public Inventory delete(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Inventory inventory;

        try {
            transaction = session.beginTransaction();

            inventory = session.get(Inventory.class, id);

            if(inventory != null) {
                session.delete(inventory);
                System.out.println("Inventory deleted: ");
                System.out.println(inventory.toString());
            }else {
                System.out.println("Inventory id: " + id + " does not exist");
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

        return inventory;
    }

    public List<Inventory> list() throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Inventory> list;

        try {
            transaction = session.beginTransaction();
            Query<Inventory> query = session.createQuery("from Inventory", Inventory.class);
            list = query.list();

            for(Inventory item: list) {
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
