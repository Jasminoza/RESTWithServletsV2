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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserRepository userRepository = new HibernateUserRepositoryImpl();

        PrintWriter writer = response.getWriter();
        StringBuilder stringBuilder = new StringBuilder();
        Long userId = null;

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