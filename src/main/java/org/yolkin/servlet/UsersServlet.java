package org.yolkin.servlet;

import org.yolkin.model.User;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;
import org.yolkin.util.ServletHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UsersServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserRepository userRepository = new HibernateUserRepositoryImpl();

        ServletHelper helper = new ServletHelper(response);

        List<User> users = userRepository.getAll();

        helper.setResponseHead("Users list");
        helper.addH1ToResponseBody("USERS DATA");

        for (User user : users) {
            helper.addToResponseBody("User ID: " + user.getId());
            helper.addToResponseBody("User name: " + user.getName());
            helper.addToResponseBody("<br/>");
        }

        helper.sendResponse();
    }
}