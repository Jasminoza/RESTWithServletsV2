package org.yolkin.servlet;

import org.yolkin.model.User;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserServlet extends HttpServlet {

    private  UserRepository userRepository;

    public void init() {
        userRepository = new HibernateUserRepositoryImpl();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            StringBuilder stringBuilder = new StringBuilder();
            Long userId;

            try {
                userId = Long.valueOf(request.getHeader("user_id"));
            } catch (Exception e) {
                stringBuilder.append("Incorrect user id.");
                writer.println(stringBuilder);
                return;
            }

            User user = userRepository.getById(userId);

            stringBuilder.append("<!DOCTYPE = html>");
            stringBuilder.append("<html>");
            stringBuilder.append("<head><title>");
            stringBuilder.append("<h1>User details</h1>");
            stringBuilder.append("</title></head>");

            stringBuilder.append("<body>");
            stringBuilder.append("<h1>User details</h1>");

            if (user == null) {
                stringBuilder.append("User not found");
            } else {
                stringBuilder.append("User ID: " + user.getId());
                stringBuilder.append("<br/>");
                stringBuilder.append("User name: " + user.getName());
                stringBuilder.append("<br/>");
                stringBuilder.append("<br/>");

                stringBuilder.append("</body>");
                stringBuilder.append("</html>");
            }

            writer.println(stringBuilder);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            StringBuilder stringBuilder = new StringBuilder();

            String usernameFromRequest = request.getHeader("username");

            if (usernameFromRequest.isBlank()) {
                response.setStatus(400);
                stringBuilder.append("Empty username.");
                writer.println(stringBuilder);
                return;
            }

            User user = new User();
            user.setName(usernameFromRequest);

            user = userRepository.create(user);

            stringBuilder.append("<!DOCTYPE = html>");
            stringBuilder.append("<html>");
            stringBuilder.append("<head><title>");
            stringBuilder.append("<h1>User details</h1>");
            stringBuilder.append("</title></head>");

            stringBuilder.append("<body>");
            stringBuilder.append("<h1>User was created successfully</h1>");

            stringBuilder.append("User ID: " + user.getId());
            stringBuilder.append("<br/>");
            stringBuilder.append("User name: " + user.getName());
            stringBuilder.append("<br/>");
            stringBuilder.append("<br/>");

            stringBuilder.append("</body>");
            stringBuilder.append("</html>");

            writer.println(stringBuilder);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            StringBuilder stringBuilder = new StringBuilder();

            String usernameFromRequest = request.getHeader("username");
            Long userIdFromRequest;
            try {
                userIdFromRequest = Long.valueOf(request.getHeader("user_id"));
            } catch (Exception e) {
                stringBuilder.append("Incorrect user id.");
                writer.println(stringBuilder);
                return;
            }

            if (usernameFromRequest.isBlank() || userIdFromRequest == 0L) {
                response.setStatus(400);
                stringBuilder.append("Empty username or user id.");
                writer.println(stringBuilder);
                return;
            }

            if (userRepository.getById(userIdFromRequest) == null) {
                response.setStatus(400);
                stringBuilder.append("User not found.");
                writer.println(stringBuilder);
                return;
            }

            User user = new User();
            user.setId(userIdFromRequest);
            user.setName(usernameFromRequest);

            user = userRepository.update(user);

            stringBuilder.append("<!DOCTYPE = html>");
            stringBuilder.append("<html>");
            stringBuilder.append("<head><title>");
            stringBuilder.append("<h1>User details</h1>");
            stringBuilder.append("</title></head>");

            stringBuilder.append("<body>");
            stringBuilder.append("<h1>User was updated successfully</h1>");

            stringBuilder.append("User ID: " + user.getId());
            stringBuilder.append("<br/>");
            stringBuilder.append("User name: " + user.getName());
            stringBuilder.append("<br/>");
            stringBuilder.append("<br/>");

            stringBuilder.append("</body>");
            stringBuilder.append("</html>");

            writer.println(stringBuilder);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {

    }
}