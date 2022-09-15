package org.yolkin.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class ServletHelper {

    private HttpServletResponse response;

    public ServletHelper(HttpServletResponse response) {
        this.response = response;
    }



    public void sendResponseWithFile(InputStream streamFromHardDrive, int MAX_FILE_SIZE) throws IOException {
        byte[] buffer = new byte[MAX_FILE_SIZE];

        int numBytesRead;
        while ((numBytesRead = streamFromHardDrive.read(buffer)) > 0) {
            response.getOutputStream().write(buffer, 0, numBytesRead);
        }
    }


}