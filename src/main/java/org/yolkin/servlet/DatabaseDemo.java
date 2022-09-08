package org.yolkin.servlet;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class DatabaseDemo extends HttpServlet {
    static final Config config = ConfigFactory.load();
    static final String JDBC_DRIVER = config.getString("db.driver");
    static final String DATABASE_URL = config.getString("db.url");
    static final String DATABASE_USER = config.getString("db.user");
    static final String DATABASE_PASSWORD = config.getString("db.password");
    static final String GET_ALL_DEVELOPERS_RECORDS = "SELECT * FROM users";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        String title = "Database Demo";
        String docType = "<!DOCTYPE html>";

        try {
            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(GET_ALL_DEVELOPERS_RECORDS);

            writer.println(docType + "<html><head><title>" + title + "</title></head><body>");
            writer.println("<h1>DEVELOPERS DATA</h1>");
            writer.println("<br/>");
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String specialty = resultSet.getString(4);
                int experience = resultSet.getInt(5);
                int salary = resultSet.getInt(6);


                writer.println("ID: " + id);
                writer.println("First name: " + firstName + "<br/>");
                writer.println("Last name: " + lastName + "<br/>");
                writer.println("Specialty: " + specialty + "<br/>");
                writer.println("Experience: " + experience + " years<br/>");
                writer.println("Salary: $" + salary + "<br/>");
                writer.println("<br/>");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        writer.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
