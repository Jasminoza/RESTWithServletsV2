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

    private List<File> getFiles() throws ParseException {
        return List.of(
                new File(1L,
                        "1.txt",
                        "file://files/1.txt",
                        new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(20220914110000L))
                ),

                new File(2L,
                        "2.txt",
                        "file://files/2.txt",
                        new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(20220914120000L))
                )
        );
    }

    @Test
    public void doGetAllSuccess() throws IOException, ParseException {
        when(fileService.getAll()).thenReturn(getFiles());
        String getUsersAsString =
                "[" +
                "{\"id\":1,\"name\":\"1.txt\",\"filepath\":\"file://files/1.txt\",\"dateOfUploading\":\"Sep 14, 2022, 11:00:00 AM\"}" +
                "," +
                "{\"id\":2,\"name\":\"2.txt\",\"filepath\":\"file://files/2.txt\",\"dateOfUploading\":\"Sep 14, 2022, 12:00:00 PM\"}" +
                "]";

        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/"));

        controllerUnderTest.doGet(request, response);
        String result = writer.toString().trim();

        verify(response).setContentType("application/json");
        assertEquals(getUsersAsString, result);
    }

    @Test
    public void doGetUserWithId1Success() throws IOException, ParseException {
        when(fileService.getById(1L)).thenReturn(getFiles().get(0));
        String userAsString =
                "{\"id\":1,\"name\":\"1.txt\",\"filepath\":\"file://files/1.txt\",\"dateOfUploading\":\"Sep 14, 2022, 11:00:00 AM\"}";
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));

        controllerUnderTest.doGet(request, response);
        String result = writer.toString().trim();

        verify(response).setContentType("application/json");
        assertEquals(userAsString, result);
    }

    @Test
    public void doGetAllFailedIncorrectId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/sdgdf"));

        controllerUnderTest.doGet(request, response);
        String result = writer.toString().trim();

        verify(response).sendError(400, "Incorrect file id");
        assertEquals("", result);
    }

    @Test
    public void doGetFailedNoUserWithSuchId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/20"));

        controllerUnderTest.doGet(request, response);
        String result = writer.toString().trim();

        verify(response).sendError(404, "There is no file with such id");
        assertEquals("", result);
    }

    @Test
    public void doPostSuccess() throws IOException, ParseException {
        File fileWithoutId = new File(
                "1.txt",
                "file://files/1.txt",
                new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(20220914110000L))
        );

        File fileWithId = new File(
                1L,
                "1.txt",
                "file://files/1.txt",
                new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(20220914110000L))
        );

        String fileWithIdAsString =
                "{\"id\":1,\"name\":\"1.txt\",\"filepath\":\"file://files/1.txt\",\"dateOfUploading\":\"Sep 14, 2022, 11:00:00 AM\"}";

        when(request.getHeader("user_id")).thenReturn("1");
        when(fileService.create(fileWithoutId)).thenReturn(fileWithId);

        controllerUnderTest.doPost(request, response);
        String result = writer.toString().trim();

        verify(request).getHeader("user_id");
        verify(response).setContentType("application/json");
        assertEquals(fileWithIdAsString, result);
    }

//    @Test
//    public void doPostFailedBlankUsername() throws IOException {
//        User userWithoutId = new User("Ivan");
//        User userWithId = new User(1L, "Ivan");
//        when(request.getHeader("username")).thenReturn(" ");
//        when(fileService.create(userWithoutId)).thenReturn(userWithId);
//
//        controllerUnderTest.doPost(request, response);
//        String result = writer.toString().trim();
//
//        verify(request).getHeader("username");
//        verify(response).sendError(400, "Username can't be null");
//        assertEquals("", result);
//    }
//
//    @Test
//    public void doPutSuccess() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//
//        User userBeforeUpdate = new User(1L, "Ivan");
//        User userAfterUpdate = new User(1L, "Petya");
//        String userAfterUpdateAsString = "{\"id\":1,\"name\":\"Petya\"}";
//
//        when(request.getHeader("username")).thenReturn("Petya");
//        when(fileService.getById(1L)).thenReturn(userBeforeUpdate);
//        when(fileService.update(userAfterUpdate)).thenReturn(userAfterUpdate);
//
//        controllerUnderTest.doPut(request, response);
//        String result = writer.toString().trim();
//
//        verify(request).getHeader("username");
//        assertEquals(userAfterUpdateAsString, result);
//    }
//
//    @Test
//    public void doPutFailedBlankUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/"));
//
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).sendError(400, "User id can't be null");
//    }
//
//    @Test
//    public void doPutFailedBlankUserName() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(request.getHeader("username")).thenReturn(" ");
//
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).sendError(400, "Username can't be null");
//    }
//
//    @Test
//    public void doPutFailedUserNotFound() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/20"));
//
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).sendError(404, "There is no user with such id");
//    }
//
//    @Test
//    public void doPutFailedIncorrectUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/erfgwe"));
//
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).sendError(400, "Incorrect user id");
//    }
//
//    @Test
//    public void doDeleteSuccess() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(fileService.getById(1L)).thenReturn(new User(1L, "Eugene"));
//
//        controllerUnderTest.doDelete(request, response);
//
//        verify(fileService).delete(1L);
//        verify(response).setStatus(200);
//    }
//
//    @Test
//    public void doDeleteFailedBlankUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/"));
//
//        controllerUnderTest.doDelete(request, response);
//
//        verify(response).sendError(400, "User id can't be null");
//    }
//
//    @Test
//    public void doDeleteUserNotFound() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/20"));
//
//        controllerUnderTest.doDelete(request, response);
//
//        verify(response).sendError(404, "There is no user with such id");
//    }
//
//    @Test
//    public void doDeleteIncorrectUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/erfgwe"));
//
//        controllerUnderTest.doDelete(request, response);
//
//        verify(response).sendError(400, "Incorrect user id");
//    }
}
