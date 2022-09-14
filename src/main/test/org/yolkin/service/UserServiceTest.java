package org.yolkin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.User;
import org.yolkin.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserServiceTest extends Mockito {

    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private StringWriter writer;
    private PrintWriter printWriter;
    private UserService serviceUnderTest;
    private String mappingUrl = "/api/v1/users/";

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.serviceUnderTest = new UserService(userRepository);
    }

    @BeforeEach
    public void setWriter() throws IOException {
        writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    private List<User> getUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Petya");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Vasya");

        return List.of(user1, user2);
    }

    private User getUserWithoutId() {
        User user = new User();
        user.setName("Ivan");
        return user;
    }

    private User getUserWithId() {
        User user = new User();
        user.setId(1L);
        user.setName("Ivan");
        return user;
    }

    @Test
    public void getAllSuccess() {
        when(userRepository.getAll()).thenReturn(getUsers());
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));

        List<User> usersFromService = serviceUnderTest.getAll();

        assertEquals(getUsers(), usersFromService);
    }

    @Test
    public void createSuccess() throws IOException {
        User userWithoutId = getUserWithoutId();
        User userWithId = getUserWithId();

        when(request.getHeader("username")).thenReturn("Ivan");
        when(userRepository.create(userWithoutId)).thenReturn(userWithId);

        User userFromService = serviceUnderTest.create(request, response);

        verify(request).getHeader("username");
        assertEquals(userWithId, userFromService);
    }

    @Test
    public void createFailedBlankUsername() throws IOException {
        when(request.getHeader("username")).thenReturn(" ");

        serviceUnderTest.create(request, response);

        verify(request).getHeader("username");
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Username can't be null");
    }

    @Test
    public void getByIdSuccess() throws IOException {
        when(userRepository.getById(1L)).thenReturn(getUserWithId());

        User userFromService = serviceUnderTest.getById("1", response);

        assertEquals(getUserWithId(), userFromService);
        verify(userRepository, times(1)).getById(1L);
    }

    @Test
    public void getByIdFailedUserNotFound() throws IOException {
        when(userRepository.getById(100L)).thenReturn(null);

        User userFromService = serviceUnderTest.getById("100", response);

        assertNull(userFromService);
        verify(userRepository, times(1)).getById(100L);
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "There is no user with such id");
    }

    @Test
    public void getByIdFailedIncorrectUserId() throws IOException {
        User userFromService = serviceUnderTest.getById("sdfgnf", response);

        assertNull(userFromService);
        verify(userRepository, never()).getById(Mockito.any());
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect user id");
    }

    @Test
    public void updateFailedBlankUserId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/"));
        User userFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertNull(userFromService);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "User id can't be null");
        verify(userRepository, never()).getById(Mockito.any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateFailedBlankUsername() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(request.getHeader("username")).thenReturn(" ");
        User userFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertNull(userFromService);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Username can't be null");
        verify(userRepository, never()).getById(Mockito.any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateFailedUserNotFound() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/100"));
        when(request.getHeader("username")).thenReturn("Eugene");
        User userFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertNull(userFromService);
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "There is no user with such id");
        verify(userRepository, times(1)).getById(100L);
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateFailedIncorrectId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/wfgkjd"));
        User userFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertNull(userFromService);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect user id");
        verify(userRepository, never()).getById(Mockito.any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateSuccess() throws IOException {
        String newUsername = "Eugene";
        User userBeforeUpdate = getUserWithId();
        User userAfterUpdate = getUserWithId();
        userAfterUpdate.setName(newUsername);

        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(request.getHeader("username")).thenReturn(newUsername);
        when(userRepository.getById(1L)).thenReturn(userBeforeUpdate);
        when(userRepository.update(userAfterUpdate)).thenReturn(userAfterUpdate);

        User userFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertEquals(userAfterUpdate, userFromService);
        verify(response, never()).sendError(Mockito.anyInt(), Mockito.anyString());
        verify(userRepository, times(1)).getById(1L);
        verify(userRepository, times(1)).update(userAfterUpdate);
    }
}