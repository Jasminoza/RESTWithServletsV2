package org.yolkin.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class UserRestControllerV1Test extends Mockito {

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private StringWriter writer;
    private PrintWriter printWriter;
    private UserRestControllerV1 controllerUnderTest;
    private final String mappingUrl = "/api/v1/users/";

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

    @Test
    public void doGetAll() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));

        controllerUnderTest.doGet(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).getAll();
        verify(userService, never()).getById(any(), any(), any());
    }

    @Test
    public void doGetUserById() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));

        controllerUnderTest.doGet(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).getById("1", response, request);
    }

    @Test
    public void doPost() throws IOException {
        controllerUnderTest.doPost(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).create(request, response);
    }

    @Test
    public void doPut() throws IOException {
        controllerUnderTest.doPut(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).update(request, response, mappingUrl);
    }

    @Test
    public void doDelete() throws IOException {
        controllerUnderTest.doDelete(request, response);

        verify(userService, times(1)).delete(request, response, mappingUrl);
    }
}