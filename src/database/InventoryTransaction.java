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
    private ErrorHandler error;

    public InventoryTransaction(SessionManager manager, User user) {
        this.user = user;
        this.sessionFactory = manager.getSessionFactory();
        this.error = new ErrorHandler();
    }

    public Inventory create(Inventory inventory) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        error.reset();

        // input validations
        if(user == null) {
            error.setMessage("Unauthorized to create inventory.");
            return null;
        }

        if(inventory.getName().isEmpty()) {
            error.setMessage("Please enter an inventory name.");
            return null;
        }

        if(inventory.getQuantity() < 0) {
            error.setMessage("Quantity must be larger than 0.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            // query to check if duplicate name exists
            Query<Inventory> query = session.createQuery("FROM Inventory I WHERE I.user.id = :user_id AND I.name = :inventory_name", Inventory.class);
            query.setParameter("user_id", user.getId());
            query.setParameter("inventory_name", inventory.getName());
            List<Inventory> list = query.list();

            if(!list.isEmpty()) {
                error.setMessage("Cannot have duplicate inventory names.");
                return null;
            }

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
            error.setMessage("Failed to create inventory.");
            return null;
        }finally {
            session.close();
        }

        return inventory;
    }

    public Inventory read(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Inventory inventory;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to read inventory.");
            return null;
        }

        if(id < 0) {
            error.setMessage("Invalid inventory identifier.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            inventory = session.get(Inventory.class, id);

            if(inventory != null && inventory.getUser().getId() == user.getId()) {
                System.out.println("Inventory found: ");
                System.out.println(inventory.toString());
            }else {
                System.out.println("Inventory id: " + id + " does not exist");
                error.setMessage("Specified inventory does not exist.");
                return null;
            }

            transaction.commit();
        }catch(HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to retrieve inventory data.");
            return null;
        }finally {
            session.close();
        }

        return inventory;
    }

    public Inventory update(Inventory inventory) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Inventory newInventory;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to update inventory.");
            return null;
        }

        // TODO: tricky here. if we do this, we need to require user to include values that don't need to update for now, we will go with this for simplicity.
        if(inventory.getName().isEmpty()) {
            error.setMessage("Please enter an inventory name.");
            return null;
        }

        if(inventory.getQuantity() < 0) {
            error.setMessage("Quantity must be larger than 0.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            long id = inventory.getId();
            newInventory = session.get(Inventory.class, id);

            if(newInventory != null && newInventory.getUser().getId() == user.getId()) {

                // query to check if duplicate name exists
                Query<Inventory> query = session.createQuery("FROM Inventory I WHERE I.user.id = :user_id AND I.name = :inventory_name", Inventory.class);
                query.setParameter("user_id", user.getId());
                query.setParameter("inventory_name", inventory.getName());
                List<Inventory> list = query.list();

                if(!list.isEmpty()) {
                    error.setMessage("Cannot have duplicate inventory names.");
                    return null;
                }

                newInventory.setName(inventory.getName());
                newInventory.setQuantity(inventory.getQuantity());
                session.update(newInventory);
                System.out.println("Inventory updated: ");
                System.out.println(newInventory.toString());
            }else {
                System.out.println("Inventory id: " + id + " does not exist");
                error.setMessage("Cannot identify inventory.");
                return null;
            }

            transaction.commit();
        }catch (HibernateException e){
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to update inventory.");
            return null;
        }finally {
            session.close();
        }

        return newInventory;
    }

    public Inventory delete(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Inventory inventory;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to delete inventory.");
            return null;
        }

        if(id < 0) {
            error.setMessage("Invalid inventory identifier.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            inventory = session.get(Inventory.class, id);

            if(inventory != null && inventory.getUser().getId() == user.getId()) {
                session.delete(inventory);
                System.out.println("Inventory deleted: ");
                System.out.println(inventory.toString());
            }else {
                System.out.println("Inventory id: " + id + " does not exist");
                error.setMessage("Specified inventory does not exist.");
                return null;
            }

            transaction.commit();
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to delete inventory.");
            return null;
        }finally {
            session.close();
        }

        return inventory;
    }

    public List<Inventory> list() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Inventory> list;

        if(user == null) {
            error.setMessage("Unauthorized to read inventory.");
            return null;
        }

        try {
            transaction = session.beginTransaction();
            Query<Inventory> query = session.createQuery("FROM Inventory I WHERE I.user.id = :user_id", Inventory.class);
            query.setParameter("user_id", user.getId());
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
            error.setMessage("Failed to retrieve inventory data.");
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