package org.yolkin.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.File;
import org.yolkin.model.User;
import org.yolkin.service.FileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileRestControllerV1Test extends Mockito {

    @Mock
    private FileService fileService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private StringWriter writer;
    private PrintWriter printWriter;
    private FileRestControllerV1 controllerUnderTest;
    private final String mappingUrl = "/api/v1/files/";

    public FileRestControllerV1Test() {
        MockitoAnnotations.openMocks(this);
        this.controllerUnderTest = new FileRestControllerV1(fileService);
    }

    @BeforeEach
    public void setWriter() throws IOException {
        writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void doGetAll() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/"));
        controllerUnderTest.doGet(request, response);

        verify(fileService, times(1)).getAll();
        verify(fileService, never()).getById(any(), any());
    }

    @Test
    public void doGetUserById() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));

        controllerUnderTest.doGet(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(fileService, times(1)).getById("1", response);
    }

    @Test
    public void doPost() throws IOException {
        controllerUnderTest.doPost(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(fileService, times(1)).create(request, response);
    }

    @Test
    public void doPut() throws IOException {
        controllerUnderTest.doPut(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(fileService, times(1)).update(request, response, mappingUrl);
    }

    @Test
    public void doDelete() throws IOException {
        controllerUnderTest.doDelete(request, response);

        verify(fileService, times(1)).delete(request, response, mappingUrl);
    }
}