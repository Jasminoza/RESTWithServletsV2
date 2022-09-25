package org.yolkin.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.yolkin.model.EventEntity;
import org.yolkin.model.FileEntity;
import org.yolkin.model.UserEntity;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(EventEntity.class);
                configuration.addAnnotatedClass(FileEntity.class);
                configuration.addAnnotatedClass(UserEntity.class);

                StandardServiceRegistryBuilder builder =
                        new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties());

                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
        return sessionFactory;
    }
}