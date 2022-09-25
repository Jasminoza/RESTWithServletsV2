package org.yolkin.rest;

import org.yolkin.service.FileService;
import org.yolkin.util.HttpUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilesRestControllerV1 extends HttpServlet {
    private final FileService fileService;
    private final String mappingUrl = "/api/v1/files/";

    public FilesRestControllerV1() {
        fileService = new FileService();
    }

    public FilesRestControllerV1(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        HttpUtil helper = new HttpUtil(resp);
//
//        String url = req.getRequestURL().toString();
//        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());
//
//        if (id.isBlank()) {
//            helper.sendJsonFrom(fileService.getAll());
//        } else {
//            helper.sendJsonFrom(fileService.getById(req, resp, mappingUrl));
//        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        HttpUtil helper = new HttpUtil(resp);
//        helper.sendJsonFrom(fileService.create(req, resp));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        HttpUtil helper = new HttpUtil(resp);
//        helper.sendJsonFrom(fileService.update(req, resp, mappingUrl));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        fileService.delete(req, resp, mappingUrl);
    }
}
