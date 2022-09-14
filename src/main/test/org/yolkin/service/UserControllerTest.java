//package org.yolkin.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.yolkin.model.User;
//import org.yolkin.rest.UserRestControllerV1;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class UserControllerTest extends Mockito {
//
//    @Mock
//    private UserService userService;
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//    private StringWriter writer;
//    private PrintWriter printWriter;
//    private UserRestControllerV1 controllerUnderTest;
//
//    public UserControllerTest() {
//        MockitoAnnotations.openMocks(this);
//        this.controllerUnderTest = new UserRestControllerV1(userService);
//    }
//
//    @BeforeEach
//    public void setWriter() throws IOException {
//        writer = new StringWriter();
//        printWriter = new PrintWriter(writer);
//        when(response.getWriter()).thenReturn(printWriter);
//    }
//
//    private List<User> getUsers() {
//        return List.of(
//                new User(1L, "Petya"),
//                new User(2L, "Vasya")
//        );
//    }
//
//    @Test
//    public void doGetAllSuccess() throws IOException {
//        when(userService.getAll()).thenReturn(getUsers());
//        String getUsersAsString = "[{\"id\":1,\"name\":\"Petya\"},{\"id\":2,\"name\":\"Vasya\"}]";
//
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));
//
//        controllerUnderTest.doGet(request, response);
//        String result = writer.toString().trim();
//
//        verify(response).setContentType("application/json;charset=UTF-8");
//        assertEquals(getUsersAsString, result);
//    }
//
//    @Test
//    public void doGetUserWithId1Success() throws IOException {
////        when(userService.getById("1L", response).thenReturn(new User(1L, "Eugene"));
////        String userAsString = "{\"id\":1,\"name\":\"Eugene\"}";
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
//
//        controllerUnderTest.doGet(request, response);
////        String result = writer.toString().trim();
//
//        verify(response).setContentType("application/json;charset=UTF-8");
//        verify(userService, times(1)).getById("1", response);
////        assertEquals(userAsString, result);
//    }
//
//    @Test
//    public void doGetAllFailedIncorrectId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/sdgdf"));
//
//        controllerUnderTest.doGet(request, response);
//        String result = writer.toString().trim();
//
//        verify(response).sendError(400, "Incorrect user id");
//        assertEquals("", result);
//    }
//
////    @Test
////    public void doGetFailedNoUserWithSuchId() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/20"));
////
////        controllerUnderTest.doGet(request, response);
////        String result = writer.toString().trim();
////
////        verify(response).sendError(404, "There is no user with such id");
////        assertEquals("", result);
////    }
////
////    @Test
////    public void doPostSuccess() throws IOException {
////        User userWithoutId = new User("Ivan");
////        User userWithId = new User(1L, "Ivan");
////        String userWithIdAsString = "{\"id\":1,\"name\":\"Ivan\"}";
////
////        when(request.getHeader("username")).thenReturn("Ivan");
////        when(userService.create(userWithoutId)).thenReturn(userWithId);
////
////        controllerUnderTest.doPost(request, response);
////        String result = writer.toString().trim();
////
////        verify(request).getHeader("username");
////        verify(response).setContentType("application/json");
////        assertEquals(userWithIdAsString, result);
////    }
////
////    @Test
////    public void doPostFailedBlankUsername() throws IOException {
////        User userWithoutId = new User("Ivan");
////        User userWithId = new User(1L, "Ivan");
////        when(request.getHeader("username")).thenReturn(" ");
////        when(userService.create(userWithoutId)).thenReturn(userWithId);
////
////        controllerUnderTest.doPost(request, response);
////        String result = writer.toString().trim();
////
////        verify(request).getHeader("username");
////        verify(response).sendError(400, "Username can't be null");
////        assertEquals("", result);
////    }
////
////    @Test
////    public void doPutSuccess() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
////
////        User userBeforeUpdate = new User(1L, "Ivan");
////        User userAfterUpdate = new User(1L, "Petya");
////        String userAfterUpdateAsString = "{\"id\":1,\"name\":\"Petya\"}";
////
////        when(request.getHeader("username")).thenReturn("Petya");
////        when(userService.getById(1L)).thenReturn(userBeforeUpdate);
////        when(userService.update(userAfterUpdate)).thenReturn(userAfterUpdate);
////
////        controllerUnderTest.doPut(request, response);
////        String result = writer.toString().trim();
////
////        verify(request).getHeader("username");
////        assertEquals(userAfterUpdateAsString, result);
////    }
////
////    @Test
////    public void doPutFailedBlankUserId() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));
////
////        controllerUnderTest.doPut(request, response);
////
////        verify(response).sendError(400, "User id can't be null");
////    }
////
////    @Test
////    public void doPutFailedBlankUserName() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
////        when(request.getHeader("username")).thenReturn(" ");
////
////        controllerUnderTest.doPut(request, response);
////
////        verify(response).sendError(400, "Username can't be null");
////    }
////
////    @Test
////    public void doPutFailedUserNotFound() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/20"));
////
////        controllerUnderTest.doPut(request, response);
////
////        verify(response).sendError(404, "There is no user with such id");
////    }
////
////    @Test
////    public void doPutFailedIncorrectUserId() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/erfgwe"));
////
////        controllerUnderTest.doPut(request, response);
////
////        verify(response).sendError(400, "Incorrect user id");
////    }
////
////    @Test
////    public void doDeleteSuccess() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
////        when(userService.getById(1L)).thenReturn(new User(1L, "Eugene"));
////
////        controllerUnderTest.doDelete(request, response);
////
////        verify(userService).delete(1L);
////        verify(response).setStatus(200);
////    }
////
////    @Test
////    public void doDeleteFailedBlankUserId() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));
////
////        controllerUnderTest.doDelete(request, response);
////
////        verify(response).sendError(400, "User id can't be null");
////    }
////
////    @Test
////    public void doDeleteUserNotFound() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/20"));
////
////        controllerUnderTest.doDelete(request, response);
////
////        verify(response).sendError(404, "There is no user with such id");
////    }
////
////    @Test
////    public void doDeleteIncorrectUserId() throws IOException {
////        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/erfgwe"));
////
////        controllerUnderTest.doDelete(request, response);
////
////        verify(response).sendError(400, "Incorrect user id");
////    }
//}