package org.yolkin.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GsonHelper {
    private final HttpServletResponse response;
    private final Gson GSON = new Gson();

    public GsonHelper(HttpServletResponse response) {
        this.response = response;
    }

    public void sendJsonFrom(Object object) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(GSON.toJson(object));
    }
}