package org.yolkin.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class HttpUtil {

    public static void sendJsonFrom(HttpServletResponse resp, Object object) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(new Gson().toJson(object));
    }

    public static boolean idFromUrlIsCorrect(String id, HttpServletResponse resp) throws IOException {
        try {
            Long.valueOf(id);
            return true;
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "URL contains incorrect id");
            return false;
        }
    }

    public static boolean headerIsNotBlank(String headerName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String headerValue = req.getHeader(headerName);

        if (headerValue == null || headerValue.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, String.format("Header \"%s\" can't be null", headerName));
            return false;
        }

        return true;
    }


}