package main.java.org.yolkin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.dto.mapper.UserMapper;
import org.yolkin.model.UserEntity;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.UserRepository;
import org.yolkin.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserEntityServiceTest extends Mockito {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    private StringWriter writer;
    private PrintWriter printWriter;
    private UserService serviceUnderTest;
    private String mappingUrl = "/api/v1/users/";

    public UserEntityServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.serviceUnderTest = new UserService(userRepository, eventRepository);
    }

    @BeforeEach
    public void setWriter() throws IOException {
        writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        when(resp.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void getAllUsersSuccess() {
        when(userRepository.getAll()).thenReturn(getUsers());
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));

        List<UserEntity> usersFromService = serviceUnderTest.getAll();

        assertEquals(getUsers(), usersFromService);
        verify(userRepository, times(1)).getAll();
    }

    @Test
    public void createUserSuccess() throws IOException {
        UserEntity userEntityWithoutId = getUserWithoutId();
        UserEntity userEntityWithId = getUserWithId();

        when(req.getHeader("username")).thenReturn("Ivan");
        when(userRepository.create(userEntityWithoutId)).thenReturn(userEntityWithId);

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.create(userEntityWithoutId));

        assertEquals(userEntityWithId, userEntityFromService);
        verify(resp, times(1)).setStatus(SC_CREATED);
        verify(resp, never()).sendError(anyInt(), anyString());
    }

    @Test
    public void createUserFailedBlankUsername() throws IOException {
        when(req.getHeader("username")).thenReturn(" ");

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.create(getUserWithoutId()));

        assertNull(userEntityFromService);
        verify(req, times(1)).getHeader("username");
        verify(resp, times(1)).sendError(SC_BAD_REQUEST, "Header \"username\" can't be null");
        verify(userRepository, never()).create(any());
    }

    @Test
    public void getUserByIdSuccess() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(userRepository.getById(1L)).thenReturn(getUserWithId());

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.getById(1L));

        assertEquals(getUserWithId(), userEntityFromService);
        verify(userRepository, times(1)).getById(1L);
        verify(resp, never()).sendError(anyInt(), anyString());
    }

    @Test
    public void getUserByIdFailedUserNotFound() throws IOException {
        Long userId = 100L;
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/100"));
        when(userRepository.getById(userId)).thenReturn(null);

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.getById(userId));

        assertNull(userEntityFromService);
        verify(userRepository, times(1)).getById(userId);
        verify(resp, times(1)).sendError(SC_NOT_FOUND, "There is no user with such id");
    }

    @Test
    public void getUserByIdFailedIncorrectUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/sdfgnf"));

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.getById(anyLong()));

        assertNull(userEntityFromService);
        verify(userRepository, never()).getById(any());
        verify(resp, times(1)).sendError(SC_BAD_REQUEST, "Incorrect id");
    }

    @Test
    public void updateUserFailedBlankUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/"));

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.update(any()));

        assertNull(userEntityFromService);
        verify(resp).sendError(SC_BAD_REQUEST, "Id can't be null");
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateUserFailedBlankUsername() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(req.getHeader("username")).thenReturn(" ");

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.update(any()));

        assertNull(userEntityFromService);
        verify(resp).sendError(SC_BAD_REQUEST, "Header \"username\" can't be null");
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateUserFailedUserNotFound() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/100"));
        when(userRepository.getById(100L)).thenReturn(null);
        when(req.getHeader("username")).thenReturn("Eugene");

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.update(any()));;

        assertNull(userEntityFromService);
        verify(resp).sendError(SC_NOT_FOUND, "There is no user with such id");
        verify(userRepository, times(1)).getById(100L);
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateFailedIncorrectUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/wfgkjd"));

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.update(any()));;

        assertNull(userEntityFromService);
        verify(resp).sendError(SC_BAD_REQUEST, "Incorrect id");
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void updateUserSuccess() throws IOException {
        String newUsername = "Eugene";
        UserEntity userEntityBeforeUpdate = getUserWithId();
        UserEntity userEntityAfterUpdate = getUserWithId();
        userEntityAfterUpdate.setName(newUsername);

        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(req.getHeader("username")).thenReturn(newUsername);
        when(userRepository.getById(1L)).thenReturn(userEntityBeforeUpdate);
        when(userRepository.update(userEntityAfterUpdate)).thenReturn(userEntityAfterUpdate);

        UserEntity userEntityFromService = UserMapper.toUser(serviceUnderTest.update(userEntityAfterUpdate));

        assertEquals(userEntityAfterUpdate, userEntityFromService);
        verify(resp, never()).sendError(anyInt(), anyString());
        verify(userRepository, times(1)).getById(1L);
        verify(userRepository, times(1)).update(userEntityAfterUpdate);
    }

    @Test
    public void deleteUserFailedBlankUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/"));

        serviceUnderTest.delete(anyLong());

        verify(resp).sendError(SC_BAD_REQUEST, "Id can't be null");
        verify(resp, never()).setStatus(SC_NO_CONTENT);
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void deleteUserFailedUserNotFound() throws IOException {
        Long userId = 100L;
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/100"));
        when(userRepository.getById(userId)).thenReturn(null);

        serviceUnderTest.delete(userId);

        verify(resp, times(1)).sendError(SC_NOT_FOUND, "There is no user with such id");
        verify(resp, never()).setStatus(SC_NO_CONTENT);
        verify(userRepository, times(1)).getById(userId);
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void deleteUserFailedIncorrectUserId() throws IOException {
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/wfgkjd"));

        serviceUnderTest.delete(anyLong());

        verify(resp, times(1)).sendError(SC_BAD_REQUEST, "Incorrect id");
        verify(resp, never()).setStatus(SC_NO_CONTENT);
        verify(userRepository, never()).getById(any());
        verify(userRepository, never()).update(any());
    }

    @Test
    public void deleteUserSuccess() {
        Long userId = 1L;
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/users/1"));
        when(userRepository.getById(userId)).thenReturn(getUserWithId());

        serviceUnderTest.delete(userId);

        verify(userRepository, times(1)).getById(userId);
        verify(userRepository, times(1)).delete(userId);
        verify(resp, times(1)).setStatus(SC_NO_CONTENT);
    }

    private List<UserEntity> getUsers() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1L);
        userEntity1.setName("Petya");
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(2L);
        userEntity2.setName("Vasya");

        return List.of(userEntity1, userEntity2);
    }

    private UserEntity getUserWithoutId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Ivan");
        return userEntity;
    }

    private UserEntity getUserWithId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Ivan");
        return userEntity;
    }
}