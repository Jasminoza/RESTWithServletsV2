package org.yolkin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.Event;
import org.yolkin.model.File;
import org.yolkin.model.User;
import org.yolkin.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest extends Mockito {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private StringWriter writer;
    private PrintWriter printWriter;
    private EventService serviceUnderTest;
    private final String mappingUrl = "/api/v1/events/";

    public EventServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.serviceUnderTest = new EventService(eventRepository);
    }

    @BeforeEach
    public void setWriter() throws IOException {
        writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void getAllSuccess() throws IOException {
        when(eventRepository.getAll()).thenReturn(getEvents());
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/"));

        List<Event> eventsFromService = serviceUnderTest.getAll();

        assertEquals(getEvents(), eventsFromService);
        verify(eventRepository, times(1)).getAll();
        verify(response, never()).sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect event id");
        verify(response, never()).sendError(HttpServletResponse.SC_NOT_FOUND, "There is no event with such id");
    }

    @Test
    public void getByIdSusses() throws IOException {
        when(eventRepository.getById(1L)).thenReturn(getEvents().get(0));
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/1"));

        Event eventFromService = serviceUnderTest.getById("1", response);

        assertEquals(getEvents().get(0), eventFromService);
        verify(eventRepository, times(1)).getById(1L);
        verify(response, never()).sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect event id");
        verify(response, never()).sendError(HttpServletResponse.SC_NOT_FOUND, "There is no event with such id");
    }

    @Test
    public void detByIdFailedEventNotFound() throws IOException {
        when(eventRepository.getById(100L)).thenReturn(null);

        Event eventFromService = serviceUnderTest.getById("100", response);

        assertNull(eventFromService);
        verify(eventRepository, times(1)).getById(100L);
        verify(response, times(1)).sendError(HttpServletResponse.SC_NOT_FOUND, "There is no event with such id");
        verify(response, never()).sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect event id");
    }

    @Test
    public void detByIdFailedIncorrectEventId() throws IOException {
        Event eventFromService = serviceUnderTest.getById("edfgsdf", response);

        assertNull(eventFromService);
        verify(eventRepository,never()).getById(any());
        verify(response, times(1)).sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect event id");
        verify(response, never()).sendError(HttpServletResponse.SC_NOT_FOUND, "There is no event with such id");
    }

    @Test
    public void deleteSuccess() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/1"));
        when(eventRepository.getById(1L)).thenReturn(getEvents().get(0));
        serviceUnderTest.delete(request, response, mappingUrl);

        verify(eventRepository, times(1)).getById(1L);
        verify(eventRepository, times(1)).delete(any());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void deleteFailedBlankEventId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/"));
        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Event id can't be null");
        verify(eventRepository, never()).getById(any());
        verify(eventRepository, never()).delete(any());
    }

    @Test
    public void deleteFailedEventNotFound() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/100"));
        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "There is no event with such id");
        verify(eventRepository, times(1)).getById(100L);
        verify(eventRepository, never()).delete(any());
    }

    @Test
    public void deleteFailedIncorrectEventId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/rgswerg"));
        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect event id");
        verify(eventRepository, never()).getById(any());
        verify(eventRepository, never()).update(any());
    }

    private List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        List<User> users = getUsers();
        List<File> files = getFiles();

        Event event1 = new Event();
        event1.setId(1L);
        event1.setUser(users.get(0));
        event1.setFiles(files.subList(0, 1));
        events.add(event1);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setUser(users.get(1));
        event2.setFiles(files.subList(2, 3));
        events.add(event2);

        return events;
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

}
