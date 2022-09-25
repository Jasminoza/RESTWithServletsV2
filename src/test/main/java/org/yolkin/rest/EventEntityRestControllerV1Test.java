//package main.java.org.yolkin.rest;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.yolkin.rest.EventsRestControllerV1;
//import org.yolkin.service.EventService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//public class EventEntityRestControllerV1Test extends Mockito {
//    @Mock
//    private EventService eventService;
//    @Mock
//    private HttpServletRequest request;
//    @Mock
//    private HttpServletResponse response;
//    private StringWriter writer;
//    private PrintWriter printWriter;
//    private EventsRestControllerV1 controllerUnderTest;
//    private final String mappingUrl = "/api/v1/events/";
//
//    public EventEntityRestControllerV1Test() {
//        MockitoAnnotations.openMocks(this);
//        this.controllerUnderTest = new EventsRestControllerV1(eventService);
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
//    public void doGetAll() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/"));
//
//        controllerUnderTest.doGet(request, response);
//
//        verify(response).setContentType("application/json;charset=UTF-8");
//        verify(eventService, times(1)).getAll();
//        verify(eventService, never()).getById(any(), any(), anyString());
//    }
//
//    @Test
//    public void doGetById() throws IOException {
//        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8088/api/v1/events/1"));
//
//        controllerUnderTest.doGet(request, response);
//
//        verify(response).setContentType("application/json;charset=UTF-8");
//        verify(eventService, never()).getAll();
//        verify(eventService, times(1)).getById(request, response, mappingUrl);
//    }
//
//    @Test
//    public void doDelete() throws IOException {
//        controllerUnderTest.doDelete(request, response);
//
//        verify(eventService, times(1)).delete(request, response, mappingUrl);
//    }
//}
