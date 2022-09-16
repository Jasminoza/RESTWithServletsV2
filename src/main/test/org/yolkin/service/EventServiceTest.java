package org.yolkin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yolkin.model.Event;
import org.yolkin.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        verify(response, never()).sendError(SC_BAD_REQUEST, "Incorrect event id");
        verify(response, never()).sendError(SC_NOT_FOUND, "There is no event with such id");
    }

    @Test
    public void getByIdSuccess() throws IOException {
        when(eventRepository.getById(1L)).thenReturn(getEvents().get(0));
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/1"));

        Event eventFromService = serviceUnderTest.getById(request, response, mappingUrl);

        assertEquals(getEvents().get(0), eventFromService);
        verify(eventRepository, times(1)).getById(1L);
        verify(response, never()).sendError(SC_BAD_REQUEST, "Incorrect event id");
        verify(response, never()).sendError(SC_NOT_FOUND, "There is no event with such id");
    }

    @Test
    public void getByIdFailedEventNotFound() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/100"));
        when(eventRepository.getById(100L)).thenReturn(null);

        Event eventFromService = serviceUnderTest.getById(request, response, mappingUrl);

        assertNull(eventFromService);
        verify(eventRepository, times(1)).getById(100L);
        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no event with such id");
        verify(response, never()).sendError(SC_BAD_REQUEST, "Incorrect event id");
    }

    @Test
    public void getByIdFailedIncorrectEventId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/edfgsdf"));

        Event eventFromService = serviceUnderTest.getById(request, response, mappingUrl);

        assertNull(eventFromService);
        verify(eventRepository,never()).getById(any());
        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Incorrect id");
        verify(response, never()).sendError(SC_NOT_FOUND, "There is no event with such id");
    }

    @Test
    public void deleteSuccess() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/1"));
        when(eventRepository.getById(1L)).thenReturn(getEvents().get(0));
        serviceUnderTest.delete(request, response, mappingUrl);

        verify(eventRepository, times(1)).getById(1L);
        verify(eventRepository, times(1)).delete(any());
        verify(response, times(1)).setStatus(SC_NO_CONTENT);
    }

    @Test
    public void deleteFailedBlankEventId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/"));
        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response).sendError(SC_BAD_REQUEST, "Id can't be null");
        verify(eventRepository, never()).getById(any());
        verify(eventRepository, never()).delete(any());
    }

    @Test
    public void deleteFailedEventNotFound() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/100"));
        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response).sendError(SC_NOT_FOUND, "There is no event with such id");
        verify(eventRepository, times(1)).getById(100L);
        verify(eventRepository, never()).delete(any());
    }

    @Test
    public void deleteFailedIncorrectEventId() throws IOException {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/rgswerg"));
        serviceUnderTest.delete(request, response, mappingUrl);

        verify(response).sendError(SC_BAD_REQUEST, "Incorrect id");
        verify(eventRepository, never()).getById(any());
        verify(eventRepository, never()).update(any());
    }

    private List<Event> getEvents() {
        List<Event> events = new ArrayList<>();

        Event event1 = new Event();
        event1.setId(1L);
        events.add(event1);

        Event event2 = new Event();
        event2.setId(2L);
        events.add(event2);

        return events;
    }
}
