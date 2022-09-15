package org.yolkin.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.util.Date;

public class ServiceHelper {

    public ServletFileUpload setupUploader(String PATH_FOR_UPLOADING, int MAX_MEMORY_SIZE, int MAX_FILE_SIZE) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(new java.io.File(PATH_FOR_UPLOADING));
        diskFileItemFactory.setSizeThreshold(MAX_MEMORY_SIZE);

        ServletFileUpload uploader = new ServletFileUpload(diskFileItemFactory);
        uploader.setSizeMax(MAX_FILE_SIZE);
        return uploader;
    }

    public java.io.File getFileFromRequest(FileItem fileItem, String PATH_FOR_UPLOADING, Date date) {
        java.io.File realFile;

        String fileName = fileItem.getName();
        if (fileName.lastIndexOf("\\") >= 0) {
            realFile = new java.io.File(PATH_FOR_UPLOADING + File.separator + date + " " +
                    fileName.substring(fileName.lastIndexOf("\\")));
        } else {
            realFile = new java.io.File(PATH_FOR_UPLOADING + File.separator + date + " " +
                    fileName.substring(fileName.lastIndexOf("\\") + 1));
        }

        return realFile;
    }
}
