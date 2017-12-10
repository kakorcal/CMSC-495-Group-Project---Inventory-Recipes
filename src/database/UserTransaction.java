package database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserTransaction {
    private SessionFactory sessionFactory;

    public UserTransaction(SessionManager manager) {
        this.sessionFactory = manager.getSessionFactory();
    }

    public long signup(String username, String password) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        long id;

        try {
            transaction = session.beginTransaction();
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            session.save(user);
            transaction.commit();
            id = user.getId();
            System.out.println("User created: ");
            System.out.println(user.toString());
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return id;
    }

    public long login(String username, String password) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            String hql = "FROM User U WHERE U.username = :username";
            Query query = session.createQuery(hql);
            query.setParameter("username", username);
            List results = query.list();
            transaction.commit();

            // throw error if username does not exist
            if(results.isEmpty()) {
                System.out.println("Username: " + username + " does not exist.");
                return -1;
            }else {
                // throw error if password is incorrect
                User user = (User) results.get(0);
                if(!password.equals(user.getPassword())) {
                    System.out.println("Incorrect password.");
                    return -1;
                }else {
                    System.out.println("Login successful");
                    return user.getId();
                }
            }
        }catch(HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new HibernateException(e);
        }finally {
            session.close();
        }
    }

    public User delete(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        User user;

        try {
            transaction = session.beginTransaction();

            user = session.get(User.class, id);

            if(user != null) {
                session.delete(user);
                System.out.println("User deleted: ");
                System.out.println(user.toString());
            }else {
                System.out.println("User id: " + id + " does not exist");
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

        return user;
    }
}
