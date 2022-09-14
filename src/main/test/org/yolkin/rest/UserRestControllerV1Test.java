package org.yolkin.rest;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.User;
import org.yolkin.service.UserService;
import org.yolkin.util.GsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRestControllerV1Test {

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private UserRestControllerV1 controllerUnderTest;

    public UserRestControllerV1Test() {
        MockitoAnnotations.openMocks(this);
        this.controllerUnderTest = new UserRestControllerV1(userService);
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Petya"));
        users.add(new User(2L, "Vasya"));
        return users;
    }

    @Test
    public void doGetAllSuccess() throws IOException {
        when(userService.getAll()).thenReturn(getUsers());
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/users/"));

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);

        controllerUnderTest.doGet(request, response);

        String result = writer.toString().trim();
        verify(response).setContentType("application/json");
        assertEquals("[{\"id\":1,\"name\":\"Petya\"},{\"id\":2,\"name\":\"Vasya\"}]", result);
    }

    @Test
    public void doGetAllFailedIncorrectId() throws IOException {
        when(userService.getAll()).thenReturn(getUsers());
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/users/sdgdf"));

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);

        controllerUnderTest.doGet(request, response);

        String result = writer.toString().trim();
        verify(response).sendError(400, "Incorrect user id");
        assertEquals("", result);
    }

    @Test
    public void doGetAllFailedNoUserWithSuchId() throws IOException {
        when(userService.getAll()).thenReturn(getUsers());
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/users/20"));

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);

        controllerUnderTest.doGet(request, response);

        String result = writer.toString().trim();
        verify(response).sendError(404, "There is no user with such id");
        assertEquals("", result);
    }
}