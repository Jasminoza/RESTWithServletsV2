package org.yolkin.rest;

import org.yolkin.model.User;
import org.yolkin.service.UserService;
import org.yolkin.util.GsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserRestControllerV1 extends HttpServlet {
    private final UserService userService;
    private final String mappingUrl = "/users/";

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
            try {
                Long idFromRequest = Long.valueOf(id);
                User user = userService.getById(idFromRequest);

                if (user != null) {
                    helper.sendJsonFrom(user);
                } else {
                    resp.sendError(404, "There is no user with such id");
                }
            } catch (NumberFormatException e) {
                resp.sendError(400, "Incorrect user id");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);

        String username = req.getHeader("username");

        if (username == null || username.isBlank()) {
            resp.sendError(400, "Username can't be null");
        } else {
            User user = new User();
            user.setName(username);
            user = userService.create(user);
            helper.sendJsonFrom(user);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            resp.sendError(400, "User id can't be null");
        } else {
            try {
                Long idFromRequest = Long.valueOf(id);

                String username = req.getHeader("username");

                if (username == null || username.isBlank()) {
                    resp.sendError(400, "Username can't be null");
                }

                User user = userService.getById(idFromRequest);

                if (user != null) {
                    User updatedUser = new User();
                    updatedUser.setId(idFromRequest);
                    updatedUser.setName(username);
                    updatedUser = userService.update(updatedUser);
                    helper.sendJsonFrom(updatedUser);
                } else {
                    resp.sendError(404, "There is no user with such id");
                }
            } catch (NumberFormatException e) {
                resp.sendError(400, "Incorrect user id");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}