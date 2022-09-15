package org.yolkin.rest;

import org.yolkin.service.EventService;
import org.yolkin.util.GsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EventRestControllerV1 extends HttpServlet {
    private final EventService eventService;
    private final String mappingUrl = "/api/v1/events/";

    public EventRestControllerV1() {
        this.eventService = new EventService();
    }

    public EventRestControllerV1(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            helper.sendJsonFrom(eventService.getAll());
        } else {
            helper.sendJsonFrom(eventService.getById(id, resp));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        eventService.delete(req, resp, mappingUrl);
    }
}
