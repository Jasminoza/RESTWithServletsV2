package org.yolkin.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.User;
import org.yolkin.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private StringWriter writer;
    private PrintWriter printWriter;
    private UserRestControllerV1 controllerUnderTest;

    public UserRestControllerV1Test() {
        MockitoAnnotations.openMocks(this);
        this.controllerUnderTest = new UserRestControllerV1(userService);
    }

    @BeforeEach
    public void setWriter() throws IOException {
        writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    private List<User> getUsers() {
        return List.of(
                new User(1L, "Petya"),
                new User(2L, "Vasya")
        );
    }

    @Test
    public void doGetAllSuccess() throws IOException {
        when(userService.getAll()).thenReturn(getUsers());
        String getUsersAsString = "[{\"id\":1,\"name\":\"Petya\"},{\"id\":2,\"name\":\"Vasya\"}]";

        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/users/"));

        controllerUnderTest.doGet(request, response);
        String result = writer.toString().trim();

        verify(response).setContentType("application/json");
        assertEquals(getUsersAsString, result);
    }

    @Test
    public void doGetUserWithId1Success() throws IOException {
        when(userService.getById(1L)).thenReturn(new User(1L, "Eugene"));
        String userAsString = "{\"id\":1,\"name\":\"Eugene\"}";
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/users/1"));

        controllerUnderTest.doGet(request, response);
        String result = writer.toString().trim();

        verify(response).setContentType("application/json");
        assertEquals(userAsString, result);
    }

    @Test
    public void doGetAllFailedIncorrectId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/users/sdgdf"));

        controllerUnderTest.doGet(request, response);
        String result = writer.toString().trim();

        verify(response).sendError(400, "Incorrect user id");
        assertEquals("", result);
    }

    @Test
    public void doGetFailedNoUserWithSuchId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/users/20"));

        controllerUnderTest.doGet(request, response);
        String result = writer.toString().trim();

        verify(response).sendError(404, "There is no user with such id");
        assertEquals("", result);
    }
}