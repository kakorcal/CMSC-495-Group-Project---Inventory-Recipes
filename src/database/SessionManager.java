package database;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

/**
 * Name: Kenneth Korcal
 * Date: 12/03/2017
 * Description:
 *
 * Loads environment variables, sets them to hibernate config file, creates hibernate session
 *
 */

/**
 * PLEASE READ: in order to connect to the local database, please follow the steps below
 *
 * 1. login to mysql through the terminal
 *
 * 2. open the schema directory and copy/paste the database.sql and inventory.sql into the mysql terminal
 *
 * 3. create a .env file in the same directory as hibernate.config.xml
 * and define the USERNAME, PASSWORD, AND DATABASE_NAME parameters:
 *
 * hibernate.connection.driver_class=com.mysql.jdbc.Driver
 * hibernate.connection.url=jdbc:mysql://localhost:3306/DATABASE_NAME
 * hibernate.connection.username=USERNAME
 * hibernate.connection.password=PASSWORD
 *
 * 4. Please do not check the hibernate.properties file into github as it contains private info.
 * I added this into the .gitignore so it should be fine but I just wanted to give a heads up.
 *
 * 5. the connection is successful if the GUI appears with no errors
 * */
public class SessionManager {

    private SessionFactory sessionFactory;

    private Properties loadEnv() throws Exception {
        Properties properties = new Properties();
        FileInputStream inputStream = null;

        try {
            System.out.println("Loading input stream");
            inputStream = new FileInputStream("src/.env");
            properties.load(inputStream);
            return properties;
        }catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }finally {
            System.out.println("Closing input stream");
            inputStream.close();
        }

    }

    private Configuration configureHibernate() throws Exception {
        Configuration configuration = new Configuration();
        Properties properties = loadEnv();

        configuration.configure("hibernate.config.xml")
                .setProperty("hibernate.connection.driver_class", properties.getProperty("hibernate.connection.driver_class"))
                .setProperty("hibernate.connection.url", properties.getProperty("hibernate.connection.url"))
                .setProperty("hibernate.connection.username", properties.getProperty("hibernate.connection.username"))
                .setProperty("hibernate.connection.password", properties.getProperty("hibernate.connection.password"))
                .addAnnotatedClass(Inventory.class);

        return configuration;
    }

    // https://docs.jboss.org/hibernate/orm/current/quickstart/html_single/
    // took code from example 4 in link above
    public void setup() throws Exception {
        // first load the env variables and set them into the hibernate config file
        Configuration configuration = configureHibernate();

        // A SessionFactory is set up once for an application!
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        try {
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }catch (Exception e) {
            e.printStackTrace();
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory so destroy it manually.
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
            throw new Exception(e);
        }
    }

    public void exit() {
        sessionFactory.close();
    }

    public SessionFactory getSessionFactory() { return sessionFactory; }
}
