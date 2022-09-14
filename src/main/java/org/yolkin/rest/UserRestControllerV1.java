package org.yolkin.rest;

import org.yolkin.service.UserService;
import org.yolkin.util.GsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserRestControllerV1 extends HttpServlet {
    private final UserService userService;
    private final String mappingUrl = "api/v1/users/";

    public UserRestControllerV1() {
        userService = new UserService();
    }

    public UserRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            helper.sendJsonFrom(userService.getAll());
        } else {
            helper.sendJsonFrom(userService.getById(id, resp));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);
        helper.sendJsonFrom(userService.create(req, resp));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);
        helper.sendJsonFrom(userService.update(req, resp, mappingUrl));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
       userService.delete(req, resp, mappingUrl);
    }
}