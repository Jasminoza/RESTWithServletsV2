package org.yolkin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.User;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.UserRepository;
import org.yolkin.util.ServiceHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserServiceTest extends Mockito {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    private ServiceHelper helper;
    private StringWriter writer;
    private PrintWriter printWriter;
    private UserService serviceUnderTest;
    private String mappingUrl = "/api/v1/users/";

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.serviceUnderTest = new UserService(userRepository, eventRepository);
    }

    @BeforeEach
    public void setWriter() throws IOException {
        helper = new ServiceHelper(eventRepository, userRepository, req, resp);
        writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        when(resp.getWriter()).thenReturn(printWriter);
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
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));

        List<User> usersFromService = serviceUnderTest.getAll();

        assertEquals(getUsers(), usersFromService);
    }

    @Test
    public void createSuccess() throws IOException {
        User userWithoutId = getUserWithoutId();
        User userWithId = getUserWithId();

        when(req.getHeader("username")).thenReturn("Ivan");
        when(userRepository.create(userWithoutId)).thenReturn(userWithId);

        User userFromService = serviceUnderTest.create(req, resp);

        verify(resp).setStatus(SC_CREATED);
        verify(resp, never()).sendError(anyInt(), anyString());
        assertEquals(userWithId, userFromService);
    }

    @Test
    public void createFailedBlankUsername() throws IOException {
        when(req.getHeader("username")).thenReturn(" ");

        serviceUnderTest.create(req, resp);

        verify(req).getHeader("username");
        verify(resp).sendError(SC_BAD_REQUEST, "username can't be null");
    }

    @Test
    public void getByIdSuccess() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(userRepository.getById(1L)).thenReturn(getUserWithId());

        User userFromService = serviceUnderTest.getById(req, resp, mappingUrl);

        assertEquals(getUserWithId(), userFromService);
        verify(userRepository, times(1)).getById(1L);
    }

    @Test
    public void getByIdFailedUserNotFound() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/100"));
        when(userRepository.getById(100L)).thenReturn(null);

        User userFromService = serviceUnderTest.getById(req, resp, mappingUrl);

        assertNull(userFromService);
        verify(userRepository, times(1)).getById(100L);
        verify(resp).sendError(SC_NOT_FOUND, "There is no user with such id");
    }

    @Test
    public void getByIdFailedIncorrectUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/sdfgnf"));

        User userFromService = serviceUnderTest.getById(req, resp, mappingUrl);

        assertNull(userFromService);
        verify(userRepository, never()).getById(any());
        verify(resp).sendError(SC_BAD_REQUEST, "Incorrect id");
    }

    @Test
    public void updateFailedBlankUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/"));
        User userFromService = serviceUnderTest.update(req, resp, mappingUrl);

        assertNull(userFromService);
        verify(resp).sendError(SC_BAD_REQUEST, "Id can't be null");
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateFailedBlankUsername() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(req.getHeader("username")).thenReturn(" ");
        User userFromService = serviceUnderTest.update(req, resp, mappingUrl);

        assertNull(userFromService);
        verify(resp).sendError(SC_BAD_REQUEST, "username can't be null");
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateFailedUserNotFound() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/100"));
        when(req.getHeader("username")).thenReturn("Eugene");
        User userFromService = serviceUnderTest.update(req, resp, mappingUrl);

        assertNull(userFromService);
        verify(resp).sendError(SC_NOT_FOUND, "There is no user with such id");
        verify(userRepository, times(1)).getById(100L);
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateFailedIncorrectUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/wfgkjd"));
        User userFromService = serviceUnderTest.update(req, resp, mappingUrl);

        assertNull(userFromService);
        verify(resp).sendError(SC_BAD_REQUEST, "Incorrect id");
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateSuccess() throws IOException {
        String newUsername = "Eugene";
        User userBeforeUpdate = getUserWithId();
        User userAfterUpdate = getUserWithId();
        userAfterUpdate.setName(newUsername);

        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(req.getHeader("username")).thenReturn(newUsername);
        when(userRepository.getById(1L)).thenReturn(userBeforeUpdate);
        when(userRepository.update(userAfterUpdate)).thenReturn(userAfterUpdate);

        User userFromService = serviceUnderTest.update(req, resp, mappingUrl);

        assertEquals(userAfterUpdate, userFromService);
        verify(resp, never()).sendError(Mockito.anyInt(), Mockito.anyString());
        verify(userRepository, times(1)).getById(1L);
        verify(userRepository, times(1)).update(userAfterUpdate);
    }

    @Test
    public void deleteFailedBlankUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/"));
        serviceUnderTest.delete(req, resp, mappingUrl);

        verify(resp).sendError(SC_BAD_REQUEST, "Id can't be null");
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void deleteFailedUserNotFound() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/100"));
        serviceUnderTest.delete(req, resp, mappingUrl);

        verify(resp).sendError(SC_NOT_FOUND, "There is no user with such id");
        verify(userRepository, times(1)).getById(100L);
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void deleteFailedIncorrectUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/wfgkjd"));
        serviceUnderTest.delete(req, resp, mappingUrl);

        verify(resp).sendError(SC_BAD_REQUEST, "Incorrect id");
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void deleteSuccess() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(userRepository.getById(1L)).thenReturn(getUserWithId());
        serviceUnderTest.delete(req, resp, mappingUrl);

        verify(userRepository, times(1)).getById(1L);
        verify(userRepository, times(1)).delete(1L);
        verify(resp, times(1)).setStatus(SC_NO_CONTENT);
    }
}