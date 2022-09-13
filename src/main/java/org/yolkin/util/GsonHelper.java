package org.yolkin.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GsonHelper {
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final Gson GSON = new Gson();

    public GsonHelper(HttpServletResponse response, HttpServletRequest request) {
        this.response = response;
        this.request = request;
    }

    public void sendJsonFrom(Object object) throws IOException {
        response.getWriter().write(GSON.toJson(object));
    }

    public void sendBadRequestStatus(String cause) throws IOException {
        response.sendError(400, cause);
    }
}
