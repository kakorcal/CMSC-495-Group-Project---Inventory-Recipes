package database;

import gui.AuthFormController;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserTransaction extends AuthFormController {
    private SessionFactory sessionFactory;
    private ErrorHandler error;
    private static final int SALT_WORK_FACTOR = 10;

    public UserTransaction(SessionManager manager) {
        this.sessionFactory = manager.getSessionFactory();
        this.error = new ErrorHandler();
    }

    public User signup(String username, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        User user;
        error.reset();

        if(username.length() < 5) {
            error.setMessage("Username must contain at least 5 characters.");
            return null;
        }

        if(password.length() < 5) {
            error.setMessage("Username must contain at least 5 characters.");
            return null;
        }

        try {
            transaction = session.beginTransaction();
            String hash = BCrypt.hashpw(password, BCrypt.gensalt(SALT_WORK_FACTOR));

            user = new User();
            user.setUsername(username);
            user.setPassword(hash);
            session.save(user);
            transaction.commit();
            System.out.println("User created: ");
            System.out.println(user.toString());
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Username already exists.");
            return null;
        }finally {
            session.close();
        }

        user.setPassword(null);
        return user;
    }

    public User login(String username, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        error.reset();

        if(username.length() < 5) {
            error.setMessage("Username must contain at least 5 characters.");
            return null;
        }

        if(password.length() < 5) {
            error.setMessage("Username must contain at least 5 characters.");
            return null;
        }

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
                error.setMessage("Username does not exist.");
                return null;
            }else {
                // throw error if password is incorrect
                User user = (User) results.get(0);

                if(!BCrypt.checkpw(password, user.getPassword())) {
                    System.out.println("Incorrect password");
                    error.setMessage("Incorrect password.");
                    return null;
                }else {
                    System.out.println("Login successful");
                    user.setPassword(null);
                    return user;
                }
            }
        }catch(HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Login failed. Please try again.");
            return null;
        }finally {
            session.close();
        }
    }

    public User delete(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        User user;
        error.reset();

        try {
            transaction = session.beginTransaction();

            user = session.get(User.class, id);

            if(user != null) {
                session.delete(user);
                System.out.println("User deleted: ");
                System.out.println(user.toString());
            }else {
                System.out.println("User id: " + id + " does not exist");
                error.setMessage("User does not exist.");
                return null;
            }

            transaction.commit();
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to delete user.");
            return null;
        }finally {
            session.close();
        }

        return user;
    }

    public ErrorHandler getError() {
        return error;
    }

    public void setError(ErrorHandler error) {
        this.error = error;
    }
}
