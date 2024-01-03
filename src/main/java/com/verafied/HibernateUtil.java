package com.verafied;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	
	private HibernateUtil(){}

	//Property based configuration
	private static SessionFactory sessionJavaConfigFactory;


    private static SessionFactory buildSessionJavaConfigFactory() {
    	try {
    	Configuration configuration = new Configuration();
		
		//Create Properties, can be read from property files too
		Properties props = new Properties();
		props.put("hibernate.connection.driver_class", "org.h2.Driver");
		props.put("hibernate.connection.url", "jdbc:h2:file:"+SettingsHandler.APPLICATION_PATH+"\\db");
		props.put("hibernate.connection.username", "verafied");
		props.put("hibernate.connection.password", "1234");
		props.put("hibernate.show_sql", true);
		props.put("hibernate.hbm2ddl.auto", "update");
		//props.put("hibernate.event.merge.entity_copy_observer", "allow");
        props.put("hibernate.connection.pool_size", 1);
        props.put("show_sql", true);
		props.put("hibernate.current_session_context_class", "thread");
		configuration.setProperties(props);
		
		//we can set mapping file or class with annotation
		//addClass(Employee1.class) will look for resource
		// com/journaldev/hibernate/model/Employee1.hbm.xml (not good)
		configuration.addAnnotatedClass(Song.class);
        configuration.addAnnotatedClass(Album.class);
        configuration.addAnnotatedClass(Artist.class);
		
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    	System.out.println("Hibernate Java Config serviceRegistry created");
    	
    	sessionJavaConfigFactory = configuration.buildSessionFactory(serviceRegistry);
    	
        return sessionJavaConfigFactory;
    	}
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
	}
	
	public static SessionFactory getSessionJavaConfigFactory() {
		return sessionJavaConfigFactory == null ? buildSessionJavaConfigFactory() : sessionJavaConfigFactory;
    }
	
}