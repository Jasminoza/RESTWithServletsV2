//package main.java.org.yolkin.rest;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.yolkin.rest.FilesRestControllerV1;
//import org.yolkin.service.FileService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class FileEntityRestControllerV1Test extends Mockito {
//
//    @Mock
//    private FileService fileService;
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//    private StringWriter writer;
//    private PrintWriter printWriter;
//    private FilesRestControllerV1 controllerUnderTest;
//    private final String mappingUrl = "/api/v1/files/";
//
//    public FileEntityRestControllerV1Test() {
//        MockitoAnnotations.openMocks(this);
//        this.controllerUnderTest = new FilesRestControllerV1(fileService);
//    }
//
//    @BeforeEach
//    public void setWriter() throws IOException {
//        writer = new StringWriter();
//        printWriter = new PrintWriter(writer);
//        when(response.getWriter()).thenReturn(printWriter);
//    }
//
//    @Test
//    public void doGetAll() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/"));
//        controllerUnderTest.doGet(request, response);
//
//        verify(fileService, times(1)).getAll();
//        verify(fileService, never()).getById(any(), any(), anyString());
//    }
//
//    @Test
//    public void doGetUserById() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//
//        controllerUnderTest.doGet(request, response);
//
//        verify(response).setContentType("application/json;charset=UTF-8");
//        verify(fileService, times(1)).getById(request, response, mappingUrl);
//    }
//
//    @Test
//    public void doPost() throws IOException {
//        controllerUnderTest.doPost(request, response);
//
//        verify(response).setContentType("application/json;charset=UTF-8");
//        verify(fileService, times(1)).create(request, response);
//    }
//
//    @Test
//    public void doPut() throws IOException {
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).setContentType("application/json;charset=UTF-8");
//        verify(fileService, times(1)).update(request, response, mappingUrl);
//    }
//
//    @Test
//    public void doDelete() throws IOException {
//        controllerUnderTest.doDelete(request, response);
//
//        verify(fileService, times(1)).delete(request, response, mappingUrl);
//    }
//}