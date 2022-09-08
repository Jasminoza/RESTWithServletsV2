package org.yolkin.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.yolkin.model.File;
import org.yolkin.repository.FileRepository;
import org.yolkin.util.HibernateSessionFactoryUtil;

import java.util.List;

public class HibernateFileRepositoryImpl implements FileRepository {
    @Override
    public List<File> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("From File", File.class).list();
        }
    }

    @Override
    public File create(File file) {
        return saveFileToDB(file);
    }

    @Override
    public File getById(Long id) {
        try (Session session = getSession()) {
            return session.get(File.class, id);
        }
    }

    @Override
    public File update(File file) {
        return saveFileToDB(file);
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

    private File saveFileToDB(File file) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            file = session.merge(file);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return ((file.getId() != null) ? file : null);
    }

    private Session getSession(){
        return HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }

}