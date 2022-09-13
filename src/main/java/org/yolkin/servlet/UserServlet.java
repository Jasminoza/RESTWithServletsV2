package org.yolkin.servlet;

import org.yolkin.model.User;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;
import org.yolkin.util.ServletHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    private UserRepository userRepository;

    public void init() {
        userRepository = new HibernateUserRepositoryImpl();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        Long userId;

        try {
            userId = Long.valueOf(request.getHeader("user_id"));
        } catch (Exception e) {
            helper.sendBadRequestStatus("Incorrect user id.");
            return;
        }

        User user = userRepository.getById(userId);

        helper.setResponseHead("User details");

        if (user == null) {
            helper.sendBadRequestStatus("User not found");
            return;
        } else {
            helper.addH1ToResponseBody("User details");
            helper.addToResponseBody("User ID: " + user.getId());
            helper.addToResponseBody("User name: " + user.getName());
            helper.addToResponseBody("<br/>");
        }

        helper.sendResponse();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        String usernameFromRequest = request.getHeader("username");

        if (usernameFromRequest.isBlank()) {
            helper.sendBadRequestStatus("Empty username.");
            return;
        }

        User user = new User();
        user.setName(usernameFromRequest);

        user = userRepository.create(user);

        helper.setResponseHead("User details");
        helper.addH1ToResponseBody("User was created successfully");

        helper.addToResponseBody("User ID: " + user.getId());
        helper.addToResponseBody("User name: " + user.getName());
        helper.addToResponseBody("<br/>");

        helper.sendResponse();
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        String usernameFromRequest = request.getHeader("username");

        Long userIdFromRequest;
        try {
            userIdFromRequest = Long.valueOf(request.getHeader("user_id"));
        } catch (Exception e) {
            helper.sendBadRequestStatus("Incorrect user id.");
            return;
        }

        if (usernameFromRequest.isBlank()) {
            helper.sendBadRequestStatus("Empty username");
            return;
        }

        if (userRepository.getById(userIdFromRequest) == null) {
            helper.sendBadRequestStatus("User not found.");
            return;
        }

        User user = new User();
        user.setId(userIdFromRequest);
        user.setName(usernameFromRequest);

        user = userRepository.update(user);

        helper.setResponseHead("User details");
        helper.addH1ToResponseBody("User was updated successfully");

        helper.addToResponseBody("User ID: " + user.getId());
        helper.addToResponseBody("User name: " + user.getName());
        helper.addToResponseBody("<br/>");

        helper.sendResponse();
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        Long userIdFromRequest;
        try {
            userIdFromRequest = Long.valueOf(request.getHeader("user_id"));
        } catch (Exception e) {
            helper.sendBadRequestStatus("Incorrect user id.");
            return;
        }

        User user = userRepository.getById(userIdFromRequest);

        if (user == null) {
            helper.sendBadRequestStatus("User not found.");
            return;
        }

        userRepository.delete(userIdFromRequest);

        helper.setResponseHead("User details");
        helper.addH1ToResponseBody("User was deleted successfully");

        helper.sendResponse();
    }
}