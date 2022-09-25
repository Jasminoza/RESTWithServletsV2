package org.yolkin.rest;

import org.yolkin.dto.UserCreationDTO;
import org.yolkin.dto.UserDTO;
import org.yolkin.dto.mapper.UserMapper;
import org.yolkin.model.UserEntity;
import org.yolkin.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;
import static org.yolkin.util.HttpUtil.*;

public class UsersRestControllerV1 extends HttpServlet {
    private final UserService userService;
    private final String mappingUrl = "/api/v1/users/";

    public UsersRestControllerV1() {
        this.userService = new UserService();
    }

    public UsersRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            sendJsonFrom(resp, userService.getAll());
        } else {
            if (idFromUrlIsCorrect(id, resp)) {
                UserDTO userDTO = userService.getById(Long.valueOf(id));
                if (userDTO == null) {
                    sendUserNotFoundResponse(resp);
                } else {
                    sendJsonFrom(resp, userDTO);
                }
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (headerIsNotBlank("username", req, resp)) {
            UserCreationDTO userCreationDTO = new UserCreationDTO(req.getHeader("username"));
            UserEntity userWithoutId = UserMapper.toUser(userCreationDTO);
            sendJsonFrom(resp, userService.create(userWithoutId));
            resp.setStatus(SC_CREATED);
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank() || !idFromUrlIsCorrect(id, resp)) {
            return;
        }

        if (headerIsNotBlank("username", req, resp)) {
            UserDTO userFromRepoDTO = userService.getById(Long.valueOf(id));

            if (userFromRepoDTO == null) {
                sendUserNotFoundResponse(resp);
                return;
            }

            userFromRepoDTO.setName(req.getHeader("username"));
            UserEntity userForUpdate = UserMapper.toUser(userFromRepoDTO);

            UserDTO userAfterUpdate = userService.update(userForUpdate);
            if (userAfterUpdate == null) {
                resp.sendError(SC_NOT_IMPLEMENTED, "Can't update User");
                return;
            }
            sendJsonFrom(resp, userAfterUpdate);
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank() || !idFromUrlIsCorrect(id, resp)) {
            return;
        }
        resp.setStatus(SC_NO_CONTENT);
        userService.delete(Long.valueOf(id));
    }

    private static void sendUserNotFoundResponse(HttpServletResponse resp) throws IOException {
        resp.sendError(SC_NOT_FOUND, "There is no User with such id");
    }
}