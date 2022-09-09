package org.yolkin.servlet;

import org.yolkin.model.User;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UsersServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserRepository userRepository = new HibernateUserRepositoryImpl();
        List<User> users = userRepository.getAll();

        PrintWriter writer = response.getWriter();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE = html>");
        stringBuilder.append("<html>");
            stringBuilder.append("<head><title>");
                stringBuilder.append("<h1>Users list</h1>");
            stringBuilder.append("</title></head>");

            stringBuilder.append("<body>");
            stringBuilder.append("<h1>USERS DATA</h1>");

        for (User user : users) {
            stringBuilder.append("User ID: " + user.getId());
            stringBuilder.append("<br/>");
            stringBuilder.append("User name: " + user.getName());
            stringBuilder.append("<br/>");
            stringBuilder.append("<br/>");
        }

            stringBuilder.append("</body>");
        stringBuilder.append("</html>");

        writer.println(stringBuilder);
    }
}