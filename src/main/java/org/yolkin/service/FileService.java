package org.yolkin.service;

import org.yolkin.model.File;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;

import java.util.List;

public class FileService {
    private final FileRepository fileRepository;

    public FileService() {
        fileRepository = new HibernateFileRepositoryImpl();
    }

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> getAll() {
        return fileRepository.getAll();
    }

    public File create(File file) {
        return fileRepository.create(file);
    }

    public File getById(Long id) {
        return fileRepository.getById(id);
    }

    public File update(File file) {
        return fileRepository.update(file);
    }

    public void delete(Long id) {
        fileRepository.delete(id);
    } 
}