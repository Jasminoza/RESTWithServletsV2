package org.yolkin.rest;

import org.yolkin.service.FileService;
import org.yolkin.util.GsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileRestControllerV1 extends HttpServlet {
    private final FileService fileService;
    private final String mappingUrl = "/api/v1/files/";

    public FileRestControllerV1() {
        fileService = new FileService();
    }

    public FileRestControllerV1(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            helper.sendJsonFrom(fileService.getAll());
        } else {
            helper.sendJsonFrom(fileService.getById(id, req, resp));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);
        helper.sendJsonFrom(fileService.create(req, resp));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);
        helper.sendJsonFrom(fileService.update(req, resp, mappingUrl));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        fileService.delete(req, resp, mappingUrl);
    }
}
