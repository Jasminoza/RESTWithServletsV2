//package main.java.org.yolkin.service;
//
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileUploadException;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Answers;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.yolkin.model.FileEntity;
//import org.yolkin.model.UserEntity;
//import org.yolkin.repository.EventRepository;
//import org.yolkin.repository.FileRepository;
//import org.yolkin.repository.UserRepository;
//import org.yolkin.service.FileService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import static javax.servlet.http.HttpServletResponse.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class FileEntityServiceTest extends Mockito {
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private FileRepository fileRepository;
//    @Mock
//    private EventRepository eventRepository;
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//    @Mock
//    private Iterator<FileItem> iterator;
//    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
//    private ServletFileUpload uploader;
//    @Mock
//    private FilterInputStream is;
//    private StringWriter writer;
//    private PrintWriter printWriter;
//    private FileService serviceUnderTest;
//    private String mappingUrl = "/api/v1/files/";
//    static final int MAX_FILE_SIZE = 100 * 1024;
//
//    public FileEntityServiceTest() {
//        MockitoAnnotations.openMocks(this);
//        this.serviceUnderTest = new FileService(fileRepository, userRepository, eventRepository);
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
//    public void getAllFilesSuccess() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/"));
//        when(fileRepository.getAll()).thenReturn(getFiles());
//
//        List<FileEntity> filesFromService = serviceUnderTest.getAll();
//
//        assertEquals(getFiles(), filesFromService);
//        verify(fileRepository, times(1)).getAll();
//        verify(response, never()).sendError(anyInt(), anyString());
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void createFileSuccess() throws IOException, FileUploadException {
//
//        when(request.getHeader("user_id")).thenReturn("1");
//        when(request.getContentType()).thenReturn("multipart/form-data");
//        when(userRepository.getById(1L)).thenReturn(getUsers().get(0));
//        when(fileRepository.create(getFileWithoutId())).thenReturn(getFileWithId());
//
//        when(uploader.parseRequest(request).iterator()).thenReturn(iterator);
//        doReturn(iterator).when(uploader).parseRequest(request).iterator();
//        when(iterator.hasNext()).thenReturn(true, false);
//
//        FileEntity fileEntityFromService = serviceUnderTest.create(request, response);
//
//        assertEquals(getFileWithId(), fileEntityFromService);
//        verify(response, never()).sendError(anyInt(), anyString());
//
//        verify(userRepository, times(1)).getById(1L);
//        verify(fileRepository, times(1)).create(getFileWithoutId());
//        verify(eventRepository, times(1)).create(any());
//        verify(response, times(1)).setStatus(SC_CREATED);
//        verify(eventRepository, times(1)).create(any());
//    }
//
//    @Test
//    public void createFileFailedBlankUserId() throws IOException {
//        when(request.getHeader("user_id")).thenReturn(" ");
//
//        FileEntity fileEntity = serviceUnderTest.create(request, response);
//
//        assertNull(fileEntity);
//        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Header \"user_id\" can't be null");
//        verify(userRepository, never()).getById(any());
//        verify(fileRepository, never()).create(any());
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void createFileFailedIncorrectUserId() throws IOException {
//        when(request.getHeader("user_id")).thenReturn("ghdgsdfg");
//
//        FileEntity fileEntity = serviceUnderTest.create(request, response);
//
//        assertNull(fileEntity);
//        verify(response, times(1)).sendError(SC_BAD_REQUEST, "user_id is incorrect");
//        verify(userRepository, never()).getById(any());
//        verify(fileRepository, never()).create(any());
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void createFileFailedUserNotFound() throws IOException {
//        when(request.getHeader("user_id")).thenReturn("1");
//
//        FileEntity fileEntity = serviceUnderTest.create(request, response);
//
//        assertNull(fileEntity);
//        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no user with such id");
//        verify(userRepository, times(1)).getById(any());
//        verify(fileRepository, never()).create(any());
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void getFileByIdSuccess() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
//
//        FileEntity fileEntityFromService = serviceUnderTest.getById(request, response, mappingUrl);
//
//        assertEquals(getFileWithId(), fileEntityFromService);
//        verify(fileRepository, times(1)).getById(1L);
//        verify(response, never()).sendError(anyInt(), anyString());
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void getFileByIdFailedFileNotFound() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/100"));
//        when(fileRepository.getById(100L)).thenReturn(null);
//
//        FileEntity fileEntityFromService = serviceUnderTest.getById(request, response, mappingUrl);
//
//        assertNull(fileEntityFromService);
//        verify(fileRepository, times(1)).getById(100L);
//        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no file with such id");
//        verify(response, never()).sendError(SC_BAD_REQUEST, "Incorrect file id");
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void getFileByIdFailedIncorrectFileId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/edfgsdf"));
//
//        FileEntity fileEntityFromService = serviceUnderTest.getById(request, response, mappingUrl);
//
//        assertNull(fileEntityFromService);
//        verify(fileRepository, never()).getById(any());
//        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Incorrect id");
//        verify(response, never()).sendError(SC_NOT_FOUND, "There is no file with such id");
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void updateFileFailedBlankUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
//        when(request.getHeader("user_id")).thenReturn(" ");
//
//        FileEntity fileEntityFromService = serviceUnderTest.update(request, response, mappingUrl);
//
//        assertNull(fileEntityFromService);
//        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Header \"user_id\" can't be null");
//        verify(response, never()).setStatus(SC_OK);
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void updateFileFailedUserNotFound() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
//        when(userRepository.getById(1L)).thenReturn(null);
//        when(request.getHeader("user_id")).thenReturn("1");
//
//        FileEntity fileEntityFromService = serviceUnderTest.update(request, response, mappingUrl);
//
//        assertNull(fileEntityFromService);
//        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no user with such id");
//        verify(response, never()).setStatus(SC_OK);
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void updateFileFailedIncorrectUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
//        when(request.getHeader("user_id")).thenReturn("dfgjknsdfk");
//
//        FileEntity fileEntityFromService = serviceUnderTest.update(request, response, mappingUrl);
//
//        assertNull(fileEntityFromService);
//        verify(response, times(1)).sendError(SC_BAD_REQUEST, "user_id is incorrect");
//        verify(response, never()).setStatus(SC_OK);
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void updateFileSuccess() throws IOException {
//        //::TODO
//
//        FileEntity fileEntityFromService = serviceUnderTest.update(request, response, mappingUrl);
//
//        assertNull(fileEntityFromService);
//        verify(eventRepository, times(1)).create(any());
//    }
//
//    @Test
//    public void deleteFileSuccess() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/files/1"));
//        when(request.getHeader("user_id")).thenReturn("1");
//        when(userRepository.getById(1L)).thenReturn(getUsers().get(0));
//        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
//
//        serviceUnderTest.delete(request, response, mappingUrl);
//
//        verify(fileRepository, times(1)).getById(1L);
//        verify(fileRepository, times(1)).delete(1L);
//        verify(response, times(1)).setStatus(SC_NO_CONTENT);
//        verify(eventRepository, times(1)).create(any());
//    }
//
//    @Test
//    public void deleteFileFailedBlankFileId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/files/"));
//
//        serviceUnderTest.delete(request, response, mappingUrl);
//
//        verify(response).sendError(SC_BAD_REQUEST, "Id can't be null");
//        verify(fileRepository, never()).getById(any());
//        verify(fileRepository, never()).delete(any());
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void deleteFileFailedFileNotFound() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(fileRepository.getById(1L)).thenReturn(null);
//        when(userRepository.getById(1L)).thenReturn(getUsers().get(0));
//        when(request.getHeader("user_id")).thenReturn("1");
//
//        serviceUnderTest.delete(request, response, mappingUrl);
//
//        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no file with such id");
//        verify(fileRepository, never()).delete(1L);
//        verify(response, never()).setStatus(SC_NO_CONTENT);
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void deleteFileFailedUserNotFound() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(fileRepository.getById(1L)).thenReturn(getFileWithId());
//        when(userRepository.getById(1L)).thenReturn(null);
//        when(request.getHeader("user_id")).thenReturn("1");
//
//        serviceUnderTest.delete(request, response, mappingUrl);
//
//        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no user with such id");
//        verify(response, never()).setStatus(SC_NO_CONTENT);
//        verify(fileRepository, never()).delete(1L);
//        verify(eventRepository, never()).create(any());
//    }
//
//    @Test
//    public void deleteFileFailedIncorrectUserId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/files/1"));
//        when(request.getHeader("user_id")).thenReturn("dfgjknsdfk");
//
//        serviceUnderTest.delete(request, response, mappingUrl);
//
//        verify(response, times(1)).sendError(SC_BAD_REQUEST, "user_id is incorrect");
//        verify(response, never()).setStatus(SC_OK);
//        verify(fileRepository, never()).delete(1L);
//        verify(eventRepository, never()).create(any());
//    }
//
//    private List<FileEntity> getFiles() {
//        List<FileEntity> fileEntities = new ArrayList<>();
//
//        FileEntity fileEntity1 = new FileEntity();
//        fileEntity1.setId(1L);
//        fileEntity1.setName("1.txt");
//        fileEntity1.setFilepath("/storage/1.txt");
//        fileEntities.add(fileEntity1);
//
//        FileEntity fileEntity2 = new FileEntity();
//        fileEntity1.setId(2L);
//        fileEntity1.setName("2.txt");
//        fileEntity1.setFilepath("/storage/2.txt");
//        fileEntities.add(fileEntity2);
//
//        FileEntity fileEntity3 = new FileEntity();
//        fileEntity1.setId(3L);
//        fileEntity1.setName("3.txt");
//        fileEntity1.setFilepath("/storage/3.txt");
//        fileEntities.add(fileEntity3);
//
//        FileEntity fileEntity4 = new FileEntity();
//        fileEntity1.setId(4L);
//        fileEntity1.setName("4.txt");
//        fileEntity1.setFilepath("/storage/4.txt");
//        fileEntities.add(fileEntity4);
//
//        return fileEntities;
//    }
//
//    private FileEntity getFileWithoutId() {
//        FileEntity fileEntity1 = new FileEntity();
//        fileEntity1.setName("1.txt");
//        fileEntity1.setFilepath("/storage/1.txt");
//        return fileEntity1;
//    }
//
//    private FileEntity getFileWithId() {
//        FileEntity fileEntity1 = new FileEntity();
//        fileEntity1.setId(1L);
//        fileEntity1.setName("1.txt");
//        fileEntity1.setFilepath("/storage/1.txt");
//        return fileEntity1;
//    }
//
//    private List<UserEntity> getUsers() {
//        UserEntity userEntity1 = new UserEntity();
//        userEntity1.setId(1L);
//        userEntity1.setName("Petya");
//        UserEntity userEntity2 = new UserEntity();
//        userEntity2.setId(2L);
//        userEntity2.setName("Vasya");
//
//        return List.of(userEntity1, userEntity2);
//    }
//}
