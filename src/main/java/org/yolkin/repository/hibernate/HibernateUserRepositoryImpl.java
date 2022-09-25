package org.yolkin.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.yolkin.model.UserEntity;
import org.yolkin.repository.UserRepository;
import org.yolkin.util.HibernateSessionFactoryUtil;

import java.util.List;

public class HibernateUserRepositoryImpl implements UserRepository {
    @Override
    public List<UserEntity> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("select u from UserEntity u left join fetch u.events", UserEntity.class).list();
        }
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        return saveUserToDB(userEntity);
    }

    @Override
    public UserEntity getById(Long id) {
        try (Session session = getSession()) {
            return session.get(UserEntity.class, id);
        }
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        return saveUserToDB(userEntity);
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

    private UserEntity saveUserToDB(UserEntity userEntity) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            userEntity = session.merge(userEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return ((userEntity.getId() != null) ? userEntity : null);
    }

    private Session getSession() {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }
}