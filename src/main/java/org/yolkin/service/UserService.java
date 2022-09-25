package org.yolkin.service;

import org.yolkin.dto.UserDTO;
import org.yolkin.dto.mapper.UserMapper;
import org.yolkin.model.EventEntity;
import org.yolkin.model.UserEntity;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public UserService() {
        userRepository = new HibernateUserRepositoryImpl();
        eventRepository = new HibernateEventRepositoryImpl();
    }

    public UserService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public List<UserEntity> getAll() {
        return userRepository.getAll();
    }

    public UserDTO create(UserEntity userWithoutId) {
        UserDTO userDTO = UserMapper.toUserDto(userWithoutId);

        UserEntity userWithId = userRepository.create(userWithoutId);
        userDTO.setId(userWithId.getId());

        return userDTO;
    }

    public UserDTO getById(Long id) {
        UserEntity userFromRepo = userRepository.getById(id);

        if (userFromRepo == null) {
            return null;
        }

        List<EventEntity> events = eventRepository.getAll().stream()
                .filter((event -> event.getUser().equals(userFromRepo)))
                .collect(Collectors.toList());

        UserDTO userDTO = UserMapper.toUserDto(userFromRepo);
        userDTO.setEvents(events);

        return userDTO;
    }

    public UserDTO update(UserEntity userForUpdate) {
        UserDTO userDTO = UserMapper.toUserDto(userRepository.update(userForUpdate));

        if (userDTO.getName().equals(userForUpdate.getName())) {
            return userDTO;
        }

        return null;
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }
}