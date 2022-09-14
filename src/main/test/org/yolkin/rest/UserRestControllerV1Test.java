package org.yolkin.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.User;
import org.yolkin.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

//    private List<User> getUsers() {
//        return List.of(
//                new User(1L, "Petya"),
//                new User(2L, "Vasya")
//        );
//    }

    @Test
    public void doGetAllSuccess() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));

        controllerUnderTest.doGet(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).getAll();

    }

    @Test
    public void doGetUserWithId1Success() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));

        controllerUnderTest.doGet(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).getById("1", response);
    }

//    @Test
//    public void doPostSuccess() throws IOException {
//        User userWithoutId = new User();
//        userWithoutId.setName("Ivan");
//        User userWithId = new User();
//        userWithId.setId(1L);
//        userWithId.setName("Ivan");
//        String userWithIdAsString = "{\"id\":1,\"name\":\"Ivan\"}";
//
//        when(request.getHeader("username")).thenReturn("Ivan");
//        when(userService.create(userWithoutId)).thenReturn(userWithId);
//
//        controllerUnderTest.doPost(request, response);
//        String result = writer.toString().trim();
//
//        verify(request).getHeader("username");
//        verify(response).setContentType("application/json");
//        assertEquals(userWithIdAsString, result);
//    }

//    @Test
//    public void doPostFailedBlankUsername() throws IOException {
//        User userWithoutId = new User("Ivan");
//        User userWithId = new User(1L, "Ivan");
//        when(request.getHeader("username")).thenReturn(" ");
//        when(userService.create(userWithoutId)).thenReturn(userWithId);
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
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
//
//        User userBeforeUpdate = new User(1L, "Ivan");
//        User userAfterUpdate = new User(1L, "Petya");
//        String userAfterUpdateAsString = "{\"id\":1,\"name\":\"Petya\"}";
//
//        when(request.getHeader("username")).thenReturn("Petya");
//        when(userService.getById(1L)).thenReturn(userBeforeUpdate);
//        when(userService.update(userAfterUpdate)).thenReturn(userAfterUpdate);
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
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));
//
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).sendError(400, "User id can't be null");
//    }
//
//    @Test
//    public void doPutFailedBlankUserName() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
//        when(request.getHeader("username")).thenReturn(" ");
//
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).sendError(400, "Username can't be null");
//    }
//
//    @Test
//    public void doPutFailedUserNotFound() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/20"));
//
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).sendError(404, "There is no user with such id");
//    }
//
//    @Test
//    public void doPutFailedIncorrectUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/erfgwe"));
//
//        controllerUnderTest.doPut(request, response);
//
//        verify(response).sendError(400, "Incorrect user id");
//    }
//
//    @Test
//    public void doDeleteSuccess() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
//        when(userService.getById(1L)).thenReturn(new User(1L, "Eugene"));
//
//        controllerUnderTest.doDelete(request, response);
//
//        verify(userService).delete(1L);
//        verify(response).setStatus(200);
//    }
//
//    @Test
//    public void doDeleteFailedBlankUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));
//
//        controllerUnderTest.doDelete(request, response);
//
//        verify(response).sendError(400, "User id can't be null");
//    }
//
//    @Test
//    public void doDeleteUserNotFound() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/20"));
//
//        controllerUnderTest.doDelete(request, response);
//
//        verify(response).sendError(404, "There is no user with such id");
//    }
//
//    @Test
//    public void doDeleteIncorrectUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/erfgwe"));
//
//        controllerUnderTest.doDelete(request, response);
//
//        verify(response).sendError(400, "Incorrect user id");
//    }
}