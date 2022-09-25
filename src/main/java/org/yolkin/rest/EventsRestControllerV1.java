package org.yolkin.rest;

import org.yolkin.dto.EventDTO;
import org.yolkin.service.EventService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static org.yolkin.util.HttpUtil.idFromUrlIsCorrect;
import static org.yolkin.util.HttpUtil.sendJsonFrom;

public class EventsRestControllerV1 extends HttpServlet {
    private final EventService eventService;
    private final String mappingUrl = "/api/v1/events/";

    public EventsRestControllerV1() {
        this.eventService = new EventService();
    }

    public EventsRestControllerV1(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            sendJsonFrom(resp, eventService.getAll());
        } else {
            if (idFromUrlIsCorrect(id, resp)) {
                EventDTO eventDTO = eventService.getById(Long.valueOf(id));
                if (eventDTO == null) {
                    resp.sendError(SC_NOT_FOUND, "There is no Event with such id");
                } else {
                    sendJsonFrom(resp, eventDTO);
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank() || !idFromUrlIsCorrect(id, resp)) {
            return;
        }
        resp.setStatus(SC_NO_CONTENT);
        eventService.delete(Long.valueOf(id));
    }
}