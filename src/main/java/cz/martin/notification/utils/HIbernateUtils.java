package cz.martin.notification.utils;

import cz.martin.notification.entity.Container;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HIbernateUtils {
    private static SessionFactory sessionFactory=null;

    public static SessionFactory getSessionFactory() {

        try {
            if (sessionFactory == null) {
                //create config
                Configuration configuration = new Configuration();
                configuration.configure();
                configuration.addAnnotatedClass(Container.class);
                // create session factory
                sessionFactory = configuration.buildSessionFactory();
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Something wrong with config file.");
        }
        return sessionFactory;
    }
}
