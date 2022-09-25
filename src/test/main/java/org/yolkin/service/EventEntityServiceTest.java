//package main.java.org.yolkin.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.yolkin.model.EventEntity;
//import org.yolkin.repository.EventRepository;
//import org.yolkin.service.EventService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.ArrayList;
//import java.util.List;
//
//import static javax.servlet.http.HttpServletResponse.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class EventEntityServiceTest extends Mockito {
//    @Mock
//    private EventRepository eventRepository;
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//    private StringWriter writer;
//    private PrintWriter printWriter;
//    private EventService serviceUnderTest;
//    private final String mappingUrl = "/api/v1/events/";
//
//    public EventEntityServiceTest() {
//        MockitoAnnotations.openMocks(this);
//        this.serviceUnderTest = new EventService(eventRepository);
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
//    public void getAllEventsSuccess() throws IOException {
//        when(eventRepository.getAll()).thenReturn(getEvents());
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/"));
//
//        List<EventEntity> eventsFromService = serviceUnderTest.getAll();
//
//        assertEquals(getEvents(), eventsFromService);
//        verify(eventRepository, times(1)).getAll();
//        verify(response, never()).sendError(SC_BAD_REQUEST, "Incorrect event id");
//        verify(response, never()).sendError(SC_NOT_FOUND, "There is no event with such id");
//    }
//
//    @Test
//    public void getEventByIdSuccess() throws IOException {
//        Long eventId = 1L;
//        when(eventRepository.getById(eventId)).thenReturn(getEvents().get(0));
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/1"));
//
//        EventEntity eventEntityFromService = serviceUnderTest.getById(eventId);
//
//        assertEquals(getEvents().get(0), eventEntityFromService);
//        verify(eventRepository, times(1)).getById(eventId);
//        verify(response, never()).sendError(SC_BAD_REQUEST, "Incorrect event id");
//        verify(response, never()).sendError(SC_NOT_FOUND, "There is no event with such id");
//    }
//
//    @Test
//    public void getEventByIdFailedEventNotFound() throws IOException {
//        Long eventId = 100L;
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/100"));
//        when(eventRepository.getById(eventId)).thenReturn(null);
//
//        EventEntity eventEntityFromService = serviceUnderTest.getById(eventId);
//
//        assertNull(eventEntityFromService);
//        verify(eventRepository, times(1)).getById(eventId);
//        verify(response, times(1)).sendError(SC_NOT_FOUND, "There is no event with such id");
//        verify(response, never()).sendError(SC_BAD_REQUEST, "Incorrect event id");
//    }
//
//    @Test
//    public void getEventByIdFailedIncorrectEventId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/edfgsdf"));
//
//        EventEntity eventEntityFromService = serviceUnderTest.getById(anyLong());
//
//        assertNull(eventEntityFromService);
//        verify(eventRepository,never()).getById(any());
//        verify(response, times(1)).sendError(SC_BAD_REQUEST, "Incorrect id");
//        verify(response, never()).sendError(SC_NOT_FOUND, "There is no event with such id");
//    }
//
//    @Test
//    public void deleteEventSuccess() throws IOException {
//        Long eventId = 1L;
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/1"));
//        when(eventRepository.getById(eventId)).thenReturn(getEvents().get(0));
//        serviceUnderTest.delete(eventId);
//
//        verify(eventRepository, times(1)).getById(eventId);
//        verify(eventRepository, times(1)).delete(any());
//        verify(response, times(1)).setStatus(SC_NO_CONTENT);
//    }
//
//    @Test
//    public void deleteEventFailedBlankEventId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/"));
//        serviceUnderTest.delete(anyLong());
//
//        verify(response).sendError(SC_BAD_REQUEST, "Id can't be null");
//        verify(eventRepository, never()).getById(any());
//        verify(eventRepository, never()).delete(any());
//    }
//
//    @Test
//    public void deleteEventFailedEventNotFound() throws IOException {
//        Long eventId = 100L;
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/100"));
//        serviceUnderTest.delete(eventId);
//
//        verify(response).sendError(SC_NOT_FOUND, "There is no event with such id");
//        verify(eventRepository, times(1)).getById(eventId);
//        verify(eventRepository, never()).delete(any());
//    }
//
//    @Test
//    public void deleteEventFailedIncorrectEventId() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/events/rgswerg"));
//        serviceUnderTest.delete(anyLong());
//
//        verify(response).sendError(SC_BAD_REQUEST, "Incorrect id");
//        verify(eventRepository, never()).getById(any());
//        verify(eventRepository, never()).update(any());
//    }
//
//    private List<EventEntity> getEvents() {
//        List<EventEntity> eventEntities = new ArrayList<>();
//
//        EventEntity eventEntity1 = new EventEntity();
//        eventEntity1.setId(1L);
//        eventEntities.add(eventEntity1);
//
//        EventEntity eventEntity2 = new EventEntity();
//        eventEntity2.setId(2L);
//        eventEntities.add(eventEntity2);
//
//        return eventEntities;
//    }
//}
