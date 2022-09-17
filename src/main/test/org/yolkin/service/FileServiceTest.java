package org.yolkin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.File;
import org.yolkin.model.User;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FileServiceTest extends Mockito {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private StringWriter writer;
    private PrintWriter printWriter;
    private FileService serviceUnderTest;
    private String mappingUrl = "/api/v1/files/";
    static final int MAX_FILE_SIZE = 100 * 1024;

    public FileServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.serviceUnderTest = new FileService(fileRepository, userRepository, eventRepository);
    }

    @BeforeEach
    public void setWriter() throws IOException {
        writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void getAllFilesSuccess() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/"));
        when(fileRepository.getAll()).thenReturn(getFiles());

        List<File> filesFromService = serviceUnderTest.getAll();

        assertEquals(getFiles(), filesFromService);
        verify(fileRepository, times(1)).getAll();
        verify(response, never()).sendError(anyInt(), anyString());
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void createFileSuccess() throws IOException {
        //::TODO

        when(request.getHeader("user_id")).thenReturn("1");
        when(userRepository.getById(1L)).thenReturn(getUsers().get(0));
        when(fileRepository.create(getFileWithoutId())).thenReturn(getFileWithId());

        File fileFromService = serviceUnderTest.create(request, response);

        assertEquals(getFileWithId(), fileFromService);
        verify(response, never()).sendError(anyInt(), anyString());

        verify(userRepository, times(1)).getById(1L);
        verify(fileRepository, times(1)).create(getFileWithoutId());
        verify(eventRepository, times(1)).create(any());
        verify(response, times(1)).setStatus(SC_CREATED);
        verify(eventRepository, times(1)).create(any());
    }

    @Test
    public void createFileFailedBlankUserId() throws IOException {
        when(request.getHeader("user_id")).thenReturn(" ");

        File file = serviceUnderTest.create(request, response);

        assertNull(file);
        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Header \"user_id\" can't be null");
        verify(userRepository, never()).getById(any());
        verify(fileRepository, never()).create(any());
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void createFileFailedIncorrectUserId() throws IOException {
        when(request.getHeader("user_id")).thenReturn("ghdgsdfg");

        File file = serviceUnderTest.create(request, response);

        assertNull(file);
        verify(response, times(1)).sendError(SC_BAD_REQUEST, "user_id is incorrect");
        verify(userRepository, never()).getById(any());
        verify(fileRepository, never()).create(any());
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void createFileFailedUserNotFound() throws IOException {
        when(request.getHeader("user_id")).thenReturn("1");

        File file = serviceUnderTest.create(request, response);

        assertNull(file);
        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no user with such id");
        verify(userRepository, times(1)).getById(any());
        verify(fileRepository, never()).create(any());
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void getFileByIdSuccess() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
        when(fileRepository.getById(1L)).thenReturn(getFileWithId());

        File fileFromService = serviceUnderTest.getById(request, response, mappingUrl);

        assertEquals(getFileWithId(), fileFromService);
        verify(fileRepository, times(1)).getById(1L);
        verify(response, never()).sendError(anyInt(), anyString());
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void getFileByIdFailedFileNotFound() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/100"));
        when(fileRepository.getById(100L)).thenReturn(null);

        File fileFromService = serviceUnderTest.getById(request, response, mappingUrl);

        assertNull(fileFromService);
        verify(fileRepository, times(1)).getById(100L);
        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no file with such id");
        verify(response, never()).sendError(SC_BAD_REQUEST, "Incorrect file id");
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void getFileByIdFailedIncorrectFileId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/edfgsdf"));

        File fileFromService = serviceUnderTest.getById(request, response, mappingUrl);

        assertNull(fileFromService);
        verify(fileRepository, never()).getById(any());
        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Incorrect id");
        verify(response, never()).sendError(SC_NOT_FOUND, "There is no file with such id");
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void updateFileFailedBlankUserId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
        when(request.getHeader("user_id")).thenReturn(" ");

        File fileFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertNull(fileFromService);
        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Header \"user_id\" can't be null");
        verify(response, never()).setStatus(SC_OK);
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void updateFileFailedUserNotFound() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
        when(userRepository.getById(1L)).thenReturn(null);
        when(request.getHeader("user_id")).thenReturn("1");

        File fileFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertNull(fileFromService);
        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no user with such id");
        verify(response, never()).setStatus(SC_OK);
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void updateFileFailedIncorrectUserId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
        when(request.getHeader("user_id")).thenReturn("dfgjknsdfk");

        File fileFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertNull(fileFromService);
        verify(response, times(1)).sendError(SC_BAD_REQUEST, "user_id is incorrect");
        verify(response, never()).setStatus(SC_OK);
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void updateFileSuccess() throws IOException {
        //::TODO

        File fileFromService = serviceUnderTest.update(request, response, mappingUrl);

        assertNull(fileFromService);
        verify(eventRepository, times(1)).create(any());
    }

    @Test
    public void deleteFileSuccess() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/files/1"));
        when(request.getHeader("user_id")).thenReturn("1");
        when(userRepository.getById(1L)).thenReturn(getUsers().get(0));
        when(fileRepository.getById(1L)).thenReturn(getFileWithId());

        serviceUnderTest.delete(request, response, mappingUrl);

        verify(fileRepository, times(1)).getById(1L);
        verify(fileRepository, times(1)).delete(1L);
        verify(response, times(1)).setStatus(SC_NO_CONTENT);
        verify(eventRepository, times(1)).create(any());
    }

    @Test
    public void deleteFileFailedBlankFileId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/files/"));

        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response).sendError(SC_BAD_REQUEST, "Id can't be null");
        verify(fileRepository, never()).getById(any());
        verify(fileRepository, never()).delete(any());
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void deleteFileFailedFileNotFound() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
        when(fileRepository.getById(1L)).thenReturn(null);
        when(userRepository.getById(1L)).thenReturn(getUsers().get(0));
        when(request.getHeader("user_id")).thenReturn("1");

        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no file with such id");
        verify(fileRepository, never()).delete(1L);
        verify(response, never()).setStatus(SC_NO_CONTENT);
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void deleteFileFailedUserNotFound() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
        when(userRepository.getById(1L)).thenReturn(null);
        when(request.getHeader("user_id")).thenReturn("1");

        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no user with such id");
        verify(response, never()).setStatus(SC_NO_CONTENT);
        verify(fileRepository, never()).delete(1L);
        verify(eventRepository, never()).create(any());
    }

    @Test
    public void deleteFileFailedIncorrectUserId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
        when(request.getHeader("user_id")).thenReturn("dfgjknsdfk");

        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, "user_id is incorrect");
        verify(response, never()).setStatus(SC_OK);
        verify(fileRepository, never()).delete(1L);
        verify(eventRepository, never()).create(any());
    }

    private List<File> getFiles() {
        List<File> files = new ArrayList<>();

        File file1 = new File();
        file1.setId(1L);
        file1.setName("1.txt");
        file1.setFilepath("/storage/1.txt");
        file1.setDateOfUploading(new Date(202201010213011L));
        files.add(file1);

        File file2 = new File();
        file1.setId(2L);
        file1.setName("2.txt");
        file1.setFilepath("/storage/2.txt");
        file1.setDateOfUploading(new Date(20220101213012L));
        files.add(file2);

        File file3 = new File();
        file1.setId(3L);
        file1.setName("3.txt");
        file1.setFilepath("/storage/3.txt");
        file1.setDateOfUploading(new Date(20220101213013L));
        files.add(file3);

        File file4 = new File();
        file1.setId(4L);
        file1.setName("4.txt");
        file1.setFilepath("/storage/4.txt");
        file1.setDateOfUploading(new Date(20220101213014L));
        files.add(file4);

        return files;
    }

    private File getFileWithoutId() {
        File file1 = new File();
        file1.setName("1.txt");
        file1.setFilepath("/storage/1.txt");
        file1.setDateOfUploading(new Date(202201010213011L));
        return file1;
    }

    private File getFileWithId() {
        File file1 = new File();
        file1.setId(1L);
        file1.setName("1.txt");
        file1.setFilepath("/storage/1.txt");
        file1.setDateOfUploading(new Date(202201010213011L));
        return file1;
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
}
