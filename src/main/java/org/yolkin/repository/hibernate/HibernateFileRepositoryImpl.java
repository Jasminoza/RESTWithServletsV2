package org.yolkin.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.yolkin.model.FileEntity;
import org.yolkin.repository.FileRepository;
import org.yolkin.util.HibernateSessionFactoryUtil;

import java.util.List;

public class HibernateFileRepositoryImpl implements FileRepository {
    @Override
    public List<FileEntity> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("select f from FileEntity f left join fetch f.events", FileEntity.class).list();
        }
    }

    @Override
    public FileEntity create(FileEntity fileEntity) {
        return saveFileToDB(fileEntity);
    }

    @Override
    public FileEntity getById(Long id) {
        try (Session session = getSession()) {
            return session.get(FileEntity.class, id);
        }
    }

    @Override
    public FileEntity update(FileEntity fileEntity) {
        return saveFileToDB(fileEntity);
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

    private FileEntity saveFileToDB(FileEntity fileEntity) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            fileEntity = session.merge(fileEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return ((fileEntity.getId() != null) ? fileEntity : null);
    }

    private Session getSession(){
        return HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }

}