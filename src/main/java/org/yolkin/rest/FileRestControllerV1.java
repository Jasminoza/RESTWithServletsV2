package org.yolkin.rest;

import org.yolkin.model.File;
import org.yolkin.service.FileService;
import org.yolkin.util.GsonHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileRestControllerV1 extends HttpServlet {

    private final FileService fileService;
    private final String mappingUrl = "api/v1/files/";

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
            try {
                Long idFromRequest = Long.valueOf(id);
                File file = fileService.getById(idFromRequest);

                if (file != null) {
                    helper.sendJsonFrom(file);
                } else {
                    resp.sendError(404, "There is no file with such id");
                }
            } catch (Exception e) {
                resp.sendError(400, "Incorrect file id");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);

        String username = req.getHeader("user_id");

        if (username == null || username.isBlank()) {
            resp.sendError(400, "Username can't be null");
        } else {
            helper.sendJsonFrom(fileService.create(new File()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}
