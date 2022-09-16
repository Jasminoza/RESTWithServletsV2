package org.yolkin.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.yolkin.model.User;
import org.yolkin.repository.UserRepository;
import org.yolkin.util.HibernateSessionFactoryUtil;

import java.util.List;

public class HibernateUserRepositoryImpl implements UserRepository {
    @Override
    public List<User> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("From User", User.class).list();
        }
    }

    @Override
    public User create(User user) {
        return saveUserToDB(user);
    }

    @Override
    public User getById(Long id) {
        try (Session session = getSession()) {
            return session.get(User.class, id);
        }
    }

    @Override
    public User update(User user) {
        return saveUserToDB(user);
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.remove(getById(id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    private User saveUserToDB(User user) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            user = session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return ((user.getId() != null) ? user : null);
    }

    private Session getSession(){
        try {
            return HibernateSessionFactoryUtil.getSessionFactory().openSession();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}