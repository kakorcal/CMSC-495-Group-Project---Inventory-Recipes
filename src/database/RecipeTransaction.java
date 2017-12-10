package database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RecipeTransaction {
    private SessionFactory sessionFactory;
    private User user;

    public RecipeTransaction(SessionManager manager, User user) {
        this.user = user;
        this.sessionFactory = manager.getSessionFactory();
    }

    public Recipe create(Recipe recipe) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            recipe.setUser(user);
            session.save(recipe);
            transaction.commit();
            System.out.println("Recipe created: ");
            System.out.println(recipe.toString());
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new HibernateException(e);
        }finally {
            session.close();
        }

        return recipe;
    }

    public Recipe read(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Recipe recipe;

        try {
            transaction = session.beginTransaction();

            recipe = session.get(Recipe.class, id);

            if(recipe != null && recipe.getUser().getId() == user.getId()) {
                System.out.println("Recipe found: ");
                System.out.println(recipe.toString());
            }else {
                System.out.println("Recipe id: " + id + " does not exist");
                recipe = null;
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

        return recipe;
    }

    public Recipe update(Recipe recipe) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Recipe newRecipe;

        try {
            transaction = session.beginTransaction();
            long id = recipe.getId();
            newRecipe = session.get(Recipe.class, id);

            if(newRecipe != null && newRecipe.getUser().getId() == user.getId()) {
                newRecipe.setTitle(recipe.getTitle());
                newRecipe.setSourceUrl(recipe.getSourceUrl());
                newRecipe.setImageUrl(recipe.getImageUrl());
                session.update(newRecipe);
                System.out.println("Recipe updated: ");
                System.out.println(newRecipe.toString());
            }else {
                System.out.println("Recipe id: " + id + " does not exist");
                newRecipe = null;
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

        return newRecipe;
    }

    public Recipe delete(long id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Recipe recipe;

        try {
            transaction = session.beginTransaction();

            recipe = session.get(Recipe.class, id);

            if(recipe != null && recipe.getUser().getId() == user.getId()) {
                session.delete(recipe);
                System.out.println("Recipe deleted: ");
                System.out.println(recipe.toString());
            }else {
                System.out.println("Recipe id: " + id + " does not exist");
                recipe = null;
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

        return recipe;
    }

    public List<Recipe> list() throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Recipe> list;

        try {
            transaction = session.beginTransaction();
            Query<Recipe> query = session.createQuery("FROM Recipe I WHERE I.user.id = :user_id", Recipe.class);
            query.setParameter("user_id", user.getId());
            list = query.list();

            for(Recipe item: list) {
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
