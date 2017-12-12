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
    private ErrorHandler error;

    public RecipeTransaction(SessionManager manager, User user) {
        this.user = user;
        this.sessionFactory = manager.getSessionFactory();
        this.error = new ErrorHandler();
    }

    public Recipe create(Recipe recipe) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to create recipe.");
            return null;
        }

        if(recipe.getTitle().isEmpty()) {
            error.setMessage("Please enter the recipe title.");
            return null;
        }

        // TODO: need to test this. how to check if null?
        if(recipe.getPrice() < 0) {
            error.setMessage("Recipe price must be greater than 0.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            // check for dups
            Query<Recipe> query = session.createQuery("FROM Recipe R WHERE R.user.id = :user_id AND R.title = :recipe_title", Recipe.class);
            query.setParameter("user_id", user.getId());
            query.setParameter("recipe_title", recipe.getTitle());
            List<Recipe> list = query.list();

            if(!list.isEmpty()) {
                error.setMessage("Cannot have duplicate recipe titles.");
                return null;
            }

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
            error.setMessage("Failed to create recipe.");
            return null;
        }finally {
            session.close();
        }

        return recipe;
    }

    public Recipe read(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Recipe recipe;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to read recipe.");
            return null;
        }

        if(id < 0) {
            error.setMessage("Invalid recipe identifier.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            recipe = session.get(Recipe.class, id);

            if(recipe != null && recipe.getUser().getId() == user.getId()) {
                System.out.println("Recipe found: ");
                System.out.println(recipe.toString());
            }else {
                System.out.println("Recipe id: " + id + " does not exist");
                error.setMessage("Specified recipe does not exist.");
                return null;
            }

            transaction.commit();
        }catch(HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to retrieve recipe data.");
            return null;
        }finally {
            session.close();
        }

        return recipe;
    }

    public Recipe update(Recipe recipe) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Recipe newRecipe;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to update recipe.");
            return null;
        }

        // Required to add title and price even if same value
        if(recipe.getTitle().isEmpty()) {
            error.setMessage("Please enter the recipe title.");
            return null;
        }

        if(recipe.getPrice() < 0) {
            error.setMessage("Recipe price must be greater than 0.");
            return null;
        }

        try {
            transaction = session.beginTransaction();
            long id = recipe.getId();
            newRecipe = session.get(Recipe.class, id);

            if(newRecipe != null && newRecipe.getUser().getId() == user.getId()) {

                if(newRecipe.getTitle().equals(newRecipe.getTitle())) {
                    error.setMessage("Recipe title must be unique.");
                    return null;
                }

                newRecipe.setTitle(recipe.getTitle());
                newRecipe.setSourceUrl(recipe.getSourceUrl());
                newRecipe.setImageUrl(recipe.getImageUrl());
                newRecipe.setPrice(recipe.getPrice());
                session.update(newRecipe);
                System.out.println("Recipe updated: ");
                System.out.println(newRecipe.toString());
            }else {
                System.out.println("Recipe id: " + id + " does not exist");
                error.setMessage("Cannot identify recipe.");
                return null;
            }

            transaction.commit();
        }catch (HibernateException e){
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to update recipe.");
            return null;
        }finally {
            session.close();
        }

        return newRecipe;
    }

    public Recipe delete(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Recipe recipe;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to delete recipe.");
            return null;
        }

        if(id < 0) {
            error.setMessage("Invalid recipe identifier.");
            return null;
        }

        try {
            transaction = session.beginTransaction();

            recipe = session.get(Recipe.class, id);

            if(recipe != null && recipe.getUser().getId() == user.getId()) {
                session.delete(recipe);
                System.out.println("Recipe deleted: ");
                System.out.println(recipe.toString());
            }else {
                System.out.println("Recipe id: " + id + " does not exist");
                error.setMessage("Specified recipe does not exist.");
                return null;
            }

            transaction.commit();
        }catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            error.setMessage("Failed to delete recipe.");
            return null;
        }finally {
            session.close();
        }

        return recipe;
    }

    public List<Recipe> list() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Recipe> list;
        error.reset();

        if(user == null) {
            error.setMessage("Unauthorized to read recipe.");
            return null;
        }

        try {
            transaction = session.beginTransaction();
            Query<Recipe> query = session.createQuery("FROM Recipe R WHERE R.user.id = :user_id", Recipe.class);
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
            error.setMessage("Failed to retrieve recipe data.");
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
