package org.yolkin.repository.hibernate;

import org.hibernate.Session;
import org.yolkin.model.File;
import org.yolkin.repository.FileRepository;
import org.yolkin.util.HibernateSessionFactoryUtil;

import java.util.List;

public class HibernateFileRepositoryImpl implements FileRepository {
    @Override
    public List<File> getAll() {
        return null;
    }

    @Override
    public File create(File file) {
        return null;
    }

    @Override
    public File getById(Long aLong) {
        return null;
    }

    @Override
    public File update(File file) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    private Session getSession(){
        return HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }

}
