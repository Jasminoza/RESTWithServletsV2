package org.yolkin.rest;

import org.yolkin.service.UserService;
import org.yolkin.util.GsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserRestControllerV1 extends HttpServlet {
    private final UserService userService;

    public UserRestControllerV1() {
        userService = new UserService();
    }

    public UserRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp, req);

        String id = req.getHeader("user_id");

        if (id == null) {
            helper.sendJsonFrom(userService.getAll());
        } else {
            try {
                Long idFromRequest = Long.valueOf(id);
                helper.sendJsonFrom(userService.getById(idFromRequest));
            } catch (Exception e) {
                helper.sendBadRequestStatus("Incorrect user id.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}
