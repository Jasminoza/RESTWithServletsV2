package org.yolkin.service;

import org.yolkin.model.User;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;
import org.yolkin.util.ServiceHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;


public class UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ServiceHelper helper;

    public UserService() {
        userRepository = new HibernateUserRepositoryImpl();
        eventRepository = new HibernateEventRepositoryImpl();
        helper = new ServiceHelper(eventRepository);
    }

    public UserService(UserRepository userRepository, EventRepository eventRepository, ServiceHelper helper) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.helper = helper;
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getHeader("username");

        if (username == null || username.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "Username can't be null");
            return null;
        } else {
            User user = new User();
            user.setName(username);
            user = userRepository.create(user);

            helper.makeCreateUserEvent(user);

            resp.setStatus(SC_CREATED);
            return user;
        }
    }

    public User getById(String id, HttpServletResponse resp) throws IOException {
        User user = null;
        try {
            Long idFromRequest = Long.valueOf(id);
            user = userRepository.getById(idFromRequest);

            if (user == null) {
                resp.sendError(SC_NOT_FOUND, "There is no user with such id");
            }
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect user id");
        }

        return user;
    }

    public User update(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        User user = null;

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "User id can't be null");
            return null;
        } else {
            try {
                Long idFromRequest = Long.valueOf(id);

                String username = req.getHeader("username");

                if (username == null || username.isBlank()) {
                    resp.sendError(SC_BAD_REQUEST, "Username can't be null");
                    return null;
                }
                user = userRepository.getById(idFromRequest);

                if (user == null) {
                    resp.sendError(SC_NOT_FOUND, "There is no user with such id");
                    return null;
                } else {
                    user.setName(username);
                    user = userRepository.update(user);
                }
            } catch (NumberFormatException e) {
                resp.sendError(SC_BAD_REQUEST, "Incorrect user id");
            }
        }
        return user;
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "User id can't be null");
        } else {
            try {
                Long idFromRequest = Long.valueOf(id);
                User user = userRepository.getById(idFromRequest);

                if (user == null) {
                    resp.sendError(SC_NOT_FOUND, "There is no user with such id");
                } else {
                    userRepository.delete(idFromRequest);
                    resp.setStatus(SC_NO_CONTENT);
                }
            } catch (NumberFormatException e) {
                resp.sendError(SC_BAD_REQUEST, "Incorrect user id");
            }
        }
    }
}