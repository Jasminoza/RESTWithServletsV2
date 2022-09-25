package main.java.org.yolkin.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.dto.mapper.UserMapper;
import org.yolkin.model.UserEntity;
import org.yolkin.rest.UsersRestControllerV1;
import org.yolkin.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;

public class UserEntityRestControllerV1Test extends Mockito {

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private StringWriter writer;
    private PrintWriter printWriter;
    private UsersRestControllerV1 controllerUnderTest;
    private final String mappingUrl = "/api/v1/users/";

    public UserEntityRestControllerV1Test() {
        MockitoAnnotations.openMocks(this);
        this.controllerUnderTest = new UsersRestControllerV1(userService);
    }

    @BeforeEach
    public void setWriter() throws IOException {
        writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void doGetAllSuccess() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/"));

        controllerUnderTest.doGet(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response, never()).sendError(anyInt(), anyString());
        verify(userService, times(1)).getAll();
        verify(userService, never()).getById(anyLong());
    }

    @Test
    public void doGetUserByIdSuccess() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
        when(userService.getById(1L)).thenReturn(UserMapper.toUserDto(getUserWithId()));

        controllerUnderTest.doGet(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).getById(1L);
    }

    @Test
    public void doPostSuccess() throws IOException {
        when(request.getHeader("username")).thenReturn("Ivan");
        when(userService.create(getUserWithoutId())).thenReturn(UserMapper.toUserDto(getUserWithId()));

        controllerUnderTest.doPost(request, response);

        verify(response, times(1)).setStatus(SC_CREATED);
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).create(any());
    }

    @Test
    public void doPostFailedBlankUsername() throws IOException {
        when(request.getHeader("username")).thenReturn(" ");
        when(userService.create(getUserWithoutId())).thenReturn(UserMapper.toUserDto(getUserWithId()));

        controllerUnderTest.doPost(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Header \"username\" can't be null");
        verify(response, never()).setContentType("application/json;charset=UTF-8");
        verify(userService, never()).create(any());
    }

    @Test
    public void doPut() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));
        when(request.getHeader("username")).thenReturn("Ivan");
        when(userService.getById(1L)).thenReturn(UserMapper.toUserDto(getUserWithId()));
        when(userService.update(any())).thenReturn(UserMapper.toUserDto(getUserWithId()));

        controllerUnderTest.doPut(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        verify(userService, times(1)).update(any());
    }

    @Test
    public void doDelete() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/users/1"));

        controllerUnderTest.doDelete(request, response);

        verify(userService, times(1)).delete(1L);
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