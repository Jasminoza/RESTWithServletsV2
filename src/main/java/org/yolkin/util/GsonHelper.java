package org.yolkin.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GsonHelper {

    private final HttpServletResponse response;

    public GsonHelper(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void sendJsonFrom(Object object) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new Gson().toJson(object));
    }
}