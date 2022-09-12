package org.yolkin.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class ServletHelper {

    private StringBuilder stringBuilder = new StringBuilder();
    private HttpServletResponse response;
    private HttpServletRequest request;

    public ServletHelper(HttpServletResponse response, HttpServletRequest request) {
        this.response = response;
        this.request = request;
    }

    public void sendBadRequestStatus(String cause) throws IOException {
        response.setStatus(400);
        stringBuilder.append(cause);
        response.getWriter().println(stringBuilder);
    }

    public void sendResponseWithFile(InputStream streamFromHardDrive, int MAX_FILE_SIZE) throws IOException {
        byte[] buffer = new byte[MAX_FILE_SIZE];

        int numBytesRead;
        while ((numBytesRead = streamFromHardDrive.read(buffer)) > 0) {
            response.getOutputStream().write(buffer, 0, numBytesRead);
        }
    }
}
