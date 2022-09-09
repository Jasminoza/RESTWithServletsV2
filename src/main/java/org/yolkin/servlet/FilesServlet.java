package org.yolkin.servlet;

import org.yolkin.model.File;
import org.yolkin.model.User;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FilesServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }
}
